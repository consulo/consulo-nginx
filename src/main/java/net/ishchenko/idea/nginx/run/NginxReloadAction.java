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

import consulo.annotation.component.ActionImpl;
import consulo.document.FileDocumentManager;
import consulo.execution.ExecutionDataKeys;
import consulo.execution.ExecutionManager;
import consulo.execution.ui.RunContentDescriptor;
import consulo.execution.ui.console.ConsoleView;
import consulo.execution.ui.console.ConsoleViewContentType;
import consulo.language.editor.PlatformDataKeys;
import consulo.platform.base.icon.PlatformIconGroup;
import consulo.process.ExecutionException;
import consulo.process.ProcessHandler;
import consulo.process.ProcessHandlerBuilder;
import consulo.process.ProcessOutputTypes;
import consulo.process.cmd.GeneralCommandLine;
import consulo.process.event.ProcessAdapter;
import consulo.process.event.ProcessEvent;
import consulo.project.Project;
import consulo.ui.ex.action.AnAction;
import consulo.ui.ex.action.AnActionEvent;
import consulo.util.dataholder.Key;
import consulo.virtualFileSystem.LocalFileSystem;
import consulo.virtualFileSystem.VirtualFile;
import net.ishchenko.idea.nginx.NginxBundle;
import net.ishchenko.idea.nginx.configurator.NginxServerDescriptor;
import net.ishchenko.idea.nginx.platform.PlatformDependentTools;

import jakarta.annotation.Nullable;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 25.07.2009
 * Time: 4:15:37
 */
@ActionImpl(id = "nginx.reload")
public class NginxReloadAction extends AnAction {
    public NginxReloadAction() {
        super("Reload nginx configuration files", "Reloads nginx configuration files", PlatformIconGroup.actionsRefresh());
    }

    public void actionPerformed(final AnActionEvent event) {

        Project project = event.getData(PlatformDataKeys.PROJECT);

        FileDocumentManager.getInstance().saveAllDocuments(); //todo: save configs only

        RunContentDescriptor runContentDescriptor = getRunContentDescriptor(event);
        if (runContentDescriptor != null) {
            if (runContentDescriptor.getProcessHandler() == null) {
                return;
            }

            final ConsoleView console = (ConsoleView) runContentDescriptor.getExecutionConsole();

            try {
                //the action will be disabled when there's no process handler, so no npe here. i hope...
                NginxProcessHandler processHandler = (NginxProcessHandler) runContentDescriptor.getProcessHandler();
                NginxServerDescriptor descriptor = processHandler.getDescriptor();

                //evil user may have deleted either server configuration
                validateDescriptor(descriptor);

                //it can be omitted for windows, but in linux we see silence if configuration file is invalid on reload
                //as reload is done by SIGHUP
                testConfig(descriptor, console, project);

                doReload(descriptor, console, project);

            } catch (ReloadException e) {
                console.print(e.getMessage() + "\n", ConsoleViewContentType.ERROR_OUTPUT);
            } catch (Exception e) {
                console.print(e.getClass() + " " + e.getMessage() + "\n", ConsoleViewContentType.ERROR_OUTPUT);
            }

        }
    }

    private void validateDescriptor(NginxServerDescriptor descriptor) throws ReloadException {

        VirtualFile executable = LocalFileSystem.getInstance().findFileByPath(descriptor.getExecutablePath());
        if (executable == null) {
            throw new ReloadException(NginxBundle.message("run.error.badpath"));
        }

    }

    private void testConfig(final NginxServerDescriptor descriptor, final ConsoleView console, Project project) throws ReloadException, ExecutionException {

        VirtualFile executableFile = LocalFileSystem.getInstance().findFileByPath(descriptor.getExecutablePath());
        PlatformDependentTools pdt = PlatformDependentTools.getInstance();
        String[] testCommand = pdt.getTestCommand(descriptor);
        int exitValue = runAndGetExitValue(testCommand, new File(executableFile.getParent().getPath()), console);

        if (exitValue != 0) {
            throw new ReloadException(NginxBundle.message("run.validationfailed"));
        }

    }

    private void doReload(NginxServerDescriptor descriptor, ConsoleView console, Project project) throws ReloadException, ExecutionException {

        VirtualFile executableFile = LocalFileSystem.getInstance().findFileByPath(descriptor.getExecutablePath());
        PlatformDependentTools pdt = PlatformDependentTools.getInstance();
        String[] reloadCommand = pdt.getReloadCommand(descriptor);
        int exitValue = runAndGetExitValue(reloadCommand, new File(executableFile.getParent().getPath()), console);

        if (exitValue != 0) {
            throw new ReloadException(NginxBundle.message("run.validationfailed"));
        }

    }

    private int runAndGetExitValue(String[] testCommand, File dir, final ConsoleView console) throws ExecutionException {
        ProcessHandler osph = ProcessHandlerBuilder.create(new GeneralCommandLine(testCommand).withWorkDirectory(dir)).build();
        osph.addProcessListener(new ProcessAdapter() {
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
        return osph.getExitCode();
    }


    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setEnabled(isEnabled(e));
    }

    private boolean isEnabled(AnActionEvent e) {
        ProcessHandler processHandler = getProcessHandler(e);
        return processHandler != null && !(processHandler.isProcessTerminated() || processHandler.isProcessTerminating());
    }

    @Nullable
    private RunContentDescriptor getRunContentDescriptor(AnActionEvent e) {

        //magic copied from com.intellij.execution.actions.StopAction
        RunContentDescriptor runContentDescriptor = e.getData(ExecutionDataKeys.RUN_CONTENT_DESCRIPTOR);
        if (runContentDescriptor == null) {
            Project project = e.getData(PlatformDataKeys.PROJECT);
            if (project != null) {
                runContentDescriptor = ExecutionManager.getInstance(project).getContentManager().getSelectedContent();
            }
        }
        return runContentDescriptor;

    }

    @Nullable
    private ProcessHandler getProcessHandler(AnActionEvent e) {
        RunContentDescriptor rcd = getRunContentDescriptor(e);
        return rcd != null ? rcd.getProcessHandler() : null;
    }

    private static class ReloadException extends Exception {

        private ReloadException(String message) {
            super(message);
        }
    }

}
