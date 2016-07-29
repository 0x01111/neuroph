/**
 * Copyright 2010 Neuroph Project http://neuroph.sourceforge.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.neuroph.core;

import java.io.Serializable;
import java.util.Objects;

/**
 *  一个链接，通过一个权值，链接两个神经元 
 *  Weighted connection to another neuron.
 * 
 * @see Weight
 * @see Neuron
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class Connection implements Serializable {

    /**
     * The class fingerprint that is set to indicate serialization
     * compatibility with a previous version of the class
     */
    private static final long serialVersionUID = 1L;

    /**
     * 链接的左神经元
     * From neuron for this connection (source neuron).
     * This connection is output connection for from neuron.
     */
    protected Neuron fromNeuron;

    /**
     * 链接的右神经元
     * To neuron for this connection (target, destination neuron)
     * This connection is input connection for to neuron.
     */
    protected Neuron toNeuron;
    
    /**
     * 链接权值
     * Weight for this connection
     */
    protected Weight weight;

    /**
     * 构造函数 
     * 创建两个神经元的链接
     * Creates a new connection between specified neurons with random weight
     *
     * @param fromNeuron neuron to connect from
     * @param  toNeuron neuron to connect to
     */
    public Connection(Neuron fromNeuron, Neuron toNeuron) {

        if (fromNeuron == null) {
            throw new IllegalArgumentException("From neuron in connection cant be null !");
        } else {
            this.fromNeuron = fromNeuron;
        }

        if (toNeuron == null) {
            throw new IllegalArgumentException("To neuron in connection cant be null!");
        } else {
            this.toNeuron = toNeuron;
        }

        this.weight = new Weight();
    }

    /**
     * 构造函数
     * 可以输入权值 
     * Creates a new connection to specified neuron with specified weight object
     *
     * @param fromNeuron neuron to connect from
     * @param toNeuron neuron to connect to
     * @param weight
     *            weight for this connection
     */
    public Connection(Neuron fromNeuron, Neuron toNeuron, Weight weight) {
        this(fromNeuron, toNeuron);
        
        if (weight == null) {
            throw new IllegalArgumentException("Connection Weight cant be null!");
        } else {
            this.weight = weight;
        }        
        
    }

    /**
     * 构造函数 
     * 左神经元--右神经元，链接权值
     * Creates a new connection to specified neuron with specified weight value
     *
     * @param fromNeuron neuron to connect from
     * @param  toNeuron neuron to connect to
     * @param weightVal
     *            weight value for this connection
     */
    public Connection(Neuron fromNeuron, Neuron toNeuron, double weightVal) {
        this(fromNeuron, toNeuron, new Weight(weightVal));
    }

    /**
     * 获取权值
     *
     * @return weight for this connection
     */
    public Weight getWeight() {
        return this.weight;
    }

    /**
     * 设置权值
     * @param weight The new weight of the connection.
     */
    public void setWeight(Weight weight) {
        if (weight == null) {
            throw new IllegalArgumentException("Connection Weight cant be null!");
        } else {
            this.weight = weight;
        }        
    }

    /**
     * 获取左神经元的值
     * @return input received through this connection
     */
    public double getInput() {
        return this.fromNeuron.getOutput();
    }

    /**
     * 左神经元的值 * 权值 
     * Returns the weighted input received through this connection
     *
     * @return weighted input received through this connection
     */
    public double getWeightedInput() {
        return this.fromNeuron.getOutput() * weight.value;
    }

    /**
     * 左神经元 
     * Gets from neuron for this connection
     * @return from neuron for this connection
     */
    public Neuron getFromNeuron() {
        return fromNeuron;
    }

    /**
     * 右神经元
     * Gets to neuron for this connection
     * @return neuron to set as to neuron
     */
    public Neuron getToNeuron() {
        return toNeuron;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Connection cloned = (Connection)super.clone(); 
        cloned.setWeight((Weight)weight.clone());
        cloned.toNeuron = (Neuron)toNeuron.clone();
        cloned.fromNeuron = (Neuron)fromNeuron.clone();
        
        return cloned;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.fromNeuron);
        hash = 67 * hash + Objects.hashCode(this.toNeuron);
        hash = 67 * hash + Objects.hashCode(this.weight);
        return hash;
    }
    /**
     * 判断是否是一个链接：左右神经元相等，权值相等
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Connection other = (Connection) obj;
        if (!Objects.equals(this.fromNeuron, other.fromNeuron)) {
            return false;
        }
        if (!Objects.equals(this.toNeuron, other.toNeuron)) {
            return false;
        }
        if (!Objects.equals(this.weight, other.weight)) {
            return false;
        }
        return true;
    }
    /**
     * to String 
     * "Connection{" + "fromNeuron=" + fromNeuron + ", toNeuron=" + toNeuron + ", weight=" + weight + '}';
     */
    @Override
    public String toString() {
        return "Connection{" + "fromNeuron=" + fromNeuron + ", toNeuron=" + toNeuron + ", weight=" + weight + '}';
    }
   
}
