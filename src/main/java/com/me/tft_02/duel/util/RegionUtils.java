package com.me.tft_02.duel.util;

import com.me.tft_02.duel.Duel;
import com.me.tft_02.duel.config.Config;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.LinkedList;

public class RegionUtils
{

    public static boolean canDuelHere( Location location )
    {
        return canDuelHere( location.getWorld() ) && ( Duel.p.isWorldGuardEnabled() && canDuelHere( getRegion( location ) ) );
    }

    public static boolean canDuelHereWG( Location location )
    {
        return Duel.p.isWorldGuardEnabled() && canDuelHere( getRegion( location ) );
    }

    public static boolean canDuelHere( World world )
    {
        boolean isWhitelist = Config.getInstance().getWorldUseAsWhitelist();

        if( Config.getInstance().getWorldList().contains( world.getName() ) )
        {
            return isWhitelist;
        }

        return !isWhitelist;
    }

    private static boolean canDuelHere( String region )
    {
        boolean isWhitelist = Config.getInstance().getWGUseAsWhitelist();

        if( isListedRegion( region ) )
        {
            return isWhitelist;
        }

        return !isWhitelist;
    }

    private static boolean isListedRegion( String region )
    {
        for( String name : Config.getInstance().getWGRegionList() )
        {
            if( region.equalsIgnoreCase( "[" + name + "]" ) )
            {
                return true;
            }
        }
        return false;
    }

    private static String getRegion( Location location )
    {
        RegionManager regionManager = Duel.p.getWorldGuard().getRegionManager( location.getWorld() );
        ApplicableRegionSet set = regionManager.getApplicableRegions( location );
        LinkedList<String> parentNames = new LinkedList<String>();
        LinkedList<String> regions = new LinkedList<String>();

        for( ProtectedRegion region : set )
        {
            String id = region.getId();
            regions.add( id );
            ProtectedRegion parent = region.getParent();
            while( parent != null )
            {
                parentNames.add( parent.getId() );
                parent = parent.getParent();
            }
        }

        for( String name : parentNames )
        {
            regions.remove( name );
        }

        return regions.toString();
    }
}
