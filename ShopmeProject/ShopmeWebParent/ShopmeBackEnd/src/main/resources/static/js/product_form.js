$(document).ready(function() {
	dropDownBrands = $("#brand");
	dropDownCategories = $("#category");

	getCategories();

	dropDownBrands.change(function() {
		getCategories();
	});

	$("#shortDescription").richText();
	$("#fullDescription").richText();

	$("input[name='extraImage']").each(function(index) {
		$(this).change(function() {
			showExtraImageThumbnail(this, index + 1);
		});
	});
});

function showExtraImageThumbnail(fileInput, index) {
	if (checkFileSize(fileInput)) {
		var file = fileInput.files[0];
		var reader = new FileReader();
		reader.onload = function(e) {
			$("#extraThumbnail" + index).attr("src", e.target.result);
		}

		reader.readAsDataURL(file);

		addNextExtraImageSection(index + 1);
	}
}

function addNextExtraImageSection(index) {
	htmlExtraImageSection = `
		<div class="col border m-3 p-2">
			<div id="extraImageHeader${index}">
				<label>Extra Image #${index}:</label>
			</div>
			<div class="m-2">
				<img id="extraThumbnail${index}" alt="Extra Image #${index} Preview" class="img-fluid"
					src="${missingImageSrc}">
			</div>
			<div>
				<input type="file" name="extraImage" accept="image/png, image/jpeg" onchange="showExtraImageThumbnail(this, ${index})" />
			</div>
		</div>
	`;

	$("#divProductImages").append(htmlExtraImageSection);
}

function getCategories() {
	dropDownCategories.empty();

	brandId = dropDownBrands.val();

	url = brandModuleUrl + "/" + brandId + "/categories";

	$.get(url, function(responseJson) {
		$.each(responseJson, function(index, category) {
			$("<option>").val(category.id).text(category.name).appendTo(dropDownCategories);
		});
	});
}

function checkUnique(form) {
	productId = $("#id").val();
	productName = $("#name").val();
	productAlias = $("#alias").val();
	csrfValue = $("input[name='_csrf']").val();

	params = { id: productId, name: productName, alias: productAlias, _csrf: csrfValue };

	$.post(checkUniqueUrl, params, function(response) {
		if (response == "OK") {
			form.submit();
		} else if (response == "DuplicateName") {
			showWarningModalDialog("There is another product having the name " + productName);
		} else if (response == "DuplicateAlias") {
			showWarningModalDialog("There is another product having the alias " + productAlias);
		} else {
			showErrorModalDialog("Unknown response from server");
		}
	}).fail(function() {
		showErrorModalDialog("Could not connect to the server");
	});

	return false;
}