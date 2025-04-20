<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="true" %>
<%@ page import="java.util.List" %>
<%@ page import="org.gowtham.model.Item" %>
<%@ page import="com.google.gson.Gson" %>
<html>
<head>
    <title>Items</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
<div class="container mt-5">

    <h2 class="mb-4">Items</h2>

    <div class="d-flex justify-content-between mb-3">
                <a href="dashboard.jsp" class="btn btn-secondary">&larr; Back to Dashboard</a>

                 <button class="btn btn-primary mb-3" data-bs-toggle="modal" data-bs-target="#addItemModal">Add Item</button>
            </div>

    <table class="table table-bordered">
        <thead>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Category ID</th>
            <th>Cost Price</th>
            <th>Selling Price</th>
            <th>Created At</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody id="itemsTable">
        <!-- Items will be loaded here -->
        </tbody>
    </table>
</div>

<!-- Add Item Modal -->
<div class="modal fade" id="addItemModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Add Item</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <form id="addItemForm">
                    <div class="mb-3">
                        <label class="form-label">Name</label>
                        <input type="text" class="form-control" id="addItemName" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Category ID</label>
                        <input type="number" class="form-control" id="addItemCategoryId" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Cost Price</label>
                        <input type="number" step="0.01" class="form-control" id="addItemCostPrice" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Selling Price</label>
                        <input type="number" step="0.01" class="form-control" id="addItemSellingPrice" required>
                    </div>
                    <button type="submit" class="btn btn-success">Add</button>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- Edit Item Modal -->
<div class="modal fade" id="editItemModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Edit Item</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <form id="editItemForm">
                    <input type="hidden" id="editItemId">
                    <div class="mb-3">
                        <label class="form-label">Name</label>
                        <input type="text" class="form-control" id="editItemName" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Category ID</label>
                        <input type="number" class="form-control" id="editItemCategoryId" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Cost Price</label>
                        <input type="number" step="0.01" class="form-control" id="editItemCostPrice" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Selling Price</label>
                        <input type="number" step="0.01" class="form-control" id="editItemSellingPrice" required>
                    </div>
                    <button type="submit" class="btn btn-primary">Update</button>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- Delete Confirmation Modal -->
<div class="modal fade" id="deleteItemModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Confirm Deletion</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <p>Are you sure you want to delete this item?</p>
                <button id="confirmDeleteBtn" class="btn btn-danger">Delete</button>
            </div>
        </div>
    </div>
</div>

<script>
const token = getCookie("token");

$(document).ready(function() {
    const token = getCookie("token");
    console.log("Retrieved Token:", token); // ✅ Debugging Step

    if (!token) {
        alert("Session expired! Redirecting to login...");
        window.location.href = "login.jsp";
        return;
    }
    function loadItems() {
        $.ajax({
            url: "/webapi/items",
            type: "GET",
            headers: { "Authorization": "Bearer " + token },
            success: function(data) {
                let rows = "";
                data.forEach(item => {
                    rows += `<tr>
                        <td>${item.id}</td>
                        <td>${item.name}</td>
                        <td>${item.categoryId}</td>
                        <td>${item.averageCostPrice}</td>
                        <td>${item.defaultSellingPrice}</td>
                        <td>${item.formattedCreatedAt}</td>
                        <td>
                            <button class="btn btn-warning btn-sm editItemBtn" data-id="${item.id}">Edit</button>
                            <button class="btn btn-danger btn-sm deleteItemBtn" data-id="${item.id}">Delete</button>
                        </td>
                    </tr>`;
                });
                $("#itemsTable").html(rows);
            },
            error: function(xhr) {
                if (xhr.status === 401) {
                    alert("Unauthorized! Redirecting to login...");
                    window.location.href = "login.jsp";
                }
            }
        });
    }

    loadItems();

    // Submit New Item Form
        $("#addItemForm").submit(function(event) {
            event.preventDefault(); // Prevent default form submission

            let newItem = {
                name: $("#addItemName").val(),
                categoryId: $("#addItemCategoryId").val(),
                averageCostPrice: $("#addItemCostPrice").val(),
                defaultSellingPrice: $("#addItemSellingPrice").val()
            };

            $.ajax({
                url: "/webapi/items",
                type: "POST",
                contentType: "application/json",
                headers: { "Authorization": "Bearer " + token },
                data: JSON.stringify(newItem),
                success: function(response) {
                    alert("Item added successfully!");
                    $("#addItemModal").modal("hide");
                    loadItems(); // Refresh item list
                },
                error: function(xhr) {
                    console.error("Error adding item:", xhr.responseText);
                    alert("Failed to add item!");
                }
            });
        });

    // Open Edit Item Modal
    $(document).on("click", ".editItemBtn", function() {
        let itemId = $(this).data("id");
        $.ajax({
            url: `/webapi/items/${itemId}`, // ✅ Corrected syntax
            type: "GET",
            headers: { "Authorization": "Bearer " + token },
            success: function(item) {
                $("#editItemId").val(item.id);
                $("#editItemName").val(item.name);
                $("#editItemCategoryId").val(item.categoryId);
                $("#editItemCostPrice").val(item.averageCostPrice);
                $("#editItemSellingPrice").val(item.defaultSellingPrice);
                $("#editItemModal").modal("show");
            },
            error: function(xhr) {
                console.error("Error fetching item:", xhr.responseText); // ✅ Debugging
                if (xhr.status === 401) {
                    alert("Unauthorized! Redirecting to login...");
                    window.location.href = "login.jsp";
                } else {
                    alert("Failed to fetch item details!");
                }
            }
        });
    });


    // Submit Edit Item Form
    $("#editItemForm").submit(function(event) {
        event.preventDefault();
        let itemId = $("#editItemId").val();
        let itemData = {
            name: $("#editItemName").val(),
            categoryId: $("#editItemCategoryId").val(),
            averageCostPrice: $("#editItemCostPrice").val(),
            defaultSellingPrice: $("#editItemSellingPrice").val()
        };
        $.ajax({
            url: `/webapi/items/${itemId}`,
            type: "PUT",
            contentType: "application/json",
            headers: { "Authorization": "Bearer " + token },
            data: JSON.stringify(itemData),
            success: function() {
                $("#editItemModal").modal("hide");
                loadItems();
            },
            error: function(xhr) {
                if (xhr.status === 401) {
                    alert("Unauthorized! Redirecting to login...");
                    window.location.href = "login.jsp";
                }
            }
        });
    });

    // Delete Item
    let deleteItemId;
    $(document).on("click", ".deleteItemBtn", function() {
        deleteItemId = $(this).data("id");
        $("#deleteItemModal").modal("show");
    });

    $("#confirmDeleteBtn").click(function() {
        $.ajax({
            url: `/webapi/items/${deleteItemId}`,
            type: "DELETE",
            headers: { "Authorization": "Bearer " + token },
            success: function() {
                $("#deleteItemModal").modal("hide");
                loadItems();
            },
            error: function(xhr) {
                if (xhr.status === 401) {
                    alert("Unauthorized! Redirecting to login...");
                    window.location.href = "login.jsp";
                }
            }
        });
    });
});
$(document).ready(function() {
    const token = getCookie("token");

    // Open Add Item Modal
    $("#addItemBtn").click(function() {
        $("#addItemModal").modal("show");
    });


});


// Function to get token from cookies
function getCookie(name) {
    const cookies = document.cookie.split("; ");
    for (let cookie of cookies) {
        const [key, value] = cookie.split("=");
        if (key === name) return decodeURIComponent(value);
    }
    return null;
}

</script>

</body>
</html>
