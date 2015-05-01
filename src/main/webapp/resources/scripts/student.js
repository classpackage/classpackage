$(document).ready(function() {
		$("#stud-join-Room-Btn").click(joinRoomAjax);
});

function joinRoomAjax() {
	$("#joinRoomForm").validate(
			{
				errorPlacement : function(error, element) {
					error.insertAfter(element);
				},
				submitHandler : function(form) { // Show loading indicator
					$.mobile.loading("show"); // join post request
					$.post("/ClassPackage/api/room/joinRoom",
							$('#joinRoomForm').serialize(), function(data) {
								localStorage.currroom = $("#join-room-number")
										.val();
								window.location = "#activeroom";
							});
				}
			})
};
