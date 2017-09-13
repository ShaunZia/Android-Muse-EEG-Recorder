package com.shaunzia.eegrecorder;

import com.interaxon.libmuse.ConnectionState;
import com.interaxon.libmuse.Muse;
import com.interaxon.libmuse.MuseArtifactPacket;
import com.interaxon.libmuse.MuseConnectionListener;
import com.interaxon.libmuse.MuseConnectionPacket;
import com.interaxon.libmuse.MuseDataListener;
import com.interaxon.libmuse.MuseDataPacket;
import com.interaxon.libmuse.MuseDataPacketType;
import com.interaxon.libmuse.MuseManager;
import com.interaxon.libmuse.MusePreset;

import android.content.Context;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * MuseWrapper.java utilizes the Muse libraries
 *
 * @author Shaun Zia
 * @version 2.0 27/11/2015
 */
public class MuseWrapper {

    // Declare MuseWrapper variables
    public static final String TAG = MuseWrapper.class.getSimpleName();
    private static MuseWrapper instance;

    // Declare wrapper context
    private Context context;

    // Declare listener variables
    private final DataListener dataListener = new DataListener();
    private final ConnectionListener connectionListener = new ConnectionListener();

    // Return instance of context
    public static synchronized MuseWrapper getInstance(Context context) {
        if (instance == null) {
            instance = new MuseWrapper(context);
        }
        return instance;
    }

    // MuseWrapper context
    private MuseWrapper(Context ctx) {
        context = ctx;
    }

    // List Muse devices
    public List<Muse> getPairedMused() {
        MuseManager.refreshPairedMuses();
        return MuseManager.getPairedMuses();
    }

    // Connect
    public void connect(Muse muse) {
        config(muse);
        if (muse != null && muse.getConnectionState() != ConnectionState.CONNECTED) {
            try {
                muse.runAsynchronously();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Disconnect
    public void disconnect(Muse muse) {
        if (muse != null) {
            muse.disconnect(true);
        }
    }

    // Configure Muse EEG & horseshoe data packets
    private void config(Muse muse) {
        muse.registerConnectionListener(connectionListener);
        muse.registerDataListener(dataListener, MuseDataPacketType.EEG);
        muse.registerDataListener(dataListener, MuseDataPacketType.HORSESHOE);
        muse.setPreset(MusePreset.PRESET_14);
        muse.enableDataTransmission(true);
    }

    // Data packet listener
    private class DataListener extends MuseDataListener {

        public DataListener() {
            super();
        }

        @Override
        public void receiveMuseDataPacket(MuseDataPacket museDataPacket) {
            if (museDataPacket != null
                    && museDataPacket.getPacketType() == MuseDataPacketType.HORSESHOE) {
                EventBus.getDefault().post(museDataPacket.getValues());
            } else {
                EventBus.getDefault().post(museDataPacket);
            }
        }

        // Artifact packet
        @Override
        public void receiveMuseArtifactPacket(MuseArtifactPacket museArtifactPacket) {

        }
    }

    // Connection listener
    private class ConnectionListener extends MuseConnectionListener {
        public ConnectionListener() {
            super();
        }

        @Override
        public void receiveMuseConnectionPacket(MuseConnectionPacket museConnectionPacket) {
            EventBus.getDefault().post(museConnectionPacket);
        }
    }
}