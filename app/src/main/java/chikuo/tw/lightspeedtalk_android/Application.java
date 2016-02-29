package chikuo.tw.lightspeedtalk_android;

import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.arrownock.exception.ArrownockException;
import com.arrownock.im.AnIM;
import com.arrownock.social.AnSocial;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chikuo on 2016/2/24.
 */
public class Application extends android.app.Application {

    public AnIM anIM;	/** Lightspeed Talk component */
    public AnSocial anSocial;		/** Lightspeed Social component */

    public String mClientId;
    public String mUsername;
    public String mCircleId;

    /** Map of clientId and userName，make searching userName by clientId faster*/
    public Map<String,String> mUsersMap;
    /** Map of topicId and topicName，make searching topicName by topicId faster*/
    public Map<String,String> mTopicsMap;


    public static Application getInstance(Context ct){
        Application application = (Application) ct.getApplicationContext();
        application.init();
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }

    public void init() {
        try {
            anIM = new AnIM(this);
            anSocial = new AnSocial(this, getString(R.string.app_key));
            mUsersMap = new HashMap<String,String>();
            mTopicsMap = new HashMap<String,String>();
        } catch (ArrownockException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

}
