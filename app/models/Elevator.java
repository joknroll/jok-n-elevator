package models;

import java.math.BigDecimal;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import tools.Call;
import tools.Directions;

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

	public AtomicInteger userIn = new AtomicInteger();

	public Queue<Integer> goQueue = new ConcurrentLinkedQueue<Integer>();
	public Queue<Call> callQueue = new ConcurrentLinkedQueue<Call>();

	public String name = "jok Elevator";
	public String previousCommand = "OPEN";

	public static synchronized Elevator getInstance() {
		if (instance == null) {
			instance = new Elevator();
		}
		return instance;
	}

	private static String commands[] = { "NOTHING", "OPEN", "CLOSE"
	};

	public String nextCommand() {
		boolean upOrDownState = count % 3 == 0;
		String commandToReturn = "NONE";
		if (upOrDownState) {
			boolean noCallAndNoGo = goQueue.size() == 0 && callQueue.size() == 0;
			if (noCallAndNoGo) {
				commandToReturn = NOTHING;
				count = 0;
			} else {
				boolean onlyCall = goQueue.size() == 0 && callQueue.size() > 0;
				boolean onlyGo = goQueue.size() > 0 && callQueue.size() == 0;
				boolean both = goQueue.size() > 0 && callQueue.size() > 0;
				if (onlyGo) {
					commandToReturn = onlyGoAction(commandToReturn);
				} else if(onlyCall){
					commandToReturn = onlyCallAction(commandToReturn);
				} else if (both) {
					commandToReturn = onlyGoAction(commandToReturn);
				}
			}
			count  = 1;
		} else {
			if (previousCommand.equals(UP)) {
				currentFloor++;
			} else if (previousCommand.equals(DOWN)) {
				currentFloor--;
			}
			commandToReturn = commands[(count++) % commands.length];
		}

		previousCommand = commandToReturn;
		return commandToReturn;
	}

	private String onlyGoAction(String commandToReturn) {
		int go = goQueue.peek();

		boolean iAmAtAGoFloor = currentFloor == go;

		if (iAmAtAGoFloor) {
			goQueue.poll();
			if(goQueue.size() > 0){
				go = goQueue.peek();
				commandToReturn = goUpOrDown(commandToReturn, go);
			}else{
				commandToReturn = "NOTHING";
			}
		}else{
			commandToReturn = goUpOrDown(commandToReturn, go);
		}
		
		return commandToReturn;
	}

	private String goUpOrDown(String commandToReturn, int go) {
		boolean goFloorIsUp = currentFloor < go;
		boolean goFloorIsDown = currentFloor > go;
		if (goFloorIsUp) {
			commandToReturn = "UP";
		} else if(goFloorIsDown){
			commandToReturn = "DOWN";
		}else{
			commandToReturn = "NOTHING";
			System.out.println("goUpOrDown : i am lost");
		}
		return commandToReturn;
	}

	private String onlyCallAction(String commandToReturn) {
		Call call = callQueue.peek();
		boolean iAmAtACallFloor = currentFloor == call.atFloor;
		boolean callFloorIsUp = currentFloor < call.atFloor;
		boolean callFloorIsDown = currentFloor > call.atFloor;
		if (iAmAtACallFloor) {
			callQueue.poll();
			if(callQueue.size() >0){
				call = callQueue.peek();
				commandToReturn = callUpOrDown(commandToReturn, call);
			}else{
				commandToReturn = "NOTHING";
			}
		} else {
			commandToReturn = callUpOrDown(commandToReturn, call);
		}
			
		return commandToReturn;
	}

	private String callUpOrDown(String commandToReturn, Call call) {
		boolean callFloorIsUp = currentFloor < call.atFloor;
		boolean callFloorIsDown = currentFloor > call.atFloor;
		if (callFloorIsUp) {
			commandToReturn = "UP";
		} else if(callFloorIsDown){
			commandToReturn = "DOWN";
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

	public void addGo(int floorToGo) {
		goQueue.offer(floorToGo);
	}

	public void addCall(int atFloor, String to) {
		Call call = new Call(atFloor, Directions.valueOf(to));
		callQueue.offer(call);
	}

	public Integer addUser() {
		return userIn.incrementAndGet();
	}

	public Integer removeUser() {
		return userIn.decrementAndGet();
	}

}
