package com.pahana.edu.billing.dao;

import com.pahana.edu.billing.model.Bill;
import java.util.List;

public interface IBillDAO {
    int createBill(Bill bill);
    Bill getBillById(int id);
    List<Bill> getAllBills();
    boolean updateBill(Bill bill);
    boolean deleteBill(int id);
}
