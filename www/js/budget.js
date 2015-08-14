/*
 * Script de budget
 */

var mois;
var annee;
var idCompte; 
var budgetCourant;
// Liste des catégories
var listeCategories;
 
var app = {
    // Application Constructor
    initialize: function() {
		
		// Register Chargement du budget sur sélection
		$('#selectCompte').bind("change",compteClass.select);
		// Register des swipe
		$(document).on( "swipeleft", app.swipeHandler(-1) );
		$(document).on( "swiperight", app.swipeHandler(+1) );
		categoriesClass.initialize();
		utilisateurClass.initialize();
    },
	swipeHandler : function(sens) {
		if(!mois == NaN){
			alert("Chargmeent de " + (mois + sens));
		}
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
		  beforeSend: addBasicAuth
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
	//	console.log("Recherche de la categorie [" +idCategorie+ "] = " + categorie[0].libelle);
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

//********************
// 		Utilisateur
//********************
var utilisateurClass = {
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
			utilisateurClass.initUIFromContext(data)
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
		compteClass.select(); 
	}
}
//********************
// 		Compte
//********************
var compteClass = {
	// Sélection du compte : Enregistrement en session et déclenchement de l'event SelectCompte
	select : function(){
		idCompte = $( "#selectCompte option:selected" ).val();
		budgetClass.initialize();
	}
}
//********************
// 		Budget
//********************
var budgetClass = {
	initialize : function(){
		var aujourdhui = new Date();
		mois = aujourdhui.getMonth();
		var strMois = "" + mois;
		if(mois < 10){
			strMois = "0"+mois;
		}
		annee = ""+ aujourdhui.getFullYear();
		console.log("Chargement du budget ["+ idCompte +"] du " + strMois + "/" + annee);
		// Affichage de la date
		$( "#date_courante" ).text(getLabelMois(strMois) + " " + annee);
		
		// Suppression des boutons d'actions sur dépenses à l'init
		$('#buttonRealiser').prop('disabled', true);
		$('#buttonPrevu').prop('disabled', true);
		$('#buttonAnnuler').prop('disabled', true);
		$('#buttonReporter').prop('disabled', true);
		$('#buttonSupprimer').prop('disabled', true);
		$('#buttonEditer').prop('disabled', true);
		// Chargement du budget
		
		budgetClass.get(idCompte, strMois, annee);
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
			// Chargement du budget
			console.log('Budget : ', data);
			budgetClass.budgetCharge(data);
		}, function(err) {
			console.log('Erreur lors du chargement du budget', err);
			$('#table-liste-depenses').empty();
			alert('Erreur lors du chargement du budget');
		});
	},
	budgetCharge(budget){
		budgetCourant = budget;
		// Chargmeent des dépenses
		depensesClass.get(budgetCourant.id);
		// Maj du bouton si actif
		$('#buttonAjouter').prop('disabled', !budget.actif);
	}
}


//********************
// 		Depenses
//********************
var depensesClass = {
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
			console.log("Depenses chargées");
			console.log(data);
			listeDepenses=data;
			depensesClass.fillTableauDepenses(data);
		}, function(err) {
			$('#table-liste-depenses').empty();
			console.log('Erreur lors du chargement des dépenses', err);
			alert('Erreur lors du chargement des dépenses');
		});
	},
	findDepenseById: function(idDepense){
		depense = $.grep(listeDepenses, function(depense){ 
														return depense.id === idDepense; 
														});	
		console.log("Recherche de la dépense [" +idDepense+ "] = " + depense[0].libelle);
		return depense[0];
	},
	selectDepense: function(idDepense){
		var etatDepense = depensesClass.findDepenseById(idDepense).etat;
		console.log("Actions sur la dépense : " + idDepense + "::"+etatDepense);
		// Activation des boutons
		$('#buttonRealiser').prop('disabled', etatDepense == "REALISEE");
		$('#buttonPrevu').prop('disabled', etatDepense == "PREVUE");
		$('#buttonAnnuler').prop('disabled', etatDepense == "ANNULEE");
		$('#buttonReporter').prop('disabled', etatDepense == "REPORTEE");
		$('#buttonSupprimer').prop('disabled', false);
		$('#buttonEditer').prop('disabled', false);
		// Ancienne ligne sélectionnée => Désélectionnée
		if(!($("#table-liste-depenses").find('.ui-depense-SELECTIONNEE')[0] === undefined )){
			idLigne = $("#table-liste-depenses").find('.ui-depense-SELECTIONNEE')[0].id;
			// Remplacement de SELECTIONNE par le type de dépense
			var reclassDepense = $('#'+ idLigne).prop('class').replace(/SELECTIONNEE/g, $('#'+ idLigne).attr('type'));
			$('#'+ idLigne).prop('class', reclassDepense);
		}
		// Ligne sélectionnée
		var classeDepense = $('#'+ idDepense).prop('class');
		//$('#'+ idDepense).prop('class', 'ui-ligne-depenses .ui-depense-SELECTIONNEE');
		classeDepense = classeDepense.replace(/REALISEE/g, "SELECTIONNEE");
		classeDepense = classeDepense.replace(/PREVUE/g, "SELECTIONNEE");
		classeDepense = classeDepense.replace(/ANNULEE/g, "SELECTIONNEE");
		classeDepense = classeDepense.replace(/REPORTEE/g, "SELECTIONNEE");
		$('#'+ idDepense).prop('class', classeDepense);

	},
	fillTableauDepenses: function(depenses){
		$('#table-liste-depenses').empty();
		/* Itération du tableau des dépenses */
		$.each(depenses, function( index, depense ) {
			var classDepense = 'ui-ligne-depenses ';
			if(depense.derniereOperation){
				classDepense += 'ui-depense-last ';
			}
			classDepense += 'ui-depense-' + depense.etat;
			$('#table-liste-depenses').append($('<tr>', { id : depense.id , class : classDepense, type: depense.etat}));
			var valeur = depense.valeur + " &euro;";
			if(depense.typeDepense == 'DEPENSE'){
				valeur = "- " + valeur;
			}
			$('#'+depense.id)
				.append($('<td>', {class: "ui-tab-depenses"}).text(categoriesClass.findCategorieById(depense.idCategorie).libelle))			
				.append($('<td>', {class: "ui-tab-depenses"}).text(categoriesClass.findSSCategorieById(depense.idSSCategorie).libelle))
				.append($('<td>', {class: "ui-tab-depenses"}).text(depense.libelle))
				.append($('<td>', {class: 'ui-tab-depenses ui-depense-' + depense.typeDepense}).html(valeur))
				; 
		});
		$('#table-liste-depenses tr').click(function(event) {
            depensesClass.selectDepense(this.id);
        });
	}
}

app.initialize();