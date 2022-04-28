package clientgui;

import client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/** represents the GUI Controller of the client update order form.
 *
 */
public class UpdateOrderFormController implements Initializable {
    /**
     * order number which the <code>ViewOrdersTable</code> updates once the client selected an order to update and clicked Modify.
     */
    private int orderNumber = 0;

    @FXML
    private Button btnBrowseOrders;

    @FXML
    private Button btnUpdate;

    @FXML
    private DatePicker dpNewDate;

    @FXML
    private ComboBox cbNewColor;

    @FXML
    private ComboBox cbTime;

    @FXML
    private Label lblOrderNumber;

    @FXML
    private Label lblMessage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> colorList = new ArrayList<String>();
        List<String> timeList = new ArrayList<String>();

        /**
         * Adds colors to Color ComboBox.
         */
        colorList.add("Black");
        colorList.add("White");
        colorList.add("Gray");
        colorList.add("Silver");
        colorList.add("Maroon");
        colorList.add("Red");
        colorList.add("Purple");
        colorList.add("Pink");
        colorList.add("Green");
        colorList.add("Lime");
        colorList.add("Olive");
        colorList.add("Yellow");
        colorList.add("Navy");
        colorList.add("Blue");
        colorList.add("Teal");
        colorList.add("Aqua");

        /**
         * Add hours to Time ComboBox.
         */
        for (int i = 8; i <= 18; i++) {
            timeList.add(String.valueOf(i) + ":00");
            if (i != 18)
                timeList.add(String.valueOf(i) + ":30");
        }


        ObservableList observableList = FXCollections.observableList(colorList);
        cbNewColor.getItems().clear();
        cbNewColor.setItems(observableList);

        observableList = FXCollections.observableList(timeList);
        cbTime.getItems().clear();
        cbTime.setItems(observableList);

        LocalDate minDate = LocalDate.now();

        /**
         * Initializes the DatePicker for date selection.
         */
        dpNewDate.setDayCellFactory(d ->
                new DateCell() {
                    @Override public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setDisable(item.isBefore(minDate) || item.isAfter(minDate.plusMonths(2)));
                    }});
    }

    @FXML
    void clickBtnBrowseOrders(ActionEvent event) throws Exception {
        ((Node) event.getSource()).getScene().getWindow().hide();

        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("ViewOrdersTable.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setTitle("Zerli Client");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        Client.clientController.attachExitEventToStage(primaryStage);
    }

    @FXML
    void clickBtnUpdate(ActionEvent event) throws Exception {
        if(dpNewDate.getValue() == null && cbTime.getSelectionModel().isEmpty() && cbNewColor.getSelectionModel().isEmpty()) {
            lblMessage.setStyle("-fx-text-fill: indianred");
            lblMessage.setText("You must select a new color or date to update");
            return;
        }

        if(!cbTime.getSelectionModel().isEmpty() && dpNewDate.getValue() == null || cbTime.getSelectionModel().isEmpty() && dpNewDate.getValue() != null) {
            lblMessage.setStyle("-fx-text-fill: indianred");
            lblMessage.setText("You must select date and time together");
            return;
        }

        /**
         * passed all validations - send update message to server through <code>ClientController</code>.
         */
        boolean result = Client.clientController.updateOrder(orderNumber,
                    dpNewDate.getValue() != null ? dpNewDate.getValue().toString() : null,
                    cbTime.getValue() != null ? cbTime.getValue().toString() : null,
                    cbNewColor.getValue() != null ? cbNewColor.getValue().toString() : null
                );

        if(result) {
            lblMessage.setStyle("-fx-text-fill: green");
            lblMessage.setText("Update saved successfully!");
        }
        else {
            lblMessage.setStyle("-fx-text-fill: indianred");
            lblMessage.setText("Failed to update order");
        }
    }

    /** The method is used by other controllers to initialize orderNumber to update.
     *
     * @param orderNumber Order to update.
     */
    void setLblOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
        lblOrderNumber.setText(lblOrderNumber.getText() + " " + String.valueOf(orderNumber));
    }

}
