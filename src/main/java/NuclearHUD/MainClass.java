package NuclearHUD;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import cn.nukkit.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;

public class MainClass extends PluginBase {

    @Override
    public void onLoad() {
        this.getLogger().info(TextFormat.WHITE + "NuclearHUD by Bulk enabled");
    }

    @Override
    public void onEnable() {
        this.getLogger().info(TextFormat.DARK_GREEN + "LOADED!");

        this.getLogger().info(String.valueOf(this.getDataFolder().mkdirs()));
        Config config = new Config(
                new File(this.getDataFolder(), "config.yml"),
                Config.YAML,
                //Default values (not necessary)
                new LinkedHashMap<String, Object>() {
                    {
                        put("message", "§l§bNuclearHUD ");
                        put("ticks", 1);
                    }
                });
        //Don't forget to save it!
        config.save();
        

        this.getServer().getScheduler().scheduleRepeatingTask(new sendTipTask(this), config.get("ticks"));

    }

    @Override
    public void onDisable() {
        this.getLogger().info(TextFormat.DARK_RED + "DISABLED!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (command.getName()) {
            case "hud":
                try {
                    this.toggleHud(sender);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
        }
        return true;
    }

}
