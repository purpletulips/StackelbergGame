/*
 * @author Rong Yang
 * 
 */

package model.securityGameModels.sparsGameModels;

import java.util.HashMap;
import java.util.Map;

public class PatrolCoverage implements Comparable<Schedule> {
	public Map<Integer, Double> targetCoverage;

	public int id;

	static int counter = 1;

	public PatrolCoverage() {
		super();
		this.id = counter;

		Schedule.counter++;
		targetCoverage = new HashMap<Integer, Double>();
	}

	@Override
	public String toString() {
		String str = new String();
		str += "Schedule [id= " + id + ", setTargets=[";
		for (Map.Entry<Integer, Double> tCov : this.targetCoverage.entrySet()) {
			str += tCov.getKey() + "(" + tCov.getValue() + "), ";
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

	public void addTarget(int tId, double coverage) {
		this.targetCoverage.put(tId, coverage);
	}

	public int getNumTargets() {
		return this.targetCoverage.size();
	}

	public boolean hasTarget(int tId) {
		return this.targetCoverage.containsKey(tId);
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
