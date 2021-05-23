/* Toggle between showing and hiding the navigation menu links when the user clicks on the hamburger menu / bar icon */
function toggleNav() {
    let nav = document.getElementById("nav");
    if (nav.classList.contains("hidden")) {
        nav.classList.remove("hidden");
        nav.classList.add("show-nav");
    } else {
        nav.classList.add("hidden");
        nav.classList.remove("show-nav");
    }
}

function closeToast() {
    $("#toast").fadeOut();
}