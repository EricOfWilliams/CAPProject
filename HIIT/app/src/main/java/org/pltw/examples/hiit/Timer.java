package org.pltw.examples.hiit;

/**
 * Created by Eric W. on 05 17, 2017.
 *
 * This keeps track of the time in each rep, whether or not you should be resting, and contains
 * the method to stop working out.
 */
public class Timer {
    // Fields
    // Booleans for whether or not you are resting
    private boolean resting = false;
    // Whether or not the thing has stopped entirely, either by tapout or out of time
    private boolean stopped;

    // Keeps track of the time left in the rest or exercise
    private Countdown timeLeft;

    // Amount of iterations to do
    private int iterations;
    // Amount of time to be spent exercising, in seconds
    private int timeExercising;
    // amount of time to be spent resting, in seconds
    private int timeResting;

    /*
     * Constructor for org.pltw.examples.hiit.Timer
     * iterations: Amount of reps you want to do
     * timeExercising: amount of time you want to spend exercising
     * timeResting: amount of time you want to spend resting
     */
    public Timer(int iterations, int timeExercising, int timeResting) {
        this.iterations = iterations;
        this.timeExercising = timeExercising;
        this.timeResting = timeResting;

        this.timeLeft = new Countdown(timeExercising);
    }

    // Getters
    // Return whether or not is resting
    public boolean isResting() {
        return resting;
    }

    // Return the amount of iterations that have gone through
    public int getIterations() {
        return iterations;
    }

    public int getTimeExercising() {
        return timeExercising;
    }

    public int getTimeResting() {
        return timeResting;
    }

    public boolean isStopped() {
        return stopped;
    }

    // Get time remaining on the countdown
    public int getTimeLeft()
    {
        return (int) timeLeft.getTimeLeft();
    }

    // Return the countdown object
    public Countdown getCountdown()
    {
        return timeLeft;
    }

    public boolean isTimeOut()
    {
        return timeLeft.timeOut();
    }

    // Setters
    // Set the iteration the timer is on
    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    /* Stop the timer entirely
     * Either ran out of iterations, or tap out was hit
     */
    public void stop()
    {
        stopped = true;
    }

    /*
     * Changes the App from exercising to resting
     * Switches resting to true
     * Makes a new org.pltw.examples.hiit.Countdown with the time for resting
     */
    private void switchToResting()
    {
        resting = true;
        timeLeft = new Countdown(timeResting);
    }

    /*
     * Changes the App from resting to exercising
     * Switches resting to false
     * Makes a new org.pltw.examples.hiit.Countdown with the time for exercising
     */
    private void switchToExercising()
    {
        resting = false;
        timeLeft = new Countdown(timeExercising);
    }

    /*
     * Switch from exercising to resting, or resting to exercising
     */
    public void switchStatus()
    {
        if (isResting()) { // Resting --> Exercising
            switchToExercising();
        }
        else { // Exercising --> Resting
            switchToResting();
        }
    }

    /*
     * Return whether exercising or resting as a string
     */
    public String getStatus()
    {
        if (isResting())
        {
            return "Resting";
        }
        else {
            return "Exercising";
        }
    }

    // Decrements the amount of iterations left
    public void decrement()
    {
        iterations--;
    }

}
