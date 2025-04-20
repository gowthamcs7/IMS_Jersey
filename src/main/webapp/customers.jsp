<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="true" %>

<html>
<head>
    <title>Customers</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
    <div class="container mt-4">
        <h2 class="text-center">Customers</h2>
        <div class="d-flex justify-content-between mb-3">
            <a href="dashboard.jsp" class="btn btn-secondary">&larr; Back to Dashboard</a>
            <button class="btn btn-primary mb-3" data-bs-toggle="modal" data-bs-target="#createCustomerModal">
                                + Create Customer
                   </button>
        </div>

        <!-- Search Bar -->
        <div class="mb-3">
            <input type="text" id="searchInput" class="form-control" placeholder="Search customers...">
        </div>

        <!-- Customers Table -->
        <table class="table table-striped" id="customersTable">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Phone</th>
                    <th>Email</th>
                    <th>Address</th>
                    <th>Registered</th>
                    <th>Actions</th> <!-- New column for buttons -->
                </tr>
            </thead>
            <tbody>
                <!-- Data will be populated here -->
            </tbody>
        </table>


    </div>

    <!-- Edit Customer Modal -->
    <div class="modal fade" id="editCustomerModal" tabindex="-1" aria-labelledby="editCustomerModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="editCustomerModalLabel">Edit Customer</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form id="editCustomerForm">
                        <input type="hidden" id="customerId">
                        <div class="mb-3">
                            <label for="customerName" class="form-label">Name</label>
                            <input type="text" class="form-control" id="customerName" required>
                        </div>
                        <div class="mb-3">
                            <label for="customerPhone" class="form-label">Phone</label>
                            <input type="text" class="form-control" id="customerPhone" required>
                        </div>
                        <div class="mb-3">
                            <label for="customerEmail" class="form-label">Email</label>
                            <input type="email" class="form-control" id="customerEmail" required>
                        </div>
                        <div class="mb-3">
                            <label for="customerAddress" class="form-label">Address</label>
                            <input type="text" class="form-control" id="customerAddress" required>
                        </div>
                        <button type="submit" class="btn btn-primary">Update</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Create Customer Modal -->
    <div class="modal fade" id="createCustomerModal" tabindex="-1" aria-labelledby="createCustomerModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="createCustomerModalLabel">Create New Customer</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form id="createCustomerForm">
                        <div class="mb-3">
                            <label for="newCustomerName" class="form-label">Name</label>
                            <input type="text" class="form-control" id="newCustomerName" required>
                        </div>
                        <div class="mb-3">
                            <label for="newCustomerPhone" class="form-label">Phone</label>
                            <input type="text" class="form-control" id="newCustomerPhone" required>
                        </div>
                        <div class="mb-3">
                            <label for="newCustomerEmail" class="form-label">Email</label>
                            <input type="email" class="form-control" id="newCustomerEmail" required>
                        </div>
                        <div class="mb-3">
                            <label for="newCustomerAddress" class="form-label">Address</label>
                            <input type="text" class="form-control" id="newCustomerAddress" required>
                        </div>
                        <button type="submit" class="btn btn-success">Create</button>
                    </form>
                </div>
            </div>
        </div>
    </div>


    <script>
        document.addEventListener("DOMContentLoaded", function () {
            fetchCustomers();
        });

        function fetchCustomers() {
            fetch("http://localhost:8080/webapi/customers", {
                method: "GET",
                headers: { "Authorization": "Bearer " + getToken() }
            })
            .then(response => response.json())
            .then(data => populateCustomersTable(data))
            .catch(error => console.error("Error fetching customers:", error));
        }

        function populateCustomersTable(customers) {
            const tableBody = document.querySelector("#customersTable tbody");
            tableBody.innerHTML = "";

            customers.forEach(customer => {
                let row = document.createElement("tr");
                row.innerHTML = `
                    <td>${customer.id}</td>
                    <td>${customer.name}</td>
                    <td>${customer.phone}</td>
                    <td>${customer.email}</td>
                    <td>${customer.address}</td>
                    <td>${customer.formattedCreatedAt}</td>
                    <td>
                        <button class="btn btn-warning btn-sm" onclick="editCustomer(${customer.id})">Edit</button>
                        <button class="btn btn-danger btn-sm" onclick="deleteCustomer(${customer.id})">Delete</button>
                    </td>
                `;
                tableBody.appendChild(row);
            });
        }

        function getToken() {
            return document.cookie.split('; ').find(row => row.startsWith('token='))?.split('=')[1] || "";
        }

        function editCustomer(id) {
            fetch(`http://localhost:8080/webapi/customers/${id}`, {
                method: "GET",
                headers: { "Authorization": "Bearer " + getToken() }
            })
            .then(response => response.json())
            .then(customer => {
                document.getElementById("customerId").value = customer.id;
                document.getElementById("customerName").value = customer.name;
                document.getElementById("customerPhone").value = customer.phone;
                document.getElementById("customerEmail").value = customer.email;
                document.getElementById("customerAddress").value = customer.address;

                var modal = new bootstrap.Modal(document.getElementById("editCustomerModal"));
                modal.show();
            })
            .catch(error => console.error("Error fetching customer details:", error));
        }

        document.getElementById("editCustomerForm").addEventListener("submit", function (event) {
            event.preventDefault();

            let id = document.getElementById("customerId").value;
            let updatedCustomer = {
                name: document.getElementById("customerName").value,
                phone: document.getElementById("customerPhone").value,
                email: document.getElementById("customerEmail").value,
                address: document.getElementById("customerAddress").value
            };

            fetch(`http://localhost:8080/webapi/customers/${id}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Bearer " + getToken()
                },
                body: JSON.stringify(updatedCustomer)
            })
            .then(response => response.json())
            .then(data => {
                alert("Customer updated successfully!");
                fetchCustomers();
                bootstrap.Modal.getInstance(document.getElementById("editCustomerModal")).hide();
            })
            .catch(error => console.error("Error updating customer:", error));
        });

        function deleteCustomer(id) {
            if (confirm("Are you sure you want to delete this customer?")) {
                fetch(`http://localhost:8080/webapi/customers/${id}`, {
                    method: "DELETE",
                    headers: { "Authorization": "Bearer " + getToken() }
                })
                .then(response => response.json())
                .then(data => {
                    alert(data.message);
                    fetchCustomers();
                })
                .catch(error => console.error("Error deleting customer:", error));
            }
        }

        document.getElementById("searchInput").addEventListener("input", function () {
            let filter = this.value.toLowerCase();
            let rows = document.querySelectorAll("#customersTable tbody tr");

            rows.forEach(row => {
                let text = row.innerText.toLowerCase();
                row.style.display = text.includes(filter) ? "" : "none";
            });
        });

        document.getElementById("createCustomerForm").addEventListener("submit", function (event) {
            event.preventDefault(); // Prevent form from refreshing the page

            let newCustomer = {
                name: document.getElementById("newCustomerName").value,
                phone: document.getElementById("newCustomerPhone").value,
                email: document.getElementById("newCustomerEmail").value,
                address: document.getElementById("newCustomerAddress").value
            };

            fetch("http://localhost:8080/webapi/customers", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Bearer " + getToken()
                },
                body: JSON.stringify(newCustomer)
            })
            .then(response => response.json())
            .then(data => {
                alert(data.message);
                fetchCustomers(); // Refresh table after adding customer
                bootstrap.Modal.getInstance(document.getElementById("createCustomerModal")).hide(); // Close modal
            })
            .catch(error => console.error("Error adding customer:", error));
        });

    </script>
</body>
</html>
