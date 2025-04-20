<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Manage Vendors</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">

</head>
<body class="container mt-4">

    <h2 class="text-center mb-4">Manage Vendors</h2>

     <div class="d-flex justify-content-between mb-3">
            <a href="dashboard.jsp" class="btn btn-secondary">&larr; Back to Dashboard</a>

            <button class="btn btn-primary" onclick="openCreateVendorModal()">Add Vendor</button>
        </div>

    <table class="table table-bordered table-striped">
        <thead class="table-dark">
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Contact Person</th>
                <th>Phone</th>
                <th>Email</th>
                <th>Address</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody id="vendorsTable"></tbody>
    </table>

    <!-- Create/Edit Vendor Modal -->
    <div class="modal fade" id="vendorModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="vendorModalTitle">Create Vendor</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form id="vendorForm">
                        <input type="hidden" id="vendorId">
                        <div class="mb-3">
                            <label class="form-label">Name</label>
                            <input type="text" class="form-control" id="vendorName" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Contact Person</label>
                            <input type="text" class="form-control" id="vendorContactPerson" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Phone</label>
                            <input type="text" class="form-control" id="vendorPhone" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Email</label>
                            <input type="email" class="form-control" id="vendorEmail" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Address</label>
                            <input type="text" class="form-control" id="vendorAddress" required>
                        </div>
                        <button type="submit" class="btn btn-success w-100">Save Vendor</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>

        document.addEventListener("DOMContentLoaded", function () {
            fetchVendors();
        });

        function fetchVendors() {
            fetch("http://localhost:8080/webapi/vendors", {
                headers: { "Authorization": "Bearer " + getToken() }
            })
            .then(response => response.json())
            .then(data => populateVendorsTable(data))
            .catch(error => console.error("Error fetching vendors:", error));
        }

        function populateVendorsTable(vendors) {
            const tableBody = document.querySelector("#vendorsTable");
            tableBody.innerHTML = "";
            vendors.forEach(vendor => {
                let row = document.createElement("tr");
                row.innerHTML = `
                    <td>\${vendor.id}</td>
                    <td>\${vendor.name}</td>
                    <td>\${vendor.contactPerson}</td>
                    <td>\${vendor.phone}</td>
                    <td>\${vendor.email}</td>
                    <td>\${vendor.address}</td>
                    <td>
                        <button class="btn btn-warning btn-sm" onclick="editVendor(\${vendor.id})">Edit</button>
                        <button class="btn btn-danger btn-sm" onclick="deleteVendor(\${vendor.id})">Delete</button>
                    </td>
                `;
                tableBody.appendChild(row);
            });
        }

        function getToken() {
            return document.cookie.split('; ').find(row => row.startsWith('token='))?.split('=')[1] || "";
        }

        function openCreateVendorModal() {
            document.getElementById("vendorForm").reset();
            document.getElementById("vendorId").value = "";
            document.getElementById("vendorModalTitle").innerText = "Create Vendor";
            var modal = new bootstrap.Modal(document.getElementById("vendorModal"));
            modal.show();
        }

        function editVendor(id) {
            fetch(`http://localhost:8080/webapi/vendors/\${id}`, {
                headers: { "Authorization": "Bearer " + getToken() }
            })
            .then(response => response.json())
            .then(vendor => {
                document.getElementById("vendorId").value = vendor.id;
                document.getElementById("vendorName").value = vendor.name;
                document.getElementById("vendorContactPerson").value = vendor.contactPerson;
                document.getElementById("vendorPhone").value = vendor.phone;
                document.getElementById("vendorEmail").value = vendor.email;
                document.getElementById("vendorAddress").value = vendor.address;

                document.getElementById("vendorModalTitle").innerText = "Edit Vendor";
                var modal = new bootstrap.Modal(document.getElementById("vendorModal"));
                modal.show();
            })
            .catch(error => console.error("Error fetching vendor details:", error));
        }

        document.getElementById("vendorForm").addEventListener("submit", function (event) {
            event.preventDefault();

            let id = document.getElementById("vendorId").value;
            let vendorData = {
                name: document.getElementById("vendorName").value,
                contactPerson: document.getElementById("vendorContactPerson").value,
                phone: document.getElementById("vendorPhone").value,
                email: document.getElementById("vendorEmail").value,
                address: document.getElementById("vendorAddress").value
            };

            let url = "http://localhost:8080/webapi/vendors";
            let method = "POST";

            if (id) {
                url += `/\${id}`;
                method = "PUT";
            }

            fetch(url, {
                method: method,
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Bearer " + getToken()
                },
                body: JSON.stringify(vendorData)
            })
            .then(response => response.json())
            .then(data => {
                alert(data.message);
                fetchVendors();
                bootstrap.Modal.getInstance(document.getElementById("vendorModal")).hide();
            })
            .catch(error => console.error("Error saving vendor:", error));
        });

        function deleteVendor(id) {
            if (confirm("Are you sure you want to delete this vendor?")) {
                fetch(`http://localhost:8080/webapi/vendors/\${id}`, {
                    method: "DELETE",
                    headers: { "Authorization": "Bearer " + getToken() }
                })
                .then(response => response.json())
                .then(data => {
                    alert(data.message);
                    fetchVendors();
                })
                .catch(error => console.error("Error deleting vendor:", error));
            }
        }

        document.getElementById("searchInput").addEventListener("input", function () {
            let filter = this.value.toLowerCase();
            let rows = document.querySelectorAll("#vendorsTable tr");

            rows.forEach(row => {
                let text = row.innerText.toLowerCase();
                row.style.display = text.includes(filter) ? "" : "none";
            });
        });
    </script>
</body>
</html>
