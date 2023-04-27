package net.ishchenko.idea.nginx;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.editor.documentation.LanguageDocumentationProvider;
import consulo.language.psi.PsiElement;
import consulo.logging.Logger;
import net.ishchenko.idea.nginx.psi.NginxDirectiveName;
import net.ishchenko.idea.nginx.psi.NginxInnerVariable;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 19.08.2009
 * Time: 0:49:41
 */
@ExtensionImpl
public class NginxDocumentationProvider implements LanguageDocumentationProvider {

    public static final Logger LOG = Logger.getInstance(NginxDocumentationProvider.class);

    @Override
    public String generateDoc(PsiElement element, PsiElement originalElement) {

        if (element instanceof NginxDirectiveName) {
            return generateDocForDirectiveName((NginxDirectiveName) element);
        } else if (element instanceof NginxInnerVariable) {
            return generateDocForInnerVariable((NginxInnerVariable) element);
        }
        return null;
    }

    private String generateDocForDirectiveName(NginxDirectiveName element) {

        StringBuilder result = new StringBuilder();
        InputStream docStream = getClass().getResourceAsStream("/docs/directives/" + element.getText() + ".html");
        if (docStream == null) {
            result.append(NginxBundle.message("docs.directive.notfound", element.getText()));
        } else {
            BufferedReader keywordsReader = new BufferedReader(new InputStreamReader(docStream));
            try {
                String line;
                while ((line = keywordsReader.readLine()) != null) {
                    result.append(line).append("\n");
                }
            } catch (IOException e) {
                LOG.error(e);
                return null;
            } finally {
                try {
                    keywordsReader.close();
                } catch (IOException e) {
                    LOG.error(e);
                }
            }
        }

        return result.toString();
    }

    private String generateDocForInnerVariable(NginxInnerVariable element) {

        StringBuilder result = new StringBuilder();
        InputStream docStream = getClass().getResourceAsStream("/docs/variables/" + element.getName() + ".html");
        if (docStream == null) {
            result.append(NginxBundle.message("docs.variable.notfound", element.getName()));
        } else {
            result.append("<b>").append(element.getText()).append("</b><br>");
            BufferedReader keywordsReader = new BufferedReader(new InputStreamReader(docStream));
            try {
                String line;
                while ((line = keywordsReader.readLine()) != null) {
                    result.append(line).append("\n");
                }
            } catch (IOException e) {
                LOG.error(e);
                return null;
            } finally {
                try {
                    keywordsReader.close();
                } catch (IOException e) {
                    LOG.error(e);
                }
            }
        }

        return result.toString();

    }

    @Nonnull
    @Override
    public Language getLanguage() {
        return NginxLanguage.INSTANCE;
    }
}
