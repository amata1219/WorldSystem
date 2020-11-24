package world.gamerule;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import world.system.WorldSystem;

public class CancelFireFlowerDamage implements Listener {
    WorldSystem plugin;

    public CancelFireFlowerDamage() {
        this.plugin = WorldSystem.plugin;
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
        Entity damager = e.getDamager();
        Entity damagee = e.getEntity();
        if (damager instanceof Firework) {
            if (damagee instanceof LivingEntity) {
                if (this.plugin.getConfig().getBoolean("gamerule.cfd")) {
                    e.setCancelled(true);
                }
            }
        }
    }
}
