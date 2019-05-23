package com.example.greenchecks;

import com.android.tools.lint.client.api.JavaEvaluator;
import com.android.tools.lint.detector.api.ConstantEvaluator;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.LintFix;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.SourceCodeScanner;
import com.intellij.psi.PsiMethod;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.uast.UCallExpression;
import org.jetbrains.uast.UExpression;

import java.util.Collections;
import java.util.List;

public class SensorCoalesceDetector extends Detector implements SourceCodeScanner {

    public static final Issue ISSUE = Issue.create(
            // ID: utilisé dans les avertissements "warning" @SuppressLint etc
            "SensorCoalesce",

            //Titre -- montré dans le dialogue de préférences de l'IDE, comme en-tête de catégorie
            // dans la fenêtre de résultats de l'analyse, etc
            "Events should be kept temporarily before being delivered",

            //Description complète de l'issue
            "The method registerListener(SensorEventListener, Sensor, int, int) allows events"+
                    "to be stored in the hardware queue (FIFO) up to maxReportLatencyUs. Setting maxReportLatencyUs"
                    + " to a positive value allows to reduce the number of interrupts the AP receives, hence " +
                    "reducing power consumption.",

            MyIssueRegistry.GREENNESS,
            6,
            Severity.WARNING,
            new Implementation(
                    SensorCoalesceDetector.class,
                    Scope.JAVA_FILE_SCOPE
            )
    )
            .addMoreInfo("https://developer.android.com/reference/android/hardware/SensorManager.html#registerListener(android.hardware.SensorEventListener,%20android.hardware.Sensor,%20int,%20int)")
            .setAndroidSpecific(true);


    private static final String REGISTER_METHOD = "registerListener";
    private static final String SENSORMANAGER_CLS = "android.hardware.SensorManager";

    @Nullable
    @Override
    public List<String> getApplicableMethodNames() {
        return Collections.singletonList(REGISTER_METHOD);
    }

    @Override
    public void visitMethodCall(@NotNull JavaContext context, @NotNull UCallExpression node, @NotNull PsiMethod method) {
        JavaEvaluator jeval = context.getEvaluator();

        if (!jeval.isMemberInClass(method, SENSORMANAGER_CLS)) return;

        if (REGISTER_METHOD.equals(method.getName()) && method.getParameterList().getParametersCount()==3) {

            String replace = "registerListener(SensorEventListener, Sensor, int, int)";
            String old = "registerListener(SensorEventListener, Sensor, int)";
            String message = String.format("Should use `%1$s` instead of `%2$s`", replace, old);

            LintFix fix = fix()
                    .name("Replace with " + replace)
                    .replace()
                    .text(node.asSourceString())
                    .with(node.asSourceString().replaceFirst(".$","") +  ", 20000)")
                    .build();

            context.report(ISSUE, node.getMethodIdentifier(), context.getLocation(node), message, fix);

        } else if (REGISTER_METHOD.equals(method.getName()) && method.getParameterList().getParametersCount()==4) {

            UExpression varg = node.getValueArguments().get(3);
            Object value =  ConstantEvaluator.evaluate(context, varg);
            long Lvalue = Long.parseLong(value.toString());

            if (Lvalue == 0L) {
                context.report(ISSUE, varg, context.getLocation(varg), "This parameter must be set to a positive value");
            }

        }
    }
}
