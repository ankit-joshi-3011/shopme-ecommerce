var buttonLoadCountryListForStates;
var dropdownCountryListForStates;
var dropdownStateList;
var fieldStateName;
var buttonAddState;
var buttonUpdateState;
var buttonDeleteState;

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

	fieldStateName = $("#stateName");

	buttonAddState = $("#buttonAddState");
	buttonUpdateState = $("#buttonUpdateState");
	buttonDeleteState = $("#buttonDeleteState");

	dropdownStateList.on("change", function() {
		changeStateFormStateForSelection();
	});

	buttonAddState.click(function() {
		if (buttonAddState.val() == "Add") {
			addState();
		} else {
			changeStateFormStateForCreation();
		}
	});

	buttonUpdateState.click(function() {
		updateState();
	});

	buttonDeleteState.click(function() {
		deleteState();
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
		fieldStateName.val("");

		$.each(responseJson, function(_, state) {
			$("<option>").val(state.id).text(state.name).appendTo(dropdownStateList);
		});
	}).done(function() {
		showToastMessage("All states have been loaded");
	}).fail(function() {
		showToastMessage("Could not connect to the server");
	});
}

function changeStateFormStateForSelection() {
	buttonAddState.prop("value", "New");
	buttonUpdateState.prop("disabled", false);
	buttonDeleteState.prop("disabled", false);

	var selectedStateName = $("#dropdownStateList option:selected").text();
	fieldStateName.val(selectedStateName);
}

function validateFormState() {
	var formState = document.getElementById("formState");

	if (!formState.checkValidity()) {
		formState.reportValidity();
		return false;
	}

	return true;
}

function addState() {
	if (!validateFormState()) {
		return;
	}

	var url = CONTEXT_PATH + "states/save";

	var stateName = fieldStateName.val();
	var selectedCountryIdAndCode = dropdownCountryListForStates.val().split("-");
	var countryName = $("#dropdownCountryListForStates option:selected").text();

	var requestBody = {
		name: stateName,
		country: {
			id: selectedCountryIdAndCode[0],
			name: countryName,
			code: selectedCountryIdAndCode[1],
		},
	};

	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(requestBody),
		contentType: 'application/json'
	}).done(function(stateId) {
		selectNewlyAddedState(stateId, stateName, countryCode);
		showToastMessage("The new state has been added successfully");
	}).fail(function() {
		showToastMessage("Could not connect to the server");
	});
}

function selectNewlyAddedState(stateId, stateName) {
	$("<option>").val(stateId).text(stateName).appendTo(dropdownStateList);

	$("#dropdownStateList option[value='" + stateId + "']").prop("selected", true);

	changeStateFormStateForSelection();
}

function changeStateFormStateForCreation() {
	buttonAddState.prop("value", "Add");
	buttonUpdateState.prop("disabled", true);
	buttonDeleteState.prop("disabled", true);

	fieldStateName.val("").focus();
}

function updateState() {
	if (!validateFormState()) {
		return;
	}

	var url = CONTEXT_PATH + "states/save";

	var stateId = dropdownStateList.val();
	var stateName = fieldStateName.val();
	var selectedCountryIdAndCode = dropdownCountryListForStates.val().split("-");
	var countryName = $("#dropdownCountryListForStates option:selected").text();

	var requestBody = {
		id: stateId,
		name: stateName,
		country: {
			id: selectedCountryIdAndCode[0],
			name: countryName,
			code: selectedCountryIdAndCode[1],
		},
	};

	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(requestBody),
		contentType: 'application/json'
	}).done(function(stateId) {
		$("#dropdownStateList option:selected").val(stateId);
		$("#dropdownStateList option:selected").text(stateName);
		showToastMessage("The state has been updated successfully");
	}).fail(function() {
		showToastMessage("Could not connect to the server");
	});
}

function deleteState() {
	var stateId = dropdownStateList.val();
	var url = CONTEXT_PATH + "states/delete/" + stateId;

	$.ajax({
		type: 'DELETE',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function() {
		$("#dropdownStateList option:selected").remove();
		changeStateFormStateForCreation();
		showToastMessage("The state has been deleted successfully");
	}).fail(function() {
		showToastMessage("Could not connect to the server");
	});
}
