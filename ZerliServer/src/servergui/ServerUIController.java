package servergui;

import communication.ClientInfo;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ocsf.server.ConnectionToClient;
import server.ExternalSystemController;
import server.Server;
import user.User;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/** represents the GUI Controller of the server.
 *
 */
public class ServerUIController implements Initializable {

    /** an <code>ObservableList</code> used to store current connected clients so they can be displayed
     * on the table in the gui.
     */
    public static ObservableList<ClientInfo> clients = FXCollections.observableArrayList();
    public static TableView tableClientsBox;
    public static TextArea serverConsoleBox;

    @FXML
    private TextField fldDbName;

    @FXML
    private Button btnStart;

    @FXML
    private Button btnStop;

    @FXML
    private TextArea txtServerConsole;

    @FXML
    private TextField fldDbUser;

    @FXML
    private PasswordField fldDbPassword;

    @FXML
    private TableView<ClientInfo> tableClients;

    @FXML
    private TableColumn<ClientInfo, String> columnIP;

    @FXML
    private TableColumn<ClientInfo, String> columnClient;

    @FXML
    private TableColumn<ClientInfo, String> columnStatus;

    @FXML
    private Label lblServerConsole;
    @FXML
    private Button btnImport;

    @FXML
    void clickBtnImport(ActionEvent event) {
        if(!ExternalSystemController.connect())
            return;
        ExternalSystemController.importUsers();
        btnImport.setDisable(true);
        printToServerConsoleUI("Import data from external system successfully");
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //  tableClientsBox.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        serverConsoleBox = txtServerConsole;
        tableClientsBox = tableClients;
        columnIP.setCellValueFactory(new PropertyValueFactory<ClientInfo, String>("ipAddress"));
        columnClient.setCellValueFactory(new PropertyValueFactory<ClientInfo, String>("hostname"));
        columnStatus.setCellValueFactory(new PropertyValueFactory<ClientInfo, String>("status"));

        tableClients.setItems(clients);

        txtServerConsole.setDisable(true);
    }

    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("ServerUI.fxml"));

        Scene scene = new Scene(root);
        primaryStage.setTitle("Zerli Server");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void clickBtnStart(ActionEvent event) throws Exception {
        /**
         * Calls <code>startServer()</code> to start listening for connections.
         */
        String output = Server.startServer(fldDbName.getText(), fldDbUser.getText(), fldDbPassword.getText());

        txtServerConsole.setDisable(false);

        btnStart.setDisable(true);
        btnStop.setDisable(false);
        fldDbName.setDisable(true);
        fldDbUser.setDisable(true);
        fldDbPassword.setDisable(true);
        tableClients.setDisable(false);
        btnImport.setDisable(false);
    }

    public void clickBtnStop(ActionEvent event) throws Exception {
        /**
         * Calls <code>stopServer()</code> to stop listening for connections.
         */
        Server.stopServer();
        printToServerConsoleUI("Server has stopped listening for connections.");

        btnStart.setDisable(false);
        btnStop.setDisable(true);
        fldDbName.setDisable(false);
        fldDbUser.setDisable(false);
        fldDbPassword.setDisable(false);
        tableClients.getItems().clear();
        tableClients.setDisable(true);

        txtServerConsole.setText("");
        txtServerConsole.setDisable(true);
    }

    /** The method is used to add newly connected clients from outside the gui controller.
     *
     * @param client to be added.
     */
    public void addClientToTable(ConnectionToClient client) {
        clients.add(new ClientInfo(client.getInetAddress().getHostAddress(), client.getInetAddress().getCanonicalHostName()));
    }

    /** The method is used to remove disconnected client from outside the gui controller.
     *
     * @param client to be removed.
     */
    public static void removeClientFromTable(ConnectionToClient client) {
        for(ClientInfo clientInTable : clients)
            if(client.getInetAddress().getHostAddress().equals(clientInTable.getIpAddress()) && client.getInetAddress().getCanonicalHostName().equals(clientInTable.getHostname()))
                clients.remove(clientInTable);
    }

    public static void setClientLoggedInTable(ConnectionToClient client, User user) {
        for(ClientInfo clientInTable : clients)
            if(client.getInetAddress().getHostAddress().equals(clientInTable.getIpAddress()) && client.getInetAddress().getCanonicalHostName().equals(clientInTable.getHostname())) {
                clientInTable.setStatus("Logged In (" + user.getUsername() + ")");
                tableClientsBox.refresh();
                printToServerConsoleUI(clientInTable.getHostname() + " is now logged in as " + user.getUsername());
                break;
            }
    }

    public static void setClientLoggedOutTable(ConnectionToClient client) {
        for(ClientInfo clientInTable : clients)
            if(client.getInetAddress().getHostAddress().equals(clientInTable.getIpAddress()) && client.getInetAddress().getCanonicalHostName().equals(clientInTable.getHostname())) {
                String clientUsername = clientInTable.getStatus().substring(11, clientInTable.getStatus().length() - 1);
                clientInTable.setStatus("Connected (Guest)");
                tableClientsBox.refresh();
                printToServerConsoleUI(clientInTable.getHostname() + " (" + clientUsername + ") has logged out");
                break;
            }
    }

    /** The method is used to update the gui server console log.
     *
     * @param text to be added.
     */
    public static void printToServerConsoleUI(String text) {
        Platform.runLater(() -> {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String timestamp = "[" + dateTimeFormatter.format(LocalTime.now()) + "] ";
            serverConsoleBox.setText(serverConsoleBox.getText() + "\n" + timestamp + text);
        });
    }

}