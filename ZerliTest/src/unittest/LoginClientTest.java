package unittest;

import branch.Branch;
import client.IMessageService;
import client.UserController;
import communication.Message;
import communication.MessageFromServer;

import org.junit.Before;
import org.junit.Test;
import user.BranchEmployee;
import user.Customer;
import user.User;
import user.UserType;

import static org.junit.Assert.*;


public class LoginClientTest {

    //Stub class for MessageService to mock DB functionality
    private class MessageServiceStub implements IMessageService {
        private Message response;

        public MessageServiceStub() {
        }

        @Override
        public Message sendToServer(Object message, boolean await) {
            Message msg = (Message) message;
            User user = (User) msg.getData();

            if (user.getPassword().equals("") || user.getUsername().equals("")) {
                setResponse(new Message(null, MessageFromServer.LOGIN_FAIL));
                return getResponse();
            }



            switch (user.getUsername()) {
                case "customer":
                    setResponse(new Message(setUpCustomer(user), MessageFromServer.LOGIN_SUCCESS));
                    break;
                case "branch":
                    setResponse(new Message(setUpBranchEmployee(user), MessageFromServer.LOGIN_SUCCESS));
                    break;
                case "ceo":
                    setResponse(new Message(setUpRegularUser(user, UserType.CEO), MessageFromServer.LOGIN_SUCCESS));
                    break;
                case "expert":
                    setResponse(new Message(setUpRegularUser(user, UserType.EXPERT_SERVICE_EMPLOYEE), MessageFromServer.LOGIN_SUCCESS));
                    break;
                case "branch_manager":
                    setResponse(new Message(setUpRegularUser(user, UserType.BRANCH_MANAGER), MessageFromServer.LOGIN_SUCCESS));
                    break;
                case "service":
                    setResponse(new Message(setUpRegularUser(user, UserType.SERVICE_EMPLOYEE), MessageFromServer.LOGIN_SUCCESS));
                    break;
                case "delivery":
                    setResponse(new Message(setUpRegularUser(user, UserType.DELIVERY_OPERATOR), MessageFromServer.LOGIN_SUCCESS));
                    break;
                case "alreadyLoggedIn":
                    setResponse(new Message(null,MessageFromServer.ALREADY_LOGGED_IN));
                    break;
                case "blocked":
                    setResponse(new Message(null,MessageFromServer.CUSTOMER_IS_BLOCKED));
                    break;

            }
            return getResponse();
        }

        @Override
        public Message getResponse() {
            return response;
        }

        @Override
        public void setResponse(Message message) {
            response = message;
        }

    }

    private String username;
    private String password;
    private UserController userController;

    @Before
    public void setUp() {
        username = "";
        password = "";
        userController = new UserController(new MessageServiceStub());
    }

    // description: test login customer successful
    // input:  username = "customer", password = "customer"
    // expected result: Customer object fill with the correct data and LOGIN_SUCCESS message
    @Test
    public void customerLoginSucceed() {
        username = "customer";
        password = "customer";

        //Method under test
        Message actual = userController.login(username, password);

        //expected
        User user = setUser(username, password);
        Customer customer = setUpCustomer(user);
        Message expected = new Message(customer, MessageFromServer.LOGIN_SUCCESS);

        assertEquals(actual.getAnswer(), expected.getAnswer());
        assertEquals(actual.getData(), expected.getData());
    }

    // description: test login branch employee successful
    // input: username = "branch", password = "branch"
    // expected result: BranchEmployee object fill with the correct data and LOGIN_SUCCESS message
    @Test
    public void branchEmployeeLoginSucceed() {
        username = "branch";
        password = "branch";

        //Method under test
        Message actual = userController.login(username, password);

        //expected
        User user = setUser(username, password);
        BranchEmployee branchEmployee = setUpBranchEmployee(user);
        Message expected = new Message(branchEmployee, MessageFromServer.LOGIN_SUCCESS);

        assertEquals(actual.getAnswer(), expected.getAnswer());
        assertEquals(actual.getData(), expected.getData());
    }

    // description: test login CEO successful
    // input: username = "ceo", password = "ceo"
    // expected result: User object fill with the correct data and LOGIN_SUCCESS message
    @Test
    public void CEOLoginSucceed() {
        username = "ceo";
        password = "ceo";

        //Method under test
        Message actual = userController.login(username, password);

        //expected
        User user = setUser(username, password);
        User CEO = setUpRegularUser(user, UserType.CEO);
        Message expected = new Message(CEO, MessageFromServer.LOGIN_SUCCESS);

        assertEquals(actual.getAnswer(), expected.getAnswer());
        assertEquals(actual.getData(), expected.getData());
    }

    // description: test login service employee successful
    // input: username = "service", password = "service"
    // expected result: User object fill with the correct data and LOGIN_SUCCESS message
    @Test
    public void serviceLoginSucceed() {
        username = "service";
        password = "service";

        //Method under test
        Message actual = userController.login(username, password);

        //expected
        User user = setUser(username, password);
        User service = setUpRegularUser(user, UserType.SERVICE_EMPLOYEE);
        Message expected = new Message(service, MessageFromServer.LOGIN_SUCCESS);

        assertEquals(actual.getAnswer(), expected.getAnswer());
        assertEquals(actual.getData(), expected.getData());
    }

    // description: test login service employee successful
    // input: username = "expert", password = "expert"
    // expected result: User object fill with the correct data and LOGIN_SUCCESS message
    @Test
    public void expertLoginSucceed() {
        username = "expert";
        password = "expert";

        //Method under test
        Message actual = userController.login(username, password);

        //expected
        User user = setUser(username, password);
        User expert = setUpRegularUser(user, UserType.EXPERT_SERVICE_EMPLOYEE);
        Message expected = new Message(expert, MessageFromServer.LOGIN_SUCCESS);

        assertEquals(actual.getAnswer(), expected.getAnswer());
        assertEquals(actual.getData(), expected.getData());
    }

    // description: test login branch manager successful
    // input: username = "branch_manager", password = "branch_manager"
    // expected result: User object fill with the correct data and LOGIN_SUCCESS message
    @Test
    public void branchManagerLoginSucceed() {
        username = "branch_manager";
        password = "branch_manager";

        //Method under test
        Message actual = userController.login(username, password);

        //expected
        User user = setUser(username, password);
        User branch_manager = setUpRegularUser(user, UserType.BRANCH_MANAGER);
        Message expected = new Message(branch_manager, MessageFromServer.LOGIN_SUCCESS);

        assertEquals(actual.getAnswer(), expected.getAnswer());
        assertEquals(actual.getData(), expected.getData());
    }

    // description: test login delivery operator successful
    // input: username = "delivery", password = "delivery"
    // expected result: User object fill with the correct data and LOGIN_SUCCESS message
    @Test
    public void deliveryLoginSucceed() {
        username = "delivery";
        password = "delivery";

        //Method under test
        Message actual = userController.login(username, password);

        //expected
        User user = setUser(username, password);
        User delivery = setUpRegularUser(user, UserType.DELIVERY_OPERATOR);
        Message expected = new Message(delivery, MessageFromServer.LOGIN_SUCCESS);

        assertEquals(actual.getAnswer(), expected.getAnswer());
        assertEquals(actual.getData(), expected.getData());
    }

    // description: test login empty password
    // input: username = "delivery", password = ""
    // expected result: LOGIN_FAIL message
    @Test
    public void emptyPasswordProvide() {
        username = "delivery";
        password = "";

        //Method under test
        Message actual = userController.login(username, password);

        //expected
        Message expected = new Message(null, MessageFromServer.LOGIN_FAIL);

        assertEquals(actual.getAnswer(), expected.getAnswer());

    }

    // description: test login empty username
    // input: username = "", password = "ceo"
    // expected result: LOGIN_FAIL message
    @Test
    public void emptyUsernameProvide() {
        username = "";
        password = "ceo";

        //Method under test
        Message actual = userController.login(username, password);

        //expected
        Message expected = new Message(null, MessageFromServer.LOGIN_FAIL);

        assertEquals(actual.getAnswer(), expected.getAnswer());

    }

    // description: test login empty password and username
    // input: username = "", password = ""
    // expected result: LOGIN_FAIL message
    @Test
    public void emptyUsernameAndPasswordProvide() {
        username = "";
        password = "";

        //Method under test
        Message actual = userController.login(username, username);

        //expected
        Message expected = new Message(null, MessageFromServer.LOGIN_FAIL);

        assertEquals(actual.getAnswer(), expected.getAnswer());

    }

    // description: test login null username and password
    // input: username = null, password = null
    // expected result: NullPointerException
    @Test
    public void nullUsernameAndPasswordProvide() {
        username = null;
        password = null;

        //Method under test
        try {
            Message actual = userController.login(username, password);
        } catch (NullPointerException e) {
            assertTrue(true);
            return;
        }
        //expected
        Message expected = new Message(null, MessageFromServer.LOGIN_FAIL);
        fail();
    }

    // description: test login empty password
    // input: username = "alreadyLoggedIn", password = "alreadyLoggedIn"
    // expected result: ALREADY_LOGGED_IN message
    @Test
    public void userAlreadyLoggedIn() {

        username = "alreadyLoggedIn";
        password = "alreadyLoggedIn";

        //Method under test
        Message actual = userController.login(username, password);
        //expected
        Message expected = new Message(null, MessageFromServer.ALREADY_LOGGED_IN);

        assertEquals(actual.getAnswer(), expected.getAnswer());
    }

    // description: test login customer blocked
    // input: username = "blocked", password = "blocked"
    // expected result: CUSTOMER_IS_BLOCKED message
    @Test
    public void customerBlockedLoggedIn() {

        username = "blocked";
        password = "blocked";

        //Method under test
        Message actual = userController.login(username, password);
        //expected
        Message expected = new Message(null, MessageFromServer.CUSTOMER_IS_BLOCKED);

        assertEquals(actual.getAnswer(), expected.getAnswer());
    }

    private User setUser(String username, String password) {
        return new User(-1, username, password, null, false, null, null,
                null, null, null);
    }

    private BranchEmployee setUpBranchEmployee(User user) {
        BranchEmployee branchEmployee = new BranchEmployee(user);
        branchEmployee.setBranch(new Branch("Karmiel"));
        branchEmployee.setCatalogue(true);
        branchEmployee.setDiscount(true);
        branchEmployee.setSurvey(true);
        return branchEmployee;
    }

    private Customer setUpCustomer(User user) {
        Customer customer = new Customer(user);
        customer.setBlocked(false);
        customer.setBalance(100.0f);
        customer.setCvv("123");
        customer.setExpDate("10/2023");
        customer.setCreditCard("1234123412341234");
        return customer;
    }

    private User setUpRegularUser(User user, UserType userType) {
        user.setUserId(0);
        user.setLoggedIn(false);
        user.setUserType(userType);
        switch (user.getUserType()) {
            case CEO:
                user.setFirstName("ceo");
                user.setLastName("ceo");
                break;
            case DELIVERY_OPERATOR:
                user.setFirstName("delivery");
                user.setLastName("delivery");
                break;
            case EXPERT_SERVICE_EMPLOYEE:
                user.setFirstName("expert");
                user.setLastName("expert");
                break;
            case BRANCH_MANAGER:
                user.setFirstName("branch_manager");
                user.setLastName("branch_manager");
                break;
            case SERVICE_EMPLOYEE:
                user.setFirstName("service");
                user.setLastName("service");
                break;
        }
        user.setId("0");
        user.setEmail("test@test.com");
        user.setPhone("0000000000");


        return user;
    }
}
