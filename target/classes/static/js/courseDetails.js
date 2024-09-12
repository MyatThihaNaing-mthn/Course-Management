var selectedItems = [];

function addItem() {
  var select = document.getElementById("dropdown");
  var selectedItem = select.options[select.selectedIndex].text;

  if (selectedItem !== "" && !selectedItems.includes(selectedItem)) {
    selectedItems.push(selectedItem);
    var newItem = document.createElement("li");
    newItem.innerHTML = '<span class="removeItem" onclick="removeItem(this)">[Remove]</span> ' + selectedItem;
    newItem.classList.add("list-group-item", "list-group-item-primary", "clearfix");
    document.getElementById("selectedItems").appendChild(newItem);
  }
}

function removeItem(item) {
  var itemText = item.parentNode.textContent.replace("[Remove]", "").trim();
  var index = selectedItems.indexOf(itemText);
  if (index !== -1) {
    selectedItems.splice(index, 1);
  }
  item.parentNode.remove();
}
