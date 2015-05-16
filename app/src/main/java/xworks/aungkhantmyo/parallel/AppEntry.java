package xworks.aungkhantmyo.parallel;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.widget.CheckBox;

import java.io.File;

/**
 * Created by Aung Khant Myo on 2/27/15.
 */
public class AppEntry {
    private final AppLoader mLoader;
    private final ApplicationInfo mInfo;
    private final File mApkFile;
    private String mLabel;
    private Drawable mIcon;
    private boolean mMounted;
    private boolean mSelected;

    public AppEntry(AppLoader loader, ApplicationInfo info) {
        mLoader = loader;
        mInfo = info;
        mApkFile = new File(info.sourceDir);
    }

    public ApplicationInfo getApplicationInfo() {
        return mInfo;
    }

    public boolean isSelected() {
        return mSelected;
    }

    public void setSelected(boolean selected){
        this.mSelected = selected;
    }

    public String getLabel() {
        return mLabel;
    }

    public String getPackageName() {
        return mInfo.packageName;
    }

    public Drawable getIcon() {
        return mIcon;
    }

    @Override
    public String toString() {
        return mLabel;
    }

    public void loadLabel(Context context) {
        if (mLabel == null || !mMounted) {
            if (!mApkFile.exists()) {
                mMounted = false;
                mLabel = null;
            } else {
                mMounted = true;
                CharSequence label = mInfo.loadLabel(mLoader.mPm);
                mLabel = label != null ? label.toString() : null;
            }
        }
    }

    public void loadIcon(Context context) {
        if (mIcon == null || !mMounted) {
            if (!mApkFile.exists()) {
                mMounted = false;
                mIcon = null;
            } else {
                mMounted = true;
                mIcon = mInfo.loadIcon(mLoader.mPm);
                if (mIcon == null)
                    mIcon = context.getResources().getDrawable(android.R.drawable.sym_def_app_icon);
            }
        }
    }

}