async function showSection(buttonClass, sectionClass, buttonId, sectionId) {

    if (typeof contentLoaded !== "boolean" || !contentLoaded) {
        console.error("ERROR - content not loaded yet!");

    } else {
        const buttons = document.querySelectorAll(buttonClass);
        buttons.forEach(section => section.classList.remove('active'));

        const sections = document.querySelectorAll(sectionClass);
        sections.forEach(section => section.classList.remove('active'));

        const activeButton = document.getElementById(buttonId);
        activeButton.classList.add('active');

        const activeSection = document.getElementById(sectionId);
        activeSection.classList.add('active');
    }
}
