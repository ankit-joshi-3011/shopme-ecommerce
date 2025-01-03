$(document).ready(function() {
	$("#buttonCancel").on("click", function() {
		window.location = moduleUrl;
	});

	$("#fileImage").change(function() {
		if (checkFileSize(this)) {
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

function checkFileSize(fileInput) {
	filesize = fileInput.files[0].size;

	if (filesize > (1 * 1024 * 1024)) {
		fileInput.setCustomValidity("You must choose an image less than 1MB");
		fileInput.reportValidity();
		return false;
	} else {
		fileInput.setCustomValidity("");
		return true;
	}
}

function showModalDialog(title, message) {
	$("#modalTitle").text(title);
	$("#modalBody").text(message);
	$("#modalDialog").modal();
}

function showErrorModalDialog(message) {
	showModalDialog("Error", message);
}

function showWarningModalDialog(message) {
	showModalDialog("Warning", message);
}
