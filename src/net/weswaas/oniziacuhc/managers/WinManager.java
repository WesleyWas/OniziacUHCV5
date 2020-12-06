package net.weswaas.oniziacuhc.managers;


import net.weswaas.oniziacuhc.Game;
import net.weswaas.oniziacuhc.GameState;
import net.weswaas.oniziacuhc.Timer;
import net.weswaas.oniziacuhc.stats.PlayerDataManager;
import net.weswaas.oniziacuhc.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class WinManager implements Listener{
	
	private Game game;
	private PlayerDataManager data;
	private Timer timer;
	
	private boolean said = false;
	
	public WinManager(Game game, PlayerDataManager data, Timer timer) {
		
		this.game = game;
		this.data = data;
		this.timer = timer;
	}

	public void winCheck(){

		if(!GameState.isState(GameState.GAME)){
			return;
		}

		if(net.weswaas.oniziacuhc.OniziacUHC.getInstance().getPlayers().size() == 0){
            return;
        }

        Player p = net.weswaas.oniziacuhc.OniziacUHC.getInstance().getPlayers().get(0);

		int count = net.weswaas.oniziacuhc.OniziacUHC.getInstance().getPlayers().size();
		int teamSize = game.getTeamSize();

		if(!said){

            if(teamSize == 1){
                if(count == 1){
                    winEvent();
                }
            }
            else{
                Team team = Team.getTeam(p);
                if(team.isLastTeam()){
                    winEvent();
                }
            }

		}

	}




	
	public void checkWin(Player p){
		
		if(!GameState.isState(GameState.GAME)){
			return;
		}
		
		int playerSize = net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers.size();
		int teamSize = game.getTeamSize();
		int count = 0;
		
		if(!said){
			if(playerSize == teamSize){
				if(teamSize == 1){
					winEvent();
				}else{
					for(Player pls : net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers){
						if(pls != p){
							if(Team.getTeam(p).getPlayerNames().contains(pls.getName())){
								count++;
								if(count == teamSize){
									winEvent();
								}
							}
						}
					}
				}
			}else if(playerSize < teamSize){
				if(playerSize == 1){
					winEvent();
				}else{
					for(Player pls : net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers){
						if(pls != p){
							if(Team.getTeam(p).getPlayerNames().contains(pls.getName())){
								count++;
								if(count == teamSize){
									winEvent();
								}
							}
						}
					}
				}
			}
		}
		
		
		
	}
	
	@SuppressWarnings("deprecation")
	public void winEvent(){
		
		String winners = null;
		StringBuilder list = new StringBuilder("");
		int i = 1;

		if(game.getTeamSize() == 1){
            Player p = net.weswaas.oniziacuhc.OniziacUHC.getInstance().getPlayers().get(0);
			String uuid = p.getUniqueId().toString();
            String name = p.getName();
			winners = name;
			data.getStatsToStoreByUUID(uuid).addWin();
		}else{
			for(String s : Team.getTeam(net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers.get(0)).getPlayersForever()){
				if(s != null){
					Player pls = Bukkit.getPlayer(s);
					OfflinePlayer op = Bukkit.getOfflinePlayer(s);
					String uuid;
					if(pls == null){
						uuid = op.getUniqueId().toString();
					}else{
						uuid = pls.getUniqueId().toString();
					}
					data.getStatsToStoreByUUID(uuid).addWin();
					if(list.length() > 1){
						if(i == Team.getTeam(net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers.get(0)).getPlayersForever().size()){
							list.append(" and ");
						}else{
							list.append(", ");
						}
					}
					list.append(pls == null ? op.getName() : pls.getName());
					i++;
					winners = list.toString().trim();
				}

			}
		}
		
		Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Congratulations to " + winners + " for winning the UHC !");
		said = true;
		if(game.isStats()){
			sendStats();
		}
		timer.stopTimer();
	}
	
	private void sendStats(){
		new BukkitRunnable() {
			public void run() {
				
				Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Stats are getting stored in the database... Lags can be feeled...");
				data.sendStatsToBase();
				
			}
		}.runTaskLater(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 40);
	}

}
