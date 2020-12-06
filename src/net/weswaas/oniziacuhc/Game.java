package net.weswaas.oniziacuhc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Game {

    private Player host;
    private String hostName;
    private boolean damages = false;
    private boolean open = false;
    private int mapRadius = 1500;
    private int netherRadius = 185;
    private boolean isGenerated = false;
    private ArrayList<Location> locs;
    private int teamSize = 1;
    private boolean isStarted = false;
    private int currentBorder = mapRadius;
    private int currentNetherBorder = netherRadius;
    private boolean arenaGenerated = false;
    private boolean isMuted = false;
    private boolean isRush = false;
    private boolean isMapOK = false;
    private boolean stats = true;

    public void setHost(Player p){
        this.host = p;
        this.hostName = p.getDisplayName();
    }

    public void setMapOK(boolean ok){
        this.isMapOK = ok;
    }

    public boolean isMapOK(){
        return this.isMapOK;
    }

    public Player getHost(){
        return this.host;
    }

    public boolean isHost(String name){
        return name.equalsIgnoreCase(this.hostName);
    }

    public String getHostName(){
        return this.host == null ? "None" : getHost().getDisplayName();
    }

    public boolean hasHost(){
        return host != null;
    }

    public void setRush(boolean rush){
        this.isRush = rush;
    }

    public boolean isRush(){
        return this.isRush;
    }

    public void setDamages(boolean damages){
        this.damages = damages;
    }

    public boolean getDamage(){
        return this.damages;
    }

    public void setOpen(boolean open){
        this.open= open;
    }

    public boolean isOpen(){
        return this.open;
    }

    public void setRadius(int radius){
        this.mapRadius = radius;
        this.currentBorder = radius;
        this.netherRadius = radius / 8;
    }

    public int getRadius(){
        return this.mapRadius;
    }

    public void setGenerated(boolean generated){
        this.isGenerated = generated;
    }

    public boolean isGenerated(){
        return this.isGenerated;
    }

    public void setLocs(ArrayList<Location> locs){
        this.locs = locs;
    }

    public ArrayList<Location> getLocs(){
        return this.locs;
    }

    public void setTeamSize(int teamsize){
        this.teamSize = teamsize;
        Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Team size has been set to " + (this.teamSize == 1 ? "FFA" : "To" + this.getTeamSize()));
    }

    public int getTeamSize(){
        return this.teamSize;
    }

    public int getCurrentBorder() {
        return currentBorder;
    }

    public void setCurrentBorder(int currentBorder) {
        this.currentBorder = currentBorder;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean isStarted) {
        this.isStarted = isStarted;
    }

    public boolean isArenaGenerated() {
        return arenaGenerated;
    }

    public void setArenaGenerated(boolean arenaGenerated) {
        this.arenaGenerated = arenaGenerated;
    }

    public int getNetherRadius() {
        return netherRadius;
    }

    public int getCurrentNetherBorder() {
        return currentNetherBorder;
    }

    public void setCurrentNetherBorder(int currentNetherBorder) {
        this.currentNetherBorder = currentNetherBorder;
    }

    public void setMuted(boolean muted){
        this.isMuted = muted;
    }

    public boolean isMuted(){
        return this.isMuted;
    }

    public boolean isStats(){
        return this.stats;
    }

    public void setStats(boolean stats){
        this.stats = stats;
    }

}
