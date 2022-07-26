package bioInfo;

public class Sequence {
	
	protected String seq; //we need it in a subclass
	
	public Sequence() {
		this.seq = "";
	}
	
	public Sequence(String s) {
		this.seq = s;
	}
	
	public Sequence(Sequence s) {
		this.seq = s.seq;
	}
	
	public String toString() {
		return this.seq;
	}
	
	public double distance(Sequence otherSeq) { //returns the distance between the two sequences
		double distance = 0.0;
		double maxSeq = Math.max(seq.length(), otherSeq.seq.length());
		double minSeq = Math.min(seq.length(), otherSeq.seq.length());
		double diff = maxSeq - minSeq;
	
		for(int i = 0; i < maxSeq; i++) {
			if(i < minSeq && this.seq.charAt(i) != otherSeq.seq.charAt(i)) {
				distance++;
			} 
		}
		distance = (distance + diff) / maxSeq;
		return distance;
	}
	
	public static void main(String[] args) {
		
		Sequence seq1 = new Sequence("ATTACG");
		Sequence seq2 = new Sequence("ATATCG");
		Sequence seq3 = new Sequence("ACCCCG");
		Sequence seq4 = new Sequence("GGGGAA");
		Sequence seq5 = new Sequence("TTTACG");
		Sequence seq6 = new Sequence("ATTAC"); //beginning of seq1
		Sequence seq7 = new Sequence("ATATC"); //beginning of seq2
		
		System.out.println("Comparing same-length sequences");
		System.out.println("dist(seq1,seq1): " + seq1.distance(seq1));
		System.out.println("dist(seq1,seq2): " + seq1.distance(seq2));
		System.out.println("dist(seq2,seq1): " + seq2.distance(seq1));
		System.out.println("dist(seq6,seq7): " + seq6.distance(seq7));
		System.out.println("dist(seq7,seq6): " + seq7.distance(seq6));
		System.out.println("dist(seq1,seq3): " + seq1.distance(seq3));
		System.out.println("dist(seq2,seq3): " + seq2.distance(seq3));
		System.out.println("dist(seq1,seq4): " + seq1.distance(seq4));
		
		System.out.println("");
		System.out.println("Comparing different-length sequences");
		System.out.println("dist(seq1,seq6): " + seq1.distance(seq6));
		System.out.println("dist(seq6,seq1): " + seq6.distance(seq1));
		System.out.println("dist(seq1,seq7): " + seq1.distance(seq7));
		System.out.println("dist(seq2,seq7): " + seq2.distance(seq7));

	}

}
