package client;

import communication.Message;

public class AbstractController {

    private IMessageService service;

    AbstractController(IMessageService service) {
        this.service = service;
    }

    public IMessageService getService() {
        return service;
    }

}
