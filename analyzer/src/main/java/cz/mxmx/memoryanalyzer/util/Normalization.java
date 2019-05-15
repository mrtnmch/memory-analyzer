package cz.mxmx.memoryanalyzer.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

/**
 * Util class to normalize different names.
 */
public class Normalization {

	/**
	 * Translates string to regex.
	 * @param namespaces Namespaces.
	 * @return Namespace regexes.
	 */
	public static Collection<String> stringToRegexNamespaces(Collection<String> namespaces) {
		Collection<String> collection = new ArrayList<>();

		namespaces.forEach(namespace -> {
			collection.add(
					namespace
							.replace(".", "\\.")
							.replace("*", ".*")
							+ "([^$]*)"
			);
		});

		return collection;
	}

	/**
	 * Normalizes a class name into human readable format.
	 * @param className Class name.
	 * @return Human readable class name.
	 */
	public static String getNormalizedClassname(String className) {
		String s = className.replace("/", ".");

		if (s.startsWith("[L")) {
			s = s.replace("[L", "");
		}

		if (s.endsWith(";")) {
			s = s.replace(";", "");
		}

		return s;
	}

	/**
	 * Translates milliseconds into a date.
	 * @param milliseconds Millisecond value.
	 * @return Date value.
	 */
	public static Date millisecondsToDate(long milliseconds) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliseconds);
		return calendar.getTime();
	}

}
