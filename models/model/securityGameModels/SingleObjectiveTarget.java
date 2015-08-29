package model.securityGameModels;

import model.Configuration;

public class SingleObjectiveTarget extends Target {
	public double defCovPayoff;
	public double defUncovPayoff;
	public double attCovPayoff;
	public double attUncovPayoff;

	public SingleObjectiveTarget(int gameId) {
		super(gameId);
	}

	public SingleObjectiveTarget(int gameId, double defCovPayoff,
			double defUncovPayoff, double attCovPayoff, double attUncovPayoff) {
		this(gameId);
		this.setPayoffs(defCovPayoff, defUncovPayoff, attCovPayoff,
				attUncovPayoff);
	}
	
	public SingleObjectiveTarget(int gameId, int targetId, double defCovPayoff,
			double defUncovPayoff, double attCovPayoff, double attUncovPayoff) {
		this(gameId);
		this.id = targetId;
		this.setPayoffs(defCovPayoff, defUncovPayoff, attCovPayoff,
				attUncovPayoff);
	}

	public SingleObjectiveTarget(SingleObjectiveTarget singleObjectiveTarget) {
		super();

		this.id = singleObjectiveTarget.id;
		this.securityGameId = singleObjectiveTarget.securityGameId;

		this.defCovPayoff = singleObjectiveTarget.defCovPayoff;
		this.defUncovPayoff = singleObjectiveTarget.defUncovPayoff;
		this.attCovPayoff = singleObjectiveTarget.attCovPayoff;
		this.attUncovPayoff = singleObjectiveTarget.attUncovPayoff;
	}

	public double getDeltaDef() {
		return this.defCovPayoff - this.defUncovPayoff;
	}

	public double getDeltaAtt() {
		return this.attCovPayoff - this.attUncovPayoff;
	}

	public void setRandomPayoffs() {
		boolean guassian = true;
		if (guassian == false) {
			this.defCovPayoff = (int) (Configuration.DEF_COV_MIN + Math.round(Configuration.randomGenerator
					.nextDouble() * (Configuration.DEF_COV_MAX - Configuration.DEF_COV_MIN)));		
			this.defUncovPayoff = (int) (Configuration.DEF_UNCOV_MIN + Math.round(Configuration.randomGenerator
					.nextDouble() * (Configuration.DEF_UNCOV_MAX - Configuration.DEF_UNCOV_MIN)));
			this.attCovPayoff = (int) (Configuration.ATT_COV_MIN + Math.round(Configuration.randomGenerator
					.nextDouble() * (Configuration.ATT_COV_MAX - Configuration.ATT_COV_MIN)));
			
			this.attUncovPayoff = (int) (Configuration.ATT_UNCOV_MIN + Math.round(Configuration.randomGenerator
					.nextDouble() * (Configuration.ATT_UNCOV_MAX - Configuration.ATT_UNCOV_MIN)));
		} else {
			double meanDefCovPayoff = 0.25*Configuration.DEF_COV_MAX;
			double rangeDefCovPayoff = 0.4*Configuration.DEF_COV_MAX;
			this.defCovPayoff = (int) (meanDefCovPayoff + Math.round(Configuration.randomGenerator
					.nextGaussian() * rangeDefCovPayoff));
			if (this.defCovPayoff < Configuration.DEF_COV_MIN) {
				this.defCovPayoff = Configuration.DEF_COV_MIN;
			} else if (this.defCovPayoff > Configuration.DEF_COV_MAX) {
				this.defCovPayoff = Configuration.DEF_COV_MAX;
			}
			
			double meanDefUncovPayoff = 0.5*Configuration.DEF_UNCOV_MIN;
			double rangeDefUncovPayoff = 0.5*(0 - Configuration.DEF_UNCOV_MIN);
			this.defUncovPayoff = (int) (meanDefUncovPayoff + Math.round(Configuration.randomGenerator
					.nextGaussian() * rangeDefUncovPayoff));
			if (this.defUncovPayoff < Configuration.DEF_UNCOV_MIN) {
				this.defUncovPayoff = Configuration.DEF_UNCOV_MIN;
			} else if (this.defUncovPayoff > Configuration.DEF_UNCOV_MAX) {
				this.defUncovPayoff = Configuration.DEF_UNCOV_MAX;
			}
			
			double meanAttCovPayoff = 0.7*Configuration.ATT_COV_MIN;
			double rangeAttCovPayoff = 0.5*(0 - Configuration.ATT_COV_MIN);
			this.attCovPayoff = (int) (meanAttCovPayoff + Math.round(Configuration.randomGenerator
					.nextGaussian() * rangeAttCovPayoff));
			if (this.attCovPayoff < Configuration.ATT_COV_MIN) {
				this.attCovPayoff = Configuration.ATT_COV_MIN;
			} else if (this.attCovPayoff > Configuration.ATT_COV_MAX) {
				this.attCovPayoff = Configuration.ATT_COV_MAX;
			}
			
			double meanAttUncovPayoff = 0.7*Configuration.ATT_UNCOV_MAX;
			double rangeAttUncovPayoff = 0.3*Configuration.ATT_UNCOV_MAX;
			this.attUncovPayoff = (int) (meanAttUncovPayoff + Math.round(Configuration.randomGenerator
					.nextGaussian() * rangeAttUncovPayoff));
			if (this.attUncovPayoff < Configuration.ATT_UNCOV_MIN) {
				this.attUncovPayoff = Configuration.ATT_UNCOV_MIN;
			} else if (this.attUncovPayoff > Configuration.ATT_UNCOV_MAX) {
				this.attUncovPayoff = Configuration.ATT_UNCOV_MAX;
			}
		}
		
		
	}

	public void setPayoffs(double defCovPayoff, double defUncovPayoff,
			double attCovPayoff, double attUncovPayoff) {
		this.defCovPayoff = defCovPayoff;
		this.defUncovPayoff = defUncovPayoff;
		this.attCovPayoff = attCovPayoff;
		this.attUncovPayoff = attUncovPayoff;
	}

	@Override
	public String toString() {
//		return "id=" + id;
		return "Target [securityGameId=" + securityGameId + ", id=" + id
				+ ", defCovPayoff=" + defCovPayoff + ", defUncovPayoff="
				+ defUncovPayoff + ", attCovPayoff=" + attCovPayoff
				+ ", attUncovPayoff=" + attUncovPayoff + "]";
	}

}
