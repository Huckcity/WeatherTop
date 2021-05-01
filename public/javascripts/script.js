/* Toggle between showing and hiding the navigation menu links when the user clicks on the hamburger menu / bar icon */
function showNav() {
    let nav = document.getElementById("nav");
    let navContainer = document.getElementById("header");
    if (nav.classList.contains("hidden")) {
        nav.classList.remove("hidden");
        nav.classList.add("show-nav");
        // navContainer.classList.add()
    } else {
        nav.classList.add("hidden");
        nav.classList.remove("show-nav");
    }
}