/**
 * 
 */
package com.terrier.finances.gestion.ui.communs.abstrait;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.TreeGrid;

/**
 * @author vzwingma
 * @param <D> donnée métier
 * @param <C> controleur
 */
public abstract class AbstractUITreeGridComponent<C extends AbstractUIController<?>, D> extends TreeGrid<D> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6504689977742498737L;
	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractUITreeGridComponent.class);
	
	
	private C controleur;
	


	/**
	 * Démarrage
	 */
	public void startControleur(){
		controleur = createControleurTreeGrid();
		LOGGER.info("[INIT] Controleur {} ", getControleur());
		controleur.start();
		paramComponentsOnTreeGrid();
	}
	
	/**
	 * Initialisation des composants graphiques suite au démarrage du controleur
	 */
	public abstract void paramComponentsOnTreeGrid();
	

	
	/**
	 * @return controleur associé
	 */
	public C getControleur(){
		return controleur;
	}
	
	/**
	 * @return création d'un controleur
	 */
	public abstract C createControleurTreeGrid();

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((controleur == null) ? 0 : controleur.hashCode());
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
		if (!(obj instanceof AbstractUITreeGridComponent)) {
			return false;
		}
		AbstractUITreeGridComponent<?, ?> other = (AbstractUITreeGridComponent<?, ?>) obj;
		if (controleur == null) {
			if (other.controleur != null) {
				return false;
			}
		} else if (!controleur.equals(other.controleur)) {
			return false;
		}
		return true;
	}
}
