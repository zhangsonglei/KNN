package com.sonly.knn;

import java.util.List;

/**
 * 
 * @author zsl
 *
 */
public class Node {
	private double[] features;
	private String category;
	
	
	public double[] getFeatures() {
		return features;
	}
	public void setFeatures(double[] features) {
		this.features = features;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
}