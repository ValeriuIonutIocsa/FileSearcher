function showSpinner() {
    const spinner = document.getElementById("spinner");
    if (spinner) {
        spinner.hidden = false;
    }
}

function hideSpinner() {
    const spinner = document.getElementById("spinner");
    if (spinner) {
        spinner.hidden = true;
    }
}

function showSection(buttonClass, sectionClass, buttonId, sectionId) {

    if (typeof contentLoaded !== "boolean" || !contentLoaded) {
        console.error("ERROR - content not loaded yet!");

    } else {

        showSpinner();

        setTimeout(function () {

            const buttons = document.querySelectorAll(buttonClass);
            buttons.forEach(section => section.classList.remove('active'));

            const sections = document.querySelectorAll(sectionClass);
            sections.forEach(section => section.classList.remove('active'));

            const activeButton = document.getElementById(buttonId);
            activeButton.classList.add('active');

            const activeSection = document.getElementById(sectionId);
            activeSection.classList.add('active');

            hideSpinner();
        }, 0);
    }
}
