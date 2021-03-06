package model.securityGameModels.sparsGameModels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.Configuration;
import model.securityGameModels.SecurityGame;
import model.securityGameModels.SingleObjectiveTarget;

public class SparsGame extends SecurityGame {

	/*
	 * SecurityGame already has numTargets and numResources
	 */
	public int numOffices;
	public int numSchedules;
	public int numTargetsPerSchedule;
	public List<Schedule> lstSchedules;
	public List<Integer> officeSupply;
	public List<List<Boolean>> officeToScheduleMapping;
	public int maxNumTargetsPerSchedule;
	
	// set of targets that must be covered with a minimum amount of resources
	public List<Map<Integer, Double>> constraintCoeff;
	public List<Double> constraintConstant;

	public SparsGame(SparsGame sg) {
		super(sg.numTargets, sg.numResources);
		this.numOffices = sg.numOffices;
		this.numSchedules = sg.numSchedules;
		this.numTargetsPerSchedule = sg.numTargetsPerSchedule;
		this.lstSchedules = sg.lstSchedules;
		this.officeSupply = sg.officeSupply;
		this.officeToScheduleMapping = sg.officeToScheduleMapping;

		this.createDummyHomeTarget = sg.createDummyHomeTarget;
		this.dummyHomeTarget = sg.dummyHomeTarget;
		this.setTargets = sg.setTargets;
		this.maxNumTargetsPerSchedule = 0;
	}

	public SparsGame(int numTargets, int numResources, int numOffices,
			int numSchedules, int numTargetsPerSchedule) {
		super(numTargets, numResources);
		// TODO Auto-generated constructor stub
		this.numOffices = numOffices;
		this.numSchedules = numSchedules;
		this.numTargetsPerSchedule = numTargetsPerSchedule;
		lstSchedules = new ArrayList<Schedule>();
		for (int s = 0; s < numSchedules; s++) {
			lstSchedules.add(new Schedule());
		}
		officeSupply = new ArrayList<Integer>();
		for (int o = 0; o < numOffices; o++) {
			officeSupply.add(0);
		}
		officeToScheduleMapping = new ArrayList<List<Boolean>>();
		Schedule.resetCounter();
		this.maxNumTargetsPerSchedule = 0;
	}
	
	public void setTargetCoverageConstraint(List<Map<Integer, Double>> constraintCoeff,
			List<Double> constraintConstant) {
		if (this.constraintCoeff!=null) {
			this.constraintCoeff.clear();
			this.constraintCoeff = null;
		} 
		this.constraintCoeff = new ArrayList<Map<Integer, Double>>(constraintCoeff);
		
		if (this.constraintConstant!=null) {
			this.constraintConstant.clear();
			this.constraintConstant = null;
		}
		this.constraintConstant = new ArrayList<Double>(constraintConstant);			
	}
	
	public List<Map<Integer, Double>> getTargetCoverageConstraintCoeff() {
		return this.constraintCoeff;
	}
	
	public List<Double> getTargetCoverageConstraintConstant() {
		return this.constraintConstant;
	}
	
	public Map<Integer, Double> getTargetCoverageConstraintCoeffByIndex(int index) {
		return this.constraintCoeff.get(index);
	}
	
	public double getTargetCoverageConstraintConstantByIndex(int index) {
		return this.constraintConstant.get(index);
	}

	private void setOfficeSupply() {
		int numResourcesAllocated = 0;
		int officeIdCount = 0;
		// allocate at least one resource to every office
		while (numResourcesAllocated < this.numResources
				&& officeIdCount < this.numOffices) {
			officeSupply
					.set(officeIdCount, officeSupply.get(officeIdCount) + 1);
			numResourcesAllocated++;
			officeIdCount++;
		}

		for (int o = 0; o < this.numOffices - 1
				&& numResourcesAllocated < this.numResources; o++) {
			int addResources = Configuration.randomGenerator
					.nextInt(this.numResources - numResourcesAllocated + 1);
			officeSupply.set(o, officeSupply.get(o) + addResources);
			numResourcesAllocated += addResources;
		}
		officeSupply.set(this.numOffices - 1,
				officeSupply.get(this.numOffices - 1)
						+ (this.numResources - numResourcesAllocated));
	}

	private void generateSchedules() {
		final int MAX_RETRIES = (2 * numTargets > 10 ? 2 * numTargets : 10);

		if (this.numTargetsPerSchedule == 1) {
			int scheduleNumber = 0;
			for (SingleObjectiveTarget t : this.setTargets) {
				lstSchedules.get(scheduleNumber).addTarget(t);
				
				if ( lstSchedules.get(scheduleNumber).getNumTargets() > this.maxNumTargetsPerSchedule ){
					this.maxNumTargetsPerSchedule = lstSchedules.get(scheduleNumber).getNumTargets();
				}
				
				scheduleNumber++;
			}
			return;
		}

		// first assign every target to at least one schedule
		for (SingleObjectiveTarget t : this.setTargets) {
			int randomScheduleNumber = Configuration.randomGenerator
					.nextInt(this.numSchedules);
			int failureCount = 0;
			boolean assignedToSchedule = false;
			while (failureCount < MAX_RETRIES && assignedToSchedule == false) {
				if (lstSchedules.get(randomScheduleNumber).setTargets.size() >= this.numTargetsPerSchedule) {
					failureCount++;
				} else {
					lstSchedules.get(randomScheduleNumber).addTarget(t);

					if ( lstSchedules.get(randomScheduleNumber).getNumTargets() > this.maxNumTargetsPerSchedule ){
						this.maxNumTargetsPerSchedule = lstSchedules.get(randomScheduleNumber).getNumTargets();
					}

					assignedToSchedule = true;
					break;
				}
			}
			if ( assignedToSchedule == false) {
				lstSchedules.get(randomScheduleNumber).addTarget(t);
				if ( lstSchedules.get(randomScheduleNumber).getNumTargets() > this.maxNumTargetsPerSchedule ){
					this.maxNumTargetsPerSchedule = lstSchedules.get(randomScheduleNumber).getNumTargets();
				}				
			}
		}

		List<SingleObjectiveTarget> lstTargets = new ArrayList<SingleObjectiveTarget>(
				this.setTargets);

		// now ensure that every schedule has this.numSchedulesPerTarget targets
		for (int s = 0; s < this.numSchedules; s++) {
			int failureCount = 0;
			Schedule scObj = this.lstSchedules.get(s);
			while (scObj.setTargets.size() < this.numTargetsPerSchedule) {
				// choose random target
				int randomTarget = Configuration.randomGenerator
						.nextInt(this.numTargets);
				if (scObj.hasTarget(lstTargets.get(randomTarget))) {
					failureCount++;
					if (failureCount == MAX_RETRIES) {
						break;
					}
				} else {
					scObj.addTarget(lstTargets.get(randomTarget));
					
					if ( scObj.getNumTargets() > this.maxNumTargetsPerSchedule ){
						this.maxNumTargetsPerSchedule = scObj.getNumTargets();
					}

				}
			}
		}

		Collections.sort(lstSchedules);
	}

	private void generateOfficeToScheduleMapping() {
		for (int o = 0; o < this.numOffices; o++) {
			officeToScheduleMapping.add(new ArrayList<Boolean>());
			for (Schedule s : this.lstSchedules) {
				officeToScheduleMapping.get(o).add(false);
			}
		}
		// first add every schedule to at least one office
		for (int s = 0; s < this.numSchedules; s++) {
			int officeNumber = Configuration.randomGenerator
					.nextInt(this.numOffices);
			officeToScheduleMapping.get(officeNumber).set(s, true);
		}
		if (this.numOffices == 1) {
			return;
		}
		// now everything else
		for (int o = 0; o < this.numOffices; o++) {
			officeToScheduleMapping.add(new ArrayList<Boolean>());
			for (int s = 0; s < this.numSchedules; s++) {
				boolean mapping = Configuration.randomGenerator.nextBoolean();
				if (mapping) {
					officeToScheduleMapping.get(o).set(s, true);
				}
			}
		}

	}

	private void generateGame() {
		this.setOfficeSupply();
		this.generateSchedules();
		this.generateOfficeToScheduleMapping();
	}

	public void generateGameRandom() {
		this.initializeRandom();
		this.generateGame();
	}

	public void generateGameFixed(double defCovPayoff, double defUncovPayoff,
			double attCovPayoff, double attUncovPayoff) {
		this.initializeFixed(defCovPayoff, defUncovPayoff, attCovPayoff,
				attUncovPayoff);
		this.generateGame();
	}

	@Override
	public String toString() {
		return "SparsGame [id= " + id + "]";
	}

	public String printGame() {
		return "SparsGame [id= " + id + ", numOffices=" + numOffices + ", numSchedules="
				+ numSchedules + ", numSchedulesPerTarget="
				+ numTargetsPerSchedule + ", lstSchedules=" + lstSchedules
				+ ", officeSupply=" + officeSupply
				+ ", officeToScheduleMapping=" + officeToScheduleMapping + "]";
	}
	
	public SingleObjectiveTarget getTarget(int targetId) {
		for (SingleObjectiveTarget t : this.setTargets) {
			if (t.id == targetId) {
				return t;
			}
		}

		throw new RuntimeException("Couldn't find target with id: " + targetId
				+ ".");
	}

}
