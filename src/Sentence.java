public class Sentence {
	private StringBuilder words; //stores the words in a sentence
	
	Sentence(StringBuilder w){
		words = w;
	}
	
	public Sentence() {
		
	}

	public StringBuilder getSentence(){
		return words;
	}
}
