package com.example.greenchecks.checks;

import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.SourceCodeScanner;
import com.example.greenchecks.MyIssueRegistry;

public class DurableWakeLockDetector extends Detector implements SourceCodeScanner {

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
