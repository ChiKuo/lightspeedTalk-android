package chikuo.tw.lightspeedtalk_android;

/**
 * Created by chikuo on 2016/2/24.
 */
import android.content.Context;
import android.util.Log;

import com.arrownock.im.callback.AnIMCallbackAdapter;
import com.arrownock.im.callback.AnIMMessageCallbackData;
import com.arrownock.im.callback.AnIMReceiveACKCallbackData;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MessageCallback extends AnIMCallbackAdapter {

    private Application application;
    String LOG_TAG = "MessageCallback";

    private Context context;

    public MessageCallback(Context context) {
        this.context = context;
        application = (Application) context.getApplicationContext();
    }

    // Recevied a message from other client
    @Override
    public void receivedMessage(AnIMMessageCallbackData callbackData) {
        String msgId = callbackData.getMsgId();
        final String from = callbackData.getFrom();
        Set<String> parties = callbackData.getParties();
        final String message = callbackData.getMessage();
        Map<String, String> customData = callbackData.getCustomData();
        final String type = customData==null ?
                Utils.Constant.AttachmentType.TEXT:customData.get(Utils.Constant.MsgCustomData.TYPE);
        final String data = customData==null ?
                null:customData.get(Utils.Constant.MsgCustomData.DATA);

        Log.d(LOG_TAG, "received message: " + message);

        // Update chatList
        ChatList chatList = new ChatList();
        chatList.currentClientId = application.mClientId;
        chatList.targetClientId = from ;
        chatList.lastMessage = message;
        chatList.update();

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
