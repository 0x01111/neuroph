package org.neuroph.nnet.learning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.neuroph.core.Connection;
import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.neuroph.core.transfer.Gaussian;
import org.neuroph.nnet.learning.kmeans.Cluster;
import org.neuroph.nnet.learning.kmeans.KMeansClustering;
import org.neuroph.nnet.learning.kmeans.KVector;
import org.neuroph.nnet.learning.knn.KNearestNeighbour;

/**
 * Learning rule for Radial Basis Function networks.
 * Use K-Means to determine centroids for hidden units, K-NearestNeighbour to set widths,
 * and LMS to tweak output neurons.
 * 
 * @author Zoran Sevarac sevarac@gmail.com
 */
public class RBFLearning extends LMS {

    /**
     * how many nearest neighbours to use when determining width (sigma) for
     * gaussian functions
     */
    int k=2;
    
    @Override
    protected void onStart() {
        super.onStart();         
        // 通过kmeans 设置输入层和rbf层之间的权值
        // set weights between input and rbf layer using kmeans
        KMeansClustering kmeans = new KMeansClustering(getTrainingSet());
        kmeans.setNumberOfClusters(neuralNetwork.getLayerAt(1).getNeuronsCount()); // 设置聚类数量set number of clusters as number of rbf neurons
        kmeans.doClustering(); // 开始聚类 
        
        // get clusters (centroids) 获取聚类中心 
        Cluster[] clusters = kmeans.getClusters();
        // 输出聚类中心 
//        for(Cluster cluster:clusters){
//			KVector centroid = cluster.getCentroid();
//			
//			System.out.println(centroid.toString());
//		}
        
        
        // assign each rbf neuron to one cluster 分配每个输入  到  对应的rbf的神经元
        // and use centroid vectors to initialize neuron's input weights
        Layer rbfLayer = neuralNetwork.getLayerAt(1);
        int i=0;
        for(Neuron neuron : rbfLayer.getNeurons()) {
            KVector centroid = clusters[i].getCentroid();
            double[] weightValues = centroid.getValues();
            int c=0;
            // 设置 输入层神经元的权值 getInputConnections，setValue
            // 对一个样本，下一个链接有 k 个神经元（k个聚类中心），所以一个样本向量的下一个链接就有 k 个向量
            for(Connection conn : neuron.getInputConnections()) {
                conn.getWeight().setValue(weightValues[c]);
                c++;
            }
            i++;
        }
        
        // get cluster centroids as list 聚类中心list 
        List<KVector> centroids = new ArrayList<>();                
        for(Cluster cluster : clusters) {
            centroids.add(cluster.getCentroid());
        }        

        // use KNN to calculate sigma param - gausssian function width for each neuron
        KNearestNeighbour knn = new KNearestNeighbour();
        knn.setDataSet(centroids);
        
        int n = 0;
        for(KVector centroid : centroids) {
        // calculate and set sigma for each neuron in rbf layer
        // 获取与距离中心最近的k个样本
            KVector[] nearestNeighbours = knn.getKNearestNeighbours(centroid, k);
        // 计算高斯函数，最近的k个样本，计算高斯函数的参数，
            double sigma = calculateSigma(centroid, nearestNeighbours); // calculate in method 
            Neuron neuron = rbfLayer.getNeuronAt(n);// 第 0 个神经元 
            ((Gaussian)neuron.getTransferFunction()).setSigma(sigma);
            i++;
            
        }
        
                
    }
    
    /**
     * 计算高斯函数
     * Calculates and returns  width of a gaussian function
     * @param centroid
     * @param nearestNeighbours
     * @return 
     */
    private double calculateSigma(KVector centroid,  KVector[] nearestNeighbours) {
       double sigma = 0;
              
       for(KVector nn : nearestNeighbours){
           sigma += Math.pow( centroid.distanceFrom(nn), 2 ) ;
       }
       
       sigma = Math.sqrt(1/((double)nearestNeighbours.length)  * sigma);
       
       return sigma;
    }


    
   
    
}
