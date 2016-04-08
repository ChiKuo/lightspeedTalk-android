package chikuo.tw.lightspeedtalk_android.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.arrownock.push.PushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import chikuo.tw.lightspeedtalk_android.R;
import chikuo.tw.lightspeedtalk_android.activity.ChatListActivity;

/**
 * Created by chikuo on 2016/4/7.
 */

// This is the receiver extends Lightspeed default receiver PushBroadcastReceiver
public class LightspeedPushReceiver extends PushBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
//        super.onReceive(context, intent);

        String alert = null; // message
        String title = null;
        String from = null;

        // Lightspeed notification content would be carried with intent as a bundle.
        // Get the bundle and retrieve the string by key "payload" in the bundle.
        Bundle bundle = intent.getExtras();

        if(bundle == null)
            return;

        if(!bundle.containsKey("payload") )
            return;

        String payload = bundle.getString("payload");

        try{
            // Content of payload is json format.
            JSONObject json = new JSONObject(payload);

            if (json.has("android")) {
                JSONObject androidPayload = json.getJSONObject("android");

                if (androidPayload.has("alert")) {
                    alert = (String) androidPayload.get("alert");
                }
                if (androidPayload.has("title")) {
                    title = (String) androidPayload.get("title");
                }
            }

            if (json.has("from")) {
                from = json.getString("from");
            }

        }catch (JSONException e){
            e.printStackTrace();
        }

        if (title == null){
            // TODO Use from value to find sender's name
            // Send from offline message
            sendNotification(context, "你有一則新訊息", from + " : " +alert);
        } else {
            // Send from backstage
            sendNotification(context, title, alert);
        }

    }

    private void sendNotification(Context context,String title, String message) {

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);

        // Create a Notification Builder
        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setLargeIcon(bitmap)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(new Notification.BigTextStyle().bigText(message));

        // Define the Notification's Action
        Intent resultIntent = new Intent(context, ChatListActivity.class);
        int pendingIntentId = (int) System.currentTimeMillis();
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, pendingIntentId, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Set the Notification's Click Behavior
        builder.setContentIntent(resultPendingIntent);

        // Issue the Notification
//        int notificationId = (int) System.currentTimeMillis();
        int notificationId = 0;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, builder.build());
    }
}
