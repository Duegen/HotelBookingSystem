package cli;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

public interface Inputoutput {
	String inputString(String prompt);

	void output(Object obj);

	default Integer inputInteger(String prompt) {
		return inputObject(prompt, "It's not a number", s -> {
			try {
				Integer res = Integer.parseInt(s);
				return res;
			} catch (Exception e) {

				return null;
			}
		});
	}

	default Integer inputInteger(String prompt, Integer min, Integer max) {
		return inputObject(prompt + " [" + min + ", " + max + "]",
				String.format("It's not a number in range [%d-%d]", min, max), s -> {
					try {
						Integer res = Integer.parseInt(s);
						return res >= min && res <= max ? res : null;
					} catch (Exception e) {

						return null;
					}
				});
	}

	default Double inputDouble(String prompt) {
		return inputObject(prompt, "It's not a double number", s -> {
			try {
				Double res = Double.parseDouble(s);
				return res;
			} catch (Exception e) {

				return null;
			}
		});
	}

	default Long inputLong(String prompt) {
		return inputObject(prompt, "It's not a long number", s -> {
			try {
				Long res = Long.parseLong(s);
				return res;
			} catch (Exception e) {

				return null;
			}
		});
	}

	default String inputString(String prompt, List<String> options) {
		return inputObject(String.format("%s %s", prompt, options), "String is not in options",
				s -> options.contains(s) ? s : null);
	}

	default LocalDate inputDate(String prompt, String format) {
		return inputObject(prompt + " " + format, "Wrong date " + format, s -> {
			try {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
				return LocalDate.parse(s, dtf);
			} catch (Exception e) {
				return null;
			}

		});
	}

	default <R> R inputObject(String prompt, String error, Function<String, R> mapper) {
		while (true) {
			String text = inputString(prompt);
			if (text == null)
				return null;
			R res = mapper.apply(text);
			if (res != null)
				return res;
			outputlLine(error);
		}
	}

	default void outputlLine(Object obj) {
		output(obj);
	}
}
