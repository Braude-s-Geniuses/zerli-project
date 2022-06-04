package client;

import communication.Message;

public interface IMessageService {
    public Message sendToServer(Object message, boolean await);
    public Message getResponse();
    public void setResponse(Message message);
}
