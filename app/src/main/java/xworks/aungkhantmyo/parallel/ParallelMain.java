package xworks.aungkhantmyo.parallel;


import android.content.res.XModuleResources;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.TextView;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by Aung Khant Myo on 2/21/15.
 */

public class ParallelMain implements IXposedHookZygoteInit, IXposedHookLoadPackage {

    final public XModuleResources res = null;


    public static XSharedPreferences sMainPref;
    public static XSharedPreferences sAppPref;
    public static XModuleResources sModuleResources;
    public static XSharedPreferences sForcePref;
    public static FontLoader sFontLoader;
    public static boolean sEnableAllApps;
    public static String MODULE_PATH = null;


    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpp) throws Throwable {


        if (sEnableAllApps) {
            AppsHook.handleAllApps(lpp, sMainPref);
        }
        // MIUI Hook Method

        if("com.android.thememanager".equals(lpp.packageName)){
            Log.d("HOOKME:", "I'm in MIUI !!");
            MiHook.hook(lpp);
        }

        boolean hooked;

        if (lpp.packageName.equals(Common.PACKAGE_ANDROID_SYSTEM) ||
                lpp.packageName.equals("com.android.systemui") || lpp.processName.equals("system_process") || lpp.packageName.equals("com.android.settings")) {

            hooked = AppsHook.handleLoadSystem(lpp, sMainPref);       // TODO: For System UI

            ForceFontsHook.handleLoad(lpp, sForcePref, sMainPref);  // TODO: its handle as Other Apps ( FORCE_METHOD )


        } else {             // MARK: If not in SystemUI

            hooked = AppsHook.handleLoadApps(lpp, sAppPref);         // TODO: Its for specified apps   ( APP_HOOK )

            ForceFontsHook.handleLoad(lpp, sForcePref, sAppPref);  // TODO: its handle as other apps ( FORCE_METHOD )
        }

        if ( sEnableAllApps && !hooked ) { // TODO: If upper methods not completed or hooked, call handleLoadAllApps ( FORCE_METHOD )

            ForceFontsHook.handleLoadAllApps(lpp, sForcePref, sAppPref);

        }

    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {


        sMainPref = new XSharedPreferences(Common.PACKAGE_PER_FONTS, Common.PEFERENCE_MAIN);
        sAppPref = new XSharedPreferences(Common.PACKAGE_PER_FONTS, Common.PEFERENCE_APP);
        sForcePref = new XSharedPreferences(Common.PACKAGE_PER_FONTS, Common.PEFERENCE_FORCE);

        MODULE_PATH = startupParam.modulePath;
        sModuleResources = XModuleResources.createInstance(MODULE_PATH, null);

        sFontLoader = new FontLoader(sMainPref);
        sEnableAllApps = sMainPref.getBoolean(Common.KEY_ENABLE_ALL_APPS, false);

        // MIUI INIT
        MiHook.drmManager();
    }
}
