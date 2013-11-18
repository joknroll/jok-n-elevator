package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.POJONode;

import models.Elevator;
import play.*;
import play.mvc.*;
import views.html.*;
import play.libs.Json;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Application extends Controller {

	
	public Application(){
	}
	
	public static Result index() {
		return ok(views.html.index.render(Elevator.getInstance()));
	}

	public static Result nextCommand(){
		return ok(Elevator.getInstance().nextCommand());		
	}
	
	public static Result reset(String cause, Integer lowerFloor, Integer higherFloor, Integer cabinSize){
		if(lowerFloor != null && higherFloor != null && cabinSize != null){
			Elevator.getInstance().reset(lowerFloor,higherFloor,cabinSize );			
		}else{			
			Elevator.getInstance().reset();
		}
		return ok("Reset Elevator. Cause: "+cause+ "  lowerFloor: "+lowerFloor+" ,higherFloor: "+higherFloor+" ,cabinSize: "+cabinSize);		
	}

	public static Result go(int floorToGo){
		Elevator.getInstance().addGo(floorToGo);
		return ok("go floorToGo: "+floorToGo);		
	}
	public static Result call(int atFloor, String to){
		Elevator.getInstance().addCall(atFloor, to);
		return ok("call atFloor: "+atFloor+" to:"+to);		
	}
	public static Result userHasEntered(){
		Integer nbOfUser =  Elevator.getInstance().addUser();
		return ok("userHasEntered. Now we are: "+nbOfUser);		
	}
	public static Result userHasExited(){
		Integer nbOfUser = Elevator.getInstance().removeUser();
		return ok("userHasExited Now we are: "+nbOfUser);		
	}
	
	
	@BodyParser.Of(BodyParser.Json.class)
	public static Result elevatorJson() {
	  ObjectNode result = Json.newObject();

	    result.put("name", Elevator.getInstance().name);
	    result.put("userIn", Elevator.getInstance().userIn.get());
	    result.put("currentFloor", Elevator.getInstance().currentFloor);
	    result.put("lowerFloor", Elevator.getInstance().lowerFloor);
	    result.put("higherFloor", Elevator.getInstance().higherFloor);
	    result.put("cabinSize", Elevator.getInstance().cabinSize);
	    
	    POJONode resultGoQueue =  new POJONode(Elevator.getInstance().goQueue);
	    POJONode resultCallQueue =  new POJONode(Elevator.getInstance().callQueue);
	    ObjectMapper objectMapper = new ObjectMapper();
	    
	    result.put("goQueue", resultGoQueue);
	    result.put("callQueue", objectMapper.valueToTree(resultCallQueue));
	    return ok(result);
	}

}
