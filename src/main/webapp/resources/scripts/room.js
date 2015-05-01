var pollsFeature = false;
var questionsFeature = false;
var linksFeature = false;
var alarmFeature = false;
var roomName = "";

$(document).ready(function() {
	getRoomDetails();
	if (localStorage.getItem("isLecturer") == "true") {
		splitScreens(localStorage.getItem("featurecounter"));
		createTeacherRating();
	} else {
		$("#studRate").removeClass("hidden").live('change', function() {
			updateStudentRate($("#stud-rate-slider").slider().val());
		});
	}
});

function getRoomDetails() {
	$.ajaxSetup({
		cache : false
	});

	$.ajax({
		url : '/ClassPackage/api/room/' + localStorage.getItem("currroom"),
		type : "GET",
		dataType : 'json',
		success : function(data, jqXHR) {
			if (data['status'] == 'ok') {
				var counter = 1;
				roomName = data.data.roomName;
				pollsFeature = data.data.isPolls;
				questionsFeature = data.data.isQuestions;
				$("#active-room-name").text("room: " + roomName);
				localStorage.currroom = data.data.roomNumber;

				counter = pollsFeature ? counter + 1 : counter;
				counter = questionsFeature ? counter + 1 : counter;
				localStorage.featurecounter = counter;
			} else {
				console.log(data.data);
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log(errorThrown);
		}
	});
};

function splitScreens(numFeatures) {
	if (numFeatures == 1) {
		$(
				"<div id='feature-1' style='width: 100% ; height: 100% ; position:fixed ; top:10% ; left:0;'>here teacher rate</div>"
						+ "<div id='feature-2' style='width: 50% ; height: 1000% ; background: #AAA; position:fixed ; top:0; left:50%;'>bbb</div>")
				.appendTo("#room-content");
	}

	else if (numFeatures == 2) {
		$(
				"<div id='feature-1' style='width: 50% ; height: 100% ; position:fixed ; top:30% ; left:0;'><div id='chartContainer' style='height: 300px; width: 100%;'></div>"
						+ "<div id='feature-2' style='width: 50% ; height: 100% ; background: #AAA; position:fixed ; top:0; left:50%;'>bbb</div>")
				.appendTo("#room-content");
	}

	else if (numFeatures == 3) {
		$(
				"<div id='feature-1' style='width: 50% ;height: 60%; position:fixed;top:10%; left:0;'>here teacher rate</div>"
						+ "<div id='feature-2' style='width: 50% ;height: 60%; background: #AAA; position:fixed; top:0; left:50%;'>bbb</div>"
						+ "<div id='feature-3' style='width: 100% ;height: 50%; background: #777; position:fixed; top:60%; left:0'>xxx</div>")
				.appendTo("#room-content");
	} else if (numFeatures == 4) {
		$(
				"<div id='feature-1' style='width: 50% ;height: 60%; position:fixed;top:10%; left:0;'>here teacher rate</div>"
						+ "<div id='feature-2' style='width: 50% ;height: 60%; background: #AAA; position:fixed; top:0; left:50%;'>bbb</div>"
						+ "<div id='feature-3' style='width: 50% ;height: 50%; background: #777; position:fixed; top:60%; left:0'>xxx</div>"
						+ "<div id='feature-4' style='width: 50% ;height: 50%; background: #BBB; position:fixed; top:60%; left:50%'>rrr</div>")
				.appendTo("#room-content");
	} else if (numFeatures == 5) {
		$(
				"<div id='feature-1' style='width: 33.3% ;height: 60%; position:fixed;top:10%; left:0;'>here teacher rate</div>"
						+ "<div id='feature-2' style='width: 33.3% ;height: 60%; background: #AAA; position:fixed; top:0; left:33.3%;'>bbb</div>"
						+ "<div id='feature-3' style='width: 33.3% ;height: 60%; background: #777; position:fixed; top:0; left:66.6%'>xxx</div>"
						+ "<div id='feature-4' style='width: 50% ;height: 50%; background: #BBB; position:fixed; top:60%; left:0'>rrr</div>"
						+ "<div id='feature-5' style='width: 50% ;height: 50%; background: #CCC; position:fixed; top:60%; left:50%'>ccc</div>")
				.appendTo("#room-content");
	}
	
	createTeacherRating();
}

function updateStudentRate(rate) {
	$.ajaxSetup({
		cache : false
	});

	var formData = {
		roomNum : localStorage.getItem("currroom")
	}; // Array
	$.ajax({
		url : '/ClassPackage/api/activity/updateRate/' + rate,
		type : "GET",
		data : formData,
		dataType : 'json',
		success : function(data, jqXHR) {
			if (data['status'] == 'ok') {
				console.log(data.data);
			} else {
				console.log(data.data);
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log(errorThrown);
		}
	});
};

function createTeacherRating() {

}