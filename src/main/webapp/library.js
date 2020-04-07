function createItemForList(text, input_id) {
    let item = document.createElement('li');
    item.setAttribute("data",text);
    item.appendChild(document.createTextNode(text));

    // if the user puts their mouse over it, highlight it
    item.addEventListener('mouseover', function() {
      item.style.background = "black";
      item.style.color = "white";
    });

    // if the user moves their mouse away, go to default style
    item.addEventListener('mouseout', function() {
      item.style.background = "white";
      item.style.color = "black";
    });

    // if the user clicks, put the value in the input field
    item.addEventListener('click', function(event) {
      let lend_book = document.getElementById(input_id);
      lend_book.value = event.target.innerText;
      deleteSearchBox(input_id);
    });

    return item;
}

function deleteSearchBox(input_id) {
  let searchbox = document.getElementById(input_id+"_searchbox");
  if (searchbox != null) {
    searchbox.remove();
  }
}

// removes items from the list based on whether they equal a value
function removeItemFromList(list, text) {
    var items = list.getElementsByTagName("li");
    for (var i = 0; i < items.length; ++i) {
        if (items[i].getAttribute('data') === text) {
            list.removeChild(items[i]);
        }
    }
}

function createList(input_id) {
    var innerList = document.createElement('ul');
    innerList.setAttribute("id", input_id+"_searchlist");
    innerList.style.listStyleType="none";
    innerList.style.margin="0";
    innerList.style.padding="0";

    return innerList;
}

function createSearchBox(input_id) {
    let searchbox = document.createElement('div');
    searchbox.setAttribute("id", input_id+"_searchbox");
    searchbox.style.height="auto"
    searchbox.style.width ="162px"
    searchbox.style.position = "absolute"
    searchbox.style.background = "white"
    searchbox.style.border = "solid 1px"
    return searchbox;
}

let bookdata;
let bookrequest = new XMLHttpRequest();
bookrequest.open("GET", "book", true);
bookrequest.onreadystatechange = function () {
  if (bookrequest.readyState != 4 || bookrequest.status != 200) return;
  bookdata = JSON.parse(bookrequest.responseText).map(book => book.Title);
};
bookrequest.send();

let borrowerdata;
let borrowerrequest = new XMLHttpRequest();
borrowerrequest.open("GET", "borrower", true);
borrowerrequest.onreadystatechange = function () {
  if (borrowerrequest.readyState != 4 || borrowerrequest.status != 200) return;
  borrowerdata = JSON.parse(borrowerrequest.responseText).map(borrower => borrower.Name);
};
borrowerrequest.send();

function addAutoComplete(id, getdata) {

    let list = createList(id);

    let element = document.getElementById(id);

    function considerRemovingSearchBoxOnPressingEscape(event) {
        if (event.key === "Escape") {
            deleteSearchBox(id);
        }
    }

    // if the user clicks outside the searchbox, delete the searchbox
    element.addEventListener('blur', deleteSearchBox(id));

    // if the user is typing and presses escape, kill the searchbox
    document.addEventListener('keydown', considerRemovingSearchBoxOnPressingEscape);

    function considerAutoComplete(event) {
        if (event.key === "Escape") return;
        // as the user presses keys, we keep checking what is current
        // and we provide a search box for that.

        let currentContent = element.value;
        if (currentContent.length == 0) {
            // if there's no text in the input box, don't show a searchbox.

            // delete it if it exists
             deleteSearchBox(id);
        } else {
            // if there's more than one item in the input...
             
            // if the searchbox doesn't exist, create it...
            if (document.getElementById(id+"_searchbox") == null) {
                let searchbox = createSearchBox(id);
                element.insertAdjacentElement('afterend',searchbox);
                searchbox.appendChild(list);
            }

            // clear the list
            list.innerHTML = '';

            // add filtered data to the list
            let filteredList = getdata().filter(text => text.includes(currentContent));
            for (var i = 0; i < filteredList.length; i++) {
                list.appendChild(createItemForList(filteredList[i], id))
            }
            
        }
    }

    // if the user clicks, open the autocomplete
    element.addEventListener('keyup', considerAutoComplete);
    element.addEventListener('click', considerAutoComplete);

}

addAutoComplete("lend_book", function() {return bookdata});
addAutoComplete("lend_borrower", function() {return borrowerdata});


















