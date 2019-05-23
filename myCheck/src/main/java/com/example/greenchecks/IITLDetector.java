package com.example.greenchecks;

import com.android.tools.lint.client.api.JavaEvaluator;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Detector.UastScanner;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.intellij.psi.PsiMethod;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.uast.UCallExpression;
import org.jetbrains.uast.ULoopExpression;
import org.jetbrains.uast.UastUtils;

import java.lang.String;
import java.util.Collections;
import java.util.List;

public class IITLDetector extends Detector implements UastScanner {
    //Issue décrivant le problème et pointant vers l'implementation du détecteur

    private final String HTTPURLCONN_CLS = "java.net.URL";

    public static final Issue ISSUE = Issue.create(
        // ID: utilisé dans les avertissements "warning" @SuppressLint etc
        "InternetInTheLoop",

        //Titre -- montré dans le dialogue de préférences de l'IDE, comme en-tête de catégorie
        // dans la fenêtre de résultats de l'analyse, etc
        "Making http call in a loop statement is energy greedy",

        //Description complète de l'issue
        "TODO",

            MyIssueRegistry.GREENNESS,
        6,
        Severity.ERROR,
        new Implementation(
            IITLDetector.class,
            Scope.JAVA_FILE_SCOPE
        )
    );

    @Nullable
    @Override
    public List<String> getApplicableMethodNames() {
        return Collections.singletonList("openConnection");
    }

    @Override
    public void visitMethodCall(@NotNull JavaContext context, @NotNull UCallExpression node, @NotNull PsiMethod method) {
        JavaEvaluator jeval = context.getEvaluator();

        if (!jeval.isMemberInClass(method, HTTPURLCONN_CLS)) {
            return;
        }

        boolean insideLoop = false;


        if (UastUtils.getParentOfType(node, true, ULoopExpression.class) != null) {
            insideLoop = true;
        }

        if (insideLoop) {

            context.report(ISSUE, node, context.getLocation(node), "Should use a push method instead");

        }
    }


}