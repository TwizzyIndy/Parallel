package xworks.aungkhantmyo.parallel;

import android.content.SharedPreferences;
import android.graphics.Typeface;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

import de.robv.android.xposed.XSharedPreferences;

/**
 * Created by Aung Khant Myo on 2/25/15.
 */
public class FontLoader {
    public Map<String, Typeface> map = new HashMap<String, Typeface>();

    public FontLoader(XSharedPreferences pref) {
        getFonts(pref.getString(Common.KEY_FOLDER_FONT, Common.DEFAULT_FOLDER_FONT));

    }

    private void getFonts(String folder_string) {
        File folder = new File(folder_string);

        if (!folder.exists()) {
            if(!folder.mkdir()) {
                return;
            }
        }
        if (!folder.isDirectory())
            return;

        File[] file_array = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String fileName) {
                if (fileName.endsWith(".ttf")) {
                    return true;
                }
                return false;
            }
        });

        for ( File file : file_array ) {
            try {
                map.put(file.getName(), Typeface.createFromFile(file));

            } catch ( Exception e ){
                map.put(file.getName() + Common.SETTINGS_SUFFIX_INCOMPATIBLE, null);

            }
        }
    }

    public FontLoader(SharedPreferences pref) {
        getFonts(pref.getString(Common.KEY_FOLDER_FONT, Common.DEFAULT_FOLDER_FONT));

    }

    public Typeface findFont(String fontName ){
        if ( map == null ){
            return null;
        }
        return map.get(fontName);

    }
}
