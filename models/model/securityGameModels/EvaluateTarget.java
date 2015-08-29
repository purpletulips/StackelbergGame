package model.securityGameModels;

public class EvaluateTarget implements Comparable<EvaluateTarget>{
	protected SingleObjectiveTarget t;
	protected double defenderPayoff;
	protected double attackerPayoff;
	protected double defCov;
	
	public EvaluateTarget(SingleObjectiveTarget t, double defCov, double defenderPayoff, double attackerPayoff) {
		super();
		this.t = t;
		this.defCov = defCov;
		this.defenderPayoff = defenderPayoff;
		this.attackerPayoff = attackerPayoff;
	}

	public double getAttackerPayoff() {
		return attackerPayoff;
	}

	public void setAttackerPayoff(double attackerPayoff) {
		this.attackerPayoff = attackerPayoff;
	}

	public SingleObjectiveTarget getTarget() {
		return t;
	}

	public void setTarget(SingleObjectiveTarget t) {
		this.t = t;
	}

	public double getDefenderPayoff() {
		return defenderPayoff;
	}

	public void setDefenderPayoff(double defenderPayoff) {
		this.defenderPayoff = defenderPayoff;
	}

	@Override
	public int compareTo(EvaluateTarget o) {
		// TODO Auto-generated method stub
		if ( this.defenderPayoff < o.defenderPayoff) {
			return 1;
		} else if (this.defenderPayoff > o.defenderPayoff) {
			return -1;
		} else {
			return 0;
		}
	}
	
	@Override
	public String toString() {
		return "EvaluateTarget [t=" + t.id + ", defenderPayoff=" + defenderPayoff
				+ ", defCov=" + defCov +"]";
	}



}
