package com.sonly.knn;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class KNN {
	
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
	
	
	public static void Test(String testFile) {
		
	}
	
	public static void KNN(String train, String test, String result, int k) {
		
	}
	
	public static void main(String[] args) throws IOException {
		String dataSet = "Files\\train_data_set.utf8";
		List<String[]> train_data = DataSet.readFile(dataSet);
		
		Iterator iterator = train_data.iterator();
		while (iterator.hasNext()) {
			String[] str = (String[]) iterator.next();
			for(String s:str)
				System.out.print(s+" ");
			
			System.out.println();
		}
		
	}

}
