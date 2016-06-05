package NuclearHUD;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import cn.nukkit.event.player.PlayerDeathEvent;
import cn.nukkit.Player;
import java.io.File;
import java.util.LinkedHashMap;

public class MainClass extends PluginBase implements Listener{

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
        

        this.getServer().getScheduler().scheduleRepeatingTask(new sendTipTask(this), config.getInt("ticks"));
        this.getServer().getPluginManager().registerEvents(this, this);

    }

    @Override
    public void onDisable() {
        this.getLogger().info(TextFormat.DARK_RED + "DISABLED!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (command.getName()) {
            case "hud":
                if(!(sender instanceof Player)){
                    return false;
                }
                if(this.getHud((Player) sender)){
                    sender.sendMessage(TextFormat.RED + "HUD DISABLED!");
                }else{
                    sender.sendMessage(TextFormat.GREEN + "HUD ENABLED!");
                }
                this.toggleHud((Player) sender);
                break;
        }
        return true;
    }
    public void toggleHud(Player p){
        if(!this.getConfig().exists(p.getName() + ".hud")){
            this.getConfig().set(p.getName() + ".hud", "off");
        }
        if(this.getConfig().get(p.getName() + ".hud") == "off"){
            this.getConfig().set(p.getName() + ".hud", "on");
        }
        if(this.getConfig().get(p.getName() + ".hud") == "on"){
            this.getConfig().set(p.getName() + ".hud", "off");
        }
    }
    
    public boolean getHud(Player p){
        if(this.getConfig().get(p.getName()) == "on"){
            return true;
        }
        return false;
    }
    
    public void onDeath(PlayerDeathEvent event){
        if(!this.getConfig().getBoolean(event.getEntity().getName() + ".Deaths")){
            this.getConfig().set(event.getEntity().getName() + ".Deaths", 1);
        }else{
            int deaths = this.getConfig().getInt(event.getEntity().getName() + ".Deaths");
            this.getConfig().set(event.getEntity().getName() + "Deaths", ++deaths);
        }
        if(!this.getConfig().getBoolean(event.getEntity().getLastDamageCause().getEntity().getName() + ".Kills")){
            this.getConfig().set(event.getEntity().getLastDamageCause().getEntity().getName() + ".Kills", 1);
        }else{
            int kills = this.getConfig().getInt(event.getEntity().getLastDamageCause().getEntity().getName() + ".Kills");
            this.getConfig().set(event.getEntity().getLastDamageCause().getEntity().getName() + "Kills", ++kills);
        }
    }
    
    public String getKills(Player p){
        return this.getConfig().getString(p.getName() + "Kills");
    }
    
    public String getDeaths(Player p){
        return this.getConfig().getString(p.getName() + "Deaths");
    }
    
    public String getMessage(Player p){
        return this.getConfig().getString("Message")
            .replaceAll("\\{NAME}", p.getName())
            .replaceAll("\\{KILLS}", this.getKills(p))
            .replaceAll("\\{COUNT}", Integer.toString(this.getServer().getOnlinePlayers().size()))
            .replaceAll("\\{DEATHS}", this.getDeaths(p));
    }

}
