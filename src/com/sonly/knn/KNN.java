package com.sonly.knn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale.Category;
import java.util.Map;
import java.util.Map.Entry;

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
			
			node.setFeatures(features);	
			nodes.add(node);
		}
		
		return nodes;
	}
	
	
	public static String forecast(List<Node> train, Node test, int k) {
		String category = new String();
		Iterator iterator = train.iterator();
		Node trainNode = new Node();
		Map<String, Double> map = new HashMap<>();
	
		while (iterator.hasNext()) {
			trainNode = (Node)iterator.next();
			double dist = distance(trainNode, test);
			map.put(trainNode.getCategory(), dist);
		}
		
		List<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(map.entrySet());  
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {  
            //Ωµ–Ú≈≈–Ú  
            @Override  
            public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {  
                return o2.getValue().compareTo(o1.getValue());  
            }  
        });
        
        Map<String, Integer> maxCategory = new HashMap<>();
        int count = 0;
        for(Map.Entry<String, Double> m : list) {
        	if(count == k)
        		break;
        	
        	if(maxCategory.containsKey(m.getKey()))
        		maxCategory.put(m.getKey(), maxCategory.get(m.getKey()) + 1);
        	else
        		maxCategory.put(m.getKey(), 1);
        	
        	count++;
        }
        
        List<Map.Entry<String, Integer>> list2 = new ArrayList<Map.Entry<String, Integer>>(maxCategory.entrySet());  
        Collections.sort(list2, new Comparator<Map.Entry<String, Integer>>() {  
            //Ωµ–Ú≈≈–Ú  
            @Override  
            public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {  
                return o2.getValue().compareTo(o1.getValue());  
            }  
        });
        
        return list2.get(0).getKey();
	}
	
	public static void KNN(String train, String test, String result, int k) throws IOException {
		List<String[]> trainData = FileOperator.readFile(train);
		List<String[]> testData = FileOperator.readFile(test);
		
		if(k <= trainData.size()) {
			List<Node> trainSet = preDataSet(trainData, 1);
			List<Node> testSet = preDataSet(testData, 0);
			List<String> writeToFile = new ArrayList<>();
			
			Iterator iterator = testSet.iterator();
			while(iterator.hasNext()) {
				Node node = (Node)iterator.next();
				String category = forecast(trainSet, node, k);
				node.setCategory(category);
			writeToFile.add(toString(node));
			}
			
			FileOperator.writeFile(writeToFile, result);
		}else {
			System.err.println("K is larger than train data size:"+trainData.size());
		}
	}
	
	public static String toString(Node node) {
		String string = new String();
		
		for(double d : node.getFeatures())
			string += String.valueOf(d)+"\t";
		
		string += node.getCategory();
		return string;
	}
	public static void main(String[] args) throws IOException {
		String train = "Files\\train_data_set.utf8";
		String test = "Files\\test_data_set.utf8";
		String result = "Files\\result.utf8";
		
		KNN(train,test,result,5);
	}

}
