package com.example.greenchecks.checks;

import com.android.tools.lint.client.api.JavaEvaluator;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.GradleScanner;
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

import java.io.File;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public class BatteryEfficientLocation extends Detector implements SourceCodeScanner {

    //https://www.youtube.com/watch?v=81W61JA6YHw&index=7&list=PLWz5rJ2EKKc9CBxr3BVjPTPoDPLdPIFCE
    //1) LocationManager -- > FusedLocationProviderClient
    //2) crit.setPowerRequirement(Criteria.POWER_LOW);
    //3) LocationRequest .setPriority(PIORITY_LOW_POWER)
    //4) LocationRequest .setInterval()


    /*
     * CONSTANTES
     */
    private static final String GRADLE_DEPENDENCY = "com.google.android.gms:play-services-location:11.0.1";

    private static final String CLASS_STR_FUSED_LOCATION = "com.google.android.gms.location.FusedLocationProviderClient";
    private static final String CLASS_STR_LOCATIONMANAGER = "android.location.LocationManager";
    private static final String CLASS_STR_LOCATIONCRITERIA = "android.location.Criteria";
    private static final String CLASS_STR_FUSED_LOCATIONREQUEST = "com.google.android.gms.location.LocationRequest";

    private static final String METHOD_STR_LKL = "getLastKnownLocation";
    private static final String METHOD_STR_RLU = "requestLocationUpdates";
    private static final String METHOD_STR_SETPWREQ = "setPowerRequirement";
    private static final String METHOD_STR_SETPRIO = "setPriority";
    private static final String METHOD_STR_SETITVL = "setInterval";


    private static int SUGGESTED_PRIORITY_LOW_POWER = 104;

    /*
     * ERREURS A DETECTER
     */

    public static final Issue ISSUE_FUSED_API = Issue.create(
            "FusedLocationProvider",
            "Use Fused APIs (Google Play Service) for energy-efficient location",
            "The fused location provider is a location API in Google Play services that\n" +
                    " intelligently combines different signals to provide the location \n" +
                    "information that your app needs",

            MyIssueRegistry.GREENNESS,
            6,
            Severity.WARNING,
            new Implementation(
                    BatteryEfficientLocation.class,
                    Scope.JAVA_FILE_SCOPE
            )
    ).setAndroidSpecific(true)
     .addMoreInfo("https://developers.google.com/location-context/fused-location-provider/");


    public static final Issue ISSUE_POWERREQ = Issue.create(
            "ProviderPowerRequirement",
            "Choose a provider with a low power mode",
            "TODO",

            MyIssueRegistry.GREENNESS,
            6,
            Severity.INFORMATIONAL,
            new Implementation(
                    BatteryEfficientLocation.class,
                    Scope.JAVA_FILE_SCOPE
            )
    ).setAndroidSpecific(true);


    public static final Issue ISSUE_PRIORITY = Issue.create(
            "LocationRequestPriority",
            "Choose a low poser priority for your location request",
            "TODO",

            MyIssueRegistry.GREENNESS,
            6,
            Severity.INFORMATIONAL,
            new Implementation(
                    BatteryEfficientLocation.class,
                    Scope.JAVA_FILE_SCOPE
            )
    ).setAndroidSpecific(true);


    public static final Issue ISSUE_INTERVAL = Issue.create(
            "LocationRequestInterval",
            "Choose a short interval for your location request",
            "TODO",

            MyIssueRegistry.GREENNESS,
            6,
            Severity.INFORMATIONAL,
            new Implementation(
                    BatteryEfficientLocation.class,
                    Scope.JAVA_FILE_SCOPE
            )
    ).setAndroidSpecific(true);

    /*
     *
     *  IMPLEMENTATION DU DETECTEUR
     */

    @Nullable
    @Override
    public List<String> getApplicableMethodNames() {
        return Arrays.asList(
                METHOD_STR_LKL,
                METHOD_STR_RLU,
                METHOD_STR_SETPWREQ,
                METHOD_STR_SETPRIO,
                METHOD_STR_SETITVL
                );
    }

    @Override
    public void visitMethodCall(@NotNull JavaContext context, @NotNull UCallExpression node, @NotNull PsiMethod method) {

        JavaEvaluator jeval = context.getEvaluator();
        if (!(jeval.isMemberInClass(method, CLASS_STR_LOCATIONMANAGER)  ||
                jeval.isMemberInClass(method, CLASS_STR_LOCATIONCRITERIA)  ||
                jeval.isMemberInClass(method, CLASS_STR_FUSED_LOCATIONREQUEST)
        )) return;

        String methodName = method.getName();

        //CAS 1 : utiliser le nouveau FUSED API
        if (METHOD_STR_LKL.equals(methodName) && jeval.isMemberInClass(method, CLASS_STR_LOCATIONMANAGER)) {
                context.report(ISSUE_FUSED_API, node, context.getLocation(node),
                        "Call the new `FusedLocationProviderClient.getLastLocation()` instead");
        }
         if (METHOD_STR_RLU.equals(methodName) && jeval.isMemberInClass(method, CLASS_STR_LOCATIONMANAGER)) {
                context.report(ISSUE_FUSED_API, node, context.getLocation(node),
                        "Call the new `FusedLocationProviderClient.requestLocationUpdates()` instead");
        }

        //CAS 2 : changer le mode énergie du critère
        if (METHOD_STR_SETPWREQ.equals(methodName)) {

        }




    }


}