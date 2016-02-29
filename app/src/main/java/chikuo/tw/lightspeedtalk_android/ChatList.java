package chikuo.tw.lightspeedtalk_android;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.Calendar;
import java.util.List;


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
    public long updateTime;

    @Column(name = "targetClientId")
    public String targetClientId;

    @Column(name = "lastMessage")
    public String lastMessage;

//    @Column(name = "Topic", onDelete = Column.ForeignKeyAction.CASCADE)
//    public Topic topic;
    
    public ChatList(){
        super();
    }
    
//    public List<Message> messages(){
//        return new Select().from(Message.class).where("Chat = \""+getId()+"\" and currentClientId = \""+currentClientId+"\"").execute();
//    }
//
//    public Message lastMessage(){
//    	return new Select().from(Message.class).where("Chat = \""+getId()+"\" and currentClientId = \""+currentClientId+"\"").orderBy("timestamp DESC").executeSingle();
//    }
//
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
                .orderBy("updateTime")
                .execute();
    }


    public void update(){
    	Calendar c = Calendar.getInstance();
    	updateTime = c.getTimeInMillis();

        ChatList exist;
//    	if(topic!=null){
//    		exist = new Select().from(Chat.class).where("Topic = \""+topic.getId()+"\" and currentClientId = \""+currentClientId+"\"").executeSingle();
//    	}else{
//    		exist = new Select().from(Chat.class).where("targetClientId = \""+targetClientId+"\" and currentClientId = \""+currentClientId+"\"").executeSingle();
//    	}
        exist = new Select().from(ChatList.class).where("targetClientId = \""+targetClientId+"\" and currentClientId = \""+currentClientId+"\"").executeSingle();

        if(exist == null){
    		save();
    	}else{
            exist.lastMessage = lastMessage;
    		exist.updateTime = updateTime;
    		exist.save();
    	}
    }
}
