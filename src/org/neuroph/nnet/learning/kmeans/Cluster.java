package org.neuroph.nnet.learning.kmeans;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class represents a single cluster, with corresponding centroid and assigned vectors
 * @author Zoran Sevarac
 */
public class Cluster {    
    
    /**
     * 聚类中心
     */
    KVector centroid;
    
    /**
     * 分配到该类中心的数据集Vectors assigned to this cluster during clustering
     */
    List<KVector> vectors;    

    
    public Cluster() {
        this.vectors = new ArrayList<>();
    }
   
    public KVector getCentroid() {
        return centroid;
    }

    public void setCentroid(KVector centroid) {
        this.centroid = centroid;
    }
    
    
    public void removePoint(KVector point) {
        vectors.remove(point);
    }

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
     * 给类中分配向量 Assignes vector to this cluster.
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
