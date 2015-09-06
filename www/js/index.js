/*
 * JS pour la page de login
 */

 // Initialisation de la page, lorsque le device est ready et le ping serveur est OK : Activation de la connexion
var app = {
    // Application Constructor
    initialize: function() {
        this.bindEvents();
    },
    // Bind Event Listeners
    //
    // Bind any events that are required on startup. Common events are:
    // 'load', 'deviceready', 'offline', and 'online'.
    bindEvents: function() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
    },
    // deviceready Event Handler
    //
    // The scope of 'this' is the event. In order to call the 'receivedEvent'
    // function, we must explicitly call 'app.receivedEvent(...);'
    onDeviceReady: function() {
        app.receivedEvent('deviceready');
    },
    // Update DOM on a Received Event
    receivedEvent: function(id) {   
        console.log('Received Event: ' + id);
		// Appel de Ping sur l'appli        
        $.ajax({
		  type: "GET",
		  contentType: 'text/plain',
		  dataType: 'text',
		  url: serverPingUrl,
		  success: function(msg){
			console.log("Success Ping : " + msg);
		  	app.updateStatus(true);
			app.getCookieAuth();
		  },
		  error: function(returnCall){
			console.log("Ping Server : {" +  returnCall.status + "} : " + returnCall.statusText );
		  	app.updateStatus(false);
		  }
		});
    },
    updateStatus: function(status){
        var parentElement = document.getElementById('deviceready');
        var listeningElement = parentElement.querySelector('.listening');
        var receivedElement = parentElement.querySelector('.received');
		var errorElement = parentElement.querySelector('.error');
		document.getElementById('loginButton').disabled = !status;
		if(status){
	        listeningElement.setAttribute('style', 'display:none;');
	        receivedElement.setAttribute('style', 'display:block;');
	        
	    }
        else{
	        listeningElement.setAttribute('style', 'display:none;');
	        errorElement.setAttribute('style', 'display:block;');
        }
    },
	getCookieAuth : function(){
		// Enregistrement en session des valeurs
		var loginUser = $.session.get('loginUser');
		var mdpUser = $.session.get('mdpUser');
		$( "#login" ).val(loginUser);
		$( "#mdp" ).val(mdpUser);
	}
};


// Chargement des catégories
var authenticationClass = {
	login: function() {
        var user = $( "#login" ).val();
        var mdp = $( "#mdp" ).val();
        user = "vzwingmann";
        mdp = "tushkan82";
        
		// Enregistrement en session des valeurs
		$.session.set('loginUser', user);
		$.session.set('mdpUser', mdp);
	
		 // Appel de la liste des catégories de dépenses sur l'appli        
        $.ajax({
		  type: 'GET',
		  contentType: 'application/json',
		  dataType: 'json',
		  url: serverCategorieUrl,
		  // Basic Auth with jQuery Ajax
		  beforeSend: restClass.addRequestHeader
		}).then(function(data) {
			console.log('Authentification OK', data);
			authenticationClass.goPageBudget();
		}, function(err) {
			console.log('Erreur lors du chargement des catégories', err);
			alert("Erreur d'authentification");
		});
	},
	goPageBudget: function(){
		var dirPath = location.href.replace(/\\/g,'/').replace(/\/[^\/]*$/, '');
		fullPath = dirPath + "/budget.html";
		window.location=fullPath;
	}
}

// Register OnClick sur login
$('#loginButton').click(authenticationClass.login);
// Init de la page
app.initialize();
