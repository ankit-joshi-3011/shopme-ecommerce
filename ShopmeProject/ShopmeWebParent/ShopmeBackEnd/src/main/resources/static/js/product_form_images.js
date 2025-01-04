var extraImagesCount = 0;

$(document).ready(function() {
	$("input[name='extraImage']").each(function(index) {
		extraImagesCount++;

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

		if (index == extraImagesCount) {
			addNextExtraImageSection(index + 1);
			extraImagesCount++;
		}
	}
}

function addNextExtraImageSection(index) {
	htmlExtraImageSection = `
		<div class="col border m-3 p-2" id="divExtraImageSection${index}">
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

	htmlLinkRemove = `
		<a class="btn fas fa-times-circle fa-2x icon-dark float-right" title="Remove this image"
			href="javascript:removeExtraImage(${index - 1})"></a>
	`;

	$("#divProductImages").append(htmlExtraImageSection);

	$("#extraImageHeader" + (index - 1)).append(htmlLinkRemove);
}

function removeExtraImage(index) {
	$("#divExtraImageSection" + index).remove();
}
