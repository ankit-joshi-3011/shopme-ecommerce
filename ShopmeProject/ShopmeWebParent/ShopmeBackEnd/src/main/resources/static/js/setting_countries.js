var buttonLoadCountryList;
var dropdownCountryList;

$(document).ready(function() {
	buttonLoadCountryList = $("#buttonLoadCountryList");
	dropdownCountryList = $("#dropdownCountryList");

	buttonLoadCountryList.click(function() {
		loadCountryList();
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
	});
}
