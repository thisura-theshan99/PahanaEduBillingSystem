package com.pahana.edu.billing.service;

import com.pahana.edu.billing.model.Bill;
import com.pahana.edu.billing.model.BillItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class BillCalculator {

    /** Round to 2 decimals using HALF_UP */
    public static double round2(double v) {
        return BigDecimal.valueOf(v).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    /** Safe discount between 0 and 100 */
    public static double normalizeDiscount(double discountPercent) {
        if (Double.isNaN(discountPercent)) return 0.0;
        if (discountPercent < 0) return 0.0;
        if (discountPercent > 100) return 100.0;
        return discountPercent;
    }

    /** Compute a single line total = qty * unitPrice */
    public static double computeLineTotal(int quantity, double unitPrice) {
        if (quantity < 0) quantity = 0;
        double total = quantity * unitPrice;
        return round2(total);
    }

    /** Recompute lineTotal for each BillItem (quantity * unitPrice) */
    public static void recomputeLines(List<BillItem> items) {
        if (items == null) return;
        for (BillItem bi : items) {
            double lt = computeLineTotal(bi.getQuantity(), bi.getUnitPrice());
            bi.setLineTotal(lt);
        }
    }

    /** Sum of all line totals (gross before discount) */
    public static double computeGrossTotal(List<BillItem> items) {
        if (items == null || items.isEmpty()) return 0.0;
        double sum = 0.0;
        for (BillItem bi : items) {
            sum += (bi.getLineTotal());
        }
        return round2(sum);
    }

    /** Apply discount to gross (net = gross - gross*disc/100) */
    public static double computeNetTotal(double gross, double discountPercent) {
        double d = normalizeDiscount(discountPercent);
        double net = gross - (gross * d / 100.0);
        return round2(net);
    }

    /** Convenience: compute all totals and set into Bill */
    public static void computeTotalsInto(Bill bill) {
        if (bill == null) return;
        recomputeLines(bill.getItems());
        double gross = computeGrossTotal(bill.getItems());
        bill.setTotalAmount(gross);
        bill.setDiscountPercent(normalizeDiscount(bill.getDiscountPercent()));
        bill.setNetAmount(computeNetTotal(gross, bill.getDiscountPercent()));
    }

    /** Quick validator for quantities & prices */
    public static boolean isValidItems(List<BillItem> items) {
        if (items == null || items.isEmpty()) return false;
        for (BillItem bi : items) {
            if (bi.getQuantity() <= 0) return false;
            if (bi.getUnitPrice() < 0) return false;
        }
        return true;
    }
}
