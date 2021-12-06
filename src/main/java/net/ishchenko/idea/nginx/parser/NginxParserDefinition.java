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

package net.ishchenko.idea.nginx.parser;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.lang.LanguageUtil;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import consulo.lang.LanguageVersion;
import consulo.lang.util.LanguageVersionUtil;
import net.ishchenko.idea.nginx.NginxLanguage;
import net.ishchenko.idea.nginx.lexer.NginxElementTypes;
import net.ishchenko.idea.nginx.lexer.NginxParsingLexer;
import net.ishchenko.idea.nginx.psi.impl.*;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 04.07.2009
 * Time: 14:39:39
 */
public class NginxParserDefinition implements ParserDefinition {
    @NotNull
    public Lexer createLexer(LanguageVersion languageVersion) {
        return new NginxParsingLexer();
    }

    public PsiParser createParser(LanguageVersion languageVersion) {
        return new NginxParser();
    }

    public IFileElementType getFileNodeType() {
        return NginxElementTypes.FILE;
    }

    @NotNull
    public TokenSet getWhitespaceTokens(LanguageVersion languageVersion) {
        return NginxElementTypes.WHITE_SPACES;
    }

    @NotNull
    public TokenSet getCommentTokens(LanguageVersion languageVersion) {
        return NginxElementTypes.COMMENTS;
    }

    @NotNull
    public TokenSet getStringLiteralElements(LanguageVersion languageVersion) {
        return NginxElementTypes.STRINGS;
    }

    @NotNull
    public PsiElement createElement(ASTNode node) {
        IElementType type = node.getElementType();
        if (type == NginxElementTypes.DIRECTIVE) {
            return new NginxDirectiveImpl(node);
        } else if (type == NginxElementTypes.CONTEXT_NAME) {
            return new NginxDirectiveNameImpl(node);
        } else if (type == NginxElementTypes.DIRECTIVE_NAME) {
            return new NginxDirectiveNameImpl(node);
        } else if (type == NginxElementTypes.DIRECTIVE_VALUE) {
            return new NginxDirectiveValueImpl(node);
        } else if (type == NginxElementTypes.DIRECTIVE_STRING_VALUE) {
            return new NginxDirectiveValueImpl(node);
        } else if (type == NginxElementTypes.INNER_VARIABLE) {
            return new NginxInnerVariableImpl(node);
        } else if (type == NginxElementTypes.COMPLEX_VALUE) {
            return new NginxComplexValueImpl(node);
        } else if (type == NginxElementTypes.CONTEXT) {
            return new NginxContextImpl(node);
        } else if (type == NginxElementTypes.LUA_CONTEXT) {
            return new NginxLuaContextImpl(node);
        }

        return new ASTWrapperPsiElement(node);
    }

    public PsiFile createFile(FileViewProvider fileViewProvider) {
        return new NginxPsiFileImpl(fileViewProvider);
    }

    public SpaceRequirements spaceExistenceTypeBetweenTokens(ASTNode left, ASTNode right) {
        final Lexer lexer = createLexer(LanguageVersionUtil.findDefaultVersion(NginxLanguage.INSTANCE));
        return LanguageUtil.canStickTokensTogetherByLexer(left, right, lexer);
    }
}
