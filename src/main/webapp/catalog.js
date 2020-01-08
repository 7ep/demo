window.addEventListener( "load", function () {
  function sendData(formObject) {
    const XHR = new XMLHttpRequest();

    // Bind the FormData object and the form element
    const FD = new FormData( formObject );

    // Define what happens on successful data submission
    XHR.addEventListener( "load", function(event) {
      var responseArea = formObject.getElementsByClassName("responsearea")[0];
      responseArea.innerText = event.target.responseText;
    } );

    // Define what happens in case of error
    XHR.addEventListener( "error", function( event ) {
      alert( 'Oops! Something went wrong.' );
    } );


    let queryString = '';
    if (formObject.method == "get") {
        entries = [];
        for(var pair of FD.entries()) {
           entries.push(pair[0]+ '='+ pair[1]);
        }
        queryString = '?'+entries.join('&');
    }

    // Set up our request
    XHR.open( formObject.method, formObject.action + queryString);

    // The data sent is what the user provided in the form
    XHR.send( FD );
  }

  let forms = document.querySelectorAll("form");
  for (let j = 0; j < forms.length; j++){
    // Access the form element...
    let form = forms[j];

    // ...and take over its submit event.
    form.addEventListener( "submit", function ( event ) {
      event.preventDefault();
      sendData(form);
    } );
  }
} );