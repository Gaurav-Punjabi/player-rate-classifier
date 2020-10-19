package org.college.aiProject.AiProject.essentials;

import org.springframework.stereotype.Service;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

@Service
public class DecisionTree implements ClassifierConstants{
    private J48 j48;
    private Evaluation evaluation;


    public DecisionTree(){
        j48 = new J48();
    }


    public void trainModel(Instances trainingData) throws Exception{
        /*Instances structure = trainingData.stringFreeStructure();
        //System.out.println("Structure=\n"+structure);
        evaluation = new Evaluation(trainingData);

        trainingData.setClassIndex(trainingData.numAttributes()-1);
        structure.setClassIndex(structure.numAttributes()-1);
        j48.buildClassifier(structure);

        Instance current;
        for (int j =0;j<trainingData.size();j++){
            current = trainingData.get(j);
            j48.updateClassifier(current);
        }*/
        evaluation = new Evaluation(trainingData);
        j48.buildClassifier(trainingData);
    }

    public String modalSummary(String fileName) throws Exception{
        Instances instances = new Instances(new BufferedReader(new FileReader(fileName)));
        return modalSummary(instances);
    }
    public String modalSummary(Instances testData) throws Exception{
        evaluation.evaluateModel(j48,testData);
        return evaluation.toSummaryString();
    }

    public double[] evaluateModel(Instances testData) throws Exception{
        return evaluation.evaluateModel(j48,testData);
    }


    public String predictWorkRate(String fileName,double pace,double shot, double dribble, double acceleration, double sprint )throws Exception{
        Instances instances = new Instances(new BufferedReader(new FileReader(fileName)));
        return predictWorkRate(instances,pace,shot,dribble,acceleration,sprint);
    }
    public String predictWorkRate(Instances trainingData,double pace,double shot, double dribble, double acceleration, double sprint )throws Exception{
        trainingData = prepareInstance(trainingData);
        trainModel(trainingData);
        double w = predictWorkRate(pace,shot,dribble,acceleration,sprint);
        if(w == 0.0){
            return MEDIUM;
        }
        if(w == 1.0){
            return HIGH;
        }
        return LOW;
    }

    public double predictWorkRate(double pace,double shot, double dribble, double acceleration, double sprint) throws Exception{
        Attribute paceAttr,shotAttr,dribbleAttr,accelerationAttr,sprintAttr,workAttr;
        paceAttr = new Attribute(ATTRIBUTE_PAC);
        shotAttr = new Attribute(ATTRIBUTE_SHO);
        dribbleAttr = new Attribute(ATTRIBUTE_DRI);
        accelerationAttr = new Attribute(ATTRIBUTE_ACCELERATION);
        sprintAttr = new Attribute(ATTRIBUTE_SPRINT_SPEED);

        ArrayList<String> valueList = new ArrayList<>();
        valueList.add(MEDIUM);
        valueList.add(HIGH);
        valueList.add(LOW);
        workAttr = new Attribute(ATTRIBUTE_WORK_RATE_ATT,valueList);

        ArrayList<Attribute> attributeList = new ArrayList<>();
        attributeList.add(paceAttr);
        attributeList.add(shotAttr);
        attributeList.add(dribbleAttr);
        attributeList.add(accelerationAttr);
        attributeList.add(sprintAttr);
        attributeList.add(workAttr);

        Instances dataset =  new Instances("whateevr",attributeList,0);

        double[] attributeValues = new double [6];
        attributeValues[0] = pace;
        attributeValues[1] = shot;
        attributeValues[2] = dribble;
        attributeValues[3] = acceleration;
        attributeValues[4] = sprint;

        Instance i1 = new DenseInstance(1.0,attributeValues);
        dataset.add(i1);

        dataset.setClassIndex(dataset.numAttributes() -1);

        return j48.classifyInstance(dataset.instance(0));
    }


    public static Instances prepareInstance(Instances instances) throws Exception{
        String selectionAttributes = getSelectAttributeString(instances);
        instances = selectAttributes(instances,selectionAttributes);
        instances.setClassIndex(instances.numAttributes()-1);
        return instances;
    }


    private static String getSelectAttributeString(Instances instances){
        int pacIndex = instances.attribute(ATTRIBUTE_PAC).index()+1;
        int shoIndex = instances.attribute(ATTRIBUTE_SHO).index()+1;
        int driIndex = instances.attribute(ATTRIBUTE_DRI).index()+1;
        int accelerationIndex = instances.attribute(ATTRIBUTE_ACCELERATION).index()+1;
        int sprintSpeedIndex = instances.attribute(ATTRIBUTE_SPRINT_SPEED).index()+1;
        int workRateAttIndex = instances.attribute(ATTRIBUTE_WORK_RATE_ATT).index()+1;
        return pacIndex+","+shoIndex+","+driIndex+","+accelerationIndex+","+sprintSpeedIndex+","+workRateAttIndex;
    }
    public static Instances selectAttributes(Instances instances,String attr) throws Exception{
        Remove remove = new Remove();
        remove.setAttributeIndices(attr);
        remove.setInvertSelection(true);
        remove.setInputFormat(instances);
        return Filter.useFilter(instances,remove);
    }

    public static void main(String args[]) throws Exception{
        //Instances instances = new Instances(new BufferedReader(new FileReader(args[0])));

        //Instances temp = prepareInstance(instances);
        //instances = prepareInstance(instances);
        DecisionTree demo= new DecisionTree();
        //demo.trainModel(instances);
        //String summary = demo.modalSummary(instances);
        Scanner scanner =new Scanner(System.in);
        System.out.println("Enter Pace,Shot,Dribble,Acceleration,Sprint,WorkRate in cm:");
        String  weight = demo.predictWorkRate(args[0],scanner.nextDouble(),scanner.nextDouble(),scanner.nextDouble(),
                scanner.nextDouble(),scanner.nextDouble());

        //System.out.println("Summary = "+summary);
        System.out.println("weight = "+weight);
    }
}
