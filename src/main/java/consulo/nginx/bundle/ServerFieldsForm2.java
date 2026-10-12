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
import consulo.disposer.Disposable;
import consulo.fileChooser.FileChooserDescriptorFactory;
import consulo.fileChooser.FileChooserTextBoxBuilder;
import consulo.localize.LocalizeValue;
import consulo.nginx.localize.NginxLocalize;
import consulo.ui.Component;
import consulo.ui.TextBox;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.util.FormBuilder;
import consulo.util.lang.StringUtil;
import net.ishchenko.idea.nginx.configurator.NginxServerDescriptor;

import jakarta.annotation.Nullable;
import java.util.Objects;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 28.07.2009
 * Time: 3:41:46
 */
public class ServerFieldsForm2 implements AdditionalDataConfigurable {
    private final NginxServerDescriptor nginxServerDescriptor;

    @Nullable
    private Component myComponent;
    @Nullable
    private FileChooserTextBoxBuilder.Controller myConfigurationField;
    @Nullable
    private FileChooserTextBoxBuilder.Controller myPidField;
    @Nullable
    private TextBox myGlobalsField;

    public ServerFieldsForm2(NginxServerDescriptor descriptor) {
        this.nginxServerDescriptor = descriptor;
    }

    @RequiredUIAccess
    @Override
    public Component createUIComponent(Disposable parentDisposable) {
        if (myComponent == null) {
            myConfigurationField = createFileField(parentDisposable);
            myPidField = createFileField(parentDisposable);
            myGlobalsField = TextBox.create();

            FormBuilder builder = FormBuilder.create();
            builder.addLabeled(LocalizeValue.join(NginxLocalize.runConfiguration(), LocalizeValue.colon()), myConfigurationField.getComponent());
            builder.addLabeled(LocalizeValue.join(NginxLocalize.runPidpath(), LocalizeValue.colon()), myPidField.getComponent());
            builder.addLabeled(LocalizeValue.join(NginxLocalize.runGlobals(), LocalizeValue.colon()), myGlobalsField);
            myComponent = builder.build();
        }
        return myComponent;
    }

    @RequiredUIAccess
    private static FileChooserTextBoxBuilder.Controller createFileField(Disposable parentDisposable) {
        FileChooserTextBoxBuilder builder = FileChooserTextBoxBuilder.create(null);
        builder.fileChooserDescriptor(FileChooserDescriptorFactory.createSingleFileNoJarsDescriptor());
        builder.uiDisposable(parentDisposable);
        return builder.build();
    }

    @Override
    public void setSdk(Sdk sdk) {

    }

    @RequiredUIAccess
    @Override
    public boolean isModified() {
        if (myConfigurationField == null || myPidField == null || myGlobalsField == null) {
            return false;
        }
        return !Objects.equals(myConfigurationField.getValue(), StringUtil.notNullize(nginxServerDescriptor.getConfigPath())) ||
                !Objects.equals(myPidField.getValue(), StringUtil.notNullize(nginxServerDescriptor.getPidPath())) ||
                !Objects.equals(StringUtil.notNullize(myGlobalsField.getValue()), StringUtil.notNullize(nginxServerDescriptor.getGlobals()));
    }

    @RequiredUIAccess
    @Override
    public void apply() throws ConfigurationException {
        if (myConfigurationField == null || myPidField == null || myGlobalsField == null) {
            return;
        }
        nginxServerDescriptor.setConfigPath(myConfigurationField.getValue());
        nginxServerDescriptor.setPidPath(myPidField.getValue());
        nginxServerDescriptor.setGlobals(StringUtil.notNullize(myGlobalsField.getValue()));
    }

    @RequiredUIAccess
    @Override
    public void reset() {
        if (myConfigurationField == null || myPidField == null || myGlobalsField == null) {
            return;
        }
        myConfigurationField.setValue(StringUtil.notNullize(nginxServerDescriptor.getConfigPath()));
        myPidField.setValue(StringUtil.notNullize(nginxServerDescriptor.getPidPath()));
        myGlobalsField.setValue(StringUtil.notNullize(nginxServerDescriptor.getGlobals()));
    }

    @RequiredUIAccess
    @Override
    public void disposeUIResources() {
        myComponent = null;
        myConfigurationField = null;
        myPidField = null;
        myGlobalsField = null;
    }
}
