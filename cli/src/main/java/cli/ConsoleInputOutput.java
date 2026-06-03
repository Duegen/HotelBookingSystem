package cli;

import java.util.Scanner;

public class ConsoleInputOutput implements Inputoutput {

	Scanner scanner = new Scanner(System.in);

	@Override
	public String inputString(String prompt) {
		outputlLine(prompt + " or cancel for exit");
		String text = scanner.nextLine();

		return text.equalsIgnoreCase("cancel") ? null : text;
	}

	@Override
	public void output(Object obj) {
		System.out.println(obj);

	}

}
