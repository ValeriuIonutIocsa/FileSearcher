let contentLoaded = false;

document.addEventListener("DOMContentLoaded", function () {

    const spinner = document.getElementById("spinner");
    spinner.hidden = true;
    contentLoaded = true;

    if (typeof onContentLoad === "function") {
        onContentLoad();
    }
});
