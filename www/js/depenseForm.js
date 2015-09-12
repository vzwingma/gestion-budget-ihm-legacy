/*
 * Script de budget
 */
var depensesFormClass = {
	initialize : function(){
		$('#depenseform_validerFermer').click(depensesFormClass.validerEtFermer);
		$('#depenseform_validerContinuer').click(depensesFormClass.validerEtContinuer);
		
		
	},
	// Remplissage du formulaire
	fillFormByDepense: function(depense){
		$("#depenseform_description").val(depense.libelle);
		$("#depenseform_id").text(depense.id);
		$.each(listeCategories, function( key, categorie ) {
			$.each(categorie.listeSSCategories, function( key, sscategorie ) {
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
			
		$('#depenseform_etat-button').find('span').text(getLabelEtatDepense(depense.etat));
		
		$('#depenseform_mensuelle').prop('checked', depense.periodique);
	},
	// Ajout d'une dépense
	actionAjouterDepense: function(event){
		depensesFormClass.clearForm();
		$("#popupLogin").modal();
	},
	// Edition de la dépense
	actionEditerDepense: function(event){
		$("#popupLogin").modal({overlayClose:true});
	},
	getDepenseFromForm: function() {
		
		var typeDepense = "CREDIT";
		if($('#depenseform_type-button').find('span').text() == "-"){
			typeDepense = "DEPENSE";
		}
		var depenseForm = {
				id				: $("#depenseform_id").text(), 
				libelle			: $("#depenseform_description").val(), 
				idSSCategorie	: utilsClass.getIdFromSelectOptions("depenseform_categorie"), 
				typeDepense		: typeDepense,
				valeur			: $("#depenseform_valeur").val(),
				etat			: utilsClass.getIdFromSelectOptions("depenseform_etat"),
				periodique		: $('#depenseform_mensuelle').prop('checked')
				};
		console.log("Dépense du formulaire ", depenseForm);
		if($("#depenseform_id").text() === ""){
			// Création
			depensesClass.sendCreateDepense(depenseForm);
		}
		else{
			// Update
			depensesClass.sendUpdateDepense(depenseForm);
		}
	},
	validerEtFermer : function(){
		depensesFormClass.getDepenseFromForm();
		$.modal.close();
		depensesFormClass.clearForm();
	},
	validerEtContinuer : function(){
		$.modal.close();
		depensesFormClass.clearForm();
		$("#popupLogin").modal();
	},
	clearForm : function(){
		// Clear form
		$("#depenseform_id").text("");
		$("#depenseform_description").val("");
		$('#depenseform_categorie-button').find('span').text("");
		$('#depenseform_type-button').find('span').text("");
		$("#depenseform_valeur").val("");
	}
};
depensesFormClass.initialize();