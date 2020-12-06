package net.weswaas.oniziacuhc;

public class Settings {

    private int maxPlayers = 100;
    private boolean horseHealing = true;
    private boolean horses = true;
    private boolean strength = false;
    private boolean invisibility = false;
    private boolean absorbtion = true;
    private boolean goldenHeads = true;
    private boolean godApples = false;
    private boolean nether = false;
    private boolean stats = true;

    public void setMaxPlayers(int maxPlayers){
        this.maxPlayers = maxPlayers;
    }

    public int getMaxPlayers(){
        return this.maxPlayers;
    }

    public void setHorseHealing(boolean horseHealing){
        this.horseHealing = horseHealing;
    }

    public boolean getHorseHealing(){
        return this.horseHealing;
    }

    public void setHorses(boolean horses){
        this.horses = horses;
    }

    public boolean getHorses(){
        return this.horses;
    }

    public void setStrength(boolean strength){
        this.strength = strength;
    }

    public boolean getStrength(){
        return this.strength;
    }

    public void setInvisibility(boolean invisibility){
        this.invisibility = invisibility;
    }

    public boolean getInvisibility(){
        return this.invisibility;
    }

    public void setAbsorbtion(boolean absorbtion){
        this.absorbtion = absorbtion;
    }

    public boolean getAbsorbtion(){
        return this.absorbtion;
    }

    public boolean getGoldenHeads() {
        return goldenHeads;
    }

    public void setGoldenHeads(boolean goldenHeads) {
        this.goldenHeads = goldenHeads;
    }

    public boolean getGodApples() {
        return godApples;
    }

    public void setGodApples(boolean godApples) {
        this.godApples = godApples;
    }

    public boolean getNether() {
        return nether;
    }

    public void setNether(boolean nether) {
        this.nether = nether;
    }

    public boolean getStats() {
        return stats;
    }

    public void setStats(boolean stats) {
        this.stats = stats;
    }

}
