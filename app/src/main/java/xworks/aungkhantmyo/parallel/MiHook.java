package xworks.aungkhantmyo.parallel;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.InputStream;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by Aung Khant Myo on 2/26/15.
 */
public class MiHook {

    private static final String DRMRESULT = "miui.drm.DrmManager$DrmResult";
    private static final String MODULE = "MiThemes";
    private static ClassLoader mClassLoader;

    public MiHook() {
        super();

    }

    private static void authSystem() {
        try{
            Class.forName("miui.resourcebrowser.controller.online.DrmService", false, MiHook.mClassLoader);
            XposedHelpers.findAndHookMethod("miui.resourcebrowser.controller.online.DrmService", MiHook.mClassLoader,
                    "isLegal", new Object[] {"miui.resourcebrowser.model.Resource",
                            new MiHook$8()});

        } catch (NoSuchMethodError noSuchMethodError){

        } catch (ClassNotFoundException classNotFound) {

        }

        try{
            Class.forName("miui.resourcebrowser.view.ResourceOperationHandler", false, MiHook.mClassLoader);
            XposedHelpers.findAndHookMethod("miui.resourcebrowser.view.ResourceOperationHandler",
                    MiHook.mClassLoader, "isLegal",
                    new Object[]{ new MiHook$9() } );

        } catch (NoSuchMethodError noSuchMethodError){

        } catch (ClassNotFoundException classNotFound){

        }

        try{
            Class.forName("miui.resourcebrowser.view.ResourceOperationHandler",
                    false, MiHook.mClassLoader);
            XposedHelpers.findAndHookMethod("miui.resourcebrowser.view.ResourceOperationHandler",
                    MiHook.mClassLoader, "onCheckResourceRightEventBeforeRealApply",
                    new Object[] { new MiHook$10() });

        } catch (NoSuchMethodError noSuchMethodError){

        } catch (ClassNotFoundException classNotFound){

        }

        try {
            Class.forName("miui.resourcebrowser.view.ResourceOperationHandler", false,
                    MiHook.mClassLoader);
            XposedHelpers.findAndHookMethod("miui.resourcebrowser.view.ResourceOperationHandler",
                    MiHook.mClassLoader, "checkResourceRights",
                    new Object[] { new MiHook$11() } );
        } catch (NoSuchMethodError noSuchMethodError){

        } catch (ClassNotFoundException classNotFound) {

        }

    }


    public static void drmManager() {
        MiHook.findAndHookMethod("miui.drm.DrmManager", "isLegal" , new Object[] { Context.class, String.class,
        "miui.drm.DrmManager$RightObject", new MiHook$1() } );

        MiHook.findAndHookMethod("miui.drm.DrmManager", "isLegal", new Object[] {
                Context.class, File.class, new MiHook$2()
        });

        MiHook.findAndHookMethod("miui.drm.DrmManager", "isLegal", new Object[] {
                Context.class, File.class, new MiHook$3()
        });

        MiHook.findAndHookMethod("miui.drm.DrmManager", "isRightsFileLegal", new Object[] {
                File.class, new MiHook$4()
        });

        MiHook.findAndHookMethod("miui.drm.DrmManager", "isLegal", new Object[] {
                File.class , File.class, new MiHook$5()
        });

        MiHook.findAndHookMethod("miui.drm.DrmManager", "isLegal", new Object[] {
                String.class, File.class, new MiHook$6()
        });
    }

    private static void findAndHookMethod(String packageName, String method, Object[] objects){

        try {
            XposedHelpers.findAndHookMethod(Class.forName(packageName), method, objects);
        }
        catch (NoSuchMethodError noSuchMethodError){
            Log.e("Paralle", "Method not found");
        }
        catch (ClassNotFoundException e) {
            Log.e("Paralle", "Class not found");
        }
        catch (Throwable throwable){
            Log.e("Paralle", "Throwable");
        }

    }

    public static Object getDrmResultSuccess() {
        Enum myEnum = null;

        try {
            Class myClass = Class.forName("miui.drm.DrmManager$DrmResult");
            if ( myClass == null ){
                Object object = null;
                return myEnum;
            }

            myEnum = Enum.valueOf(myClass, "DRM_SUCCESS");
            return myEnum;
        }
        catch (NoSuchMethodError e){
            Log.e("Paralle", "Method not found");
        } catch (ClassNotFoundException x ){
            Log.e("Paralle", "Class not found");
        } catch (Throwable t){
            Log.e("Paralle", "Throwable ....... get skipping");
        }

        return myEnum;
    }

    public static void hook(XC_LoadPackage.LoadPackageParam lpp) {

        MiHook.mClassLoader = lpp.classLoader;
        MiHook.authSystem();

    }

    private static void notifyPurchaseSuccess() {
        try{
            XposedHelpers.findAndHookMethod("miui.resourcebrowser.model.Resource",
                    MiHook.mClassLoader, "isProductBought", new Object[] {
                            XC_MethodReplacement.returnConstant(Boolean.valueOf(true))
                    });
        } catch (NoSuchMethodError e){

        } catch (Exception x){

        }

        try {
            XposedHelpers.findAndHookMethod("miui.resourcebrowser.model.ResourceOnlineProperties",
                    MiHook.mClassLoader, "setProductBought", new Object[] {
                            Boolean.TYPE, new MiHook$12()
                    });
        } catch (NoSuchMethodError e){

        } catch (Exception x) {

        }

        try {
            XposedHelpers.findAndHookMethod("miui.resourcebrowser.model.ResourceOnlineProperties",
                    MiHook.mClassLoader, "isProductBought", new Object[] {
                            XC_MethodReplacement.returnConstant(Boolean.valueOf(true))
                    });
        } catch (NoSuchMethodError no){

        } catch (Exception x){

        }
    }

    private static void paymentSystem() {

        try {
            Class.forName("miui.resourcebrowser.view.ResourceOperationHandler", false, MiHook.mClassLoader);
            XposedHelpers.findAndHookMethod("miui.resourcebrowser.view.ResourceOperationHandler",
                    MiHook.mClassLoader, "isAuthorizedResource", new Object[] {
                            XC_MethodReplacement.returnConstant(Boolean.valueOf(true))
                    });
        } catch (NoSuchMethodError e){

        } catch (ClassNotFoundException x ){

        }

        try {
            Class.forName("miui.resourcebrowser.controller.online.NetworkHelper", false, MiHook.mClassLoader);
            XposedHelpers.findAndHookMethod("miui.resourcebrowser.controller.online.NetworkHelper",
                    MiHook.mClassLoader, "validateResponseResult", new Object[]{
                            Integer.TYPE, InputStream.class, new MiHook$7()
                    });
        } catch (NoSuchMethodError no){

        } catch (ClassNotFoundException x){

        }
    }

    private static class MiHook$8 extends XC_MethodReplacement {

        MiHook$8() {
            super();
        }
        @Override
        protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
            return Boolean.valueOf(true);
        }
    }

    private static class MiHook$9 extends XC_MethodReplacement {

        MiHook$9() {
            super();
        }

        @Override
        protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
            return Boolean.valueOf(true);
        }
    }

    private static class MiHook$10 extends XC_MethodHook {
        MiHook$10() {
            super();
        }

        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

            XposedHelpers.setBooleanField(param.thisObject, "mIsLegal", true);

        }
    }

    private static class MiHook$11 extends XC_MethodHook {

        MiHook$11() {
            super();
        }

        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            XposedHelpers.setBooleanField(param.thisObject, "mIsLegal", true);
        }
    }

    private static class MiHook$1 extends XC_MethodHook {

        MiHook$1 () {
            super();

        }

        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            Object obj1 = param.getResult();
            Object obj2 = MiHook.getDrmResultSuccess();

            if ( obj2 != null ){
                param.setResult(obj2);

            } else if((obj1 instanceof Boolean)) {
                param.setResult(true);
            }
        }
    }

    private static class MiHook$2 extends XC_MethodHook {

        MiHook$2 () {
            super();
        }

        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            Object object = param.getResult();
            Object object1= MiHook.getDrmResultSuccess();

            if ( object1 != null ){
                param.setResult(object1);
                return;
            }

            if((object instanceof Boolean)){
                param.setResult(true);
            }
        }
    }

    private static class MiHook$3 extends XC_MethodHook {

        MiHook$3() {
            super();
        }

        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {

            Object object = param.getResult();
            Object object1= MiHook.getDrmResultSuccess();

            if ( object1 != null ){
                param.setResult(object1);
                return;
            }
            if( (object instanceof Boolean)){
                param.setResult(true);
            }

        }
    }

    private static class MiHook$4 extends XC_MethodHook {

        MiHook$4() {
            super();
        }

        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {

            if((param.getResult() instanceof Boolean)) {
                param.setResult(true);
            }
        }
    }

    private static class MiHook$5 extends XC_MethodHook {

        MiHook$5() {
            super();
        }

        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {

            if((param.getResult() instanceof Boolean)) {
                param.setResult(true);
            }
        }
    }

    private static class MiHook$6 extends XC_MethodHook {

        MiHook$6() {
            super();
        }

        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            if((param.getResult() instanceof Boolean)){
                param.setResult(true);
            }
        }
    }

    private static class MiHook$12 extends XC_MethodReplacement {

        MiHook$12() {
            super();
        }

        @Override
        protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {

            Object[] array_object = new Object[] {
                    Boolean.valueOf(true)
            };
            methodHookParam.args = array_object;

            return XposedBridge.invokeOriginalMethod(methodHookParam.method, methodHookParam.thisObject,
                    array_object);
        }
    }

    private static class MiHook$7 extends XC_MethodHook {

        MiHook$7() {
            super();
        }

        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            param.setResult(param.args[1]);
        }
    }
}
