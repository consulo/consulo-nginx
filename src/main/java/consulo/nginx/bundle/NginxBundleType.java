package consulo.nginx.bundle;

import consulo.annotation.component.ExtensionImpl;
import consulo.component.extension.ExtensionInstance;
import consulo.content.bundle.*;
import consulo.fileChooser.FileChooserDescriptor;
import consulo.nginx.icon.NginxIconGroup;
import consulo.project.ProjectBundle;
import consulo.ui.image.Image;
import consulo.util.xml.serializer.XmlSerializer;
import consulo.virtualFileSystem.LocalFileSystem;
import consulo.virtualFileSystem.VirtualFile;
import net.ishchenko.idea.nginx.configurator.NginxServerDescriptor;
import net.ishchenko.idea.nginx.platform.NginxCompileParametersExtractor;
import net.ishchenko.idea.nginx.platform.PlatformDependentTools;
import org.jdom.Element;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.function.Supplier;

/**
 * @author VISTALL
 * @since 27/04/2023
 */
@ExtensionImpl
public class NginxBundleType extends SdkType {
    private static final Supplier<NginxBundleType> INSTANCE = ExtensionInstance.from(SdkType.class);

    public static NginxBundleType getInstance() {
        return INSTANCE.get();
    }

    public NginxBundleType() {
        super("NGINX");
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
            public void validateSelectedFiles(VirtualFile[] files) throws Exception {
                if (files.length != 0) {
                    final String selectedPath = files[0].getPath();
                    boolean valid = isValidSdkHome(selectedPath);
                    if (!valid) {
                        valid = isValidSdkHome(adjustSelectedSdkHome(selectedPath));
                        if (!valid) {
                            String message = files[0].isDirectory()
                                    ? ProjectBundle.message("sdk.configure.home.invalid.error", getPresentableName())
                                    : ProjectBundle.message("sdk.configure.home.file.invalid.error", getPresentableName());
                            throw new Exception(message);
                        }
                    }
                }
            }
        };
        descriptor.setTitle(ProjectBundle.message("sdk.configure.home.title", getPresentableName()));
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

    @Override
    public String suggestSdkName(String s, String path) {
        return getPresentableName() + " " + getVersionString(path);
    }

    @Nonnull
    @Override
    public String getPresentableName() {
        return "Nginx";
    }

    @Nonnull
    @Override
    public Image getIcon() {
        return NginxIconGroup.nginx();
    }
}
