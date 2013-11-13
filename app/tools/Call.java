package tools;

public class Call {

	public Integer atFloor;
	public Directions to;
	
	public Call(Integer atFloor, Directions to) {
		super();
		this.atFloor = atFloor;
		this.to = to;
	}
	public Integer getAtFloor() {
		return atFloor;
	}
	public void setAtFloor(Integer atFloor) {
		this.atFloor = atFloor;
	}
	public Directions getTo() {
		return to;
	}
	public void setTo(Directions to) {
		this.to = to;
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((atFloor == null) ? 0 : atFloor.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
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
		Call other = (Call) obj;
		if (atFloor == null) {
			if (other.atFloor != null)
				return false;
		} else if (!atFloor.equals(other.atFloor))
			return false;
		if (to != other.to)
			return false;
		return true;
	}

	
}
