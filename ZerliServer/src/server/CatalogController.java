package server;

import communication.Message;
import communication.MessageFromServer;
import product.Item;
import product.Product;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CatalogController {
    public static Connection con = Server.databaseController.getConnection();

    /**
     * Gets all Products from the database for Catalog usage
     * @return <code>CATALOG_PRODUCTS_GET_SUCCESS</code> on success; otherwise <code>CATALOG_PRODUCTS_GET_FAIL</code>
     */
    public static Message getAllProducts() {
        List<Product> products = new ArrayList<Product>();
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = con.prepareStatement("SELECT *, (price - discount_price) as m FROM product WHERE in_catalog = 1 AND customer_product = 0 ORDER BY m DESC");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                SerialBlob serialBlob = new SerialBlob(resultSet.getBlob("image"));

                products.add(new Product(
                        resultSet.getInt("product_id"),
                        resultSet.getString("name"),
                        resultSet.getFloat("price"),
                        resultSet.getFloat("discount_price"),
                        serialBlob,
                        resultSet.getBoolean("custom_made"),
                        resultSet.getString("dominant_color"),
                        resultSet.getBoolean("in_catalog")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(null, MessageFromServer.CATALOG_PRODUCTS_GET_FAIL);
        }

        return new Message(products, MessageFromServer.CATALOG_PRODUCTS_GET_SUCCESS);
    }

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

        return new Message(items, MessageFromServer.CATALOG_GET_PRODUCT_ITEMS_SUCCESS);
    }

}