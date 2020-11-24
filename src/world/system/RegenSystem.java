package world.system;

import java.util.Iterator;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class RegenSystem {
    WorldSystem plugin;

    public RegenSystem() {
        this.plugin = WorldSystem.plugin;
    }

    public void ProtectRemoveSystem(Player p) {
        String wfcd = this.plugin.getConfig().getString("GriefPrevention.ManualDeleteClaim.WorldForCopyData");
        if (Util.getWorldEdit().getSelection(p).getMinimumPoint() != null && Util.getWorldEdit().getSelection(p).getMaximumPoint() != null) {
            World w = this.plugin.getServer().getWorld(wfcd);
            Location loc1 = Util.getWorldEdit().getSelection(p).getMinimumPoint();
            Location loc2 = Util.getWorldEdit().getSelection(p).getMaximumPoint();

            int y;
            int x;
            int z;
            Location clone;
            for(y = loc1.getBlockY(); y <= loc2.getBlockY(); ++y) {
                for(x = loc1.getBlockX(); x <= loc2.getBlockX(); ++x) {
                    for(z = loc1.getBlockZ(); z <= loc2.getBlockZ(); ++z) {
                        clone = loc1.clone();
                        clone.setX((double)x);
                        clone.setY((double)y);
                        clone.setZ((double)z);
                        if (clone.getBlock().getType().equals(w.getBlockAt(x, y, z).getType())) {
                            if (clone.getBlock().getData() != w.getBlockAt(x, y, z).getData()) {
                                clone.getBlock().setData(w.getBlockAt(x, y, z).getData());
                            }
                        } else if (clone.getBlock().getData() == w.getBlockAt(x, y, z).getData()) {
                            clone.getBlock().setType(w.getBlockAt(x, y, z).getType());
                            clone.getBlock().setData(w.getBlockAt(x, y, z).getData());
                        } else {
                            clone.getBlock().setType(w.getBlockAt(x, y, z).getType());
                            clone.getBlock().setData(w.getBlockAt(x, y, z).getData());
                        }
                    }
                }
            }

            for(y = loc1.getBlockY(); y <= loc2.getBlockY(); ++y) {
                for(x = loc1.getBlockX(); x <= loc2.getBlockX(); ++x) {
                    for(z = loc1.getBlockZ(); z <= loc2.getBlockZ(); ++z) {
                        clone = loc1.clone();
                        clone.setX((double)x);
                        clone.setY((double)y);
                        clone.setZ((double)z);
                        Iterator var11 = clone.getWorld().getEntities().iterator();

                        while(var11.hasNext()) {
                            final Entity entity = (Entity)var11.next();
                            if (entity.getLocation().getBlockX() == clone.getBlockX() && entity.getLocation().getBlockY() == clone.getBlockY() && entity.getLocation().getBlockZ() == clone.getBlockZ()) {
                                (new BukkitRunnable() {
                                    public void run() {
                                        if (entity.getType().equals(EntityType.ARMOR_STAND)) {
                                            entity.remove();
                                        } else if (entity.getType().equals(EntityType.ENDER_DRAGON) || entity.getType().equals(EntityType.WITHER)) {
                                            entity.remove();
                                            return;
                                        }

                                        if (entity instanceof LivingEntity) {
                                            LivingEntity le = (LivingEntity)entity;
                                            le.addPotionEffect(new PotionEffect(PotionEffectType.HARM, 1, 100));
                                        } else {
                                            entity.remove();
                                        }

                                    }
                                }).runTaskLater(this.plugin, 400L);
                            }
                        }
                    }
                }
            }
        }

        p.sendMessage(ChatColor.WHITE + "土地の再生成が完了しました。");
    }
}
