package server;

import communication.Message;
import communication.MessageFromServer;

import java.sql.Connection;

public  class CatalogController {
    public static Connection connection = Server.databaseController.getConnection();

//    public static Message getProductsFromDataBase() {
//
//        ArrayList<Product> products = new ArrayList<>();
//        try {
//
//            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM product");
//
//            ResultSet result = preparedStatement.executeQuery();
//
//            int i=0;
//            while (result.next()) {
//                String currentPath = System.getProperty("user.dir");
//                Product product = new Product();
//                File file=new File(currentPath + "\\ZerliCommon\\assets\\" + i + ".png");
//                FileOutputStream fos=new FileOutputStream(file);
//                byte[] b;
//                Blob blob = result.getBlob(5);
//                b=blob.getBytes(1,(int)blob.length());
//                fos.write(b);
//                String nameOfImage = i + ".png";
//                product.setProductId(result.getInt(1));
//                product.setName(result.getString(2));
//                product.setPrice(result.getFloat(3));
//                product.setDiscountPrice(result.getFloat(4));
//                product.setCustomMade(result.getBoolean(6));
//                product.setDominantColor(result.getString(7));
//                product.setImage(nameOfImage);
//                i++;
//                products.add(product);
//                System.out.println(product);
//            }
//        }catch (SQLException e) {
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//
//        return new Message(products, MessageFromServer.IMPORTED_PRODUCTS_SUCCEED);
//    }

    public static Message getOrderFromCatalog(Message msg) {

        System.out.println("I got here and received : " + msg.getData());

        return new Message("I send you back what i received :  " + msg.getData() , MessageFromServer.SEND_ORDER_CATALOG);
    }

}
