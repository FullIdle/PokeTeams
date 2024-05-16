package me.gsqfi.poketeams.poketeams.commands.v16;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import me.gsqfi.poketeams.poketeams.PlayerData;
import me.gsqfi.poketeams.poketeams.commands.AbstractTabExecutor;
import me.gsqfi.poketeams.poketeams.helper.StringHelper;
import me.gsqfi.poketeams.poketeams.helper.TemporaryHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CreateCmd extends AbstractTabExecutor {
    public CreateCmd(AbstractTabExecutor superExecutor) {
        super(superExecutor, "create");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(StringHelper.configLang("non_player"));
            return false;
        }

        if (args.length > 0) {
            if (args[0].isEmpty() || args[0].equalsIgnoreCase(" ")) {
                args = this.removeArgsOneObject(args);
            }
            StringBuilder builder = new StringBuilder();
            int lastIndex = args.length - 1;
            for (int i = 0; i < args.length; i++) {
                builder.append(args[i]);
                if (i != lastIndex) {
                    builder.append(" ");
                }
            }
            Player player = (Player) sender;
            String name = builder.toString();
            boolean containsData = PlayerData.getConfig().contains(player.getName());
            if (containsData && PlayerData.getConfig().getConfigurationSection(player.getName()).contains(name)) {
                sender.sendMessage(StringHelper.configLang("team_already_exists"));
                return false;
            }
            PlayerPartyStorage party = StorageProxy.getParty(player.getUniqueId());
            Pokemon[] all = party.getAll();

            //是否可创建
            if (!TemporaryHelper.canCreate(player)) {
                sender.sendMessage(StringHelper.configLang("reach_maximum_team"));
                return false;
            }

            //创建队伍
            ArrayList<String> list = new ArrayList<>();
            for (Pokemon pokemon : all) {
                if (pokemon == null) {
                    list.add("");
                    continue;
                }
                list.add(pokemon.getUUID().toString());
            }
            PlayerData.getConfig().set(player.getName() + "." + name, list);
            PlayerData.save();
            sender.sendMessage(StringHelper.configLang("create_team"));
            return false;
        }

        this.getSuperExecutor().getSubCmdMap().get("help").onCommand(sender, command, label, args);
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

    public static int isInt(String v) {
        try {
            return Integer.parseInt(v);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
