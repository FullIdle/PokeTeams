package me.gsqfi.poketeams.poketeams.helper;

import me.gsqfi.poketeams.poketeams.PlayerData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class TemporaryHelper {
    /**
     * 判断玩家是否能创建队伍
     */
    public static boolean canCreate(Player player){
        String permission = "poketeams.cmd.create.";
        if (!player.isOp()&&!player.hasPermission(permission+"*")) {
            int i = 0;
            for (PermissionAttachmentInfo info : player.getEffectivePermissions()) {
                if (info.getPermission().startsWith(permission)) {
                    int anInt = isInt(info.getPermission().replace(permission, ""));
                    if (anInt != -1)
                        i = Math.max(i,anInt);
                }
            }
            int y = 0;
            ConfigurationSection section = PlayerData.getConfig().getConfigurationSection(player.getName());
            if (section != null){
                y = section.getKeys(true).size();
            }
            if (y >= i) {
                return false;
            }
        }
        return true;
    }

    public static int isInt(String v){
        try {
            return Integer.parseInt(v);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
