package com.example.greenchecks.checks;

import com.android.tools.lint.client.api.UElementHandler;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.SourceCodeScanner;
import com.example.greenchecks.MyIssueRegistry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.uast.UElement;
import org.jetbrains.uast.UFile;
import org.jetbrains.uast.UImportStatement;

import java.util.Collections;
import java.util.List;


public class BLEDetector extends Detector implements SourceCodeScanner {

    private static final String ERROR_MESSAGE = "You are using Bluetooh. Did you take a look at the Bluetooth Low Energy API?";
    private static final String IMPORT_STR_CLASSIC_B = "android.bluetooth";
    private static final String IMPORT_STR_BLE = "android.bluetooth.le";


    public static final Issue ISSUE = Issue.create(
            "BluetoothLowEnergy",
            "Use the Low Energy Bluetooth API",
            "In contrast to Classic Bluetooth, Bluetooth Low Energy (BLE) is designed"
                        + "to provide significantly lower power consumption",

            MyIssueRegistry.GREENNESS,
            6,
            Severity.INFORMATIONAL,
            new Implementation(
                    BLEDetector.class,
                    Scope.JAVA_FILE_SCOPE
            )
    ).setAndroidSpecific(true)
      .addMoreInfo("https://developer.android.com/guide/topics/connectivity/bluetooth-le");



    @Override
    public List<Class<? extends UElement>> getApplicableUastTypes() {
        return Collections.singletonList(UFile.class);
    }

    @Override
    public UElementHandler createUastHandler(JavaContext context) {
        return new UElementHandler() {
            @Override
            public void visitFile(@NotNull UFile node) {

                boolean mHasBluetoothImport = false;
                boolean mHasBLEImport = false;

                 for(UImportStatement i : node.getImports()){
                     if(i.toString().contains(IMPORT_STR_CLASSIC_B)){
                         mHasBluetoothImport = true;
                     }
                     if(i.toString().contains(IMPORT_STR_BLE)){
                         mHasBLEImport = true;
                     }
                }

                if(mHasBluetoothImport && !mHasBLEImport){
                    context.report(ISSUE, Location.create(context.file), ERROR_MESSAGE);
                }
            }
        };
    }
}
