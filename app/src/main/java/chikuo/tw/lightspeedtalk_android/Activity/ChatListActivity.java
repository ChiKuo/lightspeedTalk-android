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
    private ArrayList<HashMap<String, String>> partiesList;
    private List<ChatList> chatLists;

    private ChatListAdapter chatListAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView rv;

    // TODO send
    String clientId = "AIMJYM8T2UCS5JFCD62UR8E";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        application = (Application) getApplicationContext();
        chatLists = new ArrayList<>();
        partiesList = new ArrayList<HashMap<String, String>>();

        setContentView(R.layout.activity_chat_list);

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        initView();

        // TODO test
        // Create a ChatList
//        ChatList chatList = new ChatList();
//        chatList.currentClientId = clientId;
//        chatList.targetClientId = "AIMOM1Y3KT3T8DV8YMUN5U5";
//        chatList.lastMessage = "Hi";
//        chatList.unReadCount = 3;
//        chatList.update();



        // Query the ChatList from local database
        queryChatListFromLocalDB();

        EventBus.getDefault().register(new EventBusObject<ChatListReloadEvent>() {
            @Override
            public void onEvent(ChatListReloadEvent event) {
                queryChatListFromLocalDB();
            }
        });

        // Delete
//        chatLists.get(0).delete();
    }

    private void queryChatListFromLocalDB() {
        chatLists = ChatList.getAll(clientId);
        chatListAdapter.setChatLists(chatLists);
        chatListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume(){
        super.onResume();

        MessageCallback messageCallback = new MessageCallback(ChatListActivity.this);
        application.anIM.setCallback(messageCallback);

        // Reload
        queryChatListFromLocalDB();


//        setProgressBarIndeterminateVisibility(true);
//        Thread t = new Thread(new Runnable(){
//            @Override
//            public void run() {
//                getAllUsersList();
//            }
//        });
//        t.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        application.anIM.setCallback(null);
    }

//    @Override
//    public void update(final Observable observable, final Object data) {
//        runOnUiThread(new Runnable(){
//            @Override
//            public void run() {
//                if(data instanceof Message){
//                    Message msgData = (Message)data;
//                    if(!msgData.readed){
//                    }
//                    chatListFragment.update(observable, data);
//
//                }else if(data instanceof IMManager.UpdateType && ((IMManager.UpdateType)data).equals(IMManager.UpdateType.Topic)){
//                    chatListFragment.update(observable, data);
//
//                }
//            }
//        });
//    }

    private void initView(){

        // Init RecyclerView and Adapter
        chatLists = new ArrayList<>();
        chatListAdapter = new ChatListAdapter(ChatListActivity.this,chatLists);
//        chatListAdapter.setPartiesList(partiesList);
        chatListAdapter.setChatLists(chatLists);
        chatListAdapter.setOnDataResetListener(new ChatListAdapter.OnDataResetListener() {
            @Override
            public void dataDidApply(int dataCount) {
            }
        });
        rv = (RecyclerView) findViewById(R.id.chat_recycler_view);
        rv.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(ChatListActivity.this);
        rv.setLayoutManager(mLayoutManager);
        rv.setAdapter(chatListAdapter);

    }

    private void getAllUsersList() {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("limit", 100);
//        params.put("username", "chi");
        params.put("sort", "-created_at");
        try {
            application.anSocial.sendRequest("users/query.json", AnSocialMethod.GET,
                    params, new IAnSocialCallback() {
                        @Override
                        public void onFailure(final JSONObject arg0) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    try {
                                        Toast.makeText(ChatListActivity.this, arg0.getJSONObject("meta").getString("message"), Toast.LENGTH_LONG).show();
                                        System.out.println("the error message: " + arg0.getJSONObject("meta").getString("message"));
                                    } catch (Exception e1) {
                                    }
                                }
                            });
                        }

                        @Override
                        public void onSuccess(final JSONObject arg0) {
                            Log.e("getAllUsersList", arg0.toString());
                            try {
                                final JSONArray usersArray = arg0.getJSONObject("response").getJSONArray("users");
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        application.mUsersMap.clear();
                                        partiesList.clear();
                                        chatListAdapter.notifyDataSetChanged();
                                        for (int i = 0; i < usersArray.length(); i++) {
                                            try {
                                                JSONObject user = (JSONObject) usersArray.get(i);
                                                if (user.getString("id").equals(application.mCircleId)) {
                                                    continue;
                                                }
                                                HashMap<String, String> item = new HashMap<String, String>();
                                                item.put("clientId", user.getString("clientId"));
                                                item.put("username", user.getString("username"));
                                                item.put("status", "Offline");
                                                partiesList.add(item);

                                                chatListAdapter.notifyDataSetChanged();

                                                application.mUsersMap.put(user.getString("clientId"), user.getString("username"));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        application.anIM.getClientsStatus(application.mUsersMap.keySet(), new IAnIMGetClientsStatusCallback() {
                                            @Override
                                            public void onSuccess(AnIMGetClientsStatusCallbackData anIMGetClientsStatusCallbackData) {
//                                                getClientStatus(anIMGetClientsStatusCallbackData);
                                            }

                                            @Override
                                            public void onError(ArrownockException e) {
                                                e.printStackTrace();
                                            }
                                        });
                                    }
                                });


                            } catch (Exception e) {
                                e.printStackTrace();
                                try {
                                    Toast.makeText(ChatListActivity.this,e.getMessage(), Toast.LENGTH_LONG).show();
                                } catch (Exception e1) {
                                }
                            }
                        }
                    });
        } catch (ArrownockException e) {
            e.printStackTrace();
        }
    }


}
