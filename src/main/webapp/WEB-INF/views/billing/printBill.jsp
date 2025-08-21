<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*, java.time.format.DateTimeFormatter" %>
<%@ page import="com.pahana.edu.billing.model.Bill, com.pahana.edu.billing.model.BillItem" %>

<%! String fmt(double d){ return String.format(java.util.Locale.US, "%.2f", d); } %>

<%
    Bill bill = (Bill) request.getAttribute("bill");
    @SuppressWarnings("unchecked")
    List<BillItem> billItems = (List<BillItem>) request.getAttribute("billItems");

    if (bill == null) {
%>
<!DOCTYPE html>
<html><head><meta charset="UTF-8"><title>Print Bill</title></head>
<body>
<p style="color:#c00;">No bill data to print.</p>
<p><a href="<%= request.getContextPath() %>/bills?action=view">Back to Bills</a></p>
</body></html>
<%
        return;
    }

    String createdAt = "";
    try {
        if (bill.getCreatedAt() != null) {
            createdAt = bill.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }
    } catch (Exception ignore) { createdAt = ""; }

    double subtotal    = bill.getTotalAmount();
    double discountPct = bill.getDiscountPercent();
    double discountAmt = Math.round((subtotal * (discountPct/100.0)) * 100.0) / 100.0;
    double net         = bill.getNetAmount();
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>Invoice #<%= bill.getId() %></title>
    <style>
        body{font-family:Arial, sans-serif; color:#000; margin:16px;}
        .muted{color:#666; font-size:12px;}
        .right{text-align:right;}
        .box{border:1px solid #ccc; padding:10px; border-radius:6px; margin-bottom:14px;}
        table{border-collapse:collapse; width:100%;}
        th,td{border:1px solid #ddd; padding:8px; text-align:left;}
        th{background:#f3f3f3;}
        tfoot td{border:none;}

        /* top toolbar with BACK (left) and PRINT (right) */
        .toolbar{display:flex; align-items:center; justify-content:space-between; margin:6px 0 10px;}
        .toolbar a{color:#4a2db6; text-decoration:none;}
        .toolbar a:hover{text-decoration:underline;}
        .btn-print{border:1px solid #333; background:#fff; padding:6px 12px; border-radius:6px; cursor:pointer;}
        .btn-print:hover{background:#f3f3f3;}

        .header{display:flex; align-items:flex-start; justify-content:space-between; margin:4px 0 12px;}
        .brand{font-size:20px; font-weight:bold;}

        @media print{
            .toolbar{display:none !important;}
            body{margin:0;}
        }
    </style>
</head>
<body>

<!-- BACK on left, PRINT on top-right -->
<div class="toolbar no-print">
    <div><a href="<%= request.getContextPath() %>/bills?action=view">Back</a></div>
    <div><button class="btn-print" onclick="window.print()">Print</button></div>
</div>

<div class="header">
    <div>
        <div class="brand">Pahana Edu Billing</div>
        <div class="muted">Invoice</div>
    </div>
    <div class="right">
        <div><strong>Invoice #</strong> <%= bill.getId() %></div>
        <div><strong>Date:</strong> <%= createdAt %></div>
    </div>
</div>

<div class="box">
    <table>
        <tbody>
        <tr>
            <td style="width:180px;"><strong>Customer ID</strong></td>
            <td><%= bill.getCustomerId() %></td>
        </tr>
        <tr>
            <td><strong>Bill ID</strong></td>
            <td><%= bill.getId() %></td>
        </tr>
        </tbody>
    </table>
</div>

<table>
    <thead>
    <tr>
        <th style="width:80px;">Item ID</th>
        <th>Description</th>
        <th class="right" style="width:120px;">Unit Price</th>
        <th class="right" style="width:80px;">Qty</th>
        <th class="right" style="width:120px;">Line Total</th>
    </tr>
    </thead>
    <tbody>
    <%
        if (billItems != null && !billItems.isEmpty()) {
            for (BillItem bi : billItems) {
                double unit = bi.getUnitPrice();
                int qty     = bi.getQuantity();
                double line = (bi.getLineTotal() != 0.0) ? bi.getLineTotal() : (unit * qty);
    %>
    <tr>
        <td><%= bi.getItemId() %></td>
        <td>Item #<%= bi.getItemId() %></td>
        <td class="right"><%= fmt(unit) %></td>
        <td class="right"><%= qty %></td>
        <td class="right"><%= fmt(line) %></td>
    </tr>
    <%
        }
    } else {
    %>
    <tr><td colspan="5" class="muted">No items on this bill.</td></tr>
    <%
        }
    %>
    </tbody>
</table>

<table style="margin-top:12px;">
    <tbody>
    <tr>
        <td style="border:none;"></td>
        <td style="border:none; width:360px;">
            <table style="width:100%; border:none;">
                <tr>
                    <td style="border:none; text-align:right;"><strong>Subtotal:</strong></td>
                    <td style="border:none; text-align:right; width:140px;"><%= fmt(subtotal) %></td>
                </tr>
                <tr>
                    <td style="border:none; text-align:right;"><strong>Discount (<%= fmt(discountPct) %>%)</strong></td>
                    <td style="border:none; text-align:right;"><%= fmt(discountAmt) %></td>
                </tr>
                <tr>
                    <td style="border:none; text-align:right; font-size:18px;"><strong>Net Total:</strong></td>
                    <td style="border:none; text-align:right; font-size:18px;"><strong><%= fmt(net) %></strong></td>
                </tr>
            </table>
        </td>
    </tr>
    </tbody>
</table>

<p class="muted" style="margin-top:16px;">
    Note: To show item names instead of IDs, load them in the servlet (join with <code>items</code>) and pass as attributes.
</p>

</body>
</html>
