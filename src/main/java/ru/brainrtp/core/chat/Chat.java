package ru.brainrtp.core.chat;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import ru.brainrtp.core.Config;
import ru.brainrtp.core.Main;

public class Chat implements Listener{

    private Main main;
    private String[] prefixSuffix;

    public Chat(Main main){
        this.main = main;
        Bukkit.getPluginManager().registerEvents((Listener)this, main);
    }

    @EventHandler
    public void chat(AsyncPlayerChatEvent event) {

        if (Main.isPlayerInGroup(event.getPlayer(), "admin")) {
            String input = Config.getConfig().getConfigurationSection("TabNick").getString("admin").replace('&', '§');
            prefixSuffix = input.split("<name>");
        } else {
            String input = Config.getConfig().getConfigurationSection("TabNick").getString("default").replace('&', '§');
            prefixSuffix = input.split("<name>");
        }
        event.setFormat("[" + prefixSuffix[0].replace(" ", "") + "§f] " + ((prefixSuffix[0].split(" ").length == 2) ?  prefixSuffix[0].split(" ")[1] : "§7")  + event.getPlayer().getName() + ((prefixSuffix.length > 1) ? prefixSuffix[1] : "") + " §f> " + event.getMessage());
    }
}
