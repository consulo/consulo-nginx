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

import consulo.content.bundle.Sdk;
import consulo.content.bundle.SdkUtil;
import consulo.nginx.localize.NginxLocalize;
import consulo.ui.Button;
import consulo.ui.CheckBox;
import consulo.ui.ComboBox;
import consulo.ui.Component;
import consulo.ui.TextBox;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.layout.DockLayout;
import consulo.ui.layout.LabeledLayout;
import consulo.ui.layout.VerticalLayout;
import consulo.ui.model.FlatDataModel;
import consulo.ui.model.MutableFlatDataModel;
import consulo.ui.util.FormBuilder;

import java.util.List;

public class NginxRunSettingsForm {
    final MutableFlatDataModel<Sdk> serverModel = FlatDataModel.of(List.of());
    final ComboBox<Sdk> serverCombo;
    final TextBox executableField;
    final TextBox configurationField;
    final TextBox globalsField;
    final TextBox pidField;
    final CheckBox showHttpLogCheckBox;
    final TextBox httpLogPathField;
    final CheckBox showErrorLogCheckBox;
    final TextBox errorLogPathField;

    private final Component myPanel;

    @RequiredUIAccess
    public NginxRunSettingsForm(NginxRunSettingsEditor.Mediator mediator) {
        mediator.form = this;

        serverCombo = ComboBox.create(serverModel);
        serverCombo.setRender((presentation, item) -> {
            Sdk sdk = item.getValue();
            if (sdk != null) {
                presentation.withIcon(SdkUtil.getIcon(sdk));
                presentation.append(sdk.getName());
            }
        });
        serverCombo.addValueListener(event -> mediator.onChooseDescriptor(event.getValue()));

        Button configureButton = Button.create(NginxLocalize.runConfigurebutton(), event -> mediator.showServerManagerDialog());

        executableField = createReadOnlyField();
        configurationField = createReadOnlyField();
        pidField = createReadOnlyField();
        globalsField = createReadOnlyField();

        showHttpLogCheckBox = CheckBox.create(NginxLocalize.runShowaccesslog());
        showHttpLogCheckBox.addValueListener(event -> mediator.onHttpLogCheckboxAction());
        httpLogPathField = TextBox.create();

        showErrorLogCheckBox = CheckBox.create(NginxLocalize.runShowerrorlog());
        showErrorLogCheckBox.addValueListener(event -> mediator.onErrorLogCheckboxAction());
        errorLogPathField = TextBox.create();

        DockLayout serverRow = DockLayout.create();
        serverRow.center(serverCombo);
        serverRow.right(configureButton);

        FormBuilder serverInfo = FormBuilder.create();
        serverInfo.addLabeled(NginxLocalize.runExecutable(), executableField);
        serverInfo.addLabeled(NginxLocalize.runConfiguration(), configurationField);
        serverInfo.addLabeled(NginxLocalize.runPidpath(), pidField);
        serverInfo.addLabeled(NginxLocalize.runGlobals(), globalsField);

        FormBuilder logFiles = FormBuilder.create();
        logFiles.addLabeled(showHttpLogCheckBox, httpLogPathField);
        logFiles.addLabeled(showErrorLogCheckBox, errorLogPathField);

        FormBuilder builder = FormBuilder.create();
        builder.addLabeled(NginxLocalize.runChoose(), serverRow);

        VerticalLayout panel = VerticalLayout.create();
        panel.add(builder.build());
        panel.add(LabeledLayout.create(NginxLocalize.runServerinfo(), serverInfo.build()));
        panel.add(LabeledLayout.create(NginxLocalize.runLogfiles(), logFiles.build()));
        myPanel = panel;
    }

    @RequiredUIAccess
    private static TextBox createReadOnlyField() {
        TextBox textBox = TextBox.create();
        textBox.setEditable(false);
        return textBox;
    }

    public Component getPanel() {
        return myPanel;
    }
}
