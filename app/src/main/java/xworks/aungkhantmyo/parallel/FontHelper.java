package xworks.aungkhantmyo.parallel;

import android.content.res.Resources;
import android.content.res.XModuleResources;
import android.graphics.Typeface;
import android.util.Log;

/**
 * Created by Aung Khant Myo on 2/25/15.
 */
public class FontHelper {


    // Fonts Index in Assets Folder

    private static final int INDEX_FONT_DEFAULT = 0;
    private static final int INDEX_FONT_ZAWGYI = 1;
    private static final int INDEX_FONT_PADAUK = 2;
    private static final int INDEX_FONT_MM3    = 3;
    private static final int INDEX_MAX = INDEX_FONT_MM3 + 1;

    // Font Paths and names

    private static final String ASSET_FONT_FOLDER = "fonts/";
    private static final String ASSET_FONT_ZAWGYI = ASSET_FONT_FOLDER + "SmartZawgyiPro.ttf";
    private static final String ASSET_FONT_PADAUK = ASSET_FONT_FOLDER + "Padauk.ttf";
    private static final String ASSET_FONT_MM3    = ASSET_FONT_FOLDER + "Myanmar3.ttf";


    // Get fonts list from asset folder

    public static String[] getAssetFontNames(Resources res){

        String[] str = new String[INDEX_MAX];
        str[INDEX_FONT_DEFAULT] = res.getString(R.string.font_default);
        str[INDEX_FONT_ZAWGYI]  = res.getString(R.string.font_zawgyi);
        str[INDEX_FONT_PADAUK]  = res.getString(R.string.font_padauk);
        str[INDEX_FONT_MM3]     = res.getString(R.string.font_mm3);
        return str;
    }

    public static String parseFontSyntaxIntoName(Resources res, String unparsed ) {
        if( unparsed.startsWith(Common.SETTINGS_PREFIX_FONT_ASSET )) {

            unparsed = unparsed.substring(Common.SETTINGS_PREFIX_FONT_ASSET.length());
            return getAssetFontNames(res)[Integer.parseInt(unparsed)];

        } else {
            // Fonts not from @asset will be given names
            return unparsed;
        }
    }

    public static Typeface getFontFromAssets(Resources res, int index) {
        switch (index) {

            case INDEX_FONT_ZAWGYI:
                return Typeface.createFromAsset(res.getAssets(), ASSET_FONT_ZAWGYI);
            case INDEX_FONT_PADAUK:
                return Typeface.createFromAsset(res.getAssets(), ASSET_FONT_PADAUK);
            case INDEX_FONT_MM3:
                return Typeface.createFromAsset(res.getAssets(), ASSET_FONT_MM3);
        }
        return null;
    }

    public static Typeface getFontFromAssets(Resources res, String index) {
        return getFontFromAssets(res, Integer.parseInt(index));
    }

    public static Typeface parseSettingsFontSyntax(Resources res, String unparsed, FontLoader loader) {

        if (unparsed.startsWith(Common.SETTINGS_PREFIX_FONT_ASSET)) {
            unparsed = unparsed.substring(Common.SETTINGS_PREFIX_FONT_ASSET.length());
            return getFontFromAssets(res, unparsed);
        } else {
            return loader.findFont(unparsed);
        }
    }

    public static FontType parsedPref(Resources res, String unparsedApp, FontLoader loader) {
        String[] pString = unparsedApp.split(Common.SETTINGS_SPLIT_AND); // " | "

        Log.d("HOOKME:", String.format("FontHelper.parsedPref->pString[0]=%s\npString[1]=%s", pString[0], pString[1]));

        String[] string_zawgyi = pString[0].split(Common.SETTINGS_SPLIT_SYMBOL); // Zawgyi
        String[] string_unicode= pString[1].split(Common.SETTINGS_SPLIT_SYMBOL); // Unicode


        Log.d("HOOKME:", String.format("FontHelper.parsedPref\nunparsedApp=%s",unparsedApp));
        Log.d("HOOKME:", String.format("FontHelper.parsedPref->pString[0]=%s\npString[1]=%s\n", pString[0], pString[1]));
        FontType fType = new FontType();
        Log.d("HOOKME:", String.format("FontHelper.parsedPref->weight_zawgyi=%d\nweight_unicode=%d\n", Integer.parseInt(string_zawgyi[1]), Integer.parseInt(string_unicode[1])));

        fType.font_zawgyi = parseSettingsFontSyntax(res, string_zawgyi[Common.SETTINGS_INDEX_FONT], loader);

        fType.font_unicode= parseSettingsFontSyntax(res, string_unicode[Common.SETTINGS_INDEX_FONT], loader);

        fType.weight_zawgyi = Integer.parseInt(string_zawgyi[Common.SETTINGS_INDEX_WEIGHT]);
        fType.weight_unicode= Integer.parseInt(string_unicode[Common.SETTINGS_INDEX_WEIGHT]);

        return fType;
    }


    public static String parseSettingsFontSyntaxIntoName(Resources resources, String raw_font_name) {

        if (raw_font_name.startsWith(Common.SETTINGS_PREFIX_FONT_ASSET)){

//            Log.d("HOOKME:", String.format("FontHelper.parseSettingsFontSyntaxIntoName: %s", raw_font_name.substring(Common.SETTINGS_PREFIX_FONT_ASSET.length())));

            raw_font_name = raw_font_name.substring(Common.SETTINGS_PREFIX_FONT_ASSET.length());

            return getAssetFontNames(resources)[Integer.parseInt(raw_font_name)];
        } else {
            return raw_font_name;
        }
    }

    public static FontTypeDefault parsedPrefDefault(Resources resources, String unparsed, FontLoader loader) {

        String[] str = unparsed.split(Common.SETTINGS_SPLIT_SYMBOL);
        FontTypeDefault fontType = new FontTypeDefault();
        fontType.font = parseSettingsFontSyntax(resources, str[Common.SETTINGS_INDEX_FONT], loader );
        fontType.weight = Integer.parseInt(str[Common.SETTINGS_INDEX_WEIGHT]);

        return fontType;
    }

    public static class FontTypeDefault {
        public Typeface font;
        public int weight;
    }
    public static class FontType {
        public Typeface font_zawgyi;
        public Typeface font_unicode;

        public int weight_zawgyi;
        public int weight_unicode;
    }
}
