package model.securityGameModels.sparsGameModels;

import java.util.HashSet;
import java.util.Set;

import model.securityGameModels.SingleObjectiveTarget;

public class JointScheduleAsSet {
	public int numResources;
	public Set<Schedule> setSchedules;
	public int id;
	
	static int COUNTER = 1;
	
	public JointScheduleAsSet(int numResources) {
		super();
		this.numResources = numResources;
		this.id = COUNTER;
		COUNTER ++;
	}
	
	public static void reset() {
		COUNTER = 1;
	}
	
	public void addSchedule(Schedule s){
		this.setSchedules.add(s);
	}
	
	public Set<SingleObjectiveTarget> targetsCovered(){
		Set<SingleObjectiveTarget> targetSetCov = new HashSet<SingleObjectiveTarget>();
		for ( Schedule s : this.setSchedules ) {
			targetSetCov.addAll(s.setTargets);
		}		
		return targetSetCov;
	}
}
