package algo;

import test.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Hybrid implements TimeSeriesAnomalyDetector {

	private float high_corrlation, low_corrlation;
	private HashMap<String,String> attributs_corrlation;
	
	public Hybrid() {
		this.high_corrlation = 0.95f;
		this.low_corrlation = 0.5f;
		attributs_corrlation = new HashMap<>();
	}
	
	public Hybrid(float high, float low) {
		this.high_corrlation = high;
		this.low_corrlation = low;
		attributs_corrlation = new HashMap<>();
	}
	
	private class Circle {
		Point center;
		float radius;
		
		Circle(Point c, float r) {
			center = c;
			radius = r;
		}	
	}
	
	private float calcDist(Point a, Point b) {
	    return (float)Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
	}
	
	private boolean isInside(Circle c, Point p)
	{
	    return (calcDist(c.center, p) <= c.radius);
	}
	
	Point get_circle_center(float bx, float by,float cx, float cy) {
		float B = bx * bx + by * by;
		float C = cx * cx + cy * cy;
		float D = bx * cy - by * cx;
		return new Point((cy * B - by * C) / (2 * D),(bx * C - cx * B) / (2 * D));		
	}
	
	Circle circle_from(Point A, Point B, Point C) {
		Point I = get_circle_center(B.x - A.x, B.y - A.y,C.x - A.x, C.y - A.y);
		Point D = new Point(I.x + A.x,I.y + A.y);
		return new Circle(D, calcDist(D, A));
	}
	
	Circle circle_from(Point A, Point B)
	{
	    // Set the center to be the midpoint of A and B
	    Point C = new Point((A.x + B.x) / 2 , (A.y + B.y) / 2);
	  
	    // Set the radius to be half the distance AB
	    return new Circle (C, calcDist(A, B) / 2 );
	}
	
	boolean is_valid_circle(Circle c, Point[] P){
		// Iterating through all the points
		// to check  whether the points
		// lie inside the circle or not
		for (Point p : P)
		if (!isInside(c, p))
		   return false;
		return true;
	}
	
	Circle min_circle_trivial(Point[] P)
	{
		
	    if (P.length == 0) {
	        return new Circle(new Point(0,0), 0);
	    }
	    else if (P.length == 1) {
	        return new Circle(P[0], 0);
	    }
	    else if (P.length == 2) {
	        return circle_from(P[0], P[1]);
	    }
	  
	    // To check if MEC can be determined
	    // by 2 points only
	    else if (P.length == 3) {
	    	for (int i = 0; i < 3; i++) {
		        for (int j = i + 1; j < 3; j++) {
		            Circle c = circle_from(P[i], P[j]);
		            if (is_valid_circle(c, P))
		                return c;
		        }
		    }
		    return circle_from(P[0], P[1], P[2]);
	    }
	    return null;
	}
	
	Circle welzl_helper(Point[] P, Point[] R, int n) {
// Base case when all points processed or |R| = 3
		if (n == 0 || R.length == 3) {
			return min_circle_trivial(R);
		}

// Pick a random point randomly
		Random rand = new Random();
		int idx = rand.nextInt(n);
		Point p = P[idx];

// Put the picked point at the end of P
// since it's more efficient than
// deleting from the middle of the vector
// swap(P[idx], P[n - 1]);

// Get the MEC circle d from the
// set of points P - {p}
		Circle d = welzl_helper(P, R, n - 1);

// If d contains p, return d
		if (isInside(d, p)) {
			return d;
		}

// Otherwise, must be on the boundary of the MEC
		if (R.length == 0) {
			R = new Point[1];
		}
		else {
			Point[] temp = new Point[R.length+1];
			for(int i=0; i<R.length; i++) {
				temp[i] = R[i];
			}
			R = temp;
		}
		R[R.length-1] = p;

// Return the MEC for P - {p} and R U {p}
		return welzl_helper(P, R, n - 1);
	}
	
	 void shuffleArray(Point[] array) {
		  List<Point> list = new ArrayList<>();
		  for (Point i : array) {
		    list.add(i);
		  }

		  Collections.shuffle(list);

		  for (int i = 0; i < list.size(); i++) {
		    array[i] = list.get(i);
		  }    
		}

	Circle welzl(Point[] P)
	{
	    Point[] P_copy = P.clone();
	    shuffleArray(P_copy);
	    Point[] R = new Point[0];
	    return welzl_helper(P_copy, R, P_copy.length);
	}
	
	@Override
	public void learnNormal(TimeSeries ts) {
		Map<String,ArrayList<String>> cf = new HashMap<>();
		cf = ts.getCorrelations();
		
		for(String feature: cf.keySet() ) {
			float[] f1 = ts.getAttributeArray(feature);
			float[] f2 = ts.getAttributeArray(cf.get(feature).get(0));
			Point[] ps=new Point[f1.length];
			for(int i=0;i<ps.length;i++) {
				ps[i]=new Point(f1[i],f2[i]);
			}
			float current_correlation = Math.abs(Float.parseFloat(cf.get(feature).get(1)));
			
			if(current_correlation >= high_corrlation) {  //linear regression
				
				Line lin_reg = StatLib.linear_reg(ps);
				
				float max=0;
				for(int i=0;i<ps.length;i++){
					float d=Math.abs(ps[i].y - lin_reg.f(ps[i].x));
					if(d>max) {
						max=d; 
					}
				}
				attributs_corrlation.put(feature + "," + cf.get(feature).get(0), "L,"+lin_reg.a + "," + lin_reg.b + "," + max);
			}
			else if (current_correlation < low_corrlation) {  //Zscore
				float max = 0;
				for(int i=2; i<ts.getRowSize(); i++) {  //for each row
					float[] x = new float[i];
					
					for (int j=0; j<i; j++) {  //sum the value up to the current value
						x[j] = f1[j];
					}
					float zScore = 0;
					float dev = (float) Math.sqrt(StatLib.var(x));
					if (dev!=0) {
						zScore = (float) ((Math.abs(f1[i] - StatLib.avg(x)) / (Math.sqrt(StatLib.var(x)))));
					}
					if (max < zScore) {
						max = zScore;
					}
				
				}
				attributs_corrlation.put(feature,"Z,"+max);
			}
			else {
				Circle mec = welzl(ps);
				attributs_corrlation.put(feature + "," + cf.get(feature).get(0), "C,"+mec.center.x+","+mec.center.y+","+mec.radius );	
			}
		}
	}

	@Override
	public List<AnomalyReport> detect(TimeSeries ts) {
		
		ArrayList<AnomalyReport> r = new ArrayList<>();
		for (String s: attributs_corrlation.keySet()) {
			String f1;
			String f2 = null;
			float[] x;
			float[] y = null;
			f1 = s.split(",")[0];
			x=ts.getAttributeArray(f1);
			if (s.split(",").length == 2 ) {
				f2 = s.split(",")[1];
				y=ts.getAttributeArray(f2);
			}
			if (attributs_corrlation.get(s).startsWith("L")) {
				float a = Float.parseFloat(attributs_corrlation.get(s).split(",")[1]);
				float b = Float.parseFloat(attributs_corrlation.get(s).split(",")[2]);
				Line ab = new Line(a,b);
				float max = Float.parseFloat(attributs_corrlation.get(s).split(",")[3]);	
				for(int i=0;i<x.length;i++){
					if(Math.abs(y[i] - ab.f(x[i]))>max){
						String d=f1 + "-" + f2;
						r.add(new AnomalyReport(d,(i+1)));
					}
				}
			}
			else if (attributs_corrlation.get(s).startsWith("Z")) {
				float max = Float.parseFloat(attributs_corrlation.get(s).split(",")[1]);
				for(int i=2; i<x.length; i++) {
					float[] z = new float[i];
					
					for (int j=0; j<i; j++) { 
						z[j] = x[j];
					}
					float zScore = 0;
					float dev = (float) Math.sqrt(StatLib.var(z));
					if (dev!=0) {
						zScore = (float)((Math.abs(x[i] - StatLib.avg(z))) / dev );
					}
					if ( max < zScore) {
						r.add(new AnomalyReport(f1,i));
					}
				}
			}
			else if (attributs_corrlation.get(s).startsWith("C")){
				Point[] ps = new Point[x.length];
				for(int i=0;i<ps.length;i++) {
					ps[i]=new Point(x[i],y[i]);
				}
				float radius = Float.parseFloat(attributs_corrlation.get(s).split(",")[3]);
				Point center = new Point(Float.parseFloat(attributs_corrlation.get(s).split(",")[1]),
						Float.parseFloat(attributs_corrlation.get(s).split(",")[2]));
				Circle c = new Circle(center, radius);
				
				for(int i=0; i<ps.length; i++) {
					if (!isInside(c, ps[i])) {
						r.add(new AnomalyReport(f1,i));
					}
				}
			}
		}
		
		return r;
	}
	
	public HashMap<String,String> getNormalModel(){
		return attributs_corrlation;
	}
}
