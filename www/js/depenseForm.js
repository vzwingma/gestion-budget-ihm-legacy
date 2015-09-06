/*
 * Script de budget
 */
// Liste des catégories
var listeCategories;
// id Dépense Selectionnée
var idDepenseSelectionnee = "83e600b0-52a9-4ca8-bc57-9ec7bc7e1b8a";
var depenseSelectionnee;
var listeDepenses;
 
var appForm = {
    // Application Constructor
    initialize: function() {

		// Init des classes
		categoriesClass.initialize();
		depensesFormClass.get("ingdirect_2015_9");
		setTimeout(function(){
			depenseSelectionnee = depensesFormClass.findDepenseById("83e600b0-52a9-4ca8-bc57-9ec7bc7e1b8a");
			depensesFormClass.fillFormByDepense(depenseSelectionnee);
		}, 2000);
		
    }
};


//********************
// 		Catégories
//********************
var categoriesClass = {
	initialize: function() {
		 // Appel de la liste des catégories de dépenses sur l'appli        
        $.ajax({
		  type: 'GET',
		  contentType: 'application/json',
		  dataType: 'json',
		  url: serverCategorieUrl,
		  // Basic Auth with jQuery Ajax
		  beforeSend: restClass.addRequestHeader
		}).then(function(data) {
			// Affectation des catégories
			listeCategories = data;
			categoriesClass.logCategories(data);
		}, function(err) {
			console.log('Erreur lors de l\'authentification', err);
			alert("Erreur d'authentification");
		});
	},
	logCategories: function(categorie){
		console.log("Catégories chargées");
		console.log(categorie);
		$.each(categorie, function( index, categorie ) {
			console.log( index + ": " + categorie.libelle );
			$('#ui-liste-cats').append($('<div>', { value : categorie.id }).text(categorie.libelle)); 
		});
	},
	findCategorieById: function(idCategorie){
		categorie = $.grep(listeCategories, function(categorie){ 
														return categorie.id === idCategorie; 
														});	
	//	console.log("Recherche de la categorie [" +idCategorie+ "] = " + categorie[0]);
		return categorie[0];
	},
	findSSCategorieById: function(idSSCategorie){
		var ss_categorie;
		$.each(listeCategories, function( key, categorie ) {
			$.each(categorie.listeSSCategories, function( key, sscategorie ) {
				if(sscategorie.id === idSSCategorie){
					ss_categorie = sscategorie;
					return;
				};
			})
		});
		// console.log("Recherche de la sous categorie [" +idSSCategorie+ "] = " + ss_categorie.libelle);
		return ss_categorie;
	}
}

var listeDepenses;
//********************
// 		Depenses
//********************
var depensesClass = {
	//********************
	// Chargement
	//********************
	get: function(idBudget) {
		 // Appel de chargement des lignes de dépenses
        $.ajax({
		  type: 'GET',
		  contentType: 'application/json',
		  dataType: 'json',
		  url: serverBudgetUrl + idBudget + serviceListeDepenses,
		  // Basic Auth with jQuery Ajax
		  beforeSend: restClass.addRequestHeader
		}).then(function(data) {
			console.log("Depenses chargées : ", data);
			listeDepenses = data;
			
		}, function(err) {
			console.log('Erreur lors du chargement des dépenses', err);
			alert('Erreur lors du chargement des dépenses');
		});
	},
	//********************
	// Actions métier
	//********************
	findDepenseById: function(idDepense){
		var depenseCherchee = $.grep(listeDepenses, function(depense){ 
														return depense.id === idDepense; 
														});	
		console.log("Recherche de la dépense [" +idDepense+ "]", depenseCherchee);
		return depenseCherchee[0];
	}
};

var depensesFormClass = {
	// Remplissage du formulaire
	fillFormByDepense: function(depense){
		console.log(depense.id);	
		$("#depenseform_description").val(depense.libelle);
		
		$.each(listeCategories, function( key, categorie ) {
			$.each(categorie.listeSSCategories, function( key, sscategorie ) {
				$('#depenseform_categorie').append($('<option>', { value : sscategorie.id }).text(categorie.libelle + " - " + sscategorie.libelle)); 
				if(sscategorie.id === depense.idSSCategorie){
					$('#depenseform_categorie-button').find('span').text(categorie.libelle + " - " + sscategorie.libelle);
				};
			})
		});
		typeDepense = "+";
		if(depense.typeDepense == "DEPENSE"){
			typeDepense = "-";
		}
		$('#depenseform_type-button').find('span').text(typeDepense);
		$("#depenseform_valeur").val(depense.valeur);
		
		$('#depenseform_etat-button').find('span').text(typeDepense);
		
		$('#depenseform_mensuelle').prop('checked', depense.periodique);
	}
}

// appForm.initialize();