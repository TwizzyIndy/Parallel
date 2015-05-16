package xworks.aungkhantmyo.parallel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Aung Khant Myo on 2/27/15.
 */
public class AppListAdapter extends ArrayAdapter<AppEntry> {

    private LayoutInflater mLayoutInflater;
    private AppEntryFilter mFilter;

    private List<AppEntry> mData;

    private SharedPreferences mAppPref;
    private SharedPreferences mMainPref;
    private SharedPreferences mCurrentPref;
    private FontHelper.FontType mFontType_Zawgyi = new FontHelper.FontType();

//    static FontLoader mFontLoader;
//    static FontAdapter mFontAdapter;

//    int pos;
//    int boxState[];


    @SuppressLint("WorldReadableFiles")
    public AppListAdapter(Context context) {
        super(context, R.layout.view_app_list);

        mAppPref = context.getSharedPreferences(Common.PEFERENCE_APP, Activity.MODE_WORLD_READABLE);

        mMainPref = context.getSharedPreferences(Common.PEFERENCE_MAIN,Activity.MODE_WORLD_READABLE);



        mLayoutInflater = LayoutInflater.from(context);
//
//        for ( int i = 0; i < mData.size(); i++){
//            boxState[i] = 0;
//        }
    }

    public void setData(List<AppEntry> data ) {
        clear();
        if ( data != null ){
            addAll(data);
        }
        mData = data;
    }

    public List<AppEntry> getData(){
        return mData;
    }

    static class ViewHolder {
        TextView name;
        ImageView icon;
        TextView pkg;
        CheckBox checkBox;
    }

    private String mAppPkg;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
//        View view = null;

        if(convertView == null ){

            convertView = mLayoutInflater.inflate(R.layout.view_app_list, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView) convertView.findViewById(android.R.id.icon);
            viewHolder.name = (TextView) convertView.findViewById(android.R.id.text1);
            viewHolder.pkg = (TextView) convertView.findViewById(android.R.id.text2);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(android.R.id.checkbox);

            viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    AppEntry element = (AppEntry) viewHolder.checkBox.getTag();

                    element.setSelected(compoundButton.isChecked());

//                    if(holder.checkBox.isChecked()){
//                        element.setSelected(true);
//                    } else {
//                        element.setSelected(false);
//                    }

                    Log.d("Paralle", viewHolder.pkg.getText().toString());

                    mAppPkg = element.getPackageName();

                    if(element.isSelected() ) {

                        saveSettingApp(mAppPkg);
                    } else {
                        removeSettingApp(mAppPkg);
                    }

                }
            });

            convertView.setTag(viewHolder);

            viewHolder.checkBox.setTag(mData.get(position));
//            holder.checkBox.setChecked(mData.get(position).isSelected());


        } else {

            viewHolder = (ViewHolder) convertView.getTag();
            ((ViewHolder) convertView.getTag()).checkBox.setTag(mData.get(position));
//            ((ViewHolder) )

        }

        AppEntry appInfo = mData.get(position);

        ViewHolder holder = (ViewHolder) convertView.getTag();

        holder.name.setText(appInfo.getLabel());

        String packageName = appInfo.getPackageName();
        boolean pref_enabled;

        if (packageName.equals(Common.PACKAGE_ANDROID_SYSTEM) || packageName.equals("com.android.systemui")){
            pref_enabled = mMainPref.contains(packageName);
            mCurrentPref = mMainPref;
        } else {
            pref_enabled = mAppPref.contains(packageName);
            mCurrentPref = mAppPref;
        }


        holder.name.setTextColor(pref_enabled ? Color.BLUE : Color.BLACK);

        holder.checkBox.setChecked(pref_enabled);


        holder.pkg.setText(packageName);
        holder.icon.setImageDrawable(appInfo.getIcon());



        return convertView;
    }

    private void removeSettingApp(String packageName){

        mCurrentPref.edit().remove(packageName).apply();

    }
    private void saveSettingApp(String packageName) {

        String font_unicode = mMainPref.getString(Common.CURRENT_FONT_UNICODE, Common.DEFAULT_FONT_ALL_APPS);
        String font_zawgyi  = mMainPref.getString(Common.CURRENT_FONT_ZAWGYI, Common.DEFAULT_FONT_ALL_APPS);


//        String raw_string = mCurrentPref.getString(packageName, Common.DEFAULT_PARALLEL_FONTS_ALL_APPS);

//        String parallel_zawgyi = raw_string.split(Common.SETTINGS_SPLIT_AND)[0];
//        String parallel_unicode= raw_string.split(Common.SETTINGS_SPLIT_AND)[1];

        String newValue = font_zawgyi + Common.SETTINGS_SPLIT_AND + font_unicode;

        // its gonna be like this => "com.facebook.katana"="@asset/0;0|@asset/1;0"

        mCurrentPref.edit().putString(packageName, newValue).apply();

//        initPreferenceValues(mCurrentPref.getString(mAppPkg, Common.DEFAULT_FONT_ALL_APPS));

    }

//    private void initPreferenceValues(String unparsed) {
//
////        String raw_font_name = unparsed.split(Common.SETTINGS_SPLIT_SYMBOL)[Common.SETTINGS_INDEX_FONT];
////        FontHelper.FontType type_Font = FontHelper.parsedPref(this.getContext().getResources(), unparsed, mFontLoader);
////        String proper_font_name = FontHelper.parseSettingsFontSyntaxIntoName(this.getContext().getResources(), raw_font_name);
////
//    }

    @Override
    public Filter getFilter() {
        if( mFilter == null ){
            mFilter = new AppEntryFilter(this);
        }
        return mFilter;
    }




    @Override
    public long getItemId(int position) {
        return position;
    }


    public static class AppEntryFilter extends Filter {

        private AppListAdapter mAdapter;
        public AppEntryFilter(AppListAdapter adapter){
            mAdapter = adapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            FilterResults results = new FilterResults();
            List<AppEntry> originalValues = mAdapter.getData();

            if (TextUtils.isEmpty(charSequence)) {
                ArrayList<AppEntry> entries = new ArrayList<AppEntry>(originalValues);
                results.values = entries;
                results.count = entries.size();

            } else {
                Locale locale = Locale.getDefault();
                String prefixString = charSequence.toString().toLowerCase(locale);

                final int count = originalValues.size();
                final ArrayList<AppEntry> newValues = new ArrayList<AppEntry>();

                for (int i=0; i < count; i++){
                    AppEntry value = originalValues.get(i);

                    if(value.getPackageName().startsWith(prefixString)){
                        newValues.add(value);
                    } else {
                        String label = value.getLabel().toLowerCase(locale);
                        if( label.startsWith(prefixString)) {
                            newValues.add(value);
                        }
                    }
                }
                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            mAdapter.clear();

            if(filterResults.count > 0 ){
                mAdapter.addAll((List<AppEntry>) filterResults.values);
                mAdapter.notifyDataSetChanged();
            } else {
                mAdapter.notifyDataSetInvalidated();
            }
        }
    }
}
