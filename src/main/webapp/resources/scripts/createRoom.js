var roomName = "";
var featuresContainer = new Array();
var start = "";
var end = "";

$(document).ready(function() {
	manageDateTimePicker();
	$("#submit-room-btn").click(submitRoom);
	makeDragDrop();
});

function manageDateTimePicker() {
	$('.datetimepicker').datetimepicker({
		minDate : 0
	});
}

function makeDragDrop() {
	$(".drag>img").draggable({
		revert : true,
		zIndex : 1000
	});
	$(".drop").droppable({
		drop : setDrop
	});
}

function setDrop(event, ui) {
	var draggable = ui.draggable;
	var id = ui.draggable.attr("id");
	$("#" + id).css("opacity", "0.3")
	featuresContainer.push(id);
}

function submitRoom() {
	roomName = $("#room-name").val();
	var start = $("#start_time").val() || "";
	var end = $("#end_time").val() || "";
	console.log(roomName);
	if (start != "" && end != "" && roomName != "") {
		var startTotal = start + ":00";
		var endTotal = end + ":00";
		console.log(startTotal + " ---- " + endTotal);
		var poll = false;
		var question = false;

		for (var i = 0; i < featuresContainer.length; i++) {
			switch (featuresContainer[i]) {
			case "polls":
				poll = true;
				break;
			case "questions":
				question = true;
				break;
			default:
				break;
			}
		}

		var formData = {
			start : startTotal,
			end : endTotal,
			isPoll : poll,
			isQuestion : question

		}; // Array

		createRoomAjax(formData)
	}
	cleanForm();
}

function createRoomAjax(formData) {
	$.ajaxSetup({
		cache : false
	});
	$.ajax({
		url : '/ClassPackage/api/room/createRoom/' + roomName,
		type : "GET",
		data : formData,
		dataType : 'json',
		success : function(data, jqXHR) {
			if (data['status'] == 'ok') {
				console.log("room:: " + roomName
						+ " was inserted! room number is:: "
						+ data.data.roomNumber);
				localStorage.currroom = data.data.roomNumber;

				$.mobile.loading('show', {
					theme : "a",
					text : "room was added",
					textonly : true,
					textVisible : true
				});
				// hide after delay
				setTimeout(function() {
					$.mobile.loading('hide');
				}, 2000);

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
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log(errorThrown);
		}
	});

};

function cleanForm() {
	$("#create-room-details").find("input").val("");
	$('img').show();
	$("#features").text("");
}

$(function manageMyRooms() {
	getAllMyRoomsAjax();
	setInterval(function() {
		if (page == "lecturer") {
			getAllMyRoomsAjax();
		}
	}, 60000)
});

function getAllMyRoomsAjax() {
	$.ajaxSetup({
		cache : false
	});

	$.ajax({
		url : '/ClassPackage/api/room/myRooms',
		type : "GET",
		dataType : 'json',
		success : function(data, jqXHR) {
			if (data['status'] == 'ok') {
				console.log("rooms size: " + data.data.length);
				updateRoomsListCreateRoom(data.data);
				updateRoomsListMyRooms(data.data);
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log(errorThrown);
		}
	});
}

function updateRoomsListCreateRoom(list) {
	$("#myRooms").empty();
	$("#myActiveRoom").empty();
	for (var i = 0; i < list.length; i++) {
		var diff = calculateDiffTime(list[i].startDate);
		if (Number(diff) > 0) {
			$(
					"<li id='room-" + i + "'><b>" + list[i].roomName + "</b>"
							+ " | Number: " + list[i].roomNumber
							+ "|<br>  Starts in " + diff + " minutes"
							+ "</li><br>").appendTo("#myRooms");

			if (Number(diff) <= 5) {
				$("#room-" + i).css("color", "greenyellow");
			}
		} else {
			$(
					"<li><b>" + list[i].roomName + "</b> | Number: "
							+ list[i].roomNumber + "</li>").appendTo(
					"#myActiveRoom")
		}

	}
}

function updateRoomsListMyRooms(list) {
	$("#existingRooms").empty();
	$("#activateRooms").empty();
/*
 * <div class="listing-block">
		<div class="pic"></div>
		<div class="name">
			<span class="caps">David, 21</span>
		</div>
	</div>
 */
	for (var i = 0; i < list.length; i++) {
		var diff = calculateDiffTime(list[i].startDate);
		if (Number(diff) > 5) {
			$("<div class='listing-block' id='myroom-" + list[i].roomName+ "'>" +
					"<div class='pic wait'><img src='resources/images/wait.png'></div><div class='name'>" +
					"<span><b>" + list[i].roomName+"</b>" + 
					" | Number: " + list[i].roomNumber+ " | Password: " + list[i].password.toString()
							+ "</span></div><div>").appendTo("#existingRooms");

			$("#myroom-" + list[i].roomName).click(function() {
				localStorage.currroom = $(this).text().match(/[0-9]{4}/g)[0];
				window.location = "#editRoom"
			});

		} else {
			$("<div class='listing-block' id='myActiveroom-" + list[i].roomName+ "'>" +
					"<div class='pic active'><img src='resources/images/active.png'></div><div class='name'>" +
					"<span><b>" + list[i].roomName+"</b>" + 
					" | Number: " + list[i].roomNumber+ " | Password: " + list[i].password.toString()
							+ "</span></div><div>").appendTo("#activateRooms");

			$("#myActiveroom-" + list[i].roomName).click(function() {
				localStorage.currroom = $(this).text().match(/[0-9]{4}/g)[0];
				joinRoomLecturer();
				window.location = "#activeroom"
			});

		}
	}

	$("#existingRooms").trigger("create");
	$("#existingRooms").listview("refresh");
	$("#activateRooms").trigger("create");
	$("#activateRooms").listview("refresh");

}

function joinRoomLecturer() {
	$.ajaxSetup({
		cache : false
	});

	$.ajax({
		url : '/ClassPackage/api/room/joinRoomLecturer/'
				+ localStorage.getItem("currroom"),
		type : "GET",
		dataType : 'json',
		success : function(data) {
			if (data['status'] == 'ok') {
				console.log("room session updated");
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log(errorThrown);
		}
	});
}