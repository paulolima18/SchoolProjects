package Classes;

import jade.core.AID;

public class InformStationStatus implements java.io.Serializable {

	private AID agent;
	private Position position;
	private int bikes;
	private int available;
	private int raio;
	
	public InformStationStatus(AID agent, Position position, int bikes, int raio) {
		super();
		this.agent = agent;
		this.position = position;
		this.bikes = bikes;
		this.available = bikes;
		this.raio = raio;
	}

	public InformStationStatus(AID agent, Position position, int bikes, int available, int raio) {
		super();
		this.agent = agent;
		this.position = position;
		this.bikes = bikes;
		this.available = available;
		this.raio = raio;
	}

	public InformStationStatus(AID agent,  int available) {
		super();
		this.agent = agent;
		this.available = available;
	}

	public AID getAgent() {
		return agent;
	}

	public void setAgent(AID agent) {
		this.agent = agent;
	}

	public Position getPosition() {
		return position;
	}

	public int getCapacity() {
		return bikes;
	}

	public int getAvailable() { return available;}

	public int getRaio() { return raio;}

	public void setPosition(Position position) {
		this.position = position;
	}

	public int numberBikes(){
		return bikes;
	}
	public int bikes_available() {
		return available;
	}

	public int deliveryBike() {
		return this.available++;
	}

	public int requestBike() {
		return this.available--;
	}

	public boolean isFull(){
		if(this.bikes==this.available)
			return true;
		else
			return false;
	}

	public boolean isEmpty(){
		if(this.available==0)
			return true;
		else
			return false;
	}

	public boolean area_proximidade(Position pos){
		// (x - a)^2 + (y - b)^2 <= r^2
		if((Math.pow((this.position.getX() - pos.getX()),2) + Math.pow((this.position.getY() - pos.getY()),2)) <= Math.pow(this.raio,2))
			return true;
		return false;
	}

	public boolean nivel_suficiente(){
		if(this.available <= 3/4 * this.bikes)
			return true;
		return false;
	}

	@Override
	public String toString() {
		return "InformPosition [agent=" + agent + ", position=" + position + ", bikes=" + bikes + ", available=" + available + ", radius=" + raio +"]";
	}
	
	

}
