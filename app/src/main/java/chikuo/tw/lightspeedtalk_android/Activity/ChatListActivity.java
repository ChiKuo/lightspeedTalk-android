package chikuo.tw.lightspeedtalk_android.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.arrownock.exception.ArrownockException;
import com.arrownock.im.callback.AnIMGetClientsStatusCallbackData;
import com.arrownock.im.callback.IAnIMGetClientsStatusCallback;
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

    private ChatListAdapter chatListAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        application = (Application) getApplicationContext();
        chatLists = new ArrayList<>();

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
}
