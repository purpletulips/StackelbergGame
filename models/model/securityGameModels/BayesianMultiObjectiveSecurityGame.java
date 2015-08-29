package model.securityGameModels;


import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.Configuration;
import model.genericGameModels.BayesianGenericGame;
import model.genericGameModels.GenericGame;

public class BayesianMultiObjectiveSecurityGame {
	public int numAttackerTypes;
	public int numObjectives;
	public int numTargets;
	public int numResources;
	public Map<MultiObjectiveSecurityGame, Double> bayesianGame;

	public static BayesianMultiObjectiveSecurityGame makeBayesianGame(MultiObjectiveSecurityGame mosg) {
		BayesianMultiObjectiveSecurityGame bg = new BayesianMultiObjectiveSecurityGame(1, mosg.numObjectives, mosg.numTargets, mosg.numResources);
		bg.bayesianGame.put(mosg, 1.0);
		return bg;
	}
	
	public BayesianMultiObjectiveSecurityGame(int numTypes, int numObjectives, int numTargets, int numResources) {
		super();
		this.numAttackerTypes = numTypes;
		this.numObjectives = numObjectives;
		this.numTargets = numTargets;
		this.numResources = numResources;
		this.bayesianGame = new HashMap<MultiObjectiveSecurityGame, Double>();
	}
	
	public void initialize() {
		double sumProbabilityComplement = 1.0;
		for(int ty = 1; ty <= this.numAttackerTypes - 1; ty++) {
			double probability = Configuration.randomGenerator.nextDouble() * sumProbabilityComplement;
			sumProbabilityComplement = sumProbabilityComplement - probability;
			MultiObjectiveSecurityGame at1 = new MultiObjectiveSecurityGame(this.numObjectives, this.numTargets, this.numResources);
			at1.initialize();
			bayesianGame.put(at1, probability);
		}
		MultiObjectiveSecurityGame atFinal = new MultiObjectiveSecurityGame(this.numObjectives, this.numTargets, this.numResources);
		atFinal.initialize();
		bayesianGame.put(atFinal, sumProbabilityComplement);		
	}
	
	/*
	public BayesianGenericGame getGenericGame() {		
		BayesianGenericGame bg = new BayesianGenericGame(this.numAttackerTypes, 1, this.numTargets);
		for(MultiObjectiveSecurityGame mosg : bayesianGame.keySet()){
			GenericGame gg = sg.getGenericGame();
			bg.bayesianGame.put(gg, this.bayesianGame.get(sg));
			bg.numLeaderActions = gg.numLeaderActions;
		}
		return bg;
	}
	*/

	public Set<Integer> getSetGameIDs() {
		Set<Integer> setGameIds = new HashSet<Integer>();
		for(MultiObjectiveSecurityGame g : this.bayesianGame.keySet()){
			setGameIds.add(g.id);
		}
		return setGameIds;
	}
	
	public Set<MultiObjectiveTarget> unionAllTargets() {
		Set<MultiObjectiveTarget> setTarget = new HashSet<MultiObjectiveTarget>();
		for(MultiObjectiveSecurityGame mosg : bayesianGame.keySet()){
			setTarget.addAll(mosg.targetList);
		}
		return setTarget;
	}
	
	/**
	 * Generates a restricted game with only the ids given in listGameIds
	 * @param listGameIds
	 * @return
	 */
	/*
	public BayesianMultiObjectiveSecurityGame getRestrictedGame(List<Integer> listGameIds) {
		BayesianMultiObjectiveSecurityGame restrictedBSG = new BayesianMultiObjectiveSecurityGame(listGameIds.size(), this.numTargets, this.numResources);
		for(SecurityGame g : this.bayesianGame.keySet()){
			if(listGameIds.contains(g.id)) {
				restrictedBSG.bayesianGame.put(g, this.bayesianGame.get(g));
			}
		}
		return restrictedBSG;
	}
	*/
	
	@Override
	public String toString() {
		return "BayesianSecurityGame [numAttackerTypes=" + numAttackerTypes
				+ ", numTargets=" + numTargets + ", numResources="
				+ numResources + ", bayesianGame=" + bayesianGame + "]";
	}	
}
