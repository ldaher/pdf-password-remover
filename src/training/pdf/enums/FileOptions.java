package training.pdf.enums;

public enum FileOptions {
	OPEN(0), SAVE(1);
	
	private int option;
	
	private FileOptions(int option) {
		this.option = option;
	}
	
	public int getValue() {
		return this.option;
	}
}
