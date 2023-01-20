package com.example.lightweight;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences("AppKey", 0);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public void setFlag(Boolean flag) {
        editor.putBoolean("KEY_FLAG", flag);
        editor.commit();
    }

    public boolean getFlag() {
        return sharedPreferences.getBoolean("KEY_FLAG", false);
    }

//    public void setCurrentTime(String currentTime) {
//        editor.putString("KEY_TIME", currentTime);
//        editor.commit();
//    }
//
//    public String getCurrentTime() {
//        return sharedPreferences.getString("KEY_TIME", "");
//    }

    public void setStopwatchRunningBackground(boolean isRunning) {
        editor.putBoolean("KEY_STOPWATCH_RUNNING_BG", isRunning);
        editor.commit();
    }

    public boolean getStopwatchRunningBackground() {
        return sharedPreferences.getBoolean("KEY_STOPWATCH_RUNNING_BG", false);
    }

    public void setStopwatchRunning(boolean isRunning) {
        editor.putBoolean("KEY_STOPWATCH_RUNNING", isRunning);
        editor.commit();
    }

    public boolean getStopwatchRunning() {
        return sharedPreferences.getBoolean("KEY_STOPWATCH_RUNNING", false);
    }

    public void setCurrentTime(Long currentTime) {
        editor.putLong("KEY_TIME", currentTime);
        editor.commit();
    }

    public Long getCurrentTime() {
        return sharedPreferences.getLong("KEY_TIME", 0);
    }

    public void setPauseOffset(Long pauseOffset) {
        editor.putLong("KEY_PAUSE_OFFSET", pauseOffset);
        editor.commit();
    }

    public Long getPauseOffset() {
        return sharedPreferences.getLong("KEY_PAUSE_OFFSET", 0);
    }
}
