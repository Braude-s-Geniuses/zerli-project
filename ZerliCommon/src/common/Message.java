package common;

import java.io.Serializable;

/** Represents a Message used for communication client <-> server.
 *
 */
public class Message implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Object data;
	private MessageFromClient task;
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
	
	public Message(Object data, MessageFromClient taskRequseted) {
		super();
		this.data = data;
		this.task = taskRequseted;
	}
	
	public Object getData() {
		return data;
	}
	
	public void setData(Object data) {
		this.data = data;
	}
	
	public MessageFromClient getTask() {
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
		return "Message [data=" + data + ", task=" + task + ", answer=" + answer
				+ "]";
	}
	
}
