package model.securityGameModels;

public abstract class Target implements Comparable<Target> {
	private static int counter = 1;
	public int id;
	public int securityGameId;
	
	public static void resetCounter() {
		counter = 1;
	}
	
	protected Target() {
		super();
	}
	
	public Target(int gameId) {
		super();
		this.id = counter;
		this.securityGameId = gameId;
		counter++;
	}
	
	/**
	 * Only @id defines a Target and not the securityGameId. 
	 */
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
		Target other = (Target) obj;
		if (id != other.id)
			return false;
//		if ( securityGameId != other.securityGameId ) 
//			return false;
		return true;
	}

	@Override
	public int compareTo(Target arg0) {
		if (this.equals(arg0)) {
			return 0;
		} else if (this.id < arg0.id) {
			return -1;
		} else
			return 1;
	}

	@Override
	public String toString() {
		return "iTarget [id=" + id + ", securityGameId=" + securityGameId + "]";
	}
}
