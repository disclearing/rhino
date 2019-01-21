package com.sergivb01.hcf.timer.type;

import com.sergivb01.hcf.HCF;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class KeyAllTimer {

    private KeyAllRunnable keyAllRunnable;

    public boolean cancel(){
        if(this.keyAllRunnable != null){
            this.keyAllRunnable.cancel();
            this.keyAllRunnable = null;
            return true;
        }
        return false;
    }

    public void start(final long millis){
        if(this.keyAllRunnable == null){
            (this.keyAllRunnable = new KeyAllRunnable(this, millis)).runTaskLater(HCF.getPlugin(), millis / 50L);
        }
    }

    public KeyAllRunnable getkeyAllRunnable(){
        return this.keyAllRunnable;
    }

    public static class KeyAllRunnable extends BukkitRunnable {
        private KeyAllTimer keyAllTimer;
        private long startMillis;
        private long endMillis;

        public KeyAllRunnable(final KeyAllTimer keyAllTimer, final long duration){
            super();
            this.keyAllTimer = keyAllTimer;
            this.startMillis = System.currentTimeMillis();
            this.endMillis = this.startMillis + duration;
        }

        public long getRemaining(){
            return this.endMillis - System.currentTimeMillis();
        }

        public void run(){
            HCF.getInstance().getSotwTimer().enabledSotw.clear();
            Bukkit.broadcastMessage(ChatColor.GREEN + "Key all is now!");
            this.cancel();
            this.keyAllTimer.keyAllRunnable = null;
        }
    }


}
