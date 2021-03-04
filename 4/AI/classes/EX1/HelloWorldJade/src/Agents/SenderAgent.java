package Agents;

import Behaviours.Sender_Behaviour;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class SenderAgent extends Agent {

	protected void setup() {
		// Prepare Message
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		AID reader = new AID("Receiver", AID.ISLOCALNAME);
		msg.addReceiver(reader);
		msg.setContent("Hello Receiver. I request a new home to live. == from "
				+ getLocalName());

		// Send Message
		send(msg);

		// Receive Reply
		addBehaviour(new Sender_Behaviour(this));

	}

	protected void takeDown() {
	}
}
