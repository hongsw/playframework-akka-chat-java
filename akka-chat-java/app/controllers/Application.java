package controllers;

import play.*;
import play.mvc.*;
import play.libs.*;
import play.libs.F.*;
import akka.util.*;
import akka.actor.*;
import java.util.*;
import java.text.*;
import scala.concurrent.duration.Duration;
import static java.util.concurrent.TimeUnit.*;
import scala.concurrent.ExecutionContext$;
import views.html.*;

public class Application extends Controller {
	public static ActorRef chatRoomActor = Akka.system().actorOf(
			new Props(ChatRoomActor.class));

	public static Result index() {
		return ok(index.render());
	}

	public static Result chatRoom() {
		return ok(room.render());
	}

	public static Result javascriptRoutes() {
		response().setContentType("text/javascript");
		return ok(Routes.javascriptRouter("jsRoutes",
				controllers.routes.javascript.Application.say()));
	}
	
	public static Result say(final String msg) {
		chatRoomActor.tell(new ChatMessage(msg));
		return ok("Said " + msg);
	}

	public static Result stream() {
		return ok(new Comet("parent.message") {  
            public void onConnected() {
            	chatRoomActor.tell(this); 
            } 
        });
	}

}