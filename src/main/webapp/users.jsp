<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page isELIgnored="true" %>

<html>
<head>
    <title>Users Management</title>

    <!-- Keep only these if your backend injects them dynamically -->
    <meta name="_csrf" content="${_csrf.token}" />
    <meta name="_csrf_header" content="${_csrf.headerName}" />

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">

    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</head>

<body>

<div class="container mt-4">
    <h2>Users Management</h2>
    <div class="d-flex justify-content-between mb-3">
                <a href="dashboard.jsp" class="btn btn-secondary">&larr; Back to Dashboard</a>
                <button class="btn btn-primary mb-3" data-bs-toggle="modal" data-bs-target="#addUserModal">Add User</button>
            </div>
    <!-- Add User Button -->


    <!-- Alert Messages -->
    <div id="alertContainer"></div>

    <!-- Users Table -->
    <table class="table table-bordered table-striped">
        <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Email</th>
                <th>Role</th>
                <th>Created At</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody id="usersTableBody"></tbody>
    </table>
</div>

<!-- Add User Modal -->
<div class="modal fade" id="addUserModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Add User</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <form id="addUserForm">
                    <div class="mb-3">
                        <label>Name:</label>
                        <input type="text" id="addName" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label>Email:</label>
                        <input type="email" id="addEmail" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label>Password:</label>
                        <input type="password" id="addPassword" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label>Role:</label>
                        <select id="addRole" class="form-control">
                            <option value="Admin">Admin</option>
                            <option value="Manager">Manager</option>
                            <option value="Staff">Staff</option>
                        </select>
                    </div>
                    <button type="submit" class="btn btn-success">Add User</button>
                </form>
            </div>
        </div>
    </div>
</div>
<!-- Edit User Modal -->
<div class="modal fade" id="editUserModal" tabindex="-1" aria-labelledby="editUserModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="editUserModalLabel">Edit User</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form id="editUserForm">
                    <input type="hidden" id="editUserId">

                    <div class="mb-3">
                        <label for="editName" class="form-label">Name</label>
                        <input type="text" class="form-control" id="editName" required>
                    </div>

                    <div class="mb-3">
                        <label for="editEmail" class="form-label">Email</label>
                        <input type="email" class="form-control" id="editEmail" required>
                    </div>

                    <div class="mb-3">
                        <label for="editRole" class="form-label">Role</label>
                        <select class="form-control" id="editRole" required>
                            <option value="Admin">Admin</option>
                            <option value="Manager">Manager</option>
                            <option value="Staff">Staff</option>
                        </select>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                <button type="button" class="btn btn-primary" id="saveEditUser">Save Changes</button>
            </div>
        </div>
    </div>
</div>


<script>

$(document).ready(function () {
    loadUsers();

    function getCookie(name) {
        let cookies = document.cookie.split("; ");
        for (let i = 0; i < cookies.length; i++) {
            let parts = cookies[i].split("=");
            if (parts[0] === name) {
                return decodeURIComponent(parts[1]);  // Ensure proper decoding
            }
        }
        return null;
    }

    function getCsrfToken() {
        let cookies = document.cookie.split("; ");
        for (let i = 0; i < cookies.length; i++) {
            let [key, value] = cookies[i].split("=");
            if (key === "XSRF-TOKEN") return value;
        }
        return null;
    }

    function loadUsers() {
        $.get("/webapi/users", function (users) {
            let rows = '';
            users.forEach(user => {
                rows += `<tr>
                    <td>${user.id}</td>
                    <td>${user.name}</td>
                    <td>${user.email}</td>
                    <td>${user.role}</td>
                    <td>${user.formattedCreatedAt}</td>
                    <td>
                        <button class="btn btn-warning btn-sm edit-btn" data-id="${user.id}">Edit</button>
                        <button class="btn btn-danger btn-sm delete-btn" data-id="${user.id}">Delete</button>
                    </td>
                </tr>`;
            });
            $("#usersTableBody").html(rows);
        });
    }

    $("#addUserForm").submit(function (e) {
        e.preventDefault();

        let newUser = {
            name: $("#addName").val(),
            email: $("#addEmail").val(),
            password: $("#addPassword").val(),
            role: $("#addRole").val()
        };

        let csrfToken = getCsrfToken();
        let authToken = getCookie("token");

        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/webapi/users',
            contentType: "application/json",
            data: JSON.stringify(newUser),
            beforeSend: function (xhr) {
                if (authToken) {
                    xhr.setRequestHeader("Authorization", "Bearer " + authToken);
                }
                if (csrfToken) {
                    xhr.setRequestHeader("X-CSRF-TOKEN", csrfToken);
                }
            },
            success: function () {
                $("#addUserModal").modal('hide');
                showAlert('User added successfully!', 'success');
                loadUsers();
            },
            error: function () {
                showAlert('Failed to add user.', 'danger');
            }
        });
    });

    // ✅ Fix: Retrieve userId properly before making the request
    $(document).on("click", ".edit-btn", function () {
        let userId = $(this).data("id");
        $("#editUserId").val(userId);

        $.get(`/webapi/users/${userId}`, function (user) {
            if (!user) {
                alert("User not found!");
                return;
            }

            $("#editName").val(user.name);
            $("#editEmail").val(user.email);
            $("#editRole").val(user.role);

            $("#editUserModal").modal("show");
        }).fail(function () {
            alert("Failed to load user details.");
        });
    });

    // ✅ Fix: Ensure userId is retrieved before making the update request
    $("#saveEditUser").click(function () {
        let userId = $("#editUserId").val();  // ✅ Fix: Retrieve userId here
        let updatedUser = {
            name: $("#editName").val(),
            email: $("#editEmail").val(),
            role: $("#editRole").val()
        };

        let csrfToken = getCookie("XSRF-TOKEN");
        let jwtToken = getCookie("token");

        if (!csrfToken) {
            alert("CSRF Token is missing!");
            return;
        }

        if (!jwtToken) {
            alert("JWT Token is missing or expired! Please log in again.");
            return;
        }

        $.ajax({
            url: `http://localhost:8080/webapi/users/${userId}`,  // ✅ Fix: Use retrieved userId
            type: "PUT",
            contentType: "application/json",
            data: JSON.stringify(updatedUser),
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Authorization", "Bearer " + jwtToken);
                xhr.setRequestHeader("X-CSRF-TOKEN", csrfToken);
            },
            success: function () {
                alert("User updated successfully!");
                $("#editUserModal").modal("hide");
                loadUsers();
            },
            error: function (xhr) {
                alert("Failed to update user: " + xhr.responseText);
            }
        });
    });

    $(document).on("click", ".delete-btn", function () {
        let userId = $(this).data("id");
        if (confirm("Are you sure?")) {
            let csrfToken = getCsrfToken();
            let authToken = getCookie("token");

            $.ajax({
                url: `/webapi/users/${userId}`,
                type: "DELETE",
                beforeSend: function (xhr) {
                    if (authToken) {
                        xhr.setRequestHeader("Authorization", "Bearer " + authToken);
                    }
                    if (csrfToken) {
                        xhr.setRequestHeader("X-CSRF-TOKEN", csrfToken);
                    }
                },
                success: function () {
                    showAlert("User deleted successfully!", "success");
                    loadUsers();
                },
                error: function () {
                    showAlert("Failed to delete user!", "danger");
                }
            });
        }
    });

    function showAlert(message, type) {
        let alertDiv = `<div class="alert alert-${type} alert-dismissible fade show" role="alert">
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>`;
        $("#alertContainer").html(alertDiv);
        setTimeout(() => $(".alert").alert('close'), 3000);
    }
});


</script>

</body>
</html>
