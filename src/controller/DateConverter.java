package controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.util.StringConverter;

public class DateConverter extends StringConverter<LocalDate> {

	private final String pattern = "yyyy-MM-dd";
	private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

	@Override
	public String toString(LocalDate date) {
		if (date != null) {
			return dateFormatter.format(date);
		} else {
			return "";
		}
	}

	@Override
	public LocalDate fromString(String dateString) {
		if (dateString != null && !dateString.isEmpty()) {
			return LocalDate.parse(dateString, dateFormatter);
		} else {
			return null;
		}
	}
}
