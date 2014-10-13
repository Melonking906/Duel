package com.me.tft_02.duel.commands;

import com.me.tft_02.duel.Duel;
import com.me.tft_02.duel.datatypes.player.PlayerData;
import com.me.tft_02.duel.locale.LocaleLoader;
import com.me.tft_02.duel.util.CommandUtils;
import com.me.tft_02.duel.util.Misc;
import com.me.tft_02.duel.util.player.DuelManager;
import com.me.tft_02.duel.util.player.UserManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChallengeCommand implements CommandExecutor
{

    @Override
    public boolean onCommand( CommandSender sender, Command command, String label, String[] args )
    {
        if( CommandUtils.noConsoleUsage( sender ) )
        {
            return true;
        }

        switch( args.length )
        {
            case 2:
                Player player = ( Player ) sender;
                Player target = Duel.p.getServer().getPlayer( Misc.getMatchedPlayerName( args[1] ) );

                if( CommandUtils.isOffline( player, target ) )
                {
                    return true;
                }

                if( player == target )
                {
                    sender.sendMessage( LocaleLoader.getString( "Duel.Challenge.Self" ) );
                    return true;
                }

                if( !DuelManager.canDuel( player, false ) )
                {
                    return true;
                }

                if( UserManager.getPlayer( target ).getOccupied() )
                {
                    player.sendMessage( LocaleLoader.getString( "Duel.Challenge.Occupied", target.getName() ) );
                    return true;
                }

                if( CommandUtils.tooFar( player, target, 5 ) )
                {
                    return true;
                }

                if( PlayerData.isInDuel( player ) || PlayerData.isInDuel( target ) )
                {
                    sender.sendMessage( LocaleLoader.getString( "Duel.Invite.Occupied" ) );
                    return true;
                }

                DuelManager.handleDuelInvites( player, target );
                return true;

            default:
                sender.sendMessage( LocaleLoader.getString( "Commands.Usage.2", "duel", "challenge", "<player>" ) );
                return true;
        }
    }
}
