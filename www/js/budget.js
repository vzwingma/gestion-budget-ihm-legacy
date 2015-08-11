/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

var app = {
    // Application Constructor
    initialize: function() {
       categories.initialize();
	   utilisateur.initialize();
    }
};


// Chargement des catégories
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
			console.log('Erreur lors du chargement des catégories', err);
			alert("Erreur d'authentification");
		});
	},
	logCategories: function(categorie){
		console.log("Catégories chargées");
		$.each(categorie, function( index, value ) {
			console.log( index + ": " + value.libelle );
		});
	}
}

// Contexte utilisateur
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
		}, function(err) {
			console.log('Erreur lors du chargement du contexte utilisateur', err);
		});
	},
}



app.initialize();