<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inventory Management System</title>

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">

    <style>
        body {
            display: flex;
        }
        .sidebar {
            width: 250px;
            height: 100vh;
            background: #343a40;
            color: white;
            padding-top: 20px;
        }
        .sidebar a {
            display: block;
            padding: 10px 20px;
            color: white;
            text-decoration: none;
        }
        .sidebar a:hover {
            background: #495057;
        }
        .content {
            flex-grow: 1;
            padding: 20px;
        }
    </style>

</head>
<body>

    <!-- Sidebar -->
    <div class="sidebar">
        <h4 class="text-center">Dashboard</h4>
        <hr>

        <a href="#" id="homeLink">Home</a>
        <div id="sidebarLinks">

        </div>


    </div>


    <!-- Main Content -->
    <div class="content">
    <div class="d-flex justify-content-end">
                <button id="logoutBtn" class="btn btn-danger">Logout</button>
            </div>
        <h2>Inventory Management System</h2>
        <hr>

        <h4>Inventory</h4>

        <!-- Inventory Table -->
        <table id="inventoryTable" border="1" class="table table-bordered">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Category</th>
                    <th>Cost Price</th>
                    <th>Selling Price</th>
                    <th>Stock Quantity</th>
                </tr>
            </thead>
            <tbody id="inventoryTableBody">
                <!-- Data will be populated here -->
            </tbody>
        </table>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        console.log("🟢 Dashboard JS Loaded");
    </script>

    <script>
        // ✅ Function to get token from cookies
        function getCookie(name) {
            let cookies = document.cookie.split("; ");
            for (let i = 0; i < cookies.length; i++) {
                let [key, value] = cookies[i].split("=");
                if (key === name) return value;
            }
            return null;
        }

        // ✅ Decode JWT (Basic way)
        function decodeToken(token) {
            try {
                let payload = JSON.parse(atob(token.split(".")[1]));
                console.log("🟢 Decoded Token:", payload);
                return payload;
            } catch (error) {
                console.error("🔴 Error decoding token:", error);
                return null;
            }
        }

        // ✅ Hide menu items based on role
        function setSidebarPermissions(role) {
            let usersLink = document.getElementById("usersLink");
            let purchasesLink = document.getElementById("purchasesLink");
            let vendorsLink = document.getElementById("vendorsLink");

            if (role === "Manager") {
                if (usersLink) usersLink.style.display = "none";
            } else if (role === "Staff") {
                if (usersLink) usersLink.style.display = "none";
                if (purchasesLink) purchasesLink.style.display = "none";
                if (vendorsLink) vendorsLink.style.display = "none";
            }
        }


        // ✅ Fetch inventory items from API
        function fetchInventory() {
            fetch("http://localhost:8080/webapi/items", {
                method: "GET",
                headers: { "Content-Type": "application/json" },
                credentials: "include"
            })
            .then(response => response.json())
            .then(data => {
                console.log("🟢 Inventory Data:", data);
                console.log("🟢 Data length:", data.length);

                let tableBody = document.getElementById("inventoryTableBody");

                // ✅ Clear previous rows
                tableBody.innerHTML = "";

                data.forEach((item, index) => {
                    console.log(`🟢 Item ${index + 1}:`, item);

                    let row = document.createElement("tr");

                    // ✅ Correctly handle falsy values
                    let costPrice = (typeof item.averageCostPrice === "number") ? `$${item.averageCostPrice.toFixed(2)}` : '-';
                    let sellingPrice = (typeof item.defaultSellingPrice === "number") ? `$${item.defaultSellingPrice.toFixed(2)}` : '-';

                    row.innerHTML = `
                        <td>\${item.id}</td>
                        <td>\${item.name}</td>
                        <td>\${item.categoryName}</td>
                        <td>\${item.averageCostPrice}</td>
                        <td>\${item.defaultSellingPrice}</td>
                        <td>\${item.stockQuantity}</td>
                    `;

                    tableBody.appendChild(row);
                });

                console.log("🟢 Table Body Element After populating:", tableBody.innerHTML);
            })
            .catch(error => console.error("🔴 Error fetching inventory:", error));
        }




        // ✅ Initialize dashboard
        window.onload = function() {
            let token = getCookie("token");
            if (!token) {
                alert("Unauthorized! Redirecting to login...");
                window.location.href = "login.jsp";
                return;
            }

            let decoded = decodeToken(token);
            if (decoded) {
                setSidebarPermissions(decoded.role);
            }

            fetchInventory();
        };

        document.addEventListener("DOMContentLoaded", function () {
            const decodedToken = getDecodedToken();
            if (!decodedToken) return;

            console.log("User Role:", decodedToken.role);

            const sidebarLinks = document.getElementById("sidebarLinks");

            const sidebarItems = {
                Admin: [

                    { name: "Customers", id: "customersLink", href: "customers.jsp" },
                    { name: "Vendors", id: "vendorsLink", href: "vendors.jsp" },
                    { name: "Make Sales", id: "salesLink", href: "sales.jsp" },
                    { name: "View Sales", id: "salesHistoryLink", href: "salesHistory.jsp" },
                    { name: "Make Purchases", id: "purchasesLink", href: "purchases.jsp" },
                    { name: "View Purchases", id: "purchaseHistoryLink", href: "purchaseHistory.jsp" },
                    { name: "Items", id: "itemsLink", href: "items.jsp" },
                    { name: "Users", id: "usersLink", href: "users.jsp" }

                ],
                Manager: [
                    { name: "Items", id: "itemsLink", href: "items.jsp" },
                    { name: "Customers", id: "customersLink", href: "customers.jsp" },
                    { name: "Vendors", id: "vendorsLink", href: "vendors.jsp" },
                    { name: "Make Sales", id: "salesLink", href: "sales.jsp" },
                    { name: "View Sales", id: "salesHistoryLink", href: "salesHistory.jsp" },
                    { name: "Purchases", id: "purchasesLink", href: "purchases.jsp" }
                ],
                Staff: [

                    { name: "Customers", id: "customersLink", href: "customers.jsp" },
                    { name: "Make Sales", id: "salesLink", href: "sales.jsp" },
                    { name: "View Sales", id: "salesHistoryLink", href: "salesHistory.jsp" }
                ]
            };

            // Clear existing links
            sidebarLinks.innerHTML = "";

            // Generate sidebar links
            if (sidebarItems[decodedToken.role]) {
                sidebarItems[decodedToken.role].forEach(item => {
                    const link = document.createElement("a");
                    link.href = item.href;
                    link.id = item.id;
                    link.textContent = item.name;
                    link.classList.add("sidebar-link");

                    // ✅ Attach event listener to check clicks
                    link.addEventListener("click", function (event) {
                        event.preventDefault(); // Prevent default link behavior
                        console.log(`🟢 ${item.name} clicked`); // ✅ Debug log
                        window.location.href = item.href; // ✅ Navigate correctly
                    });

                    sidebarLinks.appendChild(link);
                });
            } else {
                console.warn("⚠️ Unknown role:", decodedToken.role);
            }

            // ✅ Call setSidebarPermissions AFTER links are generated
            setSidebarPermissions(decodedToken.role);
        });


            // Clear existing links
            sidebarLinks.innerHTML = "";

            // Generate sidebar links
            if (sidebarItems[decodedToken.role]) {
                sidebarItems[decodedToken.role].forEach(item => {
                    const link = document.createElement("a");
                    link.href = item.href;
                    link.id = item.id;
                    link.textContent = item.name;
                    link.classList.add("sidebar-link");

                    // ✅ Attach event listener to check clicks
                    link.addEventListener("click", function (event) {
                        event.preventDefault(); // Prevent default link behavior
                        console.log(`🟢 ${item.name} clicked`); // ✅ Debug log
                        window.location.href = item.href; // ✅ Navigate correctly
                    });

                    sidebarLinks.appendChild(link);
                });
            } else {
                console.warn("⚠️ Unknown role:", decodedToken.role);
            }



        function getDecodedToken() {
            let token = getCookie("token");
            if (!token) {
                alert("Unauthorized! Redirecting to login...");
                window.location.href = "login.jsp";
                return null;
            }
            try {
                return JSON.parse(atob(token.split(".")[1]));
            } catch (error) {
                console.error("Error decoding token:", error);
                return null;
            }
        }

        // ✅ Function to get token from cookies
        function getCookie(name) {
            let cookies = document.cookie.split("; ");
            for (let i = 0; i < cookies.length; i++) {
                let [key, value] = cookies[i].split("=");
                if (key === name) return value;
            }
            return null;
        }

        document.getElementById("logoutBtn").addEventListener("click", function () {
            // ✅ Delete token by setting an expired cookie
            document.cookie = "token=; path=/; expires=Thu, 01 Jan 1970 00:00:00 UTC";

            // ✅ Redirect to login page
            window.location.href = "login.jsp";
        });


    </script>
    <script>
        document.addEventListener("DOMContentLoaded", function () {
            document.getElementById("logoutBtn").addEventListener("click", function () {
                console.log("🔴 Logout button clicked!"); // Debugging log

                // ✅ Delete token by setting an expired cookie
                document.cookie = "token=; path=/; expires=Thu, 01 Jan 1970 00:00:00 UTC";

                // ✅ Redirect to login page
                window.location.href = "login.jsp";
            });
        });
    </script>



</body>
</html>
