package net.ishchenko.idea.nginx.injection;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.file.FileTypeManager;
import consulo.language.file.LanguageFileType;
import consulo.document.util.TextRange;
import consulo.language.inject.InjectedLanguagePlaces;
import consulo.language.inject.LanguageInjector;
import consulo.language.psi.PsiLanguageInjectionHost;
import consulo.virtualFileSystem.fileType.FileType;
import net.ishchenko.idea.nginx.psi.NginxLuaContext;
import jakarta.annotation.Nonnull;

@ExtensionImpl
public class LuaInjector implements LanguageInjector {
    @Override
    public void injectLanguages(@Nonnull PsiLanguageInjectionHost host, @Nonnull InjectedLanguagePlaces injectionPlacesRegistrar) {
        if (host instanceof NginxLuaContext) {
            FileType luaFileType = FileTypeManager.getInstance().getFileTypeByExtension("lua");
            if (luaFileType instanceof LanguageFileType luaLangFileType) {
                int start = host.getText().indexOf("{") + 1;
                injectionPlacesRegistrar.addPlace(luaLangFileType.getLanguage(), new TextRange(start, host.getTextLength()), null, null);
            }
        }
    }
}
