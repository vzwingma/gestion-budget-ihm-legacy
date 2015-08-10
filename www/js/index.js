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
 
// var serverPingUrl = "https://budget-tushkanyogik.rhcloud.com/rest/ping";
var serverPingUrl = "http://localhost:8080/gestion-budget/rest/ping";
 
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
		  url: serverPingUrl,
		  success: function(msg){
			console.log("Success Ping " + msg);
		  	app.receivedPing(msg);
		  },
		  error: function(msg){
			console.log(msg);
		  	app.receivedPing(msg);
		  }
		});
    },
    receivedPing: function(returnCall){
    	console.log("Ping Server : {" +  returnCall.status + "} : " + returnCall.statusText );
    	app.updateStatus(returnCall.status == 200);
     //   console.log(returnCall);
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
};


// Redirection suite à connexion
function goPageBudget()
{
   var dirPath = dirname(location.href);
   fullPath = dirPath + "/budget.html";
   window.location=fullPath;
}

function dirname(path)
{
   return path.replace(/\\/g,'/').replace(/\/[^\/]*$/, '');
}
app.initialize();