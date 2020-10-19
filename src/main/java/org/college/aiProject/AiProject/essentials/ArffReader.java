package org.college.aiProject.AiProject.essentials;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

import java.io.File;
import java.io.IOException;

public class ArffReader {
    public static void main(String args[]) throws IOException {
        ArffLoader loader = new ArffLoader();
        loader.setFile(new File(args[0]));
        Instances instances= loader.getDataSet();
        System.out.println("Gilr = "+instances);
    }
}
