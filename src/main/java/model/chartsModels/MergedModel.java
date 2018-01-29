package model.chartsModels;

public class MergedModel {
    private static MergedModel ourInstance = new MergedModel();

    public static MergedModel getInstance() {
        return ourInstance;
    }

    private MergedModel() {}


}
