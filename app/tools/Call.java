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
	
}
