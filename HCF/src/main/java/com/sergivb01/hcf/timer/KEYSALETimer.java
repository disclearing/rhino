package com.sergivb01.hcf.timer;

import com.sergivb01.hcf.HCF;
import com.sergivb01.hcf.utils.config.ConfigurationService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.concurrent.TimeUnit;

public class KEYSALETimer extends GlobalTimer{

    public KEYSALETimer(){
        super(ConfigurationService.SOTW_TIMER, TimeUnit.MINUTES.toMillis(30L));
    }

    public void run(){
        if(getRemaining() % 30L == 0L){
            Bukkit.broadcastMessage(ChatColor.GREEN + "The key sale will start in " + ChatColor.GREEN.toString() + ChatColor.BOLD + HCF.getRemaining(getRemaining(), true));
        }
    }

    public ChatColor getScoreboardPrefix(){
        return ConfigurationService.SOTW_COLOUR;
    }


}
