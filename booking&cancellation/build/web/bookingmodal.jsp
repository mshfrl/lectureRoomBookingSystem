<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<div id="bookingModal" class="booking-modal-overlay">
    
    <div class="booking-modal-box">
        
        <div class="selected-room-banner">
            <strong>Target Room:</strong> <span id="displayRoomName"></span>
        </div>

        <form action="StudentServlet" method="POST" class="modal-form-layout">
            
            <input type="hidden" name="roomId" id="modalRoomId">
            
            <div class="form-group">
                <label>Location</label>
                <input type="text" id="displayRoomLocation" readonly style="background-color: #f3f4f6; color: #6b7280; cursor: not-allowed;">
            </div>

            <div class="form-group">
                <label for="bookingDate">Date</label>
                <input type="date" name="date" id="bookingDate" required>
            </div>

            <div class="split-columns">
                <div class="form-group">
                    <label for="startTime">Start Time</label>
                    <input type="time" name="startTime" id="startTime" required>
                </div>
                <div class="form-group">
                    <label for="endTime">End Time</label>
                    <input type="time" name="endTime" id="endTime" required>
                </div>
            </div>

            <div class="form-group">
                <label for="supportingLecturer">Supporting Lecturer</label>
                <select name="supportingLecturer" id="supportingLecturer" required>
                    <option value="">-- Select a lecturer --</option>
                    <c:forEach var="lecturer" items="${lecturerList}">
                        <option value="${lecturer.username}">${lecturer.fullName}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label for="purpose">Purpose of Booking</label>
                <input type="text" name="purpose" id="purpose" placeholder="e.g., Operating Systems Lab Revision" required>
            </div>

            <div class="modal-footer-actions">
                <button type="button" class="btn-modal-cancel" onclick="closeBookingModal()">Cancel</button>
                <button type="submit" class="btn-modal-submit">Submit Request</button>
            </div>
        </form>
    </div>
</div>

<script>
function openBookingModal(buttonElement) {
    if (!buttonElement) return;

    // Safely extract HTML data attributes from the card button clicked
    var roomId = buttonElement.getAttribute('data-id');
    var roomName = buttonElement.getAttribute('data-name');
    var roomLocation = buttonElement.getAttribute('data-location');

    // Bind data straight into modal form fields
    document.getElementById('displayRoomName').innerText = roomName || 'Room';
    document.getElementById('displayRoomLocation').value = roomLocation || '';
    document.getElementById('modalRoomId').value = roomId || '';

    // Switch display style to flex to show it cleanly over the screen layout
    document.getElementById('bookingModal').style.display = 'flex';
}

function closeBookingModal() {
    document.getElementById('bookingModal').style.display = 'none';
}

// Close gracefully if the user clicks out on the blurred glass background wrapper
window.onclick = function(event) {
    var modal = document.getElementById('bookingModal');
    if (event.target === modal) {
        modal.style.display = 'none';
    }
}
</script>