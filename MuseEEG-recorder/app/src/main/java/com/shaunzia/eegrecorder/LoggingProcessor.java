package com.shaunzia.eegrecorder;

import com.interaxon.libmuse.MuseDataPacket;
import com.interaxon.libmuse.MuseDataPacketType;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * LoggingProcessor.java logs raw EEG signals and assigns a timestamp in milliseconds
 *
 * @author Shaun Zia
 * @version 2.0 27/11/2015
 */
public class LoggingProcessor {

    // Define file folder structure in Android
    public static final String FILE_FOLDER = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "MuseEEG-recorder" + File.separator;

    // Declare EEG log variables
    public static final String PX_RAW = "Raw_EEG";
    public static final String EX = ".csv";

    // Assign HashMap to write files
    private HashMap<MuseDataPacketType, FileWriter> writers;

    // // Declare EEG file
    private File rawEEG;

    // Constructor
    public LoggingProcessor() {

    }

    // Start recording
    public void startLogging() {
        if (initWriters()) {
            EventBus.getDefault().register(this);
        }
    }

    // Stop recording
    public void stopLogging() {
        closeWriters();
        EventBus.getDefault().unregister(this);
    }

    // Write to column headers: timestamp & four EEG channels
    public void onEvent(final MuseDataPacket data) {
        if (writers != null && !writers.isEmpty()) {
            FileWriter writer = writers.get(data.getPacketType());

            if (writer != null) {
                try {
                    writer.write(
                            getTimestamp() + ","
                                    + data.getValues().get(0) + ","
                                    + data.getValues().get(1) + ","
                                    + data.getValues().get(2) + ","
                                    + data.getValues().get(3) + "\n");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // csv boolean logger
    public boolean isLogging() {
        return EventBus.getDefault().isRegistered(this);
    }

    // Define file path for csv records
    public boolean renameRecords(String note) {
        boolean rawEEGRename = false;

        if (rawEEG != null) {
            rawEEGRename = rawEEG.renameTo(new File(rawEEG.getAbsolutePath() + "_" + note + EX));
        }
        return rawEEGRename;
    }

    // Write raw EEG data to csv files
    private boolean initWriters() {
        File dir = makeDir();
        long currentTime = System.currentTimeMillis();
        rawEEG = new File(dir, currentTime + "_" + PX_RAW);

        try {
            writers = new HashMap<>();
            FileWriter rawEEGWriter = new FileWriter(rawEEG);
            writers.put(MuseDataPacketType.EEG, rawEEGWriter);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // Stop recording
    private boolean closeWriters() {
        if (writers != null && !writers.isEmpty()) {
            for (Map.Entry<MuseDataPacketType, FileWriter> next : writers.entrySet()) {
                if (next.getValue() != null) {
                    try {
                        next.getValue().flush();
                        next.getValue().close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }
            writers.clear();
        }
        return true;
    }

    // File directory structure
    private File makeDir() {
        File dir = new File(FILE_FOLDER);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    // Assign timestamp in milliseconds
    private String getTimestamp() {
        return System.currentTimeMillis() + "";
    }
}
