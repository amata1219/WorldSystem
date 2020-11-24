package world.system;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class InvCheckGatePlus {
    WorldSystem plugin;

    public InvCheckGatePlus() {
        this.plugin = WorldSystem.plugin;
    }

    public void CreateGatePlus(Player p, String s) {
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
                        loc.getBlock().setMetadata("WorldSystemInvCheckGatePlus", new FixedMetadataValue(this.plugin, true));
                    }
                }
            }

            String gateloc = minx + "," + miny + "," + minz + "," + maxx + "," + maxy + "," + maxz + "," + max.getWorld().getName();
            this.plugin.getConfig().set("InvCheckGatePlus." + s, gateloc);
            if (this.plugin.getConfig().getStringList("InvCheckGatePlusList").size() != 0) {
                this.plugin.getConfig().set("InvCheckGatePlusList", Arrays.asList(this.plugin.getConfig().getStringList("InvCheckGatePlusList"), s));
            } else {
                this.plugin.getConfig().set("InvCheckGatePlusList", Arrays.asList(s));
            }

            WorldSystem.config.saveConfig();
            WorldSystem.config.loadConfig();
            p.sendMessage(ChatColor.GREEN + "例外設定可能な特殊ゲート(" + s + ")を作成しました。");
        }

    }

    public void DeleteGatePlus(CommandSender sender, String s) {
        if (this.plugin.getConfig().getStringList("InvCheckGatePlusList").size() != 0) {
            if (this.plugin.getConfig().getString("InvCheckGatePlus") == null) {
                sender.sendMessage(ChatColor.RED + "指定された名前のゲートは存在しません。");
            } else {
                String[] locations = this.plugin.getConfig().getString("InvCheckGatePlus." + s).split(",");
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
                            loc.getBlock().removeMetadata("WorldSystemInvCheckGatePlus", this.plugin);
                        }
                    }
                }

                if (this.plugin.getConfig().getStringList("InvCheckGatePlusList").size() == 1) {
                    this.plugin.getConfig().set("InvCheckGatePlusList", (Object)null);
                } else {
                    i = 0;

                    for(Iterator var15 = this.plugin.getConfig().getStringList("InvCheckGatePlusList").iterator(); var15.hasNext(); ++i) {
                        String string = (String)var15.next();
                        if (string.equalsIgnoreCase(s)) {
                            break;
                        }
                    }

                    this.plugin.getConfig().getStringList("InvCheckGatePlusList").remove(i);
                }

                this.plugin.getConfig().set("InvCheckGatePlus." + s, (Object)null);
                WorldSystem.config.saveConfig();
                WorldSystem.config.loadConfig();
                sender.sendMessage(ChatColor.GREEN + "例外設定可能な特殊ゲート(" + s + ")を削除しました。");
            }
        }
    }

    public void PutItems(Player p) {
        if (p.getInventory().getItemInMainHand().getType() != null && !p.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
            if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName() == null) {
                p.sendMessage(ChatColor.RED + "アイテムにカスタムネームを設定して下さい。");
            } else {
                if (this.plugin.getConfig().getStringList("InvCheckGatePlusItems") != null) {
                    List<String> list = this.plugin.getConfig().getStringList("InvCheckGatePlusItems");
                    list.add(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().toString());
                    this.plugin.getConfig().set("InvCheckGatePlusItems", list);
                } else {
                    String[] list = new String[]{p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().toString()};
                    this.plugin.getConfig().set("InvCheckGatePlusItems", list);
                }

                WorldSystem.config.saveConfig();
                WorldSystem.config.loadConfig();
                p.sendMessage(ChatColor.GREEN + "例外設定に対象のアイテムを追加しました。");
            }
        } else {
            p.sendMessage(ChatColor.RED + "例外設定したいアイテムを持って下さい。");
        }
    }

    public void RemoveItems(CommandSender sender, String s) {
        if (this.plugin.getConfig().getStringList("InvCheckGatePlusItems") != null) {
            if (s.equalsIgnoreCase("all")) {
                this.plugin.getConfig().set("InvCheckGatePlusItems", (Object)null);
                WorldSystem.config.saveConfig();
                WorldSystem.config.loadConfig();
                sender.sendMessage(ChatColor.GREEN + "例外設定を全て削除しました。");
            } else if (this.plugin.getConfig().getStringList("InvCheckGatePlusItems").size() == 1) {
                this.plugin.getConfig().set("InvCheckGatePlusItems", (Object)null);
                WorldSystem.config.saveConfig();
                WorldSystem.config.loadConfig();
                sender.sendMessage(ChatColor.GREEN + "例外設定を全て削除しました。");
            } else {
                int i = 0;

                for(Iterator var5 = this.plugin.getConfig().getStringList("InvCheckGatePlusItems").iterator(); var5.hasNext(); ++i) {
                    String string = (String)var5.next();
                    if (string.equalsIgnoreCase(s)) {
                        break;
                    }
                }

                if (i > 0) {
                    this.plugin.getConfig().getStringList("InvCheckGateList").remove(i);
                }

                WorldSystem.config.saveConfig();
                WorldSystem.config.loadConfig();
                sender.sendMessage(ChatColor.GREEN + "例外設定を削除しました。");
            }
        }
    }
}
