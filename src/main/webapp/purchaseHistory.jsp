<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Purchase History</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body class="container mt-4">

    <h2 class="mb-4">Purchase History</h2>
    <div class="d-flex justify-content-between mb-3">
                <a href="dashboard.jsp" class="btn btn-secondary">&larr; Back to Dashboard</a>

            </div>

    <!-- Purchases Table -->
    <table class="table table-bordered">
        <thead>
            <tr>
                <th>Purchase ID</th>
                <th>Vendor</th>
                <th>User</th>
                <th>Total Amount</th>
                <th>Created At</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody id="purchaseHistoryBody">
            <tr><td colspan="6" class="text-center">Loading purchases...</td></tr>
        </tbody>
    </table>

    <!-- Purchase Items Modal -->
    <div class="modal fade" id="itemsModal" tabindex="-1" aria-labelledby="itemsModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="itemsModalLabel">Purchase Items</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th>Item Name</th>
                                <th>Quantity</th>
                                <th>Price</th>
                            </tr>
                        </thead>
                        <tbody id="itemsTableBody">
                            <!-- Items will be dynamically inserted here -->
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
                url: "http://localhost:8080/webapi/purchases",
                method: "GET",
                success: function (purchases) {
                    let tableBody = $("#purchaseHistoryBody");
                    tableBody.empty(); // Clear previous data

                    if (purchases.length === 0) {
                        tableBody.append('<tr><td colspan="6" class="text-center">No purchases found.</td></tr>');
                        return;
                    }

                    purchases.forEach(function (purchase) {
                        let row = `<tr>
                            <td>${purchase.id}</td>
                            <td>${purchase.vendorName}</td>
                            <td>${purchase.userName}</td>
                            <td>$${purchase.totalAmount.toFixed(2)}</td>
                            <td>${purchase.formattedCreatedAt}</td>
                            <td><button class="btn btn-primary btn-sm view-items" data-items='${JSON.stringify(purchase.purchaseItemList)}' data-bs-toggle="modal" data-bs-target="#itemsModal">View Items</button></td>
                        </tr>`;
                        tableBody.append(row);
                    });

                    // Handle "View Items" button click
                    $(".view-items").click(function () {
                        let items = JSON.parse($(this).attr("data-items"));
                        let itemsTableBody = $("#itemsTableBody");
                        itemsTableBody.empty(); // Clear previous items

                        if (items.length === 0) {
                            itemsTableBody.append('<tr><td colspan="3" class="text-center">No items found.</td></tr>');
                        } else {
                            items.forEach(function (item) {
                                let itemRow = `<tr>
                                    <td>${item.itemName}</td>
                                    <td>${item.qty}</td>
                                    <td>$${item.purchasePrice.toFixed(2)}</td>
                                </tr>`;
                                itemsTableBody.append(itemRow);
                            });
                        }
                    });
                },
                error: function () {
                    $("#purchaseHistoryBody").html('<tr><td colspan="6" class="text-center text-danger">Failed to load purchases.</td></tr>');
                }
            });
        });
    </script>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
