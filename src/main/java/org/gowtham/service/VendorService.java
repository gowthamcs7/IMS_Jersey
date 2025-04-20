package org.gowtham.service;
import org.gowtham.dao.VendorDAO;
import org.gowtham.model.Vendor;

import java.util.List;

public class VendorService {
    private final VendorDAO vendorDAO;

    public VendorService() {
        this.vendorDAO = new VendorDAO();
    }

    public List<Vendor> getAllVendors() {
        return vendorDAO.getAllVendors();
    }

    public Vendor getVendorById(long id) {
        return vendorDAO.getVendorById(id);
    }

    public boolean addVendor(Vendor vendor) {
        return vendorDAO.createVendor(vendor);
    }

    public boolean updateVendor(Vendor vendor) {
        return vendorDAO.updateVendor(vendor);
    }

    public boolean deleteVendor(int id) {
        return vendorDAO.deleteVendor(id);
    }
}
