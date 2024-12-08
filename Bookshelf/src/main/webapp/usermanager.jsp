<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, com.bookshelf.dtos.BookDto" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bookshelf | Dashboard</title>
    <link rel="stylesheet" href="css/dashboard.css">

</head>
<body class="background">

    <!-- Navbar -->
    <header class="navbar">
        <h1 class="navbar-title">Bookshelf</h1>
        
         <nav>
            <a href="adminDashboard.jsp" class="nav-link">Home</a>
            <a href="UserManager" class="nav-link">Users</a>
            <a href="books.jsp" class="nav-link">Libraries</a>
            <a href="books.jsp" class="nav-link">Reservations</a>
            <a href="books.jsp" class="nav-link">Books</a>
            <a href="index.jsp" class="nav-link">Logout</a>
         </nav>
 
    </header>

 
        <h2>User Manager</h2>
        
       <!-- Display if edit button has clicked -->
        <c:if test="${not empty error}">
        <p class="error">${error}</p>
        </c:if>


         <table >
         <!-- manage button -->
         
           <thead>
             <tr>

                <th>Username</th>
                <th>Email</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Address</th>
                <th>Role</th>
                <th>Status</th>
                <th>Reservations</th>
                <th></th>

             </tr>
           </thead>
         <tbody>
         
         
            <c:forEach var="userManagerDto" items="${userManagerDtos}">

                <tr>
                    <td>${userManagerDto.user.username}</td>
                    <td>${userManagerDto.user.email}</td>
                    <td>${userManagerDto.user.first_name}</td>
                    <td>${userManagerDto.user.last_name}</td>
                    <td>${userManagerDto.address.address}, ${userManagerDto.address.city}, ${userManagerDto.address.province}, ${userManagerDto.address.country}, ${userManagerDto.address.postal_code}</td>
                    <td>${userManagerDto.userRole.role_name}</td>
                    <td>${userManagerDto.userRole.status}</td>
                    <td>${userManagerDto.totalReservation}</td>
                    <td>   
                       <form action="UserManager" method="POST"> 

                         <button type="submit" name="edit" id="edit"  value="${userManagerDto.user.user_id}">EDIT</button>   
                         <button type="submit" name="delete" id="delete"  value="${userManagerDto.user.user_id}">DELETE</button>  
                       </form>
                    </td>   
                </tr>
            </c:forEach>
         </tbody>
         </form >
       </table>
 

    

    <!-- Footer -->
    <footer class="footer">
        <p>&copy; 2024 Bookshelf Application</p>
        <p>Created by the Bookshelf Development Team</p>
    </footer>

</body>
</html>