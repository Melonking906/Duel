package com.me.tft_02.duel.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.me.tft_02.duel.Duel;
import com.me.tft_02.ghosts.Ghosts;
import com.me.tft_02.ghosts.locale.LocaleLoader;

public class DuelCommand implements CommandExecutor {
    private CommandExecutor reloadCommand = new ReloadCommand();
    private CommandExecutor helpCommand = new HelpCommand();
    private CommandExecutor statsCommand = new StatsCommand();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("duel")) {
            switch (args.length) {
                case 1:
                    if (args[0].equalsIgnoreCase("help")) {
                        return helpCommand.onCommand(sender, command, label, args);
                    }
                    else if (args[0].equalsIgnoreCase("reload")) {
                        return reloadCommand.onCommand(sender, command, label, args);
                    }
                    else if (args[0].equalsIgnoreCase("stats")) {
                        return statsCommand.onCommand(sender, command, label, args);
                    }
                default:
                    return printUsage(sender);
            }
        }
        return false;
    }

    private boolean printUsage(CommandSender sender) {
        sender.sendMessage(LocaleLoader.getString("General.Plugin_Header", Duel.p.getDescription().getName(), Duel.p.getDescription().getAuthors()));
        sender.sendMessage(LocaleLoader.getString("General.Running_Version", Ghosts.p.getDescription().getVersion()));
        sender.sendMessage(LocaleLoader.getString("General.Use_Help"));
        return true;
    }
}
