$(document).ready(function() {
	$("#logoutLink").on("click", function(event) {
		event.preventDefault();
		document.logoutForm.submit();
	});
});