package server.views;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

public class PieChartView {

    private ChartFrame frame;
    private JFreeChart chart;
    private DefaultPieDataset data;

    public PieChartView(){
        draw();
    }

    private void draw(){
        data = new DefaultPieDataset();

        loadData();

        chart = ChartFactory.createPieChart(
                "Votaciones",
                data,
                true,
                true,
                false);

        frame = new ChartFrame("Pie Chart", chart);
        frame.pack();
        frame.setVisible(true);
    }

    private void loadData() {
        /*for(Candidate c : Votations.getInstance().getCandidates()){
            data.setValue(c.getName(), c.getVotes());
        }*/
    }

    public void display() {
        data.clear();
        loadData();
        frame.repaint();
    }

}