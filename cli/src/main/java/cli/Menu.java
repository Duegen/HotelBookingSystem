package cli;

public class Menu {
	Item[] items;
	Inputoutput inOut;

	public Menu(Item[] items, Inputoutput inOut) {
		super();
		this.items = items;
		this.inOut = inOut;
	}

	public void runMenu() {
		while (true) {
			for (int i = 0; i < items.length; i++) {
				inOut.outputlLine((i + 1) + ". " + items[i].displayName());
			}

			Integer selected = inOut.inputInteger("Please enter item number", 1, items.length);
			if (selected == null)
				return;
			try {
				items[selected - 1].perform();
			} catch (Exception e) {
				inOut.outputlLine(e.getMessage());
			}
			if (items[selected - 1].isExit())
				return;
		}

	}

}
