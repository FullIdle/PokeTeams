package me.gsqfi.poketeams.poketeams;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class PlayerData {
    @Getter
    private static File file;
    @Getter
    private static FileConfiguration config;

    @SneakyThrows
    public static void init(){
        clear();
        Main plugin = Main.getInstance();

        file = new File(plugin.getDataFolder(),"player_data.yml");
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    private static void clear() {

    }

    @SneakyThrows
    public static void save(){
        config.save(file);
    }
}
