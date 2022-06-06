package communication;

import java.io.Serializable;

/** Represents a Message used for communication client <-> server.
 *
 */
public class Message implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * The message data (e.g. User instance, List of Products etc..)
	 */
	private Object data;

	/**
	 * Describes the task the client is asking the server to perform
	 */
	private MessageFromClient task;

	/**
	 * Describes the result of the task completed by the server
	 */
	private MessageFromServer answer;

		
	public Message() {
		super();
		this.data = null;
		this.task = null;
		this.answer = null;
	}

	public Message(Object data, MessageFromServer answer) {
		super();
		this.data = data;
		this.answer = answer;
	}
	
	public Message(Object data, MessageFromClient task) {
		super();
		this.data = data;
		this.task = task;
	}
	
	public Object getData() {
		return data;
	}
	
	public void setData(Object data) {
		this.data = data;
	}
	
	public MessageFromClient getTask(){
		return task;
	}
	
	public void setTask(MessageFromClient task) {
		this.task = task;
	}
	
	public MessageFromServer getAnswer() {
		return answer;
	}
	
	public void setAnswer(MessageFromServer ExecutionStatus) {
		this.answer = ExecutionStatus;
	}
	
	@Override
	public String toString() {
		return "Message [data=" + data + ", task=" + task + ", answer=" + answer + "]";
	}
	
}
