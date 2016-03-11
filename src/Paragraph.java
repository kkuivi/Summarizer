import java.util.ArrayList;

public class Paragraph {
	ArrayList<Sentence> sentences; //stores sentences in a Paragraph
	
	Paragraph(ArrayList<Sentence> p){
		sentences = p;
	}
	
	public Paragraph() {
		sentences = new ArrayList<Sentence>();
	}

	public ArrayList<Sentence> getParagraph(){
		return sentences;
	}
	public void addSentence(Sentence s){
		sentences.add(s);
	}
}
