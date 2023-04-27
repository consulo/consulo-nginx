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
import consulo.language.Language;
import consulo.language.editor.completion.CompletionContributor;
import consulo.language.editor.completion.CompletionParameters;
import consulo.language.editor.completion.CompletionResultSet;
import consulo.language.editor.completion.lookup.LookupElementBuilder;
import consulo.language.psi.PsiElement;
import net.ishchenko.idea.nginx.psi.*;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 15.07.2009
 * Time: 0:37:39
 */
@ExtensionImpl
public class NginxCompletionContributor extends CompletionContributor {

    private static List<LookupElementBuilder> booleanVariants = new ArrayList<>();

    private NginxKeywordsManager keywords;

    private List<LookupElementBuilder> allLookupElements = new ArrayList<>();
    private Map<String, Set<LookupElementBuilder>> contextToDirectiveNameElements = new HashMap<>();
    private List<LookupElementBuilder> mainContextDirectiveNameElements = new ArrayList<>();

    static {
        booleanVariants.add(LookupElementBuilder.create("on"));
        booleanVariants.add(LookupElementBuilder.create("off"));
    }

    public NginxCompletionContributor() {

        this.keywords = NginxKeywordsManager.getInstance();

        for (String keyword : keywords.getKeywords()) {
            allLookupElements.add(LookupElementBuilder.create(keyword));
        }

        Map<String, Set<String>> contextToDirectives = keywords.getContextToDirectiveListMappings();
        for (Map.Entry<String, Set<String>> entry : contextToDirectives.entrySet()) {
            Set<LookupElementBuilder> directives = new HashSet<>();
            for (String directive : entry.getValue()) {
                directives.add(LookupElementBuilder.create(directive));
            }
            contextToDirectiveNameElements.put(entry.getKey(), directives);
        }

        for (String directive : keywords.getDirectivesThatCanResideInMainContext()) {
            mainContextDirectiveNameElements.add(LookupElementBuilder.create(directive));
        }

    }

    @Override
    public void fillCompletionVariants(CompletionParameters parameters, CompletionResultSet result) {

        if (parameters.getOriginalFile() instanceof NginxPsiFile) {

            PsiElement parent = parameters.getPosition().getParent();

            if (parent instanceof NginxDirectiveName && !((NginxDirectiveName) parent).getDirective().isInChaosContext()) {
                suggestName(result, (NginxDirectiveName) parent);

            } else if (parent instanceof NginxDirectiveValue) {

                PsiElement variable = parent.getPrevSibling();
                if (variable != null) {

                    if (variable instanceof NginxInnerVariable) {
                        result = result.withPrefixMatcher(variable.getText().substring(1)); //cutting first $
                    } else if (variable instanceof NginxDirectiveValue) {
                        if (variable.getText().endsWith("$")) {
                            //this is ctrl+space at the end of asdqwe$ (asdqwe$ is treated as simple value)
                            result = result.withPrefixMatcher("");
                        }
                    } else {
                        throw new AssertionError("got some weird type when autocompleting"); //hmm...
                    }
                    suggestVariable(result);

                } else {
                    suggestValue(result, (NginxDirectiveValue) parent);
                }
            }

        }

    }

    private void suggestName(CompletionResultSet result, NginxDirectiveName where) {

        NginxContext context = where.getDirective().getParentContext();
        if (context == null) {
            for (LookupElementBuilder mainContextElement : mainContextDirectiveNameElements) {
                result.addElement(mainContextElement);
            }
        } else {
            String contextName = context.getDirective().getNameString();
            Set<LookupElementBuilder> elementsForContext = contextToDirectiveNameElements.get(contextName);
            if (elementsForContext == null) {
                //parent directive might be unknown. suggest all.
                for (LookupElementBuilder LookupItem : allLookupElements) {
                    result.addElement(LookupItem);
                }
            } else {
                for (LookupElementBuilder LookupItem : elementsForContext) {
                    result.addElement(LookupItem);
                }
            }
        }

    }

    private void suggestValue(CompletionResultSet result, NginxDirectiveValue where) {
        if (keywords.checkBooleanKeyword(where.getDirective().getNameString())) {
            for (LookupElementBuilder booleanVariant : booleanVariants) {
                result.addElement(booleanVariant);
            }
        }
    }

    private void suggestVariable(CompletionResultSet result) {

        for (String variable : keywords.getVariables()) {
            result.addElement(LookupElementBuilder.create(variable));
        }

    }


    @Nonnull
    @Override
    public Language getLanguage() {
        return NginxLanguage.INSTANCE;
    }
}
