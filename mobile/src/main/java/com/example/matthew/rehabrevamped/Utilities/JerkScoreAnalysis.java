package com.example.matthew.rehabrevamped.Utilities;

import android.opengl.Matrix;
import android.util.Log;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.SimpsonIntegrator;
import org.apache.commons.math3.analysis.integration.TrapezoidIntegrator;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.filter.DefaultMeasurementModel;
import org.apache.commons.math3.filter.DefaultProcessModel;
import org.apache.commons.math3.filter.KalmanFilter;
import org.apache.commons.math3.filter.MeasurementModel;
import org.apache.commons.math3.filter.ProcessModel;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;

import java.security.Timestamp;
import java.sql.Time;
import java.util.ArrayList;

/**
 * Created by Matthew on 9/20/2016.
 */

public class JerkScoreAnalysis {
    float accelerationTotal=0;
    float Height=0;
    ArrayList<Float> jerkSave = new ArrayList<Float>();

    ArrayList<double[]> accerationList = new ArrayList<double[]>();
    ArrayList<Double> accTimeList= new ArrayList<Double>();
    ArrayList<Float[]> gyroList = new ArrayList<Float[]>();
    ArrayList<Long> gyroTimeList= new ArrayList<Long>();
    ArrayList<Float[]> magList = new ArrayList<Float[]>();
    ArrayList<Long> magTimeList= new ArrayList<Long>();

    float filteredAccelerationTotal=0;
   // ArrayList<Float> filteredJerkSave = new ArrayList<Float>();
   //int tally=0;

    public JerkScoreAnalysis(float height){
        Height=height;
    }

    /**
     * (called first)
     * called when ever new data is available. Adds that data to the lists above.
     * @param accX
     * @param accY
     * @param accZ
     * @param accTime
     * @param gyroX
     * @param gyroY
     * @param gyroZ
     * @param gyroTime
     * @param magX
     * @param magY
     * @param magZ
     * @param magTime
     */
    public void jerkAdd(float accX, float accY,float accZ, long accTime, float gyroX, float gyroY, float gyroZ, long gyroTime, float magX, float magY, float magZ, long magTime){
        double[] accerationSet = {(double)accX,(double)accY,(double)accZ};
            accerationList.add(accerationSet);
            accTimeList.add(accTime*.000000001);
            Float[] gyroSet = {gyroX, gyroY, gyroZ};
            gyroList.add(gyroSet);
            gyroTimeList.add(gyroTime);
            Float[] magSet = {magX, magY, magZ};
            magList.add(magSet);
            magTimeList.add(magTime);
            //used to calculate the
            accelerationTotal += (Math.pow(accX, 2) + Math.pow(accY, 2) + Math.pow(accZ, 2));
        //    tally=tally+1;
    }

    /**
     * (called when the action is completed correctly once)
     * When the action is completed once, it creates a thread that takes a copy of all the datapoints that have been recorded and runs them.
     * After that it calculates the jerk score with those numbers.
     * √(1/2)∫ ((d3y/dt3)^2 + (d3z/dt3)^2 +(d3x/dt3)^2) dt((Δt)^5)/A^2)
     * It also calculates the jerk score without the kalman filter(located outside of the thread) so that they can be compared.
     * through a kalman filter.
     * @param time
     */
    public void jerkCompute(float time){
        final float tempTime=time/1000;
        jerkSave.add(((float) Math.pow(((.5f*(accelerationTotal)*(Math.pow(tempTime,5)/ Math.pow(Height,2)))),.5f)));
        accelerationTotal=0;
        //Long story short this method has been modified to work without the kelman filter for now.
/*
        Log.i("Trap","Trap");
        double sample=0;
        int constant = 1;
        if((accerationList.size()-1)%2==1){
              accerationList.remove(0);
        }
        double timeValue=0;
        for(int i = 1; i<accerationList.size() ;i++) {
            double timeSpace = accTimeList.get(i) - accTimeList.get(i - 1);
            timeValue= timeValue+timeSpace;
            if (i == 1 || i == accerationList.size()-1){
                constant = 1;
            }
            else{
                constant = 2;
            }
            sample=sample + constant*timeSpace*Math.sqrt(Math.pow(accerationList.get(i)[0],2)+Math.pow(accerationList.get(i)[1],2)+Math.pow(accerationList.get(i)[2],2))*(Math.pow(timeSpace,5)/Math.pow(1,2));
            Log.i("Trap","sample: "+sample);
        }
        double i = Math.sqrt(.5*((timeValue/(tally-1))/2)*sample);
        Log.i("Trap","time: "+timeValue);
        Log.i("Trap",""+i);
        Log.i("Trap","Trap");
        tally=0;

        Log.i("jerk time",tempTime+"");
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<double[]> tempAccerationList = new ArrayList<double[]>();
                tempAccerationList.addAll(accerationList);
                accerationList.clear();
                ArrayList<Double> tempAccTimeList = new ArrayList<Double>();
                tempAccTimeList.addAll(accTimeList);
                accTimeList.clear();
                ArrayList<Float[]> tempGyroList= new ArrayList<Float[]>();
                tempGyroList.addAll(gyroList);
                gyroList.clear();
                ArrayList<Long> tempGyroTimeList = new ArrayList<Long>();
                tempGyroTimeList.addAll(gyroTimeList);
                gyroTimeList.clear();
                ArrayList<Float[]> tempMagList= new ArrayList<Float[]>();
                tempMagList.addAll(magList);
                magList.clear();
                ArrayList<Long> tempMagTimeList = new ArrayList<Long>();
                tempMagTimeList.addAll(magTimeList);
                magTimeList.clear();
                createKalmenFilter(tempAccerationList,tempAccTimeList,tempGyroList,tempGyroTimeList,tempMagList,tempMagTimeList);
                Log.i("summationFiltered",(filteredAccelerationTotal)+"");
                jerkSave.add(((float) Math.pow(((.5f*(filteredAccelerationTotal)*(Math.pow(tempTime,5)/ Math.pow(Height,2)))),.5f)));
                Log.i("jerkfiltered",((float) Math.pow(((.5f*(filteredAccelerationTotal)*(Math.pow(tempTime,5)/ Math.pow(Height,2)))),.5f))+"");
            }
        }).start();
        Log.i("summationStatic",(accelerationTotal)+"");
        jerkSave.add(((float) Math.pow(((.5f*(accelerationTotal)*(Math.pow(tempTime,5)/ Math.pow(Height,2)))),.5f)));
        Log.i("jerkstatic",((float) Math.pow(((.5f*(accelerationTotal)*(Math.pow(tempTime,5)/ Math.pow(Height,2)))),.5f))+"");
        Log.i("jerk","done");
      //  Log.i("MAD","MAD");
        double[] vector = new double[1];
        vector[0]=((sample));
       // Log.i("MAD","time: "+  tempTime);
        UnivariateFunction uf = (UnivariateFunction)new PolynomialFunction(vector);
       // SimpsonIntegrator SI = new SimpsonIntegrator();
      //  double s = Math.pow(.5*SI.integrate(1000,uf,0,tempTime),.5);
       // Log.i("MAD","simp: "+s);
        TrapezoidIntegrator TI = new TrapezoidIntegrator();
        double t = Math.pow(.5*TI.integrate(1000,uf,0,timeValue),.5);
        Log.i("MAD","trap: "+t);
      //  Log.i("MAD","MAD")
      */
    }

    /**
     * Creates and runs the kalman filter.
     * @param accerationList
     * @param accTimeList
     * @param gyroList
     * @param gyroTimeList
     * @param magList
     * @param magTimeList
     */
    public void createKalmenFilter(ArrayList<double[]> accerationList,ArrayList<Double> accTimeList,
                                   ArrayList<Float[]> gyroList,ArrayList<Long> gyroTimeList,
                                   ArrayList<Float[]> magList,ArrayList<Long> magTimeList) {
        ArrayList<double[][]> listOfStateMatrix = new ArrayList<double[][]>();
        double[] timeList = new double[accerationList.size()];
        //poputlate state matrix
        timeList[0]=0;

        double[][] initialStaticMatrix = {{accerationList.get(0)[0]},
                                          {0},
                                          {accerationList.get(0)[1]},
                                          {0},
                                          {accerationList.get(0)[2]},
                                          {0}};
        listOfStateMatrix.add(initialStaticMatrix);
        double total = 0;
        for (int i = 1; i < accerationList.size(); i++) {
            double time = (accTimeList.get(i) - accTimeList.get(i - 1));
            timeList[i] = (time);
            total=total+ timeList[i];
            Log.i("timeacc", total+"");
                if (time == 0) {
                    double[][] newStaticMatrix = {{accerationList.get(i)[0]},
                            {0},
                            {accerationList.get(i)[1]},
                            {0},
                            {accerationList.get(i)[2]},
                            {0}};
                    listOfStateMatrix.add(newStaticMatrix);
                } else {
                    double[][] newStaticMatrix = {{accerationList.get(i)[0]},
                            {(accerationList.get(i)[0] - listOfStateMatrix.get(i-1)[0][0])/ time},
                            {accerationList.get(i)[1]},
                            {(accerationList.get(i)[1] - listOfStateMatrix.get(i-1)[2][0]) / time},
                            {accerationList.get(i)[2]},
                            {(accerationList.get(i)[2] - listOfStateMatrix.get(i-1)[4][0]) / time}};
                    listOfStateMatrix.add(newStaticMatrix);
                }
        }
        for(int i = 0;i<listOfStateMatrix.size();i++){
            Log.i("StateMatricies","");
            Log.i("StateMatricies",new Array2DRowRealMatrix(listOfStateMatrix.get(i)).toString()+"");
        }
        // A = state transition matrix
        double[][] Alist = {
                {1,timeList[0],0,0,0,0},
                {0,1,0,0,0,0},
                {0,0,1,timeList[0],0,0},
                {0,0,0,1,0,0},
                {0,0,0,0,1,timeList[0]},
                {0,0,0,0,0,1}};
        RealMatrix A = new Array2DRowRealMatrix(Alist);

//  control input matrix
        double[][] Blist = {{(Math.pow(timeList[0],2))/2,0,0},
                            {timeList[0],0,0},
                            {0,(Math.pow(timeList[0],2))/2,0},
                            {0,timeList[0],0},
                            {(Math.pow(timeList[0],2))/2,0,0},
                            {timeList[0],0,0}};
        RealMatrix B =  new Array2DRowRealMatrix(Blist);
// H = measurement matrix
        double[][] Hlist = {{1,0,0,0,0,0},
                            {0,0,1,0,0,0},
                            {0,0,0,0,1,0}};
        RealMatrix H = new Array2DRowRealMatrix(Hlist);
// Q = process noise covariance matrix
        double[][] Qlist = {{.01,0,0,0,0,0},
                            {0,.01,0,0,0,0},
                            {0,0,.01,0,0,0},
                            {0,0,0,.01,0,0},
                            {0,0,0,0,.01,0},
                            {0,0,0,0,0,.01}};
        RealMatrix Q = new Array2DRowRealMatrix(Qlist);
        // R = measurement noise covariance matrix
        double[][] Rlist = {{.01,0,0},
                            {0,.01,0},
                            {0,0,.01}};
        //RealMatrix R = getCoveranceMatrix(accerationList,getAverage(listOfStateMatrix));
        RealMatrix R = new Array2DRowRealMatrix(Rlist);

        RealMatrix initialStateMatrix = new Array2DRowRealMatrix(listOfStateMatrix.get(0));
        RealVector initialStateVector = new ArrayRealVector(initialStateMatrix.getColumn(0));

        Log.i("Matrix A", A.toString());
        Log.i("Matrix B", B.toString());
        Log.i("Matrix H", H.toString());
        Log.i("Matrix Q", Q.toString());
        Log.i("Matrix R", R.toString());
        Log.i("Matrix state", initialStateVector.toString());
        ProcessModel pm = new DefaultProcessModel(A, B, Q, initialStateVector, null);
        MeasurementModel mm = new DefaultMeasurementModel(H, R);
        KalmanFilter filter = new KalmanFilter(pm, mm);

        for (int i = 0;i<listOfStateMatrix.size();i++) {
            // predict the state estimate one time-step aheadadb
            // optionally provide some control input
            double[] controlVector = {listOfStateMatrix.get(i)[1][0],listOfStateMatrix.get(i)[3][0],listOfStateMatrix.get(i)[5][0]};
            RealVector u = new ArrayRealVector(controlVector);
            Log.i("Matrix control",listOfStateMatrix.get(i)[1][0]+","+listOfStateMatrix.get(i)[3][0]+","+listOfStateMatrix.get(i)[5][0]);
            filter.predict(u);

            // obtain measurement vector z

            RealVector z = new ArrayRealVector(accerationList.get(i));
            Log.i("Matrix z",z.toString());

            // correct the state estimate with the latest measurement
            filter.correct(z);

            double[] stateEstimate = filter.getStateEstimation();
            // do something with it
            Log.i("KF",stateEstimate[0]+" "+stateEstimate[1]+" "+ stateEstimate[2]);
            Log.i("kelman filter:inputMea:",accerationList.get(i)[0]+" "+accerationList.get(i)[1]+" "+accerationList.get(i)[2]);
            Log.i("kelman filter:stateEst",stateEstimate[0]+" "+stateEstimate[2]+" "+ stateEstimate[4]);
            filteredAccelerationTotal+=(float)(Math.pow(stateEstimate[0],2)+ Math.pow(stateEstimate[2],2)+ Math.pow(stateEstimate[4],2));
        }
        Log.i("kelman filter","DONE");
    }
    public ArrayList<Float> getAllJerks(){
        return jerkSave;
    }
    public Float getJerkAverage(){
        Float sum =0f;
        for(Float f: jerkSave){
            sum+=f;
        }
        return sum/(float)jerkSave.size();
    }
    public Float getFilterJerkAverage(){
        Float sum =0f;
        for(Float f: jerkSave){
            sum+=f;
        }
        return sum/(float)jerkSave.size();
    }
}