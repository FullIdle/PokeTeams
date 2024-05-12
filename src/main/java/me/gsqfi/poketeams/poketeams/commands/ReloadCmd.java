package me.gsqfi.poketeams.poketeams.commands;

import me.gsqfi.poketeams.poketeams.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class ReloadCmd extends AbstractTabExecutor{
    public ReloadCmd(AbstractTabExecutor superExecutor) {
        super(superExecutor, "reload");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Main.getInstance().reloadConfig();
        sender.sendMessage("§aReload successful!");
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}
