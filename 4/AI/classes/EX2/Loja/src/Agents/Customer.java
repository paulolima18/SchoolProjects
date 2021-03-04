package Agents;

import Behaviours.Comprarprodutos;
import Behaviours.ReceberConfirmacao;
import jade.core.Agent;

public class Customer extends Agent {

	protected void setup() {
		super.setup();

		addBehaviour(new Comprarprodutos(this, 1000)); // fazer a compra de um produto
		addBehaviour(new ReceberConfirmacao(this)); // saber se a requisição teve sucesso
	}

	protected void takeDown() {
		super.takeDown();
	}

}