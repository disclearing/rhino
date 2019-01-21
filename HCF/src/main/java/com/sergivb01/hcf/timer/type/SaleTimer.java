package com.sergivb01.hcf.timer.type;

import com.sergivb01.hcf.HCF;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class SaleTimer {

    private SaleRunnable saleRunnable;

    public boolean cancel(){
        if(this.saleRunnable != null){
            this.saleRunnable.cancel();
            this.saleRunnable = null;
            return true;
        }
        return false;
    }

    public void start(final long millis){
        if(this.saleRunnable == null){
            (this.saleRunnable = new SaleRunnable(this, millis)).runTaskLater(HCF.getPlugin(), millis / 50L);
        }
    }

    public SaleRunnable getSaleRunnable(){
        return this.saleRunnable;
    }

    public static class SaleRunnable extends BukkitRunnable {
        private SaleTimer saleTimer;
        private long startMillis;
        private long endMillis;

        public SaleRunnable(final SaleTimer saleTimer, final long duration){
            super();
            this.saleTimer = saleTimer;
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
            this.saleTimer.saleRunnable = null;
        }

//saleRunnable
    }


}
