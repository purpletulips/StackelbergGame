package model.securityGameModels.sparsGameModels;

import java.util.HashSet;
import java.util.Set;

import model.securityGameModels.SingleObjectiveTarget;

public class JointSchedule implements Comparable<JointSchedule> {
	public int numResources;
	public Set<Integer> setTargets;
	public int id;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((setTargets == null) ? 0 : setTargets.hashCode());
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
		JointSchedule other = (JointSchedule) obj;
		
		if (setTargets == null) {
			if (other.setTargets != null)
				return false;
		} else {
			for ( int i : other.setTargets) {
				if ( !this.setTargets.contains(i) ) {
					return false;
				}
			}
			for ( int i : this.setTargets) {
				if ( !other.setTargets.contains(i)) {
					return false;
				}
			}			
		}
		return true;
	}

	static int COUNTER = 1;
	
	public JointSchedule(int numResources) {
		super();
		this.numResources = numResources;
		this.setTargets = new HashSet<Integer>();
		this.id = COUNTER;
		COUNTER ++;
	}
	
	public static void resetCounter() {
		COUNTER = 1;
	}

	@Override
	public String toString() {
		String output = "[[";
		for ( int t : setTargets ) {
			output += (t + " ");
		}
		
		return output + "], id=" + id + "]";
	}

	public void addTarget(SingleObjectiveTarget t){
		this.setTargets.add(t.id);
	}
	
	public void addTarget(Integer tId){
		this.setTargets.add(tId);
	}
	
	public void addSchedule(Schedule s){
		for ( SingleObjectiveTarget t : s.setTargets) {
			this.setTargets.add(t.id);
		}
	}
	
	public Set<Integer> targetsCovered(){
		return setTargets;
	}

	@Override
	public int compareTo(JointSchedule o) {
		// TODO Auto-generated method stub
		if ( this.equals(o)) {
			return 0;
		}
		return 1;
	}
}
