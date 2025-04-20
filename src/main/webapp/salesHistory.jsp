<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Sales History</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body class="container mt-4">
    <div class="d-flex justify-content-between mb-3">
                            <a href="dashboard.jsp" class="btn btn-secondary">&larr; Back to Dashboard</a>

                        </div>
    <h2 class="mb-4">Sales History</h2>


    <!-- Sales Table -->
    <table class="table table-bordered">
        <thead>
            <tr>
                <th>Sale ID</th>
                <th>Customer ID</th>
                <th>User ID</th>
                <th>Total Amount</th>
                <th>Created At</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody id="salesHistoryBody">
            <tr><td colspan="6" class="text-center">Loading sales...</td></tr>
        </tbody>
    </table>

    <!-- Sale Items Modal -->
    <div class="modal fade" id="salesModal" tabindex="-1" aria-labelledby="salesModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="salesModalLabel">Sale Items</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th>Item ID</th>
                                <th>Quantity</th>
                                <th>Sale Price</th>
                                <th>Total Price</th>
                            </tr>
                        </thead>
                        <tbody id="salesTableBody">
                            <!-- Sale items will be dynamically inserted here -->
                        </tbody>
                    </table>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>

    <script>
        $(document).ready(function () {
            $.ajax({
                url: "http://localhost:8080/webapi/sales",
                method: "GET",
                success: function (sales) {
                    let tableBody = $("#salesHistoryBody");
                    tableBody.empty(); // Clear previous data

                    if (sales.length === 0) {
                        tableBody.append('<tr><td colspan="6" class="text-center">No sales found.</td></tr>');
                        return;
                    }

                    sales.forEach(function (sale) {
                        let row = `<tr>
                            <td>${sale.id}</td>
                            <td>${sale.customerID}</td>
                            <td>${sale.userID}</td>
                            <td>$${sale.totalAmount.toFixed(2)}</td>
                            <td>${sale.formattedCreatedAt}</td>
                            <td><button class="btn btn-primary btn-sm view-sales" data-items='${JSON.stringify(sale.saleItems)}' data-bs-toggle="modal" data-bs-target="#salesModal">View Items</button></td>
                        </tr>`;
                        tableBody.append(row);
                    });

                    // Handle "View Items" button click
                    $(".view-sales").click(function () {
                        let items = JSON.parse($(this).attr("data-items"));
                        let salesTableBody = $("#salesTableBody");
                        salesTableBody.empty(); // Clear previous items

                        if (items.length === 0) {
                            salesTableBody.append('<tr><td colspan="4" class="text-center">No items found.</td></tr>');
                        } else {
                            items.forEach(function (item) {
                                let itemRow = `<tr>
                                    <td>${item.itemId}</td>
                                    <td>${item.qty}</td>
                                    <td>$${item.salePrice.toFixed(2)}</td>
                                    <td>$${item.totalPrice.toFixed(2)}</td>
                                </tr>`;
                                salesTableBody.append(itemRow);
                            });
                        }
                    });
                },
                error: function () {
                    $("#salesHistoryBody").html('<tr><td colspan="6" class="text-center text-danger">Failed to load sales.</td></tr>');
                }
            });
        });
    </script>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
