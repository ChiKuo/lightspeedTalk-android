package chikuo.tw.lightspeedtalk_android.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.arrownock.im.callback.IAnIMPushBindingCallback;
import com.arrownock.push.AnPush;
import com.arrownock.exception.ArrownockException;
import com.arrownock.push.AnPushCallbackAdapter;
import com.arrownock.push.AnPushStatus;
import com.arrownock.push.IAnPushRegistrationCallback;
import com.arrownock.social.AnSocialMethod;
import com.arrownock.social.IAnSocialCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chikuo.tw.lightspeedtalk_android.Application;
import chikuo.tw.lightspeedtalk_android.MessageCallback;
import chikuo.tw.lightspeedtalk_android.R;

public class MainActivity extends AppCompatActivity {

    private Application application;
    private AnPush anPush;

    private final String LOG_TAG = "LightspeedPush";
    private final String LIGHT_SPEED_PREF = "Lightspeed";
    private final String REGISTER_TIME_STAMP = "register_time_stamp";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private final long RegisterDelay = 604800000l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        application = Application.getInstance(this);
        sharedPreferences = getSharedPreferences(LIGHT_SPEED_PREF, MODE_PRIVATE);

        login("chi","chi");

        initLightSpeedPushServer();
    }

    @Override
    protected void onResume() {
        super.onResume();

        MessageCallback messageCallback = new MessageCallback(MainActivity.this);
        application.anIM.setCallback(messageCallback);
    }

    private void signUp(final String username,final String pwd){
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", username);
        params.put("password", pwd);
        params.put("password_confirmation", pwd);
        params.put("enable_im", true);
        try {
            application.anSocial.sendRequest("users/create.json", AnSocialMethod.POST, params, new IAnSocialCallback(){
                @Override
                public void onFailure(JSONObject arg0) {
                    try {
                        String errorMsg = arg0.getJSONObject("meta").getString("message");
                        Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_LONG).show();
                        System.out.println("the error message: " + errorMsg);
                        // TODO need signup
                    } catch (Exception e1) {}
                }
                @Override
                public void onSuccess(final JSONObject arg0) {
                    Log.e("signUp",arg0.toString());
                    try {
                        String userId = arg0.getJSONObject("response").getJSONObject("user").getString("id");
                        String userName = arg0.getJSONObject("response").getJSONObject("user").getString("username");
                        String clientId = arg0.getJSONObject("response").getJSONObject("user").getString("clientId");
                        System.out.println("user id is: " + userId);
                        application.mUsername = userName;
                        application.mCircleId = userId;
                        application.mClientId = clientId;
                        afterLogin();
                    } catch (Exception e1) {}
                }
            });
        } catch (ArrownockException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param username Username
     * @param pwd Password
     */
    private void login(final String username,final String pwd){
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", username);
        params.put("password", pwd);
        try {
            application.anSocial.sendRequest("users/auth.json", AnSocialMethod.POST, params, new IAnSocialCallback(){
                @Override
                public void onFailure(JSONObject arg0) {
                    try {
                        String errorMsg = arg0.getJSONObject("meta").getString("message");
                        Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_LONG).show();
                        System.out.println("the error message: " + errorMsg);
                        // TODO need login
                    } catch (Exception e1) {}
                }
                @Override
                public void onSuccess(final JSONObject arg0) {
                    Log.e("login", arg0.toString());
                    try {
                        String userId = arg0.getJSONObject("response").getJSONObject("user").getString("id");
                        String userName = arg0.getJSONObject("response").getJSONObject("user").getString("username");
                        String clientId = arg0.getJSONObject("response").getJSONObject("user").getString("clientId");

                        System.out.println("user id is: " + userId);

                        application.mUsername = userName;
                        application.mCircleId = userId;
                        application.mClientId = clientId;
                        afterLogin();
                    } catch (Exception e1) {}
                }
            });
        } catch (ArrownockException e) {
            e.printStackTrace();
        }
    }

    /**
     * After clientId has been successfully update to Lightspeed Social database。
     * Connect to Lightspeed Talk server
     */
    private void afterLogin(){
        try {
            application.anIM.connect(application.mClientId);
        } catch (ArrownockException e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        // Bind the push service (AnPush) and client
        application.anIM.bindAnPushService(anPush.getAnID(), getString(R.string.lightspeed_app_key), application.mClientId, new IAnIMPushBindingCallback() {
            @Override
            public void onSuccess() {
                Log.d(LOG_TAG,"bindAnPushService success");
            }

            @Override
            public void onError(ArrownockException e) {
                Log.d(LOG_TAG,"bindAnPushService failed");
            }
        });

        // TODO
        Intent i = new Intent(MainActivity.this, ChatListActivity.class);
        startActivity(i);
    }

    private void logout() {
        try {
            application.anIM.disconnect();
        } catch (ArrownockException e) {
            e.printStackTrace();
        }
        Intent it = new Intent();
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        it.setClass(getBaseContext(), MainActivity.class);
        startActivity(it);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();

//        //TODO Change the place when user exited the app
//        try {
//            application.anIM.disconnect();
//        } catch (ArrownockException e) {
//            e.printStackTrace();
//        }
        application.anIM.setCallback(null);
    }

    private void initLightSpeedPushServer() {

        // The channel names we'll register at Lightspeed.
        List<String> channels = new ArrayList<String>();
        channels.add("android");
        channels.add("test");

        try {
            // Instantiate Lightspeed AnPush instance which is an entry point of Lightspeed service.
            anPush = AnPush.getInstance(getBaseContext());
            anPush.setCallback(new AnPushCallbackAdapter() {

                @Override
                public void statusChanged(AnPushStatus currentStatus, ArrownockException exception) {

                    if (currentStatus == AnPushStatus.ENABLE) {
                        Log.i(LOG_TAG, "Push status enalbed");

                    } else if (currentStatus == AnPushStatus.DISABLE) {
                        Log.e(LOG_TAG, "Push status disabled");
                    }

                    if (exception != null) {
                        Log.e(LOG_TAG, "Push status changed with error occuring = " + exception.toString());
                    }
                }
            });

            // Register channels to Lightspeed service.
            // If register is success, the callback function onSuccess() in IAnPushRegistrationCallback would be invoked.
            Log.i(LOG_TAG, "Register push channels");

            if(needRegister()){
                anPush.register(channels, new IAnPushRegistrationCallback() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(LOG_TAG, "Register success " );
                        editor = sharedPreferences.edit();
                        editor.putLong(REGISTER_TIME_STAMP, Calendar.getInstance().getTimeInMillis()).commit();

                        try {
                            anPush.enable();
                        } catch (ArrownockException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ArrownockException e) {
                        Log.e(LOG_TAG, "Register with error = " + e.getMessage());
                    }
                });
            }else{
                anPush.enable();
            }

        } catch (ArrownockException ex) {
            // If there's any error occur during register procedure, we'll print error message on Logcat.
            ex.printStackTrace();
        }

    }

    private Boolean needRegister(){
        Long now = Calendar.getInstance().getTimeInMillis();
        Long last = sharedPreferences.getLong(REGISTER_TIME_STAMP, 0);
        if(now - last >= RegisterDelay){
            return true;
        }else{
            return false;
        }
    }

}
