package org.college.aiProject.AiProject.essentials.controller;

import org.college.aiProject.AiProject.essentials.DecisionTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/DM/DmProject")
public class PredictionController {
    private static final String FILE = "arff-data.arff";

    @Autowired
    DecisionTree decisionTree;
    @GetMapping("/decision")
    protected String decision(@RequestParam("pace") Double pace,@RequestParam("shot") Double shot,
                         @RequestParam("dribble") Double dribble,
                         @RequestParam("acceleration") Double acceleration,
                         @RequestParam("sprint-speed") Double sprint) {
        String workRate = decisionTree(pace,shot,dribble,acceleration,sprint);
        return workRate;
    }

    protected String decisionTree(double pace, double shot, double dribble, double acceleration, double sprintSpeed){
        try{
            /*DecisionTree decision = new DecisionTree();*/
            return decisionTree.predictWorkRate(FILE, pace, shot, dribble, acceleration, sprintSpeed);
        }
        catch(Exception e){
            return "";
        }
    }
}
