package me.gsqfi.poketeams.poketeams.helper;

import me.clip.placeholderapi.PlaceholderAPI;
import me.gsqfi.poketeams.poketeams.Data;
import org.bukkit.OfflinePlayer;

public class StringHelper {
    public static String colorCodeReplace(String str){
        return str.replace('&', '§');
    }

    public static String papi(OfflinePlayer player,String str){
        return PlaceholderAPI.setPlaceholders(player,str);
    }

    public static String configMsg(String path){
        return colorCodeReplace(Data.plugin.getConfig().getString("msg."+path));
    }
}
