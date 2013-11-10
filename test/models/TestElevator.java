package models;

import org.junit.Test;

import static org.fest.assertions.Assertions.*;

public class TestElevator {

	@Test
	public void testSetMinAndMax(){
		Elevator elevator = new Elevator();
		elevator.actionFloor(1);
		assertThat(elevator.maxFloor).isEqualTo(1);
		assertThat(elevator.minFloor).isEqualTo(1);		
	}
	
	@Test
	public  void testGoToFirstFloorFromZero(){
		Elevator elevator = new Elevator();
		assertThat(elevator.currentFloor).isEqualTo(0);
		elevator.actionFloor(1);
		repeateNextCommand(elevator , 2);		// OPEN,CLOSE
		assertThat(elevator.previousCommand).isEqualTo("CLOSE");
		String result = elevator.nextCommand();		// must be UP
		assertThat(result).isEqualTo("UP");
		assertThat(elevator.currentFloor).isEqualTo(0);
		elevator.nextCommand();		// OPEN
		assertThat(elevator.currentFloor).isEqualTo(1);
		assertThat(elevator.maxFloor).isEqualTo(1);
		assertThat(elevator.minFloor).isEqualTo(1);
	}

	@Test
	public void testMaxFloorMustWait(){
		Elevator elevator = new Elevator();
		elevator.actionFloor(2);
		repeateNextCommand(elevator , 8); // [0]OPEN,CLOSE,UP|[1]OPEN,CLOSE,UP|[2]OPEN,CLOSE
		String result = elevator.nextCommand(); // SHOULD BE NOTHING
		assertThat(result).isEqualTo("NOTHING");
		assertThat(elevator.currentFloor).isEqualTo(2);
		assertThat(elevator.maxFloor).isEqualTo(2);
		assertThat(elevator.minFloor).isEqualTo(2);
	}
	
	@Test
	public  void testCurrentFloorIsFirst(){
		Elevator elevator = new Elevator();
		assertThat(elevator.currentFloor).isEqualTo(0);
		elevator.actionFloor(1);
		repeateNextCommand(elevator , 3);		// OPEN,CLOSE,UP
		assertThat(elevator.previousCommand).isEqualTo("UP");
		String result = elevator.nextCommand();		// must be OPEN, floor 1
		assertThat(result).isEqualTo("OPEN");
		assertThat(elevator.currentFloor).isEqualTo(1);
	}

	@Test
	public void testMaxFloorMustGoDown(){
		Elevator elevator = new Elevator();
		elevator.actionFloor(2);
		repeateNextCommand(elevator , 8); // [0]OPEN,CLOSE,UP|[1]OPEN,CLOSE,UP|[2]OPEN,CLOSE
		elevator.actionFloor(0);
		String result = elevator.nextCommand(); // SHOULD BE DOWN
		assertThat(result).isEqualTo("DOWN");
		elevator.nextCommand(); // SHOULD BE OPEN
		assertThat(elevator.currentFloor).isEqualTo(1);
		assertThat(elevator.maxFloor).isEqualTo(2);
		assertThat(elevator.minFloor).isEqualTo(0);
	}
	


	@Test
	public void testMinFloorMustGoUp(){
		Elevator elevator = new Elevator();
		elevator.actionFloor(2);
		repeateNextCommand(elevator , 8);// [0]OPEN,CLOSE,UP|[1]OPEN,CLOSE,UP|[2]OPEN,CLOSE
		assertThat(elevator.currentFloor).isEqualTo(2);
		elevator.actionFloor(1);
		repeateNextCommand(elevator , 3);// OPEN,CLOSE,DOWN
		assertThat(elevator.currentFloor).isEqualTo(1);
		elevator.actionFloor(3);
		String result = elevator.nextCommand(); // SHOULD BE UP
		assertThat(result).isEqualTo("UP");
		assertThat(elevator.maxFloor).isEqualTo(3);
		assertThat(elevator.minFloor).isEqualTo(1);
	}
	
	@Test
	public void testUPAndDown(){
		Elevator elevator = new Elevator();
		elevator.actionFloor(2);
		repeateNextCommand(elevator , 7);// [0]OPEN,CLOSE,UP|[1]OPEN,CLOSE,UP|[2]OPEN
		
		// SHOULD OPEN AT 2
		assertThat(elevator.currentFloor).isEqualTo(2);
		elevator.actionFloor(1);
		
		repeateNextCommand(elevator , 4);//CLOSE, UP, OPEN,CLOSE
		
		assertThat(elevator.currentFloor).isEqualTo(1);
		
		// CALL FOR 3 Floor
		elevator.actionFloor(3);

		
		//NEXT FLOOR -> 2
		repeateNextCommand(elevator , 2);// OPEN,CLOSE
		assertThat(elevator.currentFloor).isEqualTo(1);
		String result2 = elevator.nextCommand(); // SHOULD BE UP
		assertThat(result2).isEqualTo("UP");
		elevator.nextCommand(); // OPEN AT 2
		assertThat(elevator.currentFloor).isEqualTo(2);

		//NEXT FLOOR -> 3
		repeateNextCommand(elevator , 2);// CLOSE,UP
		String result3 = elevator.nextCommand(); // OPEN AT 3
		assertThat(result3).isEqualTo("OPEN");
		assertThat(elevator.currentFloor).isEqualTo(3);

		//NEXT FLOOR -> 2
		repeateNextCommand(elevator , 1);// CLOSE
		String result4 = elevator.nextCommand(); // SHOULD BE DOWN
		assertThat(result4).isEqualTo("DOWN");
		repeateNextCommand(elevator , 1);// OPEN AT 2
		assertThat(elevator.currentFloor).isEqualTo(2);
		// NEXT FLOOR -> 1
		repeateNextCommand(elevator , 1);// CLOSE
		String result5 = elevator.nextCommand(); // SHOULD BE DOWN
		assertThat(result5).isEqualTo("DOWN");
		String result6 = elevator.nextCommand(); // SHOULD BE OPEN AT 1
		assertThat(result6).isEqualTo("OPEN");
		assertThat(elevator.currentFloor).isEqualTo(1);
		assertThat(elevator.maxFloor).isEqualTo(3);
		assertThat(elevator.minFloor).isEqualTo(1);
	}


	
	private void repeateNextCommand(Elevator elevator, int repeate) {
		for(int i =0; i<repeate ; i++ ){
			elevator.nextCommand();
		}
	}
	
}
