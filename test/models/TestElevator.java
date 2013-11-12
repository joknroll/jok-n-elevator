package models;

import org.junit.Test;

import static org.fest.assertions.Assertions.*;

public class TestElevator {

	@Test
	public void testAddCall(){
		Elevator elevator = new Elevator();
		elevator.addCall(1 , "UP");
		assertThat(elevator.callQueue.size()).isEqualTo(1);
		String nextCommand = elevator.nextCommand();
		assertThat(nextCommand).isEqualTo("UP");
		elevator.nextCommand(); // OPEN
		elevator.nextCommand(); // CLOSE
		String nextCommandNothing = elevator.nextCommand();
		assertThat(nextCommandNothing).isEqualTo("NOTHING");
		assertThat(elevator.currentFloor).isEqualTo(1);
	}

	@Test
	public void testAddGo(){
		Elevator elevator = new Elevator();
		elevator.addGo(1);
		assertThat(elevator.goQueue.size()).isEqualTo(1);
		String nextCommand = elevator.nextCommand();
		assertThat(nextCommand).isEqualTo("UP");
		elevator.nextCommand(); // OPEN
		elevator.nextCommand(); // CLOSE
		String nextCommandNothing = elevator.nextCommand();
		assertThat(nextCommandNothing).isEqualTo("NOTHING");
		assertThat(elevator.currentFloor).isEqualTo(1);
	}

	@Test
	public void testAddGoAndaddCall(){
		Elevator elevator = new Elevator();
		elevator.addGo(1);
		elevator.addCall(2, "DOWN");
		elevator.addGo(0);
		assertThat(elevator.goQueue.size()).isEqualTo(2);
		assertThat(elevator.callQueue.size()).isEqualTo(1);
		
		// Go To Floor 1
		String nextCommandIsUP = elevator.nextCommand();
		assertThat(nextCommandIsUP).isEqualTo("UP");
		
		String nextCommandIsOpen = elevator.nextCommand();
		assertThat(nextCommandIsOpen).isEqualTo("OPEN");
		assertThat(elevator.currentFloor).isEqualTo(1);

		String nextCommandIsClose = elevator.nextCommand();
		assertThat(nextCommandIsClose).isEqualTo("CLOSE");

		// Go to floor 0
		String nextCommandDown = elevator.nextCommand();
		assertThat(elevator.goQueue.size()).isEqualTo(1);
		assertThat(nextCommandDown).isEqualTo("DOWN");
		elevator.nextCommand(); // OPEN
		assertThat(elevator.currentFloor).isEqualTo(0);
	}

	@Test
	public void testCurrentFloorIsNotInGoQueue(){
		// TODO
	}

	@Test
	public void testCurrentFloorIsNotInCallQueue(){
		// TODO
	}

	@Test
	public void testMAxSizeCabineWontOpenTheDoor(){
		// TODO
	}

	
	private void repeateNextCommand(Elevator elevator, int repeate) {
		for(int i =0; i<repeate ; i++ ){
			elevator.nextCommand();
		}
	}
	
}
