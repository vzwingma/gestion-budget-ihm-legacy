package com.terrier.finances.gestion.business.statut.objects;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * Statut d'une dépendance
 * @author vzwingma
 *
 */
@JsonInclude(Include.NON_EMPTY)
public class StatutDependencyObject {

	// Nom 
	private DependencyName nom;
	
	// Status du module
	@JsonIgnore
	private StatutStateEnum statusObject;

	// Statut calculée avec les status des dépendances
	@JsonProperty("statut")
	private StatutStateEnum statusCompile;
	
	// Liste des dépendances
	private List<StatutDependencyObject> dependances = new ArrayList<>();
	
	// Date de la mise à jour
	private Calendar date;

	/**
	 * Création d'une dépendance
	 * @param nom
	 */
	public StatutDependencyObject(DependencyName nom){
		this.nom = nom;
		this.statusObject = StatutStateEnum.INCONNU;
		this.statusCompile = StatutStateEnum.INCONNU;
		this.date = Calendar.getInstance();
	}
	
	
	/**
	 * Mise à jour d'un statut au sein de l'objet
	 * @param nom nom de la dépendance
	 * @param statut statut
	 */
	public boolean updateStatusModule(DependencyName nom, StatutStateEnum statut){
		boolean dependencyFound = false;
		
		if(nom != null && nom.equals(this.nom)){
			this.statusObject = statut;
			dependencyFound = true;
		}
		else if(dependances != null && !dependances.isEmpty()){
			for (StatutDependencyObject statutDependencyObject : dependances) {
				dependencyFound = statutDependencyObject.updateStatusModule(nom, statut);
				if(dependencyFound){
					break;
				}
			}
		}
		if(dependencyFound){
			this.date = Calendar.getInstance();
			updateStatusCompile();
		}
		return dependencyFound;
	}
	
	/**
	 * Ajout d'une dépendance
	 * @param dependance dépendance à ajouter
	 * @param parent objet statut parent
	 */
	public void addDependency(DependencyName dependance, DependencyName parent){
		if(parent != null && this.nom.equals(parent)){
			StatutDependencyObject nlleDependance = new StatutDependencyObject(dependance);
			nlleDependance.updateStatusModule(dependance, StatutStateEnum.INCONNU);
			this.dependances.add(nlleDependance);
		}
		else if(this.dependances != null && !this.dependances.isEmpty()){
			for (StatutDependencyObject statutDependencyObject : dependances) {
				statutDependencyObject.addDependency(dependance, parent);
			}
		}
		updateStatusCompile();
	}
	
	/**
	 * Mise à jour du statut compilé
	 */
	private void updateStatusCompile(){
		// Parcours de tous les status aggrégé des dépendances
		int statutAggrege = 0;
		if(dependances != null && !dependances.isEmpty()){
			for (StatutDependencyObject statutDependencyObject : dependances) {
				if(statutDependencyObject.getStatusCompile().ordinal() >= statutAggrege){
					statutAggrege = statutDependencyObject.getStatusCompile().ordinal();
				}
			}
		}
		// et le statut de l'objet courant
		if(statusObject.ordinal() >= statutAggrege){
			statutAggrege = statusObject.ordinal();
		}
		// le statut global est la plus haute gravité
		this.statusCompile = StatutStateEnum.values()[statutAggrege];
	}
	
	
	/**
	 * @return the nom
	 */
	public DependencyName getNom() {
		return nom;
	}

	/**
	 * @param nom the nom to set
	 */
	public void setNom(DependencyName nom) {
		this.nom = nom;
	}


	/**
	 * @return the dependances
	 */
	public List<StatutDependencyObject> getDependances() {
		return dependances;
	}

	/**
	 * @param dependances the dependances to set
	 */
	public void setDependances(List<StatutDependencyObject> dependances) {
		this.dependances = dependances;
	}

	/**
	 * @return the timestamp
	 */
	public Long getTimestamp() {
		return this.date != null ? this.date.getTimeInMillis() : 0L;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
		return date != null ? sdf.format(this.date.getTime()) : "null";
	}


	/**
	 * @return the status
	 */
	public StatutStateEnum getStatusObject() {
		return statusObject;
	}


	/**
	 * @return the statusCompilee
	 */
	public StatutStateEnum getStatusCompile() {
		return statusCompile;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("StatutDependencyObject [nom=").append(nom).append(", statusObject=").append(statusObject)
				.append(", statusCompile=").append(statusCompile).append(", dependances=[");
		if(dependances!= null && !dependances.isEmpty()){
			for (StatutDependencyObject statutDependencyObject : dependances) {
				builder.append(statutDependencyObject.toString()).append(", ");
			}
		}
		builder.append("], date=").append(date).append("]");
		return builder.toString();
	}	
	
	
}
