/*
 * Copyright 2009 Max Ishchenko
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.ishchenko.idea.nginx;

import consulo.annotation.component.ExtensionImpl;
import consulo.ide.navigation.GotoFileContributor;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiManager;
import consulo.navigation.NavigationItem;
import consulo.project.Project;
import consulo.virtualFileSystem.LocalFileSystem;
import consulo.virtualFileSystem.VirtualFile;
import jakarta.inject.Inject;
import net.ishchenko.idea.nginx.configurator.NginxServersConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 21.07.2009
 * Time: 18:29:04
 */
@ExtensionImpl
public class NginxChooseByNameContributor implements GotoFileContributor {

    private NginxServersConfiguration configuration;

    @Inject
    public NginxChooseByNameContributor(NginxServersConfiguration configuration) {
        this.configuration = configuration;
    }

    public NavigationItem[] getItemsByName(String name, String pattern, Project project, boolean includeNonProjectItems) {

        LocalFileSystem localFileSystem = LocalFileSystem.getInstance();
        PsiManager psiManager = PsiManager.getInstance(project);
        List<PsiFile> result = new ArrayList<>();
        Map<String, Set<String>> names = configuration.getNameToPathsMapping();
        Set<String> fullPathsForName = names.get(name);
        if (fullPathsForName != null) {
            for (String s : fullPathsForName) {
                VirtualFile vfile = localFileSystem.findFileByPath(s);
                if (vfile != null) {
                    result.add(psiManager.findFile(vfile));
                }
            }
        }
        return result.toArray(new NavigationItem[result.size()]);

    }

    public String[] getNames(Project project, boolean includeNonProjectItems) {
        Set<String> names = configuration.getNameToPathsMapping().keySet();
        return names.toArray(new String[names.size()]);
    }
}
