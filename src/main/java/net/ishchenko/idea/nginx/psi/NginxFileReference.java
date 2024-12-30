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

package net.ishchenko.idea.nginx.psi;

import consulo.document.util.TextRange;
import consulo.language.psi.path.FileReference;
import consulo.language.psi.path.FileReferenceSet;
import jakarta.annotation.Nonnull;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 02.08.2009
 * Time: 20:36:16
 */
public class NginxFileReference extends FileReference {


    public NginxFileReference(FileReference fileReference) {
        super(fileReference);
    }

    public NginxFileReference(@Nonnull FileReferenceSet fileReferenceSet, TextRange textRange, int i, String s) {
        super(fileReferenceSet, textRange, i, s);
    }

}
