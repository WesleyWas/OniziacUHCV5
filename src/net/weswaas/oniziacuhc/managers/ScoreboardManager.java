package net.weswaas.oniziacuhc.managers;

import net.weswaas.oniziacuhc.Game;
import net.weswaas.oniziacuhc.Timer;
import net.weswaas.oniziacuhc.listeners.DeathListener;
import net.weswaas.oniziacuhc.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class ScoreboardManager {
	
	private Timer timer;
	private Game game;
	
	public ScoreboardManager(Timer timer, Game game) {
		
		this.timer = timer;
		this.game = game;
	}
	
	public HashMap<Scoreboard, Player> boards = new HashMap<Scoreboard, Player>();
	
	public void updater(){
		new BukkitRunnable() {
			public void run() {
				for(Scoreboard board : boards.keySet()){
					Player p = boards.get(board);

                    if(board.getTeam("score2") == null || board.getTeam("score4") == null || board.getTeam("score6") == null || board.getTeam("score8") == null || board.getTeam("score10") == null){
                        boards.remove(p);
                        createSidebar(p);
                        return;
                    }

					int timeforsc = (timer.getGameTimeSeconds() * 1000);
					int kills = (DeathListener.pKills.get(p.getName()) == null ? 0 : DeathListener.pKills.get(p.getName()));

					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
					sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

					String time = sdf.format(new Date(timeforsc));

					Map.Entry<String, Integer> map = PlayerUtils.getKTEntry();
					String name = map.getKey();
					int kt = map.getValue();

                    String nameForSC = "";

                    if(name.length() > 9){
                        nameForSC = name.substring(0, 9);
                    }else{
                        nameForSC = name;
                    }

                    if(kt == 0){
                        nameForSC = "None";
                    }

					board.getTeam("score2").setSuffix("§f" + time);
					board.getTeam("score4").setSuffix("§f" + net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers.size());
					board.getTeam("score6").setSuffix("§f" + kills);
					board.getTeam("score8").setSuffix("§f" + game.getCurrentBorder());
                    board.getTeam("score10").setSuffix("§f" + nameForSC + " - " + kt);
				}
			}
		}.runTaskTimer(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 10, 10);
	}
	
	public void createSidebar(Player p){
		
		int timeforsc = (timer.getGameTimeSeconds() * 1000);
		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		
		String time = sdf.format(new Date(timeforsc));

		Map.Entry<String, Integer> map = PlayerUtils.getKTEntry();
		String name = map.getKey();
		int kt = map.getValue();

        String nameForSC = "";

        if(name.length() > 9){
            nameForSC = name.substring(0, 9);
        }else{
            nameForSC = name;
        }

        if(kt == 0){
            nameForSC = "None";
        }
		
		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
		
		Objective obj = board.registerNewObjective("uhc", "sc");
		Objective health = board.registerNewObjective("heal", "health");
		Objective tab = board.registerNewObjective("healthtab", "health");
		
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName("§eOniziacUHC");
		
		health.setDisplaySlot(DisplaySlot.BELOW_NAME);
		health.setDisplayName("§4♥");
		
		tab.setDisplaySlot(DisplaySlot.PLAYER_LIST);
		tab.setDisplayName("§e");
		
		Team score1 = board.registerNewTeam("score1");
		score1.setPrefix("§8» §cGame Time");
		score1.setSuffix("§c:");
		score1.addEntry(ChatColor.AQUA.toString());
		
		Team score2 = board.registerNewTeam("score2");
		score2.setPrefix("§8» ");
		score2.setSuffix("§f" + time);
		score2.addEntry(ChatColor.BLACK.toString());
		
		Team score3 = board.registerNewTeam("score3");
		score3.setPrefix("§8» §cPlayers");
		score3.setSuffix("§c:");
		score3.addEntry(ChatColor.BLUE.toString());
		
		Team score4 = board.registerNewTeam("score4");
		score4.setPrefix("§8» ");
		score4.setSuffix("§f" + net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers.size());
		score4.addEntry(ChatColor.BOLD.toString());
		
		Team score5 = board.registerNewTeam("score5");
		score5.setPrefix("§8» §cKills");
		score5.setSuffix("§c:");
		score5.addEntry(ChatColor.DARK_AQUA.toString());
		
		Team score6 = board.registerNewTeam("score6");
		score6.setPrefix("§8» ");
		score6.setSuffix("§f" + (DeathListener.pKills.get(p.getName()) == null ? 0 : DeathListener.pKills.get(p.getName())));
		score6.addEntry(ChatColor.DARK_BLUE.toString());
		
		Team score7 = board.registerNewTeam("score7");
		score7.setPrefix("§8» §cBorder");
		score7.setSuffix("§c:");
		score7.addEntry(ChatColor.DARK_GRAY.toString());
		
		Team score8 = board.registerNewTeam("score8");
		score8.setPrefix("§8» ");
		score8.setSuffix("§f" + game.getCurrentBorder());
		score8.addEntry(ChatColor.DARK_GREEN.toString());

		Team score9 = board.registerNewTeam("score9");
		score9.setPrefix("§8» §cTOP KT");
		score9.setSuffix("§c:");
		score9.addEntry(ChatColor.RED.toString());

		Team score10 = board.registerNewTeam("score10");
		score10.setPrefix("§8» ");
		score10.setSuffix("§f" + nameForSC + " - " + kt);
		score10.addEntry(ChatColor.LIGHT_PURPLE.toString());

        Team score11 = board.registerNewTeam("score11");
        score11.setPrefix("§8» §7Twitter:");
        score11.setSuffix("§f");
        score11.addEntry(ChatColor.GOLD.toString());

        Team score12 = board.registerNewTeam("score12");
        score12.setPrefix("§8»§b @Oniziac");
        score12.setSuffix("§f");
        score12.addEntry(ChatColor.DARK_PURPLE.toString());

        obj.getScore(ChatColor.AQUA.toString()).setScore(12);
        obj.getScore(ChatColor.BLACK.toString()).setScore(11);
		obj.getScore(ChatColor.BLUE.toString()).setScore(10);
		obj.getScore(ChatColor.BOLD.toString()).setScore(9);
		obj.getScore(ChatColor.DARK_AQUA.toString()).setScore(8);
		obj.getScore(ChatColor.DARK_BLUE.toString()).setScore(7);
		obj.getScore(ChatColor.DARK_GRAY.toString()).setScore(6);
		obj.getScore(ChatColor.DARK_GREEN.toString()).setScore(5);
        obj.getScore(ChatColor.RED.toString()).setScore(4);
        obj.getScore(ChatColor.LIGHT_PURPLE.toString()).setScore(3);
        obj.getScore(ChatColor.GOLD.toString()).setScore(2);
        obj.getScore(ChatColor.DARK_PURPLE.toString()).setScore(1);
		
		boards.put(board, p);
		
		p.setScoreboard(board);
	}

}
