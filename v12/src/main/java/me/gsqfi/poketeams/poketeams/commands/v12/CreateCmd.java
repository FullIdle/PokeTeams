package me.gsqfi.poketeams.poketeams.commands.v12;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import me.gsqfi.poketeams.poketeams.PlayerData;
import me.gsqfi.poketeams.poketeams.commands.AbstractTabExecutor;
import me.gsqfi.poketeams.poketeams.helper.StringHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CreateCmd extends AbstractTabExecutor {
    public CreateCmd(AbstractTabExecutor superExecutor) {
        super(superExecutor, "create");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage(StringHelper.configMsg("non_player"));
            return false;
        }

        if (args.length > 0) {
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
            String name = builder.toString();
            boolean containsData = PlayerData.getConfig().contains(player.getName());
            if (containsData &&PlayerData.getConfig().getConfigurationSection(player.getName()).contains(name)) {
                sender.sendMessage(StringHelper.configMsg("team_already_exists"));
                return false;
            }
            PlayerPartyStorage party = Pixelmon.storageManager.getParty(player.getUniqueId());
            Pokemon[] all = party.getAll();
            //判断队伍数量,是否能创建队伍
            String permission = "poketeams.cmd.create.";
            if (!player.hasPermission(permission)||!player.isOp()){
                int i = 0;
                for (PermissionAttachmentInfo info : player.getEffectivePermissions()) {
                    if (info.getPermission().startsWith(permission)) {
                        int anInt = isInt(info.getPermission().replace(permission, ""));
                        if (anInt != -1)
                            i = Math.max(i,anInt);
                    }
                }
                int y = 0;
                if (!containsData){
                    y = PlayerData.getConfig().getConfigurationSection(player.getName()).getKeys(true).size();
                }
                if (y >= i) {
                    sender.sendMessage(StringHelper.configMsg("reach_maximum_team"));
                    return false;
                }
            }

            //创建队伍
            ArrayList<String> list = new ArrayList<>();
            for (Pokemon pokemon : all) {
                if (pokemon == null){
                    list.add("");
                    continue;
                }
                list.add(pokemon.getUUID().toString());
            }
            PlayerData.getConfig().set(player.getName()+"."+name,list);
            PlayerData.save();
            sender.sendMessage(StringHelper.configMsg("create_team"));
            return false;
        }

        this.getSuperExecutor().getSubCmdMap().get("help").onCommand(sender, command, label, args);
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

    public static int isInt(String v){
        try {
            return Integer.parseInt(v);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
