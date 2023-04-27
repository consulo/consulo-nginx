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

import consulo.annotation.component.ExtensionImpl;
import consulo.configurable.ApplicationConfigurable;
import consulo.configurable.ConfigurationException;
import consulo.disposer.Disposable;
import consulo.ui.annotation.RequiredUIAccess;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import net.ishchenko.idea.nginx.NginxBundle;
import org.jetbrains.annotations.Nls;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 21.07.2009
 * Time: 15:10:16
 */
@ExtensionImpl
public class NginxConfigurationManager implements ApplicationConfigurable {

    private Provider<NginxServersConfiguration> configurationProvider;
    private NginxConfigurationPanel panel;

    @Inject
    public NginxConfigurationManager(Provider<NginxServersConfiguration> configurationProvider) {
        this.configurationProvider = configurationProvider;
    }

    @Nonnull
    @Override
    public String getId() {
        return "nginx.servers";
    }

    @Nullable
    @Override
    public String getParentId() {
        return "execution";
    }

    @Nls
    public String getDisplayName() {
        return NginxBundle.message("config.title");
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
            panel = new NginxConfigurationPanel(configurationProvider.get());
        }
        return panel.getPanel();
    }

    @RequiredUIAccess
    public void disposeUIResources() {
        panel = null;
    }

}