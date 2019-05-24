package com.example.greenchecks.checks;

import com.android.tools.lint.client.api.JavaEvaluator;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.SourceCodeScanner;
import com.example.greenchecks.MyIssueRegistry;
import com.intellij.psi.PsiMethod;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.uast.UCallExpression;

import java.util.Arrays;
import java.util.List;

import static com.android.SdkConstants.CLASS_SERVICE;
import static com.android.SdkConstants.CLASS_CONTEXT;


public class EverlastingServiceDetector extends Detector implements SourceCodeScanner {


    public static final Issue ISSUE = Issue.create(
            // ID: utilisé dans les avertissements "warning" @SuppressLint etc
            "EverlastingService",

            //Titre -- montré dans le dialogue de préférences de l'IDE, comme en-tête de catégorie
            // dans la fenêtre de résultats de l'analyse, etc
            "You forgot to stop manually a running service",

            //Description complète de l'issue
            "You used `startService()` without a call to `stopService()` or `stopSelf()`",

            MyIssueRegistry.GREENNESS,
            6,
            Severity.ERROR,
            new Implementation(
                    EverlastingServiceDetector.class,
                    Scope.JAVA_FILE_SCOPE
            )
    );



    private boolean mStartServiceFound;
    private boolean mStopServiceFound;
    private Location mStartServiceLocation;

    @Override
    public void beforeCheckProject(@NotNull Context context) {
        mStartServiceFound = false;
        mStopServiceFound = false;
    }

    @Nullable
    @Override
    public List<String> getApplicableMethodNames() {
        return Arrays.asList("startService", "stopService", "stopSelf", "stopSelfResult");
    }


    @Override
    public void visitMethodCall(@NotNull JavaContext context, @NotNull UCallExpression node, @NotNull PsiMethod method) {

        JavaEvaluator eval = context.getEvaluator();

        if (!(eval.isMemberInSubClassOf(method, CLASS_CONTEXT, true)
                || !eval.isMemberInSubClassOf(method, CLASS_SERVICE, true))) return;

        String methodName = node.getMethodName();

        switch(methodName) {

            case "startService":
                mStartServiceFound = true;
                mStartServiceLocation = context.getLocation(node);
                break;
            case "stopService": case "stopSelf": case "stopSelfResult":
                mStopServiceFound = true;
                break;
        }

    }

    @Override
    public void afterCheckProject(Context context) {

        if (mStartServiceFound && !mStopServiceFound) {

            context.report(ISSUE,mStartServiceLocation,"This service is never stopped");

        }
    }

}
