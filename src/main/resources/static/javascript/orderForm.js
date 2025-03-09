let orderItems = {};
let timerId;

// Uusi piilotettu input-kenttä formin sisälle
function addCartItemToForm(productId, productName, productPrice) {
  let orderForm = document.getElementById("orderForm");
  let orderInput = document.createElement("input");
  orderInput.type = "text";
  orderInput.style.display = "none";
  orderInput.name = "order";
  const dateTime = getCurrentDateTime();
  orderInput.value =
    "TILAUSAIKA: " +
    dateTime +
    ", TUOTTEEN ID:" +
    productId +
    ", NIMI: " +
    productName +
    ", HINTA: " +
    productPrice +
    "€";
  orderForm.appendChild(orderInput);
}

// Tilauslistaan lisääminen
window.addToCart = function (productInfo) {
  let productData = productInfo.split(":");
  let productId = productData[0];
  let productName = productData[1];
  let productPrice = parseFloat(productData[2]);

  if (productName in orderItems) {
    // Tuote on jo tilauslistalla, päivitetään määrää
    orderItems[productName].count++;
    updateCartItem(productName);
    addCartItemToForm(productId, productName, productPrice);
    // Käynnistetään ajastin vain jos se ei ole vielä käynnissä
    if (!timerId) {
      timerId = setTimeout(hideOrderList, 3000);
    }
    if (timerId) {
      clearTimeout(timerId);
      timerId = setTimeout(hideOrderList, 3000); // Päivitetään ajastimen aika uudestaan
    }
  } else {
    // Lisätään tuote tilauslistalle ensimmäistä kertaa
    orderItems[productName] = {
      count: 1,
      price: productPrice,
    };

    // Lisätään tuote listalle näytölle
    addCartItem(productName, productPrice);
    // Lisätään uusi piilotettu input-kenttä formin sisälle
    addCartItemToForm(productId, productName, productPrice);
    showOrderList();
    // Käynnistetään ajastin vain jos se ei ole vielä käynnissä
    if (!timerId) {
      timerId = setTimeout(hideOrderList, 3000);
    }
    if (timerId) {
      clearTimeout(timerId);
      timerId = setTimeout(hideOrderList, 3000); // Päivitetään ajastimen aika uudestaan
    }
  }

  document.getElementById("orderFormContainer").style.display = "block";
  document.getElementById("orderFormContainer").classList.add("order-list");
  document.getElementById("order-button").classList.add("input.submit-button");
};
// Lisätään tuote listalle näytölle
function addCartItem(productName, productPrice) {
  let orderList = document.getElementById("orderList");
  let listItem = document.createElement("li");
  listItem.id = productName;
  listItem.textContent = `1 kpl, tuote: ${productName}, hinta: ${productPrice.toFixed(
    2
  )}€`;
  orderList.appendChild(listItem);
  updateTotal();
}
// Päivitetään tilauslistan tuotteen määrä
function updateCartItem(productName) {
  let listItems = document
    .getElementById("orderList")
    .getElementsByTagName("li");
  for (let item of listItems) {
    if (item.textContent.includes(productName)) {
      let count = parseInt(item.textContent.split(" ")[0]);
      item.textContent = `${count + 1} kpl, tuote: ${productName}, hinta: ${(
        orderItems[productName].price *
        (count + 1)
      ).toFixed(2)}€`;
      if (timerId) {
        clearTimeout(timerId);
        timerId = setTimeout(hideOrderList, 3000); // Päivitetään ajastimen aika uudestaan
      }
      break;
    }
  }
  if (timerId) {
    clearTimeout(timerId);
    timerId = setTimeout(hideOrderList, 3000); // Päivitetään ajastimen aika uudestaan
  }
  updateTotal();
}
// Päivitetään total tilauslistalle
function updateTotal() {
  let total = 0;
  for (let productName in orderItems) {
    total += orderItems[productName].count * orderItems[productName].price;
  }
  let totalElement = document.getElementById("total");
  totalElement.textContent = `Yhteensä: ${total.toFixed(2)}€`;
}

function getCurrentDateTime() {
  const now = new Date();
  const date = now.toLocaleDateString();
  const time = now.toLocaleTimeString();
  return `${date} ${time}`;
}

// Piilota tl
window.hideOrderList = function () {
  document.getElementById("orderFormContainer").style.display = "none";
  // näytä kori-ikoni
  document.getElementById("basket-icon").style.display = "block";
  // Nollaa ajastin
  timerId = null;
};
// Näytä tilauslista
window.showOrderList = function () {
  // Nollaa ajastin
  clearTimeout(timerId);
  document.getElementById("orderFormContainer").style.display = "block";
  // Piilota kori-ikoni
  document.getElementById("basket-icon").style.display = "none";
};
