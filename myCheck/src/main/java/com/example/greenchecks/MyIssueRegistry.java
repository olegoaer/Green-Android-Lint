package com.example.greenchecks;
import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//Cette classe contient les "issues" qui seront vérifiées
public class MyIssueRegistry extends IssueRegistry {

    public static final Category GREENNESS = Category.create("Greenness", 30);


    private static final List<Issue> sIssues;

    static {
        List<Issue> issues = new ArrayList<Issue>(12);

        issues.add(BatteryEfficientLocationDetector.ISSUE);
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

        sIssues = Collections.unmodifiableList(issues);
    }



    @Override
    public List<Issue> getIssues() {
        return sIssues;
    }
}
