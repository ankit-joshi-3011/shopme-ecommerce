$(document).ready(function() {
	$("#buttonCancel").on("click", function() {
		window.location = moduleUrl;
	});

	$("#fileImage").change(function() {
		filesize = this.files[0].size;

		if (filesize > (1 * 1024 * 1024)) {
			this.setCustomValidity("You must choose an image less than 1MB");
			this.reportValidity();
		} else {
			this.setCustomValidity("");
			showImageThumbnail(this);
		}
	});
});

function showImageThumbnail(fileInput) {
	var file = fileInput.files[0];
	var reader = new FileReader();
	reader.onload = function(e) {
		$("#thumbnail").attr("src", e.target.result);
	}

	reader.readAsDataURL(file);
}
