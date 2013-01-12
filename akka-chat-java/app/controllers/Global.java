package controllers;

import play.Logger;

import java.util.concurrent.TimeUnit;

import play.Application;
import play.GlobalSettings;
import play.libs.Akka;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.Props;

public class Global extends GlobalSettings {

	@Override
	public void onStart(Application app) {
		Logger.info("Start ChatRoomActor");
		ActorRef instance = Akka.system().actorOf(
				new Props(ChatRoomActor.class));
		Akka.system()
				.scheduler()
				.schedule(Duration.Zero(),
						Duration.create(10, TimeUnit.SECONDS), instance,
						"VALIDATE", null);
	}

	public void onStop(Application app) {
		Logger.info("Stop ChatRoomActor");
	}
}