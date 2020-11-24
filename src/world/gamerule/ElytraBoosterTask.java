package world.gamerule;

import org.bukkit.scheduler.BukkitRunnable;
import world.system.Util;
import world.system.WorldSystem;

public class ElytraBoosterTask extends BukkitRunnable {
    WorldSystem plugin;

    public ElytraBoosterTask() {
        this.plugin = WorldSystem.plugin;
    }

    public void run() {
        if (this.plugin.getConfig().getBoolean("gamerule.wtlcub")) {
            double tps = Util.Essentials().getTimer().getAverageTPS();
            if (tps <= (double)this.plugin.getConfig().getInt("tps.wtlstart")) {
                if (!(Boolean)this.plugin.elytra.get("Elytra")) {
                    this.plugin.getServer().broadcastMessage(this.plugin.getConfig().getString("message.wtlcubstart"));
                    (new BukkitRunnable() {
                        public void run() {
                            ElytraBoosterTask.this.plugin.elytra.put("Elytra", true);
                        }
                    }).runTaskLater(this.plugin, 200L);
                }
            } else if (tps >= (double)this.plugin.getConfig().getInt("tps.wtlend") && (Boolean)this.plugin.elytra.get("Elytra")) {
                this.plugin.getServer().broadcastMessage(this.plugin.getConfig().getString("message.wtlcubend"));
                (new BukkitRunnable() {
                    public void run() {
                        ElytraBoosterTask.this.plugin.elytra.put("Elytra", false);
                    }
                }).runTaskLater(this.plugin, 200L);
            }
        }

    }
}
