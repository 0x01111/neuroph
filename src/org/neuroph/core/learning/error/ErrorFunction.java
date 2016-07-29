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

package org.neuroph.core.learning.error;

/**
 * 接口，计算总在学习网络错误。
 * 可以实现自定义错误类型。
 * Interface for calculating total network error during the learning.
 * Custom error types  can be implemented.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public interface ErrorFunction {

    /**
     * 返回总的学习网络误差
     * @return total network error
     */
    public double getTotalError();

    /**
     * 设置总的误差
     * Sets total error to zero
     */
    public void reset();

    /**
     * 计算模式误差：根据预测和目标输出进行计算
     * Calculates pattern error for given predicted and target output
     */
    public double[]  calculatePatternError(double[] predictedOutput, double[] targetOutput);

}
