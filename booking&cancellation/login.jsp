<%-- 
    Document   : login
    Created on : 12 Apr 2026, 11:17:20 pm
    Author     : Asus
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login Page</title>
        <link rel="stylesheet" href="css/style.css">


        



    </head>

    <body>
        <div class="login-card">
            <div class="header">
                <h1>UMT Lecture Room
                    <br>
                    Booking System</h1>
            </div>
            <form action="LoginServlet" method="POST">

                <%
                    String error = (String) request.getAttribute("errorMessage");
                    if (error != null) {
                %>
                <p class="error-message" style="color: red; text-align: center; margin-bottom: 15px;"><b><%= error%></b></p>
                        <%
                            }
                        %>


                <div class="input-group">
                    <label for="username">User ID: </label>    
                    <input type="text" name="username" placeholder="Enter Username" required>

                    <label for="password">Password: </label>
                    <input type="password" name="password" placeholder="Enter Password" required>
                </div>
                <button type="submit" class="login-btn">Login</button>
            </form>

            <div class="demo-box" style="color: #5a189a; font-weight: bold">
                Demo Accounts:
                <br>
                Student: student / stud123
                <br>
                Lecturer: lecturer / lect123
                <br>
                Admin: admin / admin123
                <br>
            </div>
        </div>
    </body>
</html>
