package org.jointheleague.jcodrone;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KillSwitch implements KeyListener {
    private static Logger log = LogManager.getLogger(KillSwitch.class);

    private final Killable drone;
    private final JFrame frame;
    private KillMode mode = KillMode.Console;
    private int killSwitch = KeyEvent.VK_ESCAPE;
    private Thread monitor;
    private boolean shutdownThread = false;

    public KillSwitch(Killable drone) {
        this.drone = drone;
        this.frame = new JFrame();
        frame.addKeyListener(this);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.setUndecorated(true);
                frame.setSize(frame.getMaximumSize());
                frame.setBackground(new Color(0, 0, 0, 0));
                frame.setVisible(true);
                frame.setFocusable(true);
            }
        });
    }

    public void setKillSwitch(int killSwitch) {
        this.killSwitch = killSwitch;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == killSwitch) {
            for (int retries = 0; retries < 3; retries++) {
                try {
                    drone.kill();
                    return;
                } catch (KillException e1) {
                    log.warn("Exception killing drone:", e);
                }
                log.error("Could not kill drone.");
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public void disable() {
        frame.dispose();
    }
}
