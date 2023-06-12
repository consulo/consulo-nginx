/*
 * Copyright 2009 Max Ishchenko
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.ishchenko.idea.nginx.run;

import consulo.execution.ui.console.ConsoleView;
import consulo.execution.ui.console.ConsoleViewContentType;
import consulo.logging.Logger;
import consulo.process.*;
import consulo.process.cmd.GeneralCommandLine;
import consulo.process.event.ProcessEvent;
import consulo.process.event.ProcessListener;
import consulo.util.dataholder.Key;
import consulo.virtualFileSystem.LocalFileSystem;
import consulo.virtualFileSystem.VirtualFile;
import net.ishchenko.idea.nginx.NginxBundle;
import net.ishchenko.idea.nginx.configurator.NginxServerDescriptor;
import net.ishchenko.idea.nginx.configurator.NginxServersConfiguration;
import net.ishchenko.idea.nginx.platform.PlatformDependentTools;

import javax.annotation.Nullable;
import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 24.07.2009
 * Time: 2:42:59
 */
public class NginxProcessHandler extends BaseProcessHandler {

    public static final Logger LOG = Logger.getInstance(NginxProcessHandler.class);

    private final ProcessHandler originalProcessHandler;
    private final NginxServerDescriptor descriptorCopy;
    private ConsoleView console;

    private NginxProcessHandler(GeneralCommandLine commandLine, NginxServerDescriptor descriptorCopy) throws ExecutionException {
        originalProcessHandler = ProcessHandlerBuilder.create(commandLine).build();
        this.descriptorCopy = descriptorCopy;
    }

    public static NginxProcessHandler create(NginxRunConfiguration config) throws ExecutionException {

        String descriptorId = config.getServerDescriptorId();

        NginxServerDescriptor descriptor = NginxServersConfiguration.getInstance().getDescriptorById(descriptorId);
        if (descriptor == null) {
            throw new ExecutionException(NginxBundle.message("run.error.servernotfound"));
        }

        NginxServerDescriptor descriptorCopy = descriptor.clone();

        VirtualFile executableVirtualFile = LocalFileSystem.getInstance().findFileByPath(descriptorCopy.getExecutablePath());
        if (executableVirtualFile == null || executableVirtualFile.isDirectory()) {
            throw new ExecutionException(NginxBundle.message("run.error.badpath", descriptorCopy.getExecutablePath()));
        }

        PlatformDependentTools pdt = PlatformDependentTools.getInstance();

        GeneralCommandLine cmd = new GeneralCommandLine(pdt.getStartCommand(descriptorCopy));
        cmd.withWorkDirectory(new File(executableVirtualFile.getParent().getPath()));
        return new NginxProcessHandler(cmd, descriptorCopy.clone());
    }

    @Override
    public void startNotify() {
        originalProcessHandler.startNotify();
        super.startNotify();
    }

    @Override
    public void addProcessListener(ProcessListener listener) {
        originalProcessHandler.addProcessListener(listener);
    }

    @Nullable
    @Override
    public Integer getExitCode() {
        return originalProcessHandler.getExitCode();
    }

    @Override
    protected void destroyProcessImpl() {
        if (tryToStop()) {
            originalProcessHandler.destroyProcess();
        }
        else {
            console.print("Could not stop process.\n", ConsoleViewContentType.ERROR_OUTPUT);
        }
    }

    @Override
    protected void detachProcessImpl() {

    }

    @Override
    public boolean detachIsDefault() {
        return false;
    }

    @Nullable
    @Override
    public OutputStream getProcessInput() {
        return null;
    }

    private boolean tryToStop() {
        PlatformDependentTools pdt = PlatformDependentTools.getInstance();
        VirtualFile executableVirtualFile = LocalFileSystem.getInstance().findFileByPath(descriptorCopy.getExecutablePath());
        String[] stopCommand = pdt.getStopCommand(descriptorCopy);
        boolean successfullyStopped = false;
        try {
            List<String> args = new ArrayList<>();
            Collections.addAll(args, stopCommand);

            GeneralCommandLine commandLine = new GeneralCommandLine(args);
            commandLine.setWorkDirectory(executableVirtualFile.getParent().getPath());

            ProcessHandler osph = ProcessHandlerBuilder.create(commandLine).build();
            osph.addProcessListener(new ProcessListener() {
                @Override
                public void onTextAvailable(final ProcessEvent event, Key outputType) {
                    ConsoleViewContentType contentType = ConsoleViewContentType.SYSTEM_OUTPUT;
                    if (outputType == ProcessOutputTypes.STDERR) {
                        contentType = ConsoleViewContentType.ERROR_OUTPUT;
                    }
                    console.print(event.getText(), contentType);
                }
            });
            osph.startNotify();
            osph.waitFor();
            osph.destroyProcess(); //is that needed if waitFor has returned?
            successfullyStopped = Objects.equals(osph.getExitCode(), 0);

        }
        catch (Exception e) {
            LOG.error(e);
        }

        return successfullyStopped;

    }

    public void setConsole(ConsoleView console) {
        this.console = console;
    }

    public NginxServerDescriptor getDescriptor() {
        return descriptorCopy;
    }
}
