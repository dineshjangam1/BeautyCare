package model;

import java.util.ListIterator;
import java.util.Vector;

public class Messages {

    /**
   * 
   * @element-type Message
   */
  public Vector<Message>  messages;

  public void messages() {
  }
  
  public ListIterator<Message> getMessages()
  {
	  return messages.listIterator();
  }

}