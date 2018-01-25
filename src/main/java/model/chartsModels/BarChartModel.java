package model.chartsModels;

public class BarChartModel {
    private static BarChartModel ourInstance = new BarChartModel();

    public static BarChartModel getInstance() {
        return ourInstance;
    }

    private BarChartModel() {}


}
