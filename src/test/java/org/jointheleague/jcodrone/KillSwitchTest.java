package org.jointheleague.jcodrone;

import java.util.concurrent.TimeUnit;

public class KillSwitchTest implements Killable {

    private static boolean stopped = false;

    public static void main(String[] args) {
        KillSwitch killSwitch = new KillSwitch(new KillSwitchTest());
        while (!stopped) {
            try {
                System.out.println("Sleep");
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        killSwitch.disable();
    }

    @Override
    public void kill() {
        System.out.println("Killed.");
        stopped = true;
    }
}