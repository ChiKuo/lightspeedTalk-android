package chikuo.tw.lightspeedtalk_android;

public class MyMessage {
	private String From ="";
	private String Type ="";
	private String Msg ="";
	private String Data ="";
	private String Date ="";
	private byte[] Content;
	
	/**
	 * 
	 * @param from ClientId
	 * @param type Message type
	 * @param msg Text
	 * @param data Hyperlink or latlng
	 * @param content Image binary data
	 */
	public MyMessage(String from, String type, String msg, String data, String date, byte[] content){
		this.From = from;
		this.Msg = msg;
		this.Data = data;
		this.Date = date;
		this.Content = content;
		if(type == null){
			this.Type = Utils.Constant.AttachmentType.NULL;
		}else{
			this.Type = type;
		}
	}
	
	public MyMessage(){
		this.From = "";
		this.Msg = "";
		this.Data = "";
		this.Type = "";
		this.Content = new byte[0];
	}
	
	public String toString(){
		return "From: "+getFrom()+"\nType: "+getType()+"\nMsg: "+getMsg()+"\nData: "+getData()+"\nContent.size: "+getContent().length;
	}
	
	public String getFrom(){
		return From == null ? " ":From;
	}
	public String getType(){
		return Type == null ? " ":Type;
	}
	public String getMsg(){
		return Msg == null ? " ":Msg;
	}
	public String getData(){
		return Data == null ? " ":Data;
	}
	public String getDate(){
		return Date == null ? " ":Date;
	}
	public byte[] getContent(){
		return Content == null ? new byte[0]:Content;
	}
	
	public void setFrom(String from){
		From = from;
	}
	public void setType(String type){
		Type = type;
	}
	public void setMsg(String msg){
		Msg = msg;
	}
	public void setData(String data){
		Data = data;
	}
	public void setDate(String date){
		Data = date;
	}
	public void setContent(byte[] content){
		Content = content;
	}
}
