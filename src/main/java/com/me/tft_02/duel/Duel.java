package com.me.tft_02.duel;

import com.google.common.collect.ImmutableList;
import com.me.tft_02.duel.commands.DuelCommand;
import com.me.tft_02.duel.config.Config;
import com.me.tft_02.duel.config.HiddenConfig;
import com.me.tft_02.duel.database.Data;
import com.me.tft_02.duel.hooks.FactionsListener;
import com.me.tft_02.duel.hooks.GhostsListener;
import com.me.tft_02.duel.hooks.TownyListener;
import com.me.tft_02.duel.hooks.WorldGuardListener;
import com.me.tft_02.duel.listeners.EntityListener;
import com.me.tft_02.duel.listeners.PlayerListener;
import com.me.tft_02.duel.runnables.duels.DuelRangeTask;
import com.me.tft_02.duel.runnables.hooks.RegionCheckTask;
import com.me.tft_02.duel.util.LogFilter;
import com.me.tft_02.duel.util.Misc;
import com.me.tft_02.duel.util.player.UserManager;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;

public class Duel extends JavaPlugin
{
    public static Duel p;

    public static File duel;

    private boolean worldGuardEnabled = false;
    private boolean ghostsEnabled = false;
    private boolean assassinEnabled = false;
    private boolean factionsEnabled = false;
    private boolean townyEnabled = false;

    // Config Validation Check
    public boolean noErrorsInConfigFiles = true;

    /**
     * Run things on enable.
     */
    @Override
    public void onEnable()
    {
        p = this;
        getLogger().setFilter( new LogFilter( this ) );

        Config.getInstance();
        HiddenConfig.getInstance();

        if( ! noErrorsInConfigFiles )
        {
            return;
        }

        registerEvents();

        setupWorldGuard();
        setupGhosts();
        setupAssassin();
        setupFactions();
        setupTowny();

        setupFilePaths();

        if( HiddenConfig.getInstance().isCommandEnabled() )
        {
            getCommand( "duel" ).setExecutor( new DuelCommand() );
        }

        Data.loadData();

        for( Player player : ImmutableList.copyOf( Duel.p.getServer().getOnlinePlayers() ) )
        {
            UserManager.addUser( player ); // In case of reload add all users back into UserManager
        }

        scheduleTasks();
    }

    /**
     * Registers all event listeners
     */
    private void registerEvents()
    {
        PluginManager pluginManager = getServer().getPluginManager();

        // Register events
        pluginManager.registerEvents( new PlayerListener(), this );
        pluginManager.registerEvents( new EntityListener(), this );
    }

    public void debug( String message )
    {
        getLogger().info( "[Debug] " + message );
    }

    private void setupWorldGuard()
    {
        if( getServer().getPluginManager().isPluginEnabled( "WorldGuard" ) )
        {
            worldGuardEnabled = true;
            debug( "Hooked into WorldGuard successfully!" );
            getServer().getPluginManager().registerEvents( new WorldGuardListener(), this );
        }
    }

    private void setupGhosts()
    {
        if( getServer().getPluginManager().isPluginEnabled( "Ghosts" ) )
        {
            ghostsEnabled = true;
            debug( "Hooked into Ghosts successfully!" );
            getServer().getPluginManager().registerEvents( new GhostsListener(), this );
        }
    }

    private void setupAssassin()
    {
        if( getServer().getPluginManager().isPluginEnabled( "Assassin" ) )
        {
            assassinEnabled = true;
            debug( "Hooked into Assassin successfully!" );
        }
    }

    private void setupFactions()
    {
        PluginManager pluginManager = getServer().getPluginManager();

        if( pluginManager.isPluginEnabled( "Factions" ) )
        {
            // Check Factions version 2.2.0, which has FactionsEventPvpDisallowed
            int version = Integer.parseInt( pluginManager.getPlugin( "Factions" ).getDescription().getVersion().replaceAll( "[.]", "" ) );
            if( version >= 220 )
            {
                factionsEnabled = true;
                debug( "Hooked into Factions successfully!" );
                pluginManager.registerEvents( new FactionsListener(), this );
            }
            else
            {
                debug( "Duel does not support this version of Factions!" );
            }
        }
    }

    private void setupTowny()
    {
        PluginManager pluginManager = getServer().getPluginManager();

        if( pluginManager.isPluginEnabled( "Towny" ) )
        {
            // Check Towny version v0.84.0.11, which has DisallowedPVPEvent
            int version = Integer.parseInt( pluginManager.getPlugin( "Towny" ).getDescription().getVersion().replaceAll( "[.]", "" ) );

            if( version >= 84011 )
            {
                townyEnabled = true;
                debug( "Hooked into Towny successfully!" );
                pluginManager.registerEvents( new TownyListener(), this );
            }
            else
            {
                debug( "Duel does not support this version of Towny!" );
            }
        }
    }

    public WorldGuardPlugin getWorldGuard()
    {
        Plugin plugin = getServer().getPluginManager().getPlugin( "WorldGuard" );

        if( plugin == null || !( plugin instanceof WorldGuardPlugin ) )
        {
            return null;
        }

        return ( WorldGuardPlugin ) plugin;
    }

    /**
     * Run things on disable.
     */
    @Override
    public void onDisable()
    {
        Data.saveData();
        this.getServer().getScheduler().cancelTasks( this );
    }

    /**
     * Setup the various storage file paths
     */
    private void setupFilePaths()
    {
        duel = getFile();
    }

    private void scheduleTasks()
    {
        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask( this, new DuelRangeTask(), 0, 2 * Misc.TICK_CONVERSION_FACTOR );

        //Region check timer (Runs every five seconds)
        new RegionCheckTask().runTaskTimer( this, 5 * Misc.TICK_CONVERSION_FACTOR, 5 * Misc.TICK_CONVERSION_FACTOR );
    }

    public boolean isWorldGuardEnabled()
    {
        return worldGuardEnabled;
    }

    public boolean isGhostsEnabled()
    {
        return ghostsEnabled;
    }

    public boolean isAssassinEnabled()
    {
        return assassinEnabled;
    }

    public boolean isFactionsEnabled()
    {
        return factionsEnabled;
    }
}
