package server;

import communication.Message;
import communication.MessageFromServer;
import order.Product;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductController {
    public static Connection con = Server.databaseController.getConnection();

    public static Message addProduct(Message messageFromClient) {
        Product productToAdd = (Product) messageFromClient.getData();

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement("INSERT INTO product (name, price, discount_price, image, custom_made, dominant_color) VALUES (?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, productToAdd.getName());
            preparedStatement.setFloat(2, productToAdd.getPrice());
            preparedStatement.setFloat(3, productToAdd.getDiscountPrice());
            preparedStatement.setBlob(4, productToAdd.getImage());
            preparedStatement.setBoolean(5, productToAdd.isCustomMade());
            preparedStatement.setString(6, productToAdd.getDominantColor());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(null, MessageFromServer.PRODUCT_ADD_FAIL);
        }

        return new Message(null, MessageFromServer.PRODUCT_ADD_SUCCESS);
    }

    public static Message updateProduct(Message messageFromClient) {
        Product productToUpdate = (Product) messageFromClient.getData();

        return new Message(null, MessageFromServer.PRODUCT_UPDATE_SUCCESS);
    }

    public static Message deleteProduct(Message messageFromClient) {
        Product productToDelete = (Product) messageFromClient.getData();

        return new Message(null, MessageFromServer.PRODUCT_DELETE_SUCCESS);
    }

    public static Message getAllProducts() {
        List<Product> products = new ArrayList<Product>();
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = con.prepareStatement("SELECT * FROM product");
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
                        resultSet.getString("dominant_color")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(null, MessageFromServer.PRODUCTS_GET_FAIL);
        }

        return new Message(products, MessageFromServer.PRODUCTS_GET_SUCCESS);
    }
}
