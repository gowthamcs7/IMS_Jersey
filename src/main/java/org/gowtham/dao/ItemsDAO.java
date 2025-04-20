package org.gowtham.dao;

import org.gowtham.model.Item;
import org.gowtham.helper.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemsDAO {

    // Get all items with stock quantity
    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT i.id, i.name, i.categoryId, c.name AS categoryName, c.description AS categoryDescription, " +
                "i.averageCostPrice, i.defaultSellingPrice, " +
                "COALESCE(i.createdAt, NOW()) AS createdAt, " +
                "COALESCE(i.updatedAt, NOW()) AS updatedAt, " +
                "COALESCE(inv.stockQuantity, 0) AS stockQuantity " + // Fixed: Added space before FROM
                "FROM items i " +
                "JOIN categories c ON i.categoryId = c.id " +
                "LEFT JOIN inventory inv ON i.id = inv.itemId";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Item item = new Item(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("categoryId"),
                        rs.getString("categoryName"),
                        rs.getString("categoryDescription"),
                        rs.getDouble("averageCostPrice"),
                        rs.getDouble("defaultSellingPrice"),
                        rs.getTimestamp("createdAt"),
                        rs.getTimestamp("updatedAt"),
                        rs.getInt("stockQuantity")
                );
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }




    // Get item by ID
    public Item getItemById(int id) {
        String query = "SELECT i.id, i.name, i.categoryId, " +
                "c.name AS category_name, " +  // Fix alias
                "c.description AS category_description, " +
                "i.averageCostPrice, i.defaultSellingPrice, i.createdAt, i.updatedAt " +
                "FROM items i " +
                "JOIN categories c ON i.categoryId = c.id " +
                "WHERE i.id = ?";  // Add WHERE condition

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            int qty=0;
            pstmt.setInt(1, id);  // Set parameter correctly

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Item(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("categoryId"),
                            rs.getString("category_name"),  // Use correct alias
                            rs.getString("category_description"),
                            rs.getDouble("averageCostPrice"),
                            rs.getDouble("defaultSellingPrice"),
                            rs.getTimestamp("createdAt"),
                            rs.getTimestamp("updatedAt"),
                            qty
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addItem(Item item) {
        String sql = "INSERT INTO items (name, categoryId, averageCostPrice, defaultSellingPrice) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, item.getName());
            stmt.setInt(2, item.getCategoryId());
            stmt.setDouble(3, item.getAverageCostPrice());
            stmt.setDouble(4, item.getDefaultSellingPrice());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateItem(Item item) {
        String sql = "UPDATE items SET name = ?, categoryId = ?, averageCostPrice = ?, defaultSellingPrice = ?, updatedAt = NOW() WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, item.getName());
            stmt.setInt(2, item.getCategoryId());
            stmt.setDouble(3, item.getAverageCostPrice());
            stmt.setDouble(4, item.getDefaultSellingPrice());
            stmt.setInt(5, item.getId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteItem(int id) {
        String sql = "DELETE FROM items WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
