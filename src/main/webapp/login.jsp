<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
     <meta name="csrf-token" content="">
    <title>Login</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: linear-gradient(to right, #007bff, #00d4ff);
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        .login-container {
            background: white;
            padding: 35px;
            padding-right : 45px;
            border-radius: 12px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
            width: 350px;
            text-align: center;
            animation: fadeIn 0.5s ease-in-out;
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(-10px); }
            to { opacity: 1; transform: translateY(0); }
        }

        .login-container h2 {
            margin-bottom: 20px;
            color: #333;
        }

        .input-group {
            margin-bottom: 15px;
            text-align: left;
        }

        .input-group label {
            display: block;
            font-weight: bold;
            margin-bottom: 5px;
            color: #555;
        }

        .input-group input {
            width: 100%;
            padding: 12px;
            border: 1px solid #ccc;
            border-radius: 6px;
            font-size: 14px;
            transition: border-color 0.3s ease-in-out;
        }

        .input-group input:focus {
            border-color: #007bff;
            outline: none;
            box-shadow: 0 0 5px rgba(0, 123, 255, 0.5);
        }

        .btn {
            background: #007bff;
            color: white;
            border: none;
            padding: 12px;
            width: 100%;
            border-radius: 6px;
            font-size: 16px;
            cursor: pointer;
            transition: background 0.3s;
        }

        .btn:hover {
            background: #0056b3;
        }

        .register-link {
            margin-top: 10px;
            font-size: 14px;
        }

        .register-link a {
            color: #007bff;
            text-decoration: none;
            font-weight: bold;
        }

        .register-link a:hover {
            text-decoration: underline;
        }

        .error-message {
            color: red;
            margin-top: 10px;
        }
    </style>


        <script>
            function fetchCsrfToken() {
                    fetch("http://localhost:8080/webapi/auth/csrf", {
                        method: "GET",
                        credentials: "include"
                    })
                    .then(response => response.json())
                    .then(data => {
                        console.log("CSRF Token received:", data.csrfToken);
                        document.querySelector("meta[name='csrf-token']").setAttribute("content", data.csrfToken);
                    })
                    .catch(error => console.error("🔴 CSRF Token Fetch Error:", error));
            }

            window.onload = function() {
            fetchCsrfToken();
                document.getElementById("loginForm").addEventListener("submit", function(event) {
                            event.preventDefault();

                            let email = document.querySelector("input[name='email']").value;
                            let password = document.querySelector("input[name='password']").value;
                            let csrfTokenMeta = document.querySelector("meta[name='csrf-token']");
                            let csrfToken = csrfTokenMeta ? csrfTokenMeta.getAttribute("content") : "";



                            fetch("http://localhost:8080/webapi/auth/login", {
                                method: "POST",
                                headers: {
                                    "Content-Type": "application/json",
                                    "X-CSRF-TOKEN": csrfToken  // ✅ Send CSRF token
                                },
                                body: JSON.stringify({ email: email, password: password }),
                                credentials: "include"
                            })
                            .then(response => {
                                console.log("🔵 Login Response Headers:", response.headers);
                                     return response.json();
                            })
                            .then(data => {
                                console.log("🔵 Login Response Body:", data);
                                if (data.message === "Login successful") {
                                    alert("Login successful!");
                                    window.location.href = "/dashboard.jsp";

                                } else {
                                    document.getElementById("error-msg").textContent = "Invalid email or password.";
                                }
                            })
                            .catch(error => console.error("🔴 Error:", error));
                        });
            };

            // ✅ Extract CSRF Token from Cookie
            function getCsrfToken() {
                let cookies = document.cookie.split("; ");
                for (let i = 0; i < cookies.length; i++) {
                    let [key, value] = cookies[i].split("=");
                    if (key === "XSRF-TOKEN") return value;
                }
                return null;
            }
        </script>


</head>
<body>
    <div class="login-container">
        <h2>Login</h2>
        <form id="loginForm" method="POST">
            <div class="input-group">
                <label>Email:</label>
                <input type="text" name="email" required>
            </div>
            <div class="input-group">
                <label>Password:</label>
                <input type="password" name="password" required>
            </div>
            <button type="submit" class="btn">Login</button>
            <p id="error-msg" class="error-message"></p>
        </form>
        <p class="register-link">Don't have an account? <a href="register.jsp">Register</a></p>
    </div>
</body>
</html>
