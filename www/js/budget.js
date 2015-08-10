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

var serverPingUrl = "http://localhost:8080/gestion-budget/rest/categories/depenses";
// var serverPingUrl = "https://budget-tushkanyogik.rhcloud.com/rest/categories/depenses";
var loginmdp = "test";
var basicAuth = 'Basic dnp3aW5nbWFubjp0dXNoa2FuODI=';
var app = {
    // Application Constructor
    initialize: function() {
       categories.initialize();
    }
};


var categories = {
	initialize: function() {
		 // Appel de la liste des catégories de dépenses sur l'appli        
        $.ajax({
		  type: 'GET',
		  contentType: 'application/json',
		  dataType: 'json',
		  url: serverPingUrl,
		  username: loginmdp,
		  password: loginmdp,
		   // This the only way I can find that works to do Basic Auth with jQuery Ajax
			beforeSend: function(req) {
				req.setRequestHeader('Authorization', basicAuth);
			}
		}).then(function(data) {
			console.log('data', data);
		}, function(err) {
			console.log('Erreur lors du chargement des catégories', err);
		});
	},
	init2: function(){
		 $.get(serverPingUrl, function(data, status){
			console.log("Data: " + data + "\nStatus: " + status);
		});
	}
}

app.initialize();