package world.claim;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import me.ryanhamshire.GriefPrevention.Claim;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import world.system.Util;
import world.system.WorldSystem;

public class AutoDeleteClaims {
    WorldSystem plugin;

    public AutoDeleteClaims() {
        this.plugin = WorldSystem.plugin;
    }

    public void AutoDeleteProtectSystem() {
        this.plugin.getServer().broadcastMessage(ChatColor.GREEN + "期限切れの保護の削除並びに再生成を開始します。");
        int claims = 0;
        HashMap<UUID, Integer> list = new HashMap();
        int ula = this.plugin.getConfig().getInt("GriefPrevention.AutoDeleteClaims.UpperLimitArea");
        String wfcd = this.plugin.getConfig().getString("GriefPrevention.AutoDeleteClaims.WorldForCopyData");
        OfflinePlayer[] players = this.plugin.getServer().getOfflinePlayers();
        OfflinePlayer[] arrayOfOfflinePlayer1 = players;
        int j = players.length;

        label92:
        for(int i = 0; i < j; ++i) {
            OfflinePlayer p = arrayOfOfflinePlayer1[i];
            Collection<Claim> claimss = Util.getGriefPrevention().dataStore.getPlayerData(p.getUniqueId()).getClaims();
            Iterator var12 = claimss.iterator();

            while(true) {
                Claim claim;
                long difference;
                long unused;
                do {
                    do {
                        do {
                            do {
                                do {
                                    if (!var12.hasNext()) {
                                        continue label92;
                                    }

                                    claim = (Claim)var12.next();
                                    list.put(claim.ownerID, 0);
                                } while(claim.isAdminClaim());

                                List<String> cancels = this.plugin.getConfig().getStringList("GriefPrevention.AutoDeleteClaims.CancelList");
                                Iterator var15 = cancels.iterator();

                                while(var15.hasNext()) {
                                    String cancel = (String)var15.next();
                                    if (this.plugin.getServer().getOfflinePlayer(claim.ownerID).getName().equals(cancel)) {
                                        this.plugin.getServer().getLogger().info(claim.getID() + "番地の" + this.plugin.getServer().getOfflinePlayer(claim.ownerID).getName() + "さんの保護は延長申請されているため、処理をキャンセルしました。");
                                        list.put(claim.ownerID, 1);
                                    }
                                }
                            } while((Integer)list.get(claim.ownerID) != 0);
                        } while(claim.getArea() > ula);

                        int days = this.plugin.getConfig().getInt("GriefPrevention.AutoDeleteClaims.UnusedClaimDays");
                        unused = (long)(86400000 * days);
                        long lastplay = this.plugin.getServer().getOfflinePlayer(claim.ownerID).getLastPlayed();
                        difference = System.currentTimeMillis() - lastplay;
                    } while((Integer)list.get(claim.ownerID) == 0);
                } while(difference < unused);

                World w = this.plugin.getServer().getWorld(wfcd);
                Location loc1 = claim.getLesserBoundaryCorner();
                loc1.setY(0.0D);
                Location loc2 = claim.getGreaterBoundaryCorner();
                loc2.setY(255.0D);

                for(int y = loc1.getBlockY(); y <= loc2.getBlockY(); ++y) {
                    for(int x = loc1.getBlockX(); x <= loc2.getBlockX(); ++x) {
                        for(int z = loc1.getBlockZ(); z <= loc2.getBlockZ(); ++z) {
                            Location clone = loc1.clone();
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

                ++claims;
                Claim finalClaim = claim;
                (new BukkitRunnable() {
                    public void run() {
                        Util.getGriefPrevention().dataStore.deleteClaim(finalClaim);
                    }
                }).runTaskLater(this.plugin, 1200L);
            }
        }

        this.plugin.getServer().broadcastMessage(ChatColor.GREEN + "期限切れの保護を" + claims + "個削除し再生成しました。");
    }
}
