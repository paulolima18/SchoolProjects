package Classes;

public class PositionStatus implements java.io.Serializable{

    private int tempo_viagem;
    private Position atual;
    private String status;

    public PositionStatus(Position atual, String status){
        this.atual = atual;
        this.status = status;
        this.tempo_viagem = 0;
    }

    public PositionStatus(Position atual, String status, int tempo_viagem){
        this.atual = atual;
        this.status = status;
        this.tempo_viagem = tempo_viagem;
    }

        public void setAtual(Position atual) {
        this.atual = atual;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Position getAtual() {
        return atual;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "PositionStatus{" +
                "atual=" + atual +
                ", status='" + status + '\'' +
                '}';
    }

    public int getTempoViagem() {
        return this.tempo_viagem;
    }
}
