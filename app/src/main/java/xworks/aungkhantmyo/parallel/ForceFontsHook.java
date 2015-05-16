package xworks.aungkhantmyo.parallel;

import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by Aung Khant Myo on 2/22/15.
 */
public class ForceFontsHook {

    private static String GetText;

    static FontHelper.FontType mFontType;
    static FontHelper.FontTypeDefault mFontTypeDefault;

//    static FontHelper.FontType mFontType_Unicode;
//    static FontHelper.FontType mFontType_Zawgyi;

    public static boolean handleLoad(XC_LoadPackage.LoadPackageParam lpp, XSharedPreferences sForcePref, XSharedPreferences enabled_pref) {

        enabled_pref.reload();
        if (!enabled_pref.contains(lpp.packageName)) return false;

        mFontType = AppsHook.mFontType;
        mFontTypeDefault = AppsHook.mFontTypeDefault;

//        String unicodeFont = enabled_pref.getString(Common.CURRENT_FONT_UNICODE, Common.DEFAULT_FONT_ALL_APPS);
//        String zawgyiFont = enabled_pref.getString(Common.CURRENT_FONT_ZAWGYI, Common.CURRENT_FONT_ZAWGYI);

//        mFontType_Unicode = AppsHook.mFontType_Unicode;
//        mFontType_Zawgyi = AppsHook.mFontType_Zawgyi;


//        sForcePref.reload();
//        if (sForcePref.contains(lpp.packageName)) return false;
        hookTextView(lpp);



        return true;
    }


    private static void hookTextView(XC_LoadPackage.LoadPackageParam lpp) {

        XposedHelpers.findAndHookMethod(TextView.class, "setText", CharSequence.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {


//                Typeface padauk = Typeface.createFromAsset(ParallelMain.sModuleResources.getAssets(), "fonts/Myanmar3.ttf" );
//                Typeface zg = Typeface.createFromAsset(ParallelMain.sModuleResources.getAssets(), "fonts/SmartZawgyiPro.ttf");

                final TextView tv = (TextView)param.thisObject;

                GetText = tv.getText().toString();
                Log.d("HOOKME: ", GetText);

                SpannableStringBuilder SS = new SpannableStringBuilder(GetText);

                // Zawgyi

                int zg_startpos =  GetText.indexOf("[zawgyi]");
                int zg_endpos = GetText.indexOf("[/zawgyi]");


                Log.d("HOOKME: ", String.format("\n\nZG_START: %d\n\nEND: %d\n\n", zg_startpos, zg_endpos));
                // Unicode
                int uni_start = GetText.indexOf("[unicode]");
                int uni_end   = GetText.indexOf("[/unicode]");

                Log.d("HOOKME: ", String.format("\n\nUNI_START: %d\n\nEND: %d\n\n",uni_start, uni_end));


                if ( zg_startpos == -1 && zg_endpos == -1 && uni_start == -1 && uni_end == -1 )
                {
                    return;
                }

                if ( zg_startpos != -1 && zg_endpos != -1  ) {
                    SS.setSpan(new CustomTypefaceSpan("", mFontType.font_zawgyi), zg_startpos, zg_endpos, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                }

                if ( uni_start != -1 && uni_end != -1 ) {
                    SS.setSpan(new CustomTypefaceSpan("", mFontType.font_unicode), uni_start, uni_end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                }

                Log.d("HOOKME:", "ForceFontsHook.hookTextView.setText done" );
                tv.setText(SS);
            }
        });

        XposedHelpers.findAndHookMethod(TextView.class, "setTypeface", Typeface.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                param.args[0] = mFontTypeDefault.font;
                param.args[1] = mFontTypeDefault.weight;
//                param.args[0] = Typeface.createFromAsset(ParallelMain.sModuleResources.getAssets(), "fonts/Padauk.ttf");
//                param.args[1] = Typeface.createFromAsset(ParallelMain.sModuleResources.getAssets(), "fonts/Padauk.ttf").getStyle();

            }
        });

        XposedHelpers.findAndHookMethod(TextView.class, "setTypeface", Typeface.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                param.args[0] = mFontTypeDefault.font;

//                param.args[0] = Typeface.createFromAsset(ParallelMain.sModuleResources.getAssets(), "fonts/Padauk.ttf");
            }
        });
    }

    public static boolean handleLoadAllApps(XC_LoadPackage.LoadPackageParam lpp, XSharedPreferences force_pref, XSharedPreferences enabled_pref) {

        enabled_pref.reload();
        if (!enabled_pref.contains(Common.PACKAGE_ANDROID_SYSTEM)) return false;
        force_pref.reload();
        if (!force_pref.contains(Common.PACKAGE_ANDROID_SYSTEM)) return false;
        Log.d("HOOKME:", "AppsHook.handleLoadAllApps called" );
        mFontType = AppsHook.mFontType;
        mFontTypeDefault = AppsHook.mFontTypeDefault;
        hookTextView(lpp);
//        mFontType_Zawgyi = AppsHook.mFontType_Zawgyi;
//        mFontType_Unicode= AppsHook.mFontType_Unicode;


        return true;
    }
}
