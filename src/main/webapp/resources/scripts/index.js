var page="";

$(document).live('pagebeforeshow', function() {
	page = $.mobile.activePage.attr('id');
	console.log(page);
});

$( document ).on( "pageinit", "#lecturer", function() {
    $( document ).on( "swipeleft swiperight", "#lecturer", function( e ) {
        // We check if there is no open panel on the page because otherwise
        // a swipe to close the left panel would also open the right panel (and v.v.).
        // We do this by checking the data that the framework stores on the page element (panel: open).
        if ( $.mobile.activePage.jqmData( "panel" ) !== "open" ) {
            if ( e.type === "swipeleft"  ) {
                $( "#right-panel1" ).panel( "open" );
            } else if ( e.type === "swiperight" ) {
                $( "#left-panel1" ).panel( "open" );
            }
        }
    });
});

$( document ).on( "pageinit", "#student", function() {
    $( document ).on( "swipeleft swiperight", "#student", function( e ) {
        // We check if there is no open panel on the page because otherwise
        // a swipe to close the left panel would also open the right panel (and v.v.).
        // We do this by checking the data that the framework stores on the page element (panel: open).
        if ( $.mobile.activePage.jqmData( "panel" ) !== "open" ) {
            if ( e.type === "swipeleft"  ) {
                $( "#right-panel2" ).panel( "open" );
            } else if ( e.type === "swiperight" ) {
                $( "#left-panel2" ).panel( "open" );
            }
        }
    });
});

$(document).ready(function() {
	cleanForm();
	manageLogin();
	manageRegister();

});


function manageLogin() {

	$("#loginForm").validate(
			{
				errorPlacement : function(error, element) {
					error.insertAfter(element);
				},

				submitHandler : function(form) { // Show loading indicator
					$.mobile.loading("show"); // login post req
					$.ajax({
						type : 'POST',
						url : '/ClassPackage/api/user/login',
						data : $('#loginForm').serialize(),
						success : function(data) {
							switchToProfile(data);
						},
						statusCode : {
							401 : function() { // unauthorized
								$.mobile.loading("hide");
								localStorage.userid = -1;
								// show error message
								$.mobile.loading('show', {
								    theme: "a",
								    text: "Email/Password missmatch.",
								    textonly: true,
								    textVisible: true
								});
								// hide after delay
								setTimeout(function(){
									 $.mobile.loading('hide');
								}, 2000);
							}
						}

					});
				}
			});
};

function manageRegister() {
	$("#registerForm").validate(
			{
				errorPlacement : function(error, element) {
					error.insertAfter(element);
				},
				submitHandler : function(form) { // Show loading indicator
					$.mobile.loading("show"); // Post create user request to
												// the API
					$.post("/ClassPackage/api/user/register",
							$('#registerForm').serialize(), function(data) {
								switchToProfile(data);
							});
				}
			})
};

function switchToProfile(data) {

	// Hide Loading indicator
	$.mobile.loading("hide");

	if (data['status'] == 'ok') {
		localStorage.userid = data.data.serialNum;
		localStorage.fullName = data.data.firstName + " " + data.data.lastName;
		localStorage.isLecturer = data.data.isLecturer;

		$("#welcome-user").text("welcome " + localStorage.getItem("fullName"));

		if (data.data.isLecturer == true) {
			window.location = "#lecturer";
		} else {
			window.location = "#student";
		}
	} else {
		// show error message
		$.mobile.loading('show', {
		    theme: "e",
		    text: data.data,
		    textonly: true,
		    textVisible: true
		});
		// hide after delay
		setTimeout(function(){
			 $.mobile.loading('hide');
		}, 2000);
	}
}

// cleaning form data
function cleanLogin() {
	$('#password').val("");
	$('#email').val("");
}

// cleaning form data
function cleanForm() {
	$("#loginForm").find("input[type=text]").val("");
	$("#loginForm").find("input[type=email]").val("");
	$("#loginForm").find("input[type=password]").val("");

	$("#registerForm").find("input[type=text]").val("");
	$("#registerForm").find("input[type=email]").val("");
	$("#registerForm").find("input[type=password]").val("");
}

function calculateDiffTime(date) {
	var dateCompare = organizeDate(date);
	var diff = (new Date(dateCompare) - new Date());
	var minutes = Math.round(diff / 60000);
	return minutes.toString();
}
// Apr 8, 2015 11:30:00 PM
function organizeDate(date) {
	var day = date.substr(4, 2);

	if (!(/[1-3]/.test(day[0]))) {
		day = "0" + day
	}
	var mon = date.substr(0, 3);
	switch (mon) {
	case "Jan":
		mon = "01";
		break;
	case "Feb":
		mon = "02";
		break;
	case "Mar":
		mon = "03";
		break;
	case "Apr":
		mon = "04";
		break;
	case "May":
		mon = "05";
		break;
	case "Jun":
		mon = "06";
		break;
	case "Jul":
		mon = "07";
		break;
	case "Aug":
		mon = "08";
		break;
	case "Sep":
		mon = "09";
		break;
	case "Oct":
		mon = "10";
		break;
	case "Nov":
		mon = "11";
		break;
	case "Dec":
		mon = "12";
		break;
	}
	var year = date.split(", ");
	year = year[1].substr(0, 4);
	var time = date.substr(date.length - 11, 11);
	time = organizeTimeStr(time);

	var newDate = year.toString() + "/" + mon.toString() + "/" + day.toString()
			+ " " + time.toString();

	return newDate;
}

function organizeTimeStr(time) {
	var hours = parseInt(time.substr(0, 2));
	if (time.indexOf('AM') != -1 && hours == 12) {
		time = time.replace('12', '0');
	}
	if (time.indexOf('PM') != -1 && hours < 12) {
		time = time.replace(hours, (hours + 12));
	}
	return time.replace(/(\s*AM|\s*PM)/, '');
}