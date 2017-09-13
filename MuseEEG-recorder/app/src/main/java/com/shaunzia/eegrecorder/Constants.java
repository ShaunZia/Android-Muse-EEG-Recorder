package com.shaunzia.eegrecorder;

/**
 * Constants.java defines sampling rate
 *
 * @author Shaun Zia
 * @version 2.0 27/11/2015
 */
public class Constants {

    // Sampling frequency corresponds to hardware settings
    private final static int DEFAULT_SAMPLING_RATE = 220;

    // Return sampling frequency
    public static int getDefaultSamplingRate() {

        return DEFAULT_SAMPLING_RATE;
    }
}
