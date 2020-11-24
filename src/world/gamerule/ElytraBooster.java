package world.gamerule;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import world.system.WorldSystem;

import java.util.Iterator;
import java.util.List;

public class ElytraBooster implements Listener {
    WorldSystem plugin;
    public String name;
    private ItemStack item;

    public ElytraBooster() {
        this.plugin = WorldSystem.plugin;
    }

    @EventHandler
    public void ElytraBoosterCheck(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) && p.isGliding() && (p.getInventory().getItemInMainHand().getType() != null || p.getInventory().getItemInOffHand().getType() != null) && (p.getInventory().getItemInMainHand().getType().equals(Material.FIREWORK) || p.getInventory().getItemInOffHand().getType().equals(Material.FIREWORK)) && this.plugin.getConfig().getBoolean("gamerule.wtlcub")) {
            List worlds;
            String s;
            Iterator var5;
            if (this.plugin.getConfig().getBoolean("system.wtlcubflag")) {
                worlds = this.plugin.getConfig().getStringList("world");
                var5 = worlds.iterator();

                while(true) {
                    do {
                        if (!var5.hasNext()) {
                            return;
                        }

                        s = (String)var5.next();
                    } while(!p.getWorld().getName().equals(s) && !s.equals("all"));

                    e.setCancelled(true);
                }
            } else if (this.plugin.getConfig().getBoolean("gamerule.allcub")) {
                worlds = this.plugin.getConfig().getStringList("world");
                var5 = worlds.iterator();

                while(true) {
                    do {
                        if (!var5.hasNext()) {
                            return;
                        }

                        s = (String)var5.next();
                    } while(!p.getWorld().getName().equals(s) && !s.equals("all"));

                    e.setCancelled(true);
                }
            }
        }

    }

}
