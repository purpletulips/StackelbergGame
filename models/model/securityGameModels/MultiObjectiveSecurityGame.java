package model.securityGameModels;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.genericGameModels.MultiObjectiveGenericGame;

import utilities.CombinationGenerator;

public class MultiObjectiveSecurityGame implements Comparable<SecurityGame>{
	private static int counter = 1;
	public int id;
	public int numObjectives;
	public int numTargets;
	public int numResources;
	
	public List<MultiObjectiveTarget> targetList;

	public MultiObjectiveSecurityGame(int numObjectives, int numTargets, int numResources) {
		super();
		this.id = counter;
		this.numObjectives = numObjectives;
		this.numTargets = numTargets;
		this.numResources = numResources;
		counter ++;
	}
	
	public void initialize(){
		MultiObjectiveTarget.resetCounter();
		targetList = new ArrayList<MultiObjectiveTarget>();		
		for(int i = 0; i < this.numTargets; i++){
			MultiObjectiveTarget t = new MultiObjectiveTarget(id, numObjectives);
			t.setRandomPayoffs();
			targetList.add(t);
		}
	}
	
	public Set<List<MultiObjectiveTarget>> genAllDefenderCombinations() {
		Set<List<MultiObjectiveTarget>> targetCombos = new HashSet<List<MultiObjectiveTarget>>();
		
		return targetCombos;
	}
	
	public MultiObjectiveGenericGame getGenericGame() {
		CombinationGenerator cg = new CombinationGenerator(this.numTargets, this.numResources);
		BigInteger numLeaderActions = cg.getTotal();
		
		MultiObjectiveGenericGame mogg = new MultiObjectiveGenericGame(this.id, this.numObjectives, numLeaderActions.intValue(), this.numTargets);
		
		mogg.matrixLeaderPayoffs = new ArrayList<List<List<Integer>>>();
		mogg.matrixFollowerPayoffs = new ArrayList<List<Integer>>();
		
		int rowNumber = -1;
		while ( cg.hasMore() ) {			
			int[] v = cg.getNext();
			
			mogg.matrixLeaderPayoffs.add(new ArrayList<List<Integer>>());
			mogg.matrixFollowerPayoffs.add(new ArrayList<Integer>());
			rowNumber++;
		
			for(MultiObjectiveTarget t : targetList){
				boolean caught = false;
				for(int k = 0; k < this.numResources; k++) {					
					if(v[k] == t.id - 1) {
						caught = true;
						break;
					}					
				}
				
				if(caught){
					mogg.matrixLeaderPayoffs.get(rowNumber).add(new ArrayList<Integer>(t.defCovPayoff));
					mogg.matrixFollowerPayoffs.get(rowNumber).add(t.attCovPayoff);
				} else {
					mogg.matrixLeaderPayoffs.get(rowNumber).add(new ArrayList<Integer>(t.defUncovPayoff));
					mogg.matrixFollowerPayoffs.get(rowNumber).add(t.attUncovPayoff);
				}				
			}			
		}		
		return mogg;
	}
	
	@Override
	public String toString() {
		String s = "SecurityGame [numTargets=" + numTargets + ", numResources="	+ numResources + "]\n";

		for(MultiObjectiveTarget t : targetList){
			s += t.toString() + "\n";
		}		
		return s;
	}

	@Override
	public int compareTo(SecurityGame arg0) {
		if (this.equals(arg0)) {
			return 0;
		} else if (this.id < arg0.id) {
			return -1;
		} else
			return 1;
	}
	
}
