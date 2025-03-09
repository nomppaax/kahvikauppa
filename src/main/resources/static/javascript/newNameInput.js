// Toimittajan ja valmistajan valinnan muokkaaminen
document.addEventListener("DOMContentLoaded", function () {
  document.getElementById("supplier").addEventListener("change", function () {
    let isNewSupplier = document.getElementById("supplier").value === "new";
    document.getElementById("newSupplierNameInput").style.display =
      isNewSupplier ? "block" : "none";
    document.getElementById("newSupplierNameLabel").style.display =
      isNewSupplier ? "block" : "none";
  });
  document.getElementById("producer").addEventListener("change", function () {
    let isNewProducer = document.getElementById("producer").value === "new";
    document.getElementById("newProducerNameInput").style.display =
      isNewProducer ? "block" : "none";
    document.getElementById("newProducerNameLabel").style.display =
      isNewProducer ? "block" : "none";
  });
});
