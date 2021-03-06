package de.htwdd.htwdresden;


import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.htwdd.htwdresden.classes.Const;
import de.htwdd.htwdresden.interfaces.INavigation;
import de.htwdd.htwdresden.service.VolumeControllerService;

/**
 * Fragment für die Einstellungen
 *
 * @author Kay Förster
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {


    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Setze Toolbartitle
        ((INavigation) getActivity()).setTitle(getResources().getString(R.string.navi_settings));
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view != null) {
            view.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(Const.preferencesKey.PREFERENCES_AUTO_MUTE)) {
            Context context = getActivity();
            boolean value = sharedPreferences.getBoolean(s, false);

            if (value) {
                // Starte Hintergrundservice
                VolumeControllerService volumeControllerService = new VolumeControllerService();
                volumeControllerService.startMultiAlarmVolumeController(context);

                //enable a receiver -> starts alarms on reboot
                ComponentName receiver = new ComponentName(context, VolumeControllerService.HtwddBootReceiver.class);
                PackageManager pm = context.getPackageManager();
                pm.setComponentEnabledSetting(receiver,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);

            } else {
                // Beende Hintergrundservice
                VolumeControllerService volumeControllerService = new VolumeControllerService();
                volumeControllerService.cancelMultiAlarmVolumeController(context);
                volumeControllerService.resetSettingsFile(context);

                //disable a receiver, alarms will not be set on reboot
                ComponentName receiver = new ComponentName(context, VolumeControllerService.HtwddBootReceiver.class);
                PackageManager pm = context.getPackageManager();
                pm.setComponentEnabledSetting(receiver,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
            }
        }
    }
}
