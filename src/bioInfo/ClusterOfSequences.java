package bioInfo;
import java.util.ArrayList;
import java.util.*;

public class ClusterOfSequences {
	private ArrayList<ClusterOfSequences> subClusters = new ArrayList<ClusterOfSequences>(); //avoid null pointer exceptions
	private ArrayList<Sequence> elements = new ArrayList<Sequence>();
	
	public ClusterOfSequences(Sequence element) {
		elements.add(element); //a simple cluster is composed of a single instance of Sequence
	}
	
	public ClusterOfSequences(ArrayList<Sequence> eltList) {
		elements.addAll(eltList); //we add all the sequences
		for(Sequence element : eltList) { //we create the sub-clusters
			ClusterOfSequences cluster = new ClusterOfSequences(element);
			subClusters.add(cluster);
		}
		
	}
	
	public ClusterOfSequences(ClusterOfSequences cluster1, ClusterOfSequences cluster2) {
		//we merge the two clusters
		elements.addAll(cluster1.elements);
		elements.addAll(cluster2.elements);
		subClusters.add(cluster1);
		subClusters.add(cluster2);
	}
	
	
	private String getNewickIntermediate() {
		String res = "";
		if(elements.size()==1) res += elements.get(0);
		else {
			int index = 0;
			res += "(";
			while(subClusters.size() > index) {
				if(subClusters.get(index).elements.size() >= 1) {
					res += subClusters.get(index).getNewickIntermediate();
				}
				if(subClusters.size()-1 > index) res += ",";
				index++;
			}
			res += ")";
		}
		return res;
	}
	
	
	public String getNewick() {
		return this.getNewickIntermediate() + ";";
	}
	
	
	private double average(ArrayList<Double> list) {
		OptionalDouble average = list.stream().mapToDouble(a -> a)
	            .average();
		return average.isPresent() ? average.getAsDouble() : 0; 
	}
	
	//the average of the distance for each combination of sequence from each cluster
	public double linkage(ClusterOfSequences anotherCluster) {
		ArrayList<Double> distList = new ArrayList<Double>();
		for(Sequence i : elements) {
			for(Sequence j : anotherCluster.elements) {
				distList.add(i.distance(j));
			}
		}
		return average(distList);
	}
	
	
	public void clusterizeAgglomerative() {
		while(subClusters.size() > 2) {
			int index1 = 0; int index2 = 0;
			double min = Double.MAX_VALUE;
			int count1 = 0; int count2 = 0;
			for(ClusterOfSequences i : subClusters) {
				for(ClusterOfSequences j : subClusters) {
					double linkage = i.linkage(j); //distance between the two clusters
					if(linkage <= min && count1 != count2) {
						min = linkage;
						index1 = count1;
						index2 = count2;
					}
					count2++;
				}
				count2 = 0;
				count1++;
			} //end double for loop
			ClusterOfSequences sub1 = subClusters.get(index1);
			ClusterOfSequences sub2 = subClusters.get(index2);
			ClusterOfSequences cluster = new ClusterOfSequences(sub1, sub2); //we create the complex cluster
			if(index1 > index2) {
				subClusters.remove(index1);
				subClusters.remove(index2);
				subClusters.add(index2, cluster);
			} else { 
				subClusters.remove(index1);
				subClusters.remove(index2);
				subClusters.add(index1, cluster);
			}
			this.clusterizeAgglomerative();
		}
	}
	
	public void clusterize() {
		this.clusterizeAgglomerative();
	}
	
	public static void main(String[] args) {
		Sequence seq1 = new Sequence("ATTACG");
		Sequence seq2 = new Sequence("ATATCG");
		Sequence seq3 = new Sequence("ACCCCG");
		Sequence seq4 = new Sequence("GCCGAG");
		Sequence seq5 = new Sequence("TCCCCG");
		
		//five instances of simple clusters
		ClusterOfSequences cl1 = new ClusterOfSequences(seq1);
		ClusterOfSequences cl2 = new ClusterOfSequences(seq2);
		ClusterOfSequences cl3 = new ClusterOfSequences(seq3);
		ClusterOfSequences cl4 = new ClusterOfSequences(seq4);
		ClusterOfSequences cl5 = new ClusterOfSequences(seq5);

		
		ArrayList<Sequence> listSeq = new ArrayList<Sequence>(5);
		listSeq.add(seq1);
		listSeq.add(seq2);
		listSeq.add(seq3);
		listSeq.add(seq4);
		listSeq.add(seq5);
		//ClusterOfSequences bioClusterBis = new ClusterOfSequences(listSeq);
		//bioClusterBis.getNewick();
		
		//step7 - test newick
		ArrayList<Sequence> bottom = new ArrayList<Sequence>(2);
		bottom.add(seq3);
		bottom.add(seq5);
		ArrayList<Sequence> top = new ArrayList<Sequence>(2);
		top.add(seq1);
		top.add(seq2);
		ClusterOfSequences clusterBottom = new ClusterOfSequences(bottom);
		ClusterOfSequences clusterMiddle =  new ClusterOfSequences(seq4);
		ClusterOfSequences clusterLowerPart = new ClusterOfSequences(clusterMiddle, clusterBottom);
		ClusterOfSequences clusterTop = new ClusterOfSequences(top);
		ClusterOfSequences clusterFinal = new ClusterOfSequences(clusterTop, clusterLowerPart);
		//should print ((ATTACG,ATATCG),(GCCGAG,(ACCCCG,TCCCCG)))
		System.out.println("Test getNewick: " + clusterFinal.getNewick()); 
		
		//step9 - test linkage
		System.out.println("Test linkage: " + clusterTop.linkage(clusterLowerPart));
		System.out.println("");
		System.out.println("dist(cl1,cl2): " + cl1.linkage(cl2));
		System.out.println("dist(cl1,cl3): " + cl1.linkage(cl3));
		System.out.println("dist(cl2,cl3): " + cl2.linkage(cl3));
		System.out.println("dist(cl2,cl1): " + cl2.linkage(cl1));
		System.out.println("dist(cl3,cl1): " + cl3.linkage(cl1));
		System.out.println("dist(cl3,cl2): " + cl3.linkage(cl2)); 
		System.out.println("dist(cl3,cl5): " + cl3.linkage(cl5)); 
		//the distances between all the clusters are what we expect them to be
		
		ClusterOfSequences cl6 = new ClusterOfSequences(cl3, cl5);
		System.out.println("dist(cl6,cl4): " + cl6.linkage(cl4));
		ClusterOfSequences cl7 = new ClusterOfSequences(cl1, cl2);
		System.out.println("dist(cl7,cl4): " + cl7.linkage(cl4));
		System.out.println("");
		
		ClusterOfSequences bioCluster = new ClusterOfSequences(listSeq);
		System.out.println(bioCluster.getNewick());
		bioCluster.clusterize();
		System.out.println(bioCluster.getNewick());
		


		
	}

}
