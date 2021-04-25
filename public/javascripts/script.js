/* Toggle between showing and hiding the navigation menu links when the user clicks on the hamburger menu / bar icon */
function myFunction() {
    let x = document.getElementById("myLinks");
    if (x.classList.contains("hidden")) {
        x.classList.remove("hidden")
    } else {
        x.classList.add("hidden")
    }
}