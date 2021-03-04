package Classes;

public class Position implements java.io.Serializable {
	
	private int x,y;
	
	public Position(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void update(int x, int y){
		this.x = x;
		this.y = y;
	}

	public int distancia(int a, int b){
		return (int) Math.sqrt((this.x-a)^2+(this.y-b)^2);
	}

	public int distancia(Position p){
		int d =  (int) Math.sqrt((this.x-p.getX())^2+(this.y-p.getY())^2);
		if (d<2)
			d = 2;
		return d;
	}

	public boolean mesma_posicao(Position p){
		if(this.x == p.getX() && this.y == p.getY())
			return true;
		return false;
	}

	public String toString() {
		return "Position [x: " + x  + ", y: "+ y + "]";
	}
}
