package org.college.aiProject.AiProject.essentials;

import weka.classifiers.Evaluation;
import weka.core.*;
import weka.classifiers.functions.LinearRegression;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class RegressionDemo {

    private static final String ATTRIBUTE_HEIGHT= "height_cm";
    private static final String ATTRIBUTE_WEIGHT = "weight_kg";

    private LinearRegression regressionModel;
    private Evaluation evaluation;



    public RegressionDemo() throws Exception{
        regressionModel = new LinearRegression();
    }

    public final static void init(String fileName) throws Exception{
        Instances instances = new Instances(new BufferedReader(new FileReader(fileName)));

        instances = selectAttributes(instances,"10,11");
        Instances temp = prepareInstance(instances);

        temp.setClassIndex(temp.numAttributes()-1);
        RegressionDemo demo = new RegressionDemo();
    }


    /*trains the classifier belonging to this instance and the class attribute is set*/
    public void trainModel(Instances trainingData) throws Exception{
        evaluation = new Evaluation(trainingData);
        regressionModel.buildClassifier(trainingData);
    }

    public String modalSummary(String fileName) throws Exception{
        Instances instances = new Instances(new BufferedReader(new FileReader(fileName)));
        return modalSummary(instances);
    }
    public String modalSummary(Instances testData)throws Exception{
        evaluation.evaluateModel(regressionModel,testData);
        return evaluation.toSummaryString();
    }

    public double[] evaluateModel(Instances testData) throws Exception{
        return evaluation.evaluateModel(regressionModel,testData);
    }

    public double predictWeight(String fileName,double h)throws Exception{
        Instances instances = new Instances(new BufferedReader(new FileReader(fileName)));
        return predictWeight(instances,h);
    }
    public double predictWeight(Instances trainingData, double h)throws Exception{
        trainingData = prepareInstance(trainingData);
        trainModel(trainingData);
        return predictWeight(h);
    }

    public double predictWeight(double h) throws Exception{
        Attribute height = new Attribute(ATTRIBUTE_HEIGHT);
        Attribute weight = new Attribute(ATTRIBUTE_WEIGHT);
        ArrayList<Attribute> attributeList = new ArrayList<>(2);
        attributeList.add(height);
        attributeList.add(weight);

        Instances dataset = new Instances("default",attributeList,0);

        double[] attributeValues = new double[2];
        attributeValues[0] = h;
        Instance i1 =  new DenseInstance(1.0,attributeValues);
        dataset.add(i1);

        dataset.setClassIndex(dataset.numAttributes()-1);

        return regressionModel.classifyInstance(dataset.instance(0));
    }

    private static String getSelectAttributeString(Instances instances){
        int heightIndex = instances.attribute(ATTRIBUTE_HEIGHT).index()+1;
        int weightIndex = instances.attribute(ATTRIBUTE_WEIGHT).index()+1;
        return heightIndex+","+weightIndex;
    }

    public static Instances prepareInstance(Instances instances) throws Exception{
        Remove remove = new Remove();
        String attr = getSelectAttributeString(instances);
        remove.setAttributeIndices(attr);
        remove.setInvertSelection(true);
        remove.setInputFormat(instances);
        instances = Filter.useFilter(instances,remove);
        instances.setClassIndex(instances.numAttributes()-1);
        return instances;
    }

    public static Instances selectAttributes(Instances instances,String attr) throws Exception{
        Remove remove = new Remove();
        remove.setAttributeIndices(attr);
        remove.setInvertSelection(true);
        remove.setInputFormat(instances);
        instances = Filter.useFilter(instances,remove);
        instances.setClassIndex(instances.numAttributes()-1);
        return instances;
    }
    public static void main(String args[]) throws Exception{
        /*Instances instances = new Instances(new BufferedReader(new FileReader(args[0])));

        instances = selectAttributes(instances,"10,11");
        Instances temp = prepareInstance(instances);

        temp.setClassIndex(temp.numAttributes()-1);
        RegressionDemo demo = new RegressionDemo();*/

        RegressionDemo.init(args[0]);
        RegressionDemo demo = new RegressionDemo();
        /*demo.trainModel(instances);
        String summary = demo.modelSummary(instances);*/
        Scanner scanner =new Scanner(System.in);
        System.out.println("Enter height in cm:");
        //double weight = demo.predictWeight(instances,scanner.nextDouble());
        double weight = demo.predictWeight(args[0],scanner.nextDouble());

        /*System.out.println("Summary = "+summary);*/
        System.out.println("weight = "+weight);


    }
}
