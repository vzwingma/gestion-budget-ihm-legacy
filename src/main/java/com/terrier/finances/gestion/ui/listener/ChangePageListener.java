package com.terrier.finances.gestion.ui.listener;

import com.terrier.finances.gestion.ui.components.budget.mensuel.BudgetMensuelPage;
import com.terrier.finances.gestion.ui.components.stats.StatistiquesPage;
import com.terrier.finances.gestion.ui.controler.common.AbstractComponentListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;

/**
 * Listener de navigation entre les pages
 * @author vzwingma
 *
 */
public class ChangePageListener extends AbstractComponentListener implements ClickListener{


	/**
	 * 
	 */
	private static final long serialVersionUID = -6573682471450033530L;

	/**
	 * Destination 
	 */
	private Class<? extends Component> pageDestination;
	
	public ChangePageListener(Class<? extends Component> pageDestination){
		this.pageDestination = pageDestination;
	}
	
	/* (non-Javadoc)
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		Layout mainLayout = getUISession().getMainLayout();
		
		mainLayout.removeAllComponents();
		if(this.pageDestination.equals(StatistiquesPage.class)){
			
			BudgetMensuelPage page = (BudgetMensuelPage)event.getButton().getParent().getParent().getParent().getParent();
			mainLayout.addComponent(new StatistiquesPage((String)page.getComboBoxComptes().getConvertedValue()));			
		}
		else if(this.pageDestination.equals(BudgetMensuelPage.class)){
			
			StatistiquesPage page = (StatistiquesPage)event.getButton().getParent().getParent().getParent().getParent();
			mainLayout.addComponent(new BudgetMensuelPage((String)page.getComboBoxComptes().getConvertedValue()));			
		}
	}
}
