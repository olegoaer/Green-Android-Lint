package com.example.greenchecks.checks;

import com.android.tools.lint.client.api.JavaEvaluator;
import com.android.tools.lint.detector.api.ConstantEvaluator;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.SourceCodeScanner;
import com.android.tools.lint.detector.api.XmlContext;
import com.example.greenchecks.MyIssueRegistry;
import com.intellij.psi.PsiMethod;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.uast.UCallExpression;
import org.jetbrains.uast.UExpression;
import org.w3c.dom.Attr;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.android.SdkConstants.ANDROID_URI;

public class KeepScreenOn extends Detector implements SourceCodeScanner, Detector.XmlScanner {
    public static final Issue ISSUE = Issue.create(
            "KeepScreenOn",
            "You should avoid to keep the screen on",
            "As a rule of thumb, keeping the device awake instead of letting it falling asleep is a bad practice",

            MyIssueRegistry.GREENNESS,
            6,
            Severity.INFORMATIONAL,
            new Implementation(
                    KeepScreenOn.class,
                    Scope.JAVA_AND_RESOURCE_FILES
            )
    ).setAndroidSpecific(true);



    /*
     *  CODE DU DETECTEUR
     */

    private boolean mHasScreenOnFlag = false;
    private boolean mHasScreenOnAttribute = false;

    private static final String METHOD_STR_WINFLAG = "addFlags";
    private static final String CLASS_STR_WIN = "android.view.Window";
    private static final String ATTR_SCREENON = "keepScreenOn";


    @Nullable
    @Override
    public Collection<String> getApplicableAttributes() {
        return Collections.singletonList(ATTR_SCREENON);
    }

    @Nullable
    @Override
    public List<String> getApplicableMethodNames() {
        return Collections.singletonList(METHOD_STR_WINFLAG);
    }

    /*
        Cas de la version déclarative xml
     */
    @Override
    public void visitAttribute(@NotNull XmlContext context, @NotNull Attr attribute) {

        // Make sure this is really one of the android: attributes
        if (!ANDROID_URI.equals(attribute.getNamespaceURI())) {
            return;
        }

        String value = attribute.getValue();
        if (!value.isEmpty() && value.equals("true")) {

            Location location = context.getLocation(attribute);
            context.report(ISSUE, attribute, location, "Turn this value to `false`");
        }



    }


    /*
        Cas de la version programmatique
     */

    @Override
    public void visitMethodCall(@NotNull JavaContext context, @NotNull UCallExpression node, @NotNull PsiMethod method) {

        JavaEvaluator jeval = context.getEvaluator();
        if (!jeval.isMemberInClass(method, CLASS_STR_WIN)) return;

        UExpression varg = node.getValueArguments().get(0);  //premier paramètre
        Object value =  ConstantEvaluator.evaluate(context, varg);
        int Ivalue = Integer.parseInt(value.toString());

        if (Ivalue == 128) {
            context.report(ISSUE, varg, context.getLocation(varg), "The flag `FLAG_KEEP_SCREEN_ON` is dangerous");
        }


    }
}
