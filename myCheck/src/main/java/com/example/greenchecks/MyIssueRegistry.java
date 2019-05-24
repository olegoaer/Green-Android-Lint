package com.example.greenchecks;
import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.example.greenchecks.checks.BLEDetector;
import com.example.greenchecks.checks.BatteryEfficientLocation;
import com.example.greenchecks.checks.DarkUIDetector;
import com.example.greenchecks.checks.DirtyBootDetector;
import com.example.greenchecks.checks.DurableWakeLockDetector;
import com.example.greenchecks.checks.EverlastingServiceDetector;
import com.example.greenchecks.checks.IITLDetector;
import com.example.greenchecks.checks.KeepCPUOn;
import com.example.greenchecks.checks.KeepScreenOn;
import com.example.greenchecks.checks.RigidAlarmDetector;
import com.example.greenchecks.checks.SensorCoalesceDetector;
import com.example.greenchecks.checks.SensorLeakDetector2;
import com.example.greenchecks.checks.UncompressedDataTransmissionDetector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//Cette classe contient les "issues" qui seront vérifiées
public class MyIssueRegistry extends IssueRegistry {

    public static final Category GREENNESS = Category.create("Greenness", 30);


    private static final List<Issue> sIssues;

    static {
        List<Issue> issues = new ArrayList<Issue>(13);

        issues.add(BatteryEfficientLocation.ISSUE_FUSED_API);
        issues.add(BLEDetector.ISSUE);
        issues.add(DarkUIDetector.ISSUE);
        issues.add(DirtyBootDetector.ISSUE);
        issues.add(DurableWakeLockDetector.ISSUE);
        issues.add(EverlastingServiceDetector.ISSUE);
        issues.add(IITLDetector.ISSUE);
        issues.add(KeepScreenOn.ISSUE);
        issues.add(KeepCPUOn.ISSUE);
        issues.add(RigidAlarmDetector.ISSUE);
        issues.add(SensorCoalesceDetector.ISSUE);
        issues.add(SensorLeakDetector2.ISSUE);
        issues.add(UncompressedDataTransmissionDetector.ISSUE);

        sIssues = Collections.unmodifiableList(issues);
    }



    @Override
    public List<Issue> getIssues() {
        return sIssues;
    }
}
