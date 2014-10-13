package com.me.tft_02.duel.config;

import com.me.tft_02.duel.Duel;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.List;

public abstract class ConfigLoader
{
    protected static final Duel plugin = Duel.p;
    protected String fileName;
    protected File configFile;
    protected FileConfiguration config;

    public ConfigLoader( String relativePath, String fileName )
    {
        this.fileName = fileName;
        configFile = new File( plugin.getDataFolder(), relativePath + File.separator + fileName );
        loadFile();
    }

    public ConfigLoader( String fileName )
    {
        this.fileName = fileName;
        configFile = new File( plugin.getDataFolder(), fileName );
        loadFile();
    }

    protected void loadFile()
    {
        if( !configFile.exists() )
        {
            plugin.debug( "Creating Duel " + fileName + " File..." );
            createFile();
        }
        else
        {
            plugin.debug( "Loading Duel " + fileName + " File..." );
        }

        config = YamlConfiguration.loadConfiguration( configFile );
    }

    protected abstract void loadKeys();

    protected void createFile()
    {
        configFile.getParentFile().mkdirs();

        InputStream inputStream = plugin.getResource( fileName );

        if( inputStream == null )
        {
            plugin.getLogger().severe( "Missing resource file: '" + fileName + "' please notify the plugin authors" );
            return;
        }

        OutputStream outputStream = null;

        try
        {
            outputStream = new FileOutputStream( configFile );

            int read;
            byte[] bytes = new byte[1024];

            while( ( read = inputStream.read( bytes ) ) != -1 )
            {
                outputStream.write( bytes, 0, read );
            }
        }
        catch( FileNotFoundException e )
        {
            e.printStackTrace();
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }
        finally
        {
            if( outputStream != null )
            {
                try
                {
                    outputStream.close();
                }
                catch( IOException e )
                {
                    e.printStackTrace();
                }
            }

            try
            {
                inputStream.close();
            }
            catch( IOException e )
            {
                e.printStackTrace();
            }
        }
    }

    protected boolean validateKeys()
    {
        return true;
    }

    protected boolean noErrorsInConfig( List<String> issues )
    {
        for( String issue : issues )
        {
            plugin.getLogger().warning( issue );
        }

        return issues.isEmpty();
    }

    protected void validate()
    {
        if( validateKeys() )
        {
            plugin.debug( "No errors found in " + fileName + "!" );
        }
        else
        {
            plugin.getLogger().warning( "Errors were found in " + fileName + "! Duel was disabled!" );
            plugin.getServer().getPluginManager().disablePlugin( plugin );
            plugin.noErrorsInConfigFiles = false;
        }
    }
}
