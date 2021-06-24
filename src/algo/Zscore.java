package algo;

import test.AnomalyReport;
import test.StatLib;
import test.TimeSeries;
import test.TimeSeriesAnomalyDetector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//public class Zscore {
//	public Zscore() {
//		System.out.println("zscore");
//	}
//
//	
//	public String name = "shira";
//}



public class Zscore implements TimeSeriesAnomalyDetector {
	
	Map<String, Float> MaxZscore;
	
	public Zscore() {
		MaxZscore = new HashMap<>();
	}

	@Override
	public void learnNormal(TimeSeries ts) {
		
		for(String a: ts.getAttributes()) {  //for each col
			float max = 0;
			float[] data = ts.getAttributeArray(a);
			
			for(int i=2; i<ts.getRowSize(); i++) {  //for each row
				float[] x = new float[i];
				
				for (int j=0; j<i; j++) {  //sum the value up to the current value
					x[j] = data[j];
				}
				float zScore = 0;
				float dev = (float) Math.sqrt(StatLib.var(x));
				if (dev!=0) {
					zScore = (float) ((Math.abs(data[i] - StatLib.avg(x)) / (Math.sqrt(StatLib.var(x)))));
				}
				if (max < zScore) {
					max = zScore;
				}
			}
			MaxZscore.put(a, max);
		}
	}

	@Override
	public List<AnomalyReport> detect(TimeSeries ts) {
		List<AnomalyReport> reports = new ArrayList<>();
		
		for(String a: ts.getAttributes()) {
			
			float[] data = ts.getAttributeArray(a);
			
			for(int i=2; i<ts.getRowSize(); i++) {
				float[] x = new float[i];
				
				for (int j=0; j<i; j++) { 
					x[j] = data[j];
				}
				float zScore = 0;
				float dev = (float) Math.sqrt(StatLib.var(x));
				if (dev!=0) {
					zScore = (float)((Math.abs(data[i] - StatLib.avg(x))) / dev );
				}
				if ( MaxZscore.get(a) < zScore) {
					reports.add(new AnomalyReport(a,i+1));
				}
			}
		}
		return reports;
	}	
	
	
	public Map<String, Float> getNormalModel(){
		return MaxZscore;
	}

}
