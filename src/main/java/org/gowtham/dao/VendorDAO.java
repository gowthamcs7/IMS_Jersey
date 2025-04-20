package org.gowtham.dao;


//import org.gowtham.model.User;
import org.gowtham.model.Vendor;
import org.gowtham.helper.DatabaseConnection;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VendorDAO {
    public List<Vendor> getAllVendors() {
        List<Vendor> vendors = new ArrayList<>();
        String query = "SELECT * FROM vendors";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,  // Makes the ResultSet scrollable
                     ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                vendors.add(new Vendor(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("contactPerson"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getTimestamp("createdAt")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vendors;
    }

    // Get User by ID
    public Vendor getVendorById(long id) {
        String query = "SELECT * FROM vendors WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Vendor(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("contactPerson"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getTimestamp("createdAt")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean createVendor(Vendor vendor){
        String query = "INSERT INTO vendors(name,contactPerson,phone,email,address) VALUES(?,?,?,?,?)";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement psmt = connection.prepareStatement(query)){
            psmt.setString(1,vendor.getName());
            psmt.setString(2,vendor.getContactPerson());
            psmt.setString(3,vendor.getPhone());
            psmt.setString(4,vendor.getEmail());
            psmt.setString(5,vendor.getAddress());
            return psmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean updateVendor(Vendor vendor) {
        String query = "UPDATE vendors SET name = ?, contactPerson = ?, phone = ?, email = ?, address = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, vendor.getName());
            pstmt.setString(2, vendor.getContactPerson());
            pstmt.setString(3, vendor.getPhone());
            pstmt.setString(4, vendor.getEmail());
            pstmt.setString(5, vendor.getAddress());
            pstmt.setInt(6,vendor.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteVendor(int id){
        String query = "DELETE FROM vendors WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

}
