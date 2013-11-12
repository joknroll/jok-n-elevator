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

	public Integer lowerFloor;
	public Integer higherFloor;
	public Integer cabinSize;
	
	
	public AtomicInteger userIn = new AtomicInteger();

	public Queue<Integer> goQueue = new ConcurrentLinkedQueue<Integer>();
	public Queue<Call> callQueue = new ConcurrentLinkedQueue<Call>();

	public String name = "jok Elevator";
	public String previousCommand = "OPEN";

	
	public Elevator(){
		this(0,19,20);
	}
	/**
	 * 
	 * @param lowerFloor
	 * @param higherFloor
	 * @param cabinSize
	 */
	public Elevator(Integer lowerFloor, Integer higherFloor, Integer cabinSize) {
		this.lowerFloor = lowerFloor;
		this.higherFloor = higherFloor;
		this.cabinSize = cabinSize;
		this.currentFloor = lowerFloor;
	}

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
		boolean openState = count % 3 == 1;
		boolean closeState = count % 3 == 2;
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
		}else{
			commandToReturn = "NOTHING";
			System.out.println("callUpOrDown : i am lost");
		}
		return commandToReturn;
	}

	public String reset(Integer lowerFloor, Integer higherFloor, Integer cabinSize) {
		instance = new Elevator(lowerFloor,higherFloor,cabinSize);
		System.out.println("Rest : Create new instance of elevator with lowerFloor: "+lowerFloor+" ,higherFloor: "+higherFloor+" ,cabinSize: "+cabinSize);
		return "reset";
	}
	public String reset() {
		instance = new Elevator(0, 19, 20);
		return "reset";
	}

	public void addGo(int floorToGo) {
		if(floorToGo >= lowerFloor && floorToGo <= higherFloor){
			goQueue.offer(floorToGo);
		}
	}

	public void addCall(int atFloor, String to) {
		if(atFloor >= lowerFloor && atFloor <= higherFloor){
			Call call = new Call(atFloor, Directions.valueOf(to));
			callQueue.offer(call);
		}
	}

	public Integer addUser() {
		if(userIn.get() >= cabinSize){
			System.out.println("max size reach: "+cabinSize);
		}
		return userIn.incrementAndGet();			
	}

	public Integer removeUser() {
		return userIn.decrementAndGet();
	}

}
