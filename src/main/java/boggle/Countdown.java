package boggle;

import java.util.TimerTask;

// countdown class to run 2 min countdown timer
public class Countdown extends TimerTask {
	private int timerValue;

	// default constructor to set init timerValue
	public Countdown(int seconds) {
		setTimerValue(seconds);
	}

	// run method for timer in GUI class
	public void run() {
		// subtracting 1 from timerValue
		setTimerValue(getTimerValue() - 1);
		new BoggleGUI().updateTimerLabel();
	}

	// getters and setters
	public int getTimerValue() {
		return timerValue;
	}

	public void setTimerValue(int timerValue) {
		this.timerValue = timerValue;
	}
}
