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