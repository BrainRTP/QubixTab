package ru.brainrtp.core;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import ru.brainrtp.core.chat.Chat;
import ru.brainrtp.core.scoreboard.AnimatedScoreBoard;
import ru.brainrtp.core.tab.BukkitTool;
import ru.brainrtp.core.tab.Tab;
import ru.brainrtp.core.tab.TabTitle;
import ru.brainrtp.core.test.Example.*;

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
        //new AnimatedScoreBoard();
        new Chat(this);

        /*getServer().getPluginManager().registerEvents(new EventListener(this), this);
        getCommand("example").setExecutor(new CommandListener(this));
        new Scheduler(this).runTaskTimer(this, 0, 20);
        cfgs = new Configs();
        cfgs.add(this, "config");
        CustomConfig cfg = cfgs.get("name1");
        if (!cfg.getCfg().contains("name1")) {
            cfg.getCfg().set("name1", "value1");
            cfg.saveCfg();
        }*/


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

            if (command.getName().equalsIgnoreCase("mytab")) {

                this.getServer().getScheduler().runTaskTimer(this, new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Object tabHeader = BukkitTool.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + ChatColor.GREEN + "QUBIX" + "\"}");
                            Object tabFooter = BukkitTool.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + ChatColor.GREEN + "KRYTO" + "\"}");
                            Constructor<?> titleConstructor = BukkitTool.getNMSClass("PacketPlayOutPlayerListHeaderFooter").getConstructor();
                            Object packet = titleConstructor.newInstance();
                            Field aField = packet.getClass().getDeclaredField("a");
                            aField.setAccessible(true);
                            aField.set(packet, tabHeader);
                            Field bField = packet.getClass().getDeclaredField("b");
                            bField.setAccessible(true);
                            bField.set(packet, tabFooter);
                            sendPacket((Player) sender, packet);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }
                }, 0, 40);
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


    // | PACKETS | ///////////////////////////////////////////////////////////////////////////////////////////////////////


    public static void sendTabTitle(Player player, String header, String footer) {
        if (header == null) header = "";
        header = ChatColor.translateAlternateColorCodes('&', header);

        if (footer == null) footer = "";
        footer = ChatColor.translateAlternateColorCodes('&', footer);

        TabTitle tabTitleSendEvent = new TabTitle(player, header, footer);

        Bukkit.getPluginManager().callEvent(tabTitleSendEvent);
        if (tabTitleSendEvent.isCancelled())
            return;

        header = header.replaceAll("%player%", player.getDisplayName());
        footer = footer.replaceAll("%player%", player.getDisplayName());

        try {
            Object tabHeader = BukkitTool.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + header + "\"}");
            Object tabFooter = BukkitTool.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + footer + "\"}");
            Constructor<?> titleConstructor = BukkitTool.getNMSClass("PacketPlayOutPlayerListHeaderFooter").getConstructor();
            Object packet = titleConstructor.newInstance();
            Field aField = packet.getClass().getDeclaredField("a");
            aField.setAccessible(true);
            aField.set(packet, tabHeader);
            Field bField = packet.getClass().getDeclaredField("b");
            bField.setAccessible(true);
            bField.set(packet, tabFooter);
            sendPacket(player, packet);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    static void sendPacket(Player p, Object packet) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, ClassNotFoundException {
        Object nmsPlayer = p.getClass().getMethod("getHandle").invoke(p);
        Object plrConnection = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
        plrConnection.getClass().getMethod("sendPacket", BukkitTool.getNMSClass("Packet")).invoke(plrConnection, packet);
    }

    public static enum ClassType{
        NMS,
        CB;
    }

    public static String getNMSPackageName() {
        return "net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
    }

    public static String getOBCPackageName(){
        return "org.bukkit.craftbukkit" + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
    }

    public static Class getClass(String name){
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            System.out.print("Failed to find a valid class for: " + name);
            return null;
        }
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
