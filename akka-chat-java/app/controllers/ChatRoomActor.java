package controllers;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import play.Logger;
import play.libs.Akka;
import play.libs.Comet;
import play.libs.F.Callback0;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ChatRoomActor extends UntypedActor {

	
	static ActorRef instance = Akka.system().actorOf(
			new Props(ChatRoomActor.class));
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	List<Comet> sockets = new ArrayList<Comet>();
	SimpleDateFormat dateFormat = new SimpleDateFormat("HH mm ss");

	public void onReceive(Object message) {

		// Handle connections
		if (message instanceof Comet) {
			final Comet cometSocket = (Comet) message;

			if (sockets.contains(cometSocket)) {

				// Brower is disconnected
				sockets.remove(cometSocket);
				Logger.info("Browser disconnected (" + sockets.size()
						+ " browsers currently connected)");

			} else {

				// Register disconnected callback
				cometSocket.onDisconnected(new Callback0() {
					public void invoke() {
						getContext().self().tell(cometSocket);
					}
				});

				// New browser connected
				sockets.add(cometSocket);
				Logger.info("New browser connected (" + sockets.size()
						+ " browsers currently connected)");

			}

		}

		if (message instanceof ChatMessage) {
			for (Comet cometSocket : sockets) {
				cometSocket.sendMessage("say " + ((ChatMessage) message).msg);
			}
			Logger.info("Got message, send it to " + sockets.size()
					+ "members.");
		}

	}

}
