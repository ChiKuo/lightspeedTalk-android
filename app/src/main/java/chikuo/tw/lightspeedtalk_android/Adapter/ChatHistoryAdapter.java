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
import java.util.Map;

import chikuo.tw.lightspeedtalk_android.Activity.ChatActivity;
import chikuo.tw.lightspeedtalk_android.MyMessage;
import chikuo.tw.lightspeedtalk_android.R;

/**
 * Created by chiuo on 2015/12/8.
 */
public class ChatHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<MyMessage> mData;
    private Map<String,String> mClientId2Name;
    private String mClientId;
    private Context ct;

    private static final int TYPE_ME = 0;
    private static final int TYPE_CLIENT = 1;

    public ChatHistoryAdapter(Context context, ArrayList<MyMessage> history, String clientId, Map<String, String> clientId2Name) {
        ct = context;
        mData = history;
        mClientId = clientId;
        mClientId2Name = clientId2Name;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        RecyclerView.ViewHolder itemViewHolder = null;
        switch (viewType) {
            case TYPE_ME: {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recycler_chat_me, parent, false);
                itemViewHolder = new MeViewHolder(v);
                break;
            }
            case TYPE_CLIENT: {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recycler_chat_client, parent, false);
                itemViewHolder = new ClientViewHolder(v);
                break;
            }
        }
        return itemViewHolder;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {

        MyMessage msg = mData.get(position);

        if (viewHolder instanceof ClientViewHolder) {

            ClientViewHolder holder = (ClientViewHolder)viewHolder;
//            holder.image.setImageDrawable();
            holder.messageTextView.setText(msg.getMsg());

        } else if (viewHolder instanceof MeViewHolder) {

            MeViewHolder holder = (MeViewHolder)viewHolder;
            holder.messageTextView.setText(msg.getMsg());
        }

    }

    @Override
    public int getItemViewType(int position) {

        MyMessage msg = mData.get(position);
        if (msg.getFrom().equals(mClientId)) {
            return TYPE_ME;
        } else {
            return TYPE_CLIENT;
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ClientViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView messageTextView;

        public ClientViewHolder(View itemView) {
            super(itemView);

            messageTextView = (TextView) itemView.findViewById(R.id.message_text_view);
            image = (ImageView) itemView.findViewById(R.id.imageView);
        }


    }

    public class MeViewHolder extends RecyclerView.ViewHolder {

        private TextView messageTextView;

        public MeViewHolder(View itemView) {
            super(itemView);

            messageTextView = (TextView) itemView.findViewById(R.id.message_text_view);
        }
    }

}
