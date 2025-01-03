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

import consulo.annotation.component.ExtensionImpl;
import consulo.execution.configuration.ConfigurationFactory;
import consulo.execution.configuration.ConfigurationType;
import consulo.execution.configuration.RunConfiguration;
import consulo.localize.LocalizeValue;
import consulo.nginx.icon.NginxIconGroup;
import consulo.nginx.localize.NginxLocalize;
import consulo.project.Project;
import consulo.ui.image.Image;
import net.ishchenko.idea.nginx.NginxBundle;
import jakarta.annotation.Nonnull;

/**
 * User: Max
 * Date: 14.07.2009
 * Time: 19:10:29
 */
@ExtensionImpl
public class NginxConfigurationType implements ConfigurationType {

    NginxConfigurationFactory ncf = new NginxConfigurationFactory(this);

    public LocalizeValue getDisplayName() {
        return NginxLocalize.cofigurationtypeDisplayname();
    }

    public LocalizeValue getConfigurationTypeDescription() {
        return NginxLocalize.configurationtypeDescription();
    }

    public Image getIcon() {
        return NginxIconGroup.nginx();
    }

    @Nonnull
    public String getId() {
        return "nginx.configuration.type";
    }

    public ConfigurationFactory[] getConfigurationFactories() {
        return new ConfigurationFactory[]{ncf};
    }

    private static class NginxConfigurationFactory extends ConfigurationFactory {

        protected NginxConfigurationFactory(@Nonnull ConfigurationType type) {
            super(type);
        }

        public RunConfiguration createTemplateConfiguration(Project project) {
            return new NginxRunConfiguration(project, this, NginxBundle.message("cofigurationtype.displayname"));
        }
    }
}
