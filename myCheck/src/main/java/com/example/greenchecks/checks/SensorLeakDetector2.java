package com.example.greenchecks.checks;

import com.android.annotations.NonNull;
import com.android.tools.lint.client.api.JavaEvaluator;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.example.greenchecks.MyIssueRegistry;
import com.intellij.psi.PsiMethod;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.uast.UCallExpression;
import org.jetbrains.uast.UMethod;
import org.jetbrains.uast.UastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public class SensorLeakDetector2 extends Detector implements Detector.UastScanner {


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
                    SensorLeakDetector2.class,
                    EnumSet.of(Scope.JAVA_FILE, Scope.RESOURCE_FILE)
            )
    )
            .addMoreInfo("https://developer.android.com/reference/android/hardware/SensorManager")
            .setAndroidSpecific(true);


    private static final String REGISTER_METHOD_NAME = "registerListener";
    private static final String UNREGISTER_METHOD_NAME = "unregisterListener";
    private static final String SENSORMANAGER_CLASS_NAME = "android.hardware.SensorManager";

    private ArrayList<String> mSensorListenerRegistered;
    private ArrayList<String> mSensorListenerUnregistered;





    /** List {@code registerListener()} calls have been encountered */
    private ArrayList<UCallExpression> registrationCalls;
    /** List {@code unregisterListener()} calls have been encountered */
    private ArrayList<UCallExpression> unregistrationCalls;
    private JavaContext callContext;


    @Override
    public void beforeCheckRootProject(@NonNull Context context) {
        registrationCalls = new ArrayList<UCallExpression>();
        unregistrationCalls = new ArrayList<UCallExpression>();
    }


    @Nullable
    @Override
    public List<String> getApplicableMethodNames() {
        return Arrays.asList(REGISTER_METHOD_NAME, UNREGISTER_METHOD_NAME);
    }


    @Override
    public void visitMethodCall(@NotNull JavaContext context, @NotNull UCallExpression node, @NotNull PsiMethod method) {

        JavaEvaluator jeval = context.getEvaluator();
        callContext = context;

        if (!jeval.isMemberInClass(method, SENSORMANAGER_CLASS_NAME)) return;

        if (REGISTER_METHOD_NAME.equals(method.getName())) {
            registrationCalls.add(node);
        }

        if (UNREGISTER_METHOD_NAME.equals(method.getName())) {
            unregistrationCalls.add(node);
        }

    }

    /*

     c'est plus subtil que cela en fait car il faut vérifier que la classe qui implemente
     un SensorListener (premier argument de registerListener) est la même qu'au moment de
     unregisterListener(premier et seul argment)

     */

    @Override
    public void afterCheckRootProject(@NonNull Context context) {

        if (!registrationCalls.isEmpty() && unregistrationCalls.isEmpty()) {
            for(UCallExpression n: registrationCalls) {
                context.report(ISSUE, callContext.getLocation(n), "The unregistration of this sensor is missing");
            }
        } else if (!registrationCalls.isEmpty() && !unregistrationCalls.isEmpty()) {
            //Returns the immediate parent of this object, or null.
            UMethod enclosingMethod = UastUtils.getParentOfType(unregistrationCalls.get(0), false, UMethod.class);
            if (!enclosingMethod.getName().equals("onPause")) {
                context.report(ISSUE, callContext.getLocation(unregistrationCalls.get(0)), "The unregistration of this sensor is misplaced");

            }
        }
    }



}
