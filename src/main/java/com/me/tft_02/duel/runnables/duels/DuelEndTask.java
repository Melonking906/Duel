package com.me.tft_02.duel.runnables.duels;

import com.me.tft_02.duel.datatypes.player.PlayerData;
import com.me.tft_02.duel.util.player.DuelManager;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class DuelEndTask extends BukkitRunnable
{
    private Player player;

    public DuelEndTask( Player player )
    {
        this.player = player;
    }

    @Override
    public void run()
    {
        if( player.isValid() && PlayerData.isInDuel( player ) )
        {
            DuelManager.endDuelInTie( player );
        }
    }
}
