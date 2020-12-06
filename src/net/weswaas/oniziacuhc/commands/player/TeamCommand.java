package net.weswaas.oniziacuhc.commands.player;

import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.PacketPlayOutChat;
import net.minecraft.server.v1_7_R4.PlayerConnection;
import net.weswaas.oniziacuhc.Game;
import net.weswaas.oniziacuhc.GameState;
import net.weswaas.oniziacuhc.commands.UHCCommand;
import net.weswaas.oniziacuhc.team.Team;
import net.weswaas.oniziacuhc.utils.SoundsUtils;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TeamCommand extends UHCCommand{
	
	private Game game;
	
	private HashMap<String, String> invites = new HashMap<String, String>();

	public TeamCommand(Game game) {
		super("team", "/team <value>");
		
		this.game = game;
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		
		if(sender instanceof Player){
			Player p = (Player) sender;
			
				if(game.getTeamSize() > 1){
					
					if(args.length == 0){
						p.sendMessage("§a====================================================");
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Team commands:");
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§a");
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "/team invite §7» Invite a player to your team.");
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "/team accept <player> §7» Accept the invitation.");
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "/team leave §7» Leave your current team.");
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "/team kick §7» Kick a player in your team.");
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "/team list <player> §7» List of player in a team.");
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "/team sc §7» Send coords to your teammates.");
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "/team chat §7» Talk in your team chat");
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§0");
						p.sendMessage("§a====================================================");
					}
					
					else if(args[0].equalsIgnoreCase("list")){
						if(args.length == 1){
							if(Team.hasTeam(p)){
								
								String s = null;
								StringBuilder list = new StringBuilder("");
								int i = 1;
								
								for(String s1 : Team.getTeam(p).getPlayersForever()){
									if(list.length() > 1){
										if(i == Team.getTeam(p).getPlayerNames().size()){
											list.append(" §aand ");
										}else{
											list.append(", ");
										}
									}
									OfflinePlayer pls = Bukkit.getOfflinePlayer(UUID.fromString(s1));
									if(pls != null && pls.isOnline()){
										if(net.weswaas.oniziacuhc.OniziacUHC.getInstance().getPlayerByName(pls.getName()) != null){
											list.append("§a" + s1);
										}else{
											list.append("§c" + s1 + "§a");
										}
									}else{
										list.append("§c" + s1 + "§a");
									}
									i++;
									s = list.toString().trim();
								}
								
								p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Players in " + Team.getTeam(p).getLeader().getName() + "'s team §8» " + s);
								
							}else{
								p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cYou are not in a team.");
							}
						}else if(args.length == 2){
							Player target = Bukkit.getPlayer(args[1]);
							if(target != null && target.isOnline()){
								if(Team.hasTeam(target)){
									
									String s = null;
									StringBuilder list = new StringBuilder("");
									int i = 1;
									
									for(String s1 : Team.getTeam(target).getPlayersForever()){
										if(list.length() > 1){
											if(i == Team.getTeam(target).getPlayersForever().size()){
												list.append(" and ");
											}else{
												list.append(", ");
											}
										}
										
										OfflinePlayer pls = Bukkit.getOfflinePlayer(s1);
										if(pls != null && pls.isOnline()){
											if(net.weswaas.oniziacuhc.OniziacUHC.getInstance().getPlayerByName(pls.getName()) != null){
												list.append("§a" + s1);
											}else{
												list.append("§c" + s1);
											}
										}else{
											list.append("§c" + s1);
										}
										i++;
										s = list.toString().trim();
									}
									
									p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Players in " + Team.getTeam(target).getLeader().getName() + "'s team §8» " + s);
									
								}else{
									p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + ChatColor.RED + target.getName() + " is not in a team.");
								}
							}else{
								p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cPlayer can't be found.");
							}
						}else{
							p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cInvalid synthax. Please try with /team list <player>");
						}
					}
					
					else if(args[0].equalsIgnoreCase("leave")){
						if(GameState.isState(GameState.LOBBY)){
							if(Team.hasTeam(p)){
								
								if(Team.getTeam(p).isLeader(p)){
									for(String s : Team.getTeam(p).getPlayerNames()){
										Player pls = Bukkit.getPlayer(s);
										pls.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + p.getName() + " removed the team.");
										new SoundsUtils(pls).playSounds(Sound.ANVIL_USE);
									}
									Team.getTeam(p).removeTeam();
								}else{
									p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "You left " + Team.getTeam(p).getLeader().getName() + "'s team.");
									for(String s : Team.getTeam(p).getPlayerNames()){
										Player pls = Bukkit.getPlayer(s);
										if(pls != p){
											pls.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + p.getName() + " left the team.");
										}
									}
									Team.getTeam(p).removePlayer(p);
								}
								
							}else{
								p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cYou are not in a team.");
							}
						}
					}
					
					else if(args[0].equalsIgnoreCase("accept")){
						if(GameState.isState(GameState.LOBBY)){
							if(args.length > 1){
								Player target = Bukkit.getPlayer(args[1]);
								if(target != null && target.isOnline()){
									if(!(game.getTeamSize() <= Team.getTeam(target).getPlayerNames().size())){
										if(this.invites.containsValue(p.getName())){
											if(this.invites.containsKey(target.getName())){
												Team.getTeam(target).addPlayer(p);
												for(String s : Team.getTeam(p).getPlayerNames()){
													Player pls = Bukkit.getPlayer(s);
													pls.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + p.getName() + " joined the team.");
												}
											}
										}else{
											p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cYou are not invited in any team.");
										}
									}else{
										p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cThis team is full.");
									}
								}else{
									p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cPlayer can't be found.");
								}
							}else{
								p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cInvalid synthax. Please try with /team accept <player>");
							}
						}
					}
					
					else if(args[0].equalsIgnoreCase("invite")){
						if(GameState.isState(GameState.LOBBY)){
							if(!Team.hasTeam(p)){
								new Team(p);
							}
							if(Team.getTeam(p).isLeader(p)){
								try{
									
									Player target = Bukkit.getPlayer(args[1]);
									if(target != null && target.isOnline()){
										
										if(!Team.hasTeam(target)){
											
											this.invites.put(p.getName(), target.getName());
											for(String s : Team.getTeam(p).getPlayerNames()){
												Player pls = Bukkit.getPlayer(s);
												pls.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + target.getName() + " has been invited to the team.");
											}
											sendRawMessage(target, p);
											
										}else{
											p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cThis player is already in a team.");
										}
										
									}else{
										p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cPlayer can't be found.");
									}
									
								}catch (Exception e){
									p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cAn internal error occured.");
									e.printStackTrace();
								}
								
							}else{
								p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cYou are not the leader of your team.");
							}
						}
					}
					
					else if(args[0].equalsIgnoreCase("kick")){
						if(GameState.isState(GameState.LOBBY)){
							if(Team.hasTeam(p)){
								if(Team.getTeam(p).isLeader(p)){
									try{
										Player target = Bukkit.getPlayer(args[1]);
										if(Team.getTeam(p).getPlayerNames().contains(target.getName())){
											if(target != null && target.isOnline()){
												if(target != p){
												
													Team.getTeam(p).removePlayer(target);
													target.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "You've been kicked from " + Team.getTeam(p).getLeader().getName() + "'s team.");
													for(String s : Team.getTeam(p).getPlayerNames()){
														Player pls = Bukkit.getPlayer(s);
														pls.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + target.getName() + " has been kicked from the team.");
													}
												}
												
											}else{
												p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cPlayer can't be found.");
											}
										}else{
											p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cThis player is not in your team.");
										}
									}catch (Exception e){
										p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cAn internal error occured.");
									}
								}else{
									p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cYou are not the leader of your team.");
								}
							}else{
								p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cYou are not in a team.");
							}
						}
						
						
					}else if(args[0].equalsIgnoreCase("sc")){
						if(Team.hasTeam(p)){
							for(String s : Team.getTeam(p).getPlayerNames()){
								Player teamPlayer = Bukkit.getPlayer(s);
								Location loc = p.getLocation();
								teamPlayer.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§b[Team]" + p.getName() + ": " + ChatColor.AQUA + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
							}
						}else{
							p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cYou need a team to send coords !");
						}
					}
					
					else if(args[0].equalsIgnoreCase("set")){
						if(p.hasPermission("uhc.teams.manage")){
							if(args.length > 1){
								if(args[0].equalsIgnoreCase("set")){
									if(args.length == 2){
										game.setTeamSize(Integer.valueOf(args[1]));
									}else{
										p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cInvalid synthax. Please try with /team set <value>");
									}
								}
							}else{
								p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cInvalid synthax. Please try with /team set <value>.");
							}
						}	
					}
					
					else if(args[0].equalsIgnoreCase("chat")){
						if(Team.hasTeam(p)){
							if(args.length > 1){
								
								StringBuilder list = new StringBuilder("");
								
								for(String s : args){
									if(s != args[0]){
										list.append(s + " ");
									}
								}
								
								for(String s : Team.getTeam(p).getPlayerNames()){
									Player pls = Bukkit.getPlayer(s);
									pls.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§b[TeamChat]" + p.getName() + "§8 »§f " + list.toString().trim());
								}
								
							}else{
								p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cInvalid synthax. Please try with /team chat <sentence>");
							}
						}else{
							p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cYou need a team to talk with them.");
						}
					}
					
				}else{
					if(p.hasPermission("uhc.teams.manage")){
						if(args.length > 0){
							if(args[0].equalsIgnoreCase("set")){
								if(args.length == 2){
									game.setTeamSize(Integer.valueOf(args[1]));
								}else{
									p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cInvalid synthax. Please try with /team set <value>");
								}
							}
						}else{
							p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cTeams are acually disabled. Ask the host to enable them.");
						}
						
					}else{
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cTeams are acually disabled. Ask the host to enable them.");
					}
				}
		}
		
		return false;
	}

	private void sendRawMessage(Player p, Player target){
		PlayerConnection connection = ((CraftPlayer)p).getHandle().playerConnection;
		PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§3You've been invited to join §b" + p.getName() + "§3's team. §bClick HERE §3to join his team." + "\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/team accept " + target.getName() + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"§bClick here to join " + target.getName() + "'s team\",\"color\":\"aqua\"}]}}}"));
		connection.sendPacket(packet);
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String[] args) {
		return null;
	}

}
