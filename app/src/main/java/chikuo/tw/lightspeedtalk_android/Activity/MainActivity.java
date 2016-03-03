package chikuo.tw.lightspeedtalk_android.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.arrownock.exception.ArrownockException;
import com.arrownock.social.AnSocialMethod;
import com.arrownock.social.IAnSocialCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import chikuo.tw.lightspeedtalk_android.Application;
import chikuo.tw.lightspeedtalk_android.MessageCallback;
import chikuo.tw.lightspeedtalk_android.R;

public class MainActivity extends AppCompatActivity {

    private Application application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        application = Application.getInstance(this);

        login("chi","chi");
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

        application.anIM.setCallback(null);
    }


}
