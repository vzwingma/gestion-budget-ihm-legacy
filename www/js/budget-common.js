/*
 * Classe commune à toutes les pages
 */
// var rootServer = "https://budget-tushkanyogik.rhcloud.com/rest/v2"
//var rootServer = "https://budgetdev-tushkanyogik.rhcloud.com/rest/v2"
 var rootServer = "http://localhost:8080/gestion-budget/rest/v2"

// Ping
var serverPingUrl = rootServer + "/ping";
// Catégories dépenses : GET /categories/depenses
var serverCategorieUrl = rootServer + "/categories/depenses";
// Contexte utilisateur : GET /utilisateur
var serverUtilisateurUrl = rootServer + "/utilisateur";
// Budget : GET /budget/{idCompte}/{strMois}/{strAnnee}
var serverBudgetUrl = rootServer + "/budget/";
// Depenses
// Chargement de la liste de dépenses : GET /depenses/{idBudget}
var serverDepensesUrl = rootServer + "/depenses/";
// Mise à jour etat : POST : /depenses/{idBudget}/{idDepense}
// Création d'une dépense : PUT /depenses/{idBudget}


var restClass = {
	// Ajout de la BasicAuthentication à la requête
	addRequestHeader : function(req){
		restClass.addBasicAuth(req);
	},
	addBasicAuth : function(req){
		req.setRequestHeader('Authorization', 'Basic ' + btoa($.session.get('loginUser') + ":" + $.session.get('mdpUser')));
	}
}

// Affichage du nom du mois
function getLabelMois(noMois){
	if(noMois == 0){
		return "Janvier";
	}
	else if(noMois == 1){
		return "Février";
	}
	else if(noMois == 2){
		return "Mars";
	}
	else if(noMois == 3){
		return "Avril";
	}
	else if(noMois == 4){
		return "Mai";
	}
	else if(noMois == 5){
		return "Juin";
	}
	else if(noMois == 6){
		return "Juillet";
	}
	else if(noMois == 7){
		return "Aout";
	}
	else if(noMois == 8){
		return "Septembre";
	}
	else if(noMois == 9){
		return "Octobre";
	}
	else if(noMois == 10){
		return "Novembre";
	}
	else if(noMois == 11){
		return "Décembre";
	}
}
