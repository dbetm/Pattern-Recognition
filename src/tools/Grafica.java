package tools;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/* @author david */
public class Grafica {
    private JFreeChart grafica;
    private XYSeriesCollection series;
    private String titulo;
    private String tituloEjeX;
    private String tituloEjeY;

    public Grafica(String titulo, String tituloEjeX, String tituloEjeY) {
        this.titulo = titulo;
        this.tituloEjeX = tituloEjeX;
        this.tituloEjeY = tituloEjeY;
        this.grafica = null;
        this.series = new XYSeriesCollection();
    }
    
    public void agregarSerie(String id) {
        XYSeries serie = new XYSeries(id);
        this.series.addSeries(serie);
    }
    
    public void agregarPunto(String id, Punto punto) {
        XYSeries serie = this.series.getSeries(id);
        serie.add(punto.getX(), punto.getY());
    }
    
    public void crearGrafica(){
        this.grafica = ChartFactory.createXYLineChart(titulo, tituloEjeX, tituloEjeY, series);
        ChartFrame panel = new ChartFrame("Tiempos", grafica);
        panel.setVisible(true);
    }
    
    public void crearGraficaPuntos() {
        this.grafica = ChartFactory.createScatterPlot(titulo, tituloEjeX, tituloEjeY, series);
        ChartFrame pane1 = new ChartFrame("Panel", this.grafica);
        pane1.setVisible(true);
    }
}
