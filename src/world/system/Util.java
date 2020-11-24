package world.system;

import com.earth2me.essentials.Essentials;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class Util {
    public Util() {
    }

    public static Essentials Essentials() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Essentials");
        return plugin != null && plugin instanceof Essentials ? (Essentials)plugin : null;
    }

    public static WorldEditPlugin getWorldEdit() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        return plugin instanceof WorldEditPlugin ? (WorldEditPlugin)plugin : null;
    }

    public static WorldGuardPlugin getWorldGuard() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
        return plugin != null && plugin instanceof WorldGuardPlugin ? (WorldGuardPlugin)plugin : null;
    }

    public static GriefPrevention getGriefPrevention() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("GriefPrevention");
        return plugin != null && plugin instanceof GriefPrevention ? (GriefPrevention)plugin : null;
    }

    public static MultiverseCore getMultiverseCore() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
        return plugin instanceof MultiverseCore ? (MultiverseCore)plugin : null;
    }
}
