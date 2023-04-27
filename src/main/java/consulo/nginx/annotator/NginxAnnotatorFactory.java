package consulo.nginx.annotator;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.editor.annotation.Annotator;
import consulo.language.editor.annotation.AnnotatorFactory;
import net.ishchenko.idea.nginx.NginxLanguage;
import net.ishchenko.idea.nginx.annotator.NginxAnnotatingVisitor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author VISTALL
 * @since 07/01/2023
 */
@ExtensionImpl
public class NginxAnnotatorFactory implements AnnotatorFactory {
    @Nullable
    @Override
    public Annotator createAnnotator() {
        return new NginxAnnotatingVisitor();
    }

    @Nonnull
    @Override
    public Language getLanguage() {
        return NginxLanguage.INSTANCE;
    }
}
