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
		changeFormStateForSelection();
	});

	fieldCountryName = $("#countryName");
	fieldCountryCode = $("#countryCode");

	buttonAddCountry.click(function() {
		if (buttonAddCountry.val() == "Add") {
			addCountry();
		} else {
			changeFormStateForUpdation();
		}
	});
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

function changeFormStateForSelection() {
	buttonAddCountry.prop("value", "New");
	buttonUpdateCountry.prop("disabled", false);
	buttonDeleteCountry.prop("disabled", false);

	var selectedCountryName = $("#dropdownCountryList option:selected").text();
	fieldCountryName.val(selectedCountryName);

	var selectedOptionValue = dropdownCountryList.val();
	var selectedCountryCode = selectedOptionValue.split("-")[1];
	fieldCountryCode.val(selectedCountryCode);
}

function changeFormStateForUpdation() {
	buttonAddCountry.prop("value", "Add");
	buttonUpdateCountry.prop("disabled", true);
	buttonDeleteCountry.prop("disabled", true);

	fieldCountryName.val("").focus();
	fieldCountryCode.val("");
}

function addCountry() {
	var url = CONTEXT_PATH + "countries/save";
	var countryName = fieldCountryName.val();
	var countryCode = fieldCountryCode.val();
	var requestBody = {
		name: countryName,
		code: countryCode,
	};

	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(requestBody),
		contentType: 'application/json'
	}).done(function(countryId) {
		selectNewlyAddedCountry(countryId, countryName, countryCode);
		showToastMessage("The new country has been added successfully");
	}).fail(function() {
		showToastMessage("Could not connect to the server");
	});
}

function selectNewlyAddedCountry(countryId, countryName, countryCode) {
	var optionValue = countryId + "-" + countryCode;
	$("<option>").val(optionValue).text(countryName).appendTo(dropdownCountryList);

	$("#dropdownCountryList option[value='" + optionValue + "']").prop("selected", true);
}
