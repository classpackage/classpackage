var startEdit = "";
var endEdit = "";

$(document).ready(function() {
	$(".edit-input").hide();
	$("#edit-room-btn").click(updateFromInputs);
	manageEditTabs();
	manageEditsRooms();
	manageDateTimePicker();
});

function manageEditTabs() {
	$('#edittabs ul li a').on('click', function(e) {
		e.preventDefault();
		var newcontent = $(this).attr('href');

		$('#edittabs ul li a').removeClass('sel');
		$(this).addClass('sel');

		$('section').each(function() {
			if (!$(this).hasClass('hidden')) {
				$(this).addClass('hidden');
			}
		});

		$(newcontent).removeClass('hidden');
	});
}

function manageEditsRooms() {
	$("#edit-img-roomName").click(function() {
		if (!$("#roomName-input-edit").is(":visible")) {
			$("#roomName-input-edit").show();
			$("#edit-room-btn").show();
		} else {
			$("#roomName-input-edit").hide();
			if (!$(".start").is(":visible") && !$(".end").is(":visible")) {
				$("#edit-room-btn").hide();
				$("input").val("");
			}
		}
	});
	$("#edit-img-roomStartTime").click(
			function() {
				if (!$(".start").is(":visible")) {
					$(".start").show();
					$("#edit-room-btn").show();
				} else {
					$(".start").hide();
					if (!$(".end").is(":visible")
							&& !$("#roomName-input-edit").is(":visible")) {
						$("#edit-room-btn").hide();
						$("input").val("");
					}
				}
			});

	$("#edit-img-roomEndTime").click(
			function() {
				if (!$(".end").is(":visible")) {
					$(".end").show();
					$("#edit-room-btn").show();
				} else {
					$(".end").hide();
					if (!$(".start").is(":visible")
							&& !$("#roomName-input-edit").is(":visible")) {
						$("input").val("");
						$("#edit-room-btn").hide();
					}
				}
			});
};

$(function manageRoomDetails() {
	$.ajaxSetup({
		cache : false
	});

	$.ajax({
		url : '/ClassPackage/api/room/' + localStorage.getItem("currroom"),
		type : "GET",
		dataType : 'json',
		success : function(data, jqXHR) {
			if (data['status'] == 'ok') {
				$("#roomName-input").text(data.data.roomName);
				$("#roomStartTime-input").text(
						organizeDate(data.data.startDate));
				$("#roomEndTime-input").text(organizeDate(data.data.endDate));
			} else {
				console.log(data.data);
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log(errorThrown);
		}
	});

});

function updateFromInputs() {
	var name = $("#roomName-edit").val() || "";
	var startTime = $("#edit-start-time").val() || "";
	var endTime = $("#edit-end-time").val() || "";
	var start = "";
	var end = "";
	// $k$ check date validity end>start
	if (startTime != "") {
		start = startTime + ":00"
	}

	if (endEdit != "") {
		end = endTime + ":00"
	}

	var formData = {
		name : name,
		start : start,
		end : end
	}; // Array

	updateSettingsAjax(formData);

}

function updateSettingsAjax(formData) {
	$.ajaxSetup({
		cache : false
	});

	$.ajax({
		url : '/ClassPackage/api/room/updateRoomSetting/'
				+ localStorage.getItem("currroom"),
		type : "GET",
		data : formData,
		dataType : 'json',
		success : function(data, jqXHR) {
			if (data['status'] == 'ok') {
				console.log("room:: " + roomName
						+ " was updated! room number is:: "
						+ data.data.roomNumber);
				$.mobile.loading('show', {
					theme : "a",
					text : "room was updated",
					textonly : true,
					textVisible : true
				});
				// hide after delay
				setTimeout(function() {
					$.mobile.loading('hide');
				}, 2000);

				manageRoomDetails();
				cleanInputs();

			} else {
				$.mobile.loading('show', {
					theme : "a",
					text : data.data,
					textonly : true,
					textVisible : true
				});
				// hide after delay
				setTimeout(function() {
					$.mobile.loading('hide');
				}, 2000);

				cleanInputs();
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log(errorThrown);
			cleanInputs();
		}

	});

};


function cleanInputs() {
	$("input").val("");
	$(".edit-input").hide();
	$("#edit-room-btn").hide();
}
