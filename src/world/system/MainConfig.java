package world.system;

import org.bukkit.configuration.file.FileConfiguration;

public class MainConfig {
    private static FileConfiguration config;
    private WorldSystem mplugin;

    MainConfig(WorldSystem plugin) {
        this.mplugin = plugin;
        this.mplugin.saveDefaultConfig();
    }

    public void loadConfig() {
        config = this.mplugin.getConfig();
    }

    public void reloadConfig() {
        this.mplugin.reloadConfig();
        config = this.mplugin.getConfig();
    }

    public void saveConfig() {
        this.mplugin.saveConfig();
    }

    public void saveDefaultConfig() {
        this.mplugin.saveDefaultConfig();
    }

    public FileConfiguration getConfig() {
        return this.mplugin.getConfig();
    }
}
