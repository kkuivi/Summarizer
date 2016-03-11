import java.util.Hashtable;


public class Word {
	String name; 
	Hashtable<String, Word> synonyms = new Hashtable<String, Word>(); //stores this word's synonyms 
	WordType type; //stores the word's type: Verb, Noun, Preposition, Adjective, Adverb 
	
	Word(String n, Hashtable<String, Word> s, WordType t){
		name = n;
		synonyms = s;
		type = t;
	}
	Word(String n, WordType t){
		name = n;
		type = t;
	}
	Word(String n){
		name = n;
	}
	
	public String getName(){
		return name;
	}
	public void setName(String n){
		name = n;
	}
	
	public void setSynonymList(Hashtable<String, Word> synonymList){
		synonyms = synonymList;
	}
	
	/*Adds a synonym to the word's list of synonyms*/
	public void addSynonym(Word w){
		/*Only adds the word if it is not contained in the word's list of synonyms*/
		if(!synonyms.containsKey(w.getName()))
			synonyms.put(w.getName(), w);
	}
	
	/*Checks if a word is a synonym of this word*/
	public boolean checkSynonym(String w){
		if(synonyms.containsKey(w))
			return true;
		else
			return false;
	}
	
	public Hashtable<String, Word> getSynonymList(){
		return synonyms;
	}
	
	public void setWordType(WordType t){
		type = t;
	}
	public WordType getWordType(){
		return type;
	}
}
