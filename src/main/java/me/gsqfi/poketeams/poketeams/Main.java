package me.gsqfi.poketeams.poketeams;

import me.fullidle.ficore.ficore.common.SomeMethod;
import me.gsqfi.poketeams.poketeams.commands.HelpCmd;
import me.gsqfi.poketeams.poketeams.commands.MainCmd;
import me.gsqfi.poketeams.poketeams.commands.v12.CreateCmd;
import me.gsqfi.poketeams.poketeams.commands.v12.ListCmd;
import me.gsqfi.poketeams.poketeams.commands.v12.SeeCmd;
import me.gsqfi.poketeams.poketeams.helper.StringHelper;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        Data.plugin = this;

        reloadConfig();

        MainCmd cmd = new MainCmd(this.getDescription().getName().toLowerCase());
        {//subCmd
            String mcVer = SomeMethod.getMinecraftVersion();
            if (mcVer.equalsIgnoreCase("1.12.2")) {
                new CreateCmd(cmd);
                new ListCmd(cmd);
                new SeeCmd(cmd);
            }
            if (mcVer.equalsIgnoreCase("1.16.5")){
                new me.gsqfi.poketeams.poketeams.commands.v16.CreateCmd(cmd);
                new me.gsqfi.poketeams.poketeams.commands.v16.ListCmd(cmd);
                new me.gsqfi.poketeams.poketeams.commands.v16.SeeCmd(cmd);
            }
            if (mcVer.equalsIgnoreCase("1.20.2")){
                new me.gsqfi.poketeams.poketeams.commands.v20.CreateCmd(cmd);
                new me.gsqfi.poketeams.poketeams.commands.v20.ListCmd(cmd);
                new me.gsqfi.poketeams.poketeams.commands.v20.SeeCmd(cmd);
            }
        }
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
        HelpCmd.help = this.getConfig().getStringList("msg.help").stream().
                map(StringHelper::colorCodeReplace).toArray(String[]::new);
    }
}