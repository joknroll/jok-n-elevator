package controllers;

import models.Elevator;
import play.*;
import play.mvc.*;
import views.html.*;

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

}
