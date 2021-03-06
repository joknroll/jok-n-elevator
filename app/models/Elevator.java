package models;

import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.databind.node.ObjectNode;

import tools.Call;
import tools.Directions;
import tools.Door;
import views.html.defaultpages.badRequest;

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

	public Map<Integer , Integer > waitingUserToUpByFloor = new ConcurrentHashMap<Integer, Integer>();
	public Map<Integer , Integer > waitingUserToDownByFloor = new ConcurrentHashMap<Integer, Integer>();
	public Map<Integer , Integer > goUserByFloor = new ConcurrentHashMap<Integer, Integer>();	
	
	// TODO add map of nbWaitingUserByStage, use it to 
	
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
		
		for(int i = lowerFloor; i <= higherFloor; i++){
			waitingUserToUpByFloor.put(i, 0);
			waitingUserToDownByFloor.put(i, 0);
			goUserByFloor.put(i, 0);
		}
		
		instance = this;
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
		String commandToReturn = "NOTHING";
		if (upOrDownState) {
			commandToReturn = getUpOrDownCommand(commandToReturn);
		} else {
			updateFloor();
			boolean doIHaveToStop = doIHaveToStop();
			if(doIHaveToStop){
				commandToReturn = commands[(count++) % commands.length];				
			}else if(previousCommand.equals("CLOSE") || previousCommand.equals("UP") || previousCommand.equals("DOWN")){
				count = 0;
				commandToReturn = getUpOrDownCommand(commandToReturn);
			}else{
				commandToReturn = commands[(count++) % commands.length];				
			}
		}
		previousCommand = commandToReturn;
		return commandToReturn;
	}
	
	private void updateFloor() {
		if (previousCommand.equals(UP)) {
			currentFloor++;
		} else if (previousCommand.equals(DOWN)) {
			currentFloor--;
		}
	}

	private String getUpOrDownCommand(String commandToReturn) {
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
		boolean imOnTop = currentFloor == higherFloor; 
		boolean imOnBottom = currentFloor == lowerFloor; 
		if (goFloorIsUp || imOnBottom) {
			commandToReturn = "UP";
		} else if(goFloorIsDown || imOnTop){
			commandToReturn = "DOWN";
		}else{
			commandToReturn = goToNearestExtremity();
		}
		return commandToReturn;
	}
	private String goToNearestExtremity() {
		String commandToReturn;
		if(Math.abs(currentFloor - lowerFloor) >  Math.abs(currentFloor - higherFloor)){
			commandToReturn = "DOWN";
		}else{
			commandToReturn = "UP";
		}
		System.out.println("callUpOrDown : goToNearestExtremity "+commandToReturn);
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
		boolean imOnTop = currentFloor == higherFloor; 
		boolean imOnBottom = currentFloor == lowerFloor; 
		if (callFloorIsUp || imOnBottom) {
			commandToReturn = "UP";
		} else if(callFloorIsDown || imOnTop){
			commandToReturn = "DOWN";
		}else{
			commandToReturn = goToNearestExtremity();

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
			if(!goQueue.contains(floorToGo)){
				goQueue.offer(floorToGo);
			}
		}else{
			System.out.println("addGo out of limit: "+floorToGo+" (must be between "+lowerFloor+" and "+higherFloor+")" );
		}
	}

	public void addCall(int atFloor, String to) {
		if(atFloor >= lowerFloor && atFloor <= higherFloor){
			addCall(atFloor, Directions.valueOf(to));
		}else{
			System.out.println("addCall out of limit: "+atFloor+" (must be between "+lowerFloor+" and "+higherFloor+")" );
		}
	}
	
	public void addCall(int atFloor, Directions to) {
		Call call = new Call(atFloor, to);
		if(!callQueue.contains(call)){
			callQueue.offer(call);
		}
		if(call.to.equals(Directions.UP)){
			waitingUserToUpByFloor.put(call.atFloor, waitingUserToUpByFloor.get(call.atFloor) +1 );
		}else if(call.to.equals(Directions.DOWN)){
			waitingUserToDownByFloor.put(call.atFloor, waitingUserToDownByFloor.get(call.atFloor) +1 );			
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

	private boolean doIHaveToStop() {
		boolean doIHaveToStop = false;
		if(goQueue.size() > 0){
			Directions actualDirection = Directions.UP;
			boolean iAmAtAGoFloor = currentFloor ==  goQueue.peek();
			if(currentFloor > goQueue.peek()){
				actualDirection = Directions.DOWN;
			}
			boolean elevatorIsNotFull = (userIn.get() < cabinSize );
			boolean isCurrentFloorInCallQueueWithDirection = findCurrentFloorInCallQueueWithDirection(actualDirection);
			boolean isCurrentFloorInCallQueueWithDirectionAndCabineIsNotFull = elevatorIsNotFull && isCurrentFloorInCallQueueWithDirection;
			doIHaveToStop = iAmAtAGoFloor || isCurrentFloorInCallQueueWithDirectionAndCabineIsNotFull;
			if(iAmAtAGoFloor){
				removeCurentFloorInGoQueue();
			}
			if(isCurrentFloorInCallQueueWithDirectionAndCabineIsNotFull){
				removeCurrentFloorInCallQueueWithAllDirection();
				waitingUserToUpByFloor.put(currentFloor, 0);
				waitingUserToDownByFloor.put(currentFloor, 0);
			}
		}else if(callQueue.size() > 0){
			doIHaveToStop = findCurrentFloorInCallQueue();
			if(doIHaveToStop){
				removeCurrentFloorInCallQueueWithAllDirection();
				waitingUserToUpByFloor.put(currentFloor, 0);
				waitingUserToDownByFloor.put(currentFloor, 0);				
			}
		}
		return doIHaveToStop;
	}
	protected void removeCurentFloorInGoQueue() {
		while(goQueue.contains(currentFloor)){
			goQueue.remove(currentFloor);
		}
	}

	private boolean findCurrentFloorInCallQueue() {
		return findCallInCallQueue(new Call(currentFloor, Directions.UP)) || findCallInCallQueue(new Call(currentFloor, Directions.DOWN));
	}	
	
	private boolean findCurrentFloorInCallQueueWithDirection(Directions actualDirection) {
		return findCallInCallQueue(new Call(currentFloor, actualDirection)) ;
	}	
	private void removeCurrentFloorInCallQueueWithAllDirection() {
		removeCallInCallQueue(new Call(currentFloor, Directions.UP)) ;
		removeCallInCallQueue(new Call(currentFloor, Directions.DOWN)) ;
	}	
	
	protected boolean findGoInCallQueue(int i) {
		for(Integer goElement : goQueue){
			if(goElement == i){
				return true;
			}
		}
		return false;
	}
	

	protected boolean findCallInCallQueue(Call call){
		return callQueue.contains(call);
	}	
	
	protected void removeCallInCallQueue(Call call){
		while(callQueue.contains(call)){
			callQueue.remove(call);
		}
	}	
	
	

	
}
