package com.example.greenchecks.checks;

import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.BinaryResourceScanner;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LintFix;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.ResourceContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.XmlContext;
import com.android.tools.lint.detector.api.XmlScanner;
import com.example.greenchecks.MyIssueRegistry;
import com.example.greenchecks.utils.ColorHelper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Element;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import javax.imageio.ImageIO;

import static com.android.SdkConstants.ATTR_PARENT;

public class DarkUIDetector extends Detector implements XmlScanner, BinaryResourceScanner {

    public static final Issue ISSUE = Issue.create(
            // ID: utilisé dans les avertissements "warning" @SuppressLint etc
            "DarkUI",

            //Titre -- montré dans le dialogue de préférences de l'IDE, comme en-tête de catégorie
            // dans la fenêtre de résultats de l'analyse, etc
            "Provide a UI with dark background colors",

            //Description complète de l'issue
            "Dark theme is particularly beneficial for mobile devices with AMOLED screens, which are more energy efficient when displaying dark colors",

            MyIssueRegistry.GREENNESS,
            6,
            Severity.WARNING,
            new Implementation(
                    DarkUIDetector.class,
                    Scope.RESOURCE_FILE_SCOPE
            )


    ).setAndroidSpecific(true);


    @Nullable
    @Override
    public Collection<String> getApplicableElements() {
        return Arrays.asList("style");
    }

    @Override
    public void visitElement(@NotNull XmlContext context, @NotNull Element element) {

        String themeName = element.getAttribute(ATTR_PARENT);

        if (themeName.toLowerCase().contains("light")) {

                LintFix fix = fix().name("Replace with a darker theme").replace()
                        .text(themeName).with("android:Theme.Holo").build();

                context.report(ISSUE, context.getLocation(element.getAttributeNode(ATTR_PARENT)), "Light theme is not recommended", fix);
        }

    }

    @Override
    public boolean appliesTo(@NotNull ResourceFolderType folderType) {
        return ResourceFolderType.DRAWABLE==folderType || ResourceFolderType.MIPMAP==folderType;
    }

    @Override
    public void checkBinaryResource(@NotNull ResourceContext context) {
        String filename = context.file.getName();
        Location fileLocation = Location.create(context.file);
        try {
            BufferedImage targetImage = ImageIO.read(context.file);

            Double bright = ColorHelper.brightness(targetImage);
            context.report(ISSUE, fileLocation, "The average value of this image is" + String.valueOf(bright));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
