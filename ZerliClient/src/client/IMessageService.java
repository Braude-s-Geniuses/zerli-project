package client;

import communication.Message;

/**
 * Interface for defining the steps before sending a message to the server
 * and once receiving a response from it
 */
public interface IMessageService {
    /**
     * this method is a wrapper for <code>handleMessageFromUI</code> inorder to enable
     * testing of functionality using it.
     * @param message - message to send to server
     * @param await - if the client should wait for server's response
     * @return server response if exists; null otherwise
     * @see {@link client.ZerliClient#handleMessageFromUI(Object, boolean)}
     */
    Message sendToServer(Object message, boolean await);

    /**
     * getter for server response
     * @return
     */
    Message getResponse();

    /**
     * setter for server response
     * @param message
     */
    void setResponse(Message message);
}
