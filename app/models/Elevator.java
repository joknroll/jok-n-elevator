package models;

import java.math.BigDecimal;

public class Elevator {

	private static final String DOWN = "DOWN";
	private static final String UP = "UP";
	private static final String NOTHING = "NOTHING";
	private static Elevator instance = null;
	private int count = 0;
	public int maxFloor = 0;
	public int minFloor = 0;
	public int currentFloor = 0;
	public int consecutiveUp = 0;
	public int consecutiveDown = 0;

	public String name = "jok Elevator";
	public String previousCommand = "OPEN";

	public static synchronized Elevator getInstance() {
		if (instance == null) {
			instance = new Elevator();
		}
		return instance;
	}

	private static String commands[] = { "OPEN", "CLOSE", NOTHING,
	// "OPEN","CLOSE","UP",
	// "OPEN","CLOSE","UP",
	// "OPEN","CLOSE","UP",
	// "OPEN","CLOSE","UP",
	// "OPEN","CLOSE","UP",
	// "OPEN","CLOSE","DOWN",
	// "OPEN","CLOSE","DOWN",
	// "OPEN","CLOSE","DOWN",
	// "OPEN","CLOSE","DOWN",
	// "OPEN","CLOSE","DOWN"
	};

	public String nextCommand() {
		String commandToReturn = commands[(count++) % commands.length];
		boolean upOrDownState = count % 3 == 0;

		if (upOrDownState) {
			boolean currentFloorIsMax = (maxFloor == currentFloor);
			boolean currentFloorIsMin = (minFloor == currentFloor);

			if (currentFloorIsMax && currentFloorIsMin) {
				commandToReturn = NOTHING;
				System.out.println("++ currentFloor: "+currentFloor+ " maxFloor: "+maxFloor+" minFloor"+minFloor);
			} else {
				commandToReturn = chooseUpOrDown(
						commandToReturn);
			}

		}else{
			if (previousCommand.equals(UP)) {
				currentFloor++;
			} else if (previousCommand.equals(DOWN)) {
				currentFloor--;
			}
		}

		incrementConsecutivesDirections(commandToReturn);
		previousCommand = commandToReturn;
		return commandToReturn;
	}

	private String chooseUpOrDown(String commandToReturn) {

		if (maxFloor > currentFloor) {
			commandToReturn = UP;
		} else if (minFloor < currentFloor && (maxFloor == currentFloor)) {
			commandToReturn = DOWN;
//			maxFloor = minFloor;
		} else if(minFloor == currentFloor){
			commandToReturn = UP;
		} else if(currentFloor == 0){
			commandToReturn = NOTHING;			
		}

		return commandToReturn;
	}

	private void incrementConsecutivesDirections(String commandToReturn) {
		if (commandToReturn.equals(UP)) {
			consecutiveUp++;
		} else if (commandToReturn.equals(DOWN)) {
			consecutiveDown++;
		}
	}

	public String reset() {
		instance = new Elevator();
		return "reset";
	}

	public void actionFloor(int floorToGo) {
		if(currentFloor == 0){
			minFloor = floorToGo;
		}else{
			minFloor = Math.min(minFloor, floorToGo);
		}
		maxFloor = Math.max(maxFloor, floorToGo);
		System.out.println("floorToGo: "+floorToGo+ " maxFloor: "+maxFloor+" minFloor"+minFloor);
	}

}
