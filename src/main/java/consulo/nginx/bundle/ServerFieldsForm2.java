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

package consulo.nginx.bundle;

import consulo.configurable.ConfigurationException;
import consulo.content.bundle.AdditionalDataConfigurable;
import consulo.content.bundle.Sdk;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.ex.awt.ComponentWithBrowseButton;
import net.ishchenko.idea.nginx.configurator.NginxServerDescriptor;

import javax.annotation.Nullable;
import javax.swing.*;
import java.util.Objects;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 28.07.2009
 * Time: 3:41:46
 */
public class ServerFieldsForm2 implements AdditionalDataConfigurable {
    private JPanel panel;
    private ComponentWithBrowseButton<JTextField> configurationField;
    private ComponentWithBrowseButton<JTextField> pidField;
    private JTextField globalsField;

    private final NginxServerDescriptor nginxServerDescriptor;

    public ServerFieldsForm2(NginxServerDescriptor descriptor) {
        this.nginxServerDescriptor = descriptor;

        configurationField.getChildComponent().setText(descriptor.getConfigPath());
        pidField.getChildComponent().setText(descriptor.getPidPath());
        globalsField.setText(descriptor.getGlobals());
    }

    @RequiredUIAccess
    @Nullable
    @Override
    public JComponent createComponent() {
        return panel;
    }

    private void createUIComponents() {
        configurationField = new ComponentWithBrowseButton<>(new JTextField(), null);
        pidField = new ComponentWithBrowseButton<>(new JTextField(), null);
    }

    @Override
    public void setSdk(Sdk sdk) {

    }

    @RequiredUIAccess
    @Override
    public boolean isModified() {
        return !Objects.equals(configurationField.getChildComponent().getText(),  nginxServerDescriptor.getConfigPath()) ||
                !Objects.equals(pidField.getChildComponent().getText(), nginxServerDescriptor.getPidPath()) ||
                !Objects.equals(globalsField.getText(), nginxServerDescriptor.getGlobals());
    }

    @RequiredUIAccess
    @Override
    public void apply() throws ConfigurationException {
        nginxServerDescriptor.setConfigPath(configurationField.getChildComponent().getText());
        nginxServerDescriptor.setPidPath(pidField.getChildComponent().getText());
        nginxServerDescriptor.setGlobals(globalsField.getText());
    }
}
