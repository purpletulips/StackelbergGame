package model.securityGameModels.sparsGameModels;

import java.util.ArrayList;
import java.util.List;

import lpWrapper.LPSolverException;

import securtyGameSolvers.sparsGameSolvers.MultipleLPs.Slave;

public class SaturationForFlow {
	BayesianSparsGame bsg;

	public SaturationForFlow(BayesianSparsGame bsg) {
		super();
		this.bsg = bsg;
	}
	
	public int getSaturationValue() {
		
		Slave slaveObj = new Slave(bsg);
		slaveObj.loadProblem();
		
		slaveObj.forceUseOfAllResources();
		slaveObj.removeObjective();
		
		List<Integer> officeSupply = new ArrayList<Integer>();
		for ( int i = 0; i < bsg.numOffices; i++) {
			officeSupply.add(0);
		}
		
//		int saturationResources = 1; //this.bsg.numTargets;
		int saturationResources = this.bsg.numTargets;
		while ( true ) {
			for ( int i = 0; i < bsg.numOffices; i++) {
				officeSupply.set(i, saturationResources);
			}
						
			slaveObj.editNumberOfResources(officeSupply, true);
			
			try {
				slaveObj.solve();
				System.out.println("breaking...");
				break;
			} catch (LPSolverException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				if ( saturationResources % 10 == 0 ) {
					System.out.print(saturationResources);
				} else {
					System.out.print(".");
				}
					
//				System.out.println(saturationResources + ": NOT POSSIBLE");
			}
			saturationResources --;
		}
		System.out.println();
		slaveObj.end();
		
		return (saturationResources * this.bsg.numOffices);
	}
}
