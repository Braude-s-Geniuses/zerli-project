package server;

import communication.Message;
import communication.MessageFromServer;
import order.Item;
import order.Product;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;


public class CatalogController {
    public static Connection con = Server.databaseController.getConnection();

//    public static Message getProductsFromDataBase() {
//
//        ArrayList<Product> products = new ArrayList<>();
//
//        try {
//
//            PreparedStatement preparedProductStatement = connection.prepareStatement("SELECT * FROM product");
//            ResultSet productResult = preparedProductStatement.executeQuery();
//
//            PreparedStatement preparedProductWithItemsStatement = connection.prepareStatement("SELECT i.item_id,i.name,i.type,i.color,i.price,pi.quantity,pi.product_id \n" +
//                    "FROM item i\n" +
//                    "JOIN product_item pi ON i.item_id = pi.item_id");
//            ResultSet productWithItemsResult = preparedProductWithItemsStatement.executeQuery();
//
//            int i = 0;
//            while (productResult.next()) {
//                String currentPath = System.getProperty("user.dir");
//                Product product = new Product();
//                File file = new File(currentPath + "\\ZerliCommon\\assets\\" + i + ".png");
//                FileOutputStream fos = new FileOutputStream(file);
//                byte[] b;
//                Blob blob = productResult.getBlob(5);
//                b = blob.getBytes(1, (int) blob.length());
//                fos.write(b);
//                String nameOfImage = i + ".png";
//                product.setProductId(productResult.getInt(1));
//                product.setName(productResult.getString(2));
//                product.setPrice(productResult.getFloat(3));
//                product.setDiscountPrice(productResult.getFloat(4));
//                product.setCustomMade(productResult.getBoolean(6));
//                product.setDominantColor(productResult.getString(7));
//                product.setImage(nameOfImage);
//                i++;
//                products.add(product);
//
//            }
//
//            while (productWithItemsResult.next()) {
//                for (Product p : products) {
//                    if (p.getProductId() == productWithItemsResult.getInt(7)) {
//                        int id = productWithItemsResult.getInt(1);
//                        String name = productWithItemsResult.getString(2);
//                        String type = productWithItemsResult.getString(3);
//                        String color = productWithItemsResult.getString(4);
//                        int price = productWithItemsResult.getInt(6);
//                        p.setItems(new Item(id, name, type, color, price), price);
//
//                    }
//                }
//
//            }
//
//        } catch (SQLException | IOException e) {
//            System.out.println(e.getMessage());
//        }
//
//        return new Message(products, MessageFromServer.IMPORTED_PRODUCTS_SUCCEED);
//    }

    public static Message getProductItems(Message messageFromClient) {
        int productId = (int) messageFromClient.getData();

        ArrayList<Item> productItems = new ArrayList<>();
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = con.prepareStatement("SELECT i.* FROM item i INNER JOIN product_item pi ON i.item_id = pi.item_id WHERE pi.product_id = ?");
            preparedStatement.setInt(1, productId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                productItems.add(new Item(
                        resultSet.getInt("item_id"),
                        resultSet.getString("name"),
                        resultSet.getString("type"),
                        resultSet.getString("color"),
                        resultSet.getFloat("price"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(null, MessageFromServer.CATALOG_GET_PRODUCT_ITEMS_FAIL);
        }

        return new Message(productItems, MessageFromServer.CATALOG_GET_PRODUCT_ITEMS_SUCCEED);
    }

}