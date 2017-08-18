package ru.brainrtp.core;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Config {
	//Все поля - приватные.
    public static Main main = Main.getPlugin(Main.class);
    public static File configf, presentsf, usersf;
    public static FileConfiguration config, presents, users;

    public static FileConfiguration getUsersConfig() {return users;}
    public static FileConfiguration getPresentsConfig() {return presents;}
    public static FileConfiguration getConfig() {return config;}

    public static void createFiles(JavaPlugin plugin) {

        configf = new File(getDataFolder(), "config.yml");
        presentsf = new File(getDataFolder(), "eastereggs.yml");
        usersf = new File(getDataFolder(), "users.yml");

        if (!configf.exists()) {
            configf.getParentFile().mkdirs();
            main.getLogger().info("config.yml not found, creating!");
            main.saveResource("config.yml", false);
        }
        if (!presentsf.exists()) {
            presentsf.getParentFile().mkdirs();
            main.getLogger().info("eastereggs.yml not found, creating!");
            main.saveResource("eastereggs.yml", false);
        }
        if (!usersf.exists()) {
            usersf.getParentFile().mkdirs();
            main.getLogger().info("users.yml not found, creating!");
            main.saveResource("users.yml", false);
        }
        config = new YamlConfiguration();
        presents = new YamlConfiguration();
        users = new YamlConfiguration();
        try {
			//Dereku start
			//Dereku end
            config.load(configf);
            presents.load(presentsf);
            users.load(usersf);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
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
