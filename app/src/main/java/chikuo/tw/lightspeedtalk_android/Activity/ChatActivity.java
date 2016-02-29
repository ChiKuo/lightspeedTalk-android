package chikuo.tw.lightspeedtalk_android.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
//
//import com.parse.GetCallback;
//import com.parse.ParseException;
//import com.parse.ParseFile;
//import com.parse.ParseQuery;
//import com.parse.ParseUser;
//
//import java.util.HashMap;
//import java.util.Map;
import com.arrownock.exception.ArrownockException;
import com.arrownock.im.AnIMMessage;
import com.arrownock.im.AnIMMessageType;
import com.arrownock.im.callback.AnIMBinaryCallbackData;
import com.arrownock.im.callback.AnIMCallbackAdapter;
import com.arrownock.im.callback.AnIMMessageCallbackData;
import com.arrownock.im.callback.AnIMMessageSentCallbackData;
import com.arrownock.im.callback.AnIMStatusUpdateCallbackData;
import com.arrownock.im.callback.AnIMTopicBinaryCallbackData;
import com.arrownock.im.callback.AnIMTopicMessageCallbackData;
import com.arrownock.im.callback.IAnIMHistoryCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import chikuo.tw.lightspeedtalk_android.Adapter.ChatHistoryAdapter;
import chikuo.tw.lightspeedtalk_android.Application;
import chikuo.tw.lightspeedtalk_android.MyMessage;
import chikuo.tw.lightspeedtalk_android.R;
import chikuo.tw.lightspeedtalk_android.Utils;

//import tw.soleil.ibento_android.util.BentoLightSpeedUserManager;
//import tw.soleil.ibento_android.view.ChatView;
//import tw.soleil.lightspeedim.Constant;
//import tw.soleil.lightspeedim.IMManager;
//import tw.soleil.lightspeedim.model.Chat;
//import tw.soleil.lightspeedim.model.ChatUser;
//import tw.soleil.lightspeedim.model.TopicMember;
//import tw.soleil.lightspeedim.model.User;

public class ChatActivity extends AppCompatActivity implements Observer{
//	private ChatView mChatView;
//	private Chat mChat;

	private Application application;
	private ActionBar actionBar;

	private int roomType = Utils.Constant.RoomType.CLIENT;
	private RecyclerView chatHistoryRv;
	private ChatHistoryAdapter chatHistoryAdapter;
	private ArrayList<MyMessage> history;

	// TODO send data
	private String targetId = "AIMOM1Y3KT3T8DV8YMUN5U5";
	private MyMessage messageToSend;
	private EditText messageEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		application = (Application) getApplicationContext();

		initView();
		getHistory();

//		checkBundle();

		actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setDisplayUseLogoEnabled(false);
			actionBar.setTitle(targetId);
		}
	}


	@Override
	protected void onPause() {
		super.onPause();
		application.anIM.setCallback(null);
	}

	@Override
	protected void onResume() {
		super.onResume();
		application.anIM.setCallback(messageCallback);
	}

	private void initView() {
		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
		chatHistoryRv = (RecyclerView) findViewById(R.id.chat_history_rv);
		chatHistoryRv.setLayoutManager(layoutManager);
		history = new ArrayList<MyMessage>();
		chatHistoryAdapter = new ChatHistoryAdapter(ChatActivity.this, history,application.mClientId,application.mUsersMap);
		chatHistoryRv.setAdapter(chatHistoryAdapter);

		// Send message button
		Button sendButton = (Button) findViewById(R.id.send_button);
		sendButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				messageEditText = (EditText) findViewById(R.id.message_input_edit_text);
				messageToSend = new MyMessage(
						application.mClientId,
						Utils.Constant.AttachmentType.TEXT,
						messageEditText.getText().toString().trim(),
						null,
						null
				);
				sendMsg(messageToSend);
			}
		});
	}

	@Override
	protected void onNewIntent(Intent intent) {
	    super.onNewIntent(intent);
	    setIntent(intent);
//		checkBundle();
	}

//
//	private void checkBundle(){
//		if(getIntent().getExtras()!=null && getIntent().getExtras().containsKey(Constant.INTENT_EXTRA_KEY_CHAT)){
//			mChat = (Chat) getIntent().getExtras().getSerializable(Constant.INTENT_EXTRA_KEY_CHAT);
//
//			User mUser = BentoLightSpeedUserManager.getInstance(this).getCurrentUser();
//			final ChatUser cUser = new ChatUser(mUser.clientId,mUser.userName,mUser.userPhotoUrl);
//			mChatView.setChat(cUser,mChat.getFromTable());
//
//			final Map<String,ChatUser> userMap = new HashMap<String,ChatUser>();
//
//			if(mChat.topic == null){
//				// Personal
//				final String userClientId = mChat.targetClientId;
//
//				if (userClientId!=null) {
//					// Check user
//					ParseQuery<ParseUser> parseUserParseQuery = ParseUser.getQuery();
//					parseUserParseQuery.whereEqualTo("lightspeedUserId", mChat.targetClientId);
//					parseUserParseQuery.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
//					parseUserParseQuery.setMaxCacheAge(6 * 60 * 1000);
//					parseUserParseQuery.getFirstInBackground(new GetCallback<ParseUser>() {
//						@Override
//						public void done(ParseUser parseUser, ParseException e) {
//							if (e == null && parseUser != null){
//
//								String userName = parseUser.getString("nickName");
//								ParseFile userImage = parseUser.getParseFile("imageFile");
//
//								if (userName != null){
//									// Set userName , Let it show on notification
//									cUser.setUsername(userName);
//									mChatView.setChat(cUser,mChat.getFromTable());
//								}
//
//								if(userImage != null){
//									userMap.put(userClientId, new ChatUser(userClientId, userName, userImage.getUrl()));
//								}else{
//									userMap.put(userClientId, new ChatUser(userClientId, userName, null));
//								}
//								mChatView.setUserMap(userMap);
//								// Show interlocutor name on actionBar
//								if (userMap.get(mChat.targetClientId).getUsername() != null){
//									actionBar.setTitle(userMap.get(mChat.targetClientId).getUsername());
//								}
//							}
//
//						}
//					});
//
//				}
//			} else{
//				// Group
//				for(TopicMember topicMember : mChat.topic.members()){
//					User user = BentoLightSpeedUserManager.getInstance(this).getUserByClientId(topicMember.clientId);
//					if(user!=null)
//						userMap.put(user.clientId,  new ChatUser(user.clientId,user.userName,user.userPhotoUrl));
//				}
//			}
//			mChatView.setUserMap(userMap);
//		}
//	}

	public void sendMsg(MyMessage mymessage) {

		String type = mymessage.getType();
		String msg = mymessage.getMsg();
		String data = mymessage.getData();
		byte[] content = mymessage.getContent();

		try {
			if(type == Utils.Constant.AttachmentType.IMAGE){
				if (roomType == Utils.Constant.RoomType.CLIENT) {
					application.anIM.sendBinary(targetId, content,Utils.Constant.AttachmentType.IMAGE);
				} else if (roomType == Utils.Constant.RoomType.TOPIC) {
					application.anIM.sendBinaryToTopic(targetId, content,Utils.Constant.AttachmentType.IMAGE);
				}
			}else if(type.equals(Utils.Constant.AttachmentType.TEXT)){
				Log.i("send Msg to " + targetId, "message: " + msg);
				if (roomType == Utils.Constant.RoomType.CLIENT) {
					application.anIM.sendMessage(targetId, msg);
				} else if (roomType == Utils.Constant.RoomType.TOPIC) {
					application.anIM.sendMessageToTopic(targetId, msg);
				}
			}else{
				HashMap map = new HashMap();
				map.put(Utils.Constant.MsgCustomData.DATA, data);
				map.put(Utils.Constant.MsgCustomData.TYPE, type);
				if (roomType == Utils.Constant.RoomType.CLIENT) {
					application.anIM.sendMessage(targetId, msg, map);
				} else if (roomType == Utils.Constant.RoomType.TOPIC) {
					application.anIM.sendMessageToTopic(targetId, msg, map);
				}
			}
		} catch (ArrownockException e) {
			e.printStackTrace();
			Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}


	public void getHistory() {
		IAnIMHistoryCallback historyCallback = new IAnIMHistoryCallback() {
			@Override
			public void onError(final ArrownockException arg0) {
				Log.e("getHistory", arg0.getMessage());
				runOnUiThread(new Runnable(){
					public void run() {
						Toast.makeText(getBaseContext(), arg0.getMessage(), Toast.LENGTH_LONG).show();
					}
				});

			}
			@Override
			public void onSuccess(final List mHistory, int arg1) {
				final List<AnIMMessage> historyList = mHistory;
				runOnUiThread(new Runnable() {
					public void run() {
						history.clear();
						for (int i = historyList.size()-1; i >=0; i--) {
							MyMessage mMessage;
							if(historyList.get(i).getType() == AnIMMessageType.AnIMBinaryMessage){
								mMessage = new MyMessage(
										historyList.get(i).getFrom(),
										historyList.get(i).getFileType(),
										null,
										null,
										historyList.get(i).getContent()
								);
							}else{
								Map customData = historyList.get(i).getCustomData();
								if(customData != null){
									String customData_data = customData.containsKey(Utils.Constant.MsgCustomData.DATA) ?
											(String) customData.get(Utils.Constant.MsgCustomData.DATA) : null;
									String customData_type = customData.containsKey(Utils.Constant.MsgCustomData.TYPE) ?
											(String) customData.get(Utils.Constant.MsgCustomData.TYPE) : null;

									mMessage = new MyMessage(
											historyList.get(i).getFrom(),
											customData_type,
											historyList.get(i).getMessage(),
											customData_data,
											historyList.get(i).getContent()
									);
								}else{
									mMessage = new MyMessage(
											historyList.get(i).getFrom(),
											Utils.Constant.AttachmentType.TEXT,
											historyList.get(i).getMessage(),
											null,
											null
									);
								}
							}
							history.add(mMessage);
						}
						updateHistoryList(true);
					}
				});
			}
		};
		if (roomType == Utils.Constant.RoomType.CLIENT) {
			Set<String> targetIds;
			targetIds = new HashSet();
			targetIds.add(targetId);
			application.anIM.getHistory(targetIds, application.mClientId, 30, 0, historyCallback);
		} else if (roomType == Utils.Constant.RoomType.TOPIC) {
			application.anIM.getTopicHistory(targetId, application.mClientId, 30, 0, historyCallback);
		}
	}

	public void updateHistoryList(Boolean scroll) {
		chatHistoryAdapter.notifyDataSetChanged();
		if(scroll)
			chatHistoryRv.smoothScrollToPosition(chatHistoryRv.getBottom());
	}

	AnIMCallbackAdapter messageCallback = new AnIMCallbackAdapter() {
		@Override
		public void receivedTopicMessage(AnIMTopicMessageCallbackData callbackData) {
			final String from = callbackData.getFrom();
			final String fromTopic = callbackData.getTopic();
			final String message = callbackData.getMessage();
			final Map<String, String> customData = callbackData.getCustomData();
			final String type = customData == null ?
					Utils.Constant.AttachmentType.TEXT:customData.get(Utils.Constant.MsgCustomData.TYPE);
			final String data = customData == null ?
					null:customData.get(Utils.Constant.MsgCustomData.DATA);

			if (roomType == Utils.Constant.RoomType.TOPIC && targetId.equals(fromTopic)) {
				runOnUiThread(new Runnable() {
					public void run() {
						MyMessage mMessage = new MyMessage(
								from,
								type,
								message,
								data,
								null
						);
						history.add(mMessage);
						updateHistoryList(true);
					}
				});
			} else {
				runOnUiThread(new Runnable() {
					public void run() {
						if (type.equals(Utils.Constant.AttachmentType.TEXT)){
							Toast.makeText(getBaseContext(),"["+application.mTopicsMap.get(fromTopic)+"] "+application.mUsersMap.get(from)+" : " + message, Toast.LENGTH_LONG).show();
						}else{
							Toast.makeText(getBaseContext(),"["+application.mTopicsMap.get(fromTopic)+"] "+application.mUsersMap.get(from)+" : [" + customData.get("type") + "]", Toast.LENGTH_LONG).show();
						}
					}
				});
			}

		}

		@Override
		public void receivedMessage(AnIMMessageCallbackData callbackData) {
			String msgId = callbackData.getMsgId();
			final String from = callbackData.getFrom();
			final String message = callbackData.getMessage();
			final Map<String, String> customData = callbackData.getCustomData();
			final String type = customData==null ?
					Utils.Constant.AttachmentType.TEXT:customData.get(Utils.Constant.MsgCustomData.TYPE);
			final String data = customData==null ?
					null:customData.get(Utils.Constant.MsgCustomData.DATA);

			if (roomType == Utils.Constant.RoomType.CLIENT && from.equals(targetId)) {
				runOnUiThread(new Runnable() {
					public void run() {
						MyMessage mMessage = new MyMessage(
								from,
								type,
								message,
								data,
								null
						);
						history.add(mMessage);
						updateHistoryList(true);
					}
				});
			} else {
				runOnUiThread(new Runnable() {
					public void run() {
						if (type.equals(Utils.Constant.AttachmentType.TEXT)){
							Toast.makeText(getBaseContext(),application.mUsersMap.get(from)+" : " + message, Toast.LENGTH_LONG).show();
						}else{
							Toast.makeText(getBaseContext(),application.mUsersMap.get(from)+" : [" + customData.get("type") + "]", Toast.LENGTH_LONG).show();
						}
					}
				});
			}

			// Already read
			try {
				application.anIM.sendReadACK(from, msgId);
			} catch (ArrownockException e) {
				e.printStackTrace();
			}

		}

		@Override
		public void receivedBinary(final AnIMBinaryCallbackData callbackData){
			if (roomType == Utils.Constant.RoomType.CLIENT && callbackData.getFrom().equals(targetId)) {
				runOnUiThread(new Runnable() {
					public void run() {
						MyMessage mMessage = new MyMessage(
								callbackData.getFrom(),
								callbackData.getFileType(),
								null,
								null,
								callbackData.getContent()
						);
						history.add(mMessage);
						updateHistoryList(true);
					}
				});
			}else{
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(getBaseContext(),application.mUsersMap.get(callbackData.getFrom())+" : ["+callbackData.getFileType()+"]", Toast.LENGTH_LONG).show();
					}
				});
			}
		}

		@Override
		public void receivedTopicBinary(final AnIMTopicBinaryCallbackData callbackData){
			if (roomType == Utils.Constant.RoomType.TOPIC && callbackData.getTopic().equals(targetId)) {
				runOnUiThread(new Runnable() {
					public void run() {
						MyMessage mMessage = new MyMessage(
								callbackData.getFrom(),
								callbackData.getFileType(),
								null,
								null,
								callbackData.getContent()
						);
						history.add(mMessage);
						updateHistoryList(true);
					}
				});
			}else{
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(getBaseContext(),"["+application.mTopicsMap.get(callbackData.getTopic())+"] "+application.mUsersMap.get(callbackData.getFrom())+" : ["+callbackData.getFileType()+"]", Toast.LENGTH_LONG).show();
					}
				});
			}
		}

		@Override
		public void messageSent(final AnIMMessageSentCallbackData data){
			if(data.isError()){
				runOnUiThread(new Runnable(){
					public void run() {
						Toast.makeText(getBaseContext(), data.getException().getMessage(), Toast.LENGTH_LONG).show();
					}
				});
			}else{
				runOnUiThread(new Runnable() {
					public void run() {
						MyMessage mMessage = new MyMessage(
								application.mClientId,
								messageToSend.getType(),
								messageToSend.getMsg(),
								messageToSend.getData(),
								messageToSend.getContent()
						);
						history.add(mMessage);

						InputMethodManager imm = (InputMethodManager) ChatActivity.this
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(messageEditText.getWindowToken(), 0);
						messageEditText.setText("");
						messageToSend = new MyMessage();

						updateHistoryList(true);
					}
				});
			}
		}
		@Override
		public void statusUpdate(final AnIMStatusUpdateCallbackData data){
			final ArrownockException e = data.getException();
			if(e == null){
				runOnUiThread(new Runnable(){
					public void run() {
						Toast.makeText(getBaseContext(), data.getStatus().name(), Toast.LENGTH_LONG).show();
					}
				});
			}else{
				runOnUiThread(new Runnable(){
					public void run() {
						Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
					}
				});
				if (data.getException().getErrorCode() == ArrownockException.IM_FORCE_CLOSED
						|| data.getException().getErrorCode() == ArrownockException.IM_FAILED_DISCONNECT) {
					try {
						application.anIM.disconnect();
					} catch (ArrownockException e1) {
						e1.printStackTrace();
					}
					Intent it = new Intent();
					it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					it.setClass(getBaseContext(), MainActivity.class);
					startActivity(it);
					finish();
				}
			}
		}
	};

	@Override
	public void onBackPressed(){
		super.onBackPressed();
		overridePendingTransition(android.R.anim.fade_in,android.R.anim.slide_out_right);
	}

	@Override
	public void update(Observable observable, Object data) {
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home: {
				finish();
				break;
			}
		}
		return super.onOptionsItemSelected(item);
	}
}
