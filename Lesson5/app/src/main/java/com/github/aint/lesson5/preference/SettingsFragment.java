package com.github.aint.lesson5.preference;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.github.aint.lesson5.R;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    private static final String TOGGLE_IMAGE_KEY = "toggle_image";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        CheckBoxPreference toggleImage = (CheckBoxPreference) findPreference(TOGGLE_IMAGE_KEY);
//        toggleImage.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
//        if ("true".equals(newValue.toString())) {
//
//        }
        return false;
    }
}
