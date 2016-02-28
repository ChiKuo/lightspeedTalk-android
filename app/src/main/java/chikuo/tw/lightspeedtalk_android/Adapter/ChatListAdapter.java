package chikuo.tw.lightspeedtalk_android.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import chikuo.tw.lightspeedtalk_android.Activity.ChatActivity;
import chikuo.tw.lightspeedtalk_android.R;

/**
 * Created by chiuo on 2015/12/8.
 */
public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

//    private List<Chat> chatList;
//    private List<Chat> filteredChatList;
//    private Map<String, User> userMap;
    ArrayList<HashMap<String, String>> partiesList;
    private Context ct;
//    private Handler handler;

    public ChatListAdapter(Context ct) {
        this.ct = ct;
//        chatList = new ArrayList<Chat>();
//        filteredChatList = new ArrayList<Chat>();
//        userMap = new HashMap<String, User>();
//        handler = new Handler();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_chat_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        HashMap<String, String> eachChat = partiesList.get(position);

//        holder.textName.setText(eachChat.get("clientId")); //Token
        holder.textName.setText(eachChat.get("username"));

//        holder.textMsg.setText(eachChat.message);
//        holder.imgIcon.setImageResource(R.drawable.friend_group);



//        Chat chat = filteredChatList.get(position);
//        if (chat.topic != null) {
//            // Group
//            holder.textName.setText(chat.topic.topicName + "(" + chat.topic.members().size() + ")");
//            holder.imgIcon.setImageResource(R.drawable.friend_group);
//
//        } else if (chat.targetClientId != null) {
//            // Personal
//            if (userMap.containsKey(chat.targetClientId)) {
//                holder.textName.setText(userMap.get(chat.targetClientId).userName);
//                if(userMap.get(chat.targetClientId).userPhotoUrl!=null){
//                    Ion.with(ct)
//                            .load(userMap.get(chat.targetClientId).userPhotoUrl)
//                            .withBitmap()
//                            .placeholder(R.drawable.friend_default)
//                            .transform(new CircleTransform())
//                            .intoImageView(holder.imgIcon);
//                }
//            }else{
//                holder.imgIcon.setImageResource(R.drawable.friend_default);
//            }
//        }
//
//        Message lastMsg = chat.lastMessage();
//        if (lastMsg != null) {
//            if (lastMsg.type.equals(Message.TYPE_TEXT)) {
//                // Last message is text
//                holder.textMsg.setText(lastMsg.message);
//
//            } else if (lastMsg.type.equals(Message.TYPE_IMAGE)) {
//                // Last message is image
//                if (lastMsg.fromClient.equals(IMManager.getInstance(ct).getCurrentClientId())) {
//                    // Show : You sent an image
//                    holder.textMsg.setText(R.string.chat_send_image);
//                } else {
//                    // Show : XXX send you an image
//                    String text = ct.getString(R.string.chat_received_image);
//                    text = text.replace("#", lastMsg.fromUsername);
//                    holder.textMsg.setText(text);
//                }
//            } else if (lastMsg.type.equals(Message.TYPE_RECORD)) {
//                // Last message is a voice message
//                if (lastMsg.fromClient.equals(IMManager.getInstance(ct).getCurrentClientId())) {
//                    // Show : You sent  a voice message
//                    holder.textMsg.setText(R.string.chat_send_record);
//                } else {
//                    // Show : XXX sent you a voice message
//                    String text = ct.getString(R.string.chat_received_record);
//                    text = text.replace("#", lastMsg.fromUsername);
//                    holder.textMsg.setText(text);
//                }
//            }
//
//            // Show last message time
//            Calendar c = Calendar.getInstance();
//            c.setTimeInMillis(lastMsg.timestamp);
//            holder.textTime.setText(new SimpleDateFormat("yyyy-MM-dd kk:mm").format(c.getTime()));
//
//            // Show unread message count
//            holder.badge.setBadgeCount(chat.unReadedMessages().size());
//
//        }

    }


    @Override
    public int getItemCount() {
//        return filteredChatList.size();
        return partiesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        private TextView textName, textMsg, textTime;
        private ImageView imgIcon;
//        private BadgeView badge;

        public ViewHolder(View itemView) {
            super(itemView);
//
            textName = (TextView) itemView.findViewById(R.id.chat_list_item_name);
            textMsg = (TextView) itemView.findViewById(R.id.chat_list_item_msg);
            textTime = (TextView) itemView.findViewById(R.id.chat_list_item_timestamp);
            imgIcon = (ImageView) itemView.findViewById(R.id.chat_list_item_icon);
//            badge = (BadgeView) itemView.findViewById(R.id.chat_list_item_badge);
//            badge.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
//            badge.setTextColor(ct.getResources().getColor(R.color.no5));
//            badge.setBadgeColor(ct.getResources().getColor(R.color.no1));
//
            itemView.setOnClickListener(this);
//            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Intent chatIntent = new Intent(ct,ChatActivity.class);
            ct.startActivity(chatIntent);

//            // Show the chat detail
//            Chat chat = filteredChatList.get(getAdapterPosition());
//
//            if (chat.topic != null) {
//                // Group
////                Intent i = new Intent(ct,ChatActivity.class);
////                Bundle b = new Bundle();
////                b.putSerializable(Constant.INTENT_EXTRA_KEY_CHAT, chat);
////                i.putExtras(b);
////                ct.startActivity(i);
//            } else {
//                // Personal
//                if (chat.targetClientId != null) {
//                    Intent i = new Intent(ct, ChatActivity.class);
//                    Bundle b = new Bundle();
//                    b.putSerializable(Constant.INTENT_EXTRA_KEY_CHAT, chat);
//                    i.putExtras(b);
//                    ct.startActivity(i);
//                }
//            }
        }


        @Override
        public boolean onLongClick(View v) {

//            final Chat chat = filteredChatList.get(getAdapterPosition());
//
//            // Delete the message
//            AlertDialog.Builder dialogBuiler = new AlertDialog.Builder(ct);
//            dialogBuiler.setTitle(R.string.chat_delete_chat_confirm);
//            dialogBuiler.setPositiveButton(ct.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                    IMManager.getInstance(ct).deleteChat(chat);
//                    removeItem(getAdapterPosition());
//                }
//            });
//            dialogBuiler.setNegativeButton(ct.getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//            dialogBuiler.create().show();

            return false;
        }
    }

//    public void removeItem(int position) {
//        filteredChatList.remove(position);
//        notifyDataSetChanged();
//    }
//
//    public void fillLocalData() {
//        IMManager.getInstance(ct).getAllMyChat(new IMManager.GetChatCallback() {
//            @Override
//            public void onFinish(List<Chat> data) {
//
//                chatList.clear();
//                chatList.addAll(data);
//
//                filter(null);
//
//                refreshUseMap();
//                notifyDataSetChanged();
//
//                if (onDataResetListener != null && data != null) {
//                    onDataResetListener.dataDidApply(data.size());
//                }
//            }
//        });
//    }
//    public void filter(String charText) {
//        filteredChatList.clear();
//        if (charText == null || charText.length() == 0) {
//            filteredChatList.addAll(chatList);
//        } else {
//            for (Chat chat : chatList) {
//                if (chat.topic != null && chat.topic.topicName.contains(charText)) {
//                    filteredChatList.add(chat);
//                } else if (chat.targetClientId != null) {
//                    if (userMap.containsKey(chat.targetClientId) && userMap.get(chat.targetClientId).userName.contains(charText)) {
//                        filteredChatList.add(chat);
//                    }
//                }
//            }
//        }
//        notifyDataSetChanged();
//    }
//    private void refreshUseMap() {
//        final List<Chat> chats = new ArrayList<Chat>();
//        chats.addAll(chatList);
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for (Chat chat : chats) {
//                    if (chat.topic == null && chat.targetClientId != null && !userMap.containsKey(chat.targetClientId)) {
//                        User user = getUserData(chat.targetClientId);
//                        userMap.put(chat.targetClientId, user);
//                    }
//                }
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        notifyDataSetChanged();
//                    }
//                });
//
//            }
//        }).start();
//    }
//
//
//    private User getUserData(String lightspeedUserId) {
//        User user = new User();
//        user.clientId = lightspeedUserId;
//
//        // User query
//        ParseQuery<ParseUser> parseUserParseQuery = ParseUser.getQuery();
//        parseUserParseQuery.whereEqualTo("lightspeedUserId", lightspeedUserId);
//        try {
//            ParseUser parseUser = parseUserParseQuery.getFirst();
//            user.userName = parseUser.getString("nickName");
//            if(parseUser.getParseFile("imageFile") != null){
//                user.userPhotoUrl = parseUser.getParseFile("imageFile").getUrl();
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        return user;
//    }


    public ArrayList<HashMap<String, String>> getPartiesList() {
        return partiesList;
    }

    public void setPartiesList(ArrayList<HashMap<String, String>> partiesList) {
        this.partiesList = partiesList;
    }


    private OnDataResetListener onDataResetListener;

    public OnDataResetListener getOnDataResetListener() {
        return onDataResetListener;
    }

    public void setOnDataResetListener(OnDataResetListener onDataResetListener) {
        this.onDataResetListener = onDataResetListener;
    }

    public interface OnDataResetListener {
        public void dataDidApply(int dataCount);
    }

}
