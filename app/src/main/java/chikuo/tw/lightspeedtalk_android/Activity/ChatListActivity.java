package chikuo.tw.lightspeedtalk_android.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.arrownock.exception.ArrownockException;
import com.arrownock.im.AnIMMessage;
import com.arrownock.im.callback.AnIMGetClientsStatusCallbackData;
import com.arrownock.im.callback.IAnIMGetClientsStatusCallback;
import com.arrownock.im.callback.IAnIMHistoryCallback;
import com.arrownock.social.AnSocialMethod;
import com.arrownock.social.IAnSocialCallback;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chikuo.tw.lightspeedtalk_android.Application;
import chikuo.tw.lightspeedtalk_android.adapter.ChatListAdapter;
import chikuo.tw.lightspeedtalk_android.ChatList;
import chikuo.tw.lightspeedtalk_android.MessageCallback;
import chikuo.tw.lightspeedtalk_android.R;
import chikuo.tw.lightspeedtalk_android.util.ChatListReloadEvent;
import chikuo.tw.lightspeedtalk_android.util.EventBusObject;
import de.greenrobot.event.EventBus;

/**
 * Created by edward_chiang on 15/5/19.
 */
public class ChatListActivity extends AppCompatActivity {

    private Application application;
    private List<ChatList> chatLists;
    private List<AnIMMessage> offlineHistoryMessage;

    private ChatListAdapter chatListAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        application = (Application) getApplicationContext();
        chatLists = new ArrayList<>();
        offlineHistoryMessage = new ArrayList<>();

        setContentView(R.layout.activity_chat_list);

        EventBus.getDefault().register(new EventBusObject<ChatListReloadEvent>() {
            @Override
            public void onEvent(ChatListReloadEvent event) {
                queryChatListFromLocalDB();
            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        initView();

        // Query the ChatList from local database
        queryChatListFromLocalDB();

        // Query the message which send when user offline
        getOfflineHistory();
    }

    @Override
    public void onResume(){
        super.onResume();
        MessageCallback messageCallback = new MessageCallback(ChatListActivity.this);
        application.anIM.setCallback(messageCallback);
    }

    @Override
    protected void onPause() {
        super.onPause();

        application.anIM.setCallback(null);
    }

    private void initView(){
        // Init RecyclerView and Adapter
        chatLists = new ArrayList<>();
        chatListAdapter = new ChatListAdapter(ChatListActivity.this,chatLists);
        chatListAdapter.setChatLists(chatLists);
        rv = (RecyclerView) findViewById(R.id.chat_recycler_view);
        rv.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(ChatListActivity.this);
        rv.setLayoutManager(mLayoutManager);
        rv.setAdapter(chatListAdapter);
    }

    private void queryChatListFromLocalDB() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chatLists = ChatList.getAll(application.mClientId);
                chatListAdapter.setChatLists(chatLists);
                chatListAdapter.notifyDataSetChanged();
            }
        });
    }

    private void getOfflineHistory(){

        int historyLimit = 10;
        application.anIM.getOfflineHistory(application.mClientId, historyLimit, new IAnIMHistoryCallback() {
            @Override
            public void onSuccess(List<AnIMMessage> messages, int count) {
                offlineHistoryMessage.addAll(messages);

                if (count > 0) {
                    // If have offline message yet
                    getOfflineHistory();
                } else {
                    // Get all offline message done
                    int offlineMessageSize = offlineHistoryMessage.size();
                    if (offlineMessageSize > 0) {
                        for (int i = offlineMessageSize - 1 ; i >= 0; i--) {
                            // Update local chatList
                            ChatList chatList = new ChatList();
                            chatList.currentClientId = application.mClientId;
                            chatList.targetClientId = offlineHistoryMessage.get(i).getFrom();
                            chatList.lastMessage = offlineHistoryMessage.get(i).getMessage();
                            chatList.update();
                        }
                    }
                }
            }

            @Override
            public void onError(ArrownockException exception) {
                Log.d("getOfflineHistory","getOfflineHistory error = " + exception.getMessage());
            }
        });
    }
}
