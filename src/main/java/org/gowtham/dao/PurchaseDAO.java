package org.gowtham.dao;

import org.gowtham.model.Purchase;
import org.gowtham.model.PurchaseItem;
import org.gowtham.helper.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PurchaseDAO {
    public List<Purchase> getAllPurchases() {
        List<Purchase> purchases = new ArrayList<>();
        String query = "SELECT p.id AS purchaseId, p.userId, u.name AS userName, " +
                "p.vendorId, v.name AS vendorName, p.totalAmount, p.createdAt, " +
                "pi.itemId, i.name AS itemName, pi.qty, pi.purchasePrice, pi.totalPrice " +
                "FROM purchases p " +
                "JOIN users u ON p.userId = u.id " +
                "JOIN vendors v ON p.vendorId = v.id " +
                "JOIN purchaseItems pi ON p.id = pi.purchaseId " +
                "JOIN items i ON pi.itemId = i.id " +
                "ORDER BY p.createdAt DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            List<PurchaseItem> purchaseItems = new ArrayList<PurchaseItem>();
            Map<Integer, Purchase> purchaseMap = new LinkedHashMap<>();
            while (rs.next()) {
                int purchaseId = rs.getInt("purchaseId");


                // If purchase is not in the map, create a new entry
                if (!purchaseMap.containsKey(purchaseId)) {
                    Purchase purchase = new Purchase(
                            purchaseId,
                            rs.getInt("userId"),
                            rs.getString("userName"),
                            rs.getInt("vendorId"),

                            rs.getString("vendorName"),
                            rs.getDouble("totalAmount"),
                            rs.getTimestamp("createdAt"),
                            purchaseItems = new ArrayList<PurchaseItem>()

                    );
                    purchase.setFormattedCreatedAt();
                    purchaseMap.put(purchaseId, purchase);
                }

                // Add purchase item to the respective purchase
                PurchaseItem item = new PurchaseItem(
                        rs.getInt("itemId"),
                        rs.getString("itemName"),
                        rs.getInt("qty"),
                        rs.getDouble("purchasePrice"),
                        rs.getDouble("totalPrice")
                );
                purchaseMap.get(purchaseId).getPurchaseItemList().add(item);
            }

            purchases.addAll(purchaseMap.values());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (Purchase p : purchases) {
            System.out.println("Purchase ID: " + p.getId() + ", Formatted Date: " + p.getFormattedCreatedAt());
        }

        return purchases;
    }

    public boolean createPurchase(Purchase purchase) {
        String insertPurchaseSQL = "INSERT INTO purchases (userId, vendorId, totalAmount) VALUES (?, ?, ?)";
        String insertPurchaseItemSQL = "INSERT INTO purchaseItems (purchaseId, itemId, qty, purchasePrice, totalPrice) VALUES (?, ?, ?, ?, ?)";
        String updateItemSQL = "UPDATE items SET averageCostPrice = ?, defaultSellingPrice = ? WHERE id = ?";
        String updateInventorySQL = "UPDATE inventory SET stockQuantity = stockQuantity + ? WHERE itemId = ?";

        Connection conn = null;
        PreparedStatement purchaseStmt = null;
        PreparedStatement itemStmt = null;
        PreparedStatement inventoryStmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Step 1: Insert into purchases table
            purchaseStmt = conn.prepareStatement(insertPurchaseSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            purchaseStmt.setInt(1, purchase.getUserId());
            purchaseStmt.setInt(2, purchase.getVendorId());
            purchaseStmt.setDouble(3, purchase.getTotalAmount());

            int affectedRows = purchaseStmt.executeUpdate();
            if (affectedRows == 0) throw new SQLException("Creating purchase failed, no rows affected.");

            // Get generated purchaseId
            generatedKeys = purchaseStmt.getGeneratedKeys();
            if (!generatedKeys.next()) throw new SQLException("Creating purchase failed, no ID obtained.");
            int purchaseId = generatedKeys.getInt(1);

            // Step 2: Insert into purchaseItems table
            itemStmt = conn.prepareStatement(insertPurchaseItemSQL);
            inventoryStmt = conn.prepareStatement(updateInventorySQL);

            for (PurchaseItem item : purchase.getPurchaseItemList()) {
                itemStmt.setInt(1, purchaseId);
                itemStmt.setInt(2, item.getItemId());
                itemStmt.setInt(3, item.getQty());
                itemStmt.setDouble(4, item.getPurchasePrice());
                itemStmt.setDouble(5, item.getTotalPrice());
                itemStmt.addBatch();

                // Step 3: Update inventory
                inventoryStmt.setInt(1, item.getQty());
                inventoryStmt.setInt(2, item.getItemId());
                inventoryStmt.addBatch();
            }

            itemStmt.executeBatch();
            inventoryStmt.executeBatch();

            // Step 4: Update items table (correcting cost price calculation)
            // SQL to fetch stock quantity and current averageCostPrice
            String getStockAndCostSQL = "SELECT i.stockQuantity, it.averageCostPrice " +
                    "FROM inventory i " +
                    "JOIN items it ON i.itemId = it.id " +
                    "WHERE i.itemId = ?";
            PreparedStatement getStockAndCostStmt = conn.prepareStatement(getStockAndCostSQL);
            PreparedStatement updateItemStmt = conn.prepareStatement(updateItemSQL);

            for (PurchaseItem item : purchase.getPurchaseItemList()) {
                // Fetch existing stock quantity and average cost price
                getStockAndCostStmt.setInt(1, item.getItemId());
                ResultSet rs = getStockAndCostStmt.executeQuery();

                double newAverageCost = item.getPurchasePrice(); // Default if no stock exists
                int existingStock = 0;

                if (rs.next()) {
                    existingStock = rs.getInt("stockQuantity");
                    double existingCost = rs.getDouble("averageCostPrice");

                    if (existingStock > 0) {
                        newAverageCost = ((existingStock * existingCost) + (item.getQty() * item.getPurchasePrice()))
                                / (existingStock + item.getQty());
                    }
                }

                double sellingPrice = newAverageCost * 1.1; // 10% margin

                // Update items table with correct average cost price
                updateItemStmt.setDouble(1, newAverageCost);
                updateItemStmt.setDouble(2, sellingPrice);
                updateItemStmt.setInt(3, item.getItemId());
                updateItemStmt.addBatch();
            }

            updateItemStmt.executeBatch();



            conn.commit(); // Commit transaction
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback on error
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        }
    }
}
