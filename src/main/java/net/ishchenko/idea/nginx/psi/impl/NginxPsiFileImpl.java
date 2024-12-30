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

package net.ishchenko.idea.nginx.psi.impl;

import consulo.language.impl.psi.PsiFileBase;
import consulo.virtualFileSystem.fileType.FileType;
import consulo.language.file.FileViewProvider;
import net.ishchenko.idea.nginx.NginxFileType;
import net.ishchenko.idea.nginx.psi.NginxPsiFile;
import jakarta.annotation.Nonnull;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 07.07.2009
 * Time: 17:29:16
 */
public class NginxPsiFileImpl extends PsiFileBase implements NginxPsiFile {

    public NginxPsiFileImpl(FileViewProvider viewProvider) {
        super(viewProvider, NginxFileType.getInstance().getLanguage());
    }

    @Nonnull
    public FileType getFileType() {
        return NginxFileType.getInstance();
    }

}
