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

import consulo.language.impl.psi.ASTWrapperPsiElement;
import consulo.language.ast.ASTNode;
import net.ishchenko.idea.nginx.psi.NginxPsiElement;
import jakarta.annotation.Nonnull;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 07.07.2009
 * Time: 17:23:03
 */
public class NginxElementImpl extends ASTWrapperPsiElement implements NginxPsiElement {

    public NginxElementImpl(@Nonnull ASTNode node) {
        super(node);
    }

    @Override
    public String toString() {
        String classname = getClass().getName();
        if (classname.endsWith("Impl")) {
            classname = classname.substring(0, classname.length() - "Impl".length());
        }
        classname = classname.substring(classname.lastIndexOf(".") + 1);
        return classname;
    }
}
