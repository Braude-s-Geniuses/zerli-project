package server;

import communication.Message;
import communication.MessageFromServer;
import product.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemController {
    public static Connection con = Server.databaseController.getConnection();

    /**
     * Adds an Item to the database
     * @param messageFromClient - Item to be added
     * @return <code>ITEM_ADD_SUCCESS</code> on success; otherwise <code>ITEM_ADD_FAIL</code>
     */
    public static Message addItem(Message messageFromClient) {
        Item itemToAdd = (Item) messageFromClient.getData();

        if(isItemNameExists(itemToAdd.getName()))
            return new Message(null, MessageFromServer.ITEM_ADD_FAIL);

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement("INSERT INTO item (name, type, color, price) VALUES (?, ?, ?, ?)");
            preparedStatement.setString(1, itemToAdd.getName());
            preparedStatement.setString(2, itemToAdd.getType());
            preparedStatement.setString(3, itemToAdd.getColor());
            preparedStatement.setFloat(4, itemToAdd.getPrice());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(null, MessageFromServer.ITEM_ADD_FAIL);
        }

        return new Message(null, MessageFromServer.ITEM_ADD_SUCCESS);
    }

    /**
     * Updates an Item in the database
     * @param messageFromClient - Item to be updated
     * @return <code>ITEM_UPDATE_SUCCESS</code> on success; otherwise <code>ITEM_UPDATE_FAIL</code>
     */
    public static Message updateItem(Message messageFromClient) {
        Item itemToUpdate = (Item) messageFromClient.getData();

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement("SELECT * FROM item WHERE name =? AND item_id !=?");
            preparedStatement.setString(1, itemToUpdate.getName());
            preparedStatement.setInt(2, itemToUpdate.getItemId());
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            if(resultSet.getRow() == -1)
                return new Message(null, MessageFromServer.ITEM_UPDATE_FAIL);

            preparedStatement = con.prepareStatement("UPDATE item SET name=?, type=?, color=?, price=? WHERE item_id=?");
            preparedStatement.setString(1, itemToUpdate.getName());
            preparedStatement.setString(2, itemToUpdate.getType());
            preparedStatement.setString(3, itemToUpdate.getColor());
            preparedStatement.setFloat(4, itemToUpdate.getPrice());
            preparedStatement.setInt(5, itemToUpdate.getItemId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(null, MessageFromServer.ITEM_UPDATE_FAIL);
        }

        return new Message(null, MessageFromServer.ITEM_UPDATE_SUCCESS);
    }

    /**
     * Deletes an Item from the database
     * @param messageFromClient - Item to be deleted
     * @return <code>ITEM_DELETE_SUCCESS</code> on success; otherwise <code>ITEM_DELETE_FAIL</code>
     */
    public static Message deleteItem(Message messageFromClient) {
        Item itemToDelete = (Item) messageFromClient.getData();

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement("DELETE FROM item WHERE item_id = ?");
            preparedStatement.setInt(1, itemToDelete.getItemId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(null, MessageFromServer.ITEM_DELETE_FAIL);
        }

        return new Message(null, MessageFromServer.ITEM_DELETE_SUCCESS);
    }

    /**
     * Checks if given item name is already taken
     * @param name - Item name to check
     * @return - true if not taken; false otherwise
     */
    private static boolean isItemNameExists(String name) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement("SELECT * FROM item WHERE name = ?");
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            if(resultSet.getRow() == 0)
                return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * Gets all Items from the database
     * @return <code>ITEMS_GET_SUCCESS</code> on success; otherwise <code>ITEMS_GET_FAIL</code>
     */
    public static Message getAllItems() {
        List<Item> items = new ArrayList<Item>();
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = con.prepareStatement("SELECT * FROM item");
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                items.add(new Item(
                        resultSet.getInt("item_id"),
                        resultSet.getString("name"),
                        resultSet.getString("type"),
                        resultSet.getString("color"),
                        resultSet.getFloat("price"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(null, MessageFromServer.ITEMS_GET_FAIL);
        }

        return new Message(items, MessageFromServer.ITEMS_GET_SUCCESS);
    }
}
