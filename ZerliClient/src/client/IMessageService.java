package client;

import communication.Message;

public interface IMessageService {
    /**
     * this method is a wrapper for <code>handleMessageFromUI</code> inorder to enable
     * testing of functionality using it.
     * @param message - message to send to server
     * @param await - if the client should wait for server's response
     * @return server response if exists; null otherwise
     */
    public Message sendToServer(Object message, boolean await);

    /**
     * getter for server response
     * @return
     */
    public Message getResponse();

    /**
     * setter for server response
     * @param message
     */
    public void setResponse(Message message);
}
