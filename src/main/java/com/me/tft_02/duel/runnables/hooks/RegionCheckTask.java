package com.me.tft_02.duel.runnables.hooks;

import com.me.tft_02.duel.datatypes.player.PlayerData;
import com.me.tft_02.duel.locale.LocaleLoader;
import com.me.tft_02.duel.util.RegionUtils;
import com.me.tft_02.duel.util.player.DuelManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RegionCheckTask extends BukkitRunnable
{

    @Override
    public void run()
    {
        checkRegion();
    }

    public void checkRegion()
    {
        for( Player player : PlayerData.getDuelingPlayers() )
        {
            Location location = player.getLocation();

            if( !RegionUtils.canDuelHereWG( location ) )
            {
                player.sendMessage( LocaleLoader.getString( "Duel.Cancelled.WorldGuard" ) );
                DuelManager.endDuelInTie( player );
                return;
            }
        }
    }
}
