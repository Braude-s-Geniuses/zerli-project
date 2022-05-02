package server;

import communication.Message;
import communication.MessageFromServer;
import javafx.scene.image.Image;
import order.Product;

import java.sql.*;
import java.util.ArrayList;

public  class CatalogController {



    public static Connection response = Server.databaseController.getConnection();

    public static Message getProductsFromDataBase() {

        ArrayList<Product> products = new ArrayList<>();
        try {

            PreparedStatement preparedStatement = response.prepareStatement("SELECT * FROM product");

            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {

                Product product = new Product();
                product.setProductId(result.getInt(1));
                product.setName(result.getString(2));
                product.setPrice(result.getFloat(3));
                product.setDiscountPrice(result.getFloat(4));
                product.setImage((Image) result.getBlob(5));

                products.add(product);
            }
        }catch (SQLException e) {

        }


        return new Message(products, MessageFromServer.IMPORTED_PRODUCTS_SUCCEED);
    }

}
