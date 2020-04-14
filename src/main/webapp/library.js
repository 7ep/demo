function createItemForList(text, input_id) {
    let item = document.createElement('li');
    item.setAttribute("data",text);
    item.appendChild(document.createTextNode(text));

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
    return innerList;
}

function createSearchBox(input_id) {
    let searchbox = document.createElement('div');
    searchbox.setAttribute("id", input_id+"_searchbox");
    searchbox.setAttribute("class", "searchbox");
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

    function handleBlurEvent(event) {
        // if we tab from this input to another, we'll get a "relatedTarget", and that's
        // the only situation where we would want to close the searchbox - tabbing to another field.
        // so if it's null, ignore the blur event.
        if (event.relatedTarget === null) {
            return;
        } else {
            deleteSearchBox(id);
        }

    }

    function considerKillingThisModalIfOutsideClick(event) {
      if (event.target != document.getElementById(id+"searchbox")) {
        deleteSearchBox(id);
      }
    }

    document.addEventListener('keydown', considerRemovingSearchBoxOnPressingEscape);
    document.addEventListener('click', considerKillingThisModalIfOutsideClick);
    element.addEventListener('blur', handleBlurEvent);

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
            let filteredList = getdata().filter(text => text.includes(currentContent)).slice(0,5);
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

let extractData = function(value, extractor) {
  try {
    return JSON.parse(value).map(extractor).sort();
  } catch (error) {
    console.log("unable to parse the following as JSON: " + value);
  }
}


talk("GET", "book")
.then(function(v){
  let bookdata = extractData(v, book => book.Title);
  addAutoComplete("lend_book", function() {return bookdata});
});

talk("GET", "borrower")
.then(function(v) {
  let borrowerdata = extractData(v, borrower => borrower.Name);
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


   