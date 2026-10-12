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
import consulo.ui.CheckBox;
import consulo.ui.Component;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.util.lang.StringUtil;
import jakarta.annotation.Nullable;
import net.ishchenko.idea.nginx.configurator.NginxServerDescriptor;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 14.07.2009
 * Time: 19:29:20
 */
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
    @RequiredUIAccess
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

    @Override
    protected void disposeEditor() {
        mediator.form = null;
    }

    class Mediator {
        @Nullable
        NginxRunSettingsForm form;

        @RequiredUIAccess
        void showServerManagerDialog() {
            ShowSettingsUtil.getInstance()
                .showProjectStructureDialog(project, projectStructureSelector -> projectStructureSelector.select((Sdk) null, true))
                .whenComplete((result, error) -> project.getUIAccess().give(() -> {
                    if (!project.isDisposed()) {
                        resetEditorFrom(config);
                    }
                }));
        }

        @RequiredUIAccess
        public void applyEditorTo(NginxRunConfiguration s) {
            NginxRunSettingsForm form = this.form;
            if (form == null) {
                return;
            }

            Sdk descriptor = form.serverCombo.getValue();
            if (descriptor != null) {
                s.setServerDescriptorId(descriptor.getName());
                s.setShowHttpLog(isSelected(form.showHttpLogCheckBox));
                s.setHttpLogPath(StringUtil.notNullize(form.httpLogPathField.getValue()));
                s.setShowErrorLog(isSelected(form.showErrorLogCheckBox));
                s.setErrorLogPath(StringUtil.notNullize(form.errorLogPathField.getValue()));
            }
        }

        @RequiredUIAccess
        public void onChooseDescriptor(@Nullable Sdk sdk) {
            NginxRunSettingsForm form = this.form;
            if (form == null) {
                return;
            }

            NginxServerDescriptor descriptor = sdk != null && sdk.getSdkAdditionalData() instanceof NginxServerDescriptor serverDescriptor
                ? serverDescriptor
                : null;
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
                form.httpLogPathField.setEnabled(isSelected(form.showHttpLogCheckBox));
            }
        }

        @RequiredUIAccess
        public void onErrorLogCheckboxAction() {
            NginxRunSettingsForm form = this.form;
            if (form != null) {
                form.errorLogPathField.setEnabled(isSelected(form.showErrorLogCheckBox));
            }
        }

        @RequiredUIAccess
        public void resetEditorFrom(NginxRunConfiguration configuration) {
            NginxRunSettingsForm form = this.form;
            if (form == null) {
                return;
            }

            List<Sdk> servers = project.getApplication().getInstance(SdkTable.class).getSdksOfType(NginxBundleType.getInstance());
            form.serverModel.replaceAll(servers);

            Sdk descriptor = findServer(servers, configuration.getServerDescriptorId());
            form.serverCombo.setValue(descriptor, false);
            onChooseDescriptor(descriptor);

            String httpLogPath = configuration.getHttpLogPath();
            if (!StringUtil.isEmpty(httpLogPath)) {
                form.httpLogPathField.setValue(httpLogPath);
            }
            String errorLogPath = configuration.getErrorLogPath();
            if (!StringUtil.isEmpty(errorLogPath)) {
                form.errorLogPathField.setValue(errorLogPath);
            }

            form.showHttpLogCheckBox.setValue(configuration.isShowHttpLog(), false);
            form.httpLogPathField.setEnabled(configuration.isShowHttpLog());
            form.showErrorLogCheckBox.setValue(configuration.isShowErrorLog(), false);
            form.errorLogPathField.setEnabled(configuration.isShowErrorLog());
        }

        @Nullable
        private Sdk findServer(List<Sdk> servers, @Nullable String name) {
            if (name == null) {
                return null;
            }
            for (Sdk server : servers) {
                if (name.equals(server.getName())) {
                    return server;
                }
            }
            return null;
        }

        @RequiredUIAccess
        private boolean isSelected(CheckBox checkBox) {
            return Boolean.TRUE.equals(checkBox.getValue());
        }
    }
}
