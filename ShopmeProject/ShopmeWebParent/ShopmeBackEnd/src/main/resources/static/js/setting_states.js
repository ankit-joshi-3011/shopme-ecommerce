var buttonLoadCountryListForStates;
var dropdownCountryListForStates;
var dropdownStateList;

$(document).ready(function() {
	buttonLoadCountryListForStates = $("#buttonLoadCountryListForStates");
	dropdownCountryListForStates = $("#dropdownCountryListForStates");
	dropdownStateList = $("#dropdownStateList");

	buttonLoadCountryListForStates.click(function() {
		loadCountryListForStates();
	});

	dropdownCountryListForStates.on("change", function() {
		loadStatesForSelectedCountry();
	});
});

function loadCountryListForStates() {
	var url = CONTEXT_PATH + "countries/list";

	$.get(url, function(responseJson) {
		dropdownCountryListForStates.empty();

		$.each(responseJson, function(_, country) {
			var optionValue = country.id + "-" + country.code;
			$("<option>").val(optionValue).text(country.name).appendTo(dropdownCountryListForStates);
		});

		loadStatesForSelectedCountry();
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

function loadStatesForSelectedCountry() {
	var selectedCountryOptionValue = dropdownCountryListForStates.val();
	var selectedCountryId = selectedCountryOptionValue.split("-")[0];

	var url = CONTEXT_PATH + "states/list/" + selectedCountryId;

	$.get(url, function(responseJson) {
		dropdownStateList.empty();

		$.each(responseJson, function(_, state) {
			$("<option>").val(state.id).text(state.name).appendTo(dropdownStateList);
		});
	}).done(function() {
		showToastMessage("All states have been loaded");
	}).fail(function() {
		showToastMessage("Could not connect to the server");
	});
}
