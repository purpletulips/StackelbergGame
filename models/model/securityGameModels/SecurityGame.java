 package model.securityGameModels;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.Configuration;
import model.genericGameModels.GenericGame;
import utilities.CombinationGenerator;

public class SecurityGame implements Comparable<SecurityGame> {
	private static int counter = 1;
	public int id;
	public int numTargets;
	public int numResources;
	public boolean createDummyHomeTarget;
	public SingleObjectiveTarget dummyHomeTarget;
	public Set<SingleObjectiveTarget> setTargets;
	
	public SingleObjectiveTarget getDummyHomeTarget() {
		return dummyHomeTarget;
	}

	public void setCreateDummyHomeSite(boolean createDummyHomeSite) {
		this.createDummyHomeTarget = createDummyHomeSite;
	}
	
	public void end() {
		setTargets.clear();
	}
	
	public static void resetCounter(){
		counter = 1;
	}

	public SecurityGame(int numTargets, int numResources) {
		super();
		this.id = counter;
		this.numTargets = numTargets;
		this.numResources = numResources;
		this.createDummyHomeTarget = false;
		counter++;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void initializeRandom() {
		SingleObjectiveTarget.resetCounter();
		setTargets = new HashSet<SingleObjectiveTarget>();
		this.createDummyHomeSite();
		for (int i = this.setTargets.size(); i < this.numTargets; i++) {
			SingleObjectiveTarget t = new SingleObjectiveTarget(id);
			t.setRandomPayoffs();			
			setTargets.add(t);
		}
	}
	
	private void createDummyHomeSite() {
		// TODO Auto-generated method stub
		if ( this.createDummyHomeTarget == true ) {
			SingleObjectiveTarget t = new SingleObjectiveTarget(id);
			dummyHomeTarget = t;
			t.setPayoffs(0, 0, 0, 0);
			setTargets.add(t);
		}
	}
	

	public void initializeFixed(double defCovPayoff, double defUncovPayoff,
			double attCovPayoff, double attUncovPayoff) {
		SingleObjectiveTarget.resetCounter();
		setTargets = new HashSet<SingleObjectiveTarget>();
		this.createDummyHomeSite();
		for (int i = this.setTargets.size(); i < this.numTargets; i++) {
			SingleObjectiveTarget t = new SingleObjectiveTarget(id);
			t.setPayoffs(defCovPayoff, defUncovPayoff, attCovPayoff, attUncovPayoff);			
			setTargets.add(t);
		}
	}
	
	public Set<List<SingleObjectiveTarget>> genAllDefenderCombinations() {
		Set<List<SingleObjectiveTarget>> targetCombos = new HashSet<List<SingleObjectiveTarget>>();

		return targetCombos;
	}

	public GenericGame getGenericGame() {
		CombinationGenerator cg = new CombinationGenerator(this.numTargets,
				this.numResources);
		BigInteger numLeaderActions = cg.getTotal();

		GenericGame gg = new GenericGame(this.id, numLeaderActions.intValue(),
				this.numTargets);

		gg.matrixLeaderPayoffs = new ArrayList<List<Double>>();
		gg.matrixFollowerPayoffs = new ArrayList<List<Double>>();

		List<SingleObjectiveTarget> targetList = new ArrayList<SingleObjectiveTarget>(
				this.setTargets);
		Collections.sort(targetList);

		int rowNumber = -1;
		while (cg.hasMore()) {
			int[] v = cg.getNext();

			gg.matrixLeaderPayoffs.add(new ArrayList<Double>());
			gg.matrixFollowerPayoffs.add(new ArrayList<Double>());
			rowNumber++;

			for (SingleObjectiveTarget t : targetList) {
				boolean caught = false;
				for (int k = 0; k < this.numResources; k++) {
					if (v[k] == t.id - 1) {
						caught = true;
						break;
					}
				}

				if (caught) {
					gg.matrixLeaderPayoffs.get(rowNumber).add(t.defCovPayoff);
					gg.matrixFollowerPayoffs.get(rowNumber).add(t.attCovPayoff);
				} else {
					gg.matrixLeaderPayoffs.get(rowNumber).add(t.defUncovPayoff);
					gg.matrixFollowerPayoffs.get(rowNumber).add(
							t.attUncovPayoff);
				}
			}
		}
		return gg;
	}

	public double eval(double[] x) {
		double maxAttUtility = model.Configuration.ATT_COV_MIN;
		double maxDefUtility = model.Configuration.DEF_UNCOV_MIN;
		for (SingleObjectiveTarget t : setTargets) {
			double attU = x[t.id] * t.getDeltaAtt() + t.attUncovPayoff;
			if (attU >= maxAttUtility - 1e-6) {
				double defU = x[t.id] * t.getDeltaDef() + t.defUncovPayoff;
				if (attU > maxAttUtility + 1e-6) {
					maxDefUtility = defU;
					maxAttUtility = attU;
				} else {
					if (defU > maxDefUtility) {
						maxDefUtility = defU;
					}
				}
			}
			
		}
		return maxDefUtility;
	}

	@Override
	public String toString() {
		String s = "SecurityGame [id=" + id + ", numTargets=" + numTargets
				+ ", numResources=" + numResources + "]\n";

		List<SingleObjectiveTarget> targetList = new ArrayList<SingleObjectiveTarget>(
				this.setTargets);
		Collections.sort(targetList);

		for (SingleObjectiveTarget t : targetList) {
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
	
	// *********** write random payoff to file ************ //
	public void WriteGame2File (String file_path) throws IOException{
	    
	    FileWriter fstream = new FileWriter(file_path);
	    for (int i=0; i<this.numTargets; i++ ) {
			SingleObjectiveTarget t = new SingleObjectiveTarget(id);
			t.setRandomPayoffs();			
			fstream.append(Double.toString(t.defCovPayoff));
			fstream.append(',');
			fstream.append(Double.toString(t.defUncovPayoff));
			fstream.append(',');
			fstream.append(Double.toString(t.attUncovPayoff));
			fstream.append(',');
			fstream.append(Double.toString(t.attCovPayoff));
	        fstream.append('\n');
	    }
	    fstream.close();
	}
	
	public void LoadTestGame(int payoff_index){		
		
		int[][] payoff;
		if (payoff_index==1){
			//payoff 1		
			int[][]	temp = {{2,6,7,7,8,8,6,9},
					{-8,-10,-3,-1,-10,-5,-2,-5},
					{10,8,3,7,6,7,8,2},
					{-7,-4,-6,-8,-4,-2,-9,-3}};
			payoff = temp;
		}
		else if(payoff_index==2){		
			// payoff 2
			int[][] temp = {
					{3,8,9,9,7,7,4,1},
					{-10,-2,-5,-1,-7,-6,-2,-1},
					{9,8,2,9,10,1,10,1},
					{-10,-1,-10,-8,-4,-10,-5,-3}};
			payoff = temp;
		}
		else if (payoff_index==3) {
			// payoff 3
			int[][] temp ={
			           {5,3,8,3,3,4,3,6},
			           {-2,-5,-4,-6,-3,-10,-7,-2},
			           {8,6,1,3,1,7,3,5},
			           {-6,-9,-3,-7,-7,-2,-5,-2}};
			payoff = temp;
		}
		else if (payoff_index==4) {
			// payoff 4
			int[][] temp={
			           {5,9,10,2,10,4,8,8},
			           {-10,-4,-9,-3,-10,-10,-2,-5},
			           {3,7,3,9,2,9,7,8},
			           {-4,-8,-5,-8,-9,-4,-1,-6}};
			payoff = temp;
		}
		else if (payoff_index==5){			       
			// payoff 5 (pita game 1)
			int[][] temp = {		
					{1,4,2,3,4,1,5,2},
					{-5,-8,-1,-6,-5,-1,-7,-7},
					{1,9,5,6,7,1,10,3},
					{-2,-4,-3,-3,-3,-2,-4,-3}};
			payoff = temp;
		}
		else if (payoff_index==6){
			// payoff 6
	        int[][] temp={
				{4,3,1,5,1,2,5,2},
				{-8,-10,-1,-8,-1,-3,-11,-5},
				{8,5,3,10,1,3,9,4},
				{-3,-2,-3,-2,-3,-3,-2,-3}};
	        payoff = temp;
		}
		else {
	        // payoff 7
	        int[][] temp={
				{4,3,1,5,1,2,5,2},
				{-8,-5,-1,-10,-5,-3,-9,-6},
				{8,5,2,10,1,3,9,4},
				{-3,-3,-3,-3,-3,-3,-3,-3}};
	        payoff = temp;
		}
		
		
		SingleObjectiveTarget.resetCounter();
		setTargets = new HashSet<SingleObjectiveTarget>();		
		for (int i=0; i<this.numTargets; i++ ) {
			//System.out.println("row "+i);
			SingleObjectiveTarget t = new SingleObjectiveTarget(id, payoff[0][i], payoff[1][i], payoff[3][i], payoff[2][i]);
			setTargets.add(t);
		}
	}
	
	// *********** load game payoff from file ************ //
	public void LoadGameFromFile (String file_path) throws IOException{
		SingleObjectiveTarget.resetCounter();
		setTargets.clear();
	    try	    {	    	
	    	
		    //create BufferedReader to read csv file
		    BufferedReader br = new BufferedReader( new FileReader(file_path));
		    String strLine = "";
		    //read comma separated file line by line

		    int cntTarget = 0;
		    while( (strLine = br.readLine()) != null)
		    {
		    	
			   String[] stringArray = strLine.split(",");
			   assert stringArray.length==4;
			   
			   SingleObjectiveTarget t = new SingleObjectiveTarget(id);
			   t.defCovPayoff = Double.parseDouble(stringArray[0]);
			   t.defUncovPayoff = Double.parseDouble(stringArray[1]);
			   t.attUncovPayoff = Double.parseDouble(stringArray[2]);
			   t.attCovPayoff = Double.parseDouble(stringArray[3]);
			   
			   setTargets.add(t);
			   cntTarget ++;		   

			}
		    br.close();

		    //System.out.print("numTargets: "+ cntTarget +"\n");
	    }
	    catch(Exception e)
	    {
	    	System.out.println("Exception while reading csv file: " + e);
	    }
	    
	}
	

}
