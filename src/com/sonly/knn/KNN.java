package com.sonly.knn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class KNN {
	
	/**
	 * calculate euclidean distance between two node
	 * @param node1
	 * @param node2
	 * @return double
	 */
	public static double distance(Node node1, Node node2) {
		double distance = 0.0;
		double[] factor1 = node1.getFeatures();
		double[] factor2 = node2.getFeatures();
		
		if(factor1.length == factor2.length) {
			for(int i = 0; i < factor1.length ; i++) 
				distance += (factor1[i]-factor2[i])*(factor1[i]-factor2[i]);
			
		}else {
			System.err.println("different length in features");
		}
		return distance;
	}
	
	/**
	 * type==1 list is train data
	 * type==0 list is test data
	 * preprocessing data set to node list
	 * @param list
	 * @return List<Node>
	 */
	public static List<Node> preDataSet(List<String[]> list, int type) {
		List<Node> nodes = new ArrayList<>();
		Node node = new Node();
		Iterator iterator = list.iterator();
		double[] features = null;
		while(iterator.hasNext()) {
			String[] strings = iterator.next().toString().split("\t");
			if(1 == type) {
				for(int i = 1; i < strings.length - 1; i++)
					features[i] = Double.parseDouble(strings[i]);
				node.setCategory(strings[strings.length - 1]);
			}else if(0 == type){
				for(int i = 1; i < strings.length; i++) 
					features[i] = Double.parseDouble(strings[i]);
				node.setCategory(null);
			}else 
				System.err.println("Wrong data set type\n1 is train data set\n0is test data set");
			node.setId(Integer.parseInt(strings[0]));
			node.setFeatures(features);	
			nodes.add(node);
		}
		
		return nodes;
	}
	
	
	public static void forecast(List<Node> train, Node test, String resultFile, int k) {
		Iterator iterator = train.iterator();
		Node trainNode = new Node();
		Map<Integer, Double> map = new HashMap<>();
		
		while (iterator.hasNext()) {
			trainNode = (Node)iterator.next();
			double dist = distance(trainNode, test);
			map.put(trainNode.getId(), dist);
		}
		
		
	}
	
	public static void KNN(String train, String test, String result, int k) throws IOException {
		List<String[]> trainData = FileOperator.readFile(train);
		List<String[]> testData = FileOperator.readFile(test);
		
		List<Node> trainSet = preDataSet(trainData, 1);
		List<Node> testSet = preDataSet(testData, 0);
		
		Iterator iterator = testSet.iterator();
		while(iterator.hasNext()) 
			forecast(trainSet, (Node)iterator.next(), result, k);
	}
	
	public static void main(String[] args) throws IOException {
		String train = "Files\\train_data_set.utf8";
		String test = "Files\\test_data_set.utf8";
		String result = "Files\\result.utf8";
		
		KNN(train,test,result,3);
	}

}
