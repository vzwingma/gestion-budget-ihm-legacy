/*
 * Classe commune à toutes les pages
 */
// var rootServer = "https://budget-tushkanyogik.rhcloud.com/rest"
var rootServer = "https://budgetdev-tushkanyogik.rhcloud.com/rest/v2"
// var rootServer = "http://192.168.0.17:8080/gestion-budget/rest"

// Ping
var serverPingUrl = rootServer + "/ping";
// Catégories dépenses
var serverCategorieUrl = rootServer + "/categories/depenses";
// Contexte utilisateur
var serverUtilisateurUrl = rootServer + "/utilisateur";
// Budget
var serverBudgetUrl = rootServer + "/budget/";
// Depenses
var serverDepensesUrl = rootServer + "/depenses/";


// Ajout de la BasicAuthentication à la requête
function addBasicAuth(req){
	req.setRequestHeader('Authorization', 'Basic ' + btoa($.session.get('loginUser') + ":" + $.session.get('mdpUser')));
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
