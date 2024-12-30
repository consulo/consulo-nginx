package net.ishchenko.idea.nginx;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.cacheBuilder.WordsScanner;
import consulo.language.findUsage.FindUsagesProvider;
import consulo.language.psi.PsiElement;
import net.ishchenko.idea.nginx.psi.NginxInnerVariable;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

@ExtensionImpl
public class NginxFindUsagesProvider implements FindUsagesProvider {

    @Nullable
    @Override
    public WordsScanner getWordsScanner() {
        return new NginxWordsScanner();
    }

    @Override
    public boolean canFindUsagesFor(@Nonnull PsiElement psiElement) {
        return psiElement instanceof NginxInnerVariable;
    }

    @Nonnull
    @Override
    public String getType(@Nonnull PsiElement psiElement) {
        if (psiElement instanceof NginxInnerVariable) return "variable";
        return "";
    }

    @Nonnull
    @Override
    public String getDescriptiveName(@Nonnull PsiElement psiElement) {
        if (psiElement instanceof NginxInnerVariable) {
            NginxInnerVariable var = (NginxInnerVariable)psiElement;
            return var.getName();
        }
        return "";
    }

    @Nonnull
    @Override
    public String getNodeText(@Nonnull PsiElement psiElement, boolean b) {
        if (psiElement instanceof NginxInnerVariable) {
            NginxInnerVariable var = (NginxInnerVariable)psiElement;
            return var.getName();
        }
        return "";
    }

    @Nonnull
    @Override
    public Language getLanguage() {
        return NginxLanguage.INSTANCE;
    }
}
