package consulo.nginx;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.editor.highlight.SingleLazyInstanceSyntaxHighlighterFactory;
import consulo.language.editor.highlight.SyntaxHighlighter;
import net.ishchenko.idea.nginx.NginxLanguage;
import net.ishchenko.idea.nginx.lexer.NginxSyntaxHighlighter;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 06/12/2021
 */
@ExtensionImpl
public class NginxSyntaxHighlighterFactory extends SingleLazyInstanceSyntaxHighlighterFactory {
  @Nonnull
  @Override
  protected SyntaxHighlighter createHighlighter() {
    return new NginxSyntaxHighlighter();
  }

  @Nonnull
  @Override
  public Language getLanguage() {
    return NginxLanguage.INSTANCE;
  }
}
