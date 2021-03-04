package Behaviours;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class Receiver_Behaviour extends CyclicBehaviour {

	public Receiver_Behaviour() {
		// TODO Auto-generated constructor stub
	}

	public Receiver_Behaviour(Agent a) {
		super(a);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		ACLMessage msg = myAgent.receive();
		if (msg!=null) {
			System.out.println(" == Answer" + " <- " 
					 +  msg.getContent() + " == from "
					 +  msg.getSender().getName() );
			
			ACLMessage reply = msg.createReply();
            reply.setPerformative( ACLMessage.INFORM );
            reply.setContent("Welcome to your new home Agent " + msg.getSender().getName()+ ". You are most welcome!");
            myAgent.send(reply);
		}
		block();
	}

}
