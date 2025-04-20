package org.gowtham.dao;





import org.gowtham.exception.InsufficientStockException;
import org.gowtham.model.Sale;
import org.gowtham.model.SaleItem;
import org.gowtham.helper.DatabaseConnection;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaleDAO {

    public boolean createSale(Sale sale, List<SaleItem> saleItems) throws SQLException {
        Connection conn = null;
        PreparedStatement saleStmt = null;
        PreparedStatement saleItemStmt = null;
        PreparedStatement inventoryStmt = null;
        ResultSet rs = null;

        String saleQuery = "INSERT INTO sales (customerId, userId, totalAmount) VALUES (?, ?, ?)";
        String saleItemQuery = "INSERT INTO salesItems (saleId, itemId, qty, salePrice, totalPrice) VALUES (?, ?, ?, ?, ?)";
        String inventoryUpdateQuery = "UPDATE inventory SET stockQuantity = stockQuantity - ? WHERE itemId = ? AND stockQuantity >= ?";

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);  // Start transaction

            // Insert into sales table
            saleStmt = conn.prepareStatement(saleQuery, Statement.RETURN_GENERATED_KEYS);
            saleStmt.setInt(1, sale.getCustomerID());
            saleStmt.setInt(2, sale.getUserID());
            saleStmt.setDouble(3, sale.getTotalAmount());
            saleStmt.executeUpdate();

            rs = saleStmt.getGeneratedKeys();
            if (!rs.next()) {
                throw new InsufficientStockException("Not enough stock available for sale.");
            }
            int saleId = rs.getInt(1);

            // Insert into saleItems
            saleItemStmt = conn.prepareStatement(saleItemQuery);
            inventoryStmt = conn.prepareStatement(inventoryUpdateQuery);

            for (SaleItem item : saleItems) {
                saleItemStmt.setInt(1, saleId);
                saleItemStmt.setInt(2, item.getItemId());
                saleItemStmt.setInt(3, item.getQty());
                saleItemStmt.setDouble(4, item.getSalePrice());
                saleItemStmt.setDouble(5, item.getTotalPrice());
                saleItemStmt.addBatch();

                // Update inventory
                inventoryStmt.setInt(1, item.getQty());
                inventoryStmt.setInt(2, item.getItemId());
                inventoryStmt.setInt(3, item.getQty());
                inventoryStmt.addBatch();
            }

            saleItemStmt.executeBatch();
            int[] inventoryUpdates = inventoryStmt.executeBatch();

            // Ensure stock updates were successful
            for (int update : inventoryUpdates) {
                if (update == 0) {
                    conn.rollback();
                    throw new InsufficientStockException("Not enough stock available for sale.");
                }
            }

            conn.commit();
            return true;

        } catch (InsufficientStockException e) {
            throw e;  // Directly propagate stock issue
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();  // Rollback transaction on error
            }
            e.printStackTrace();


            throw new RuntimeException("Database Error Occured...");




        } finally {
            if (rs != null) rs.close();
            if (saleStmt != null) saleStmt.close();
            if (saleItemStmt != null) saleItemStmt.close();
            if (inventoryStmt != null) inventoryStmt.close();
            if (conn != null) conn.setAutoCommit(true);
        }
    }

    public List<Sale> getAllSales() throws SQLException {
        List<Sale> sales = new ArrayList<>();
        Map<Integer, Sale> saleMap = new HashMap<>();

        String query = "SELECT s.id AS saleId, s.customerId, c.name AS customerName, s.userId, " +
                "u.name AS userName, s.totalAmount, s.createdAt AS saleDate, " +
                "si.id AS saleItemId, si.itemId, i.name AS itemName, si.qty, si.salePrice, si.totalPrice " +
                "FROM sales s " +
                "JOIN users u ON s.userId = u.id " +
                "JOIN customer c ON s.customerId = c.id " +
                "LEFT JOIN salesItems si ON s.id = si.saleId " +
                "LEFT JOIN items i ON si.itemId = i.id " +
                "ORDER BY s.id, si.id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            List<SaleItem> saleItems = new ArrayList<SaleItem>();
            while (rs.next()) {
                int saleId = rs.getInt("saleId");

                // If sale is not already in the map, add it
                if (!saleMap.containsKey(saleId)) {
                    Sale sale = new Sale(
                            saleId,
                            rs.getInt("customerId"),
                            rs.getInt("userId"),
                            rs.getDouble("totalAmount"),
                            rs.getTimestamp("saleDate"),
                            saleItems = new ArrayList<>()
                    );
                    saleMap.put(saleId, sale);
                }

                // If sale item exists, add it to the list
                if (rs.getInt("saleItemId") != 0) {
                    SaleItem saleItem = new SaleItem(
                            rs.getInt("saleItemId"),
                            saleId,
                            rs.getInt("itemId"),
                            rs.getInt("qty"),
                            rs.getDouble("salePrice"),
                            rs.getDouble("totalPrice")
                    );
                    saleMap.get(saleId).getSaleItems().add(saleItem);
                }
            }

            sales.addAll(saleMap.values());
        }
        return sales;
    }
}
