// Haetaan yläpalkin elementit
let upperBar = document.querySelector(".navigation-items");
let navigationLinks = document.querySelectorAll(".navigation-items li a");

window.addEventListener("scroll", function () {
  // Sivun nykyinen scroll-arvo
  let scrollTop = window.pageYOffset || document.documentElement.scrollTop;

  if (scrollTop > 0) {
    // Sivua rullataan alaspäin, muutetaan yläpalkin väriä
    upperBar.style.backgroundColor = "whitesmoke";
    upperBar.style.border = "0.5px solid black";
    navigationLinks.forEach(function (link) {
      link.style.color = "black";
    });
  } else {
    // Sivua ei ole rullattu (ollaan yläosassa), palautetaan väri
    upperBar.style.backgroundColor = "transparent";
    upperBar.style.border = "transparent";
    navigationLinks.forEach(function (link) {
      link.style.color = "whitesmoke";
    });
  }
});
