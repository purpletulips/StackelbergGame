package model.securityGameModels;

import model.Configuration;

import java.util.Vector;

public class MultiObjectiveTarget extends Target{
	public int numObjectives;
	public Vector<Integer> defCovPayoff;
	public Vector<Integer> defUncovPayoff;
	public int attCovPayoff;
	public int attUncovPayoff;
	
	public MultiObjectiveTarget(int gameId, int numObjectives) {
		super(gameId);
		this.numObjectives = numObjectives;
		this.defCovPayoff = new Vector<Integer>();
		this.defUncovPayoff = new Vector<Integer>();
	}

	public MultiObjectiveTarget(int gameId, int numObjectives, Vector<Integer> defCovPayoff, Vector<Integer> defUncovPayoff, int attCovPayoff, int attUncovPayoff) {
		this(gameId, numObjectives);
		this.setPayoffs(defCovPayoff, defUncovPayoff, attCovPayoff, attUncovPayoff);
	}
	
	public int getDeltaAtt() {
		return this.attCovPayoff - this.attUncovPayoff;
	}

	public void setRandomPayoffs(){
		for(int i = 0; i < numObjectives; i++){
			this.defCovPayoff.add((int) (Configuration.DEF_COV_MIN + (Configuration.randomGenerator.nextDouble() * (Configuration.DEF_COV_MAX - Configuration.DEF_COV_MIN))));
			this.defUncovPayoff.add((int) (Configuration.DEF_UNCOV_MIN + (Configuration.randomGenerator.nextDouble() * (Configuration.DEF_UNCOV_MAX - Configuration.DEF_UNCOV_MIN))));
		}
		
		this.attCovPayoff = (int) (Configuration.ATT_COV_MIN + (Configuration.randomGenerator.nextDouble() * (Configuration.ATT_COV_MAX - Configuration.ATT_COV_MIN)));
		this.attUncovPayoff = (int) (Configuration.ATT_UNCOV_MIN + (Configuration.randomGenerator.nextDouble() * (Configuration.ATT_UNCOV_MAX - Configuration.ATT_UNCOV_MIN)));
	}

	public void setPayoffs(Vector<Integer> defCovPayoff, Vector<Integer> defUncovPayoff, int attCovPayoff, int attUncovPayoff) {
		this.defCovPayoff = new Vector<Integer>(defCovPayoff);
		this.defUncovPayoff = new Vector<Integer>(defUncovPayoff);
		this.attCovPayoff = attCovPayoff;
		this.attUncovPayoff = attUncovPayoff;
	}

	@Override
	public String toString() {
		return "Target [securityGameId=" + securityGameId + ", id=" + id + ", defCovPayoff=" + defCovPayoff
				+ ", defUncovPayoff=" + defUncovPayoff + ", attCovPayoff="
				+ attCovPayoff + ", attUncovPayoff=" + attUncovPayoff + "]";
	}	

}
