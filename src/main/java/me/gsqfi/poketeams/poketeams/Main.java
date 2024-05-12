package me.gsqfi.poketeams.poketeams;

import me.gsqfi.poketeams.poketeams.commands.HelpCmd;
import me.gsqfi.poketeams.poketeams.commands.MainCmd;
import me.gsqfi.poketeams.poketeams.helper.StringHelper;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static Main plugin;

    @Override
    public void onEnable() {
        plugin = this;

        reloadConfig();

        MainCmd cmd = new MainCmd(this.getDescription().getName().toLowerCase());
        PluginCommand command = getCommand(cmd.getName());
        command.setExecutor(cmd);
        command.setTabCompleter(cmd);

        getLogger().info("§aPlugin enabled!");
    }

    @Override
    public void reloadConfig() {
        saveDefaultConfig();
        super.reloadConfig();
        PlayerData.init();
        HelpCmd.help = Main.getInstance().getConfig().getStringList("msg.help").stream().
                map(StringHelper::colorCodeReplace).toArray(String[]::new);
    }

    public static Main getInstance() {
        return plugin;
    }
}