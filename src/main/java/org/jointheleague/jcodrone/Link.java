package org.jointheleague.jcodrone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jointheleague.jcodrone.protocol.CommandType;
import org.jointheleague.jcodrone.protocol.information.Address;
import org.jointheleague.jcodrone.protocol.link.LinkDiscoveredDevice;
import org.jointheleague.jcodrone.system.ModeLinkBroadcast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

public class Link {
    private static Logger log = LogManager.getLogger(Link.class);
    private final CoDrone codrone;

    private Address address;
    private ArrayList<LinkDiscoveredDevice> devices;
    private boolean discoveringDevices;
    private boolean connected;

    public Link(CoDrone codrone) {
        this.codrone = codrone;
    }

    public void connect(String deviceName) throws CoDroneNotFoundException {
        // ModeLinkBroadcast.Passive mode change
        setBroadcastMode(ModeLinkBroadcast.PASSIVE);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // start searching device
        devices = new ArrayList<>(20);
        discoveringDevices = true;
        startDiscovery();

        // wait for 5sec
        for (int i = 0; i < 50; i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            if (!discoveringDevices) break;
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }

        if (devices.size() == 0) {
            throw new CoDroneNotFoundException();
        }

        LinkDiscoveredDevice targetDevice;
        if (deviceName == null || deviceName.isEmpty()) {
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
        } else{
            Optional<LinkDiscoveredDevice> matchingDevice = devices.stream().filter(p -> p.getName().substring(0,12).equals(deviceName.substring(0,12))).findFirst();
            if (matchingDevice.isPresent()) {
                targetDevice = matchingDevice.get();
            } else {
                throw new CoDroneNotFoundException(deviceName);
            }
        }

        connected = false;
        connectToIndex(targetDevice.getIndex());
        for (int i = 0; i < 50; i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            if (connected) break;
        }
        try {
            Thread.sleep(1200);
        } catch (InterruptedException e) {
        }
    }

    public void resetSystem(){
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

    public void startDiscovery(){
        codrone.sendCommand(CommandType.LINK_DISCOVER_START);
    }

    public void stopDiscovery(){
        codrone.sendCommand(CommandType.LINK_DISCOVER_STOP);
    }
    public void startRSSIPolling(){
        codrone.sendCommand(CommandType.LINK_RSSI_POLLING_START);
    }

    public void stopRSSIPolling(){
        codrone.sendCommand(CommandType.LINK_RSSI_POLLING_STOP);
    }

    public void clearDevices() {
        devices = new ArrayList<>(20);
    }

    public void setDiscoveringDevices(boolean discoveringDevices) {
        this.discoveringDevices = discoveringDevices;
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
}