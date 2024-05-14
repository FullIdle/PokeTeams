package me.gsqfi.poketeams.poketeams.commands.v20;

import com.google.common.collect.Lists;
import me.gsqfi.poketeams.poketeams.PlayerData;
import me.gsqfi.poketeams.poketeams.commands.AbstractTabExecutor;
import me.gsqfi.poketeams.poketeams.gui.v20.TeamsGui;
import me.gsqfi.poketeams.poketeams.helper.StringHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Collections;
import java.util.List;

public class SeeCmd extends AbstractTabExecutor {
    public SeeCmd(AbstractTabExecutor superExecutor) {
        super(superExecutor, "see");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage(StringHelper.configMsg("non_player"));
            return false;
        }

        if (args.length > 0){
            if (args[0].isEmpty()||args[0].equalsIgnoreCase(" ")){
                args = this.removeArgsOneObject(args);
            }
            StringBuilder builder = new StringBuilder();
            int lastIndex = args.length - 1;
            for (int i = 0; i < args.length; i++) {
                builder.append(args[i]);
                if (i != lastIndex){
                    builder.append(" ");
                }
            }
            Player player = (Player) sender;
            String team_name = builder.toString();
            if (!PlayerData.getConfig().contains(player.getName()) || !Lists.newArrayList(PlayerData.getConfig().getConfigurationSection(player.getName()).getKeys(false)).contains(team_name)){
                sender.sendMessage(StringHelper.configMsg("team_not_exist"));
                return false;
            }
            Inventory inv = new TeamsGui(player, team_name).getInventory();
            player.closeInventory();
            player.openInventory(inv);
            return false;
        }
        this.getSuperExecutor().getSubCmdMap().get("help").onCommand(sender, command, label, args);
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}
