package net.ishchenko.idea.nginx.psi.impl;

import consulo.document.util.TextRange;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiElementVisitor;
import consulo.language.psi.PsiReference;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.language.util.IncorrectOperationException;
import consulo.util.lang.function.Conditions;
import net.ishchenko.idea.nginx.annotator.NginxElementVisitor;
import net.ishchenko.idea.nginx.psi.NginxContext;
import net.ishchenko.idea.nginx.psi.NginxInnerVariable;
import net.ishchenko.idea.nginx.psi.NginxVariableReference;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 14.08.2009
 * Time: 14:31:26
 */
public class NginxInnerVariableImpl extends NginxElementImpl implements NginxInnerVariable {

    public NginxInnerVariableImpl(@Nonnull ASTNode node) {
        super(node);
    }

    @Override
    public void accept(@Nonnull PsiElementVisitor visitor) {
        if (visitor instanceof NginxElementVisitor) {
            ((NginxElementVisitor) visitor).visitInnerVariable(this);
        } else {
            visitor.visitElement(this);
        }
    }

    @Override
    public String getName() {
        return getText().substring(1);
    }

    @Override
    public PsiElement setName(@NonNls String name) throws IncorrectOperationException {
        throw new IncorrectOperationException();
    }

    @Override
    public PsiReference getReference() {
        PsiReference[] references = getReferences();
        return references.length == 0 ? null : references[0];
    }

    @Nonnull
    @Override
    public PsiReference[] getReferences() {
        Collection<NginxInnerVariable> children = PsiTreeUtil.findChildrenOfType(
                PsiTreeUtil.findFirstParent(this, Conditions.instanceOf(NginxContext.class)),
                NginxInnerVariable.class);
        return children.stream()
                .filter(child -> child.getName().contains(this.getName()))
                .map(child -> new NginxVariableReference(this, new TextRange(0, child.getName().length() + 1)))
                .toArray(PsiReference[]::new);
    }

}
