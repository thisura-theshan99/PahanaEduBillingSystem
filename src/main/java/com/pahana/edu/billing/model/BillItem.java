package com.pahana.edu.billing.model;

public class BillItem {
    private int id;          // Unique ID for each bill_item
    private int billId;      // FK -> bills.id
    private int itemId;      // FK -> items.id
    private int quantity;    // >= 1
    private double unitPrice; // snapshot at billing time
    private double lineTotal; // quantity * unitPrice

    // --- Getters & Setters ---
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getBillId() {
        return billId;
    }
    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getItemId() {
        return itemId;
    }
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getLineTotal() {
        return lineTotal;
    }
    public void setLineTotal(double lineTotal) {
        this.lineTotal = lineTotal;
    }

    // Optional: Helper method to recompute totals
    public void recomputeLineTotal() {
        this.lineTotal = this.unitPrice * this.quantity;
    }
}
