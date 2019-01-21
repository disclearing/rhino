package com.sergivb01.hcf.timer.type;

import com.sergivb01.hcf.HCF;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class KeySaleTimer{

    private KeySaleRunnable keySaleRunnable;

    public boolean cancel(){
        if(this.keySaleRunnable != null){
            this.keySaleRunnable.cancel();
            this.keySaleRunnable = null;
            return true;
        }
        return false;
    }

    public void start(final long millis){
        if(this.keySaleRunnable == null){
            (this.keySaleRunnable = new KeySaleRunnable(this, millis)).runTaskLater(HCF.getPlugin(), millis / 50L);
        }
    }

    public KeySaleRunnable getKeySaleRunnable(){
        return this.keySaleRunnable;
    }

    public static class KeySaleRunnable extends BukkitRunnable {
        private KeySaleTimer keySaleTimer;
        private long startMillis;
        private long endMillis;

        public KeySaleRunnable(final KeySaleTimer keySaleTimer, final long duration){
            super();
            this.keySaleTimer = keySaleTimer;
            this.startMillis = System.currentTimeMillis();
            this.endMillis = this.startMillis + duration;
        }

        public long getRemaining(){
            return this.endMillis - System.currentTimeMillis();
        }

        public void run(){
            HCF.getInstance().getSotwTimer().enabledSotw.clear();
            Bukkit.broadcastMessage(ChatColor.GREEN + "The key sale has ended.");
            this.cancel();
            this.keySaleTimer.keySaleRunnable = null;
        }


    }


}
