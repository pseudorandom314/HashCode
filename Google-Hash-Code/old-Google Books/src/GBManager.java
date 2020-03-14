import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.util.*;

public class GBManager {
	String fileName;
	Scanner sc;
	ArrayList<BookRank> priority = new ArrayList<BookRank>();
	ArrayList<Library> libraries = new ArrayList<Library>();
	private int totalBooks;
	private int totalLibraries;
	private int days;
	
	/**
	 * methods:set_fileName, scan
	 * purpose: to initialize GBManager through reading in the text file
	 */
	
	// ask user to choose text file
	public void set_fileName() {
		Scanner in = new Scanner(System.in);
	    System.out.println("Enter a library set (.txt file): ");
	    fileName = in.next();
	}
	
	//gets the scanner ready
	public void scan(String fileName) throws FileNotFoundException{
		FileReader file;
		try {
			file= new FileReader(fileName);	
			
			}
		catch(Exception e) {
			throw new FileNotFoundException("File Not Found");
			}
		
		sc = new Scanner(file);	
	}
	
	/**
	 * methods:firstLine, setScore, sortPriority
	 * purpose: to initialize GBManager through reading in the text file
	 */
	
	// takes care of the first line 
	public void firstLine() {
		totalBooks= sc.nextInt();
		totalLibraries=sc.nextInt();
		days=sc.nextInt();
	}
	
	//set the scores of each book in priority
	public void setScore() {
		for(int i=0;i<totalBooks;i++) {
			int score= sc.nextInt();
			priority.add(new BookRank(i, score));
		}
	}
	//sorts priority in descending order
	public void sortPriority() {
		Collections.sort(priority, new Comparator<BookRank>()
			{
			public int compare(BookRank br1, BookRank br2) {
				return Integer.valueOf(br2.score).compareTo(br1.score);
			}
			}); 
		
	}
	
	
	/**
	 * methods: getScore, sortBooks, makeLibrary, addLibraries
	 * purpose: to fill in libraries[]
	 */
	
	//get score of the book searched
	public int getScore(int name) {
		int score=-1;
		for(int i=0;i<priority.size();i++) {
			if(priority.get(i).name==name) {
				score=priority.get(i).score;
			}
		}
		return score;
	}
	//sort books by score (to determine which book to ship out first)
	public void sortBooks(int[] books) {
		int i,j,keyIndex,key;
		for(j=1;j<books.length;j++){
			keyIndex=books[j];
			key= getScore(keyIndex);
			for(i=j-1; (i>=0)&&(getScore(books[i])<key);i--){
				books[i+1]=books[i];
				books[i]=keyIndex;
			}
			books[i+1]=keyIndex;
		}
	}
	
	//initialize library
	public Library makeLibrary() {
		int numBooks= sc.nextInt();
		int signUpDays= sc.nextInt();
		int shipDays=sc.nextInt();
		int[] books = new int[numBooks];
		for(int i=0; i<numBooks;i++) { //every book goes into books[]
			books[i]= sc.nextInt();
		}
		sortBooks(books);
		Library lib= new Library(numBooks, signUpDays, shipDays, books);
		return lib;
	}
	
	//add all lib to libraries
	public void addLibraries() {
		for(int i=0; i<totalLibraries;i++) {
			libraries.add(makeLibrary());		
		}
	}
	
	/**
	 * methods: getTotalScore, getDaySpent, getAveScore, sortLibraries
	 * purpose: to obtain sorted libraries[] in descending order using AveScore per day 
	 */
	
	//get max possible score of each lib
	public int getTotalScore(Library lib) {
		int tscore=0;
		for(int i=0;i<lib.numBooks;i++) {
			int name= lib.books[i];
			for(int j=0; j<totalBooks;j++) {
				if(priority.get(j).name==name) {
					tscore+= priority.get(j).score;
				}
			}
		}
		return tscore;
	}
	
	//get days spent of each lib
	public int getDaySpent(Library lib) {
		int daySpent=lib.signUpDays;
		int numBook= lib.numBooks;
		int shipDay= lib.shipDays;
		while(daySpent<=days && numBook>0) {
			numBook-=shipDay;
			daySpent++;
		}
		return daySpent;
	}
	
	public double getAveScore(Library lib) {
		double aveScore= (double)getTotalScore(lib)/(double)getDaySpent(lib); 
		return aveScore;
	}
	
	//sorts libraries by aveScore (to determine which to signup first)
	public void sortLibraries() {
		System.out.println("Before Sort:");
		libraries.forEach(library -> System.out.println(getAveScore(library)));

		//Collections.sort function!
		Collections.sort(libraries,new Comparator<Library>() {
			@Override
			public int compare(Library l1, Library l2) {
				return Double.compare(getAveScore(l2),getAveScore(l1));
			}
		});

		System.out.println("After Sort:");
		libraries.forEach(library -> System.out.println(getAveScore(library)));
	}
	
	/**
	 * methods: setUp, run
	 * purpose: runs the program
	 */
	
	//assign all ints to its appropriate variables
	public void setUp() {
		firstLine();
		setScore();
		sortPriority();
		addLibraries();
		sortLibraries();
	}
	
	//runs the program
	public void run() throws FileNotFoundException {
		set_fileName();
		try {
			scan(fileName);
		}
		catch(Exception e) {
			throw new FileNotFoundException("File Not Found");
		}
		setUp();
		System.out.println("done");
	}
	
}
