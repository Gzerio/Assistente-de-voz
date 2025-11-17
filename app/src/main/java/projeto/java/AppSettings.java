package projeto.java; 

import java.util.prefs.Preferences;

public class AppSettings {
    private static final Preferences prefs =
            Preferences.userRoot().node("voiceAssistant");

    private static final String KEY_MODEL_MEDIUM = "modelMedium";
    private static final String KEY_LANGUAGE = "language"; 
    private static final String KEY_SERVER_ALWAYS_ON = "serverAlwaysOn";

    
    public static boolean isModelMedium() {
        return prefs.getBoolean(KEY_MODEL_MEDIUM, false); 
    }

    public static String getLanguage() {
        return prefs.get(KEY_LANGUAGE, "pt"); 
    }

    public static boolean isServerAlwaysOn() {
        return prefs.getBoolean(KEY_SERVER_ALWAYS_ON, true); 
    }

   
    public static void setModelMedium(boolean value) {
        prefs.putBoolean(KEY_MODEL_MEDIUM, value);
    }

    public static void setLanguage(String lang) {
        prefs.put(KEY_LANGUAGE, lang); 
    }

    public static void setServerAlwaysOn(boolean value) {
        prefs.putBoolean(KEY_SERVER_ALWAYS_ON, value);
    }
}
