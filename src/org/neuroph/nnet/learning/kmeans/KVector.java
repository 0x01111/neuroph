package org.neuroph.nnet.learning.kmeans;

/**
 * Represents feature vector used in k-means clustering algorithm
 * @author Zoran Sevarac
 * @author Uros Stojkic
  */
public class KVector {
    
    /**
     * 特性向量，表示一条样本数据  \t Values of feature vector
     */
    private double[] values;  
    
    /**
     * 这个向量所在的聚类  
     * Cluster to which this vector is assigned during clustering
     */
    private Cluster cluster;
    
    
    /**
     * 聚类
     * Distance fro other vector (used by K nearest neighbour)
     */
    private double distance;

     
    public KVector(int size) {
        this.values = new double[size];
    }
    
    public KVector(double[] values) {
        this.values = values;
    }
        
    
    public void setValueAt(int idx, double value) {
        this.values[idx] = value;
    }
    /**
     * 设置 聚类中心idx的值
     * @param idx
     * @return
     */
    public double getValueAt(int idx) {
        return values[idx];
    }
    /**
     * 获取聚类中心 
     * @return
     */
    public double[] getValues() {
        return values;
    }
    /**
     * 设置聚类中心
     * @param values
     */
    public void setValues(double[] values) {
        this.values = values;
    }
         
    
   public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        // remove this from old cluster and assign it to new
        if (this.cluster != null) {
            this.cluster.removePoint(this);
        }
        this.cluster = cluster;
    }
    
    /**
     * 计算欧氏距离  Calculates and returns intensity of this vector
     * @return intensity of this vector
     */
    public double getIntensity() {
        double intensity = 0;
        
        for(double value : values) {
            intensity += value*value;
        }
        
        intensity = Math.sqrt(intensity);
        
        return intensity;
    }
    
    /**
     * 计算到other 的欧氏距离  Calculates and returns euclidean distance of this vector from the given cluster
     * @param cluster
     * @return euclidean distance of this vector from given cluster
     */
    public double distanceFrom(KVector otherVector) {
        // get values and do this in loop
        double[] otherValues = otherVector.getValues();
        
        double distance = 0;
        
        for(int i=0; i<values.length; i++) {
            distance += Math.pow(otherValues[i]-values[i], 2);
        }
        
        distance = Math.sqrt(distance);
        
        return distance;        
    }      
    
    public int size() {
        return values.length;
    }
    /**
     * 字符串性的形式输出聚类中心 
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("KMeansVector{");
        for(int i=0; i<values.length; i++)
            sb.append("["+i+"]="+values[i]+",");
        
        sb.append('}');
        
        return  sb.toString();
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
    
    
    
}