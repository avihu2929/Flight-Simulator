package algo;

import test.*;

import java.util.ArrayList;
import java.util.List;

public class SimpleAnomalyDetector implements TimeSeriesAnomalyDetector {
	ArrayList<CorrelatedFeatures> cfList;
	float correlation_threshold;

	public SimpleAnomalyDetector() {
		cfList = new ArrayList<>();
		correlation_threshold = (float) 0.9;
	}
	
	@Override
	public void learnNormal(TimeSeries ts) {
		ArrayList<String> atts = ts.getAttributes();
		int size = ts.getRowSize();
		float[][] values = new float[atts.size()][size];
		
		for(int i=0; i<atts.size(); i++) {     //create values list
			for(int j=0; j<size; j++) {
				values[i][j] = ts.getAttributeData(atts.get(i)).get(j);
			}
		}
		
		for(int i=0; i<atts.size(); i++) {    //check correlation between every 2 atts
			for(int j=i+1; j<atts.size(); j++) {
				
				float p = StatLib.pearson(values[i],values[j]); //calc pearson
				if (Math.abs(p) > correlation_threshold) { //check if pearson greater than threshold
					Point points[] = toPoints(ts.getAttributeData(atts.get(i)),ts.getAttributeData(atts.get(j)));
					Line lin_reg = StatLib.linear_reg(points);
					float threshold = findThreshold(points,lin_reg)*1.1f; // 10% increase

					CorrelatedFeatures c = new CorrelatedFeatures(atts.get(i), atts.get(j), p, lin_reg, threshold);

					cfList.add(c);
				}
			}
		}
	}
		
		private Point[] toPoints(ArrayList<Float> x, ArrayList<Float> y) {
			Point[] ps=new Point[x.size()];
			for(int i=0;i<ps.length;i++)
				ps[i]=new Point(x.get(i),y.get(i));
			return ps;
		}
		
		private float findThreshold(Point ps[],Line rl){
			float max=0;
			for(int i=0;i<ps.length;i++){
				float d=Math.abs(ps[i].y - rl.f(ps[i].x));
				if(d>max)
					max=d;
			}
			return max;
		}
		
		public void setCorrelation_threshold(float correlation_threshold) {
			this.correlation_threshold = correlation_threshold;
		}
		
		public float getCorrelation_threshold() {
			return correlation_threshold;
		}
		

		@Override
		public List<AnomalyReport> detect(TimeSeries ts) {
			ArrayList<AnomalyReport> v=new ArrayList<>();
			
			for(CorrelatedFeatures c : cfList) {
				ArrayList<Float> x=ts.getAttributeData(c.feature1);
				ArrayList<Float> y=ts.getAttributeData(c.feature2);
				for(int i=0;i<x.size();i++){
					if(Math.abs(y.get(i) - c.lin_reg.f(x.get(i)))>c.threshold){
						String d=c.feature1 + "-" + c.feature2;
						v.add(new AnomalyReport(d,(i+1)));
					}
				}			
			}
			return v;
		}
		
		public List<CorrelatedFeatures> getNormalModel(){
			return cfList;
		}
	}

		
