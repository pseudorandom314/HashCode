
package src;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.util.*;
import java.util.stream.*;
import java.util.Arrays;//Binhong remove duplicate ArrayUtils.removeElement


public class GBManager {
	String fileName;
	Scanner sc;
	ArrayList<BookRank> priority = new ArrayList<BookRank>();
	ArrayList<Library> libraries = new ArrayList<Library>();
	Set<Integer> uniqueBooks = new LinkedHashSet<Integer>();//remove scanned books from future library
	private int totalBooks;
	private int totalLibraries;
	private int days;

	/**
	 * methods:set_fileName, scan
	 * purpose: to initialize GBManager through reading in the text file
	 */

	//remove duplicated books from following library
	//TODO remove+shrink size
	//public int[] removeDuplicate(int[] books){
	//	int
	//}


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
	public Library makeLibrary(int id) {
		int numBooks= sc.nextInt();
		int signUpDays= sc.nextInt();
		int shipDays=sc.nextInt();
		int[] books = new int[numBooks];
		//int libraryID = 0;
		for(int i=0; i<numBooks;i++) { //every book goes into books[]
		books[i]= sc.nextInt();
		}
		sortBooks(books);
	//	uniqueBooks.addAll(IntStream.of(books).boxed().collect(Collectors.toList()));

		// TODO loop whole set to removing
		Library lib= new Library(numBooks, signUpDays, shipDays, books, id);
		return lib;
	}

	//TODO test for removing on 2 ways
	//remove duplicate 1

	/*
	public static int[] removeDuplicate(int[] books, int bookName){
		int[] noDupBook = new int[books.length -1];
		System.arraycopy(books, 0, noDupBook, 0, bookName);
		System.arraycopy(books, bookName+1, noDupBook, bookName, books.length-bookName-1);
	}
	*/

	//remove duplicate 2
	/*public static int[] removeDuplicate(int[] books, int bookName){
		books = (int[])ArrayUtils.removeElement(books, bookName);
	}
	*/

	/*
	//remove duplicate 3
	public void removeDuplicate(int[] books){
		for(int i=0; i<books.length; i++){
			if(uniqueBooks.contains(books[i]) == true){
				continue;
			} else {
				//books = ArrayUtils.removeElement(books,books[i]);
				books = ArrayUtils.remove(books,books[i]);
			}
		}
	}

	*/

	//remove duplicate 4
	public void removeDuplicate(int[] books){
		for(int i=0; i<books.length; i++){
			if(uniqueBooks.contains(books[i]) == true){
				for(int k=i; k<books.length-1;k++){
					books[k]=books[k+1];
					//shrink
				}
			}
			//break;
		}
	}


	//public void removeDuplicate(int[] books, int bookName){
		//if(uniqueBooks.contain(bookName) == false)
			//remove+shink
	//}


	//add all lib to libraries
	public void addLibraries() {
		for(int i=0; i<totalLibraries;i++) {
			libraries.add(makeLibrary(i));
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
//		System.out.println("Before Sort:");
//		libraries.forEach(library -> System.out.println(getAveScore(library)));

		//Collections.sort function!
		Collections.sort(libraries,new Comparator<Library>() {
			@Override
			public int compare(Library l1, Library l2) {
				return Double.compare(getAveScore(l2),getAveScore(l1));
			}
		});

//		System.out.println("After Sort:");
//		libraries.forEach(library -> System.out.println(getAveScore(library)));
//		libraries.forEach(library -> System.out.println(library));
//		System.out.print("[");
//		Iterator<Integer> itr = uniqueBooks.iterator();
//		while (itr.hasNext()) {
//			System.out.print(itr.next() + " ");
//		}
//		System.out.print("]");

	}

	///jihun
	public void scan() {
		int totalDays= days;
		int libCount = 0;
		int noLib=0;
		for(int i=0; i<libraries.size() && totalDays>0;i++) {
			int[]books= libraries.get(i).books;
			int bookCount=0;
			libCount++;
			for(int j=0;j<books.length;j++) {
				if(!uniqueBooks.contains(books[j])) { //not seen yet
					bookCount++;
					System.out.print(books[j] + " ");
					uniqueBooks.add(books[j]);
				}else {
					continue;
				}

			}
			if(bookCount>0) {
				System.out.println();
				System.out.println(libraries.get(i).libraryID + " " + bookCount);}
			else {
				noLib++;
			}
		}
		libCount-=noLib;
		System.out.println("Lib Count: "+ libCount);
	}
	//jihun


//	public void scan() {
//		int totalDays= days;
//		int libCount = 0;
//		for(int i=0; i<libraries.size() && totalDays>0;i++) {
//			//System.out.println("Library "+ i + ":");
//
//			int[]books= libraries.get(i).books;
//			//System.out.println("bookNum: "+books.length);
//			int bookCount=0;
//			libCount++;
//			for(int j=0;j<books.length;j++) {
//				if(!uniqueBooks.contains(books[j])) { //not seen yet
//
//					bookCount++;
//					System.out.print(books[j] + " ");
//					//System.out.print("Book: " + books[j] + " ");
//					uniqueBooks.add(books[j]);
//				}else if(uniqueBooks.contains(books[j])) {
//					//System.out.print("duplicated");
//					continue;
//				}
//
//			}
//			totalDays-=libraries.get(i).signUpDays;
//			System.out.println();
//			System.out.println(libraries.get(i).libraryID + " " + bookCount);
//			//System.out.println("Lib name: "+ libraries.get(i).libraryID + " Book Count: " + bookCount + "\n");
//		}
//
//		System.out.print("Lib Count: "+ libCount);
//	}


	/**
	 * methods: setUp, run
	 * purpose: runs the program
	 */

	//assign all ints to its appropriate variables
	public void setUp()  {
		firstLine();
		setScore();
		sortPriority();
		addLibraries();
		sortLibraries();
		scan();
		//removeDuplicate();
		//sortLibraries();//resort it after removed duplicated in here???
		//TODO whats the order to handle the file???
	}

	//greedy algorithm= re-sort library after each scanning
	//TODO greedy

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

	}

}

