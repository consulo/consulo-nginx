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

package net.ishchenko.idea.nginx.lexer;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import consulo.language.lexer.FlexAdapter;
import consulo.language.lexer.Lexer;
import consulo.codeEditor.DefaultLanguageHighlighterColors;
import consulo.codeEditor.HighlighterColors;
import consulo.colorScheme.TextAttributesKey;
import consulo.language.editor.highlight.SyntaxHighlighterBase;
import consulo.language.ast.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 06.07.2009
 * Time: 16:40:05
 */
public class NginxSyntaxHighlighter extends SyntaxHighlighterBase {

    private final TextAttributesKey[] BAD_CHARACTER_KEYS = new TextAttributesKey[]{HighlighterColors.BAD_CHARACTER};
    private final Map<IElementType, TextAttributesKey> colors = new HashMap<>();

    public NginxSyntaxHighlighter() {

        colors.put(NginxElementTypes.BAD_CHARACTER, HighlighterColors.BAD_CHARACTER);
        colors.put(NginxElementTypes.COMMENT, DefaultLanguageHighlighterColors.BLOCK_COMMENT);

        colors.put(NginxElementTypes.CONTEXT_NAME, DefaultLanguageHighlighterColors.KEYWORD);
        colors.put(NginxElementTypes.DIRECTIVE_STRING_VALUE, DefaultLanguageHighlighterColors.STRING);
        colors.put(NginxElementTypes.INNER_VARIABLE, DefaultLanguageHighlighterColors.NUMBER);
        colors.put(NginxElementTypes.TEMPLATE_VARIABLE, DefaultLanguageHighlighterColors.CONSTANT);

    }

    @Nonnull
    public Lexer getHighlightingLexer() {
        return new FlexAdapter(new _NginxLexer(null));
    }

    @Nonnull
    public TextAttributesKey[] getTokenHighlights(IElementType iElementType) {

        return new TextAttributesKey[]{colors.get(iElementType)};

    }

}
