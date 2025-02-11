var buttonLoadCountryList;
var dropdownCountryList;
var buttonAddCountry;
var buttonUpdateCountry;
var buttonDeleteCountry;
var fieldCountryName;
var fieldCountryCode;

$(document).ready(function() {
	buttonLoadCountryList = $("#buttonLoadCountryList");
	dropdownCountryList = $("#dropdownCountryList");

	buttonLoadCountryList.click(function() {
		loadCountryList();
	});

	buttonAddCountry = $("#buttonAddCountry");
	buttonUpdateCountry = $("#buttonUpdateCountry");
	buttonDeleteCountry = $("#buttonDeleteCountry");

	dropdownCountryList.on("change", function() {
		changeFormState();
	});

	fieldCountryName = $("#countryName");
	fieldCountryCode = $("#countryCode");
});

function loadCountryList() {
	var url = CONTEXT_PATH + "countries/list";

	$.get(url, function(responseJson) {
		dropdownCountryList.empty();

		$.each(responseJson, function(_, country) {
			optionValue = country.id + "-" + country.code;
			$("<option>").val(optionValue).text(country.name).appendTo(dropdownCountryList);
		});
	}).done(function() {
		buttonLoadCountryList.val("Refresh Country List");
		showToastMessage("All countries have been loaded");
	}).fail(function() {
		showToastMessage("Could not connect to the server");
	});
}

function showToastMessage(message) {
	$("#toastMessage").text(message);
	$(".toast").toast('show');
}

function changeFormState() {
	buttonAddCountry.prop("value", "New");
	buttonUpdateCountry.prop("disabled", false);
	buttonDeleteCountry.prop("disabled", false);

	var selectedCountryName = $("#dropdownCountryList option:selected").text();
	fieldCountryName.val(selectedCountryName);

	var selectedOptionValue = dropdownCountryList.val();
	var selectedCountryCode = selectedOptionValue.split("-")[1];
	fieldCountryCode.val(selectedCountryCode);
}
