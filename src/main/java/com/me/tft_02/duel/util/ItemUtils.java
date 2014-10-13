package com.me.tft_02.duel.util;

import com.me.tft_02.duel.Duel;
import com.me.tft_02.duel.config.Config;
import org.bukkit.inventory.ItemStack;

public class ItemUtils
{
    Duel plugin;

    public ItemUtils( Duel instance )
    {
        plugin = instance;
    }

    /**
     * Checks if the item is a duel item.
     *
     * @param item Item to check
     * @return true if the item is a duel item, false otherwise
     */
    public static boolean isDuelWeapon( ItemStack item )
    {
        return Config.getInstance().getDuelWeaponItems().contains( item.getType() );
    }
}
