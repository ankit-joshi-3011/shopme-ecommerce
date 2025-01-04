function addNextDetailSection() {
	var allDivDetailSections = $("[id^='divDetail']");
	var divDetailsCount = allDivDetailSections.length;

	var htmlNextDetailSection = `
		<div class="form-inline" id="divDetailSection${divDetailsCount + 1}">
			<label class="m-3">Name:</label>
			<input type="text" class="form-control w-25" name="detailNames" maxlength="255" />

			<label class="m-3">Value:</label>
			<input type="text" class="form-control w-25" name="detailValues" maxlength="255" />
		</div>
	`;

	var secondLastDivDetailSection = allDivDetailSections.last();

	var htmlLinkRemove = `
		<a class="btn fas fa-times-circle fa-2x icon-dark" title="Remove this detail"
			href="javascript:removeDetailSection('${secondLastDivDetailSection.attr("id")}')"></a>
	`;

	secondLastDivDetailSection.append(htmlLinkRemove);

	$("#divProductDetails").append(htmlNextDetailSection);

	$("input[name='detailNames']").last().focus();
}

function removeDetailSection(id) {
	$("#" + id).remove();
}
