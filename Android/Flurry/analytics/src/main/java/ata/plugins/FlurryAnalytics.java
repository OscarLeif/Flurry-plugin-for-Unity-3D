package ata.plugins;

import android.app.Fragment;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.flurry.android.FlurryAgent;
import com.flurry.android.FlurryAgentListener;
import com.flurry.android.FlurryConsent;
import com.unity3d.player.UnityPlayer;

import java.util.HashMap;
import java.util.Map;

import static android.util.Log.VERBOSE;

/**
 * Created by OscarLeif on 5/6/2017.
 */

public class FlurryAnalytics extends Fragment
{
    //Constants
    public static final String TAG   = "Flurry Analytics";
    private String FLURRY_API_KEY = "";

    //instance instance
    public static FlurryAnalytics instance;

    // Unity context.
    private String gameObjectName;

    public static void start(String gameobjectName)
    {
        // Instantiate and add Unity Player Activity;
        instance = new FlurryAnalytics();
        instance.gameObjectName = gameobjectName;
        instance.FLURRY_API_KEY = gameobjectName;
        Log.d(TAG, "start: Method Called");
        UnityPlayer.currentActivity.getFragmentManager().beginTransaction().add(instance, FlurryAnalytics.TAG).commit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Map<String, String> consentStrings = new HashMap<>();

        consentStrings.put("IAB", "yes");

        super.onCreate(savedInstanceState);
        if(UnityPlayer.currentActivity == null)
        {
            Log.d(TAG, "Warning Current Activity is null");
        }
        new FlurryAgent.Builder()
                .withLogEnabled(true)
                .withCaptureUncaughtExceptions(true)
                .withContinueSessionMillis(10000)
                .withLogLevel(VERBOSE)
                .withConsent(new FlurryConsent(true, consentStrings))
                /*.withListener(new FlurryAgentListener()
                {
                    @Override
                    public void onSessionStarted()
                    {
                        Log.d(TAG, "onSessionStarted: Flurry is working");
                    }
                })*/
                .build(UnityPlayer.currentActivity, FLURRY_API_KEY);
        //get version name
        /*try
        {
            PackageInfo pInfo = UnityPlayer.currentActivity
                    .getPackageManager()
                    .getPackageInfo(UnityPlayer.currentActivity.getPackageName(), 0);
            String version = pInfo.versionName;
            FlurryAgent.setVersionName(version);
            Log.d(TAG, "onCreate: versionName: " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }*/
        FlurryAgent.onStartSession(UnityPlayer.currentActivity);
        Log.d(TAG, "onCreate: Method Called");
        Log.d(TAG, "onCreate: KEY :" + FLURRY_API_KEY);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        FlurryAgent.onEndSession(UnityPlayer.currentActivity);
    }

    public void logEvent(String eventName)
    {
        FlurryAgent.logEvent(eventName);
    }

    public void logEvent(String eventName, boolean timed)
    {
        FlurryAgent.logEvent(eventName,timed);
    }

    public void logEvent(String eventName, Map<String, String> eventParams, boolean timed )
    {
        FlurryAgent.logEvent(eventName, eventParams, timed);
    }

    public void endTimedEvent(String eventName)
    {
        FlurryAgent.endTimedEvent(eventName);
    }

    public void endTimedEvent(String eventName, Map<String,String> eventParams)
    {
        FlurryAgent.endTimedEvent(eventName,eventParams);
    }

    // region Utilities

    private void SendUnityMessage(String methodName, String parameter)
    {
        Log.i(TAG, TAG+"SendUnityMessage(`"+methodName+"`, `"+parameter+"`)");
        UnityPlayer.UnitySendMessage(gameObjectName, methodName, parameter);
    }

// endregion
}
