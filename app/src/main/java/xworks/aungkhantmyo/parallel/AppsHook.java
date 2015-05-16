package xworks.aungkhantmyo.parallel;

import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.widget.TextView;

import java.lang.reflect.Type;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import de.robv.android.xposed.callbacks.XCallback;

/**
 * Created by Aung Khant Myo on 2/22/15.
 */
public class AppsHook {


//    private static String getStr;

    public static boolean isInitialFontSet = false;
    public static FontHelper.FontType mFontType = new FontHelper.FontType();

    // default font [ single ]
    public static FontHelper.FontTypeDefault mFontTypeDefault= new FontHelper.FontTypeDefault();

//    public static FontHelper.FontType mFontType_Unicode = new FontHelper.FontType();
//    public static FontHelper.FontType mFontType_Zawgyi = new FontHelper.FontType();


    // all apps with non-multiple font support

    public static void handleAllApps(XC_LoadPackage.LoadPackageParam lpp, XSharedPreferences sMainPref) {


        sMainPref.reload();
        if(!sMainPref.contains(lpp.packageName))
            return;



        String fontDef = sMainPref.getString(Common.KEY_DEFAULT_FONT, Common.DEFAULT_FONT_ALL_APPS);
        mFontTypeDefault = FontHelper.parsedPrefDefault(ParallelMain.sModuleResources, fontDef, ParallelMain.sFontLoader);


        String fontString = sMainPref.getString(lpp.packageName, Common.DEFAULT_PARALLEL_FONTS_ALL_APPS);
        mFontType = FontHelper.parsedPref(ParallelMain.sModuleResources, fontString, ParallelMain.sFontLoader);

        hookTextView(lpp, XCallback.PRIORITY_LOWEST);
    }

    private static void hookTextView(XC_LoadPackage.LoadPackageParam lpp, int priority) {

        XposedBridge.hookAllConstructors(TextView.class, new XC_MethodHook(priority) {


            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (mFontType == null) return;
                isInitialFontSet = false;

                final TextView tv = ((TextView) param.thisObject);

                if(mFontTypeDefault.weight == Typeface.NORMAL){
                    tv.setTypeface(mFontTypeDefault.font);
                } else {
                    tv.setTypeface(mFontTypeDefault.font, mFontTypeDefault.weight);
                }
                Log.d("HOOKME:", "AppsHook.hookTextView.setTypeface called" );

                isInitialFontSet = true;
            }
        });

    }

    public static boolean handleLoadSystem(XC_LoadPackage.LoadPackageParam lpp, XSharedPreferences mainPref) {
        if (lpp.packageName.contains(Common.PACKAGE_ANDROID_SYSTEM) ||
                lpp.packageName.equals("com.android.systemui"))
        {
            mainPref.reload();
            if (!mainPref.contains(lpp.packageName)) return false;

            Log.d("HOOKME:", "AppsHook.handleSystem called" );

            // TODO: It looks different now

            String fontDef = mainPref.getString(Common.KEY_DEFAULT_FONT, Common.DEFAULT_FONT_ALL_APPS);
            mFontTypeDefault = FontHelper.parsedPrefDefault(ParallelMain.sModuleResources, fontDef, ParallelMain.sFontLoader);

            hookTextView(lpp, XCallback.PRIORITY_HIGHEST); // Its getting HOOOOOKKKKEEEDDD !
            return true;
        }
        return false;
    }

    public static boolean handleLoadApps(XC_LoadPackage.LoadPackageParam lpp, XSharedPreferences pref) {

        pref.reload();
        if (!pref.contains(lpp.packageName)) return false;

        String unparsed = pref.getString(lpp.packageName, Common.DEFAULT_PARALLEL_FONTS_ALL_APPS);

        mFontType = FontHelper.parsedPref(ParallelMain.sModuleResources, unparsed, ParallelMain.sFontLoader);
        Log.d("HOOKME:", "unparsedFont=" + unparsed + lpp.packageName);

//        String unicodeFont = pref.getString(Common.CURRENT_FONT_UNICODE, Common.DEFAULT_FONT_ALL_APPS);
//        String zawgyiFont = pref.getString(Common.CURRENT_FONT_ZAWGYI, Common.DEFAULT_FONT_ALL_APPS);

//        mFontType_Unicode = FontHelper.parsedPref(ParallelMain.sModuleResources, unicodeFont, ParallelMain.sFontLoader);
//        mFontType_Zawgyi = FontHelper.parsedPref(ParallelMain.sModuleResources, zawgyiFont, ParallelMain.sFontLoader);

//        Log.d("HOOKME:", String.format("mFontTypeUnicode: %s\nmFontTypeZawgyi: %s", mFontType_Unicode.toString(), mFontType_Zawgyi.toString()));

        hookTextView(lpp, XCallback.PRIORITY_HIGHEST);
        return true;

    }
}
