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
import consulo.content.bundle.Sdk;
import consulo.content.bundle.SdkTable;
import consulo.execution.configuration.ui.SettingsEditor;
import consulo.ide.setting.ShowSettingsUtil;
import consulo.nginx.bundle.NginxBundleType;
import consulo.project.Project;
import consulo.ui.Component;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.util.lang.StringUtil;
import net.ishchenko.idea.nginx.configurator.NginxServerDescriptor;
import org.jspecify.annotations.Nullable;

public class NginxRunSettingsEditor extends SettingsEditor<NginxRunConfiguration> {
    private final Project project;
    private final NginxRunConfiguration config;
    private final Mediator mediator;

    public NginxRunSettingsEditor(NginxRunConfiguration config, Project project) {
        this.project = project;
        this.config = config;
        this.mediator = new Mediator();
    }

    @Override
    protected void applyEditorTo(NginxRunConfiguration s) throws ConfigurationException {
        mediator.applyEditorTo(s);
    }

    @Override
    @RequiredUIAccess
    protected void resetEditorFrom(NginxRunConfiguration s) {
        mediator.resetEditorFrom(s);
    }

    @Override
    @RequiredUIAccess
    protected Component createUIComponent() {
        return new NginxRunSettingsForm(mediator).getPanel();
    }

    class Mediator {
        @Nullable
        NginxRunSettingsForm form;

        @RequiredUIAccess
        void showServerManagerDialog() {
            ShowSettingsUtil.getInstance().showProjectStructureDialog(project, projectStructureSelector -> {
                projectStructureSelector.select((Sdk) null, true);
            });

            resetEditorFrom(config);
        }

        public void applyEditorTo(NginxRunConfiguration s) {
            NginxRunSettingsForm form = this.form;
            if (form == null) {
                return;
            }

            Sdk descriptor = form.serverCombo.getValue();
            if (descriptor != null) {
                s.setServerDescriptorId(descriptor.getName());
                s.setShowHttpLog(Boolean.TRUE.equals(form.showHttpLogCheckBox.getValue()));
                s.setHttpLogPath(form.httpLogPathField.getValue());
                s.setShowErrorLog(Boolean.TRUE.equals(form.showErrorLogCheckBox.getValue()));
                s.setErrorLogPath(form.errorLogPathField.getValue());
            }
        }

        @RequiredUIAccess
        public void onChooseDescriptor(@Nullable Sdk sdk) {
            NginxRunSettingsForm form = this.form;
            if (form == null) {
                return;
            }

            NginxServerDescriptor descriptor = sdk == null ? null : (NginxServerDescriptor) sdk.getSdkAdditionalData();
            if (descriptor != null) {
                form.executableField.setValue(StringUtil.notNullize(descriptor.getExecutablePath()));
                form.configurationField.setValue(StringUtil.notNullize(descriptor.getConfigPath()));
                form.pidField.setValue(StringUtil.notNullize(descriptor.getPidPath()));
                form.globalsField.setValue(StringUtil.notNullize(descriptor.getGlobals()));
                form.httpLogPathField.setValue(StringUtil.notNullize(descriptor.getHttpLogPath()));
                form.errorLogPathField.setValue(StringUtil.notNullize(descriptor.getErrorLogPath()));
            }
            else {
                form.executableField.setValue("");
                form.configurationField.setValue("");
                form.pidField.setValue("");
                form.globalsField.setValue("");
                form.httpLogPathField.setValue("");
                form.errorLogPathField.setValue("");
            }
        }

        @RequiredUIAccess
        public void onHttpLogCheckboxAction() {
            NginxRunSettingsForm form = this.form;
            if (form != null) {
                form.httpLogPathField.setEnabled(Boolean.TRUE.equals(form.showHttpLogCheckBox.getValue()));
            }
        }

        @RequiredUIAccess
        public void onErrorLogCheckboxAction() {
            NginxRunSettingsForm form = this.form;
            if (form != null) {
                form.errorLogPathField.setEnabled(Boolean.TRUE.equals(form.showErrorLogCheckBox.getValue()));
            }
        }

        @RequiredUIAccess
        public void resetEditorFrom(NginxRunConfiguration configuration) {
            NginxRunSettingsForm form = this.form;
            if (form == null) {
                return;
            }

            form.serverModel.replaceAll(SdkTable.getInstance().getSdksOfType(NginxBundleType.getInstance()));

            Sdk descriptor = null;
            String chosenDescriptorId = configuration.getServerDescriptorId();
            if (chosenDescriptorId != null) {
                descriptor = SdkTable.getInstance().findSdk(chosenDescriptorId);
            }
            form.serverCombo.setValue(descriptor);
            onChooseDescriptor(descriptor);

            form.showHttpLogCheckBox.setValue(configuration.isShowHttpLog(), false);
            form.httpLogPathField.setEnabled(configuration.isShowHttpLog());
            form.showErrorLogCheckBox.setValue(configuration.isShowErrorLog(), false);
            form.errorLogPathField.setEnabled(configuration.isShowErrorLog());
        }
    }
}
