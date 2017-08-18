package ru.brainrtp.core;

import java.io.IOException;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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


    // | COMMANDS | ///////////////////////////////////////////////////////////////////////////////////////////////////////


    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("qubix")) {
            switch(args[0]){
                case "help": sender.sendMessage(ChatColor.YELLOW+"QubixCore " + ChatColor.DARK_GRAY+"v"+pdfFile.getVersion()); break;
                default: sender.sendMessage(ChatColor.RED + "Неизвестная команда.");
            }


            if(args[0].equalsIgnoreCase("test")){
                //Player player = (Player) sender;
                //player.sendTitle("Ты че", "Сука", 0, 3, 0);
                //player.setPlayerListName(player.getDisplayName().replace("&", "§"));
                //TitleAPI.sendTabTitle(player, "Header", "Footer");
                //sender.setScoreboard(tab.);
                return false;
            }
            else if(args[0].equalsIgnoreCase("reload")){
                /*try {
                    loadConfig();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                sender.sendMessage(ChatColor.GREEN+"Конфиг перезагружен.");
                return false;
            }

        }
        return false;
    }



    // | OTHER | ///////////////////////////////////////////////////////////////////////////////////////////////////////


    public String format(String string){
        return ChatColor.translateAlternateColorCodes('&', string);
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
}
