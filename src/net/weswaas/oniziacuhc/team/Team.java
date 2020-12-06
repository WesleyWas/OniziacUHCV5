package net.weswaas.oniziacuhc.team;

import net.weswaas.oniziacuhc.GameState;
import net.weswaas.oniziacuhc.OniziacUHC;
import net.weswaas.oniziacuhc.listeners.JoinListener;
import net.weswaas.oniziacuhc.managers.backpack.BackPack;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class Team {
	
	private static ArrayList<Team> Teams = new ArrayList<Team>();
	private ArrayList<String> playersUUID = new ArrayList<String>();
	private ArrayList<String> playersForever = new ArrayList<>();
	private Player leader;
	private BackPack bp;
	
	public Team(Player leader) {
		this.leader = leader;
		Teams.add(this);
		this.playersUUID.add(leader.getUniqueId().toString());
		this.playersForever.add(leader.getUniqueId().toString());
		bp = new BackPack(this);
	}

    public static ArrayList<Team> getTeams(){
        return Teams;
    }
	
	public void addPlayer(Player p){
		this.playersUUID.add(p.getUniqueId().toString());
		this.playersForever.add(p.getUniqueId().toString());
	}
	
	public void removePlayer(Player p){
		this.playersUUID.remove(p.getUniqueId().toString());
        if(playersUUID.size() == 0){
            Teams.remove(this);
        }
        if(GameState.isState(GameState.LOBBY)){
			playersForever.remove(p.getUniqueId().toString());
		}
	}
	
	public void removeTeam(){
		this.playersUUID.clear();
		Teams.remove(this);
	}

	public boolean isLastTeam(){
		ArrayList<Team> aliveTeams = new ArrayList<>();
		for(Team team : Teams){
			for(String s : team.getPlayerNames()){
				OfflinePlayer op = Bukkit.getOfflinePlayer(s);
				if(op.isOnline() && OniziacUHC.getInstance().getPlayers().contains(op.getPlayer())){
                    if(!aliveTeams.contains(team)){
                        aliveTeams.add(team);
                    }
				}
			}
		}
        return (aliveTeams.size() == 1);
    }


	public BackPack getBackPack(){
        return this.bp;
    }
	
	public ArrayList<String> getPlayerNames(){

		ArrayList<String> names = new ArrayList<>();

		for(String uuid : this.playersUUID){
			String pName = Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();
			names.add(pName);
		}

		return names;
	}

	public ArrayList<String> getPlayersForever(){
		ArrayList<String> array = new ArrayList<>();

		for(String s : this.playersForever){
			String name = Bukkit.getOfflinePlayer(UUID.fromString(s)).getName();
			array.add(name);
		}
		return array;
	}
	
	public Player getLeader(){
		return this.leader;
	}
	
	public ArrayList<String> getAlivePlayerNames(){
		ArrayList<String> list = new ArrayList<>();
		for(String s : getPlayerNames()){
			OfflinePlayer pls = net.weswaas.oniziacuhc.OniziacUHC.getInstance().getPlayerByName(s);
			if(pls.isOnline() || JoinListener.inRelog.contains(s)){
				list.add(pls.getName());
			}
		}
		return list;
	}
	
	public boolean isLeader(Player p){
		return p == this.leader;
	}
	
	public static Team getTeam(Player p){
        for(Team t : Teams){
            if(t.getPlayerNames().contains(p.getName())){
                return t;
            }
        }
        return null;
    }

    public static Team getTeamByName(String name){
        for(Team t : Teams){
            if(t.getPlayerNames().contains(name)){
                return t;
            }
        }
        return null;
    }

	public static int teamsRemain(){
		return Teams.size();
	}
	
	public static boolean hasTeam(Player p){
		return getTeam(p) != null;
	}

}
