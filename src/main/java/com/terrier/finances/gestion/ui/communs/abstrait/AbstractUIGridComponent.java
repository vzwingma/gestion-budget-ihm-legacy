/**
 * 
 */
package com.terrier.finances.gestion.ui.communs.abstrait;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Grid;


/**
 * @author vzwingma
 * @param <D> donnée métier
 * @param <C> controleur
 */
public abstract class AbstractUIGridComponent<C extends AbstractUIController<?>, D> extends Grid<D> {


	/**
	 * 
	 */
	private static final long serialVersionUID = -6504689977742498737L;
	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractUIGridComponent.class);
	
	private C controleurGrid;
	


	/**
	 * Démarrage
	 */
	public void startControleur(){
		controleurGrid = createControleurGrid();
		LOGGER.info("[INIT] Controleur {}", getControleur());
		controleurGrid.start();
		paramComponentsOnGrid();
	}
	
	/**
	 * Initialisation des composants graphiques suite au démarrage du controleur
	 */
	public abstract void paramComponentsOnGrid();
	
	/**
	 * @return controleur associé
	 */
	public C getControleur(){
		return controleurGrid;
	}
	
	/**
	 * @return création d'un controleur
	 */
	public abstract C createControleurGrid();

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((controleurGrid == null) ? 0 : controleurGrid.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof AbstractUIGridComponent)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		AbstractUIGridComponent<AbstractUIController<?>, D> other = (AbstractUIGridComponent<AbstractUIController<?>, D>) obj;
		if (controleurGrid == null) {
			if (other.controleurGrid != null) {
				return false;
			}
		} else if (!controleurGrid.equals(other.controleurGrid)) {
			return false;
		}
		return true;
	}
	
	
}
