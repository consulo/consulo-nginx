package net.ishchenko.idea.nginx.psi.impl;

import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElementVisitor;
import net.ishchenko.idea.nginx.annotator.NginxElementVisitor;
import net.ishchenko.idea.nginx.psi.NginxComplexValue;
import net.ishchenko.idea.nginx.psi.NginxDirective;
import javax.annotation.Nonnull;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 15.08.2009
 * Time: 20:01:27
 */
public class NginxComplexValueImpl extends NginxElementImpl implements NginxComplexValue {

    public NginxComplexValueImpl(@Nonnull ASTNode node) {
        super(node);
    }


    public NginxDirective getDirective() {
        return (NginxDirective) getNode().getTreeParent().getPsi();
    }

    public boolean isFirstValue() {
        return !(getNode().getTreePrev().getTreePrev().getPsi() instanceof NginxComplexValue);
    }

    @Override
    public void accept(@Nonnull PsiElementVisitor visitor) {
        if (visitor instanceof NginxElementVisitor) {
            ((NginxElementVisitor) visitor).visitComplexValue(this);
        } else {
            visitor.visitElement(this);
        }
    }
}
