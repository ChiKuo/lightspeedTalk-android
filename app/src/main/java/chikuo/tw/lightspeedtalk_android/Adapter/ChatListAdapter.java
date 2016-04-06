package chikuo.tw.lightspeedtalk_android.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import chikuo.tw.lightspeedtalk_android.activity.ChatActivity;
import chikuo.tw.lightspeedtalk_android.ChatList;
import chikuo.tw.lightspeedtalk_android.R;

/**
 * Created by chiuo on 2015/12/8.
 */
public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {


//    String clientId = "AIMJYM8T2UCS5JFCD62UR8E";
    private List<ChatList> chatLists;
    private Context ct;

    public ChatListAdapter(Context ct,List<ChatList> chatLists) {
        this.ct = ct;
        this.chatLists = chatLists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_chat_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        ChatList chatList = chatLists.get(position);

        if (chatList.targetClientId != null){
            holder.textName.setText(chatList.targetClientId);
            // TODO find name by targetClientId
        }
        if (chatList.lastMessage != null){
            holder.textMsg.setText(chatList.lastMessage);
        }
        if (chatList.updateTime != null){
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            holder.textTime.setText(sdf.format(chatList.updateTime));
        }

        if (chatList.unReadCount > 0){
            holder.badge.setVisibility(View.VISIBLE);
            holder.badge.setText(String.valueOf(chatList.unReadCount));
        } else {
            holder.badge.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return chatLists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textName, textMsg, textTime;
        private ImageView imgIcon;
        private TextView badge;

        public ViewHolder(View itemView) {
            super(itemView);

            textName = (TextView) itemView.findViewById(R.id.chat_list_item_name);
            textMsg = (TextView) itemView.findViewById(R.id.chat_list_item_msg);
            textTime = (TextView) itemView.findViewById(R.id.chat_list_item_timestamp);
            imgIcon = (ImageView) itemView.findViewById(R.id.chat_list_item_icon);
            badge = (TextView) itemView.findViewById(R.id.chat_list_item_badge);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            try {
                ChatList clickChat = chatLists.get(getAdapterPosition());
                clickChat.read();

                Intent chatIntent = new Intent(ct,ChatActivity.class);
                ct.startActivity(chatIntent);
            } catch (ArrayIndexOutOfBoundsException e){
                e.printStackTrace();
            }
        }
    }

    public void setChatLists(List<ChatList> chatLists) {
        this.chatLists = chatLists;
    }

}
