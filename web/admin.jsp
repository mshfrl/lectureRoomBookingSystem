<%-- 
    Document   : admin
    Created on : 13 Apr 2026, 12:22:22 am
    Author     : Asus
--%>
<%@page import="java.util.List"%>
<%@page import="com.lab.model.room"%>
<%@page import="com.lab.dao.roomDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Admin Page</title>


        <style>
            :root {
                --primary-color: #7b2cbf;
                --bg-color: #f3eefe;
                --card-bg: #ffffff;
                --text-dark: #333333;
                --text-grey: #6b7280;
                --btn-edit-bg: #f3e8ff;
                --btn-edit-text: #7b2cbf;
                --btn-delete-bg: #fee2e2;
                --btn-delete-text: #dc2626;
            }

            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background-color: var(--bg-color);
                margin: 0;
                padding: 0;
                color: var(--text-dark);
            }

            /* Top Navigation Tabs */
            .tabs-container {
                display: flex;
                background-color: var(--card-bg);
                margin: 20px;
                border-radius: 8px;
                overflow: hidden;
                box-shadow: 0 2px 4px rgba(0,0,0,0.05);
            }

            .tab {
                flex: 1;
                text-align: center;
                padding: 15px 0;
                font-weight: 600;
                cursor: pointer;
                color: var(--text-grey);
                transition: 0.3s;
            }

            .tab.active {
                background-color: var(--primary-color);
                color: white;
            }

            .tab:not(.active):hover {
                background-color: #f9f9f9;
            }

            /* Main Content Container */
            .main-content {
                padding: 0 20px 40px 20px;
                max-width: 1200px;
                margin: 0 auto;
            }

            /* Header Section (Title & Add Button) */
            /* Updated Header Section for Search Bar */
            .header-section {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 20px;
                flex-wrap: wrap;
                gap: 15px;
            }
            .header-section h2 {
                margin: 0;
                font-weight: 500;
            }
            .header-actions {
                display: flex;
                gap: 15px;
                align-items: center;
                flex-wrap: wrap;
            }

            /* Search Bar Styles */
            .search-form {
                display: flex;
                gap: 5px;
            }
            .search-form input {
                padding: 10px 15px;
                border: 1px solid #ddd;
                border-radius: 6px;
                width: 250px;
                outline: none;
            }
            .search-form input:focus {
                border-color: var(--primary-color);
            }
            .btn-search {
                background-color: #4b5563;
                color: white;
                border: none;
                padding: 10px 15px;
                border-radius: 6px;
                cursor: pointer;
                font-weight: 500;
            }
            .btn-search:hover {
                background-color: #374151;
            }
            .btn-clear {
                background-color: #e5e7eb;
                color: #4b5563;
                border: none;
                padding: 10px 15px;
                border-radius: 6px;
                text-decoration: none;
                font-size: 0.9rem;
                display: flex;
                align-items: center;
            }

            .btn-add {
                background-color: var(--primary-color);
                color: white;
                border: none;
                padding: 10px 20px;
                border-radius: 6px;
                font-size: 1rem;
                font-weight: 500;
                cursor: pointer;
                display: flex;
                align-items: center;
                gap: 8px;
                box-shadow: 0 4px 6px rgba(123, 44, 191, 0.2);
            }

            .btn-add:hover {
                background-color: #5a189a;
            }

            /* Grid Layout for Room Cards */
            .cards-grid {
                display: grid;
                grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
                gap: 20px;
            }

            /* Individual Card Styles */
            .card {
                background-color: var(--card-bg);
                border-radius: 10px;
                padding: 20px;
                box-shadow: 0 4px 10px rgba(0,0,0,0.03);
                border: 1px solid #e5e7eb;
            }

            .card h3 {
                color: var(--primary-color);
                margin: 0 0 5px 0;
                font-size: 1.2rem;
                font-weight: 600;
            }

            .card .room-id {
                color: var(--text-grey);
                font-size: 0.85rem;
                margin-bottom: 15px;
            }

            .card .room-info {
                font-size: 0.95rem;
                margin-bottom: 8px;
                color: var(--text-dark);
            }

            .card .room-info span {
                color: var(--text-grey);
            }

            /* Card Action Buttons */
            .card-actions {
                display: flex;
                gap: 10px;
                margin-top: 20px;
            }

            .btn-action {
                flex: 1;
                padding: 10px;
                border: none;
                border-radius: 6px;
                font-weight: 600;
                font-size: 0.95rem;
                cursor: pointer;
                display: flex;
                justify-content: center;
                align-items: center;
                gap: 6px;
                transition: opacity 0.2s;
            }

            .btn-action:hover {
                opacity: 0.8;
            }

            .btn-edit {
                background-color: var(--btn-edit-bg);
                color: var(--btn-edit-text);
            }

            .btn-delete {
                background-color: var(--btn-delete-bg);
                color: var(--btn-delete-text);
            }

            /* Floating Chat Icon (Bottom Right) */
            .chat-btn {
                position: fixed;
                bottom: 30px;
                right: 30px;
                width: 60px;
                height: 60px;
                background-color: var(--primary-color);
                border-radius: 50%;
                display: flex;
                justify-content: center;
                align-items: center;
                color: white;
                box-shadow: 0 4px 10px rgba(123, 44, 191, 0.4);
                cursor: pointer;
            }


            /*styles for modal page*/

            .modal-overlay {
                display: none; /* Hidden by default */
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background-color: rgba(0, 0, 0, 0.5);
                z-index: 1000;
                justify-content: center;
                align-items: center;
            }
            .modal-content {
                background-color: white;
                padding: 30px;
                border-radius: 10px;
                width: 400px;
                max-width: 90%;
                box-shadow: 0 10px 25px rgba(0,0,0,0.2);
                position: relative;
            }
            .modal-content h3 {
                margin-top: 0;
                color: var(--primary-color);
            }
            .close-btn {
                position: absolute;
                top: 15px;
                right: 15px;
                cursor: pointer;
                font-size: 1.5rem;
                color: #aaa;
                border: none;
                background: none;
            }
            .close-btn:hover {
                color: #333;
            }
            .form-group {
                margin-bottom: 15px;
            }
            .form-group label {
                display: block;
                margin-bottom: 5px;
                font-weight: 500;
                font-size: 0.9rem;
            }
            .form-group input, .form-group select {
                width: 100%;
                padding: 10px;
                border: 1px solid #ccc;
                border-radius: 5px;
                box-sizing: border-box;
            }
            .btn-submit {
                width: 100%;
                background-color: var(--primary-color);
                color: white;
                border: none;
                padding: 12px;
                border-radius: 6px;
                font-size: 1rem;
                cursor: pointer;
                margin-top: 10px;
            }
            .btn-danger {
                background-color: #dc2626;
            }


        </style>





    </head>
    <body>
        <!-- Top Navigation Tabs -->
        <div class="tabs-container">
            <div class="tab active">Manage Rooms</div>
            <div class="tab">Review Bookings</div>
            <div class="tab">Feedback</div>
        </div>

        <!-- Main Content Area -->
        <div class="main-content">

            <div class="header-section">
                <h2>Lecture Rooms</h2>


                <div class="header-actions">
                    <!-- Search Form added here -->
                    <form action="admin.jsp" method="GET" class="search-form">
                        <%
                            // Get the search query from the URL if it exists
                            String searchParam = request.getParameter("searchQuery");
                            String displayQuery = (searchParam != null) ? searchParam : "";
                        %>
                        <input type="text" name="searchQuery" placeholder="Search by name, ID or location..." value="<%= displayQuery%>">
                        <button type="submit" class="btn-search">Search</button>

                        <%-- Show a "Clear" button only if the user is currently searching --%>
                        <% if (searchParam != null && !searchParam.trim().isEmpty()) { %>
                        <a href="admin.jsp" class="btn-clear">Clear</a>
                        <% } %>
                    </form>
                    <button class="btn-add" onclick="openModal('addModal')">
                        <!-- Plus Icon -->
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="12" y1="5" x2="12" y2="19"></line><line x1="5" y1="12" x2="19" y2="12"></line></svg>
                        Add New Room
                    </button>
                </div>
            </div>

            <div class="cards-grid">

                <%
                    // Initialize the DAO and get the list of rooms directly from the database
                    roomDAO dao = new roomDAO();
                    //List<room> roomsList = dao.selectAllRooms();
                    List<room> roomsList;

                    if (searchParam != null && !searchParam.trim().isEmpty()) {
                        roomsList = dao.searchRooms(searchParam);
                    } else {
                        roomsList = dao.selectAllRooms();
                    }

                    // Loop through the list and generate HTML cards dynamically
                    if (roomsList != null && !roomsList.isEmpty()) {
                        for (room r : roomsList) {

                %>
                <!-- Single Room Card -->
                <div class="card">
                    <h3><%= r.getRoomName()%></h3>
                    <div class="room-id">ID: <%= r.getRoomID()%></div>

                    <div class="room-info"><span>Location:</span> <%= r.getLocation()%></div>
                    <div class="room-info"><span>Capacity:</span> <%= r.getCapacity()%> people</div>

                    <div class="card-actions">
                        <button class="btn-action btn-edit" onclick="openEditModal('<%= r.getRoomID()%>', '<%= r.getRoomName().replace("'", "\\'")%>', <%= r.getCapacity()%>, '<%= r.getLocation().replace("'", "\\'")%>', <%= r.getAvailability()%>)">
                            <!-- Edit Pencil Icon -->
                            <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polygon points="16 3 21 8 8 21 3 21 3 16 16 3"></polygon></svg>
                            Edit
                        </button>
                        <button class="btn-action btn-delete" onclick="openDeleteModal('<%= r.getRoomID()%>')">
                            <!-- Delete Trash Icon -->
                            <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="3 6 5 6 21 6"></polyline><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path></svg>
                            Delete
                        </button>
                    </div>
                </div>
                <%
                    }
                } else {
                %>
                <p>No rooms found in the database. Please add a room.</p>
                <%
                    }
                %>

            </div>
        </div>


        <!-- 1. ADD ROOM MODAL -->
        <div id="addModal" class="modal-overlay">
            <div class="modal-content">
                <button class="close-btn" onclick="closeModal('addModal')">&times;</button>
                <h3>Add New Room</h3>
                <form action="roomServlet" method="POST">
                    <input type="hidden" name="action" value="add"> <!-- Tells servlet what to do -->

                    <div class="form-group"><label>Room ID (e.g. IBH14)</label>
                        <input type="text" name="roomID" required></div>
                    <div class="form-group"><label>Room Name</label>
                        <input type="text" name="roomName" required></div>
                    <div class="form-group"><label>Capacity</label>
                        <input type="number" name="capacity" required></div>
                    <div class="form-group"><label>Location</label>
                        <input type="text" name="location" required></div>
                    <div class="form-group"><label>Availability</label>
                        <select name="availability">
                            <option value="true">Available (1)</option>
                            <option value="false">Unavailable (0)</option>
                        </select>
                    </div>
                    <button type="submit" class="btn-submit">Save Room</button>
                </form>
            </div>
        </div>

        <!-- 2. EDIT ROOM MODAL -->
        <div id="editModal" class="modal-overlay">
            <div class="modal-content">
                <button class="close-btn" onclick="closeModal('editModal')">&times;</button>
                <h3>Edit Room</h3>
                <form action="roomServlet" method="POST">
                    <input type="hidden" name="action" value="edit">

                    <!-- Readonly ID because it's the Primary Key in database -->
                    <div class="form-group"><label>Room ID</label>
                        <input type="text" name="roomID" id="edit-id" readonly style="background:#eee;"></div>
                    <div class="form-group"><label>Room Name</label>
                        <input type="text" name="roomName" id="edit-name" required></div>
                    <div class="form-group"><label>Capacity</label>
                        <input type="number" name="capacity" id="edit-capacity" required></div>
                    <div class="form-group"><label>Location</label>
                        <input type="text" name="location" id="edit-location" required></div>
                    <div class="form-group"><label>Availability</label>
                        <select name="availability" id="edit-availability">
                            <option value="true">Available (1)</option>
                            <option value="false">Unavailable (0)</option>
                        </select>
                    </div>
                    <button type="submit" class="btn-submit">Update Room</button>
                </form>
            </div>
        </div>

        <!-- 3. DELETE CONFIRMATION MODAL -->
        <div id="deleteModal" class="modal-overlay">
            <div class="modal-content" style="text-align: center;">
                <button class="close-btn" onclick="closeModal('deleteModal')">&times;</button>
                <h3 style="color: #dc2626;">Confirm Delete</h3>
                <p>Are you sure you want to delete room <strong id="delete-id-display"></strong>?</p>
                <p style="font-size: 0.85rem; color: #666;">This action cannot be undone.</p>

                <form action="roomServlet" method="POST">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="roomID" id="delete-id">
                    <button type="submit" class="btn-submit btn-danger">Yes, Delete Room</button>
                    <button type="button" class="btn-submit" style="background:#ddd; color:#333;" onclick="closeModal('deleteModal')">Cancel</button>
                </form>
            </div>
        </div>


        <script>
            // Open standard modal
            function openModal(modalId) {
                document.getElementById(modalId).style.display = 'flex';
            }

            // Close modal
            function closeModal(modalId) {
                document.getElementById(modalId).style.display = 'none';
            }

            // Open Edit Modal and auto-fill data
            function openEditModal(id, name, capacity, location, avail) {
                document.getElementById('edit-id').value = id;
                document.getElementById('edit-name').value = name;
                document.getElementById('edit-capacity').value = capacity;
                document.getElementById('edit-location').value = location;
                document.getElementById('edit-availability').value = avail.toString();
                openModal('editModal');
            }

            // Open Delete Modal and set the ID
            function openDeleteModal(id) {
                document.getElementById('delete-id').value = id;
                document.getElementById('delete-id-display').innerText = id;
                openModal('deleteModal');
            }
        </script>
    </body>
</html>
