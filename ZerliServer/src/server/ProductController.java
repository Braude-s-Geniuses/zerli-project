package server;

import communication.Message;
import communication.MessageFromServer;
import product.Item;
import product.Product;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductController {
    public static Connection con = Server.databaseController.getConnection();

    /**
     * Adds a Product to the database
     * @param messageFromClient - Product to be added
     * @return <code>PRODUCT_ADD_SUCCESS</code> on success; otherwise <code>PRODUCT_ADD_FAIL</code>
     */
    public static Message addProduct(Message messageFromClient) {
        Product productToAdd = (Product) messageFromClient.getData();

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement("INSERT INTO product (name, price, discount_price, image, custom_made, dominant_color) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, productToAdd.getName());
            preparedStatement.setFloat(2, productToAdd.getPrice());
            preparedStatement.setFloat(3, productToAdd.getDiscountPrice());
            preparedStatement.setBlob(4, productToAdd.getImage());
            preparedStatement.setBoolean(5, productToAdd.isCustomMade());
            preparedStatement.setString(6, productToAdd.getDominantColor());
            preparedStatement.executeUpdate();

            try(ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if(generatedKeys.next()) {
                    productToAdd.setProductId(generatedKeys.getInt(1));
                }
            }

            for(Item item : productToAdd.getItems().keySet()) {
                preparedStatement = con.prepareStatement("INSERT INTO product_item (product_id, item_id, quantity) VALUES (?, ?, ?)");
                preparedStatement.setInt(1, productToAdd.getProductId());
                preparedStatement.setInt(2, item.getItemId());
                preparedStatement.setInt(3, productToAdd.getItems().get(item));

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(null, MessageFromServer.PRODUCT_ADD_FAIL);
        }

        return new Message(null, MessageFromServer.PRODUCT_ADD_SUCCESS);
    }

    /**
     * Updates an Product in the database
     * @param messageFromClient - Product to be updated
     * @return <code>PRODUCT_UPDATE_SUCCESS</code> on success; otherwise <code>PRODUCT_UPDATE_FAIL</code>
     */
    public static Message updateProduct(Message messageFromClient) {
        Product productToUpdate = (Product) messageFromClient.getData();
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = con.prepareStatement("UPDATE product SET name=?, price=?, discount_price=?, image=?, custom_made=?, in_catalog=? WHERE product_id=?");
            preparedStatement.setString(1, productToUpdate.getName());
            preparedStatement.setFloat(2, productToUpdate.getPrice());
            preparedStatement.setFloat(3, productToUpdate.getDiscountPrice());
            preparedStatement.setBlob(4, productToUpdate.getImage());
            preparedStatement.setBoolean(5, productToUpdate.isCustomMade());
            preparedStatement.setBoolean(6, productToUpdate.isInCatalog());
            preparedStatement.setInt(7, productToUpdate.getProductId());
            preparedStatement.executeUpdate();

            preparedStatement = con.prepareStatement("DELETE FROM product_item WHERE product_id=?");
            preparedStatement.setInt(1, productToUpdate.getProductId());
            preparedStatement.executeUpdate();

            for(Item item : productToUpdate.getItems().keySet()) {
                preparedStatement = con.prepareStatement("INSERT INTO product_item (product_id, item_id, quantity) VALUES (?, ?, ?)");
                preparedStatement.setInt(1, productToUpdate.getProductId());
                preparedStatement.setInt(2, item.getItemId());
                preparedStatement.setInt(3, productToUpdate.getItems().get(item));

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(null, MessageFromServer.PRODUCT_UPDATE_FAIL);
        }

        return new Message(null, MessageFromServer.PRODUCT_UPDATE_SUCCESS);
    }

    /**
     * Gets all Products from the database
     * @return <code>PRODUCTS_GET_SUCCESS</code> on success; otherwise <code>PRODUCTS_GET_FAIL</code>
     */
    public static Message getAllProducts() {
        List<Product> products = new ArrayList<Product>();
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = con.prepareStatement("SELECT * FROM product WHERE customer_product = 0");
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
            return new Message(null, MessageFromServer.PRODUCTS_GET_FAIL);
        }

        return new Message(products, MessageFromServer.PRODUCTS_GET_SUCCESS);
    }

    /**
     * Get the items of specific product
     * @param messageFromClient contains wanted product id
     * @return <code>PRODUCT_GET_ITEMS_SUCCEED</code> on success; otherwise <code>PRODUCT_GET_ITEMS_FAIL</code>
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
            return new Message(null, MessageFromServer.PRODUCT_GET_ITEMS_FAIL);
        }

        return new Message(items, MessageFromServer.PRODUCT_GET_ITEMS_SUCCEED);
    }
}
