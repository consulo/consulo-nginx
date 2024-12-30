package net.ishchenko.idea.nginx.injection;

import consulo.document.util.TextRange;
import consulo.language.psi.LiteralTextEscaper;
import net.ishchenko.idea.nginx.psi.NginxLuaContext;
import jakarta.annotation.Nonnull;

public class LuaTextEscaper extends LiteralTextEscaper<NginxLuaContext> {
    public LuaTextEscaper(@Nonnull NginxLuaContext host) {
        super(host);
    }

    @Override
    public boolean decode(@Nonnull TextRange rangeInsideHost, @Nonnull StringBuilder outChars) {
        outChars.append(myHost.getText(), rangeInsideHost.getStartOffset(), rangeInsideHost.getEndOffset());
        return true;
    }

    @Override
    public int getOffsetInHost(int offsetInDecoded, @Nonnull TextRange rangeInsideHost) {
        int j = offsetInDecoded + rangeInsideHost.getStartOffset();
        if (j < rangeInsideHost.getStartOffset()) {
            j = rangeInsideHost.getStartOffset();
        }
        if (j > rangeInsideHost.getEndOffset()) {
            j = rangeInsideHost.getEndOffset();
        }
        return j;
    }

    @Override
    public boolean isOneLine() {
        return false;
    }
}
