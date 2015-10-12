package server.views;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;

public class BarChartView {

    private ChartFrame frame;
    private JFreeChart chart;
    private DefaultCategoryDataset data;

    public BarChartView(){
        draw();
    }

    private void draw() {
        data = new DefaultCategoryDataset();
        loadData();

        chart = ChartFactory.createBarChart3D
                ("Votaciones", "Candidato", "Votos",
                        data, PlotOrientation.VERTICAL, true, true, false);
        chart.getTitle().setPaint(Color.black);
        CategoryPlot p = chart.getCategoryPlot();
        p.setRangeGridlinePaint(Color.red);

        frame = new ChartFrame("Bar chart", chart);
        frame.pack();
        frame.setVisible(true);
    }

    private void loadData() {
        /*for(Candidate c : Votations.getInstance().getCandidates()){
            data.setValue(c.getVotes(), c.getParty(), c.getName());
        }*/
    }

    public void display() {
        data.clear();
        loadData();
        frame.repaint();
    }


    public void update(Event event) {
        display();
    }
}