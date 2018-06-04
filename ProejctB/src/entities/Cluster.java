package entities;

import java.util.ArrayList;

public class Cluster{

	private String hiddenRepeat;
	private String dnaCluster;
	private int start, end;

	public Cluster(int start, int end, String dnaCluster) {
		this.start = start;
		this.end = end;
		this.hiddenRepeat = findHiddenRepeat(dnaCluster);
		this.dnaCluster = dnaCluster;
	}

	public String getHiddenRepeat() {
		return hiddenRepeat;
	}
	public void setHiddenRepeat(String hiddenRepeat) {
		this.hiddenRepeat = hiddenRepeat;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}

	public String findHiddenRepeat(String seg) {
		int codonSize = 3;
		int count=1;
		String archecodon="";
		ArrayList<Integer> arr=new ArrayList<Integer>();// A  T C  G
		arr.add(0);
		arr.add(0);
		arr.add(0);
		arr.add(0);
		for(int j=0;j<codonSize;j++) {
			for(int i=count-1;i<seg.length();i+=codonSize) {
				if(seg.charAt(i)=='A'||seg.charAt(i)=='a')
					arr.set(0, arr.get(0)+1);
				if(seg.charAt(i)=='T'||seg.charAt(i)=='t')
					arr.set(1, arr.get(1)+1);
				if(seg.charAt(i)=='C'||seg.charAt(i)=='c')
					arr.set(2, arr.get(2)+1);
				if(seg.charAt(i)=='G'||seg.charAt(i)=='g')
					arr.set(3, arr.get(3)+1);
			}
			int index=getMax(arr);
			if(index==0)
				archecodon+="A";
			else if(index==1)
				archecodon+="T";
			else if(index==2)
				archecodon+="C";
			else archecodon+="G";
			count++;
			arr.clear();
			arr.add(0);
			arr.add(0);
			arr.add(0);
			arr.add(0);
		}
		return archecodon;
	}
	private int getMax(ArrayList<Integer> list){
		int max =-1,index = 0;
		for(int i=0; i<list.size(); i++){
			if(list.get(i) > max){
				max = list.get(i);
				index=i;
			}
		}
		return index;
	}

	public String getDnaCluster() {
		return dnaCluster;
	}

	public void setDnaCluster(String dnaCluster) {
		this.dnaCluster = dnaCluster;
	}


}
