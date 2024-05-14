package me.gsqfi.poketeams.poketeams.commands;

import me.gsqfi.poketeams.poketeams.helper.ComponentHelper;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MainCmd extends AbstractTabExecutor {
    public MainCmd(String name) {
        super(name);
        initSubCmd();
    }

    private void initSubCmd() {
        new HelpCmd(this);
        new ReloadCmd(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String subCmd = "help";
        if (args.length > 0) {
            String ac = args[0].toLowerCase();
            if (this.getSubCmdName().contains(ac)) {
                subCmd = ac;
            }
        }
        String permission = "poketeams.cmd." + subCmd;
        if (!sender.hasPermission(permission)) {
            sender.spigot().sendMessage(new ComponentHelper("§c你缺少权限: ")
                    .apply("§3§l§n" + permission).event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, permission))
                    .create());
            return false;
        }
        this.getSubCmdMap().get(subCmd).onCommand(sender, command, label, this.removeArgsOneObject(args));
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            return this.getSubCmdName();
        }
        String aCase = args[0].toLowerCase();
        if (args.length == 1) {
            return this.getSubCmdName().stream().filter(s -> s.startsWith(aCase)).collect(Collectors.toList());
        }
        if (this.getSubCmdName().contains(aCase)) {
            return this.getSubCmdMap().get(aCase).onTabComplete(sender, command, label, this.removeArgsOneObject(args));
        }
        return Collections.emptyList();
    }
}
