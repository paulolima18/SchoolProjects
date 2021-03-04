package Agents;

import java.io.IOException;
import java.util.Random;

import Classes.PositionStatus;
import Classes.Position;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class Client extends Agent {

	private int tempo_viagem;
	private int speed;
	private boolean percurso;
	private Position atual;
	private String status;
	// usado para ir em linha reta da posiçao aonde esta até a estaçao final
	private Position inicial;
	private Position station_final;
	private int station_inicial;
	private int v; // divisao do percurso
	private int k; // ponto em que esta no momento

	protected void setup() {
		super.setup();

		station_inicial = -1;
		status = "espera";
		this.addBehaviour(new Request_bike());
		this.addBehaviour(new Reply());
	}

	protected void takeDown() {
		super.takeDown();
	}

	private class Request_bike extends OneShotBehaviour {

		public void action() {

			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType("station");
			template.addServices(sd);

			try {
				DFAgentDescription[] result = DFService.search(myAgent, template);

				// pelo menos uma station esta disponivel!
				if (result.length > 0) {
					Random rand = new Random();

//					escolhe aleatoriamente a estaçao aonde faz o pedido
					int inicial = rand.nextInt(result.length);
					while (station_inicial == inicial)
						inicial = rand.nextInt(result.length);
					station_inicial = inicial;
					ACLMessage mensagem = new ACLMessage(ACLMessage.REQUEST);
					mensagem.addReceiver(result[station_inicial].getName());
					mensagem.setContent("Request_Bike");
					myAgent.send(mensagem);
				} else {
					System.out.println("--------------------------------\n" + myAgent.getLocalName() + ": Agent Station not found!");
				}
			} catch (FIPAException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private class Reply extends CyclicBehaviour {

		public void action() {
			ACLMessage msg = receive();
			if (msg != null) {
				if (msg.getPerformative() == ACLMessage.CONFIRM) {
					// se existem bicicletas disponiveis
					try {
						atual = (Position) msg.getContentObject();
					} catch (UnreadableException e) {
						e.printStackTrace();
					}
					Random random = new Random();
					tempo_viagem = random.nextInt(90) + 1;
					speed = random.nextInt(5 - 3) + 3;
					percurso = true;
					k = -2;
					v = -1;
					int x = tempo_viagem * 3000 / 4;
					status = "alugada";
					addBehaviour(new Atualiza_posicao(this.myAgent, 1000));
					addBehaviour(new Avisa_meio_percurso(this.myAgent, x));
					addBehaviour(new EnviaParaInterface());

				} else if (msg.getPerformative() == ACLMessage.REFUSE) {
					// nao ha bicicletas disponiveis
					Random random = new Random();
					int prob = random.nextInt(100);
					if (prob <= 40) {
//						espera
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						ACLMessage resp = new ACLMessage(ACLMessage.REQUEST);
						resp.addReceiver(msg.getSender());
						resp.setContent("Request_Bike");
						myAgent.send(resp);
					}
					//vai a outra estaçao
					if (prob > 40 && prob <= 80) {
						addBehaviour(new Request_bike());
					}
					//desiste
					if (prob > 80) {
						myAgent.doDelete();
					}
				} else if (msg.getPerformative() == ACLMessage.PROPOSE) {
					ACLMessage resp = msg.createReply();
					try {
						PositionStatus station_obj = (PositionStatus) msg.getContentObject();
						if (station_obj.getStatus().equals("34 percurso")) {
							Random random = new Random();
							int accept = random.nextInt(100);
							if (accept < 75 && percurso) {
								resp.setContent("ACEITO");
								resp.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
								myAgent.send(resp);

								percurso = false;
								station_final = station_obj.getAtual();
								inicial = new Position(atual.getX(), atual.getY());
								v = atual.distancia(station_final);
								k = 0;
							}
						}
						if (station_obj.getStatus().equals("fim percurso")) {
							if (percurso) {
								resp.setContent("ACEITO");
								resp.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
								myAgent.send(resp);
								percurso = false;
								station_final = station_obj.getAtual();
								inicial = new Position(atual.getX(), atual.getY());
								v = atual.distancia(station_final);

								k = 0;
							}
						}
					} catch (UnreadableException e) {
						e.printStackTrace();
					}
				}
			} else {
				block();
			}
		}
	}

	private class Atualiza_posicao extends TickerBehaviour {
		public Atualiza_posicao(Agent a, long period) {
			super(a, period);
		}

		@Override
		public void onTick() {
			if (percurso) {
				int x = -1, y = -1;
				while (x < 0 || x > 100 || y < 0 || y > 100) {
					int angle = (int) (Math.random() * Math.PI * 2);
					x = (int) (Math.cos(angle) * speed + atual.getX());
					y = (int) (Math.sin(angle) * speed + atual.getY());
				}
				atual.update(x, y);
			} else {
				if (!atual.mesma_posicao(station_final)) {
					int x = (int) (k / (double) v * station_final.getX() + (v - k) / (double) v * inicial.getX());
					int y = (int) (k / (double) v * station_final.getY() + (v - k) / (double) v * inicial.getY());
					atual.update(x, y);
					k++;
				} else {
					status = "cheguei";
				}
			}
			addBehaviour(new EnviaParaInterface());

		}
	}


	private class Avisa_meio_percurso extends WakerBehaviour {

		public Avisa_meio_percurso(Agent a, long timeout) {
			super(a, timeout);
		}

		public void handleElapsedTimeout() {

			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType("station");
			template.addServices(sd);

			try {
				DFAgentDescription[] result = DFService.search(myAgent, template);

				// pelo menos uma station esta disponivel!
				if (result.length > 0) {
					ACLMessage mensagem = new ACLMessage(ACLMessage.CFP);

					// avisa todas as estaçoes
					for (DFAgentDescription dfAgentDescription : result)
						mensagem.addReceiver(dfAgentDescription.getName());

					status = "34 percurso";

					PositionStatus client_obj = new PositionStatus(atual, status);
					mensagem.setContentObject(client_obj);
					myAgent.send(mensagem);
				} else {
					System.out.println("--------------------------------\n" + myAgent.getLocalName() + ": Agent Station not found!");
				}
				addBehaviour(new Final_percurso(this.myAgent, tempo_viagem / 4));
			} catch (FIPAException | IOException e) {
				e.printStackTrace();
			}
		}
	}


	private class Final_percurso extends WakerBehaviour {

		public Final_percurso(Agent myAgent, int tempo_viagem) {
			super(myAgent, tempo_viagem);
		}

		@Override
		public void handleElapsedTimeout() {
			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType("station");
			template.addServices(sd);
			if (percurso) {
				try {
					DFAgentDescription[] result = DFService.search(myAgent, template);

					// pelo menos uma station esta disponivel!
					if (result.length > 0) {
						ACLMessage mensagem = new ACLMessage(ACLMessage.CFP);

						// avisa todas as estaçoes
						for (DFAgentDescription dfAgentDescription : result)
							mensagem.addReceiver(dfAgentDescription.getName());

						status = "fim percurso";
						PositionStatus client_obj = new PositionStatus(atual, status);
						mensagem.setContentObject(client_obj);
						myAgent.send(mensagem);
					} else {
						System.out.println("--------------------------------\n" + myAgent.getLocalName() + ": Agent Station not found!");
					}
				} catch (FIPAException | IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private class EnviaParaInterface extends OneShotBehaviour {

		PositionStatus info_client = new PositionStatus(atual, status, tempo_viagem);

		@Override
		public void action() {
			DFAgentDescription templatei = new DFAgentDescription();
			ServiceDescription sdi = new ServiceDescription();
			sdi.setType("interface");
			templatei.addServices(sdi);
			try {
				DFAgentDescription[] resulti = DFService.search(myAgent, templatei);

				// se interface esta disponivel!
				if (resulti.length > 0) {

					ACLMessage mensagem = new ACLMessage(ACLMessage.INFORM);
					for (int i = 0; i < resulti.length; i++)
						mensagem.addReceiver(resulti[i].getName());

					mensagem.setContentObject(info_client);
					mensagem.setConversationId("client_info");
					myAgent.send(mensagem);

					if(info_client.getStatus().equals("cheguei")){
						this.myAgent.doDelete();
					}
				}
				else{
					System.out.println("--------------------------------\n" + myAgent.getLocalName() + ": Agent Interface not found!");
				}
			} catch(FIPAException | IOException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
