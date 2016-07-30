package org.neuroph.nnet.learning.kmeans;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 这个类代表一个单的聚类中心 \t This class represents a single cluster, with corresponding centroid and assigned vectors
 * @author Zoran Sevarac
 */
public class Cluster {    
    
    /**
     * 聚类中心
     */
    KVector centroid;
    
    /**
     * 分配到该类中心的数据，list存储，每个list就是一条数据   Vectors assigned to this cluster during clustering
     */
    List<KVector> vectors;    

    
    public Cluster() {
        this.vectors = new ArrayList<>();
    }
    /**
     * 获取聚类中心
     * @return
     */
    public KVector getCentroid() {
        return centroid;
    }
    /**
     * 设置聚类中心
     * @param centroid
     */
    public void setCentroid(KVector centroid) {
        this.centroid = centroid;
    }
    
    /**
     * 删除这个样本数据
     * @param point
     */
    public void removePoint(KVector point) {
        vectors.remove(point);
    }
    /**
     * 获取该类的数据 
     * @return
     */
    public List<KVector> getPoints() {
        return vectors;
    }
    
    /**
     * 计算并返回类中心 Calculate and return avg sum vector for all vectors
     * @return avg sum vector 
     */
    public double[] getAvgSum() {
        double size = vectors.size();
        double[] avg = new double[vectors.get(0).size()];
        
        for(KVector item : vectors) {
            double[] values = item.getValues();
            
            for(int i=0; i<values.length; i++ ) {
                avg[i] += values[i]/size;
            }
            
        }
        
        return avg;
    }
    

    /**
     * 判断两个类中心是否相等 Returns true if two clusters have same centroid
     * @param obj
     * @return 
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final Cluster other = (Cluster) obj;
        double[] otherValues = other.getCentroid().getValues();
        double[] values = other.getCentroid().getValues();
        // do this ina for loop here
        for(int i=0; i<centroid.size(); i++) {
            if (otherValues[i] != values[i])
                return false;
        }
                       
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.centroid);
        hash = 97 * hash + Objects.hashCode(this.vectors);
        return hash;
    }

    
    
    
    /**
     * 该聚类中 添加一个样本数据      Assignes vector to this cluster.
     * @param vector vector to assign
     */
    public void assignVector(KVector vector) {
        // if vector's cluster is allready set to this, save some cpu cycles
        if (vector.getCluster() != this) { 
            vector.setCluster(this);
            vectors.add(vector);
        }
    }
    
    /**
     * 该类数据的大小 Returns number of vectors assigned to this cluster.
     * @return number of vectors assigned to this cluster
     */
    public int size() {
        return vectors.size();
    }
    
    
    
}
