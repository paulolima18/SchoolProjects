package Agents;

import Classes.InformStationStatus;
import Classes.PositionStatus;
import Classes.Variables;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Interface extends Agent {

    private HashMap<String,PositionStatus> clients;
    private HashMap<String, InformStationStatus> stations;

    private static int tamanho_mapa;
    private final DefaultCategoryDataset model = new DefaultCategoryDataset();
    private final DefaultCategoryDataset datasetAlugueres = new DefaultCategoryDataset();;
    private final DefaultCategoryDataset m = new DefaultCategoryDataset();
    private final DefaultPieDataset dataset = new DefaultPieDataset();
    private int seconds;
    private long tempo_minimo;
    private long tempo_maximo;
    private long tempo_medio;
    private long client_total;
    private long duration_total;
    private JFrame f;
    private Work work;

    protected void setup() {
        super.setup();

        this.clients = new HashMap<>();
        this.stations = new HashMap<>();

        this.tempo_maximo = 0;
        this.tempo_minimo = 1000000;
        this.tempo_medio = 0;
        this.duration_total = 0;
        this.client_total = 0;
        tamanho_mapa = Variables.TAM_MAPA;
        JFrame frame = new JFrame(getClass().getSimpleName());

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        work = new Work();
        JScrollPane scroll = new JScrollPane(work);
        frame.add(scroll);
        work.setPreferredSize(new Dimension(screenSize.width, screenSize.height));
        frame.pack();
        frame.setVisible(true);
        f = new JFrame("Estatisticas");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JFreeChart chart = ChartFactory.createLineChart(
                "Alugueres Ativos",
                "Tempo (s)",
                "Numero de alugueres",
                this.datasetAlugueres,
                PlotOrientation.VERTICAL,
                true,true,false);
        ChartPanel barPanel = new ChartPanel(chart);
        Update();
        JFreeChart chart2 = ChartFactory.createPieChart3D("Alugueres ativos por Estacao",
                dataset, // data
                true,
                true, true);
        PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator("{0}: {2}", new DecimalFormat("0"), new DecimalFormat("0%"));
        PiePlot plot1 = (PiePlot) chart2.getPlot();
        plot1.setLabelGenerator(labelGenerator);
        ChartPanel chartPanel = new ChartPanel(chart2);

        JFreeChart chart3 = ChartFactory.createBarChart("Tempo Medio", "", "Tempo", m, PlotOrientation.VERTICAL, false, false, false);
        ChartPanel barPanel2 = new ChartPanel(chart3);
        barPanel2.setPreferredSize(new Dimension(600,400));
        chartPanel.setPreferredSize(new Dimension(600,400));
        barPanel.setPreferredSize(new Dimension(600,400));
        f.getContentPane().add(barPanel,BorderLayout.SOUTH);
        f.getContentPane().add(chartPanel,BorderLayout.EAST);
        f.getContentPane().add(barPanel2,BorderLayout.CENTER);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        // Register Agent in Directory Facilitator (Yellow Pages)
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setName(getLocalName());
        sd.setType("interface");
        dfd.addServices(sd);

        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        addBehaviour(new Receiver());
        addBehaviour(new TimeCount(this,1000));
        addBehaviour(new printEstados(this, 3000));
    }

    protected void takeDown() {
        super.takeDown();
        // De-register Agent from DF before killing it
        try {
            DFService.deregister(this);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class Receiver extends CyclicBehaviour {
        public void action(){
            ACLMessage msg = receive();
            if (msg != null) {
                String sender = msg.getSender().getLocalName();
                if (msg.getPerformative() == ACLMessage.INFORM && msg.getConversationId().equals("station_info")){
                    try {
                        InformStationStatus station_info = (InformStationStatus) msg.getContentObject();
                        if(stations.containsKey(sender)){
                            stations.replace(sender, station_info);
                        }
                        else{
                            stations.put(sender, station_info);
                        }
                        work.updateUI();
                    } catch (UnreadableException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getPerformative() == ACLMessage.INFORM && msg.getConversationId().equals("client_info")) {
                    try {
                        PositionStatus client_info = (PositionStatus) msg.getContentObject();
                        if(clients.containsKey(sender)){
                            if (client_info.getStatus().equals("cheguei")) {

                                clients.remove(sender);
                            }else
                                clients.replace(sender, client_info);
                        } else {
                            TimeUpdate(client_info.getTempoViagem());
                            Update();
                            clients.put(sender, client_info);
                        }
                        work.updateUI();
                    } catch (UnreadableException e) {
                        e.printStackTrace();
                    }
                }
            }
            createSampleDataset();
        }
    }

    private class printEstados extends TickerBehaviour {
        public printEstados(Agent a, long period) {
            super(a, period);
        }

        @Override
        public void onTick() {
            PrintStream print = System.out;
            int tamanho = 14;
            int tam_impar = tamanho + 1;
            int espacos;

            String[] headers = {"ID", "Position", "Status"};

            // PRINT SEPARATOR
            print.print("+");
            for (String header : headers) {
                int length = header.length();
                if ( length % 2 == 0 )
                    espacos = tamanho;
                else
                    espacos = tam_impar;
                print.print("-".repeat(espacos));
                print.print("+");
            }
            print.println();

            // PRINT HEADERS
            print.print("|");
            for (String header : headers) {
                int length = header.length();
                if ( length % 2 == 0 )
                    espacos = tamanho - length;
                else
                    espacos = tam_impar - length;
                print.format("%"+espacos/2+"s", "");
                print.print(header);
                print.format("%"+espacos/2+"s", "");
                print.print("|");
            }
            print.println();

            // PRINT SEPARATOR
            print.print("+");
            for (String header : headers) {
                int length = header.length();
                if ( length % 2 == 0 )
                    espacos = tamanho;
                else
                    espacos = tam_impar;
                print.print("-".repeat(espacos));
                print.print("+");
            }
            print.println();

            // PRINT VALUES
            if (clients.size() != 0) {
                for (Map.Entry<String, PositionStatus> value : clients.entrySet()) {
                    print.print("|");
                    int length = value.getKey().length();
                        espacos = tamanho - length;
                    print.print(" ");
                    print.print(value.getKey());
                    print.format("%"+(espacos-1)+"s", "");
                    print.print("|");
                    for (String info : getClientInfoArray(value.getValue())) {
                        length = info.length();
                            espacos = tamanho - length;
                        print.print(" ");
                        print.print(info);
                        print.format("%"+(espacos-1)+"s", "");
                        print.print("|");
                    }
                    print.println();
                }
            }

            // PRINT SEPARATOR
            print.print("+");
            for (String header : headers) {
                int length = header.length();
                if ( length % 2 == 0 )
                    espacos = tamanho;
                else
                    espacos = tam_impar;
                print.print("-".repeat(espacos));
                print.print("+");
            }
            print.println();

            //
            // Estações
            //

            String[] headers2 = {"ID", "Position", "Capacity", "Available", "APE"};

            // PRINT SEPARATOR
            print.print("+");
            for (String header : headers2) {
                int length = header.length();
                if ( length % 2 == 0 )
                    espacos = tamanho;
                else
                    espacos = tam_impar;
                print.print("-".repeat(espacos));
                print.print("+");
            }
            print.println();

            // PRINT HEADERS
            print.print("|");
            for (String header : headers2) {
                int length = header.length();
                if ( length % 2 == 0 )
                    espacos = tamanho - length;
                else
                    espacos = tam_impar - length;
                print.format("%"+espacos/2+"s", "");
                print.print(header);
                print.format("%"+espacos/2+"s", "");
                print.print("|");
            }
            print.println();

            // PRINT SEPARATOR
            print.print("+");
            for (String header : headers2) {
                int length = header.length();
                if ( length % 2 == 0 )
                    espacos = tamanho;
                else
                    espacos = tam_impar;
                print.print("-".repeat(espacos));
                print.print("+");
            }
            print.println();

            // PRINT VALUES
            if (stations.size() != 0) {
                for (Map.Entry<String, InformStationStatus> value : stations.entrySet()) {
                    int i = 0;
                        print.print("|");
                        int length = value.getKey().length();
                        if ( headers2[i].length() % 2 == 0 )
                            espacos = tamanho - length;
                        else
                            espacos = tam_impar - length;
                        i++;
                        print.print(" ");
                        print.print(value.getKey());
                        print.format("%" + (espacos - 1) + "s", "");
                        print.print("|");
                        for (String info : getStationInfoArray(value.getValue())) {
                            length = info.length();
                            if ( headers2[i].length() % 2 == 0 )
                                espacos = tamanho - length;
                            else
                                espacos = tam_impar - length;
                            i++;
                            print.print(" ");
                            print.print(info);
                            print.format("%" + (espacos - 1) + "s", "");
                            print.print("|");
                        }
                        print.println();
                    }
            }

            // PRINT SEPARATOR
            print.print("+");
            for (String header : headers2) {
                int length = header.length();
                if ( length % 2 == 0 )
                    espacos = tamanho;
                else
                    espacos = tam_impar;
                print.print("-".repeat(espacos));
                print.print("+");
            }
            print.println();

        }
    }

    private ArrayList<String> getClientInfoArray(PositionStatus status) {
        ArrayList<String> result = new ArrayList<>();

        result.add(status.getStatus());
        result.add("(" + status.getAtual().getX() + "," + status.getAtual().getY() + ")");

        return result;
    }

    private ArrayList<String> getStationInfoArray(InformStationStatus status) {
        ArrayList<String> result = new ArrayList<>();

        result.add("(" + status.getPosition().getX() + "," + status.getPosition().getY() + ")");
        result.add(String.valueOf(status.getCapacity()));
        result.add(String.valueOf(status.getAvailable()));
        result.add(status.getRaio() + "m raio");


        return result;
    }

    public class Work extends JPanel{
        public void paintComponent(Graphics g){
            int x, y;
            super.paintComponent(g);
            for (int row = 0; row < tamanho_mapa; row++) {
                for (int col = 0; col < tamanho_mapa; col++) {
                    x = row * 10;
                    y = col * 10;
                    g.drawRect(x, y, 10, 10);
                }
            }
            for(InformStationStatus pa : stations.values()) {
                g.setColor(Color.BLUE);
                g.fillRect(pa.getPosition().getX() * 10, pa.getPosition().getY() * 10, 10, 10);
            }
            for(PositionStatus pc : clients.values()) {
                g.setColor(Color.ORANGE);
                g.fillRect(pc.getAtual().getX() * 10, pc.getAtual().getY() * 10, 10, 10);
            }
        }
    }

    public void Update() {
        String ROW_KEY = "Values";
        model.setValue(this.clients.size(), ROW_KEY, "Clients");
    }

    private void createSampleDataset() {
            int clients = this.clients.size();
            if (clients != 0) {
                for ( Map.Entry<String, InformStationStatus> station : this.stations.entrySet()) {
                    dataset.setValue(station.getKey(), (double)(station.getValue().getCapacity() - station.getValue().getAvailable())/clients*100);
                }
            }
    }

    public void TimeUpdate(int duracao) {
        String ROW_KEY = "Values";
        this.client_total++;
        this.duration_total = this.duration_total + duracao;
        if(duracao<tempo_minimo) tempo_minimo = duracao;
        if (duracao>tempo_maximo) tempo_maximo = duracao;
        tempo_medio = duration_total/client_total;
        m.setValue(tempo_minimo, ROW_KEY, "Minimo");
        m.setValue(tempo_medio, ROW_KEY, "Medio");
        m.setValue(tempo_maximo, ROW_KEY, "Maximo");
    }

    public class TimeCount extends TickerBehaviour {
        public TimeCount(Agent a, long period) {
            super(a, period);
        }

        @Override
        protected void onTick() {
            datasetAlugueres.addValue(clients.size(), "Alugueres ativos", String.valueOf(seconds));
            if(datasetAlugueres.getColumnCount() > 30){
                datasetAlugueres.removeColumn(0);
            }
            seconds++;
        }
    }
}

