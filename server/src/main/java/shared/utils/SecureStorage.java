package shared.utils;


//Preferences class-> provides a way to store and retrieve user and system preference and configuration data

import java.util.prefs.Preferences;

public class SecureStorage {

    private static final Preferences pref = Preferences.userRoot().node("MyApp");

    // retrieves the root preference node for the current user. This node is used to store user-specific preferences.
    //This node will be used to store preferences specific to the application->node("MyApp")

    public static void saveCredentials(String phoneNumber, String token) {
        pref.put("phoneNumber", phoneNumber);
        pref.put("token", token);
    }

    public static String getPhoneNumber() {
        return pref.get("phoneNumber", null);
    }

    public static String getToken() {
        return pref.get("token", null);
    }

    public static void clearCredentials() {
        pref.remove("phoneNumber");
        pref.remove("token");
    }

}

