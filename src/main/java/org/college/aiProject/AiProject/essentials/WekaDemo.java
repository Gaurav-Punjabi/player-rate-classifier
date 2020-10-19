package org.college.aiProject.AiProject.essentials;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.core.*;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

import java.io.BufferedReader;
import java.io.FileReader;

public class WekaDemo {
    private Instances mInstances;

    public WekaDemo(){}


    public void setInstances(String file) throws Exception{
        mInstances = new Instances(new BufferedReader(new FileReader(file)));
    }

    public Instances getInstances(){
        return mInstances;
    }

    public Instances selectAttributes(String attr) throws Exception{
        Remove remove = new Remove();
        remove.setAttributeIndices(attr);
        remove.setInvertSelection(true);
        remove.setInputFormat(mInstances);
        return Filter.useFilter(mInstances,remove);
    }

    public Instances prepareTargetData(int a,int h,int w){
        Attribute age = new Attribute("age");
        Attribute height = new Attribute("height_cm");
        Attribute weight = new Attribute("weight_cm");
        FastVector fvclassVal = new FastVector(3);
        fvclassVal.addElement("Lean");
        fvclassVal.addElement("Normal");
        fvclassVal.addElement("Stocky");

        Attribute classAttribute = new Attribute("body_type",fvclassVal);


        FastVector fvWekaAttributes = new FastVector(4);
        fvWekaAttributes.addElement(age);
        fvWekaAttributes.addElement(height);
        fvWekaAttributes.addElement(weight);
        fvWekaAttributes.addElement(classAttribute);


        Instances dataset = new Instances("whatever",fvWekaAttributes,0);

        double[] attValues = new double[dataset.numAttributes()];
        attValues[0] = a;
        attValues[1] = h;
        attValues[2] = w;

        Instance i1 = new DenseInstance(1.0, attValues);
        dataset.add(i1);
        dataset.setClassIndex(dataset.numAttributes()-1);

        return dataset;
    }
    public static void main(String args[]) throws Exception{

        WekaDemo demo = new WekaDemo();
        demo.setInstances(/*args[0]*/"arff-data.arff");
        Instances i = demo.getInstances();
        //System.out.println("Instance ="+i);
        //i.setClassIndex(12);
        i = demo.selectAttributes("22,23,25,44,45,31");
        //System.out.println("Instances attribute stats   =\n"+i);

        Instances structure = i.stringFreeStructure();
        System.out.println("Structure=\n"+structure);

        i.setClassIndex(i.numAttributes()-1);
        structure.setClassIndex(structure.numAttributes()-1);
        NaiveBayesUpdateable nb = new NaiveBayesUpdateable();
        nb.buildClassifier(structure);

        Instance current;
        for (int j =0;j<i.size();j++){
            current = i.get(j);
            nb.updateClassifier(current);
        }
        System.out.println(nb);



        Evaluation evaluation = new Evaluation(i);
        /*returns an array of size number of instances with result*/
        double[] arr = evaluation.evaluateModel(nb,i);
        System.out.println("Summary = ");
        System.out.println(evaluation.toSummaryString());
        /*System.out.println("Length  = "+arr.length+" evaluateModel  = ");
        System.out.println(Arrays.toString(arr));*/
        //System.out.println("Lean = 0 Normal = 1 Stocky = 2");
        int att[] = new int[9];
        int correct = 0;
        int incorrect = 0;
        int predictedAtt[] = new int[9];
        for(int j = 0;j<i.size();j++){
            //for(int j = 0;j<10;j++){
            double x = nb.classifyInstance(i.instance(j));
            //System.out.println(x);
            /*if(x == 1.0){
                System.out.println(j+" Lean");
            }
            else if (x == 2.0){
                System.out.println(j+" Normal");
            }
            else if(x == 3.0){
                System.out.println(j+" Stocky");
            }*/
            //System.out.println("Class value = "+i.instance(0).classValue());
            int temp = (int)i.instance(j).classValue();
            if(temp == x){
                correct++;
            }
            else{
                incorrect++;
            }
            att[temp] ++;
            predictedAtt[(int)x]++;
        }

        System.out.println("Correct = "+correct+" incorrect = "+incorrect);
        System.out.println("Class     \tLean\tNormal\tStocky\tC. Ronaldo\tMessi\tNeymar\tCourtois\tShaqiri\tAkinfenwa");
        System.out.println("Actual    \t"+att[0]+"\t"+att[1]+"\t"+att[2]+"\t"+att[3]+"\t"+att[4]+"\t"+att[5]+"\t"+att[6]+"\t"+att[7]+"\t"+att[8]);
        System.out.println("Predicted \t"+predictedAtt[0]+"\t"+predictedAtt[1]+"\t"+predictedAtt[2]+"\t"+predictedAtt[3]+"\t"+predictedAtt[4]+"\t"+predictedAtt[5]+"\t"+predictedAtt[6]+"\t"+predictedAtt[7]+"\t"+predictedAtt[8]);

        i = demo.prepareTargetData(28,191,150);
        double a = nb.classifyInstance(i.instance(0));

        System.out.println("CLassified = "+a);
    }
}
