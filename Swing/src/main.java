/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
/**
 *
 * @author ADITYA
 */
public class main {
    
    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
     void searchContinuityAboveValue(double[] data, int indexBegin, int indexEnd, double threshold, int winLength)
                {
         double[] value = null;
         int i,j = 0,k;
                for(i=indexBegin;i<=indexEnd;i++){
                    if(data[i]>threshold){
                       value[j]=data[i];
                       j++;
                       
                }
            } for(k=0;k<winLength;k++){
                System.out.println("Values that met this criteria are"+ value[k]);
            } }
     void backSearchContinuityWithinRange(double[] data, int indexBegin, int indexEnd, double thresholdlo, double thresholdhi, int winLength)
                {
         double[] value = null;
         int i,j = 0,k;
                for(i=indexBegin;i>=indexEnd;i--){
                    if(data[i]>thresholdlo && data[i]<thresholdhi){
                        value[j]=data[i];
                       j++;
                                        }
            } for(k=0;k<winLength;k++){
                System.out.println("Values that met this criteria are"+ value[k]);}
            } 
     void searchContinuityAboveValueTwoSignals(double[] data1,double[] data2, int indexBegin, int indexEnd, double threshold1,double threshold2, int winLength)
                {
         double[] value1 = null;
         double[] value2 = null;
         int i,j = 0,k;
                for(i=indexBegin;i<=indexEnd;i++){
                    if(data1[i]>threshold1 && data2[i]>threshold2){
                       value1[j]=data1[i];
                       value2[j]=data2[i];
                       j++;
                }
            } for(k=0;k<winLength;k++){
                System.out.println("Values that met this criteria are"+ value1[k]+ "        "+value2[k]);}
            } 
      void searchMultiContinuityWithinRange(double[] data, int indexBegin, int indexEnd, double thresholdlo, double thresholdhi, int winLength)
                {
        double[] value = null;
         int i,j = 0,k;
                for(i=indexBegin;i<=indexEnd;i++){
                    if(data[i]>thresholdlo && data[i]<thresholdhi){
                        value[j]=data[i];
                       j++;
                }
            } for(k=0;k<winLength;k++){
                System.out.println("Values that met this criteria are"+ value[k]);}
            } 
    public static void main(String[] args) throws FileNotFoundException, IOException {
       Scanner sc=new Scanner(System.in); 
       String[] file = null;
        String csvF = "C://Users/adity/Documents/NetBeansProjects/Swing/doc/Swing.csv";
        String line = "";
        String cvsSplitBy = ",";
        try (BufferedReader br = new BufferedReader(new FileReader(csvF))) {

            while ((line = br.readLine()) != null) {

                // use comma as separator
                file = line.split(cvsSplitBy);
                
                System.out.println(file[0] + "  " + file[1] + "  " + file[2] + "  " + file[3]+"  "+ file[4]+ "  " + file[5]+"  "+file[6]);}
        }catch (IOException e) {
            e.printStackTrace();}
        double[] filed = new double[file.length];
               System.out.println("1 for searchContinuityAboveValue");
               System.out.println("2 for backSearchContinuityWithinRange");
               System.out.println("3 for searchContinuityAboveValueTwoSignals"); 
               System.out.println("4 for searchMultiContinuityWithinRange"); 
                System.out.println("Enter your Choice"); 
                int ch=sc.nextInt();
               switch (ch) {
            case 1:  System.out.println("Enter your data choice"
                    + " 1=timestamp, 2=ax, 3=ay, 4=az, 5=wx, 6=wy, 7=wz");
            int i1=sc.nextInt();
                    System.out.println("Enter your index begin");
                    int ib1=sc.nextInt();
                    System.out.println("Enter your index end");
                    int ie1=sc.nextInt();
                    System.out.println("Enter your threshold");
                    double th1=sc.nextDouble();
                    System.out.println("Enter your win length");
                    int wl1=sc.nextInt();
                    searchContinuityAboveValue(filed[i1],ib1,ie1,th1,wl1);
                    break;
            case 2:  System.out.println("Enter your data choice"
                    + " 1=timestamp, 2=ax, 3=ay, 4=az, 5=wx, 6=wy, 7=wz");
            int i2=sc.nextInt();
                    System.out.println("Enter your index begin");
                    int ib2=sc.nextInt();
                    System.out.println("Enter your index end");
                    int ie2=sc.nextInt();
                    System.out.println("Enter your threshold low");
                    double thl1=sc.nextDouble();
                    System.out.println("Enter your threshold high");
                    double thh1=sc.nextDouble();
                    System.out.println("Enter your win length");
                    int wl2=sc.nextInt();
                    backSearchContinuityWithinRange(filed[i2],ib2,ie2,thl1,thh1,wl2);
                     break;
            case 3:  System.out.println("Enter your first data choice"
                    + " 1=timestamp, 2=ax, 3=ay, 4=az, 5=wx, 6=wy, 7=wz");
                    int i3=sc.nextInt();
                    System.out.println("Enter your second data choice"
                    + " 1=timestamp, 2=ax, 3=ay, 4=az, 5=wx, 6=wy, 7=wz");
                    int i4=sc.nextInt();
                    System.out.println("Enter your index begin");
                    int ib3=sc.nextInt();
                    System.out.println("Enter your index end");
                    int ie3=sc.nextInt();
                    System.out.println("Enter your threshold low");
                    double thl2=sc.nextDouble();
                    System.out.println("Enter your threshold high");
                    double thh2=sc.nextDouble();
                    System.out.println("Enter your win length");
                    int wl3=sc.nextInt();
                    searchContinuityAboveValueTwoSignals(filed[i3],filed[i4],ib3,ie3,thl2,thh2,wl3);
                     break;
            case 4:  System.out.println("Enter your data choice"
                    + " 1=timestamp, 2=ax, 3=ay, 4=az, 5=wx, 6=wy, 7=wz");
            int i5=sc.nextInt();
                    System.out.println("Enter your index begin");
                    int ib4=sc.nextInt();
                    System.out.println("Enter your index end");
                    int ie4=sc.nextInt();
                    System.out.println("Enter your threshold");
                    double thl3=sc.nextDouble();
                    System.out.println("Enter your threshold");
                    double thh3=sc.nextDouble();
                    System.out.println("Enter your win length");
                    int wl4=sc.nextInt();
                    searchMultiContinuityWithinRange(filed[i5],ib4,ie4,thl3,thh3,wl4);
                     break;
            default:   System.out.println("Entered the wrong choice");
                    break;
               }     
    }    
}
