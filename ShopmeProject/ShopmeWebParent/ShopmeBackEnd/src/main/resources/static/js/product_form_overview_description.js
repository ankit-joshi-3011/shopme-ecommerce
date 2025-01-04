$(document).ready(function() {
	dropDownBrands = $("#brand");
	dropDownCategories = $("#category");

	getCategories();

	dropDownBrands.change(function() {
		getCategories();
	});

	$("#shortDescription").richText();
	$("#fullDescription").richText();
});

function getCategories() {
	dropDownCategories.empty();

	brandId = dropDownBrands.val();

	url = brandModuleUrl + "/" + brandId + "/categories";

	$.get(url, function(responseJson) {
		$.each(responseJson, function(_index, category) {
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
