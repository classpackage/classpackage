$(document).ready(function() {
	$(".edit-input").hide();
	$("#edit-profile-btn").click(updateFromInputs);
	$("#back-from-profile").click(switchView);
	$("#delete-profile-btn").click(deleteUserAjax);
	manageEditsProfile();
});

function switchView() {
	if ($("#userType-input").text() == "lecturer") {
		window.location = "#lecturer";
	} else {
		window.location = "#student";
	}
}

function manageEditsProfile() {
	$("#edit-img-userEmail").click(
			function() {
				if (!$("#userEmail-input-edit").is(":visible")) {
					$("#userEmail-input-edit").show();
					$("#edit-profile-btn").show();
				} else {
					$("#userEmail-input-edit").hide();
					if (!$("#userPassword-input-edit").is(":visible")
							&& !$("#userType-input-edit").is(":visible")) {
						$("#edit-profile-btn").hide();
						$("input").val("");
					}
				}
			});
	$("#edit-img-userPassword").click(
			function() {
				if (!$("#userPassword-input-edit").is(":visible")) {
					$("#userPassword-input-edit").show();
					$("#edit-profile-btn").show();
				} else {
					$("#userPassword-input-edit").hide();
					if (!$("#userEmail-input-edit").is(":visible")
							&& !$("#userType-input-edit").is(":visible")) {
						$("#edit-profile-btn").hide();
						$("input").val("");
					}
				}
			});

	$("#edit-img-userType").click(
			function() {
				if (!$("#userType-input-edit").is(":visible")) {
					$("#userType-input-edit").show();
					$("#edit-profile-btn").show();
				} else {
					$("#userType-input-edit").hide();
					if (!$("#userEmail-input-edit").is(":visible")
							&& !$("#userPassword-input-edit").is(":visible")) {
						$("#edit-profile-btn").hide();
						$("input").val("");
					}
				}
			});
};

$(function manageProfileDetails() {
	profileDetailsAjax();
});

function updateFromInputs() {
	var email = $("#userEmail-edit").val() || "";
	var oldPassword = $("#userOldPassword-edit").val() || "";
	var newPassword = $("#userNewPassword-edit").val() || "";
	var type = $("#flip-type").val() || "";

	if (type == "lecturer") {
		type = "true"
	} else if (type == "student") {
		type = "false"
	}

	var formData = {
		email : email,
		oldPassword : oldPassword,
		newPassword : newPassword,
		type : type
	}; // Array

	updateSettingsAjax(formData);
}

function profileDetailsAjax() {
	$.ajaxSetup({
		cache : false
	});

	$.ajax({
		url : '/ClassPackage/api/user/' + localStorage.getItem("userid"),
		type : "GET",
		dataType : 'json',
		success : function(data, jqXHR) {
			if (data['status'] == 'ok') {
				$("#userFirstName-input").text(data.data.firstName);
				$("#userLastName-input").text(data.data.lastName);
				$("#userEmail-input").text(data.data.email);
				if (data.data.isLecturer) {
					$("#userType-input").text("lecturer");

				} else {
					$("#userType-input").text("student");
				}
			} else {
				console.log(data.data);
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log(errorThrown);
		}
	});
}

function updateSettingsAjax(formData) {
	$.ajaxSetup({
		cache : false
	});

	$.ajax({
		url : '/ClassPackage/api/user/updateProfileSetting/'
				+ localStorage.getItem("userid"),
		type : "GET",
		data : formData,
		dataType : 'json',
		success : function(data, jqXHR) {
			if (data['status'] == 'ok') {
				$.mobile.loading('show', {
					theme : "a",
					text : "profile was updated",
					textonly : true,
					textVisible : true
				});
				// hide after delay
				setTimeout(function() {
					$.mobile.loading('hide');
				}, 2000);
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
		},
		complete : function() {
			profileDetailsAjax();
		}

	});
};

function deleteUserAjax() {
	$.ajaxSetup({
		cache : false
	});
	$.ajax({
		url : '/ClassPackage/api/user/deleteprofile/'
				+ localStorage.getItem("userid"),
		method : 'GET',
		dataType : 'json',
		success : function(data, jqXHR) {
			if (data['status'] == 'ok') {
				console.log("profile was deleated! ");
					window.location = "#homePage";
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log(errorThrown);
		}
	});
};

function cleanInputs() {
	$("input").val("");
	$(".edit-input").hide();
	$("#edit-profile-btn").hide();
}
