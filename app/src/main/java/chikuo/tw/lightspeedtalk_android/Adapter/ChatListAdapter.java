package chikuo.tw.lightspeedtalk_android.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import chikuo.tw.lightspeedtalk_android.Activity.ChatActivity;
import chikuo.tw.lightspeedtalk_android.ChatList;
import chikuo.tw.lightspeedtalk_android.R;

/**
 * Created by chiuo on 2015/12/8.
 */
public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {


    List<ChatList> chatLists;
    private Context ct;
//    private Handler handler;

    public ChatListAdapter(Context ct) {
        this.ct = ct;
//        handler = new Handler();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_chat_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        ChatList chatList = chatLists.get(position);

//        holder.textName.setText(eachChat.get("clientId")); //Token
        holder.textName.setText(chatList.targetClientId);
        holder.textMsg.setText(chatList.lastMessage);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        holder.textTime.setText(sdf.format(chatList.updateTime));

//            // Show unread message count
//            holder.badge.setBadgeCount(chat.unReadedMessages().size());
//
//        }

    }


    @Override
    public int getItemCount() {
        return chatLists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

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

        }
    }


    public void setChatLists(List<ChatList> chatLists) {
        this.chatLists = chatLists;
    }



    // TODO Check use
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
