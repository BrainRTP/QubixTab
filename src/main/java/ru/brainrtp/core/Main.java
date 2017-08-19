package ru.brainrtp.core;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import ru.brainrtp.core.chat.Chat;
import ru.brainrtp.core.tab.Tab;

import java.util.logging.Logger;

public final class Main extends JavaPlugin {

    // | CONSTS | ///////////////////////////////////////////////////////////////////////////////////////////////////////
    // Добавляем префикс, чтобы его потом дергать отсюда, а не писать его постоянно.
    private static String prefix = "§c[§fQubixCore§c] §7> §r";
    public static Main main;
    private PluginDescriptionFile pdfFile = this.getDescription();
    private final Logger _log = Logger.getLogger("Minecraft");


    // | ENABLE | ///////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public void onEnable() {
        main = this;

        final LuckPermsApi api = LuckPerms.getApi();
//
//        не уверен, но, возможно, нужно юзать вот это... Посмотрим
//
//        ServicesManager manager = Bukkit.getServicesManager();
//        if (manager.isProvidedFor(LuckPermsApi.class)) {
//            final LuckPermsApi api = manager.getRegistration(LuckPermsApi.class).getProvider();
//        }

        Config.createFiles(main);
        new Tab();
        new Chat(this);

        this._log.info("[QubixCore] " + pdfFile.getName() + " v" + pdfFile.getVersion() + " Enabled");
    }


    // | DISABLE | ///////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public void onDisable() {
        this._log.info("[QubixCore] " + pdfFile.getName() + " Disabled");
    }



    // | COMMANDS | ///////////////////////////////////////////////////////////////////////////////////////////////////////


    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("qq") || cmd.getName().equalsIgnoreCase("qubixcore") || cmd.getName().equalsIgnoreCase("qubix")) { // your command name
            if (!(sender instanceof Player)) { //this makes sure its a player executing the command
                Bukkit.getConsoleSender().sendMessage(prefix + "§cИспользуйте команду в игре!");
                return true;
            }
            Player player = (Player) sender;
            if (player.hasPermission("qubixcore.admin")) {

                if (args.length == 0) {
                    player.sendMessage(prefix + "Для подбробной информации напишите /" + cmd.getName() + " help");
                    return true;
                }

                switch (args[0]) {
                    case "help":
                        player.sendMessage("§7§m-------§a QubixCoer " + getDescription().getVersion() + " §7§m-------");
                        player.sendMessage("§e/qq help §7- Помощь.");
                        break;
                    default:
                        sender.sendMessage(prefix + ChatColor.RED + "Неизвестная команда.");
                }
            }

            return false;
        }
        return false;
    }



    // | OTHER | ///////////////////////////////////////////////////////////////////////////////////////////////////////


    public String format(String string){
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static boolean isPlayerInGroup(Player player, String group) {
        return player.hasPermission("group." + group);
    }

    // | PACKETS | ///////////////////////////////////////////////////////////////////////////////////////////////////////

//    public static void sendTabTitle(Player player, String header, String footer) {
//        if (header == null) header = "";
//        header = ChatColor.translateAlternateColorCodes('&', header);
//
//        if (footer == null) footer = "";
//        footer = ChatColor.translateAlternateColorCodes('&', footer);
//
//        TabTitle tabTitleSendEvent = new TabTitle(player, header, footer);
//
//        Bukkit.getPluginManager().callEvent(tabTitleSendEvent);
//        if (tabTitleSendEvent.isCancelled())
//            return;
//
//        header = header.replaceAll("%player%", player.getDisplayName());
//        footer = footer.replaceAll("%player%", player.getDisplayName());
//
//        try {
//            Object tabHeader = BukkitTool.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null,
//                    "{\"text\":\"" + header + "\"}");
//            Object tabFooter = BukkitTool.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null,
//                    "{\"text\":\"" + footer + "\"}");
//            Constructor<?> titleConstructor = BukkitTool.getNMSClass("PacketPlayOutPlayerListHeaderFooter").getConstructor();
//            Object packet = titleConstructor.newInstance();
//            Field aField = packet.getClass().getDeclaredField("a");
//            aField.setAccessible(true);
//            aField.set(packet, tabHeader);
//            Field bField = packet.getClass().getDeclaredField("b");
//            bField.setAccessible(true);
//            bField.set(packet, tabFooter);
//            sendPacket(player, packet);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//
//
//    static void sendPacket(Player p, Object packet) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, ClassNotFoundException {
//        Object nmsPlayer = p.getClass().getMethod("getHandle").invoke(p);
//        Object plrConnection = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
//        plrConnection.getClass().getMethod("sendPacket", BukkitTool.getNMSClass("Packet")).invoke(plrConnection, packet);
//    }
//
//    public static enum ClassType{
//        NMS,
//        CB;
//    }
//    public static String getNMSPackageName() {
//        return "net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
//    }
//
//    public static String getOBCPackageName(){
//        return "org.bukkit.craftbukkit" + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
//    }
//
//    public static Class getClass(String name){
//        try {
//            return Class.forName(name);
//        } catch (ClassNotFoundException e) {
//            System.out.print("Failed to find a valid class for: " + name);
//            return null;
//        }
//    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
