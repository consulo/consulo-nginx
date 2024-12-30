package net.ishchenko.idea.nginx.run;

import consulo.annotation.component.ExtensionImpl;
import consulo.execution.configuration.RunProfile;
import consulo.execution.executor.DefaultRunExecutor;
import consulo.execution.runner.DefaultProgramRunner;
import jakarta.annotation.Nonnull;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 17.10.2009
 * Time: 19:44:31
 */
@ExtensionImpl
public class NginxRunner extends DefaultProgramRunner {

    @Nonnull
    public String getRunnerId() {
        return "NginxRunner";
    }

    public boolean canRun(@Nonnull String executorId, @Nonnull RunProfile profile) {
        return DefaultRunExecutor.EXECUTOR_ID.equals(executorId) && profile instanceof NginxRunConfiguration;
    }

}
