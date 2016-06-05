package NuclearHUD;

import cn.nukkit.scheduler.PluginTask;
import cn.nukkit.Player;

public class sendTipTask extends PluginTask<MainClass> {

    public sendTipTask(MainClass owner) {
        super(owner);
    }

    @Override
    public void onRun(int currentTick) {
        for(Player p : this.getOwner().getServer().getOnlinePlayers().values()){
            if(this.getOwner().getHud(p)){
                p.sendTip(this.getOwner().getMessage(p));
            }
        }
    }
}
