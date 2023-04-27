package net.ishchenko.idea.nginx.psi.impl;

import consulo.annotation.component.ExtensionImpl;
import consulo.application.ApplicationManager;
import consulo.document.Document;
import consulo.document.FileDocumentManager;
import consulo.document.util.TextRange;
import consulo.language.psi.AbstractElementManipulator;
import consulo.language.psi.PsiDocumentManager;
import consulo.language.util.IncorrectOperationException;
import net.ishchenko.idea.nginx.configurator.NginxServersConfiguration;
import net.ishchenko.idea.nginx.psi.NginxDirectiveValue;

import javax.annotation.Nonnull;

/**
 * User: Max
 * Date: 24.08.2009
 * Time: 14:48:06
 */
@ExtensionImpl
public class NginxDirectiveValueManipulator extends AbstractElementManipulator<NginxDirectiveValue> {

    /**
     * Some included file name has been changed. Changing value text and rebuilding configuration file types mapping
     */
    public NginxDirectiveValue handleContentChange(NginxDirectiveValue element, TextRange range, String newContent) throws IncorrectOperationException {

        String oldText = element.getText();
        String newText = oldText.substring(0, range.getStartOffset()) + newContent + oldText.substring(range.getEndOffset());
        Document document = FileDocumentManager.getInstance().getDocument(element.getContainingFile().getVirtualFile());
        document.replaceString(element.getTextRange().getStartOffset(), element.getTextRange().getEndOffset(), newText);
        PsiDocumentManager.getInstance(element.getProject()).commitDocument(document);

        NginxServersConfiguration nginxServersConfiguration = ApplicationManager.getApplication().getComponent(NginxServersConfiguration.class);
        nginxServersConfiguration.rebuildFilepaths();

        return element;

    }

    @Nonnull
    @Override
    public Class<NginxDirectiveValue> getElementClass() {
        return NginxDirectiveValue.class;
    }
}
