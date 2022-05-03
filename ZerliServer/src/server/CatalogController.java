package server;

import communication.Message;
import communication.MessageFromServer;
import javafx.scene.image.Image;
import order.Product;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;

public  class CatalogController {



    public static Connection response = Server.databaseController.getConnection();

    public static Message getProductsFromDataBase() {

        ArrayList<Product> products = new ArrayList<>();
        try {

            PreparedStatement preparedStatement = response.prepareStatement("SELECT * FROM product");

            ResultSet result = preparedStatement.executeQuery();

            int i=0;
            while (result.next()) {
                String currentpath = System.getProperty("user.dir");
                Product product = new Product();
                File file=new File(currentpath + "\\ZerliCommon\\assets\\" + i + ".png");
                FileOutputStream fos=new FileOutputStream(file);
                byte b[];
                Blob blob = result.getBlob(5);
                b=blob.getBytes(1,(int)blob.length());
                fos.write(b);
                String nameOfImage = i + ".png";
                product.setProductId(result.getInt(1));
                product.setName(result.getString(2));
                product.setPrice(result.getFloat(3));
                product.setDiscountPrice(result.getFloat(4));
                product.setImage(nameOfImage);
                i++;
                products.add(product);
            }
        }catch (SQLException e) {

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return new Message(products, MessageFromServer.IMPORTED_PRODUCTS_SUCCEED);
    }

}
