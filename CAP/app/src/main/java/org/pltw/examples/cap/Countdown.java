package org.pltw.examples.cap;

/**
 * Created by Vikas A., Tudor C., Michael S., Eric W. on 05 17, 2017.
 *
 * Counts down by seconds
 * Helper class for Timer
 */
public class Countdown {
    // Fields
    // Time going to be spent on the countdown
    private long duration;
    private long durationMillis;

    // Start and end times
    private long timeStart;
    private long timeEnd;

    // Current time
    private long timeCurrent;
    // Amount of time that's left
    private long timeLeft;

    // Updates the countdown
    private void tick()
    {
        timeCurrent = System.currentTimeMillis();
        timeLeft = timeEnd - timeCurrent;
    }

    // Constructor
    public Countdown(int seconds)
    {
        duration = (long) seconds;
        durationMillis = duration * 1000;

        timeStart = System.currentTimeMillis();
        timeEnd = timeStart + durationMillis;

        tick();
    }

    // Getters
    public long getDuration() {
        return duration;
    }

    public long getDurationMillis() {
        return durationMillis;
    }

    public long getTimeStart() {
        return timeStart;
    }

    public long getTimeEnd() {
        return timeEnd;
    }

    // Return the amount of time left in the countdown
    public long getTimeLeft() {
        tick(); // need to update the time left on the countdown first
        return timeLeft;
    }

    // Return whether or not the time is up
    public boolean timeOut()
    {
        if (timeLeft <= 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
