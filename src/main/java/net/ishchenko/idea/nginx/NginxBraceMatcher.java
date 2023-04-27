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

import consulo.annotation.component.ExtensionImpl;
import consulo.language.BracePair;
import consulo.language.Language;
import consulo.language.PairedBraceMatcher;
import net.ishchenko.idea.nginx.lexer.NginxElementTypes;

import javax.annotation.Nonnull;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 13.07.2009
 * Time: 20:37:13
 */
@ExtensionImpl
public class NginxBraceMatcher implements PairedBraceMatcher {

    public static final BracePair[] BRACES = new BracePair[]{new BracePair(NginxElementTypes.OPENING_BRACE, NginxElementTypes.CLOSING_BRACE, true)};

    public BracePair[] getPairs() {
        return BRACES;
    }

    @Nonnull
    @Override
    public Language getLanguage() {
        return NginxLanguage.INSTANCE;
    }
}
