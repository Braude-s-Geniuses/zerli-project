package unittest;

import communication.Message;
import communication.MessageFromServer;
import org.junit.Before;
import org.junit.Test;
import server.DatabaseController;
import server.UserController;
import user.Customer;
import user.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static server.Server.databaseController;
import static user.UserType.*;

public class LoginServerTest {

    //Stub class for DatabaseController to mock DB connection
    public class DatabaseControllerStub extends DatabaseController{
        public DatabaseControllerStub()
        {
            databaseController.getInstance();
        }
        @Override
        public String connect() {
            String result = "";

            try {
                Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
                result += "\nDriver definition succeed";
            } catch (Exception ex) {
                /* handle the error*/
                result += "\nDriver definition failed";
            }

            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost/" + getDbName() + "?serverTimezone=Asia/Jerusalem", getDbUser(), getDbPassword());
                result += "\nSQL connection succeed";

            } catch (SQLException ex) {/* handle any erroresultSet*/
                result += "\nSQLException: " + ex.getMessage();
                result += "\nSQLState: " + ex.getSQLState();
                result += "\nVendorError: " + ex.getErrorCode();
            }
            return result;
        }
    }

    static UserController userController;

    @Before
    public void setUp(){
        DatabaseControllerStub databaseControllerStub = new DatabaseControllerStub();
        databaseControllerStub.setDbName("zerli");
        databaseControllerStub.setDbUser("root");
        databaseControllerStub.setDbPassword("root");
        databaseControllerStub.connect();
        Connection con = databaseControllerStub.getConnection();
        userController = new UserController(con);
    }


    // description: test login customer successful
    // input: User fill with username = "omer5", password = "1234"
    // expected result: Customer object fill with the correct data and LOGIN_SUCCESS message
    @Test
    public void customerLoginSucceed() {
        //omer5 is a customer in DB
        User newUser = new User(0,"omer5","1234",null,false,null,null,null,null,null);
        //actual
        Message actual = userController.login(newUser);

        //expected
        User expectedUser = new User(1,"omer5","1234",CUSTOMER,false,"Omer","Rogel","4456","4rog@a.com","0526823811");
        Customer expectedCustomer = new Customer(expectedUser);
        expectedCustomer.setCreditCard("1234567812345678");
        expectedCustomer.setExpDate("02/2024");
        expectedCustomer.setCvv("547");
        //details from DB
        Message expected = new Message(expectedUser, MessageFromServer.LOGIN_SUCCESS);

        assertEquals(actual.getAnswer(),expected.getAnswer());
        assertEquals(actual.getData(), expectedCustomer);
        userController.logout(1);
    }

    // description: test login branch employee successful
    // input: User fill with username = "branchi", password = "branchi"
    // expected result: User object fill with the correct data and LOGIN_SUCCESS message
    @Test
    public void branchEmployeeLoginSucceed() {
        //branchi is a branch employee in DB
        User newUser = new User(0,"branchi","branchi",null,false,null,null,null,null,null);
        //actual
        Message actual = userController.login(newUser);

        //expected
        User expectedUser = new User(144,"branchi","branchi",BRANCH_EMPLOYEE,false,"BranchE","E","678976656","be@gmail.com","0573456672");
        //details from DB
        Message expected = new Message(expectedUser, MessageFromServer.LOGIN_SUCCESS);

        assertEquals(actual.getAnswer(),expected.getAnswer());
        assertEquals(actual.getData(), expected.getData());
        userController.logout(144);
    }

    // description: test login CEO successful
    // input: User fill with username = "marom", password = "marom"
    // expected result: User object fill with the correct data and LOGIN_SUCCESS message
    @Test
    public void CEOLoginSucceed() {
        //marom is a CEO in DB
        User newUser = new User(0,"marom","marom",null,false,null,null,null,null,null);
        //actual
        Message actual = userController.login(newUser);

        //expected
        User expectedUser = new User(151,"marom","marom",CEO,false,"Marom","Blumenfeld","746352728","ceo@gmail.com","0504382824");
        //details from DB
        Message expected = new Message(expectedUser, MessageFromServer.LOGIN_SUCCESS);

        assertEquals(expected.getAnswer(), actual.getAnswer());
        assertEquals(expected.getData(), actual.getData());
        userController.logout(151);
    }

    // description: test login service employee successful
    // input: User fill with username = "service", password = "service"
    // expected result: User object fill with the correct data and LOGIN_SUCCESS message
    @Test
    public void ServiceLoginSucceed() {
        //service is a service employee in DB
        User newUser = new User(0,"service","service",null,false,null,null,null,null,null);
        //actual
        Message actual = userController.login(newUser);

        //expected
        User expectedUser = new User(4,"service","service",SERVICE_EMPLOYEE,false,"Service","Service","468476576","Service@zerli.com","0501234567");
        //details from DB
        Message expected = new Message(expectedUser, MessageFromServer.LOGIN_SUCCESS);

        assertEquals(actual.getAnswer(),expected.getAnswer());
        assertEquals(actual.getData(), expected.getData());
        userController.logout(4);
    }

    // description: test login expert service employee successful
    // input: User fill with username = "esp", password = "esp"
    // expected result: User object fill with the correct data and LOGIN_SUCCESS message
    @Test
    public void ExpertLoginSucceed() {
        //esp is an expert service employee in DB
        User newUser = new User(0,"esp","esp",null,false,null,null,null,null,null);
        //actual
        Message actual = userController.login(newUser);

        //expected
        User expectedUser = new User(146,"esp","esp",EXPERT_SERVICE_EMPLOYEE,false,"expert","Emploee","767365578","esp@gmail.com","0542246363");
        //details from DB
        Message expected = new Message(expectedUser, MessageFromServer.LOGIN_SUCCESS);

        assertEquals(actual.getAnswer(),expected.getAnswer());
        assertEquals(actual.getData(), expected.getData());
        userController.logout(146);
    }

    // description: test login branch manager successful
    // input: User fill with username = "gal", password = "gal"
    // expected result: User object fill with the correct data and LOGIN_SUCCESS message
    @Test
    public void BranchManagerLoginSucceed() {
        //gal is a branch manager in DB
        User newUser = new User(0,"gal","gal",null,false,null,null,null,null,null);
        //actual
        Message actual = userController.login(newUser);

        //expected
        User expectedUser = new User(147,"gal","gal",BRANCH_MANAGER,false,"Gal","Amar","123454321","branchi@gmail.com","0505483726");
        //details from DB
        Message expected = new Message(expectedUser, MessageFromServer.LOGIN_SUCCESS);

        assertEquals(expected.getAnswer(), actual.getAnswer());
        assertEquals(expected.getData(), actual.getData());
        userController.logout(147);
    }

    // description: test login delivery operator successful
    // input: User fill with username = "do", password = "do"
    // expected result: User object fill with the correct data and LOGIN_SUCCESS message
    @Test
    public void DeliveryLoginSucceed() {
        //do is a delivery operator in DB
        User newUser = new User(0,"do","do",null,false,null,null,null,null,null);
        //actual
        Message actual = userController.login(newUser);

        //expected
        User expectedUser = new User(145,"do","do",DELIVERY_OPERATOR,false,"Dellivery","Operator","876543678","do@gmaill.com","0563542436");
        //details from DB
        Message expected = new Message(expectedUser, MessageFromServer.LOGIN_SUCCESS);

        assertEquals(expected.getAnswer(), actual.getAnswer());
        assertEquals(expected.getData(), actual.getData());
        userController.logout(145);
    }

    // description: test login with empty password
    // input: User fill with username = "do" , password = ""
    // expected result: LOGIN_FAIL message
    @Test
    public void EmptyPasswordProvide() {

        User newUser = new User(0,"do","",null,false,null,null,null,null,null);
        //actual
        Message actual = userController.login(newUser);

        //details from DB
        Message expected = new Message(null, MessageFromServer.LOGIN_FAIL);

        assertEquals(actual.getAnswer(),expected.getAnswer());
    }

    // description: test login with empty username
    // input: User fill with username = "" , password = "do"
    // expected result: LOGIN_FAIL message
    @Test
    public void EmptyUsernameProvide() {
        User newUser = new User(0,"","do",null,false,null,null,null,null,null);
        //actual
        Message actual = userController.login(newUser);

        //details from DB
        Message expected = new Message(null, MessageFromServer.LOGIN_FAIL);

        assertEquals(actual.getAnswer(),expected.getAnswer());
    }

    // description: test login with empty username and password
    // input: User fill with username = "" , password = ""
    // expected result: LOGIN_FAIL message
    @Test
    public void EmptyUsernameAndPasswordProvide() {
        User newUser = new User(0,"","",null,false,null,null,null,null,null);
        //actual
        Message actual = userController.login(newUser);

        //details from DB
        Message expected = new Message(null, MessageFromServer.LOGIN_FAIL);

        assertEquals(actual.getAnswer(),expected.getAnswer());
    }

    // description: test login with Already Logged-In User
    // input: User fill with username = "already" , password = "already"
    // expected result: LOGIN_FAIL message
    @Test
    public void AlreadyLoggedInUserProvide() {
        User newUser = new User(0,"already","already",null,false,null,null,null,null,null);
        //actual
        Message actual = userController.login(newUser);

        //details from DB
        Message expected = new Message(null, MessageFromServer.ALREADY_LOGGED_IN);

        assertEquals(expected.getAnswer(), actual.getAnswer());
    }

    // description: test login with blocked customer
    // input: User fill with username = "blocked" , password = "blocked"
    // expected result: LOGIN_FAIL message
    @Test
    public void BlockedCustomerProvide() {
        User newUser = new User(0,"blocked","blocked",null,false,null,null,null,null,null);
        //actual
        Message actual = userController.login(newUser);

        //details from DB
        Message expected = new Message(null, MessageFromServer.CUSTOMER_IS_BLOCKED);

        assertEquals(expected.getAnswer(), actual.getAnswer());
        userController.logout(155);
    }

    // description: test login with null user
    // input: User = null
    // expected result: NullPointerException
    @Test
    public void NullUserProvide() {
        Message actual;
        try {
            actual = userController.login(null);
        } catch (NullPointerException e) {
            assertTrue(true);
            return;
        }
        fail();
    }


}