package Agents;

import Classes.InformStationStatus;
import Classes.Position;
import Classes.PositionStatus;
import Classes.Variables;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.Random;

public class Station extends Agent {

	private InformStationStatus station;


	protected void setup() {
		super.setup();

		// Register Agent in Directory Facilitator (Yellow Pages)
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setName(getLocalName());
		sd.setType("station");
		dfd.addServices(sd);

		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}

		Random rand = new Random();
		Position position = new Position(rand.nextInt(100), rand.nextInt(100));
		int low = Variables.NUM_BIKES_MIN;
		int high = Variables.NUM_BIKES_MAX;
		int bikes = rand.nextInt(high - low) + low;
		int raio = rand.nextInt(Variables.APE_RADIUS_MAX - Variables.APE_RADIUS_MIN) + Variables.APE_RADIUS_MIN;

		station = new InformStationStatus(this.getAID(), position, bikes, raio);
		addBehaviour(new EnviaParaInterfaceStation(station));
		addBehaviour(new Receiver_request());
	}

	protected void takeDown() {
		super.takeDown();
		// De-register Agent from DF before killing it
		try {
			DFService.deregister(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private class Receiver_request extends CyclicBehaviour {

		public void action() {
			ACLMessage msg = receive();
			if (msg != null) {
				String client = msg.getSender().getLocalName();
				ACLMessage resp = msg.createReply();

				if (msg.getPerformative() == ACLMessage.REQUEST && msg.getContent().equals("Request_Bike")) {
					if (!station.isEmpty()) {
						station.requestBike();
						addBehaviour(new EnviaParaInterfaceStation(station));
						try {
							resp.setContentObject(station.getPosition());
						} catch (IOException e) {
							e.printStackTrace();
						}
						resp.setPerformative(ACLMessage.CONFIRM);
					} else {
						resp.setContent("Não ha bicibletas disponiveis");
						resp.setPerformative(ACLMessage.REFUSE);
					}
					myAgent.send(resp);
				} else {
					if (msg.getPerformative() == ACLMessage.CFP) {
						try {
							PositionStatus client_obj = (PositionStatus) msg.getContentObject();
							if (client_obj.getStatus().equals("34 percurso")) {
								if (station.area_proximidade(client_obj.getAtual()) && !station.isFull()) {
									PositionStatus status_pos = new PositionStatus(station.getPosition(), "34 percurso");
									resp.setContentObject(status_pos);
									resp.setPerformative(ACLMessage.PROPOSE);
									myAgent.send(resp);
									System.out.println("--------------------------------\n" + myAgent.getLocalName() + ": " + client + " dentro da area de proximidade");
								}
							} else if (client_obj.getStatus().equals("fim percurso")) {
								if (!station.isFull()) {
									PositionStatus status_pos = new PositionStatus(station.getPosition(), "fim percurso");
									resp.setContentObject(status_pos);
									resp.setPerformative(ACLMessage.PROPOSE);
									myAgent.send(resp);
								} else {
									System.out.println("--------------------------------\n" + myAgent.getLocalName() + ": " + client + " a estaçao encontra-se cheia");
								}
							}
						} catch (UnreadableException | IOException e) {
							e.printStackTrace();
						}
					}
					if (msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
						System.out.println("--------------------------------\n" + myAgent.getLocalName() + ": " + client + " aceitou pedido");
						station.deliveryBike();
						addBehaviour(new EnviaParaInterfaceStation(station));
					}

				}
			} else {
				block();
			}
		}
	}


	private static class EnviaParaInterfaceStation extends OneShotBehaviour {

		InformStationStatus info_station;
		public EnviaParaInterfaceStation(InformStationStatus info){
			this.info_station = info;
		}

		public void action(){
			DFAgentDescription templatei = new DFAgentDescription();
			ServiceDescription sdi = new ServiceDescription();
            sdi.setType("interface");
            templatei.addServices(sdi);
			try {
				DFAgentDescription[] resulti = DFService.search(myAgent, templatei);

				// se interface esta disponivel!
				if (resulti.length > 0) {

					ACLMessage mensagem = new ACLMessage(ACLMessage.INFORM);
					for (DFAgentDescription dfAgentDescription : resulti)
						mensagem.addReceiver(dfAgentDescription.getName());

					mensagem.setContentObject(info_station);
					mensagem.setConversationId("station_info");
					myAgent.send(mensagem);
				}
				else {
					System.out.println("--------------------------------\n" + myAgent.getLocalName() + ": Agent Interface not found!");
				}
			} catch (FIPAException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
