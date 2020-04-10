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

function addAutoComplete(id, getdata) {

    let list = createList(id);

    let element = document.getElementById(id);

    function considerRemovingSearchBoxOnPressingEscape(event) {
        if (event.key === "Escape") {
            deleteSearchBox(id);
        }
    }

    document.addEventListener('keydown', considerRemovingSearchBoxOnPressingEscape);
    element.addEventListener('blur', function(event){deleteSearchBox(id)});

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



function talk(verb, path, data) {
  return new Promise((resolve, reject) => {
    let r = new XMLHttpRequest();
    r.open(verb, path, true);
    //Send the proper header information along with the request
    r.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    r.onreadystatechange = function () {
      if (r.readyState != 4 || r.status != 200) {
        return;
      } else {
          resolve(r.responseText);
      }

    }
    r.send(data);
  });
}

talk("GET", "book")
.then(function(v){
  let bookdata;
  try {
    bookdata = JSON.parse(v).map(book => book.Title);  
  } catch (error) {
    console.log("unable to parse the following as JSON: " + v);
  }
  addAutoComplete("lend_book", function() {return bookdata});    
});

talk("GET", "borrower")
.then(function(v) {
  let borrowerdata;
  try {
    borrowerdata = JSON.parse(v).map(borrower => borrower.Name);  
  } catch (error) {
    console.log("unable to parse the following as JSON: " + v);
  }
  addAutoComplete("lend_borrower", function() {return borrowerdata});
});


/*
// this should clean the database

talk("GET", "flyway")
.then(function(v){
  return talk("POST", "registerborrower", "borrower=alice");
})
.then(function(v){
  return talk("GET", "borrower");  
})
.then(function(v){
  console.assert(v === '[{"Name": "alice", "Id": "1"}]', 'result was ' + v);  
  return JSON.parse(v).map(borrower => borrower.Name);
})
.then(function(v) {console.assert(v[0] === "alice", "result should be alice, was " + v[0])});

*/


   