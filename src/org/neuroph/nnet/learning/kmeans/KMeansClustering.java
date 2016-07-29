package org.neuroph.nnet.learning.kmeans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

/**
 * 
 *   1. Pick an initial set of K centroids (this can be random or any other means)
 *   2. For each data point, assign it to the member of the closest centroid according to the given distance function
 *   3. Adjust the centroid position as the mean of all its assigned member data points. Go back to (2) until the membership isn't change and centroid position is stable.
 *
 * @author Zoran Sevarac
 * @author Uros Stojkic
 */
public class KMeansClustering {

    /**
     * 数据集
     */
    private DataSet dataSet;
    // 存储数据集
    private KVector[] dataVectors;
    
    /**
     * 聚类数量
     */
    private int numberOfClusters;
    
    /**
     * 聚类中心 Clusters
     */
    private Cluster[] clusters;
        
    StringBuilder log=new StringBuilder();
    /**
     * 构造函数，dataSet数据存储到dataSet中，dataVector每个向量存储一个样本数据
     * @param dataSet
     */
    public KMeansClustering(DataSet dataSet) {
        this.dataSet = dataSet;
        this.dataVectors = new KVector[dataSet.size()];
        // iterate dataset and create dataVectors field
        this.dataVectors = new KVector[dataSet.size()];
         // iterate dataset and create dataVectors field
        int i=0;
        for(DataSetRow row : dataSet.getRows()) {
            KVector vector = new KVector(row.getInput());
            this.dataVectors[i]=vector;
            i++;
        }        
    }
    /**
     * 构造函数，增加了聚类数量
     * @param dataSet
     * @param numberOfClusters
     */
    public KMeansClustering(DataSet dataSet, int numberOfClusters) {
        this.dataSet = dataSet;
        this.numberOfClusters = numberOfClusters;
        this.dataVectors = new KVector[dataSet.size()];
         // iterate dataset and create dataVectors field
        int i=0;
        for(DataSetRow row : dataSet.getRows()) {
            KVector vector = new KVector(row.getInput());
            this.dataVectors[i]=vector;
            i++;
            
        }
    }
    /**
     * 初始化聚类中心
     * 随机初始化聚类中心
     */
    // http://en.wikipedia.org/wiki/K-means_clustering
    public void initClusters() {               
        
        ArrayList<Integer> idxList = new ArrayList<>();
        
        for(int i=0; i<dataSet.size(); i++) {
            idxList.add(i);
        }
        // 数据id下标随机化
        Collections.shuffle(idxList);
        
     //   log.append("Clusters initialized at:\n\n");
        // 取出前 numberOfCluster个数据当作聚类中心 
        clusters = new Cluster[numberOfClusters];
        for (int i = 0; i < numberOfClusters; i++) {
            clusters[i] = new Cluster();
            int randomIdx = idxList.get(i);
            KVector randomVector = dataVectors[randomIdx];
            clusters[i].setCentroid(randomVector);
            //log.append(randomVector.toString()+System.lineSeparator() );
        }
        //log.append(System.lineSeparator());

    }
    
    /**
     * 计算与当前vector最新的聚类中心
     * @param vector
     * @return 
     */
    private Cluster getNearestCluster(KVector vector) {
        Cluster nearestCluster = null;
        double minimumDistanceFromCluster = Double.MAX_VALUE;
        double distanceFromCluster = 0;

        for (Cluster cluster : clusters) {
            distanceFromCluster = vector.distanceFrom(cluster.getCentroid());
            if (distanceFromCluster < minimumDistanceFromCluster) {
                minimumDistanceFromCluster = distanceFromCluster;
                nearestCluster = cluster;
            }
        }

        return nearestCluster;
    }

    // 运行聚类
    public void doClustering() {
        // throw exception if number of clusters is 0 
        if (numberOfClusters <= 0) {
            throw new RuntimeException("Error: Number of clusters must be greater then zero!");
        }
        
        // 初始化类中心
        initClusters();
        
        // 初始化类别
        for (KVector vector : dataVectors) { // Iterate all dataSet    
            Cluster nearestCluster = getNearestCluster(vector);
            nearestCluster.assignVector(vector); // vector 数据，分配到最新的类别 
        }

        // this is the loop doing the main thing
        //  keep re-calculating centroids and assigning points until there is no change
        boolean clustersChanged; // 聚类类别没有改变的时候停止迭代 
        do {
            clustersChanged = false;            
            recalculateCentroids();// 更新聚类聚类中心                 

            for (KVector vector : dataVectors) {
                Cluster nearestCluster = getNearestCluster(vector);
                if (!vector.getCluster().equals(nearestCluster)) {
                    nearestCluster.assignVector(vector); // 更新数据所在的类中心
                    clustersChanged = true;
                }
            }
        } while(clustersChanged);
    }

    /**
     * 根据不同类的数据，计算新的类中心
     */
    private void recalculateCentroids() {
        // for each cluster do the following
        for (Cluster cluster : clusters) { // for each cluster
            if (cluster.size()>0) {         // that cointains data
                double[] avgSum = cluster.getAvgSum();  // calculate avg sum
                cluster.getCentroid().setValues(avgSum);  // and set new centroid to avg sum
            }                                  
        }
    }

    public DataSet getDataSet() {
        return dataSet;
    }
    /**
     * 设置数据集
     * @param vectors
     */
    public void setDataSet(DataSet vectors) {
        this.dataSet = vectors;
    }
    /**
     * 设置聚类的数量
     * @param numberOfClusters
     */
    public void setNumberOfClusters(int numberOfClusters) {
        this.numberOfClusters = numberOfClusters;
    }
    /**
     * 获取聚类中心
     * @return
     */
    public Cluster[] getClusters() {
        return clusters;
    }
    /**
     * 获取日志信息
     * @return
     */
    public String getLog() {
        return log.toString();
    }
}