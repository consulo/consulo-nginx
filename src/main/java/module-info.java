/**
 * @author VISTALL
 * @since 07/01/2023
 */
module consulo.nginx {
    requires consulo.ide.api;

    // TODO [VISTALL] remove it in future
    requires java.desktop;
    requires forms.rt;
    
    exports consulo.nginx;
    exports consulo.nginx.annotator;
    exports consulo.nginx.icon;
    exports net.ishchenko.idea.nginx;
    exports net.ishchenko.idea.nginx.annotator;
    exports net.ishchenko.idea.nginx.configurator;
    exports net.ishchenko.idea.nginx.formatter;
    exports net.ishchenko.idea.nginx.formatter.blocks;
    exports net.ishchenko.idea.nginx.injection;
    exports net.ishchenko.idea.nginx.lexer;
    exports net.ishchenko.idea.nginx.parser;
    exports net.ishchenko.idea.nginx.platform;
    exports net.ishchenko.idea.nginx.psi;
    exports net.ishchenko.idea.nginx.psi.impl;
    exports net.ishchenko.idea.nginx.run;
}