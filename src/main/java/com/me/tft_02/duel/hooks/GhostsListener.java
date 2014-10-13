package com.me.tft_02.duel.hooks;

import com.me.tft_02.duel.datatypes.player.PlayerData;
import com.me.tft_02.ghosts.events.tomb.PreTombCreateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class GhostsListener implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCreateTomb( PreTombCreateEvent event )
    {
        if( PlayerData.isInDuel( event.getPlayer() ) )
        {
            event.setCancelled( true );
        }
    }
}
