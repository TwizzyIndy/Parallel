package xworks.aungkhantmyo.parallel;

import android.os.Environment;

/**
 * Created by Aung Khant Myo on 2/22/15.
 */
public class Common {


    public static final String PACKAGE_PER_FONTS = Common.class.getPackage().getName();
    public static final String PEFERENCE_MAIN = "main" ;
    public static final String PACKAGE_ANDROID_SYSTEM = "android";

    public static final String KEY_FONT_ANDROID_SYSTEM = PACKAGE_ANDROID_SYSTEM;

    public static final String KEY_DEFAULT_FONT = "default_font";

    public static final String KEY_FOLDER_FONT = "folder_font";

    public static final String KEY_ENABLE_ALL_APPS = "pref_enable_allapps";
    public static final String KEY_ISCONVERTERON = "pref_isconverteron";

    public static final String SETTINGS_PREFIX_FONT_ASSET = "@asset/";

    public static final String SETTINGS_SPLIT_SYMBOL = ";";
    public static final String SETTINGS_SPLIT_AND = ",";

    public static final String DEFAULT_FONT_ALL_APPS = SETTINGS_PREFIX_FONT_ASSET + "0" + SETTINGS_SPLIT_SYMBOL + "0";

    public static final String DEFAULT_PARALLEL_FONTS_ALL_APPS = SETTINGS_PREFIX_FONT_ASSET + "0" +
                                                                 SETTINGS_SPLIT_SYMBOL + "0" +
                                                                 SETTINGS_SPLIT_AND +
                                                                 SETTINGS_PREFIX_FONT_ASSET + "0" +
                                                                 SETTINGS_SPLIT_SYMBOL + "0";


    public static final String PEFERENCE_APP = "app_settings";
    public static final String PEFERENCE_FORCE = "force_fonts";
    public static final String DEFAULT_FOLDER_FONT = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ParallelFonts/";

    public static final String CURRENT_FONT_UNICODE = "pref_font_unicode";
    public static final String CURRENT_FONT_ZAWGYI  = "pref_font_zawgyi";

    public static final String SETTINGS_SUFFIX_INCOMPATIBLE = "@incompatible_font";
    public static final int SETTINGS_INDEX_FONT = 0;
    public static final int SETTINGS_INDEX_WEIGHT = 1;


}
