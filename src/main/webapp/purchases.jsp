<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>New Purchase</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body class="container mt-4">
 <div class="d-flex justify-content-between mb-3">
                    <a href="dashboard.jsp" class="btn btn-secondary">&larr; Back to Dashboard</a>

                </div>

    <h2 class="mb-4">New Purchase</h2>

    <form action="/webapi/purchases" method="POST" id="purchaseForm">
        <!-- Vendors Dropdown -->
        <div class="mb-3">
            <label for="vendorDropdown" class="form-label">Select Vendor:</label>
            <select name="vendorId" id="vendorDropdown" class="form-select" required>
                <option value="">Loading vendors...</option>
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
                    <th>Unit Price</th>
                    <th>Total Price</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody></tbody>
        </table>

        <!-- Hidden Input to Store Items -->
        <input type="hidden" name="purchaseItems" id="purchaseItems">

        <!-- Total Purchase Price -->
        <div class="mb-3">
            <h4>Total: <span id="totalAmount">0.00</span></h4>
        </div>

        <!-- Submit Button -->
        <button type="submit" class="btn btn-success">Submit Purchase</button>
    </form>

   <!-- JavaScript for Fetching Vendors & Items -->
   <script>
       function fetchItems(retryCount = 3) {
           $.ajax({
               url: "http://localhost:8080/webapi/items",
               method: "GET",
               success: function (items) {
                                  console.log("Items API Response:", items);
                                  let itemDropdown = $("#itemDropdown");
                                  itemDropdown.html('<option value="">Select Item</option>');
                                  items.forEach(function (item) {
                                      itemDropdown.append(`<option value="${item.id}" data-price="${item.averageCostPrice}">${item.name}</option>`);
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
           let purchaseItems = [];

           // Fetch Vendors
           $.ajax({
               url: "http://localhost:8080/webapi/vendors",
               method: "GET",
               success: function (vendors) {
                   console.log("Vendors API Response:", vendors);
                   let vendorDropdown = $("#vendorDropdown");
                   vendorDropdown.html('<option value="">Select Vendor</option>');
                   vendors.forEach(function (vendor) {
                       vendorDropdown.append(`<option value="${vendor.id}">${vendor.name}</option>`);
                   });
               },
               error: function (xhr, status, error) {
                   console.error("Error fetching vendors:", error);
               }
           });

           // Fetch Items
           fetchItems();

           // Add Item to List
           $("#addItem").click(function () {
               let itemId = $("#itemDropdown").val();
               let itemName = $("#itemDropdown option:selected").text();
               let unitPrice = parseFloat($("#itemDropdown option:selected").data("price"));
               let quantity = parseInt($("#quantityInput").val(), 10);

               if (!itemId || isNaN(quantity) || quantity <= 0) {
                   alert("Please select an item and enter a valid quantity.");
                   return;
               }

               let totalPrice = (quantity * unitPrice).toFixed(2);
               purchaseItems.push({ itemId, quantity, unitPrice });

               // Add Row to Table
               let newRow = `<tr>
                   <td>${itemName}</td>
                   <td>${quantity}</td>
                   <td>${unitPrice.toFixed(2)}</td>
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
               purchaseItems.splice(rowIndex, 1);
               $(this).closest("tr").remove();
               updateTotal();
           });

           // Update Total Price
           function updateTotal() {
               let total = purchaseItems.reduce((sum, item) => sum + (item.quantity * item.unitPrice), 0);
               $("#totalAmount").text(total.toFixed(2));

               // Store items in hidden input
               $("#purchaseItems").val(JSON.stringify(purchaseItems));
           }

           // ✅ **Intercept Form Submission & Send JSON Instead**
           $("#purchaseForm").submit(function (event) {
               event.preventDefault(); // Prevent default form submission

               let vendorId = $("#vendorDropdown").val();
               if (!vendorId || purchaseItems.length === 0) {
                   alert("Please select a vendor and add at least one item.");
                   return;
               }

               // Convert purchaseItems to match the API format
               let formattedItems = purchaseItems.map(item => ({
                   itemId: parseInt(item.itemId),  // Convert ID to number
                   qty: item.quantity,  // Change key from "quantity" to "qty"
                   purchasePrice: item.unitPrice  // Change key from "unitPrice" to "purchasePrice"
               }));

               let purchaseData = {
                   userId: 1,  // Set userId (Modify based on your session logic)
                   vendorId: parseInt(vendorId),  // Convert vendorId to number
                   totalAmount: parseFloat($("#totalAmount").text()),  // Extract total amount
                   purchaseItemList: formattedItems // Send the modified item list
               };

               $.ajax({
                   url: "http://localhost:8080/webapi/purchases",
                   method: "POST",
                   contentType: "application/json", // ✅ Ensure JSON format
                   data: JSON.stringify(purchaseData), // ✅ Convert to JSON string
                   success: function (response) {
                       alert("Purchase saved successfully!");
                       window.location.reload();
                   },
                   error: function (xhr, status, error) {
                       console.error("Error submitting purchase:", error);
                       alert("Failed to save purchase.");
                   }
               });
           });

       });


   </script>
   <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>



</body>
</html>
