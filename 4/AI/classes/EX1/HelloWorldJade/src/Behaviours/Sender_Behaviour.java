package Behaviours;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

public class Sender_Behaviour extends SimpleBehaviour {

	public Sender_Behaviour() {
		// TODO Auto-generated constructor stub
	}

	public Sender_Behaviour(Agent a) {
		super(a);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		ACLMessage msg = myAgent.receive();
		if (msg != null) {
			System.out.println(" == Answer" + " <- " + msg.getContent()
					+ " == from " + msg.getSender().getName());

			myAgent.doDelete();
		}
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}
