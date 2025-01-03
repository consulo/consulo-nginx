package net.ishchenko.idea.nginx;

import consulo.language.cacheBuilder.DefaultWordsScanner;
import consulo.language.ast.TokenSet;
import net.ishchenko.idea.nginx.lexer.NginxElementTypes;
import net.ishchenko.idea.nginx.lexer.NginxParsingLexer;

public class NginxWordsScanner extends DefaultWordsScanner {

    public NginxWordsScanner() {
        super(new NginxParsingLexer(),
                TokenSet.create(NginxElementTypes.INNER_VARIABLE),
                NginxElementTypes.COMMENTS,
                TokenSet.create(NginxElementTypes.DIRECTIVE)
        );
    }

}
