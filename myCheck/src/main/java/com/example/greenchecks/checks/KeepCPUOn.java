package com.example.greenchecks.checks;

import com.android.tools.lint.client.api.JavaEvaluator;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.SourceCodeScanner;
import com.example.greenchecks.MyIssueRegistry;
import com.intellij.psi.PsiMethod;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.uast.UCallExpression;

import java.util.Collections;
import java.util.List;


public class KeepCPUOn extends Detector implements SourceCodeScanner {
    public static final Issue ISSUE = Issue.create(
            "KeepCPUOn",
            "Device battery life will be affected by the use of wake lock",
            "Do not acquire wake locks unless you really need them. Use the minimum levels possible",

            MyIssueRegistry.GREENNESS,
            6,
            Severity.INFORMATIONAL,
            new Implementation(
                    KeepCPUOn.class,
                    Scope.JAVA_FILE_SCOPE
            )
    ).setAndroidSpecific(true);


    private static final String CLASS_STR_POWERMANAGER = "android.os.PowerManager";
    private static final String METHOD_STR_WAKELOCK = "newWakeLock";

    @Nullable
    @Override
    public List<String> getApplicableMethodNames() {
        return Collections.singletonList(METHOD_STR_WAKELOCK);
    }

    @Override
    public void visitMethodCall(@NotNull JavaContext context, @NotNull UCallExpression node, @NotNull PsiMethod method) {

        JavaEvaluator jeval = context.getEvaluator();
        if (!jeval.isMemberInClass(method, CLASS_STR_POWERMANAGER)) return;

        context.report(ISSUE, node, context.getLocation(node), "Using wake lock is not a good idea");

    }

}
