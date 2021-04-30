import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoyalRumble {
  public List<String> getSortedList(List<String> names) {
	  List<String> namesComposed=checkStringComposed(names);
	  List<String> firstName=checkFirstName(namesComposed);
	  List<String> romanNumber=checkRomanNumber(firstName);
	  List<String> maxList=checkRomanNumber(romanNumber);
	  
	  List<String> readyToSort=new ArrayList<String>();
	  for(int i=0;i<maxList.size();i++) {
		  String listRow=maxList.get(i).toString();
		  String[] listRowSplit=listRow.split(" ");
		  String fName=listRowSplit[0];
		  String romNumber=romanToNum(listRowSplit[1])+"";
		  if(romNumber.length()==1) {
			  romNumber="0"+romNumber;
		  }else{
			  romNumber=romNumber;
		  }
		  String newListRow=fName+" "+romNumber;
		  readyToSort.add(newListRow);
	  }
	  Collections.sort(readyToSort); ; 
	  
	  List<String> sortedRoman=new ArrayList<String>();
	  for(int j=0;j<readyToSort.size();j++) {
		  String listRowsorted=readyToSort.get(j).toString();
		  String[] listRowSplitSorted=listRowsorted.split(" ");
		  String fNameRoman=listRowSplitSorted[0];
		  String convertNum=listRowSplitSorted[1];
		  if(convertNum.substring(0, 1).equals("0")) {
			  convertNum=convertNum.substring(1, convertNum.length());
		  }
		  String romNumberRoman=NumToRman(Integer.parseInt(listRowSplitSorted[1]));
		  String newListRowRoman=fNameRoman+" "+romNumberRoman;
		  sortedRoman.add(newListRowRoman);
	  }
	  
	  
    return sortedRoman;
  }
  public List<String> checkStringComposed(List<String> names) {
	 List<String> composedString=new ArrayList<String>();
	  for(int i=0;i<names.size();i++) {
		  String composedRaw=names.get(i).toString();
		  String[] composedRawDiv=composedRaw.split(" ");
		  if(composedRawDiv.length==2) {
			  composedString.add(composedRaw);
		  }
	  }
    return composedString;
  }
  public List<String> checkFirstName(List<String> composedStringCheck) {
		 List<String> firstName=new ArrayList<String>();
		  for(int i=0;i<composedStringCheck.size();i++) {
			  String composedRaw=composedStringCheck.get(i).toString();
			  String[] composedRawDiv=composedRaw.split(" ");
			  boolean upperlowerCasePresent = false;
			  for (int j = 0; j < composedRawDiv[0].length(); j++) {
			        char currentCharacter = composedRawDiv[0].charAt(j);
			        if (Character.isUpperCase(currentCharacter) || Character.isLowerCase(currentCharacter) ) {
			        	upperlowerCasePresent = true;
			        } else {
			        	upperlowerCasePresent = false;
			        	j=composedRawDiv[0].length();
			        }
			    }
			  
			  if(composedRawDiv[0].length()<=20 && upperlowerCasePresent) {
				  firstName.add(composedRaw);
			  }
			  
		  }
	    return firstName;
	  }
  
  public List<String> checkRomanNumber(List<String> composedStringCheck) {
		 List<String> romanNumber=new ArrayList<String>();
		  for(int i=0;i<composedStringCheck.size();i++) {
			  String composedRaw=composedStringCheck.get(i).toString();
			  String[] composedRawDiv=composedRaw.split(" ");
			  boolean isOrdinalNum = false;
			  for (int j = 0; j < composedRawDiv[1].length(); j++) {
			        char currentCharacter = composedRawDiv[1].charAt(j);
			        if(currentCharacter==new Character('I') || currentCharacter==new Character('V') || currentCharacter==new Character('X') || currentCharacter==new Character('L') || currentCharacter==new Character('C') || currentCharacter==new Character('D') || currentCharacter==new Character('M') || currentCharacter==new Character('i') || currentCharacter==new Character('v') || currentCharacter==new Character('x') || currentCharacter==new Character('l') || currentCharacter==new Character('c') || currentCharacter==new Character('d') || currentCharacter==new Character('m')) {
			        	isOrdinalNum=true;
			        }else {
			        	isOrdinalNum=false;
			        	j=composedRawDiv[1].length();
			        }
			    }
			  
			  if( isOrdinalNum && romanToNum(composedRawDiv[1])<=50) {
				  romanNumber.add(composedRaw);
			  }
			  
		  }
	    return romanNumber;
	  }
  
  public int romanToNum (String roman) {
	  int decimal = 0;
      int lastNumber = 0;
      String romanNumeral = roman.toUpperCase();
      for (int x = romanNumeral.length() - 1; x >= 0 ; x--) {
          char convertToDecimal = romanNumeral.charAt(x);

          switch (convertToDecimal) {
              case 'M':
                  decimal = RomanSet(1000, lastNumber, decimal);
                  lastNumber = 1000;
                  break;

              case 'D':
                  decimal = RomanSet(500, lastNumber, decimal);
                  lastNumber = 500;
                  break;

              case 'C':
                  decimal = RomanSet(100, lastNumber, decimal);
                  lastNumber = 100;
                  break;

              case 'L':
                  decimal = RomanSet(50, lastNumber, decimal);
                  lastNumber = 50;
                  break;

              case 'X':
                  decimal = RomanSet(10, lastNumber, decimal);
                  lastNumber = 10;
                  break;

              case 'V':
                  decimal = RomanSet(5, lastNumber, decimal);
                  lastNumber = 5;
                  break;

              case 'I':
                  decimal = RomanSet(1, lastNumber, decimal);
                  lastNumber = 1;
                  break;
          }
      }
      return decimal;
  }
  public static int RomanSet(int decimal, int lastNumber, int lastDecimal) {
      if (lastNumber > decimal) {
          return lastDecimal - decimal;
      } else {
          return lastDecimal + decimal;
      }
  }
  
  public List<String> checkMaxListSize(List<String> names) {
		 List<String> MaxListSize=new ArrayList<String>();
		 int max=0;
		  if(names.size()>50) {
			  max=50;
		  }else {
			  max=names.size();
		  }
		  for(int i=0;i<max;i++) {
			  MaxListSize.add(names.get(i));
		  }
	    return MaxListSize;
	  }
  
  public String NumToRman(int decimal) {
	  String romanResult="";
	  int[] values = {1000,900,500,400,100,90,50,40,10,9,5,4,1};
      String[] romanLiterals = {"M","CM","D","CD","C","XC","L","XL","X","IX","V","IV","I"};

      StringBuilder roman = new StringBuilder();

      for(int i=0;i<values.length;i++) {
          while(decimal >= values[i]) {
        	  decimal -= values[i];
              roman.append(romanLiterals[i]);
          }
      }
      romanResult=roman.toString();
	  return romanResult;
  }
  
}
