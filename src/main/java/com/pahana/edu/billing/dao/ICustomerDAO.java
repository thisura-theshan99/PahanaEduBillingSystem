package com.pahana.edu.billing.dao;

import com.pahana.edu.billing.model.Customer;
import java.util.List;

public interface ICustomerDAO {
    int addCustomer(Customer customer);
    Customer getCustomerById(int id);
    List<Customer> getAllCustomers();
    boolean updateCustomer(Customer customer);
    boolean deleteCustomer(int id);
}
