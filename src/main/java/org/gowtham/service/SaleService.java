package org.gowtham.service;



import org.gowtham.dao.SaleDAO;
import org.gowtham.model.Sale;
import org.gowtham.model.SaleItem;

import java.sql.SQLException;
import java.util.List;

public class SaleService {
    private final SaleDAO saleDAO;

    public SaleService() {
        this.saleDAO = new SaleDAO();
    }

    public boolean processSale(Sale sale, List<SaleItem> saleItems) {
        try {
            return saleDAO.createSale(sale, saleItems);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Sale> getAllSales() {
        try {
            return saleDAO.getAllSales();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
