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
import com.android.tools.lint.detector.api.SourceCodeScanner;
import com.android.tools.lint.detector.api.XmlContext;
import com.intellij.psi.PsiMethod;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.uast.UCallExpression;
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UastUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import static com.android.SdkConstants.ANDROID_URI;
import static com.android.SdkConstants.ATTR_NAME;
import static com.android.SdkConstants.CLASS_CONTEXT;
import static com.android.SdkConstants.TAG_ACTION;
import static com.android.SdkConstants.TAG_INTENT_FILTER;
import static com.android.SdkConstants.TAG_RECEIVER;
import static com.android.utils.XmlUtils.getFirstSubTagByName;
import static com.android.utils.XmlUtils.getSubTagsByName;


public class DirtyBootDetector extends Detector implements SourceCodeScanner, Detector.XmlScanner {
    public static final Issue ISSUE = Issue.create(
            "DirtyBoot",
            "Do not start services at boot time",
            "TODO",

            MyIssueRegistry.GREENNESS,
            6,
            Severity.WARNING,
            new Implementation(
                    DirtyBootDetector.class,
                    EnumSet.of(Scope.MANIFEST, Scope.JAVA_FILE)
            )
    ).setAndroidSpecific(true);


    private String mReceiverDeclaredAtBootime;
    private Location mReceiverDeclaredAtBootimeLocation;

    private String mReceiverStartingService;
    private Location mStartingServiceLocation;


    @Nullable
    @Override
    public Collection<String> getApplicableElements() {
        return Collections.singletonList(TAG_RECEIVER);
    }

    @Nullable
    @Override
    public List<String> getApplicableMethodNames() {
        return Arrays.asList("startService");
    }

    @Override
    public void visitElement(@NotNull XmlContext context, @NotNull Element element) {

        Attr classNameNode = element.getAttributeNodeNS(ANDROID_URI, ATTR_NAME);
        if (classNameNode == null) return;

        Element filter = getFirstSubTagByName(element, TAG_INTENT_FILTER);

        if (filter == null) return;

        boolean isRegisteredOnBoot = false;

        for (Element action: getSubTagsByName(filter,TAG_ACTION)) {

            String actionName = action.getAttributeNS(ANDROID_URI, ATTR_NAME);
            if ("android.intent.action.BOOT_COMPLETED".equals(actionName)) {
                isRegisteredOnBoot = true;
                mReceiverDeclaredAtBootimeLocation = context.getLocation(action);
            }
        }

        if (!isRegisteredOnBoot) return;

        String className = classNameNode.getValue();  //classe d'implementation du receiver
        String pkg = context.getMainProject().getPackage();

        if (className.isEmpty()) return;
        String fqcn;

        int dotIndex = className.indexOf('.');
        if (dotIndex <= 0) {

            if (pkg == null) return;
            if (dotIndex == 0) {
                fqcn = pkg + className;
            } else {
                fqcn = pkg + '.' + className;
            }


        } else {
            fqcn = className;
        }

        //on a un nom de classe de receiver
        mReceiverDeclaredAtBootime = fqcn;


    }

    @Override
    public void visitMethodCall(@NotNull JavaContext context, @NotNull UCallExpression node, @NotNull PsiMethod method) {

        JavaEvaluator jeval = context.getEvaluator();
        if (!jeval.isMemberInClass(method, CLASS_CONTEXT)) return;


        UClass containingClass = UastUtils.getContainingUClass(node);
        mReceiverStartingService = containingClass.getQualifiedName();
        mStartingServiceLocation = context.getLocation(node);

    }


    @Override
    public void afterCheckProject(@NotNull Context context) {


        if (mReceiverDeclaredAtBootime.equals(mReceiverStartingService)) {

            mStartingServiceLocation.setSecondary(mReceiverDeclaredAtBootimeLocation); //endroits liés

            context.report(ISSUE, mStartingServiceLocation, "Suspicious service at boot time");
        }

    }
}
