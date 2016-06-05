package NuclearHUD;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import cn.nukkit.utils.Utils;
import cn.nukkit.event.player.PlayerDeathEvent;

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
                        put("Message", "§l§bNuclearHUD ");
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
                    if(this.getHud(sender) == true){
                        sender.sendMessage(TextFormat.RED + "HUD DISABLED!");
                    }else{
                        sender.sendMessage(TextFormat.GREEN + "HUD ENABLED!");
                    }
                    this.toggleHud(sender);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
        }
        return true;
    }
    public void toggleHud(Player p){
        if(!this.getConfig().get(p.getName() + ".hud")){
            this.getConfig().set(p.getName() + ".hud", "off");
        }
        if(this.getConfig().get(p.getName() + ".hud") == "off"){
            this.getConfig().set(p.getName() + ".hud", "on");
        }
        if(this.getConfig().get(p.getName() + ".hud") == "on"){
            this.getConfig().set(p.getName() + ".hud", "off");
        }
    }
    
    public void getHud(Player p){
        if(this.getConfig().get(p.getName()) == "on"){
            return true;
        }
    }
    
    public void onDeath(PlayerDeathEvent event){
        if(!this.getConfig().get(event.getEntity().getName() + ".Deaths")){
        this.getConfig().set(event.getEntity().getName() + ".Deaths", 1);
        }else{
            int deaths = this.getConfig().get(event.getEntity().getName() + ".Deaths");
            int newDeaths = deaths++;
            this.getConfig().set(event.getEntity().getName() + "Deaths", newDeaths);
        }
        
        if(!this.getConfig().get(event.getKiller().getName() + ".Kills")){
        this.getConfig().set(event.getKiller().getName() + ".Kills", 1);
        }else{
            int kills = this.getConfig().get(event.getKiller().getName() + ".Kills");
            int newKills = kills++;
            this.getConfig().set(event.getKiller().getName() + "Kills", newKills);
        }
    }
    
    public void getKills(Player p){
        return this.getConfig().get(p.getName() + "Kills");
    }
    
    public void getDeaths(Player p){
        return this.getConfig().get(p.getName() + "Deaths");
    }
    
    public void getMessage(Player p){
        String kills = this.getKills(p);
        String deaths = this.getDeaths(p);
        int count = this.getServer().getOnlinePlayers().length;
        String m = this.getConfig().get("Message");
        String m2 = m.replaceAll({NAME}, p.getName());
        String m3 = m2.replaceAll({KILLS}, kills);
        String m4 = m3.replaceAll({COUNT}, count);
        String m5 = m4.replaceAll({DEATHS}, deaths);
        return m5;
    }

}
