package Agents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class Seller extends Agent {

	private HashMap<String, Integer> products_sold = new HashMap<>();
	private HashMap<String, Integer> products_value = new HashMap<>();

	protected void setup() {
		super.setup();

		// Register Agent in Directory Facilitator (Yellow Pages)
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setName(getLocalName());
		sd.setType("seller");
		dfd.addServices(sd);

		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		
		// Prepare Variables
		products_sold.put("A", 0); products_sold.put("B", 0); products_sold.put("C", 0); products_sold.put("D", 0);
		products_value.put("A", 5); products_value.put("B", 2); products_value.put("C", 7); products_value.put("D", 3);

		addBehaviour(new ReceberPedidosEProdutos());
		addBehaviour(new VerLucro(this, 10000));
	}

	protected void takeDown() {
		super.takeDown();
		// De-register Agent from DF before killing it
		try {
			DFService.deregister(this);
		} 
		catch (Exception e) {
		}
	}
	
	// Behaviours
	private class ReceberPedidosEProdutos extends CyclicBehaviour {
		public void action() {
			ACLMessage msg = myAgent.receive();
			if (msg != null && msg.getPerformative() == ACLMessage.REQUEST) { // receber pedidos de requisição
				
				String clienteP = msg.getSender().getLocalName();
				ACLMessage resp = msg.createReply();
				
				String produtoPedido = msg.getContent();
				
				// Take care if requested product is available in shop
				if (products_value.containsKey(produtoPedido)) {
					if (produtoPedido.equals("A")) {
						products_sold.put("A", products_sold.get("A") + 1);
						resp.setContent("A");
						resp.setPerformative(ACLMessage.CONFIRM);
					} else if (produtoPedido.equals("B")) {
						products_sold.put("B", products_sold.get("B") + 1);
						resp.setContent("B");
						resp.setPerformative(ACLMessage.CONFIRM);
					} else if (produtoPedido.equals("C")) {
						products_sold.put("C", products_sold.get("C") + 1);
						resp.setContent("C");
						resp.setPerformative(ACLMessage.CONFIRM);
					} else if (produtoPedido.equals("D")) {
						products_sold.put("D", products_sold.get("D") + 1);
						resp.setContent("D");
						resp.setPerformative(ACLMessage.CONFIRM);
					}
					System.out.println("--------------------------------\n" + myAgent.getLocalName() + ": Produto " + produtoPedido + " requisitado por " + clienteP);
				}
				// Take care if requested product is not available in shop
				else {
					System.out.println("--------------------------------\n" + myAgent.getLocalName() + ": Produto " + produtoPedido + " pedido por " + clienteP + " não existe");
					resp.setContent(produtoPedido);
					resp.setPerformative(ACLMessage.REFUSE);
				}
				myAgent.send(resp);
			}
			block();
		}
	}

	private class VerLucro extends TickerBehaviour {
		public VerLucro(Agent a, long period) {
			super(a, period);
			// TODO Auto-generated constructor stub
		}

		protected void onTick() {
			int total = products_value.get("A") * products_sold.get("A") + 
					products_value.get("B") * products_sold.get("B") + 
					products_value.get("C") * products_sold.get("C") + 
					products_value.get("D") * products_sold.get("D");
			
			System.out.println("--------------------------------\n" + myAgent.getLocalName() + ": Valor Angariado: " + total);
		}
	}
}
