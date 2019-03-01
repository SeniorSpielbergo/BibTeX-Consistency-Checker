package de.david_wille.bibtexconsistencychecker.statistics;

public class BCCStatistics {
	
	private static volatile BCCStatistics instance;
	private int errorCounter;
	private int warningCounter;

	private BCCStatistics() {
		return;
	}

	public static BCCStatistics getInstance() {
		if (instance == null) {
			synchronized (BCCStatistics.class) {
				if (instance == null) {
					instance = new BCCStatistics();
				}
			}
		}

		return instance;
	}

	public boolean wasErrorDetected() {
		return errorCounter > 0;
	}

	public boolean wasWarningDetected() {
		return warningCounter > 0;
	}

	public int getErrorCounter() {
		return errorCounter;
	}

	public int getWarningCounter() {
		return warningCounter;
	}

	public void increaseErrorCounter() {
		errorCounter++;
	}

	public void increaseWarningCounter() {
		warningCounter++;
	}
	
	public void reset() {
		errorCounter = 0;
		warningCounter = 0;
	}

}
