package model.securityGameModels.sparsGameModels;

import java.util.HashSet;
import java.util.Set;

import model.securityGameModels.SingleObjectiveTarget;
import model.securityGameModels.Target;

public class Schedule implements Comparable<Schedule> {
	public Set<SingleObjectiveTarget> setTargets;

	public int id;

	static int counter = 1;

	public Schedule() {
		super();
		this.id = counter;

		Schedule.counter++;
		setTargets = new HashSet<SingleObjectiveTarget>();
	}

	@Override
	public String toString() {
		String str = new String();
		str += "Schedule [id= " + id + ", setTargets=[";
		for (SingleObjectiveTarget t : this.setTargets) {
			str += t.id + ", ";
		}		
		return str + "] ]";
	}

	public static void resetCounter() {
		Schedule.counter = 1;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Schedule other = (Schedule) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public void addTarget(SingleObjectiveTarget t) {
		setTargets.add(t);
	}

	public int getNumTargets() {
		return this.setTargets.size();
	}

	public boolean hasTarget(SingleObjectiveTarget t) {
		return this.setTargets.contains(t);
	}

	@Override
	public int compareTo(Schedule arg0) {
		if (this.equals(arg0)) {
			return 0;
		} else if (this.id < arg0.id) {
			return -1;
		} else
			return 1;
	}
}
