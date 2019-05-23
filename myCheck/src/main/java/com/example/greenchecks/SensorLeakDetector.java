package com.example.greenchecks;

import com.android.tools.lint.client.api.JavaEvaluator;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.intellij.psi.PsiMethod;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.uast.UCallExpression;
import org.jetbrains.uast.UElement;


import java.util.Arrays;
import java.util.List;

public class SensorLeakDetector extends Detector implements Detector.UastScanner {


    public static final Issue ISSUE = Issue.create(
            // ID: utilisé dans les avertissements "warning" @SuppressLint etc
            "SensorLeak",

            //Titre -- montré dans le dialogue de préférences de l'IDE, comme en-tête de catégorie
            // dans la fenêtre de résultats de l'analyse, etc
            "You did not unregister a sensor properly",

            //Description complète de l'issue
            "Always make sure to disable sensors you don't need, especially when your activity is paused. Failing to do so can drain the battery in just a few hours. Note that the system will not disable sensors automatically when the screen turns off",

            MyIssueRegistry.GREENNESS,
            6,
            Severity.ERROR,
            new Implementation(
                    SensorLeakDetector.class,
                    Scope.JAVA_FILE_SCOPE
            )
    )
            .addMoreInfo("https://developer.android.com/reference/android/hardware/SensorManager")
            .setAndroidSpecific(true);



    private UElement registrationCallNode = null;
    private UElement unregistrationCallNode = null;
    private JavaContext jctx;
    private Location registrationCallLocation;
    private Location unregistrationCallLocation;

    private final String SENSORMANAGER_CLS = "android.hardware.SensorManager";


    @Nullable
    @Override
    public List<String> getApplicableMethodNames() {
        return Arrays.asList("registerListener", "unregisterListener");
    }

    @Override
    public void visitMethod(JavaContext context, UCallExpression node, PsiMethod method) {
        JavaEvaluator eval = context.getEvaluator();
        jctx = context;

        if (!eval.isMemberInClass(method, SENSORMANAGER_CLS)) {
            return;
        }


        if (node.getMethodName().equals("registerListener")) {

                registrationCallNode = node;
                registrationCallLocation = context.getLocation(registrationCallNode);


        } else if(node.getMethodName().equals("unregisterListener")) {

                unregistrationCallNode = node;
                unregistrationCallLocation = context.getLocation(unregistrationCallNode);
                //unregistrationCallLocation.setSecondary(registrationCallLocation);  LinkedLocation

        }


    }


    @Override
    public void afterCheckFile(Context context) {

        if (hasForgottenUnregister()) {
            jctx.report(ISSUE, registrationCallNode, registrationCallLocation, "The unregistration of this sensor is missing");
        }

        super.afterCheckFile(context);
    }

    private boolean hasForgottenUnregister() {
        return registrationCallNode!=null && unregistrationCallNode==null;
    }


    private boolean hasMisplacedUnregister() {

        return true;

    }
}
