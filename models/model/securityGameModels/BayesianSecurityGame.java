package model.securityGameModels;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.Configuration;
import model.genericGameModels.BayesianGenericGame;
import model.genericGameModels.GenericGame;

public class BayesianSecurityGame {
	public int numAttackerTypes;
	public int numTargets;
	public int numResources;
	boolean createDummyHomeTarget;
	Map<SecurityGame, SingleObjectiveTarget> mapDummyHomeTargets;
	
	public Map<SecurityGame, Double> bayesianGame;

	public static BayesianSecurityGame makeBayesianGame(SecurityGame gg) {
		BayesianSecurityGame bg = new BayesianSecurityGame(1, gg.numTargets, gg.numResources);
		bg.bayesianGame.put(gg, 1.0);
		bg.createDummyHomeTarget = gg.createDummyHomeTarget;
		return bg;
	}
	
	public BayesianSecurityGame(int numTypes, int numTargets, int numResources) {
		super();
		this.bayesianGame = new HashMap<SecurityGame, Double>();
		this.createDummyHomeTarget = false;
		this.numAttackerTypes = numTypes;
		this.numTargets = numTargets;
		this.numResources = numResources;		
	}
	
	public void initializeFixed(double defCovPayoff, double defUncovPayoff,
			double attCovPayoff, double attUncovPayoff) {
		List<Integer> weights = new ArrayList<Integer>();
		int sum = 0;
		for ( int ty=1; ty<=this.numAttackerTypes; ty++) {
			weights.add((int) lpWrapper.Configuration.MM);
			sum += lpWrapper.Configuration.MM;
		}
		
		for ( int ty=1; ty<=this.numAttackerTypes; ty++) {
			double probability = weights.get(ty-1)*1.0 / sum;
			SecurityGame at1 = new SecurityGame(this.numTargets, this.numResources);
			at1.setCreateDummyHomeSite(this.createDummyHomeTarget);
			at1.initializeFixed(defCovPayoff, defUncovPayoff, attCovPayoff, attUncovPayoff);
			bayesianGame.put(at1, probability);
		}
	}
	
	public void initializeRandom() {
		List<Integer> weights = new ArrayList<Integer>();
		int sum = 0;
		int randomNumber;
		for ( int ty=1; ty<=this.numAttackerTypes; ty++) {
			randomNumber = Configuration.randomGenerator.nextInt(lpWrapper.Configuration.MM);
			sum += randomNumber;
			weights.add(randomNumber);
		}
		
		for ( int ty=1; ty<=this.numAttackerTypes; ty++) {
			double probability = weights.get(ty-1)*1.0 / sum;
			SecurityGame at1 = new SecurityGame(this.numTargets, this.numResources);
			at1.setCreateDummyHomeSite(this.createDummyHomeTarget);
			at1.initializeRandom();
			bayesianGame.put(at1, probability);
		}
	}
	
	public BayesianGenericGame getGenericGame() {		
		BayesianGenericGame bg = new BayesianGenericGame(this.numAttackerTypes, 1, this.numTargets);
		for ( SecurityGame sg : bayesianGame.keySet()) {
			GenericGame gg = sg.getGenericGame();
			bg.bayesianGame.put(gg, this.bayesianGame.get(sg));
			bg.numLeaderActions = gg.numLeaderActions;
		}
		return bg;
	}
	
	public double eval(double[] x) {
		double val = 0;
		for (SecurityGame sg : this.bayesianGame.keySet()) {
			val += sg.eval(x) * this.bayesianGame.get(sg);
		}
		return val;
	}

	public Set<Integer> getSetGameIDs() {
		Set<Integer> setGameIds = new HashSet<Integer>();
		for ( SecurityGame g : this.bayesianGame.keySet() ) {
			setGameIds.add(g.id);
		}
		return setGameIds;
	}
	
	public Set<SingleObjectiveTarget> unionAllTargetsById() {
		Set<SingleObjectiveTarget> setTarget = new HashSet<SingleObjectiveTarget>();
		Set<Integer> setIdsOfTargets = new HashSet<Integer>();
		
		for ( SecurityGame sg : bayesianGame.keySet() ) {
			for ( SingleObjectiveTarget t : sg.setTargets ) {
				if ( ! setIdsOfTargets.contains(t.id)) {
					setTarget.add(t);
					setIdsOfTargets.add(t.id);
				}
			}
		}
		
		return setTarget;
	}
	
	public Set<SingleObjectiveTarget> unionAllTargets() {
		Set<SingleObjectiveTarget> setTarget = new HashSet<SingleObjectiveTarget>();
		for ( SecurityGame sg : bayesianGame.keySet()) {
			setTarget.addAll(sg.setTargets);
		}
		return setTarget;
	}
	
	/**
	 * Generates a restricted game with only the ids given in listGameIds
	 * @param listGameIds
	 * @return
	 */
	public BayesianSecurityGame getRestrictedGame(List<Integer> listGameIds) {
		BayesianSecurityGame restrictedBSG = new BayesianSecurityGame(listGameIds.size(), this.numTargets, this.numResources);
		for ( SecurityGame g : this.bayesianGame.keySet() ) {
			if ( listGameIds.contains(g.id) ) {
				restrictedBSG.bayesianGame.put(g, this.bayesianGame.get(g));
			}
		}
		return restrictedBSG;
	}
	
	@Override
	public String toString() {
		return "BayesianSecurityGame [numAttackerTypes=" + numAttackerTypes
				+ ", numTargets=" + numTargets + ", numResources="
				+ numResources + ", bayesianGame=" + bayesianGame + "]";
	}

	public void setCreateDummyHomeSite(boolean b) {
		// TODO Auto-generated method stub
		this.createDummyHomeTarget = b;
	}

	public Map<SecurityGame, SingleObjectiveTarget> getDummyHomeTarget() {
		// TODO Auto-generated method stub
		if ( this.createDummyHomeTarget ) {
			mapDummyHomeTargets = new HashMap<SecurityGame, SingleObjectiveTarget>();
			for ( SecurityGame sg : this.bayesianGame.keySet()) {
				mapDummyHomeTargets.put(sg, sg.getDummyHomeTarget());
			}			
			return mapDummyHomeTargets;
		}
		return null;
	}	
}
