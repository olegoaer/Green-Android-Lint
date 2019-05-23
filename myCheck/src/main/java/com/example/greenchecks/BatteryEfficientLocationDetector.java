package com.example.greenchecks;

import com.android.tools.lint.client.api.UElementHandler;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;

import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.SourceCodeScanner;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.uast.UElement;
import org.jetbrains.uast.UFile;
import org.jetbrains.uast.UImportStatement;

import java.util.Collections;
import java.util.List;

public class BatteryEfficientLocationDetector extends Detector implements SourceCodeScanner{

    private static final String ERROR_MESSAGE = "Use Fused APIs for location tracking";
    private static final String IMPORT_STR_FUSED_LOCATION = "com.google.android.gms.location";
    private static final String IMPORT_STR_LOCATION = "android.location";

    private boolean mHasLocationImport = false;
    private boolean mHasFusedLocationImport = false;
    private Location mLastLocationImport;

    public static final Issue ISSUE = Issue.create(
            "BatteryEfficientLocation",
            "Use battery-friendly APIs for location tracking",
            "Monitoring location changes is a very battery-intensive task when\\n\" +\n" +
                    "                    \"done in the regular way, while there exist optimized solution",

            MyIssueRegistry.GREENNESS,
            6,
            Severity.INFORMATIONAL,
            new Implementation(
                    BatteryEfficientLocationDetector.class,
                    Scope.JAVA_FILE_SCOPE
            )
    ).setAndroidSpecific(true);



    @Override
    public List<Class<? extends UElement>> getApplicableUastTypes() {
        return Collections.singletonList(UFile.class);
    }

    @Override
    public UElementHandler createUastHandler(JavaContext context) {
        return new UElementHandler() {
            @Override
            public void visitFile(@NotNull UFile node) {

                for (UImportStatement i : node.getImports()) {
                    if (i.toString().contains(IMPORT_STR_LOCATION)) {
                        mHasLocationImport = true;
                        mLastLocationImport = context.getLocation(i);
                    }
                    if (i.toString().contains(IMPORT_STR_FUSED_LOCATION)) {
                        mHasFusedLocationImport = true;
                    }
                }

                if (mHasLocationImport && !mHasFusedLocationImport) {
                    context.report(ISSUE, node, mLastLocationImport, ERROR_MESSAGE);
                }
            }
        };
    }
}