package models;

import org.junit.Test;

import tools.Call;
import tools.Directions;
import static org.fest.assertions.Assertions.*;

public class TestElevator {


	@Test
	public void dummyTestEquals(){
		Call callOne = new Call(0, Directions.UP);
		Call callTwo = new Call(0, Directions.UP);
		assertThat(callOne.equals(callTwo)).isTrue();
	}

	@Test
	public void dummyTestRemoveCall(){
		Call callTwo = new Call(2, Directions.UP);
		Elevator elevator  = new Elevator();
		elevator.addCall(2, Directions.UP);
		elevator.addCall(2, Directions.UP);
		elevator.addCall(2, Directions.UP);
		elevator.removeCallInCallQueue(callTwo);
		assertThat(elevator.callQueue.size()).isEqualTo(0);
	}
	
	@Test
	public void testNumberWaiting(){
		Elevator elevator  = new Elevator(0, 10, 30);
		elevator.addCall(2, Directions.UP);
		elevator.addCall(2, Directions.UP);
		elevator.addCall(2, Directions.UP);
		assertThat(elevator.waitingUserToUpByFloor.get(2)).isEqualTo(3);
		
		elevator.currentFloor = 1;
		
		// go to the second floor
		String nextCommandUp = elevator.nextCommand();
		assertThat(nextCommandUp).isEqualTo("UP");
		
		// open at the second floor
		String nextCommandOpen = elevator.nextCommand();
		assertThat(nextCommandOpen).isEqualTo("OPEN");
		
		assertThat(elevator.waitingUserToUpByFloor.get(2)).isEqualTo(0);
	}
	
	@Test
	public void testSetPriorityToEmptyCabin(){
		Elevator elevator  = new Elevator();
		elevator.userIn.set(42);
		elevator.cabinSize = 42;
		elevator.addGo(5);
		elevator.addGo(3);
		elevator.addGo(3);
		elevator.addGo(3);
		elevator.addGo(3);
		elevator.addGo(3);
		elevator.addGo(3);
		elevator.currentFloor = 6;
		// go to 5 but don't stop
		String nextCommandDown = elevator.nextCommand();
		assertThat(nextCommandDown).isEqualTo("DOWN");
		
		// go to 4
		String nextCommandDown2 = elevator.nextCommand();
		assertThat(nextCommandDown2).isEqualTo("DOWN");
		
	}
	
	@Test
	public void dummyTestRemoveGo(){
		Call callTwo = new Call(2, Directions.UP);
		Elevator elevator  = new Elevator();
		elevator.addGo(2);
		elevator.addGo(2);
		elevator.addGo(2);
		elevator.addGo(3);
		elevator.currentFloor = 2;
		elevator.removeCurentFloorInGoQueue();
		assertThat(elevator.goQueue.size()).isEqualTo(1);
	}
	
	@Test
	public void testCallQueueContains(){
		Elevator elevator  = new Elevator();
		elevator.addCall(2, Directions.UP);
		boolean result = elevator.findCallInCallQueue(new Call(2 , Directions.UP));
		assertThat(result).isTrue();
	}
	
	@Test
	public void testGoQueueContains(){
		Elevator elevator  = new Elevator();
		elevator.addGo(2);
		boolean result = elevator.findGoInCallQueue(2);
		assertThat(result).isTrue();
	}
	
	@Test
	public void testAddCallTo2(){
		Elevator elevator = new Elevator();
		elevator.addCall(2 , "UP");
		assertThat(elevator.callQueue.size()).isEqualTo(1);
		assertThat(elevator.currentFloor).isEqualTo(0);
		
		//  go to the first floor
		String nextCommandUp = elevator.nextCommand();
		assertThat(nextCommandUp).isEqualTo("UP");
		
		//  go to the second floor
		String nextCommandUpAgain = elevator.nextCommand();
		assertThat(nextCommandUpAgain).isEqualTo("UP");
		assertThat(elevator.currentFloor).isEqualTo(1);
		
		//  im am on the second floor		
		String nextCommandOpen = elevator.nextCommand();
		assertThat(nextCommandOpen).isEqualTo("OPEN");
		assertThat(elevator.currentFloor).isEqualTo(2);
	}

	@Test
	public void testAddCallTo2AndGoTo3(){
		Elevator elevator = new Elevator();
		elevator.cabinSize = 10;
		elevator.userIn.set(8);
		elevator.addCall(2 , "UP");
		elevator.addGo(3 );
		assertThat(elevator.callQueue.size()).isEqualTo(1);
		assertThat(elevator.currentFloor).isEqualTo(0);
		
		//  go to the first floor
		String nextCommandUp = elevator.nextCommand();
		assertThat(nextCommandUp).isEqualTo("UP");
		
		//  go to the second floor
		String nextCommandUpAgain = elevator.nextCommand();
		assertThat(nextCommandUpAgain).isEqualTo("UP");
		assertThat(elevator.currentFloor).isEqualTo(1);
		
		//  i am at the second floor with a call
		String nextCommandOpen = elevator.nextCommand();
		assertThat(nextCommandOpen).isEqualTo("OPEN");
		assertThat(elevator.currentFloor).isEqualTo(2);
		assertThat(elevator.callQueue.size()).isEqualTo(0);
		
		String nextCommandClose = elevator.nextCommand();
		assertThat(nextCommandClose).isEqualTo("CLOSE");
		
		//  go to the third floor
		String nextCommandUpAgain2 = elevator.nextCommand();
		assertThat(nextCommandUpAgain2).isEqualTo("UP");
		assertThat(elevator.currentFloor).isEqualTo(2);		
		
		//  i am on the third floor		
		String nextCommandOpenAtThird = elevator.nextCommand();
		assertThat(nextCommandOpenAtThird).isEqualTo("OPEN");
		assertThat(elevator.currentFloor).isEqualTo(3);
		assertThat(elevator.goQueue.size()).isEqualTo(0);
		// close at the third
		String nextCommandClose2 = elevator.nextCommand();
		assertThat(nextCommandClose2).isEqualTo("CLOSE");		
		//nothing at the third
		String nextCommandBothing = elevator.nextCommand();
		assertThat(nextCommandBothing).isEqualTo("NOTHING");
	}		
	
	@Test
	public void testAddCallTo2AndGoTo3ButCabinIsFull(){
		Elevator elevator = new Elevator();
		elevator.cabinSize = 10;
		elevator.userIn.set(10);
		elevator.addCall(2 , "UP");
		elevator.addGo(3 );
		assertThat(elevator.callQueue.size()).isEqualTo(1);
		assertThat(elevator.currentFloor).isEqualTo(0);
		
		//  go to the first floor
		String nextCommandUp = elevator.nextCommand();
		assertThat(nextCommandUp).isEqualTo("UP");
		
		//  go to the second floor
		String nextCommandUpAgain = elevator.nextCommand();
		assertThat(nextCommandUpAgain).isEqualTo("UP");
		assertThat(elevator.currentFloor).isEqualTo(1);
		
		
		//  go to the third floor
		String nextCommandUpAgain2 = elevator.nextCommand();
		assertThat(nextCommandUpAgain2).isEqualTo("UP");
		assertThat(elevator.currentFloor).isEqualTo(2);		
		
		//  i am on the third floor		
		String nextCommandOpenAtThird = elevator.nextCommand();
		assertThat(nextCommandOpenAtThird).isEqualTo("OPEN");
		assertThat(elevator.currentFloor).isEqualTo(3);
		assertThat(elevator.goQueue.size()).isEqualTo(0);
		// close at the third
		String nextCommandClose2 = elevator.nextCommand();
		assertThat(nextCommandClose2).isEqualTo("CLOSE");		

	}	
	
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


	
	private void repeateNextCommand(Elevator elevator, int repeate) {
		for(int i =0; i<repeate ; i++ ){
			elevator.nextCommand();
		}
	}
	
}
