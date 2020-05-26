package com.terrier.finances.gestion.ui.operations.edition.binder;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.terrier.finances.gestion.communs.utils.data.BudgetDateTimeUtils;
import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;

/**
 * Converteur Modèle / Présentation pour les dates (en mode édition)
 * 
 * @author vzwingma
 *
 */
public class DateOperationEditorConverter implements Converter<String, LocalDateTime> {

	//
	private static final long serialVersionUID = -3920598435421890807L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vaadin.data.Converter#convertToModel(java.lang.Object,
	 * com.vaadin.data.ValueContext)
	 */
	@Override
	public Result<LocalDateTime> convertToModel(String value, ValueContext context) {
		try {
			LocalDateTime ldt = Instant.ofEpochMilli( BudgetDateTimeUtils.DATE_DAY_HOUR_S_FORMATTER.parse(value).getTime() )
                    .atZone( ZoneId.systemDefault() )
                    .toLocalDateTime();
			return Result.ok(ldt);
		} catch (ParseException e) {
			return Result.error("Erreur : La date " + value + " n'est pas au format " + BudgetDateTimeUtils.DATE_DAY_HOUR_PATTERN);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vaadin.data.Converter#convertToPresentation(java.lang.Object,
	 * com.vaadin.data.ValueContext)
	 */
	@Override
	public String convertToPresentation(LocalDateTime value, ValueContext context) {
		return BudgetDateTimeUtils.getLibelleDate(value);
	}

}
