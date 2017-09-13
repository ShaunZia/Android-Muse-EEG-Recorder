package com.shaunzia.eegrecorder;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * SettingUtil.java defines csv records path
 *
 * @author Shaun Zia
 * @version 2.0 27/11/2015
 */
public class SettingUtil {

    // String declaration for csv records path
    public final static String getRecordPath(Context context) {
        return getPreferenceString(context, context.getString(R.string.pref_record_path), context.getString(R.string.pref_default_record_path));
    }

    // Return value for csv records path
    private static String getPreferenceString(Context context, String key, String defaultString) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, defaultString);
    }

}
