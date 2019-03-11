package liquidstars.cashtracker.view.util;

import android.content.Context;

import com.hotmail.or_dvir.easysettings.pojos.EasySettings;

public class SettingsHelper {
    public static final String SETTING_NOT_FIRST_LAUNCH = "notfirstlaunch";

    public static void putBoolean(Context context, String key, boolean value){
        EasySettings.retrieveSettingsSharedPrefs(context)
                .edit()
                .putBoolean(key, value)
                .apply();
    }

    public static boolean checkBoolean(Context context, String key){
        return EasySettings.retrieveSettingsSharedPrefs(context)
                .getBoolean(key, false);
        // if setting doesn't exists returns false
    }

    public static void putString(Context context, String key, String value){
        EasySettings.retrieveSettingsSharedPrefs(context)
                .edit()
                .putString(key, value)
                .apply();
    }
}
