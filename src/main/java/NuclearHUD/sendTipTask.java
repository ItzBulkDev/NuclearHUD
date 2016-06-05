package NuclearHUD;

import cn.nukkit.scheduler.PluginTask;

public class sendTipTask extends PluginTask<MainClass> {

    public sendTipTask(MainClass owner) {
        super(owner);
    }

    @Override
    public void onRun(int currentTick) {
        for(this.getServer().getPlayers() : p){
        String m = this.getOwner().getMessage(p);
        p.sendTip(m);
        }
    }
}
