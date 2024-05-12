package me.gsqfi.poketeams.poketeams.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class HelpCmd extends AbstractTabExecutor{
    public static String[] help;
    public HelpCmd(AbstractTabExecutor superExecutor) {
        super(superExecutor,"help");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(help);
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}
