/*
 * Script de budget
 */
// Compte
var mois;
var annee;
var idCompte; 
// Budget
var budgetCourant;
// Liste des catégories
var listeCategories;
// id Dépense Selectionnée
var idDepenseSelectionnee;
 
var app = {
    // Application Constructor
    initialize: function() {
		
		// Register Chargement du budget sur sélection
		$('#selectCompte').bind("change",compteClass.select);
		// Register des swipe
		$(document).on( "swipeleft", app.swipeHandler(-1) );
		$(document).on( "swiperight", app.swipeHandler(+1) );
		$('ui-totaux').on( "swipeleft", app.swipeHandler(-1) );
		$('ui-totaux').on( "swiperight", app.swipeHandler(+1) );
		// Init des classes
		categoriesClass.initialize();
		utilisateurClass.initialize();
		buttonsActionClass.initialize();
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
		  beforeSend: restClass.addRequestHeader
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
		
		// Désactivation des boutons
		buttonsActionClass.desactivate();
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
		  beforeSend: restClass.addRequestHeader
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
	budgetCharge: function(budget){
		budgetCourant = budget;
				// Chargement des dépenses
		depensesClass.get(budgetCourant.id);
		// Mise à jour du tableau
		ResumeTotauxClass.updateTableau(budget);
		// Maj du bouton si actif
		$('#buttonAjouter').prop('disabled', !budget.actif);
		// lastaccess
		var lastAccess = new Date(budget.dateMiseAJour);
		$('#lastaccess').text("Dernier accès : " + lastAccess.getDay() + "/" + lastAccess.getMonth() + "/" + lastAccess.getFullYear() + " " + lastAccess.getHours() + ":"+ lastAccess.getMinutes());
		// Date du mois
		
		$('#nowMois').text('Au ' + new Date(budget.dateMiseAJour).getDay() + ' ' + getLabelMois(mois));
		$('#finMois').text('Fin ' + getLabelMois(mois));
	}
}

//********************
// 		Totaux
//********************
var ResumeTotauxClass = {
	updateTableau: function(budget){
		var valeurTotal = parseFloat(budget.nowArgentAvance).toFixed(2);
		var classTotal = (valeurTotal > 0) ? "ui-totaux-credit" : "ui-totaux-debit";
		$('#nowArgentAvance').html(valeurTotal + " €");
		$('#nowArgentAvance').prop('class', classTotal);

		valeurTotal = parseFloat(budget.nowCompteReel).toFixed(2);
		classTotal = (valeurTotal > 0) ? "ui-totaux-credit" : "ui-totaux-debit";
		$('#nowCompteReel').html(valeurTotal + " €");
		$('#nowCompteReel').prop('class', classTotal);
		
		valeurTotal = parseFloat(budget.finArgentAvance).toFixed(2);
		classTotal = (valeurTotal > 0) ? "ui-totaux-credit" : "ui-totaux-debit";
		$('#finArgentAvance').html(valeurTotal + " €");
		$('#finArgentAvance').prop('class', classTotal);

		valeurTotal = parseFloat(budget.finCompteReel).toFixed(2);
		classTotal = (valeurTotal > 0) ? "ui-totaux-credit" : "ui-totaux-debit";
		$('#finCompteReel').html(valeurTotal + " €");
		$('#finCompteReel').prop('class', classTotal);

		valeurTotal = parseFloat(budget.margeSecuriteFinMois).toFixed(2);
		classTotal = (valeurTotal > 0) ? "ui-totaux-credit" : "ui-totaux-debit";
		$('#margeSecuriteFinMois').html(valeurTotal + " €");
		$('#margeSecuriteFinMois').prop('class', classTotal);

		valeurTotal = parseFloat(budget.margeSecurite).toFixed(2);
		classTotal = (valeurTotal > 0) ? "ui-totaux-credit" : "ui-totaux-debit";
		$('#margeSecurite').html(valeurTotal + " €");
		$('#margeSecurite').prop('class', classTotal);
	}
}


//********************
// 		Depenses
//********************
var depensesClass = {
	//********************
	// Chargement
	//********************
	get: function(idBudget) {
		idDepenseSelectionnee = null;
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
			listeDepenses=data;
			depensesClass.fillTableauDepenses(data);
		}, function(err) {
			$('#table-liste-depenses').empty();
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
	},
	//********************
	// Mise à jour d'une dépense
	//********************
	updateDepense : function(idDepense, etat){
		var depenseUpdate = depensesClass.findDepenseById(idDepense);
		depenseUpdate.etat = etat;
		
		// tess
		if(etat === "SUPPRIMER"){
			$(document.body).simpledialog2({
				mode: 'button',
				headerText: 'Suppression', headerClose: false,
				buttonPrompt: 'Etes vous sûr de vouler supprimer la dépense ?',
				buttons : {
					'Oui': {
						click: function () { 
							depensesClass.sendUpdateDepense(depenseUpdate);
						}
					},
					'Non': {
						click: function () { },icon: "delete"
					}
				}
			});
		}
		else{
			depensesClass.sendUpdateDepense(depenseUpdate);
		}
	},
	sendUpdateDepense : function(depenseUpdate){
		console.log("Envoi de " + JSON.stringify(depenseUpdate));
		 // Appel de la mise à jour du statut de la dépense
        $.ajax({
		  type: 'PUT',
		  contentType: 'application/json',
		  data: JSON.stringify(depenseUpdate),
		 // type attendu dataType: 'json',
		  url: serverBudgetUrl + budgetCourant.id + serviceDepense + depenseUpdate.id,
		  // Basic Auth with jQuery Ajax
		  beforeSend: restClass.addRequestHeader
		}).then(function(data) {
			// Mise à jour de la dépense dans le tableau des données
			
			// et sur l'affichage
			if(depenseUpdate.etat == "SUPPRIMER"){
				$('#' + depenseUpdate.id).remove();
				var indexDepenseToDelete = -1;
				$.each(listeDepenses, function( index, depense ) {
					if(depense.id === depenseUpdate.id){
						indexDepenseToDelete = index;
					}
				});
				if(indexDepenseToDelete > -1){
					listeDepenses.splice( indexDepenseToDelete, 1 );
				}
				depensesClass.unselectDepenses();
				console.log("Dépense supprimée",listeDepenses );
			}
			else{
				$('#' + depenseUpdate.id).prop('class', depensesClass.getClassLigneDepenseByEtat(depenseUpdate));
				console.log("Dépense mise à jour",listeDepenses );
			}
		}, function(err) {
		//	$('#table-liste-depenses').empty();
			console.log('Erreur lors de la mise à jour de la dépense', err);
			alert('Erreur lors de la mise à jour de la dépense');
		});
	},
	//********************
	// Affichage
	//********************
	selectDepense: function(idDepense){
		var etatDepense = depensesClass.findDepenseById(idDepense).etat;
		console.log("Selection de la dépense : " + idDepense + ":", etatDepense);
		idDepenseSelectionnee = idDepense;
		// Activation des boutons
		buttonsActionClass.activate(etatDepense);
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
	// Désactivation de dépenses dans le tableau
	unselectDepenses : function(){
		buttonsActionClass.desactivate();
		idDepenseSelectionnee = null;
	},
	fillTableauDepenses: function(depenses){
		$('#table-liste-depenses').empty();
		/* Itération du tableau des dépenses */
		$.each(depenses, function( index, depense ) {
			$('#table-liste-depenses').append($('<tr>', { id : depense.id , class : depensesClass.getClassLigneDepenseByEtat(depense), type: depense.etat}));
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
	},
	getClassLigneDepenseByEtat : function (depense){
		var classDepense = 'ui-ligne-depenses ';
		if(depense.derniereOperation){
			classDepense += 'ui-depense-last ';
		}
		classDepense += 'ui-depense-' + depense.etat;
		return classDepense;
	}
}

//********************
//	Boutons Actions
//********************
var buttonsActionClass = {
	initialize: function(){
		// Register des clicks
		$('#buttonRealiser').click(buttonsActionClass.actionDepense);
		$('#buttonPrevu').click(buttonsActionClass.actionDepense);
		$('#buttonAnnuler').click(buttonsActionClass.actionDepense);
		$('#buttonReporter').click(buttonsActionClass.actionDepense);
		$('#buttonSupprimer').click(buttonsActionClass.actionDepense);
		$('#buttonEditer').click(buttonsActionClass.editDepense);
		$('#buttonAjouter').click(buttonsActionClass.ajoutDepense);
	},
	// Désactivation de tous les boutons
	desactivate: function(){
		// Suppression des boutons d'actions sur dépenses à l'init
		$('#buttonRealiser').prop('disabled', true);
		$('#buttonPrevu').prop('disabled', true);
		$('#buttonAnnuler').prop('disabled', true);
		$('#buttonReporter').prop('disabled', true);
		$('#buttonSupprimer').prop('disabled', true);
		$('#buttonEditer').prop('disabled', true);
	},
	// Activation des boutons suivant l'état de la dépense sélectionnée
	activate: function(etatDepense){
		$('#buttonRealiser').prop('disabled', etatDepense == "REALISEE");
		$('#buttonPrevu').prop('disabled', etatDepense == "PREVUE");
		$('#buttonAnnuler').prop('disabled', etatDepense == "ANNULEE");
		$('#buttonReporter').prop('disabled', etatDepense == "REPORTEE");
		$('#buttonSupprimer').prop('disabled', false);
		$('#buttonEditer').prop('disabled', false);

	},
	actionDepense: function(event){
		var action = $('#'+event.currentTarget.id).attr('action');
		console.log("Action " + action + " sur la dépense " + idDepenseSelectionnee);
		depensesClass.updateDepense(idDepenseSelectionnee, action);
	},
	ajoutDepense: function(event){
		$('#edit_budget_form').load("depense.html");
		$("#popupLogin").modal();
		alert(idDepenseSelectionnee);
	},
	editDepense: function(event){
		$('#edit_budget_form').load("depense.html");
		var depenseSelectionnee = depensesClass.findDepenseById(idDepenseSelectionnee);
		setTimeout(function(){
			depensesFormClass.fillFormByDepense(depenseSelectionnee);
			$("#popupLogin").modal();
		}, 500);
	}
}
app.initialize();