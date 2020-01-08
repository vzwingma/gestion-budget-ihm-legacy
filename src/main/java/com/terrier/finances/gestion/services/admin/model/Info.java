/**
 * 
 */
package com.terrier.finances.gestion.services.admin.model;

import java.io.Serializable;

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
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AppInfo [name=").append(app.getName()).append("version=").append(app.getVersion()).append(", name=").append(app.getName()).append(", description=")
				.append(app.getDescription()).append("]");
		return builder.toString();
	}
	
	
	/**
	 * App Info
	 * @author vzwingma
	 *
	 */
	public class App implements Serializable {
		
		// UID
		private static final long serialVersionUID = -3934171045494468262L;
		
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
