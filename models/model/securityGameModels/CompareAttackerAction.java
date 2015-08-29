package model.securityGameModels;

import java.util.HashMap;
import java.util.Map;

import lpWrapper.AMIProblem.STATUS_TYPE;

public class CompareAttackerAction implements
Comparable<CompareAttackerAction> {
	public Map<SecurityGame, SingleObjectiveTarget> attackerAction;
	public STATUS_TYPE statusType;
	public double attackerReward;
	public double defenderRewardUB;
	public double defenderReward;
	
	public CompareAttackerAction() {
		super();
	}

	public CompareAttackerAction(CompareAttackerAction e) {
		super();
		this.attackerAction = new HashMap<SecurityGame, SingleObjectiveTarget>();
		for (SecurityGame g : e.attackerAction.keySet()) {
			this.attackerAction.put(g, new SingleObjectiveTarget(e.attackerAction.get(g)));
		}
		this.statusType = e.statusType;
		this.attackerReward = e.attackerReward;
		this.defenderRewardUB = e.defenderRewardUB;
		this.defenderReward = e.defenderReward;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((attackerAction == null) ? 0 : attackerAction.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompareAttackerAction other = (CompareAttackerAction) obj;
		if (attackerAction == null) {
			if (other.attackerAction != null)
				return false;
		} else if (!attackerAction.equals(other.attackerAction))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String s = new String();
		for (SecurityGame g : attackerAction.keySet()) {
			s += ("G:" + g.id + ", A:" + attackerAction.get(g).securityGameId + "-" + attackerAction.get(g).id + ", ");
		}
		s += (defenderRewardUB + ", " + defenderReward + ", " + attackerReward + ", " + statusType);
		return s;
	}

	@Override
	public int compareTo(CompareAttackerAction arg0) {
		if (arg0 == null) {
			return 1;
		} else if (this.equals(arg0)) {
			return 0;
		} else if (this.statusType == STATUS_TYPE.INFEASIBLE
				&& arg0.statusType != STATUS_TYPE.INFEASIBLE) {
			return -1;
		} else if (this.statusType != STATUS_TYPE.INFEASIBLE
				&& arg0.statusType == STATUS_TYPE.INFEASIBLE) {
			return 1;
		} else if (this.statusType == STATUS_TYPE.INFEASIBLE
				&& arg0.statusType == STATUS_TYPE.INFEASIBLE) {
			return 0;
		} else if (this.defenderRewardUB < arg0.defenderRewardUB) {
			return -1;
		} else if (this.defenderRewardUB == arg0.defenderRewardUB) {
			return 0;
		} else {
			return 1;
		}
	}
}

