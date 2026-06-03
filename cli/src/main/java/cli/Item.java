package cli;

public interface Item {
	String displayName();
	void perform();
	default boolean isExit() {
		return false;
	}
}
