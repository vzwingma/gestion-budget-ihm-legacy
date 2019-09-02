/**
 * 
 */
package com.terrier.finances.gestion.services.admin.model;

import com.terrier.finances.gestion.communs.abstrait.AbstractAPIObjectModel;

/**
 * API /Actuator/Infos
 * @author vzwingma
 *
 */
public class Info extends AbstractAPIObjectModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1415425535189056299L;
	
	private App app;
	
	/**
	 * App Info
	 * @author vzwingma
	 *
	 */
	public class App {
		
		
		public App() {}
		
		private String version;
		private String name;
		private String description;
		/**
		 * @return the version
		 */
		public String getVersion() {
			return version;
		}
		/**
		 * @param version the version to set
		 */
		public void setVersion(String version) {
			this.version = version;
		}
		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}
		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}
		/**
		 * @return the description
		 */
		public String getDescription() {
			return description;
		}
		/**
		 * @param description the description to set
		 */
		public void setDescription(String description) {
			this.description = description;
		}
		
		
	}


	/**
	 * @return the app
	 */
	public App getApp() {
		return app;
	}

	/**
	 * @param app the app to set
	 */
	public void setApp(App app) {
		this.app = app;
	}
}
