package chikuo.tw.lightspeedtalk_android;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;


import java.util.Calendar;
import java.util.Date;
import java.util.List;

import chikuo.tw.lightspeedtalk_android.util.ChatListReloadEvent;
import de.greenrobot.event.EventBus;


/**
 * ActiveAndroid Guide:
 * https://guides.codepath.com/android/activeandroid-guide#overview
 * https://github.com/pardom/ActiveAndroid/wiki/Getting-started
 */

@Table(name = "ChatList")
public class ChatList extends Model {

    @Column(name = "currentClientId")
    public String currentClientId;
    
    @Column(name = "updateTime")
    public Date updateTime;

    @Column(name = "targetClientId")
    public String targetClientId;

    @Column(name = "lastMessage")
    public String lastMessage;

    @Column(name = "unReadCount")
    public int unReadCount;

//    @Column(name = "Topic", onDelete = Column.ForeignKeyAction.CASCADE)
//    public Topic topic;
    
    public ChatList(){
        super();
    }

//    public List<Message> unReadedMessages(){
//        return new Select().from(Message.class).where("Chat = \""+getId()+"\" and readed = 0 and currentClientId = \""+currentClientId+"\"").execute();
//    }
//
//    public Chat getFromTable(){
//    	if(topic!=null){
//    		return new Select().from(Chat.class).where("Topic = \""+topic.getFromTable().getId()+"\" and currentClientId = \""+currentClientId+"\"").executeSingle();
//    	}else{
//    		return new Select().from(Chat.class).where("targetClientId = \""+targetClientId+"\" and currentClientId = \""+currentClientId+"\"").executeSingle();
//    	}
//    }

    public static List<ChatList> getAll(String currentClientId) {
        // Execute a query
        return new Select()
                .from(ChatList.class)
                .where("currentClientId = ?", currentClientId)
                .orderBy("updateTime" + " DESC")
                .execute();
    }

    // When user need to update ChatList
    public void update(boolean read){
        Date date = new Date();

        ChatList exist;
        exist = new Select().from(ChatList.class).where("targetClientId = \""+targetClientId+"\" and currentClientId = \""+currentClientId+"\"").executeSingle();

        if(exist == null){
            this.updateTime = date;
            save();
        } else {
            if (lastMessage != null){
                exist.lastMessage = lastMessage;
            }
            if (read){
                exist.unReadCount = exist.unReadCount + 1;
            }
            exist.updateTime = date;
            exist.save();
        }

        // Reload ChatList
        EventBus.getDefault().post(new ChatListReloadEvent());
    }

    // When user read message
    public void read(){

        ChatList exist;
        exist = new Select().from(ChatList.class).where("targetClientId = \""+targetClientId+"\" and currentClientId = \""+currentClientId+"\"").executeSingle();

        if(exist != null){
            exist.unReadCount = 0;
            exist.save();
        }

        // Reload ChatList
        EventBus.getDefault().post(new ChatListReloadEvent());
    }

//        // Create a ChatList
//        ChatList chatList = new ChatList();
//        chatList.currentClientId = clientId;
//        chatList.targetClientId = "AIMOM1Y3KT3T8DV8YMUN5U5";
//        chatList.lastMessage = "Hi";
//        chatList.unReadCount = 3;
//        chatList.update();
//
//        //  Delete
//        chatLists.get(0).delete();
}
