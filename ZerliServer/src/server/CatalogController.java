package server;

import communication.Message;
import communication.MessageFromServer;
import order.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


public class CatalogController {
    public static Connection con = Server.databaseController.getConnection();

    /**
     * Get the items of specific product
     * @param messageFromClient contains wanted product id
     * @return
     */
    public static Message getProductItems(Message messageFromClient) {
        int productId = (int) messageFromClient.getData();
        HashMap<Item,Integer> items = new HashMap<>();
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = con.prepareStatement("SELECT i.*, pi.quantity FROM item i INNER JOIN product_item pi ON i.item_id = pi.item_id WHERE pi.product_id = ?");
            preparedStatement.setInt(1, productId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                items.put(new Item(
                                resultSet.getInt("item_id"),
                                resultSet.getString("name"),
                                resultSet.getString("type"),
                                resultSet.getString("color"),
                                resultSet.getFloat("price")),
                                resultSet.getInt("quantity")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(null, MessageFromServer.CATALOG_GET_PRODUCT_ITEMS_FAIL);
        }

        return new Message(items, MessageFromServer.CATALOG_GET_PRODUCT_ITEMS_SUCCEED);
    }

}