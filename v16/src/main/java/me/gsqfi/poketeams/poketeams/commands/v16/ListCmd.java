package me.gsqfi.poketeams.poketeams.commands.v16;

import me.gsqfi.poketeams.poketeams.commands.AbstractTabExecutor;
import me.gsqfi.poketeams.poketeams.gui.v16.TeamsListGui;
import me.gsqfi.poketeams.poketeams.helper.StringHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class ListCmd extends AbstractTabExecutor {
    public ListCmd(AbstractTabExecutor superExecutor) {
        super(superExecutor, "list");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage(StringHelper.configMsg("non_player"));
            return false;
        }
        Player player = (Player) sender;
        /*ComponentHelper helper = new ComponentHelper("§3§lList\n");
        ArrayList<String> list = Lists.newArrayList(PlayerData.getConfig().getConfigurationSection(player.getName()).getKeys(false));
        int lastIndex = list.size() - 1;
        for (int i = 0; i < list.size(); i++) {
            String team_name = list.get(i);
            helper.apply("  §7§l- ").apply("§l§3§n"+team_name).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/poketeams see "+team_name));
            if (i != lastIndex){
                helper.apply("\n");
            }
        }
        BaseComponent[] components = helper.create();
        player.spigot().sendMessage(components);*/
        player.openInventory(new TeamsListGui(player,0).getInventory());
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}
