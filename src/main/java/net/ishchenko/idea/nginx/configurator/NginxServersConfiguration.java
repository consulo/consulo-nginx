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

package net.ishchenko.idea.nginx.configurator;

import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ServiceAPI;
import consulo.annotation.component.ServiceImpl;
import consulo.disposer.Disposable;
import consulo.application.Application;
import consulo.component.persist.PersistentStateComponent;
import consulo.component.persist.State;
import consulo.component.persist.Storage;
import consulo.virtualFileSystem.LocalFileSystem;
import consulo.virtualFileSystem.VirtualFile;
import consulo.util.xml.serializer.XmlSerializer;
import jakarta.inject.Singleton;
import org.jdom.Element;
import javax.annotation.Nullable;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 22.07.2009
 * Time: 15:33:19
 */
@Singleton
@State(name = "nginxServers", storages = @Storage("nginx.xml"))
@ServiceAPI(ComponentScope.APPLICATION)
@ServiceImpl
public class NginxServersConfiguration implements PersistentStateComponent<Element>, Disposable {
    private Set<NginxServerDescriptor> descriptors = new LinkedHashSet<>();
    private NginxServerDescriptor[] cachedDescriptors = null;
    private Map<String, Set<String>> cachedNameToPathsMapping;
    private Set<String> cachedFilepaths;

    public static NginxServersConfiguration getInstance() {
        return Application.get().getInstance(NginxServersConfiguration.class);
    }

    public synchronized NginxServerDescriptor[] getServersDescriptors() {
        if (cachedDescriptors == null) {
            cachedDescriptors = descriptors.toArray(new NginxServerDescriptor[descriptors.size()]);
            cachedNameToPathsMapping = extractNameToPaths();
            cachedFilepaths = extractFilepaths();
        }
        return cachedDescriptors;
    }

    public synchronized void addServerDescriptor(NginxServerDescriptor descriptor) {
        descriptors.add(descriptor);
        cachedDescriptors = null;
        cachedNameToPathsMapping = null;
        cachedFilepaths = null;
    }

    public synchronized void removeAllServerDescriptors() {
        descriptors.clear();
        cachedDescriptors = null;
        cachedNameToPathsMapping = null;
        cachedFilepaths = null;
    }

    @Override
    public void dispose() {

    }

    @Override
    public synchronized Element getState() {
        Element element = new Element("servers");
        for (final NginxServerDescriptor description : getServersDescriptors()) {
            element.addContent(XmlSerializer.serialize(description));
        }
        return element;
    }

    @Override
    public synchronized void loadState(final Element state) {
        removeAllServerDescriptors();
        for (final Object o : state.getChildren()) {
            Element element = (Element) o;
            addServerDescriptor(XmlSerializer.deserialize(element, NginxServerDescriptor.class));
        }
    }

    @Nullable
    public synchronized NginxServerDescriptor getDescriptorById(String id) {
        for (NginxServerDescriptor descriptor : descriptors) {
            if (descriptor.getId().equals(id)) {
                return descriptor;
            }
        }
        return null;
    }

    public synchronized Map<String, Set<String>> getNameToPathsMapping() {
        if (cachedNameToPathsMapping == null) {
            cachedNameToPathsMapping = extractNameToPaths();
        }
        return cachedNameToPathsMapping;
    }

    public synchronized Set<String> getFilepaths() {
        if (cachedFilepaths == null) {
            cachedFilepaths = extractFilepaths();
        }
        return cachedFilepaths;
    }

    public synchronized void rebuildFilepaths() {

        //very ugly, indeed

        cachedDescriptors = null;
        getServersDescriptors();

        cachedNameToPathsMapping = null;
        getNameToPathsMapping();

        cachedFilepaths = null;
        getFilepaths();

    }

    private Map<String, Set<String>> extractNameToPaths() {
        Map<String, Set<String>> result = new HashMap<>();
        for (NginxServerDescriptor descriptor : getServersDescriptors()) {
            VirtualFile vfile = LocalFileSystem.getInstance().findFileByPath(descriptor.getConfigPath());
            if (vfile != null) {
                VirtualFile parent = vfile.getParent();
                if (parent != null && parent.isDirectory()) {
                    recursiveAdd(result, parent, 0);
                }
            }
        }
        return result;
    }

    private Set<String> extractFilepaths() {
        Set<String> result = new HashSet<>();
        Map<String, Set<String>> nameToPathsMapping = getNameToPathsMapping();
        for (Map.Entry<String, Set<String>> entry : nameToPathsMapping.entrySet()) {
            result.addAll(entry.getValue());
        }
        return result;
    }

    private void recursiveAdd(Map<String, Set<String>> result, VirtualFile file, int level) {

        if (level > 2) {
            //only 2 inner folders, dude. that should be enough for most cases.
            //and it should be done the other way at all (parsing includes in conf)
            return;
        }
        if (file.isDirectory()) {
            for (VirtualFile child : file.getChildren()) {
                recursiveAdd(result, child, level + 1);
            }
        }
        else {
            String path = file.getPath();
            String name = path.substring(path.lastIndexOf('/') + 1);
            if (!result.containsKey(name)) {
                result.put(name, new HashSet<>());
            }
            result.get(name).add(path);
        }

    }
}