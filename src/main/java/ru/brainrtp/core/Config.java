package ru.brainrtp.core;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Config {
    public static Main main = Main.getPlugin(Main.class);
    public static File configf;
    public static FileConfiguration config;

    public static FileConfiguration getConfig() {return config;}

    public static void createFiles(JavaPlugin plugin) {

        configf = new File(getDataFolder(), "config.yml");

        if (!configf.exists()) {
            configf.getParentFile().mkdirs();
            main.getLogger().info("config.yml не найден, создаю!!");
            main.saveResource("config.yml", false);
        }

        config = new YamlConfiguration();
        try {
            config.load(configf);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private static File getDataFolder() {return main.getDataFolder();}
    public static void save(File file, FileConfiguration filec) {
        try {
            filec.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
