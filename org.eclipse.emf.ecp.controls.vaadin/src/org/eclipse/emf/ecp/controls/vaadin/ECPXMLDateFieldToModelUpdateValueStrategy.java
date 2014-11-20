/*******************************************************************************
 * Copyright (c) 2014 Dennis Melzer and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Dennis - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.controls.vaadin;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.core.databinding.conversion.IConverter;

/**
 * XMLGregorianCalendar converter.
 *
 * @author Dennis Melzer
 *
 */
public class ECPXMLDateFieldToModelUpdateValueStrategy extends ECPTextFieldToModelUpdateValueStrategy {

	/**
	 * Default Constructor.
	 */
	public ECPXMLDateFieldToModelUpdateValueStrategy() {
		final IConverter converter = getConverter();
		setConverter(converter);
	}

	private IConverter getConverter() {
		return new IConverter() {

			@Override
			public Object getToType() {
				return XMLGregorianCalendar.class;
			}

			@Override
			public Object getFromType() {
				return Date.class;
			}

			@Override
			public Object convert(Object value) {
				final DateFormat dateInstance = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
				try {
					return convertDate(value, dateInstance);
				} catch (final ParseException ex) {
					ex.printStackTrace();
				} catch (final DatatypeConfigurationException e) {
					e.printStackTrace();
				}
				return value;
			}

		};
	}

	private XMLGregorianCalendar convertDate(Object value, final DateFormat dateInstance)
		throws ParseException, DatatypeConfigurationException {
		final Date date = dateInstance.parse(dateInstance.format(value));

		final Calendar targetCal = Calendar.getInstance();
		targetCal.setTime(date);
		final XMLGregorianCalendar cal = DatatypeFactory.newInstance().newXMLGregorianCalendar();
		cal.setYear(targetCal.get(Calendar.YEAR));
		cal.setMonth(targetCal.get(Calendar.MONTH) + 1);
		cal.setDay(targetCal.get(Calendar.DAY_OF_MONTH));

		cal.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
		return cal;
	}
}
