package client;

/**
 * AbstractController used by all Controllers communicating with the server
 */
public class AbstractController {

    /**
     * Stores the MessageService implementation injected by the constructor
     */
    private final IMessageService service;

    AbstractController(IMessageService service) {
        this.service = service;
    }

    /**
     * Getter for <code>service</code>
     * @return <code>service</code>
     */
    public IMessageService getService() {
        return service;
    }

}
