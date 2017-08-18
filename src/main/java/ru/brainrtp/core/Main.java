package ru.brainrtp.core;

import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import ru.brainrtp.core.chat.Chat;
import ru.brainrtp.core.scoreboard.AnimatedScoreBoard;
import ru.brainrtp.core.tab.Tab;

public final class Main extends JavaPlugin {

    // | CONSTS | ///////////////////////////////////////////////////////////////////////////////////////////////////////

    public static Main main;
    private PluginDescriptionFile pdfFile = this.getDescription();
    private final Logger _log = Logger.getLogger("Minecraft");


    // | ENABLE | ///////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public void onEnable() {
        main = this;
        new Tab(this);
        new AnimatedScoreBoard();
        new Chat(this);

        this._log.info("[QubixCore] " + pdfFile.getName() + " v" + pdfFile.getVersion() + " Enabled");
    }


    // | DISABLE | ///////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public void onDisable() {
        this._log.info("[QubixCore] " + pdfFile.getName() + " Disabled");
    }


    // | OTHER | ///////////////////////////////////////////////////////////////////////////////////////////////////////


    public String format(String string){
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////

}
