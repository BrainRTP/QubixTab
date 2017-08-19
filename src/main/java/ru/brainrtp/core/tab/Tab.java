package ru.brainrtp.core.tab;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import ru.brainrtp.core.Config;
import ru.brainrtp.core.Main;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class Tab implements Listener {

    private String header;
    private String footer;
//    private String prefix;
//    private String suffix;
    private Team team;

    public Tab(){
        //Т.к не динамический TAB, то присваиваем сразу значения, которые берем из конфига
        this.footer = Config.getConfig().getString("Footer");
        this.header = Config.getConfig().getString("Header");
//        this.prefix = Config.getConfig().getString("Prefix");
//        this.suffix = Config.getConfig().getString("Suffix");
        this.setSuffixPrefix();
        this.setHeaderFooter();
        Bukkit.getPluginManager().registerEvents((Listener)this, Main.getPlugin(Main.class));
    }

    private void setSuffixPrefix() {
        Scoreboard scoreboard = Main.getPlugin(Main.class).getServer().getScoreboardManager().getMainScoreboard();

        //Создание тим
        team = scoreboard.getTeam("default");
        if (team == null) {
            team = scoreboard.registerNewTeam("default");
        }
        team = scoreboard.getTeam("admin");
        if (team == null) {
            team = scoreboard.registerNewTeam("admin");
        }

        for (Player player : Main.getPlugin(Main.class).getServer().getOnlinePlayers()) {
            if (Main.isPlayerInGroup(player, "admin")){
                //"Выбираем" тиму, которую нужно редактировать
                team = scoreboard.getTeam("admin");

                //Берез из конфига TabNick -> admin
                String input = format(Config.getConfig().getConfigurationSection("TabNick").getString("admin"));
                //Разделяем него по "<name>", чтобы prefixSuffix был такого типа:
                // [0] - префикс
                // [1] - суффикс
                final String[] prefixSuffix = input.split("<name>");

                //Есть admin: "&cАдмин &f<name>"
                //prefixSuddix[0] = "&cАдмин "
                //Разделяем на пробел и получаем префик без пробела, то бишь "&cАдмин" (чтобы сэкономить один символ)
                team.setPrefix(format(prefixSuffix[0].split(" ")[0]));
                team.setSuffix(format((prefixSuffix.length > 1) ? prefixSuffix[1] : ""));
            } else {
                team = scoreboard.getTeam("default");

                String input = format(Config.getConfig().getConfigurationSection("TabNick").getString("default"));
                final String[] prefixSuffix = input.split("<name>");

                team.setPrefix(format(prefixSuffix[0]));
                team.setSuffix(format((prefixSuffix.length > 1) ? prefixSuffix[1] : ""));

            }
            //Добавляем в тиму игрока
            team.addEntry(player.getName());
        }
    }
    private void setHeaderFooter() {
        for (Player player : Main.getPlugin(Main.class).getServer().getOnlinePlayers()) {
            try {
                //Вывод хедера/футера с плейсхолдером %player%
                Object tabHeader = BukkitTool.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null,
                        "{\"text\":\"" + format(header).replace("%player%", player.getName()) + "\"}");
                Object tabFooter = BukkitTool.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null,
                        "{\"text\":\"" + format(footer).replace("%player%", player.getName()) + "\"}");
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
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        setHeaderFooter();
        setSuffixPrefix();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        //Чтобы не забивать память, удаляем игрока из тимы при его выходе
        team.removeEntry(event.getPlayer().getName());
    }

    private String format(String string){
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    static void sendPacket(Player p, Object packet) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, ClassNotFoundException {
        Object nmsPlayer = p.getClass().getMethod("getHandle").invoke(p);
        Object plrConnection = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
        plrConnection.getClass().getMethod("sendPacket", BukkitTool.getNMSClass("Packet")).invoke(plrConnection, packet);
    }

}
