package net.ishchenko.idea.nginx.psi;

import consulo.document.util.TextRange;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiNamedElement;
import consulo.language.psi.PsiReference;
import consulo.language.psi.PsiReferenceBase;
import consulo.language.psi.util.PsiTreeUtil;
import net.ishchenko.idea.nginx.NginxKeywordsManager;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Collection;

public class NginxVariableReference extends PsiReferenceBase<PsiElement> implements PsiReference {

    private final String name;

    public NginxVariableReference(PsiNamedElement element, TextRange rangeInElement) {
        super(element, rangeInElement);
        name = element.getName();
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        PsiElement parent = PsiTreeUtil.getParentOfType(myElement, NginxContext.class);
        PsiElement source = null;
        while (source == null && parent != null && parent.getParent() != null) {
            source = findSource(parent);
            parent = PsiTreeUtil.getParentOfType(parent, NginxContext.class);
        }

        return source;
    }

    private PsiElement findSource(PsiElement parent) {
        Collection<PsiNamedElement> children = PsiTreeUtil.findChildrenOfType(parent, NginxInnerVariable.class);
        return children.stream()
                .filter(child -> child.getName() != null
                        && child.getName().equals(name)
                        && child.getParent().getParent() instanceof NginxDirective
                        && NginxKeywordsManager.SET_DIRECTIVES.contains(
                            ((NginxDirective)child.getParent().getParent()).getNameString()))
                .findFirst()
                .orElse(null);
    }

    @Nonnull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }

}
