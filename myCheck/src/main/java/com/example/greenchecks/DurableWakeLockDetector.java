package com.example.greenchecks;

import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;

public class DurableWakeLockDetector extends Detector implements Detector.UastScanner, Detector.XmlScanner {

    public static final Issue ISSUE = Issue.create(
            "DurableWakeLock",
            "TODO",
            "TODO`",

            MyIssueRegistry.GREENNESS,
            6,
            Severity.WARNING,
            new Implementation(
                    DurableWakeLockDetector.class,
                    Scope.JAVA_AND_RESOURCE_FILES
            )
    ).setAndroidSpecific(true);
}
