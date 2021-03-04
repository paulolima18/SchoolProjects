package Behaviours;

import java.util.ArrayList;
import java.util.Random;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class Comprarprodutos extends TickerBehaviour {
	
	private ArrayList<String> products;

	public Comprarprodutos(Agent a, long period) {
		super(a, period);
		// TODO Auto-generated constructor stub
		products = new ArrayList<String>();
		products.add("A");
		products.add("B");
		products.add("C");
		products.add("D");
		products.add("E");
		products.add("F");
	}

	@Override
	protected void onTick() {
		// TODO Auto-generated method stub
		Random randomizer = new Random();
		String random = products.get(randomizer.nextInt(products.size())); // Escolhe um produto random da lista

		try {
			// Build the description used as template for the search
			DFAgentDescription dfd = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType("seller");
			dfd.addServices(sd);

			// Search DF
			DFAgentDescription[] results = DFService.search(myAgent, dfd);

			if (results.length > 0) {
				System.out.println("--------------------------------\n" + myAgent.getLocalName() + ": Agent Seller found!");
				for (DFAgentDescription dfd1 : results) {
					// Agent Found
					AID provider = dfd1.getName();

					ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
					msg.addReceiver(provider);
					msg.setContent(random);
					myAgent.send(msg);

				}
			} else {
				System.out.println("--------------------------------\n" + myAgent.getLocalName() + ": Agent Seller not found!");
			}
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}

}
