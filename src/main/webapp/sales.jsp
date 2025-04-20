<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>New Sale</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body class="container mt-4">
 <div class="d-flex justify-content-between mb-3">
                    <a href="dashboard.jsp" class="btn btn-secondary">&larr; Back to Dashboard</a>

                </div>

    <h2 class="mb-4">New Sale</h2>

    <form action="/webapi/sales" method="POST" id="saleForm">
        <!-- Customers Dropdown -->
        <div class="mb-3">
            <label for="customerDropdown" class="form-label">Select Customer:</label>
            <select name="customerID" id="customerDropdown" class="form-select" required>
                <option value="">Loading customers...</option>
            </select>
        </div>

        <!-- Items Selection -->
        <div class="mb-3">
            <label for="itemDropdown" class="form-label">Select Item:</label>
            <select id="itemDropdown" class="form-select">
                <option value="">Loading items...</option>
            </select>
        </div>

        <!-- Quantity Input -->
        <div class="mb-3">
            <label for="quantityInput" class="form-label">Quantity:</label>
            <input type="number" id="quantityInput" class="form-control" min="1">
        </div>

        <!-- Add Item Button -->
        <button type="button" class="btn btn-primary mb-3" id="addItem">Add Item</button>

        <!-- Items Table -->
        <table class="table table-bordered" id="itemsTable">
            <thead>
                <tr>
                    <th>Item Name</th>
                    <th>Quantity</th>
                    <th>Sale Price</th>
                    <th>Total Price</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody></tbody>
        </table>

        <!-- Hidden Input to Store Sale Items -->
        <input type="hidden" name="saleItems" id="saleItems">

        <!-- Total Sale Price -->
        <div class="mb-3">
            <h4>Total: <span id="totalAmount">0.00</span></h4>
        </div>

        <!-- Submit Button -->
        <button type="submit" class="btn btn-success">Submit Sale</button>
    </form>

    <!-- JavaScript for Fetching Customers & Items -->
    <script>
    function fetchItems(retryCount = 3) {
        $.ajax({
            url: "http://localhost:8080/webapi/items",
            method: "GET",
            success: function (items) {
                console.log(items);
                let itemDropdown = $("#itemDropdown");
                itemDropdown.html('<option value="">Select Item</option>');
                items.forEach(function (item) {
                    itemDropdown.append(`<option value="${item.id}" data-price="${item.defaultSellingPrice}">${item.name}</option>`);
                });
            },
            error: function (xhr, status, error) {
                console.error("Error fetching items:", error);
                if (retryCount > 0) {
                    console.log(`Retrying... (${3 - retryCount + 1})`);
                    setTimeout(() => fetchItems(retryCount - 1), 1000);
                }
            }
        });
    }
        $(document).ready(function () {
            let saleItems = [];

            // Fetch Customers
            $.ajax({
                url: "http://localhost:8080/webapi/customers",
                method: "GET",
                success: function (customers) {
                    let customerDropdown = $("#customerDropdown");
                    customerDropdown.html('<option value="">Select Customer</option>');
                    customers.forEach(function (customer) {
                        customerDropdown.append(`<option value="${customer.id}">${customer.name}</option>`);
                    });
                },
                error: function (xhr, status, error) {
                    console.error("Error fetching customers:", error);
                }
            });


            // Fetch Items
            fetchItems();

            // Add Item to List
            $("#addItem").click(function () {
                let itemId = $("#itemDropdown").val();
                let itemName = $("#itemDropdown option:selected").text();
                let salePrice = parseFloat($("#itemDropdown option:selected").data("price"));
                let quantity = parseInt($("#quantityInput").val(), 10);

                if (!itemId || isNaN(quantity) || quantity <= 0) {
                    alert("Please select an item and enter a valid quantity.");
                    return;
                }

                let totalPrice = (quantity * salePrice).toFixed(2);
                saleItems.push({ itemId, qty: quantity, salePrice, totalPrice });

                // Add Row to Table
                let newRow = `<tr>
                    <td>${itemName}</td>
                    <td>${quantity}</td>
                    <td>${salePrice.toFixed(2)}</td>
                    <td>${totalPrice}</td>
                    <td><button type="button" class="btn btn-danger btn-sm removeItem">Remove</button></td>
                </tr>`;
                $("#itemsTable tbody").append(newRow);
                updateTotal();

                // Reset Inputs
                $("#itemDropdown").val("");
                $("#quantityInput").val("");
            });

            // Remove Item from List
            $("#itemsTable").on("click", ".removeItem", function () {
                let rowIndex = $(this).closest("tr").index();
                saleItems.splice(rowIndex, 1);
                $(this).closest("tr").remove();
                updateTotal();
            });

            // Update Total Price
            function updateTotal() {
                let total = saleItems.reduce((sum, item) => sum + (item.qty * item.salePrice), 0);
                $("#totalAmount").text(total.toFixed(2));

                // Store items in hidden input
                $("#saleItems").val(JSON.stringify(saleItems));
            }

            // Submit Form
            $("#saleForm").submit(function (event) {
                event.preventDefault();

                let customerID = $("#customerDropdown").val();
                if (!customerID) {
                    alert("Please select a customer.");
                    return;
                }

                if (saleItems.length === 0) {
                    alert("Please add at least one item to the sale.");
                    return;
                }

                let saleData = {
                    customerID: parseInt(customerID),
                    userID: 3, // Hardcoded userID (change as needed)
                    totalAmount: parseFloat($("#totalAmount").text()),
                    saleItems: saleItems
                };

                $.ajax({
                    url: "http://localhost:8080/webapi/sales",
                    method: "POST",
                    contentType: "application/json",
                    data: JSON.stringify(saleData),
                    success: function (response) {
                        alert(response.message);
                        window.location.reload();
                    },
                    error: function (xhr, status, error) {
                        if (xhr.responseJSON && xhr.responseJSON.message) {
                                    alert(xhr.responseJSON.message);  // Directly show backend error message
                                }
                        else {
                                    alert("An unexpected error occurred.");  // Fallback for unknown errors
                                }

                    }
                });
            });
        });
    </script>
</body>
</html>
