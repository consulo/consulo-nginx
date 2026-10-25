package consulo.nginx.bundle;

import consulo.annotation.component.ExtensionImpl;
import consulo.component.extension.ExtensionInstance;
import consulo.content.bundle.*;
import consulo.fileChooser.FileChooserDescriptor;
import consulo.localize.LocalizeValue;
import consulo.nginx.icon.NginxIconGroup;
import consulo.project.localize.ProjectLocalize;
import consulo.util.xml.serializer.XmlSerializer;
import consulo.virtualFileSystem.LocalFileSystem;
import consulo.virtualFileSystem.VirtualFile;
import consulo.util.io.FileUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import net.ishchenko.idea.nginx.configurator.NginxServerDescriptor;
import net.ishchenko.idea.nginx.platform.NginxCompileParametersExtractor;
import net.ishchenko.idea.nginx.platform.PlatformDependentTools;
import org.jdom.Element;

import java.util.function.Supplier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * @author VISTALL
 * @since 2023-04-27
 */
@ExtensionImpl
public class NginxBundleType extends SdkType {
    private static final Supplier<NginxBundleType> INSTANCE = ExtensionInstance.from(SdkType.class);

    public static NginxBundleType getInstance() {
        return INSTANCE.get();
    }

    public NginxBundleType() {
        super("NGINX", LocalizeValue.localizeTODO("Nginx"), NginxIconGroup.nginx());
    }

    @Override
    public boolean isValidSdkHome(String path) {
        return PlatformDependentTools.getInstance().checkExecutable(path);
    }

    @Nullable
    @Override
    public String getVersionString(String path) {
        try {
            return NginxCompileParametersExtractor.extract(path).getVersion();
        }
        catch (PlatformDependentTools.ThisIsNotNginxExecutableException ignored) {
        }
        return null;
    }

    @Nonnull
    @Override
    public FileChooserDescriptor getHomeChooserDescriptor() {
        final FileChooserDescriptor descriptor = new FileChooserDescriptor(true, false, false, false, false, false) {
            @Override
            public void validateSelectedPaths(List<Path> paths) throws Exception {
                if (!paths.isEmpty()) {
                    final String selectedPath = FileUtil.toSystemIndependentName(paths.get(0).toString());
                    boolean valid = isValidSdkHome(selectedPath);
                    if (!valid) {
                        valid = isValidSdkHome(adjustSelectedSdkHome(selectedPath));
                        if (!valid) {
                            LocalizeValue message = Files.isDirectory(paths.get(0))
                                    ? ProjectLocalize.sdkConfigureHomeInvalidError(getDisplayName())
                                    : ProjectLocalize.sdkConfigureHomeFileInvalidError(getDisplayName());
                            throw new Exception(message.get());
                        }
                    }
                }
            }
        };
        descriptor.withTitle(ProjectLocalize.sdkConfigureHomeTitle(getDisplayName()));
        return descriptor;
    }

    @Override
    public void setupSdkPaths(@Nonnull SdkModificator sdkModificator) {
        try {
            VirtualFile path = LocalFileSystem.getInstance().findFileByPath(sdkModificator.getHomePath());

            NginxServerDescriptor descriptor = PlatformDependentTools.getInstance().createDescriptorFromFile(path);

            sdkModificator.setSdkAdditionalData(descriptor);
        }
        catch (PlatformDependentTools.ThisIsNotNginxExecutableException ignored) {
        }
    }

    @Nullable
    @Override
    public AdditionalDataConfigurable createAdditionalDataConfigurable(SdkModel sdkModel, SdkModificator sdkModificator) {
        return new ServerFieldsForm2((NginxServerDescriptor) sdkModificator.getSdkAdditionalData());
    }

    @Override
    public void saveAdditionalData(SdkAdditionalData additionalData, Element additional) {
        XmlSerializer.serializeInto(additionalData, additional);
    }

    @Nullable
    @Override
    public SdkAdditionalData loadAdditionalData(Sdk currentSdk, Element additional) {
        return XmlSerializer.deserialize(additional, NginxServerDescriptor.class);
    }
}
