package com.example.greenchecks.checks;

import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.SourceCodeScanner;
import com.example.greenchecks.MyIssueRegistry;

public class UncompressedDataTransmissionDetector extends Detector implements SourceCodeScanner {

    public static final Issue ISSUE = Issue.create(
            "UncompressedDataTransmission",
            "Data must be compressed before being sent over the network",
            "",

            MyIssueRegistry.GREENNESS,
            6,
            Severity.WARNING,
            new Implementation(
                    UncompressedDataTransmissionDetector.class,
                    Scope.JAVA_FILE_SCOPE
            )
    ).setAndroidSpecific(true);
}
