package src;
import java.util.*;
public class Library{
  public int numBooks;
  public int signUpDays;
  public int shipDays;
  public int[] books;
  public int libraryID;//Binhong= return which lib we are taking about

//public Library(int numBooks, int signUpDays, int shipDays, int[] books){
public Library(int numBooks, int signUpDays, int shipDays, int[] books, int libraryID){
  this.numBooks=numBooks;
  this.signUpDays=signUpDays;
  this.shipDays=shipDays;
  this.books=books;
  this.libraryID=libraryID; //Binhong testing
}

@Override
public String toString() {
  // TODO Please fix method later
  //return "Numbooks: "+numBooks+", SignupDays: "+signUpDays+", shipDays:"+shipDays+", books: "+Arrays.toString(books);
  return "Numbooks: "+numBooks+", SignupDays: "+signUpDays+", shipDays:"+shipDays+", books: "+Arrays.toString(books)+", libraryID:" + libraryID;
}


}