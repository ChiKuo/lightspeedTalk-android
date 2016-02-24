package chikuo.tw.lightspeedtalk_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.WindowManager;
//
//import com.parse.GetCallback;
//import com.parse.ParseException;
//import com.parse.ParseFile;
//import com.parse.ParseQuery;
//import com.parse.ParseUser;
//
//import java.util.HashMap;
//import java.util.Map;
import java.util.Observable;
import java.util.Observer;

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
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		
//		IMManager.getInstance(this).addObserver(this);

//		initView();
//		checkBundle();

		actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setDisplayUseLogoEnabled(false);
		}
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
	    super.onNewIntent(intent);
	    setIntent(intent);
//		checkBundle();
	}
	
//	private void initView(){
//		mChatView = (ChatView)findViewById(R.id.chatView);
//	}
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
