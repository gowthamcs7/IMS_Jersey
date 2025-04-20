package org.gowtham.service;

import org.gowtham.dao.PurchaseDAO;
import org.gowtham.model.Purchase;

import java.util.List;

public class PurchaseService {
    private final PurchaseDAO purchaseDAO = new PurchaseDAO();

    public boolean createPurchase(Purchase purchase) {
        return purchaseDAO.createPurchase(purchase);
    }

    public List<Purchase> getAllPurchases() {
        return purchaseDAO.getAllPurchases();
    }
}
