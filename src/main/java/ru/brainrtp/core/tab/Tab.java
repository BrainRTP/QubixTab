package ru.brainrtp.core.tab;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import ru.brainrtp.core.Main;

public final class Tab{

    private Main main;

    public Tab(Main main) {
        this.main = main;
        this.Tab(main);
    }

    public void Tab(Main main) {
        main.getServer().getScheduler().runTaskTimer(main, new Runnable() {
            @Override
            public void run() {
                Scoreboard scoreboard = main.getServer().getScoreboardManager().getMainScoreboard();

                Team team = scoreboard.getTeam("team");
                if (team == null){
                    team = scoreboard.registerNewTeam("team");
                }
                team.setPrefix(format("&7"));
                team.setSuffix(format("&7"));

                for (Player player : main.getServer().getOnlinePlayers()){
                    team.addEntry(player.getName());
                }
            }
        },0, 40);
    }
    public String format(String string){
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}
