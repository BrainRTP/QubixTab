package ru.brainrtp.core;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import ru.brainrtp.core.chat.Chat;
import ru.brainrtp.core.scoreboard.AnimatedScoreBoard;
import ru.brainrtp.core.tab.Tab;

public final class Main extends JavaPlugin {
    public static Main main;

    @Override
    public void onEnable() {
        main = this;
        new Tab(this);
        new AnimatedScoreBoard();

        //
        new Chat(this);
    }

    public String format(String string){
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

//iuhagyvcfsd

}
