/**
 * @author VISTALL
 * @since 07/01/2023
 */
module consulo.nginx {
    requires consulo.ide.api;

    requires consulo.application.api;
    requires consulo.application.content.api;
    requires consulo.code.editor.api;
    requires consulo.color.scheme.api;
    requires consulo.component.api;
    requires consulo.configurable.api;
    requires consulo.disposer.api;
    requires consulo.document.api;
    requires consulo.execution.api;
    requires consulo.execution.impl;
    requires consulo.file.chooser.api;
    requires consulo.language.api;
    requires consulo.language.impl;
    requires consulo.language.editor.api;
    requires consulo.language.code.style.api;
    requires consulo.localize.api;
    requires consulo.logging.api;
    requires consulo.navigation.api;
    requires consulo.platform.api;
    requires consulo.process.api;
    requires consulo.project.api;
    requires consulo.ui.api;
    requires consulo.ui.ex.api;
    requires consulo.ui.ex.awt.api;
    requires consulo.virtual.file.system.api;
    requires consulo.util.collection;
    requires consulo.util.dataholder;
    requires consulo.util.io;
    requires consulo.util.lang;
    requires consulo.util.xml.serializer;

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