public class Library{
  public int numBooks;
  public int signUpDays;
  public int shipDays;
  public int[] books;

public Library(int numBooks, int signUpDays, int shipDays, int[] books){
  this.numBooks=numBooks;
  this.signUpDays=signUpDays;
  this.shipDays=shipDays;
  this.books=books;
}

@Override
public String toString() {
  // TODO Please fix method later
  return numBooks + "";
}


}