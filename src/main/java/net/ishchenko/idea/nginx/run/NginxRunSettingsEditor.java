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

import consulo.configurable.ConfigurationException;
import consulo.execution.configuration.ui.SettingsEditor;
import consulo.ide.setting.ShowSettingsUtil;
import net.ishchenko.idea.nginx.configurator.NginxConfigurationManager;
import net.ishchenko.idea.nginx.configurator.NginxServerDescriptor;
import net.ishchenko.idea.nginx.configurator.NginxServersConfiguration;

import javax.annotation.Nonnull;
import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 14.07.2009
 * Time: 19:29:20
 */
public class NginxRunSettingsEditor extends SettingsEditor<NginxRunConfiguration> {

    private NginxRunConfiguration config;
    private Mediator mediator;

    public NginxRunSettingsEditor(NginxRunConfiguration config) {
        this.config = config;
        this.mediator = new Mediator();
    }

    protected void applyEditorTo(NginxRunConfiguration s) throws ConfigurationException {
        mediator.applyEditorTo(s);
    }

    protected void resetEditorFrom(NginxRunConfiguration s) {
        mediator.resetEditorFrom(s);
    }

    @Nonnull
    protected JComponent createEditor() {
        return new NginxRunSettingsForm(mediator).getPanel();
    }

    protected void disposeEditor() {
        mediator = null;
    }

    class Mediator {

        NginxRunSettingsForm form;

        void showServerManagerDialog() {
            ShowSettingsUtil.getInstance().showAndSelect(null, NginxConfigurationManager.class);

            resetEditorFrom(config);

        }

        public void applyEditorTo(NginxRunConfiguration s) {
            if (form.serverCombo.getSelectedItem() != null) {
                NginxServerDescriptor descriptor = (NginxServerDescriptor) form.serverCombo.getSelectedItem();
                s.setServerDescriptorId(descriptor.getId());
                s.setShowHttpLog(mediator.form.showHttpLogCheckBox.isSelected());
                s.setHttpLogPath(mediator.form.httpLogPathField.getText());
                s.setShowErrorLog(mediator.form.showErrorLogCheckBox.isSelected());
                s.setErrorLogPath(mediator.form.errorLogPathField.getText());
            }
        }

        public void onChooseDescriptor(NginxServerDescriptor descriptor) {
            if (descriptor != null) {
                form.executableField.setText(descriptor.getExecutablePath());
                form.configurationField.setText(descriptor.getConfigPath());
                form.pidField.setText(descriptor.getPidPath());
                form.globalsField.setText(descriptor.getGlobals());
                form.httpLogPathField.setText(descriptor.getHttpLogPath());
                form.errorLogPathField.setText(descriptor.getErrorLogPath());
            } else {
                form.executableField.setText("");
                form.configurationField.setText("");
                form.pidField.setText("");
                form.globalsField.setText("");
                form.httpLogPathField.setText("");
                form.errorLogPathField.setText("");
            }
        }

        public void onHttpLogCheckboxAction() {
            form.httpLogPathField.setEnabled(form.showHttpLogCheckBox.isSelected());
        }

        public void onErrorLogCheckboxAction() {
            form.errorLogPathField.setEnabled(form.showErrorLogCheckBox.isSelected());
        }

        public void resetEditorFrom(NginxRunConfiguration configuration) {
            DefaultComboBoxModel model = (DefaultComboBoxModel) form.serverCombo.getModel();
            model.removeAllElements();
            NginxServersConfiguration servers = NginxServersConfiguration.getInstance();
            for (NginxServerDescriptor descriptor : servers.getServersDescriptors()) {
                model.addElement(descriptor);
            }
            String chosenDescriptorId = configuration.getServerDescriptorId();
            if (chosenDescriptorId != null) {
                NginxServersConfiguration serversConfig = NginxServersConfiguration.getInstance();
                NginxServerDescriptor descriptor = serversConfig.getDescriptorById(chosenDescriptorId);
                if (descriptor != null) {
                    model.setSelectedItem(descriptor);
                } else {
                    model.setSelectedItem(null);
                }
            } else {
                model.setSelectedItem(null);
            }
            form.showHttpLogCheckBox.setSelected(configuration.isShowHttpLog());
            form.httpLogPathField.setEnabled(configuration.isShowHttpLog());
            form.showErrorLogCheckBox.setSelected(configuration.isShowErrorLog());
            form.errorLogPathField.setEnabled(configuration.isShowErrorLog());
        }

    }

}
