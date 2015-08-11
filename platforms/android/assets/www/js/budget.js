/*
 * Script de budget
 */

var mois = "07";
var annee = "2015";
var idCompte; 
 
var app = {
    // Application Constructor
    initialize: function() {
		
		// Register Chargement du budget sur sélection
		$('#selectCompte').bind("change",compte.select);
		
		categories.initialize();
		utilisateur.initialize();
    }
};


//********************
// 		Catégories
//********************
var categories = {
	initialize: function() {
		 // Appel de la liste des catégories de dépenses sur l'appli        
        $.ajax({
		  type: 'GET',
		  contentType: 'application/json',
		  dataType: 'json',
		  url: serverCategorieUrl,
		  // Basic Auth with jQuery Ajax
		  beforeSend: addBasicAuth
		}).then(function(data) {
			categories.logCategories(data);
		}, function(err) {
			console.log('Erreur lors de l\'authentification', err);
			alert("Erreur d'authentification");
		});
	},
	logCategories: function(categorie){
		console.log("Catégories chargées");
		$.each(categorie, function( index, categorie ) {
			console.log( index + ": " + categorie.libelle );
			$('#ui-liste-cats').append($('<div>', { value : categorie.id }).text(categorie.libelle)); 
		});
	}
}

//********************
// 		Utilisateur
//********************
var utilisateur = {
	initialize: function() {
		 // Appel du contexte utilisateur        
        $.ajax({
		  type: 'GET',
		  contentType: 'application/json',
		  dataType: 'json',
		  url: serverUtilisateurUrl,
		  // Basic Auth with jQuery Ajax
		  beforeSend: addBasicAuth
		}).then(function(data) {
			console.log('Contexte Utilisateur : ', data);
			utilisateur.initUIFromContext(data)
		}, function(err) {
			console.log('Erreur lors du chargement du contexte utilisateur', err);
		});
	},
	initUIFromContext: function(contexte){
		// Chargement de la liste des comptes
		$.each(contexte.comptes, function(key, compte) {
			$('#selectCompte').append($('<option>', { value : compte.id }).text(compte.libelle)); 
			if(compte.defaut){
				$('#selectCompte-button').find('span').text(compte.libelle);
			}
		});
		// Sélection du compte
		compte.select(); 
	}
}
//********************
// 		Compte
//********************
var compte = {
	// Sélection du compte : Enregistrement en session et déclenchement de l'event SelectCompte
	select : function(){
		idCompte = $( "#selectCompte option:selected" ).val();
		budget.initialize();
	}
}
//********************
// 		Budget
//********************
var budget = {
	initialize : function(){
		var mois = "07";
		var annee = "2015";
		console.log("Chargement du budget ["+ idCompte +"] du " + date);
		budget.get(idCompte, mois, annee);
	},
	get: function(idCompte, mois, annee) {
		 // Appel du budget      
        $.ajax({
		  type: 'GET',
		  contentType: 'application/json',
		  dataType: 'json',
		  url: serverBudgetUrl + idCompte + "/"+ mois + "/" + annee,
		  // Basic Auth with jQuery Ajax
		  beforeSend: addBasicAuth
		}).then(function(data) {
			console.log('Budget : ', data);
			var idBudget = data.id;
			depenses.get(idBudget);
		}, function(err) {
			console.log('Erreur lors du chargement du budget', err);
		});
	}
}


//********************
// 		Depenses
//********************
var depenses = {
	get: function(idBudget) {
		 // Appel du budget      
        $.ajax({
		  type: 'GET',
		  contentType: 'application/json',
		  dataType: 'json',
		  url: serverDepensesUrl + idBudget,
		  // Basic Auth with jQuery Ajax
		  beforeSend: addBasicAuth
		}).then(function(data) {
			depenses.logDepenses(data);
		}, function(err) {
			console.log('Erreur lors du chargement des dépenses', err);
		});
	},
	logDepenses: function(depenses){
		console.log("Depenses chargées");
		$.each(depenses, function( index, depense ) {
			console.log( index + ": " + depense.libelle );
			$('#ui-liste-depenses').append($('<div>', { value : depense.id }).text(depense.libelle)); 
		});
	}
}



app.initialize();

