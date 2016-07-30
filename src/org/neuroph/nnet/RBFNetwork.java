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

package org.neuroph.nnet;

import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.input.Difference;
import org.neuroph.core.transfer.Gaussian;
import org.neuroph.nnet.learning.LMS;
import org.neuroph.nnet.learning.RBFLearning;
import org.neuroph.util.*;

/**
 * RBF 神经网络 
 * Radial basis function neural network.
 * 
 * TODO: learning for rbf layer: k-means clustering
 * weights between input and rbf layer are Ci vector
 * each weight is a component of a Ci vector
 * Ci are centroids of the clusters trained by k means clustering
 * Each neuron in rbf layer corresponds to a single cluster
 * neuronns in rbf layer are clusters
 * 
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class RBFNetwork extends NeuralNetwork {
	
	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */		
	private static final long serialVersionUID = 1L;

	/**
	 * 构造函数 
	 * Creates new RBFNetwork with specified number of neurons in input, rbf and output layer
	 * 
	 * @param inputNeuronsCount
	 *		number of neurons in input layer
	 * @param rbfNeuronsCount
	 *		number of neurons in rbf layer
	 * @param outputNeuronsCount
	 *		number of neurons in output layer
	 */
	public RBFNetwork(int inputNeuronsCount, int rbfNeuronsCount, int outputNeuronsCount) {
		this.createNetwork(inputNeuronsCount, rbfNeuronsCount, outputNeuronsCount);
	}

	/**
	 * Creates RBFNetwork architecture with specified number of neurons in input
	 * layer, output layer and transfer function
	 * 
	 * @param inputNeuronsCount
	 *		number of neurons in input layer 输入层神经元个数
	 * @param rbfNeuronsCount
	 *		number of neurons in rbf layer rbf层神经元个数
	 * @param outputNeuronsCount
	 *		number of neurons in output layer 输出层神经元个数 
	 */
	private void createNetwork(int inputNeuronsCount, int rbfNeuronsCount,
			int outputNeuronsCount) {
		// init neuron settings for this network
		NeuronProperties rbfNeuronProperties = new NeuronProperties(); // 构建rbf网络的设置
		rbfNeuronProperties.setProperty("inputFunction", Difference.class);
		rbfNeuronProperties.setProperty("transferFunction", Gaussian.class);
		
		// 设置网络类型：rbf 
		// set network type code
		this.setNetworkType(NeuralNetworkType.RBF_NETWORK); 
		
		// 设置输入层：线性函数
		// create input layer
		Layer inputLayer = LayerFactory.createLayer(inputNeuronsCount, TransferFunctionType.LINEAR); 
		this.addLayer(inputLayer);
		
		// 设置 rbf层 ：rbf函数 
		// create rbf layer
		Layer rbfLayer = LayerFactory.createLayer(rbfNeuronsCount, rbfNeuronProperties); 
		this.addLayer(rbfLayer);
		
		// 设置输出层：线性函数
		// create output layer
		Layer outputLayer = LayerFactory.createLayer(outputNeuronsCount, TransferFunctionType.LINEAR); 
		this.addLayer(outputLayer);
		
		// 输入层 和 rbf层进行全链接
		// create full conectivity between input and rbf layer
		ConnectionFactory.fullConnect(inputLayer, rbfLayer); 
		// rbf 层和 输出层进行全链接 
		// create full conectivity between rbf and output layer
		ConnectionFactory.fullConnect(rbfLayer, outputLayer); 

		// set input and output cells for this network
		NeuralNetworkFactory.setDefaultIO(this);
		
		// 设置学习规则 
		// set appropriate learning rule for this network
		this.setLearningRule(new RBFLearning()); 
	}

}