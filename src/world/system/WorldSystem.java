package world.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import world.gamerule.CancelFireFlowerDamage;
import world.gamerule.ElytraBooster;
import world.gamerule.ElytraBoosterTask;

public class WorldSystem extends JavaPlugin {
    public static WorldSystem plugin;
    public static MainConfig config;
    public HashMap<String, TabExecutor> command;
    public HashMap<String, Boolean> elytra;
    public List<BukkitTask> task = new ArrayList();

    public WorldSystem() {
    }

    public void onEnable() {
        plugin = this;
        config = new MainConfig(this);
        this.getServer().getPluginManager().registerEvents(new InvCheckGate(), plugin);
        this.getServer().getPluginManager().registerEvents(new ElytraBooster(), plugin);
        this.getServer().getPluginManager().registerEvents(new CancelFireFlowerDamage(), plugin);
        this.command = new HashMap();
        this.command.put("ws", new MainCommand(plugin));
        this.elytra = new HashMap();
        this.elytra.put("Elytra", false);
        this.startTaskRunnable();
        this.setGateMetadata();
    }

    public void onDisable() {
        this.stopAllTaskRunnable();
    }

    public void startTaskRunnable() {
        int wtltask = plugin.getConfig().getInt("tasktimer.wtltask") * 20;
        this.task.add((new ElytraBoosterTask()).runTaskTimer(this, 0L, (long)wtltask));
    }

    public void stopAllTaskRunnable() {
        Iterator var2 = this.task.iterator();

        while(var2.hasNext()) {
            BukkitTask task = (BukkitTask)var2.next();
            task.cancel();
        }

    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        return ((TabExecutor)this.command.get(cmd.getName())).onCommand(sender, cmd, label, args);
    }

    public void setGateMetadata() {
        String s;
        Iterator var2;
        String[] locations;
        int minx;
        int miny;
        int minz;
        int maxx;
        int maxy;
        int maxz;
        int x;
        int y;
        int z;
        Location loc;
        if (plugin.getConfig().getStringList("InvCheckGateList") != null) {
            var2 = plugin.getConfig().getStringList("InvCheckGateList").iterator();

            while(var2.hasNext()) {
                s = (String)var2.next();
                locations = plugin.getConfig().getString("InvCheckGate." + s).split(",");
                minx = Integer.valueOf(locations[0]);
                miny = Integer.valueOf(locations[1]);
                minz = Integer.valueOf(locations[2]);
                maxx = Integer.valueOf(locations[3]);
                maxy = Integer.valueOf(locations[4]);
                maxz = Integer.valueOf(locations[5]);

                for(x = minx; x <= maxx; ++x) {
                    for(y = miny; y <= maxy; ++y) {
                        for(z = minz; z <= maxz; ++z) {
                            loc = new Location(plugin.getServer().getWorld(locations[6]), (double)x, (double)y, (double)z);
                            loc.getBlock().setMetadata("WorldSystemInvCheckGate", new FixedMetadataValue(plugin, true));
                        }
                    }
                }
            }
        }

        if (plugin.getConfig().getStringList("InvCheckGatePlustList") != null) {
            var2 = plugin.getConfig().getStringList("InvCheckGatePlusList").iterator();

            while(var2.hasNext()) {
                s = (String)var2.next();
                locations = plugin.getConfig().getString("InvCheckGatePlus." + s).split(",");
                minx = Integer.valueOf(locations[0]);
                miny = Integer.valueOf(locations[1]);
                minz = Integer.valueOf(locations[2]);
                maxx = Integer.valueOf(locations[3]);
                maxy = Integer.valueOf(locations[4]);
                maxz = Integer.valueOf(locations[5]);

                for(x = minx; x <= maxx; ++x) {
                    for(y = miny; y <= maxy; ++y) {
                        for(z = minz; z <= maxz; ++z) {
                            loc = new Location(plugin.getServer().getWorld(locations[6]), (double)x, (double)y, (double)z);
                            loc.getBlock().setMetadata("WorldSystemInvCheckGatePlus", new FixedMetadataValue(plugin, true));
                        }
                    }
                }
            }
        }

    }
}
