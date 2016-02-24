package chikuo.tw.lightspeedtalk_android;

/**
 * Created by chikuo on 2016/2/24.
 */
import android.util.Log;

import com.arrownock.exception.ArrownockException;
import com.arrownock.im.callback.AnIMCallbackAdapter;
import com.arrownock.im.callback.AnIMMessageCallbackData;
import com.arrownock.im.callback.AnIMReceiveACKCallbackData;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MessageCallback extends AnIMCallbackAdapter {

    String LOG_TAG = "MessageCallback";

    // Recevied a message from other client
    @Override
    public void receivedMessage(AnIMMessageCallbackData data) {
        String msgId = data.getMsgId();
        final String from = data.getFrom();
        Set<String> parties = data.getParties();
        final String message = data.getMessage();
        Map<String, String> customData = data.getCustomData();
        Log.d(LOG_TAG, "received message: " + message);

        if (customData != null) {
            Set<String> keys = customData.keySet();
            Iterator<String> it = keys.iterator();
            while (it.hasNext()) {
                String key = it.next();
                String status = customData.get(key);
                Log.d(LOG_TAG, key + " : " + status);
            }
        }
    }

    // Received the acknowledgement for msgId from IM server
    @Override
    public void receivedReceiveACK(AnIMReceiveACKCallbackData data) {
        String msgId = data.getMsgId();
        String from = data.getFrom();
        Log.d(LOG_TAG, "message: " + msgId + " recevied by: " + from);
    }
}
