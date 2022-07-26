package bioInfo;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.io.File;

public class SequenceLabeled extends Sequence {
	
	private String label;
	
	public SequenceLabeled() {
		this.label = "";
		this.seq = "";
	}
	
	public SequenceLabeled(String seq, String label) {
		this.seq = seq;
		this.label = label;
	}
	
	public SequenceLabeled(SequenceLabeled s) {
		this.label = s.label;
		this.seq = s.seq;
	}
	
	public SequenceLabeled(File file, String label) throws FileNotFoundException {
		this.seq = Utils.readFasta(file);
		this.label = label;
	}
	
	public String toString() { //it returns the label
		return this.label;
	}
	
	public String getSequence() {
		  return this.seq;
	}
	
	public static void main(String[] args) {
	
		System.out.println("Looking for files from: " + System.getProperty("user.dir"));
		try {
			SequenceLabeled sl1 = new SequenceLabeled(new File(System.getProperty("user.dir") + "/data/Homo_sapiens_HBA1_sequence.fa"), "Homo sapiens HBA1");
			System.out.println(sl1);
			System.out.println(sl1.getSequence());
			
			/*
			 * DNA
			 */
			LinkedList<String> samples = new LinkedList<String>();
			samples.add("Homo sapiens (HBA1)");
			samples.add("Pan paniscus (HBA1)");
			samples.add("Pan troglodytes (HBA1)");
			samples.add("Mus musculus (Hba_a1)");
			samples.add("Mus musculus (Hba_a2)");
			samples.add("Rattus norvegicus (Hba_a2-1)");
			samples.add("Rattus norvegicus (Hba_a2-2)");
			samples.add("Rattus norvegicus (Hba_a3)");
			samples.add("Felis catus (HBA1)");
			samples.add("Bos taurus (HBA)");
			samples.add("Danio rerio (hbaa1)");
			samples.add("Macaca mulatta (HBA2)");
			samples.add("Xenopus tropicalis (hba1)");
			int nbSamples = samples.size();
			
			ArrayList<Sequence> data = new ArrayList<Sequence>(nbSamples);
			String seqFilePath;
			for (String currentSample : samples) {
				seqFilePath = System.getProperty("user.dir") + "/data/" + Utils.getTaxon(currentSample).replace(' ', '_') + "_" + Utils.getGeneName(currentSample) + "_sequence.fa";
				data.add(new SequenceLabeled(new File(seqFilePath), Utils.getTaxon(currentSample) + " " + Utils.getGeneName(currentSample)));
			}
			ClusterOfSequences clusterHemoglobinNucleotides = new ClusterOfSequences(data);
			System.out.println(clusterHemoglobinNucleotides.getNewick());
			clusterHemoglobinNucleotides.clusterize();
			System.out.println(clusterHemoglobinNucleotides.getNewick());
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
