package org.college.aiProject.AiProject.essentials;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * Servlet implementation class DmProject
 */
@WebServlet("/DmProject")
public class DmProject extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String FILE = "C:\\Users\\nithy\\workspace\\DM\\src\\arff-data.arff";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DmProject() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**v
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        String methodName = request.getParameter("method");

        PrintWriter out = response.getWriter();
        if(methodName.equals("linear")){
            double weight = linearRegression(Double.parseDouble(request.getParameter("height")));
            //echo weight
            out.println(weight);
        }
        else if(methodName.equals("naive")){
            double  pace = Double.parseDouble(request.getParameter("pace"));
            double  shot = Double.parseDouble(request.getParameter("shot"));
            double  dribble = Double.parseDouble(request.getParameter("dribble"));
            double  acceleration = Double.parseDouble(request.getParameter("acceleration"));
            double  sprint = Double.parseDouble(request.getParameter("sprint-speed"));

            String workRate = naiveBayes(pace,shot,dribble,acceleration,sprint);
            //echo workRate
            out.println(workRate);
        }
        else if(methodName.equals("decision")){
            double  pace = Double.parseDouble(request.getParameter("pace"));
            double  shot = Double.parseDouble(request.getParameter("shot"));
            double  dribble = Double.parseDouble(request.getParameter("dribble"));
            double  acceleration = Double.parseDouble(request.getParameter("acceleration"));
            double  sprint = Double.parseDouble(request.getParameter("sprint-speed"));

            String workRate = decisionTree(pace,shot,dribble,acceleration,sprint);
            //echo workRate
            out.println(workRate);
        }
    }
    protected double linearRegression(double height) {
        try{
            RegressionDemo.init(FILE);
            RegressionDemo regression= new RegressionDemo();
            return regression.predictWeight(FILE,height);
        }
        catch(Exception e){
            return 0.0;
        }
    }
    protected String naiveBayes(double pace, double shot, double dribble, double acceleration, double sprintSpeed){
        try{
            NaiveBayesDemo naive = new NaiveBayesDemo();
            return naive.predictWorkRate(FILE, pace, shot, dribble, acceleration, sprintSpeed);
        }
        catch(Exception e){
            return "";
        }
    }
    protected String decisionTree(double pace, double shot, double dribble, double acceleration, double sprintSpeed){
        try{
            DecisionTree decision = new DecisionTree();
            return decision.predictWorkRate(FILE, pace, shot, dribble, acceleration, sprintSpeed);
        }
        catch(Exception e){
            return "";
        }
    }

}
