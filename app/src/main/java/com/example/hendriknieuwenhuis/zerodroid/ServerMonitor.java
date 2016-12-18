package com.example.hendriknieuwenhuis.zerodroid;

import com.bono.api.ACKException;
import com.bono.api.ChangeListener;
import com.bono.api.DefaultCommand;
import com.bono.api.Endpoint;
import com.bono.api.Status;
import com.bono.api.protocol.MPDStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Created by hendriknieuwenhuis on 27/08/16.
 */
public class ServerMonitor extends Thread {

    private Endpoint endpoint = new Endpoint("192.168.2.4", 6600);

    private boolean running;

    private Status status;

    private Collection<String> reply;

    private List<ChangeListener> listeners = new ArrayList<>();

    public ServerMonitor(Status status) {
        this.status = status;
    }

    @Override
    public void run() {
        super.run();
        running = true;

        while (running) {
            if (reply == null) {
                reply = new ArrayList<>();
            }
            reply.clear();
            try {
                reply = endpoint.command(new DefaultCommand(MPDStatus.IDLE));
            } catch (ACKException ack) {
                ack.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            updateStatus();

        }
    }

    public void updateStatus() {
        if (reply == null) {
            reply = new ArrayList<>();
        }
        reply.clear();
        try {
            reply = endpoint.command(new DefaultCommand(MPDStatus.STATUS));
        } catch (ACKException ack) {
            ack.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        status.populate(reply);
    }

    public void endMonitoring() {
        running = false;
        try {
            endpoint.closeEndpoint();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void addListener(ChangeListener l) {
        status.addListener(l);
    }
}
