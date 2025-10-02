function showSection(buttonClass, sectionClass, buttonId, sectionId) {

    const buttons = document.querySelectorAll(buttonClass);
    buttons.forEach(section => section.classList.remove('active'));

    const sections = document.querySelectorAll(sectionClass);
    sections.forEach(section => section.classList.remove('active'));

    const activeButton = document.getElementById(buttonId);
    activeButton.classList.add('active');

    const activeSection = document.getElementById(sectionId);
    activeSection.classList.add('active');
}