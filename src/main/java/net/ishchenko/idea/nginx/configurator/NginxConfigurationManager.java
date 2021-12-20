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

package net.ishchenko.idea.nginx.configurator;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import consulo.disposer.Disposable;
import consulo.ui.annotation.RequiredUIAccess;
import net.ishchenko.idea.nginx.NginxBundle;
import org.jetbrains.annotations.Nls;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 21.07.2009
 * Time: 15:10:16
 */
public class NginxConfigurationManager implements Configurable  {

    private NginxServersConfiguration configuration;
    private NginxConfigurationPanel panel;

    public NginxConfigurationManager(NginxServersConfiguration configuration) {
        this.configuration = configuration;
    }

    public NginxServersConfiguration getConfiguration() {
        return configuration;
    }

    @Nls
    public String getDisplayName() {
        return NginxBundle.message("config.title");
    }

    public String getHelpTopic() {
        return null;
    }

    @RequiredUIAccess
    @Override
    public void reset() {
        panel.reset();
    }

    @RequiredUIAccess
    @Override
    public void apply() throws ConfigurationException {
        panel.apply();
    }

    @RequiredUIAccess
    @Override
    public boolean isModified() {
        return panel.isModified();
    }

    @RequiredUIAccess
    @Override
    public JComponent createComponent(Disposable disposable) {
        if (panel == null) {
            panel = new NginxConfigurationPanel(configuration);
        }
        return panel.getPanel();
    }

    @RequiredUIAccess
    public void disposeUIResources() {
        panel = null;
    }

}