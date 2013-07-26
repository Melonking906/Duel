package com.me.tft_02.duel.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.me.tft_02.duel.util.Permissions;
import com.me.tft_02.ghosts.locale.LocaleLoader;

public class HelpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
            case 2:
                if (Integer.parseInt(args[1]) > 1) {
                    getHelpPage(Integer.parseInt(args[1]), sender);
                    return true;
                }
                else {
                    getHelpPage(1, sender);
                    return true;
                }

            default:
                getHelpPage(1, sender);
                return true;
        }
    }

    private void getHelpPage(int page, CommandSender sender) {
        int maxPages = 2;
        int nextPage = page + 1;

        if (page > maxPages) {
            sender.sendMessage(LocaleLoader.getString(LocaleLoader.getString("HelpCommand.0"), maxPages));
            return;
        }

        sender.sendMessage(LocaleLoader.getString("Help.Page_Header", page, maxPages));
        switch (page) {
            case 0:
                sendHelpPage(sender, LocaleLoader.getString("Help.Page_0.Line_0"));
                sendHelpPage(sender, LocaleLoader.getString("Help.Page_0.Line_1"));
                sendHelpPage(sender, LocaleLoader.getString("Help.Page_0.Line_2"));
                sendHelpPage(sender, LocaleLoader.getString("Help.Page_0.Line_3"));
                sendHelpPage(sender, LocaleLoader.getString("Help.Page_0.Line_4"));

            case 1:
                sendHelpPage(sender, LocaleLoader.getString("Help.Page_1.Line_0"));

                if (Permissions.reload(sender)) {
                    sendHelpPage(sender, LocaleLoader.getString("Help.Page_1.Line_1"));
                }

                if (Permissions.stats(sender)) {
                    sendHelpPage(sender, LocaleLoader.getString("Help.Page_1.Line_2"));
                }

                sendHelpPage(sender, LocaleLoader.getString("Help.Page_1.Line_3"));
                sendHelpPage(sender, LocaleLoader.getString("Help.Page_1.Line_4"));
            default:
                if (nextPage <= maxPages) {
                    sender.sendMessage(LocaleLoader.getString("Help.Page_Ending", "/duel help", nextPage));
                }
        }
    }

    /**
     * Send a string, but only if .length > 0
     * */
    private void sendHelpPage(CommandSender sender, String string) {
        if (string.length() > 0) {
            sender.sendMessage(string);
        }
    }
}
