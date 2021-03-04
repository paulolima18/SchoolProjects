package Behaviours;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class ReceberConfirmacao extends CyclicBehaviour {

	public ReceberConfirmacao() {
		// TODO Auto-generated constructor stub
	}

	public ReceberConfirmacao(Agent a) {
		super(a);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		ACLMessage msg = myAgent.receive();
		if (msg != null) {
			// Message Threatment based on different ACLMessage performatives
			if (msg.getPerformative() == ACLMessage.CONFIRM) {
				System.out.println( "--------------------------------\n" + myAgent.getLocalName() + ": Reply CONFIRM = Product " + msg.getContent() );
			}
			else if (msg.getPerformative() == ACLMessage.REFUSE) {
				System.out.println( "--------------------------------\n" + myAgent.getLocalName() + ": Reply REFUSE = Product " + msg.getContent() );
			}
		}
	}
}
