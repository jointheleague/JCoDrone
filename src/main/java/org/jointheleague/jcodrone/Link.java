package org.jointheleague.jcodrone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jointheleague.jcodrone.protocol.DataType;
import org.jointheleague.jcodrone.protocol.link.LinkDiscoveredDevice;
import org.jointheleague.jcodrone.system.ModeLink;
import org.jointheleague.jcodrone.system.ModeLinkBroadcast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Link {
    public static final ModeLinkBroadcast BROADCAST_MODE = ModeLinkBroadcast.ACTIVE;
    private static final int STATE_REQUEST_RETRIES = 3;

    private static Logger log = LogManager.getLogger(Link.class);
    private final CoDrone codrone;

    private Address address;
    private ArrayList<LinkDiscoveredDevice> devices;
    private final ReentrantLock deviceDiscoveryLock = new ReentrantLock();
    private Condition discovery = deviceDiscoveryLock.newCondition();
    private boolean connected;
    private ModeLink mode = ModeLink.NONE;
    private ModeLinkBroadcast broadcastMode = ModeLinkBroadcast.NONE;
    private boolean scanning = false;

    public Link(CoDrone codrone) {
        this.codrone = codrone;
    }

    public void connect(String deviceName) throws CoDroneNotFoundException, MessageNotSentException, InterruptedException {
        connect(deviceName, null);
    }


    public void connect(Address address) throws CoDroneNotFoundException, MessageNotSentException, InterruptedException {
        connect(null, address);
    }


    public void connect(String deviceName, Address address) throws CoDroneNotFoundException, MessageNotSentException, InterruptedException {
        // ModeLinkBroadcast.Passive mode change
        requestState();

        if (mode == ModeLink.CONNECTED) {
            if ((deviceName != null && !deviceName.isEmpty())) {
                // Assume we're connected to the right drone? TODO
                connected = true;
                return;
//            } else if (address != null) {
            } else {
                disconnect();
            }
        }

        for (int retries = 0; retries < 3 && getLinkBroadcastMode() != BROADCAST_MODE; retries++) {
            setBroadcastMode(BROADCAST_MODE);
            requestState();
        }

        if (getLinkBroadcastMode() != BROADCAST_MODE) {
            log.error("Could not switch link to selected broadcast mode. Is red LED flashing?");
            throw new CoDroneNotFoundException();
        }

        // start searching device
        devices = new ArrayList<>(20);
        deviceDiscoveryLock.lock();
        log.debug("Starting discovery.");
        clearDevices();
        startDiscovery();
        if (discovery.await(5, TimeUnit.SECONDS)) {
            if (!scanning) {
                log.error("Scanning started message was not received.");
                throw new CoDroneNotFoundException();
            }
        } else {
            log.error("Timeout waiting for scanning to start.");
            throw new CoDroneNotFoundException();
        }


        if (discovery.await(5, TimeUnit.SECONDS)) {
            if (scanning) {
                log.error("Scanning did not complete as expected.");
                throw new CoDroneNotFoundException();
            }
        } else {
            log.error("Timeout waiting for scanning to end.");
            throw new CoDroneNotFoundException();
        }

        TimeUnit.SECONDS.sleep(1);

        if (devices.size() == 0) {
            throw new CoDroneNotFoundException();
        }

        LinkDiscoveredDevice targetDevice;
        if (deviceName != null && !deviceName.isEmpty()) {
            Optional<LinkDiscoveredDevice> matchingDevice = devices.stream().filter(p -> p.getName().substring(0, 12).equals(deviceName.substring(0, 12))).findFirst();
            if (matchingDevice.isPresent()) {
                targetDevice = matchingDevice.get();
            } else {
                throw new CoDroneNotFoundException(deviceName);
            }
        } else if (address != null) {
            Optional<LinkDiscoveredDevice> matchingDevice = devices.stream().filter(p -> Arrays.equals(p.getAddress(), address.getAddress())).findFirst();
            if (matchingDevice.isPresent()) {
                targetDevice = matchingDevice.get();
            } else {
                throw new CoDroneNotFoundException(deviceName);
            }
        } else {
            Optional<LinkDiscoveredDevice> closestDevice = devices.stream().max(new Comparator<LinkDiscoveredDevice>() {
                @Override
                public int compare(LinkDiscoveredDevice o1, LinkDiscoveredDevice o2) {
                    return (int) o2.getRssi() - (int) o1.getRssi();
                }
            });
            if (closestDevice.isPresent()) {
                targetDevice = closestDevice.get();
            } else {
                throw new CoDroneNotFoundException();
            }
        }

        log.debug("Connecting to {}[{}]", targetDevice.getNameString(), targetDevice.getFormattedAddress());
        setBroadcastMode(ModeLinkBroadcast.PASSIVE);
        connected = false;
        connectToIndex(targetDevice.getIndex());
        // TODO Wait for connected
        for (int i = 0; i < 50; i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            if (connected) break;
        }

        // TODO Wait for ready to command
    }

    public void disconnect() {
        codrone.sendCommand(CommandType.LINK_DISCONNECT);
    }

    public void resetSystem() {
        codrone.sendCommand(CommandType.LINK_SYSTEM_RESET);
        try {
            // TODO Alternative to sleeping for 3 seconds?
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            log.warn("Sleep following reset was interrupted.", e);
        }
    }

    public void connectToIndex(byte index) {
        codrone.sendCommand(CommandType.LINK_CONNECT, index);
    }

    public void setBroadcastMode(ModeLinkBroadcast mode) {
        codrone.sendCommand(CommandType.LINK_MODE_BROADCAST, mode.value());
    }

    public void startDiscovery() {
        codrone.sendCommand(CommandType.LINK_DISCOVER_START);
    }

    public void stopDiscovery() {
        codrone.sendCommand(CommandType.LINK_DISCOVER_STOP);
    }

    public void startRSSIPolling() {
        codrone.sendCommand(CommandType.LINK_RSSI_POLLING_START);
    }

    public void stopRSSIPolling() {
        codrone.sendCommand(CommandType.LINK_RSSI_POLLING_STOP);
    }

    public void clearDevices() {
        devices = new ArrayList<>(20);
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public void addDevice(LinkDiscoveredDevice device) {
        devices.add(device);
    }

    public Address getAddress() {
        return address;
    }

    public void requestState() throws InterruptedException {
        this.mode = ModeLink.NONE;
        codrone.sendCommand(CommandType.REQUEST, DataType.LINK_STATE.value());
        int attempt = 0;
        Thread.sleep(100);
        while (mode == ModeLink.NONE && attempt < STATE_REQUEST_RETRIES) {
            codrone.sendCommand(CommandType.REQUEST, DataType.LINK_STATE.value());
            attempt++;
            Thread.sleep(100);
        }
    }


    public ModeLinkBroadcast getLinkBroadcastMode() {
        return broadcastMode;
    }

    public void setStartedDiscovery() {
        log.debug("Notify scan started.");
        deviceDiscoveryLock.lock();
        scanning = true;
        discovery.signal();
        deviceDiscoveryLock.unlock();
    }

    public void setStoppedDiscovery() {
        log.debug("Notify scan stopped.");
        deviceDiscoveryLock.lock();
        scanning = false;
        discovery.signal();
        deviceDiscoveryLock.unlock();
    }

    public void setCurrentMode(ModeLink mode) {
        this.mode = mode;
    }

    public void setCurrentBroadcastMode(ModeLinkBroadcast broadcastMode) {
        this.broadcastMode = broadcastMode;
    }
}