package com.terrier.finances.gestion.ui.communs;

import com.terrier.finances.gestion.services.FacadeServices;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;


/**
 * Used to show events.
 * 
 * @author vzwingma
 */
public final class InformationDialog extends Window implements Button.ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 75321576037631582L;
	private static final int ONE_HUNDRED_PERCENT = 100;
	private static final int INFORMATION_DIALOG_WIDTH = 400;
	private static final int INFORMATION_DIALOG_HEIGHT = 150;

	private final transient InformationDialogCallback callback;
	private final Button okButton;

	private VerticalLayout mainLayout;
	
	/**
	 * * Constructor for configuring confirmation dialog. * @param caption the
	 * dialog caption. * @param question the question. * @param okLabel the Ok
	 * button label. * @param cancelLabel the cancel button label. * @param
	 * callback the callback.
	 */
	public InformationDialog(final String caption, final String message, final String buttonLabel, final InformationDialogCallback callback) {

		super(caption);

		okButton = new Button(buttonLabel, this);
		okButton.setStyleName("friendly");
		setModal(true);
		setResizable(false);

		setContent(buildMainLayout());
		this.callback = callback;

		Label label = new Label(message, ContentMode.HTML);

		if (message != null) {
			mainLayout.addComponent(label);
			mainLayout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
		}

		final HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);
		buttonLayout.addComponent(okButton);
		mainLayout.addComponent(buttonLayout);

		((VerticalLayout) getContent()).setHeight(ONE_HUNDRED_PERCENT, Unit.PERCENTAGE);
		((VerticalLayout) getContent()).setComponentAlignment(buttonLayout, Alignment.MIDDLE_CENTER);
		setWidth(INFORMATION_DIALOG_WIDTH, Unit.PIXELS);
		setHeight(INFORMATION_DIALOG_HEIGHT, Unit.PIXELS);
	}
	

	
	/**
	 * @return layout
	 */
	private VerticalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();
		mainLayout.setMargin(true);
		// top-level component properties
		setSizeFull();
		return mainLayout;
	}


	/** 
	 * Event handler for button clicks. 
	 * @param event the click event. */
	public void buttonClick(final ClickEvent event) {
		if (getParent() != null) {
			FacadeServices.get().getServiceUserSessions().getSession().getPopupModale().close();
		}
		callback.response();
	}

	/** * Interface for information dialog callbacks. */
	public interface InformationDialogCallback {

		/** * The user action */
		void response();
	}
}