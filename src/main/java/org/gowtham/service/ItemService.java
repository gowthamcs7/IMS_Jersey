package org.gowtham.service;

import org.gowtham.dao.ItemsDAO;
import org.gowtham.model.Item;

import java.util.List;

public class ItemService {
    private final ItemsDAO itemsDAO;

    public ItemService(){
        itemsDAO = new ItemsDAO();
    }

    public List<Item> getAllItems(){
        return itemsDAO.getAllItems();
    }

    public Item getItemById(int id){
        return itemsDAO.getItemById(id);
    }

    public boolean createItem(Item item) {
        // ✅ Validate input (basic validation)
        if (item.getName() == null || item.getName().trim().isEmpty()) {
            return false; // Name cannot be empty
        }
        if (item.getCategoryId() <= 0) {
            return false; // Invalid category
        }
        if (item.getAverageCostPrice() <= 0 || item.getDefaultSellingPrice() <= 0) {
            return false; // Prices must be greater than 0
        }

        // ✅ Call DAO to save item
        return itemsDAO.addItem(item);
    }

    public boolean updateItem(Item item) {
        return itemsDAO.updateItem(item);
    }

    public boolean deleteItem(int id) {
        return itemsDAO.deleteItem(id);
    }
}
