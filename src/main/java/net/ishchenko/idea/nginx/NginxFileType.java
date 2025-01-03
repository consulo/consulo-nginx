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

package net.ishchenko.idea.nginx;

import consulo.language.file.LanguageFileType;
import consulo.virtualFileSystem.fileType.FileTypeIdentifiableByVirtualFile;
import consulo.virtualFileSystem.VirtualFile;
import consulo.localize.LocalizeValue;
import consulo.nginx.icon.NginxIconGroup;
import consulo.ui.image.Image;
import net.ishchenko.idea.nginx.configurator.NginxServersConfiguration;
import jakarta.annotation.Nonnull;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 04.07.2009
 * Time: 1:03:43
 */
public class NginxFileType extends LanguageFileType implements FileTypeIdentifiableByVirtualFile {

    public static final NginxFileType INSTANCE = new NginxFileType();

    NginxFileType() {
        super(NginxLanguage.INSTANCE);
    }

    public static NginxFileType getInstance() {
        return INSTANCE;
    }

    @Nonnull
    public String getId() {
        return "nginx";
    }

    @Nonnull
    public LocalizeValue getDescription() {
        return LocalizeValue.localizeTODO("nginx config file");
    }

    @Nonnull
    public String getDefaultExtension() {
        return "conf";
    }

    public Image getIcon() {
        return NginxIconGroup.nginx();
    }

    public boolean isMyFileType(VirtualFile virtualFile) {
        return NginxServersConfiguration.getInstance().getFilepaths().contains(virtualFile.getPath());
    }

}



