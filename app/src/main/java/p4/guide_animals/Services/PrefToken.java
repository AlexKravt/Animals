package p4.guide_animals.Services;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by kravtsov.a on 26.10.2016.
 */

public final class PrefToken {

    private static final String PREFS_NAME = "guide_animals.ya.money";
    private static final String PREF_TOKEN = "guide_animals.ya.money.token";

    private final SharedPreferences prefs;

    public PrefToken(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public boolean storeToken(String token) {
       return prefs.edit().putString(PREF_TOKEN, token).commit();
    }
    public void storeTokenApply(String token) {
         prefs.edit().putString(PREF_TOKEN, token).apply();
    }
    public String restoreToken() {
        return prefs.getString(PREF_TOKEN, null);
    }

    public boolean deleteToken() {

        return  prefs.edit().remove(PREFS_NAME).clear().commit();
    }
}