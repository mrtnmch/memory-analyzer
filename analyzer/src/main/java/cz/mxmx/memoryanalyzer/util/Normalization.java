package cz.mxmx.memoryanalyzer.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

public class Normalization {
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

	public static Date millisecondsToDate(long milliseconds) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliseconds);
		return calendar.getTime();
	}

}
