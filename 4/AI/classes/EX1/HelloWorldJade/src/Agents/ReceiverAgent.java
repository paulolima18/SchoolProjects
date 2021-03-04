package Agents;

import Behaviours.Receiver_Behaviour;
import jade.core.Agent;

public class ReceiverAgent extends Agent {

	protected void setup() 
    {
		System.out.println("Waiting for new Agents == from " + getLocalName());
		
		this.addBehaviour(new Receiver_Behaviour(this));
	}

	protected void takeDown() {
	}
}
