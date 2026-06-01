<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Lecturer Dashboard - Lecture Room Portal</title>
        <link rel="stylesheet" type="text/css" href="style(partAdriana).css">
        <style>
            /* Lecturer Specific Actions Styling */
            .btn-approve {
                background-color: #10b981;
                color: white;
                border: none;
                padding: 10px 16px;
                border-radius: 6px;
                cursor: pointer;
                font-weight: bold;
                flex: 1;
            }
            .btn-reject {
                background-color: #ef4444;
                color: white;
                border: none;
                padding: 10px 16px;
                border-radius: 6px;
                cursor: pointer;
                font-weight: bold;
                flex: 1;
            }
            .btn-approve:hover {
                background-color: #059669;
            }
            .btn-reject:hover {
                background-color: #dc2626;
            }
            .action-buttons-flex {
                display: flex;
                gap: 12px;
                margin-top: 16px;
                width: 100%;
            }

            /* Custom Modal Layout for Lecturer Dashboard */
            .lecturer-modal {
                display: none;
                position: fixed;
                z-index: 1000;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
                background-color: rgba(0,0,0,0.5);
                align-items: center;
                justify-content: center;
            }
            .lecturer-modal-content {
                background-color: #ffffff;
                padding: 24px;
                border-radius: 12px;
                width: 100%;
                max-width: 450px;
                box-shadow: 0 4px 20px rgba(0,0,0,0.15);
                box-sizing: border-box;
            }
            .modal-form-group {
                margin-bottom: 16px;
                display: flex;
                flex-direction: column;
            }
            .modal-form-group label {
                font-size: 14px;
                font-weight: 600;
                color: #334155;
                margin-bottom: 6px;
            }
            .modal-form-group input, .modal-form-group textarea {
                padding: 10px;
                border: 1px solid #cbd5e1;
                border-radius: 6px;
                font-size: 14px;
                width: 100%;
                box-sizing: border-box;
            }
            .modal-time-row {
                display: flex;
                gap: 12px;
            }
            .modal-time-row .modal-form-group {
                flex: 1;
            }
            .modal-header-title {
                font-size: 18px;
                font-weight: 700;
                color: #1e293b;
                margin-top: 0;
                margin-bottom: 16px;
            }
            .modal-btn-container {
                display: flex;
                gap: 12px;
                margin-top: 20px;
            }
            .modal-submit-btn {
                background-color: #6366f1;
                color: white;
                border: none;
                padding: 12px;
                border-radius: 6px;
                cursor: pointer;
                font-weight: bold;
                flex: 1;
            }
            .modal-cancel-btn {
                background-color: #e2e8f0;
                color: #475569;
                border: none;
                padding: 12px;
                border-radius: 6px;
                cursor: pointer;
                font-weight: bold;
                flex: 1;
            }
        </style>
    </head>
    <body>

        <div class="portal-header-bar">
            <div class="portal-brand-block">
                <h1 class="portal-title">Lecturer Portal</h1>
                <span class="portal-user-name">
                    <c:choose>
                        <c:when test="${not empty sessionScope.full_name}">
                            <c:out value="${sessionScope.full_name}" />
                        </c:when>
                        <c:otherwise>
                            Dr. Khairul Anuar
                        </c:otherwise>
                    </c:choose>
                </span>
            </div>
            <a href="LogoutServlet" class="logout-link" style="color: #ef4444; text-decoration: none; font-weight: 600; font-size: 14px;">
                <span>Logout</span> &#x2794;
            </a>
        </div>

        <div class="dashboard-workspace">

            <div class="section-header-row">
                <h3 class="section-heading">Pending Student Requests</h3>
            </div>

            <div class="bookings-vertical-stack" style="margin-bottom: 40px;">
                <c:forEach var="req" items="${listPendingRequests}">
                    <div class="booking-card-row">
                        <div class="booking-card-header-flex">
                            <div>
                                <h4 class="booking-room-name">${req.roomName}</h4>
                                <p class="booking-room-location">${req.location}</p>
                            </div>
                            <span class="status-badge-pending" style="background: #fef08a; color: #854d0e;">Awaiting Your Approval</span>
                        </div>

                        <div class="booking-card-schedule-flex">
                            <div class="schedule-meta-item"><span>🧑‍🎓</span> Student: <strong>${req.studentName}</strong></div>
                            <div class="schedule-meta-item"><span>📅</span> ${req.bookingDate}</div>
                            <div class="schedule-meta-item"><span>🕒</span> ${req.bookingTime}</div>
                        </div>

                        <div class="booking-card-details-divider">
                            <div><strong>Purpose:</strong> ${req.purpose}</div>
                        </div>

                        <div class="action-buttons-flex">
                            <form action="LecturerServlet" method="POST" style="flex: 1; display: flex;">
                                <input type="hidden" name="studentUsername" value="${req.studentName}">
                                <input type="hidden" name="roomName" value="${req.roomName}">
                                <input type="hidden" name="bookingDate" value="${req.bookingDate}">
                                <input type="hidden" name="bookingTime" value="${req.bookingTime}">
                                <button type="submit" name="action" value="approve" class="btn-approve">
                                    &#x2714; Approve & Forward to Admin
                                </button>
                            </form>
                            
                            <form action="LecturerServlet" method="POST" style="flex: 1; display: flex;">
                                <input type="hidden" name="studentUsername" value="${req.studentName}">
                                <input type="hidden" name="roomName" value="${req.roomName}">
                                <input type="hidden" name="bookingDate" value="${req.bookingDate}">
                                <input type="hidden" name="bookingTime" value="${req.bookingTime}">
                                <button type="submit" name="action" value="reject" class="btn-reject">
                                    &#x2A2F; Reject Request
                                </button>
                            </form>
                        </div>
                    </div>
                </c:forEach>

                <c:if test="${empty listPendingRequests}">
                    <div style="text-align: center; color: #94a3b8; padding: 32px 0; font-size: 14px; background: #ffffff; border: 1px dashed #e2e8f0; border-radius: 8px;">
                        No pending student requests at the moment.
                    </div>
                </c:if>
            </div>

            <div class="section-header-row">
                <h3 class="section-heading">Available Lecture Rooms</h3>
            </div>

            <div class="rooms-grid-layout" style="margin-bottom: 40px;">
                <c:forEach var="room" items="${listAvailableRooms}">
                    <div class="room-card-node">
                        <div>
                            <div class="room-card-title">${room.roomName}</div>
                            <div class="room-card-meta-id">ID: ${room.roomId}</div>
                            <div class="room-card-info-line"><span>📍</span> ${room.location}</div>
                            <div class="room-card-info-line"><span>👥</span> Capacity: ${room.capacity} people</div>
                        </div>
                        <button type="button" class="book-room-action-btn" 
                                data-id="${room.roomId}" 
                                data-name="${room.roomName}"
                                onclick="openLecturerModal(this)">
                            + Book Room
                        </button>
                    </div>
                </c:forEach>
            </div>

            <div class="section-header-row">
                <h3 class="section-heading">My Bookings</h3>
            </div>

            <div class="bookings-vertical-stack">
                <c:forEach var="booking" items="${listLecturerBookings}">
                    <div class="booking-card-row">
                        <div class="booking-card-header-flex">
                            <div>
                                <h4 class="booking-room-name">${booking.roomName}</h4>
                                <p class="booking-room-location">${booking.location}</p>
                            </div>
                            <span class="status-badge-pending" style="background: #e0f2fe; color: #0369a1;">${booking.status}</span>
                        </div>

                        <div class="booking-card-schedule-flex">
                            <div class="schedule-meta-item"><span>📅</span> ${booking.bookingDate}</div>
                            <div class="schedule-meta-item"><span>🕒</span> ${booking.bookingTime}</div>
                        </div>

                        <div class="booking-card-details-divider">
                            <div><strong>Purpose:</strong> ${booking.purpose}</div>
                        </div>
                    </div>
                </c:forEach>

                <c:if test="${empty listLecturerBookings}">
                    <div style="text-align: center; color: #94a3b8; padding: 32px 0; font-size: 14px; background: #ffffff; border: 1px dashed #e2e8f0; border-radius: 8px;">
                        You have not made any room bookings yet.
                    </div>
                </c:if>
            </div>

        </div>

        <div id="lecturerBookingModal" class="lecturer-modal">
            <div class="lecturer-modal-content">
                <h3 class="modal-header-title">Book Lecture Room</h3>

                <form action="LecturerServlet" method="POST">
                    <input type="hidden" name="roomId" id="modalRoomId">

                    <div class="modal-form-group">
                        <label>Selected Room</label>
                        <input type="text" id="modalRoomName" readonly style="background: #f1f5f9; font-weight: bold; color: #475569;">
                    </div>

                    <div class="modal-form-group">
                        <label for="bookingDate">Date</label>
                        <input type="date" name="date" id="bookingDate" required>
                    </div>

                    <div class="modal-time-row">
                        <div class="modal-form-group">
                            <label for="startTime">Start Time</label>
                            <input type="time" name="startTime" id="startTime" required>
                        </div>
                        <div class="modal-form-group">
                            <label for="endTime">End Time</label>
                            <input type="time" name="endTime" id="endTime" required>
                        </div>
                    </div>

                    <div class="modal-form-group">
                        <label for="purpose">Booking Purpose</label>
                        <textarea name="purpose" id="purpose" rows="3" placeholder="e.g., Replacement Class, Exam Review Session..." required></textarea>
                    </div>

                    <div class="modal-btn-container">
                        <button type="button" class="modal-cancel-btn" onclick="closeLecturerModal()">Cancel</button>
                        <button type="submit" class="modal-submit-btn">Submit Request</button>
                    </div>
                </form>
            </div>
        </div>

        <script>
            function openLecturerModal(button) {
                const roomId = button.getAttribute('data-id');
                const roomName = button.getAttribute('data-name');

                document.getElementById('modalRoomId').value = roomId;
                document.getElementById('modalRoomName').value = roomName;

                document.getElementById('lecturerBookingModal').style.display = 'flex';
            }

            function closeLecturerModal() {
                document.getElementById('lecturerBookingModal').style.display = 'none';
            }

            window.onclick = function (event) {
                const modal = document.getElementById('lecturerBookingModal');
                if (event.target == modal) {
                    modal.style.display = "none";
                }
            }
        </script>

    </body>
</html>