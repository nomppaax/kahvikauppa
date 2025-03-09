document.addEventListener("DOMContentLoaded", function () {
  let textarea = document.getElementById("description");
  let counter = document.getElementById("charCount");

  // Alustava merkkimäärän laskenta
  let initialCount = textarea.value.length;
  counter.textContent = initialCount + "/2000";

  textarea.addEventListener("input", function () {
    let count = textarea.value.length;
    counter.textContent = count + "/2000"; // Enimmäismerkkimäärä 2000 tietokannassa
  });
});
