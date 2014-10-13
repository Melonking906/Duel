package com.me.tft_02.duel.database;

import com.me.tft_02.duel.Duel;
import org.bukkit.Bukkit;

import java.io.*;
import java.util.HashMap;
import java.util.logging.Level;

public class Data
{
    static Duel plugin;

    public Data( Duel instance )
    {
        plugin = instance;
    }

    /*
     * Credits to BlahBerrys
     */

    public static void deleteDir( File dir )
    {
        if( dir.isDirectory() )
        {
            String[] children = dir.list();
            for( int i = 0; i < children.length; i++ )
            {
                deleteDir( new File( dir, children[i] ) );
            }
        }
        dir.delete();
    }

    public static void saveData()
    {
        File f = new File( Duel.p.getDataFolder(), "data.dat" );
        try
        {
            if( !f.exists() )
            {
                Duel.p.getDataFolder().mkdirs();
                f.createNewFile();
            }
            ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream( f ) );
            oos.writeObject( DatabaseManager.wins );
            oos.writeObject( DatabaseManager.losses );
            oos.writeObject( DatabaseManager.ties );
            oos.flush();
            oos.close();
            Duel.p.getLogger().log( Level.INFO, "Saved data successfully." );
        }
        catch( Exception e )
        {
            Duel.p.getLogger().log( Level.SEVERE, "Failed to save data." );
            e.printStackTrace();
        }
    }

    @SuppressWarnings({ "unchecked" })
    public static void loadData()
    {
        File f = new File( Duel.p.getDataFolder(), "data.dat" );
        if( f.exists() )
        {
            try
            {
                @SuppressWarnings("resource")
                ObjectInputStream ois = new ObjectInputStream( new FileInputStream( f ) );
                DatabaseManager.wins = ( HashMap<String, Integer> ) ois.readObject();
                DatabaseManager.losses = ( HashMap<String, Integer> ) ois.readObject();
                DatabaseManager.ties = ( HashMap<String, Integer> ) ois.readObject();
                Duel.p.getLogger().log( Level.INFO, "Loaded data successfully." );
            }
            catch( Exception e )
            {
                Duel.p.getLogger().log( Level.SEVERE, "Failed to load data. Disabling the plugin!" );
                e.printStackTrace();
                Bukkit.getServer().getPluginManager().disablePlugin( Duel.p );
            }
        }
    }
}
