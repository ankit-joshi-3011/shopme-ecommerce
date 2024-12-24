$(document).ready(function() {
	$("#logoutLink").on("click", function(event) {
		event.preventDefault();
		document.logoutForm.submit();
	});

	addAnimationToDropDownMenu();
});

function addAnimationToDropDownMenu() {
	$(".navbar .dropdown").hover(
		function() {
			$(this).find('.dropdown-menu').first().stop(true, true).delay(250).slideDown();
		},
		function() {
			$(this).find('.dropdown-menu').first().stop(true, true).delay(150).slideUp();
		}
	);
}

function showDeleteConfirmationModalDialog(e, link, entity) {
	e.preventDefault();
	$("#yesButton").attr("href", link.attr("href"));
	$("#confirmText").text("Are you sure you want to delete this " + entity + " with ID " + link.attr("entityId") + "?");
	$("#confirmModalDialog").modal();
}