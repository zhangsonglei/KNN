package com.sonly.knn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
		Iterator<String[]> iterator = list.iterator();
		
		while(iterator.hasNext()) {
			String[] strings = iterator.next();
			Node node = new Node();
			if(1 == type) {
				double[] features = new double[strings.length - 2];
				for(int i = 1; i < strings.length - 1; i++)
					features[i - 1] = Double.parseDouble(strings[i]);
				node.setId(Integer.parseInt(strings[0]));
				node.setCategory(strings[strings.length - 1]);
				node.setFeatures(features);	
			}else if(0 == type){
				double[] features = new double[strings.length];
				for(int i = 0; i < strings.length; i++) 
					features[i] = Double.parseDouble(strings[i]);
				
				node.setCategory(null);
				node.setFeatures(features);	
			}else 
				System.err.println("Wrong data set type\n1 is train data set\n0is test data set");
			
			nodes.add(node);
		}
		
		return nodes;
	}
	
	
	public static String forecast(List<Node> train, Node test, int k) {
		Iterator<Node> iterator = train.iterator();
		Node trainNode = new Node();
		Map<Integer, Double> id_dist = new HashMap<>();
		Map<Integer, String> id_category = new HashMap<>();
	
		while (iterator.hasNext()) {
			trainNode = (Node)iterator.next();
			double dist = distance(trainNode, test);
			id_dist.put(trainNode.getId(), dist);
			id_category.put(trainNode.getId(), trainNode.getCategory());
		}
		
		
		
		List<Map.Entry<Integer, Double>> list = new ArrayList<Map.Entry<Integer, Double>>(id_dist.entrySet());  
        Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {  
            //…˝–Ú≈≈–Ú  
            @Override  
            public int compare(Entry<Integer, Double> o1, Entry<Integer, Double> o2) {
            	if(o2.getValue() > o1.getValue())
            		return -1;
            	else if(o2.getValue() < o1.getValue())
            		return 1;
            	else 
            		return 0;  
            }  
        });
        
        Map<String, Integer> maxCategory = new HashMap<>();
        int count = 0;
        for(Map.Entry<Integer, Double> m : list) {
        	if(count == k)
        		break;
  
        	String temp = id_category.get(m.getKey());
        	
        	if(maxCategory.containsKey(temp))
        		maxCategory.put(temp, maxCategory.get(temp) + 1);
        	else
        		maxCategory.put(temp, 1);
        	
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
        
        for(int ks=0;ks<list2.size();ks++) {
        	Entry<String, Integer> coutMap = list2.get(ks);
        	System.out.println(coutMap.getKey()+" "+coutMap.getValue());
        }
        	
        return list2.get(0).getKey();
	}
	
	public static void kNN(String train, String test, String result, int k) throws IOException {
		List<String[]> trainData = FileOperator.readFile(train);
		List<String[]> testData = FileOperator.readFile(test);
		
		if(k <= trainData.size()) {
			List<Node> trainSet = preDataSet(trainData, 1);
			List<Node> testSet = preDataSet(testData, 0);
			List<String> writeToFile = new ArrayList<>();
			
			Iterator<Node> iterator = testSet.iterator();
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
		
		kNN(train,test,result,5);
	}

}
