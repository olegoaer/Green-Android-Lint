package com.example.greenchecks;

import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Project;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.SourceCodeScanner;
import com.intellij.psi.PsiMethod;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.uast.UCallExpression;

import java.util.List;

public class RigidAlarmDetector extends Detector implements SourceCodeScanner {
    public static final Issue ISSUE = Issue.create(
            "RigidAlarm",
            "TODO",
            "TODO`",

            MyIssueRegistry.GREENNESS,
            6,
            Severity.WARNING,
            new Implementation(
                    RigidAlarmDetector.class,
                    Scope.JAVA_AND_RESOURCE_FILES
            )
            ).setAndroidSpecific(true);


    private static final int OLD_API = 19;

    @Nullable
    @Override
    public List<String> getApplicableMethodNames() {
        return super.getApplicableMethodNames();
    }


    @Override
    public void visitMethodCall(@NotNull JavaContext context, @NotNull UCallExpression node, @NotNull PsiMethod method) {

        Project project = context.getMainProject();
        int targetSdk = project.getTargetSdk();





    }
}




