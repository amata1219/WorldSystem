package world.system;

import java.util.Arrays;
import java.util.Iterator;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class InvCheckGate implements Listener {
    WorldSystem plugin;

    public InvCheckGate() {
        this.plugin = WorldSystem.plugin;
    }

    public void CreateGate(Player p, String s) {
        if (Util.getWorldEdit().getSelection(p).getMinimumPoint() != null && Util.getWorldEdit().getSelection(p).getMaximumPoint() != null) {
            Location min = Util.getWorldEdit().getSelection(p).getMinimumPoint();
            int minx = min.getBlockX();
            int miny = min.getBlockY();
            int minz = min.getBlockZ();
            Location max = Util.getWorldEdit().getSelection(p).getMaximumPoint();
            int maxx = max.getBlockX();
            int maxy = max.getBlockY();
            int maxz = max.getBlockZ();

            for(int x = minx; x <= maxx; ++x) {
                for(int y = miny; y <= maxy; ++y) {
                    for(int z = minz; z <= maxz; ++z) {
                        Location loc = new Location(min.getWorld(), (double)x, (double)y, (double)z);
                        loc.getBlock().setMetadata("WorldSystemInvCheckGate", new FixedMetadataValue(this.plugin, true));
                    }
                }
            }

            String gateloc = minx + "," + miny + "," + minz + "," + maxx + "," + maxy + "," + maxz + "," + max.getWorld().getName();
            this.plugin.getConfig().set("InvCheckGate." + s, gateloc);
            if (this.plugin.getConfig().getStringList("InvCheckGateList").size() != 0) {
                this.plugin.getConfig().set("InvCheckGateList", Arrays.asList(this.plugin.getConfig().getStringList("InvCheckGateList"), s));
            } else {
                this.plugin.getConfig().set("InvCheckGateList", Arrays.asList(s));
            }

            WorldSystem.config.saveConfig();
            WorldSystem.config.loadConfig();
            p.sendMessage(ChatColor.GREEN + "特殊ゲート(" + s + ")を作成しました。");
        }

    }

    public void DeleteGate(CommandSender sender, String s) {
        if (this.plugin.getConfig().getStringList("InvCheckGateList").size() != 0) {
            if (this.plugin.getConfig().getString("InvCheckGate." + s) == null) {
                sender.sendMessage(ChatColor.RED + "指定された名前のゲートは存在しません。");
            } else {
                String[] locations = this.plugin.getConfig().getString("InvCheckGate." + s).split(",");
                int minx = Integer.valueOf(locations[0]);
                int miny = Integer.valueOf(locations[1]);
                int minz = Integer.valueOf(locations[2]);
                int maxx = Integer.valueOf(locations[3]);
                int maxy = Integer.valueOf(locations[4]);
                int maxz = Integer.valueOf(locations[5]);

                int i;
                for(i = minx; i <= maxx; ++i) {
                    for(int y = miny; y <= maxy; ++y) {
                        for(int z = minz; z <= maxz; ++z) {
                            Location loc = new Location(this.plugin.getServer().getWorld(locations[6]), (double)i, (double)y, (double)z);
                            loc.getBlock().removeMetadata("WorldSystemInvCheckGate", this.plugin);
                        }
                    }
                }

                if (this.plugin.getConfig().getStringList("InvCheckGateList").size() == 1) {
                    this.plugin.getConfig().set("InvCheckGateList", (Object)null);
                } else {
                    i = 0;

                    for(Iterator var15 = this.plugin.getConfig().getStringList("InvCheckGateList").iterator(); var15.hasNext(); ++i) {
                        String string = (String)var15.next();
                        if (string.equalsIgnoreCase(s)) {
                            break;
                        }
                    }

                    if (i > 0) {
                        this.plugin.getConfig().getStringList("InvCheckGateList").remove(i);
                    }
                }

                this.plugin.getConfig().set("InvCheckGate." + s, (Object)null);
                WorldSystem.config.saveConfig();
                WorldSystem.config.loadConfig();
                sender.sendMessage(ChatColor.GREEN + "特殊ゲート(" + s + ")を削除しました。");
            }
        }
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent e) {
        ItemStack item;
        int var3;
        int var4;
        ItemStack[] var5;
        if (e.getTo().getBlock().hasMetadata("WorldSystemInvCheckGate")) {
            var4 = (var5 = e.getPlayer().getInventory().getContents()).length;

            for(var3 = 0; var3 < var4; ++var3) {
                item = var5[var3];
                if (item != null && item.getType() != Material.AIR) {
                    e.getPlayer().sendMessage(ChatColor.RED + "インベントリ内にアイテムが存在するため進めません。");
                    e.setCancelled(true);
                    break;
                }
            }
        } else if (e.getTo().getBlock().hasMetadata("WorldSystemInvCheckGatePlus")) {
            if (this.plugin.getConfig().getStringList("InvCheckGatePlusItems") == null) {
                var4 = (var5 = e.getPlayer().getInventory().getContents()).length;

                for(var3 = 0; var3 < var4; ++var3) {
                    item = var5[var3];
                    if (item != null && item.getType() != Material.AIR) {
                        e.getPlayer().sendMessage(ChatColor.RED + "インベントリ内にアイテムが存在するため進めません。");
                        e.setCancelled(true);
                        break;
                    }
                }

                return;
            }

            var4 = (var5 = e.getPlayer().getInventory().getContents()).length;

            for(var3 = 0; var3 < var4; ++var3) {
                item = var5[var3];
                if (item != null) {
                    if (item.getItemMeta().getDisplayName() == null) {
                        e.getPlayer().sendMessage(ChatColor.RED + "インベントリ内にアイテムが存在するため進めません。");
                        e.setCancelled(true);
                        break;
                    }

                    if (item.getType() != null && item.getType() != Material.AIR) {
                        int i = 0;
                        Iterator var8 = this.plugin.getConfig().getStringList("InvCheckGatePlusItems").iterator();

                        while(var8.hasNext()) {
                            String s = (String)var8.next();
                            if (s.equalsIgnoreCase(item.getItemMeta().getDisplayName().toString())) {
                                ++i;
                            }
                        }

                        if (i == 0) {
                            e.getPlayer().sendMessage(ChatColor.RED + "インベントリ内にアイテムが存在するため進めません。");
                            e.setCancelled(true);
                            break;
                        }
                    }
                }
            }
        }

    }
}
