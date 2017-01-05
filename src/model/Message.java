package model;


public class Message {

  public int messageId;

  public int senderId;

  public String message;

  public int receiverId;
  
  public String senderName;


  public int getSenderId() {
	  return this.senderId;
  }
  
  public String getSenderName() {
	  return this.senderName;
  }

  public int getMessageId() {
	  return this.messageId;
  }

  public String getMessage() {
	  return this.message;
  }

  public Message() {
	  this.messageId = -1;
	  this.senderId = -1;
	  this.message = "";
	  this.receiverId = -1;
	  this.senderName = "";
  }
  
  public Message(int messageId, int senderId, String message, int receiverId, String senderName) {
	  this.messageId = messageId;
	  this.senderId = senderId;
	  this.message = message;
	  this.receiverId = receiverId;
	  this.senderName = senderName;
  }

  public int getReceiverId() {
	  return this.receiverId;
  }

}