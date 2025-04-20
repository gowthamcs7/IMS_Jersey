package org.gowtham.service;



import org.gowtham.dao.CustomersDAO;
import org.gowtham.model.Customer;

import java.util.List;

public class CustomerService {
    private final CustomersDAO customersDAO;

    public CustomerService() {
        this.customersDAO = new CustomersDAO();
    }

    public List<Customer> getAllCustomers() {
        return customersDAO.getAllCustomers();
    }

    public Customer getCustomerById(long id) {
        return customersDAO.getCustomerById(id);
    }

    public boolean addCustomer(Customer customer) {
        return customersDAO.createCustomer(customer);
    }

    public boolean updateCustomer(Customer customer) {
        return customersDAO.updateCustomer(customer);
    }

    public boolean deleteCustomer(int id) {
        return customersDAO.deleteCustomer(id);
    }
}
