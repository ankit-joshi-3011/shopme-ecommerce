var buttonLoadCountryListForStates;
var dropdownCountryListForStates;

$(document).ready(function() {
	buttonLoadCountryListForStates = $("#buttonLoadCountryListForStates");
	dropdownCountryListForStates = $("#dropdownCountryListForStates");

	buttonLoadCountryListForStates.click(function() {
		loadCountryListForStates();
	});
});

function loadCountryListForStates() {
	var url = CONTEXT_PATH + "countries/list";

	$.get(url, function(responseJson) {
		dropdownCountryListForStates.empty();

		$.each(responseJson, function(_, country) {
			optionValue = country.id + "-" + country.code;
			$("<option>").val(optionValue).text(country.name).appendTo(dropdownCountryListForStates);
		});
	}).done(function() {
		buttonLoadCountryListForStates.val("Refresh Country List");
		showToastMessage("All countries have been loaded");
	}).fail(function() {
		showToastMessage("Could not connect to the server");
	});
}

function showToastMessage(message) {
	$("#toastMessage").text(message);
	$(".toast").toast('show');
}
