package xworks.aungkhantmyo.parallel;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class ParallelSetting extends PreferenceActivity {
    /**
     * Determines whether to always show the simplified settings UI, where
     * settings are presented in a single list. When false, settings are shown
     * as a master/detail two-pane view on tablets. When true, a single pane is
     * shown on tablets.
     */
    private static final boolean ALWAYS_SIMPLE_PREFS = false;


    static FontAdapter mFontAdapter;
    static FontLoader mFontLoader;
    SharedPreferences mMainPref;
//    SharedPreferences mCurrentPref;
//    SharedPreferences mAppPref;
//    TextView mFontPreview;


//    String mUnicode = Common.CURRENT_FONT_UNICODE;
//    String mZawgyi = Common.CURRENT_FONT_ZAWGYI;

    String mPrefAllApps = Common.KEY_ENABLE_ALL_APPS;
    String mPrefConverter = Common.KEY_ISCONVERTERON;

    boolean mCheckAllApps;
    boolean mCheckConverter;


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mMainPref = getSharedPreferences(Common.PEFERENCE_MAIN, MODE_MULTI_PROCESS);

        mCheckAllApps = mMainPref.getBoolean(mPrefAllApps, false);
        mCheckConverter = mMainPref.getBoolean(mPrefConverter, false);

        if (mFontLoader == null )
            mFontLoader = new FontLoader(mMainPref);
        if (mFontAdapter == null )
            mFontAdapter = new FontAdapter(this, mFontLoader);

        setupSimplePreferencesScreen();
    }


    /**
     * Shows the simplified settings UI if the device configuration if the
     * device configuration dictates that a simplified, single-pane UI should be
     * shown.
     */
    @SuppressLint("WorldReadableFiles")
    private void setupSimplePreferencesScreen() {
        if (!isSimplePreferences(this)) {
            return;
        }



        // In the simplified UI, fragments are not used at all and we instead
        // use the older PreferenceActivity APIs.

        // Add 'general' preferences.
        addPreferencesFromResource(R.xml.pref_general);

        // Add 'Zawgyi' preferences, and a corresponding header.
        PreferenceCategory fakeHeader = new PreferenceCategory(this);
        fakeHeader.setTitle(R.string.pref_header_zawgyi);
        getPreferenceScreen().addPreference(fakeHeader);
        addPreferencesFromResource(R.xml.pref_zawgyi);

        // Unicode pref
        fakeHeader = new PreferenceCategory(this);
        fakeHeader.setTitle(R.string.pref_header_unicode);
        getPreferenceScreen().addPreference(fakeHeader);
        addPreferencesFromResource(R.xml.pref_unicode);

        // Add 'About' preferences, and a corresponding header.
        fakeHeader = new PreferenceCategory(this);
        fakeHeader.setTitle(R.string.pref_header_about);
        getPreferenceScreen().addPreference(fakeHeader);
        addPreferencesFromResource(R.xml.pref_about);

        PreferenceScreen preferenceScreen = getPreferenceScreen();

        final CheckBoxPreference prefAllApps = (CheckBoxPreference) preferenceScreen.findPreference("pref_checkbox_allapps");
        final CheckBoxPreference prefConverter= (CheckBoxPreference) preferenceScreen.findPreference("pref_checkbox_converter");


        Preference prefAppList = preferenceScreen.findPreference("pref_app_list");
        Preference prefDefaultFont = preferenceScreen.findPreference("pref_default_font");
        Preference prefZawgyi = preferenceScreen.findPreference("pref_zawgyi_list");
        Preference prefUnicode= preferenceScreen.findPreference("pref_unicode_list");

        // Checkbox All Apps
        if(!prefAllApps.isChecked())
            mMainPref.edit().putBoolean(mPrefAllApps, false).apply();

        if(mCheckAllApps){ // FIXME: Its for displaying all the apps in Parallel Mode

            prefAllApps.setChecked(true);
        } else {
            prefAllApps.setChecked(false);
        }

        // Checkbox Converter
        if(!prefConverter.isChecked()){
            mMainPref.edit().putBoolean(mPrefConverter, false).apply();
        }
        if(mCheckConverter) {
            prefConverter.setChecked(true);
        } else {
            prefConverter.setChecked(false);
        }

        prefConverter.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                if(prefConverter.isChecked()){
                    mMainPref.edit().putBoolean(mPrefConverter, true).apply();
                } else {
                    mMainPref.edit().putBoolean(mPrefConverter, false).apply();
                }

                return true;
            }
        });
        prefAllApps.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                if(prefAllApps.isChecked())
                {
                    mMainPref.edit().putBoolean(mPrefAllApps, true).apply();
                } else
                {
                    mMainPref.edit().putBoolean(mPrefAllApps, false).apply();
                }

                return true;
            }
        });


        prefDefaultFont.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                showFontChooser(Common.KEY_DEFAULT_FONT);

                return true;
            }
        });

        prefAppList.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent i = new Intent(getApplicationContext(), AppsListFragment.class);
                startActivity(i);
                return true;
            }
        });


        prefZawgyi.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

//                Intent i = new Intent(getApplicationContext(), FontsListAct.class);
//                startActivity(i);

                showFontChooser(Common.CURRENT_FONT_ZAWGYI);
                return true;
            }
        });

        prefUnicode.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                showFontChooser(Common.CURRENT_FONT_UNICODE);
                return true;
            }
        });

    }

    private void showFontChooser(final String zawgyiOrUnicodeORdefault) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.layout_apps_list, null );
        builder.setView(view);


        final AlertDialog dialog = builder.create();
        final ListView listView = (ListView) view.findViewById(R.id.app_list);
        final EditText search_text = (EditText) view.findViewById(R.id.edittext_search);
        final ImageButton searchButton = (ImageButton) view.findViewById(R.id.button_search);
        final View progressBar = view.findViewById(R.id.progressbar);

        search_text.setVisibility(View.INVISIBLE);
        searchButton.setVisibility(View.INVISIBLE);
        mFontAdapter.update(progressBar);
        listView.setAdapter(mFontAdapter);

//        listView.setPersistentDrawingCache(ViewGroup.PERSISTENT_ALL_CACHES);
//        listView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {

                FontAdapter.FontItem info = (FontAdapter.FontItem) adapterView.getItemAtPosition(pos);

                if( info.filename.toString()
                        .endsWith(Common.SETTINGS_SUFFIX_INCOMPATIBLE)) {
                    Toast.makeText(view.getContext(), "Selected font is not compatible yet.", Toast.LENGTH_LONG).show();
                }

                saveSettingsFont(info.filename.toString(), zawgyiOrUnicodeORdefault);
                Toast.makeText(view.getContext(), "Font saved.", Toast.LENGTH_LONG ).show();
                dialog.dismiss();

            }
        });

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

    }


    private void saveSettingsFont(String fontfile, String uniOrZawgyiORdefaultfont){

        String raw = mMainPref.getString(uniOrZawgyiORdefaultfont, Common.DEFAULT_FONT_ALL_APPS);

        String weight = raw.split(Common.SETTINGS_SPLIT_SYMBOL)[Common.SETTINGS_INDEX_WEIGHT];

        String toSave = fontfile + Common.SETTINGS_SPLIT_SYMBOL + weight;


        // "pref_font_zawgyi"="@asset/0;0"
        // "pref_font_unicode"="@asset/1;0;
        //
        //
        // SAVED IN APP LIST => "com.facebook.katana"="@asset/0;0|@asset/1;0"
        //                          packagename           zawgyi | unicode
        //
        //
        if( uniOrZawgyiORdefaultfont.equals(Common.CURRENT_FONT_ZAWGYI) ){

            mMainPref.edit().putString(Common.CURRENT_FONT_ZAWGYI, toSave).apply();

        } else if ( uniOrZawgyiORdefaultfont.equals(Common.CURRENT_FONT_UNICODE) ){

            mMainPref.edit().putString(Common.CURRENT_FONT_UNICODE, toSave).apply();

        }else if( uniOrZawgyiORdefaultfont.equals(Common.KEY_DEFAULT_FONT)) {

            mMainPref.edit().putString(Common.KEY_DEFAULT_FONT, toSave).apply();
//            return;
        }

//        mMainPref.edit().putString(uniOrZawgyiORdefaultfont, toSave).apply();
//        initPreferenceValues(mMainPref.getString())
//        mMainPref.edit().putString(uniOrZawgyiORdefaultfont, fontfile).apply();


//        String raw_string = mCurrentPref.getString(uniOrZawgyi, Common.DEFAULT_FONT_ALL_APPS);
//        String weight = raw_string.split(Common.SETTINGS_SPLIT_SYMBOL)[Common.SETTINGS_INDEX_WEIGHT];
//        String newValue = fontfile + Common.SETTINGS_SPLIT_SYMBOL + weight;
//        mCurrentPref.edit().putString(uniOrZawgyi, newValue).commit();
//        initPreferenceValues(mCurrentPref.getString(mAppPkg, Common.DEFAULT_FONT_ALL_APPS));
    }


    public boolean onIsMultiPane() {
        return isXLargeTablet(this) && !isSimplePreferences(this);
    }

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Determines whether the simplified settings UI should be shown. This is
     * true if this is forced via {@link #ALWAYS_SIMPLE_PREFS}, or the device
     * doesn't have newer APIs like {@link PreferenceFragment}, or the device
     * doesn't have an extra-large screen. In these cases, a single-pane
     * "simplified" settings UI should be shown.
     */
    private static boolean isSimplePreferences(Context context) {
        return ALWAYS_SIMPLE_PREFS
                || Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB
                || !isXLargeTablet(context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        if (!isSimplePreferences(this)) {
            loadHeadersFromResource(R.xml.pref_headers, target);
        }
    }

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
//    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
//        @Override
//        public boolean onPreferenceChange(Preference preference, Object value) {
////            String stringValue = value.toString();
//
//            if (preference instanceof ListPreference) {
//                // For list preferences, look up the correct display value in
//                // the preference's 'entries' list.
//                ListPreference listPreference = (ListPreference) preference;
//                int index = listPreference.findIndexOfValue(stringValue);
//
//                // Set the summary to reflect the new value.
//                preference.setSummary(
//                        index >= 0
//                                ? listPreference.getEntries()[index]
//                                : null);
//
//            } else if (preference instanceof RingtonePreference) {
//                // For ringtone preferences, look up the correct display value
//                // using RingtoneManager.
//                if (TextUtils.isEmpty(stringValue)) {
//                    // Empty values correspond to 'silent' (no ringtone).
//                    preference.setSummary(R.string.pref_ringtone_silent);
//
//                } else {
//                    Ringtone ringtone = RingtoneManager.getRingtone(
//                            preference.getContext(), Uri.parse(stringValue));
//
//                    if (ringtone == null) {
//                        // Clear the summary if there was a lookup error.
//                        preference.setSummary(null);
//                    } else {
//                        // Set the summary to reflect the new ringtone display
//                        // name.
//                        String name = ringtone.getTitle(preference.getContext());
//                        preference.setSummary(name);
//                    }
//                }
//
//            } else {
//                // For all other preferences, set the summary to the value's
//                // simple string representation.
//                preference.setSummary(stringValue);
//            }
//            return true;
//        }
//    };

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
//    private static void bindPreferenceSummaryToValue(Preference preference) {
//        // Set the listener to watch for value changes.
//        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
//
//        // Trigger the listener immediately with the preference's
//        // current value.
//        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
//                PreferenceManager
//                        .getDefaultSharedPreferences(preference.getContext())
//                        .getString(preference.getKey(), ""));
//    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
//            bindPreferenceSummaryToValue(findPreference("example_text"));
//            bindPreferenceSummaryToValue(findPreference("example_list"));
        }
    }

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class ZawgyiSelectionPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_zawgyi);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
//            bindPreferenceSummaryToValue(findPreference("notifications_new_message_ringtone"));
        }

        @Override
        public Preference findPreference(CharSequence key) {
            return super.findPreference(key);
        }







    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class UnicodeSelectionPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_unicode);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
//            bindPreferenceSummaryToValue(findPreference("notifications_new_message_ringtone"));
        }
    }
    /**
     * This fragment shows data and sync preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class AboutPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_about);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
//            bindPreferenceSummaryToValue(findPreference("sync_frequency"));
        }
    }


}
