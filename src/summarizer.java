import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

public class summarizer {
	static Hashtable<String, Word> DICTIONARY = new Hashtable<String, Word>();
	static ArrayList<Word> WORDS = new ArrayList<Word>(); //stores the words in a sentence
	static final char[] BEGINNING_PUNCTUATIONS = {'(', '#', '\"'}; //list of punctuations that occur at the beginning of a word
	static final char[] ENDING_PUNCTUATIONS = {'.', ',', ';' , ':' , '\"', ')', '?', '!', '-'}; //list of punctuatiions that occur at the end of a word
	static final String ALTERVISTA_ENDPOINT = "http://thesaurus.altervista.org/thesaurus/v1"; //link to the online Altervista Thesaurus
	static final String ALTERVISTA_KEY = "qN15E22FQq5EmSv1Wk8x"; //key to be used as a parameter when making request to Altervista
	static final String ALTERVISTA_LANGUAGE = "en_US"; //indicates to Altervista that synonyms should be return in English
	static final String ALTERVISTA_OUTPUT = "json"; //indicates to Altervista that request should be returned in JSON
	
	
	public static void main(String[] args) throws FileNotFoundException{
		readFile("test1.txt");
		for(int i = 0; i < WORDS.size(); i ++){
			System.out.println(WORDS.get(i).name);
		}
	}
	
	static boolean readFile(String filename) throws FileNotFoundException{
		Scanner filereader; 
		
		/*Try to read file with the given filename. If the file does not exist, print an error statement, and
		 * return false*/
		try{
			filereader = new Scanner(new FileReader(new File(filename)));
		} catch(Exception e){
			System.out.println("ERROR! File does not exist!");
			return false;
		}
		
		while(filereader.hasNext()){
			String word = filereader.next();
			/*if the word contains a punctuation then check if the punctuation occurs at the beginning or end of the 
			 * word, and place the word before the punctuation or the punctuation before the word depending on the order they 
			 * appear.*/
			int punctuation_index = containsPunctuation(word);
			if(punctuation_index != -1){ 
				if(punctuation_index == 0){ //punctuation occurs before the word
					String punctuation = Character.toString(word.charAt(punctuation_index));
					WORDS.add(new Word(punctuation, WordType.Punctuation));
					String word_input = word.substring(punctuation_index + 1);
					WORDS.add(new Word(word_input));
				}
				else if(punctuation_index == word.length() - 1){ //punctuation occurs at the end of the word
					String word_input = word.substring(0, punctuation_index);
					WORDS.add(new Word(word_input));
					String punctuation = Character.toString(word.charAt(punctuation_index));
					WORDS.add(new Word(punctuation, WordType.Punctuation));
				}
				
			} 
			else
				WORDS.add(new Word(word));	
		}
		return true;
	}
	
	static int containsPunctuation(String word){
		char c = word.charAt(0); 
		//check if punctuation occurs at the beginning of a word
		if(punctuation(true, c))
			return 0;
		c = word.charAt(word.length() - 1);
		//check if punctuation occurs at the end of a word
		if(punctuation(false, c))
			return word.length() - 1;
		
		return -1; //word does not contain punctuations
	}
	
	/*Check if a character is a punctuation. Uses the value of the boolean parameter to know which character array
	 * to check: True --> BEGINNING_PUNCTUATIONS, False --> ENDING_PUNCTUATIONS*/
	static boolean punctuation(boolean beginningPunctuation, char c){
		char[] arr = beginningPunctuation == true ? BEGINNING_PUNCTUATIONS : ENDING_PUNCTUATIONS;
		for(int i = 0; i < arr.length; i ++){
			if(c == arr[i])
				return true;
		}
		return false;
	}
	
	/*Go through each word in the WORDS ArrayList and get it's list of synonyms*/
	static void checkDictionary(){
		for(Word w : WORDS){
			/*if a word is in our Thesaurus then just set w's list of synonyms to that word's synonym list*/
			if(DICTIONARY.containsKey(w.name)){ 
				Hashtable<String, Word> synonymList = DICTIONARY.get(w.name).getSynonymList();
				w.setSynonymList(synonymList);
			}
			/*check Altervista Online service for the word's synonym list */
			else{
				//TODO create Async checkAlterVista method 
				checkAlterVista(w.name);
			}
		}
	}
	
	/*Make a request to the Altervista server to get a word's synonym list*/
	static void checkAlterVista(String word){
		/*Coonect to the Altervista server, make a request to get a word's synonym list, and parse the JSON result*/
		try{
			URL serverAddress = new URL(ALTERVISTA_ENDPOINT + "?word=" + URLEncoder.encode(word, "UTF-8") + "&language=" + ALTERVISTA_LANGUAGE + "&key=" + ALTERVISTA_KEY + "&output=" + ALTERVISTA_OUTPUT);
			HttpURLConnection connection = (HttpURLConnection)serverAddress.openConnection();
			connection.connect(); //connect to server
			int rc = connection.getResponseCode(); //get the response code from trying to connect to the server. 
												   //200--> Successful connection
			if(rc == 200){ //successful connection
				String line = null;
				BufferedReader br = new BufferedReader(new java.io.InputStreamReader(connection.getInputStream()));
				StringBuilder sb = new StringBuilder(); //store every character from the JSON response
				while((line = br.readLine()) != null)
					sb.append(line + '\n');
				JSONObject obj = (JSONObject) JSONValue.parse(sb.toString());
				JSONArray array = (JSONArray) obj.get("response");
				for(int i = 0; i < array.size(); i++){
					JSONObject list = (JSONObject) ((JSONObject)array.get(i)).get("list");
				}
				
			}else //unsuccessful connection
				System.out.println("HTTP error: " + rc);
			connection.disconnect();
		}catch (java.net.MalformedURLException e) { 
		      e.printStackTrace(); 
	    } catch (java.net.ProtocolException e) { 
	      e.printStackTrace(); 
	    } catch (java.io.IOException e) { 
	      e.printStackTrace(); 
	    } 
	}
}
	

