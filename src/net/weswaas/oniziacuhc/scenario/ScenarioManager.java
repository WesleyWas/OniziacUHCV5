package net.weswaas.oniziacuhc.scenario;

import com.google.common.collect.ImmutableList;
import net.weswaas.oniziacuhc.Settings;
import net.weswaas.oniziacuhc.Timer;
import net.weswaas.oniziacuhc.scenario.scenarios.*;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class ScenarioManager {
	
	private final List<Scenario> scenarios = new ArrayList<Scenario>();
	
	public Scenario getScenario(String name){
		for(Scenario scen : scenarios){
			if(scen.getName().equalsIgnoreCase(name)){
				return scen;
			}
		}
		return null;
	}
	
	public Scenario getScenarioByMaterial(Material mat){
		for(Scenario scen : scenarios){
			if(scen.getMaterial().getType().equals(mat)){
				return scen;
			}
		}
		return null;
	}
	
	public List<Scenario> getScenarios(){
		return ImmutableList.copyOf(scenarios);
	}
	
	public List<Scenario> getDisabledScenarios(){
		List<Scenario> list = new ArrayList<Scenario>();
		
		for(Scenario scen : scenarios){
			if(!scen.isEnabled()){
				list.add(scen);
			}
		}
		return list;
	}
	
	public void registerScenarios(Timer timer, Timebomb timebomb, Settings settings){
		Cutclean cc = new Cutclean(this);
		
		scenarios.add(cc);
		scenarios.add(timebomb);
		scenarios.add(new InfiniteEnchanter());
		scenarios.add(new GoneFishing());
		scenarios.add(new GoldLess(cc));
		scenarios.add(new DiamondLess(cc));
		scenarios.add(new Bowless());
		scenarios.add(new Barebones(cc, timer));
		scenarios.add(new VanillaPlus());
		scenarios.add(new BloodDiamonds());
		scenarios.add(new Fireless());
		scenarios.add(new Limitations());
		scenarios.add(new Horseless(settings));
		scenarios.add(new Soup());
		scenarios.add(new TripleOres(cc));
		scenarios.add(new MiddleControl());
		scenarios.add(new GoldenRetriever());
		scenarios.add(new Timber());
		scenarios.add(new HasteyBoys());
		scenarios.add(new Backpacks());
		scenarios.add(new NoClean());
	}

	public ArrayList<Scenario> getEnabledScenarios() {
		ArrayList<Scenario> scens = new ArrayList<Scenario>();

		for (Scenario scen : this.scenarios) {
			if (scen.isEnabled()) {
				scens.add(scen);
			}
		}

		return scens;
	}

	public String getStringOfEnabledScenarios(){

		String scenarios = "Vanilla";

		if(this.getEnabledScenarios().size() > 0){
			StringBuilder list = new StringBuilder("");
			int i = 1;

			if(this.getEnabledScenarios().size() > 0){
				for(Scenario scen : this.getEnabledScenarios()){
					if(list.length() > 0){
						if(i == this.getEnabledScenarios().size()){
							list.append(" and ");
						}else{
							list.append(", ");
						}
					}

					list.append(scen.getName());
					i++;

				}
			}
			scenarios = list.toString().trim();
		}
		return scenarios;
	}

}
