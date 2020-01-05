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

    // Set up our request
    XHR.open( "POST", formObject.action );

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
    console.log("taking over submit event for " + form.action);
  }



} );