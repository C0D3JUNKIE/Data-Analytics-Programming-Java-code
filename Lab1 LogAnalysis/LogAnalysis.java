public class LogAnalysis {

	public static String[][] readStringLine(String str){
		
		// write the string into a matrix of n*3, n is the number of record
		String[] strSplitLine = str.split("\n");
		String[][] strSplit = new String[strSplitLine.length][3];
		for(int i=0;i<strSplitLine.length;i++) {
			strSplit[i] = strSplitLine[i].split(",");
		}
		return strSplit;
	}

	//Create a string array to identify each person
	public static String[] peopleString(String[][] str) {
		String[] strPeople = new String[1000];
		int k = 0;
		int p = -1;
		for(int i=0;i<str.length;i++) {
			for(int j=0;j<=k;j++) {
				if(str[i][2].equals(strPeople[j])) {    // check if the person in process exists in the name list
					p = j;
					break;
				}
			}
			if(p == -1){
				strPeople[k] = str[i][2];
				k += 1;
			}else{
				p = -1;
			}
		}	
		return strPeople;
	}
	
	//Create a string array to identify each page
	public static String[] pageString(String[][] str) {
		String[] strPage = new String[5000];
		int k = 0;
		int p = -1;
		for(int i=0;i<str.length;i++) {
			for(int j=0;j<=k;j++) {
				if(str[i][1].equals(strPage[j])) {
					p = j;
					break;
				}
			}
			if(p == -1){
				strPage[k] = str[i][1];
				k += 1;
			}else{
				p = -1;
			}
		}	
		return strPage;
	}
	
	//return the index of certain people, the same mapping a person to a integer
	public static int returnPeopleIndex(String[] str1, String[] PeopleArray) {
		String people = str1[2];
		int m = 0;
		for(int i=0; i<PeopleArray.length; i++) {
			if (people.equals(PeopleArray[i])) {
				m = i;
			}
		}
		return m;
	}
	
	//return the index of certain page, the same as mapping a page to a integer
	public static int returnPageIndex(String[] str1, String[] PageArray) {
		String page = str1[1];
		int n = 0;
		for(int i=0; i<PageArray.length; i++) {
			if (page.equals(PageArray[i])) {
				n = i;
			}
		}
		return n;
	}
	
	//Build up the matrix to record whether a certain person visited a certain page
	public static boolean[][] pp(String[][] strarr, String[] peopleS, String[] pageS){		
		boolean[][] pp = new boolean[1000][5000];	
		int m=0;
		int n=0;
		for(int i=0;i<strarr.length;i++){
			m = returnPeopleIndex(strarr[i],peopleS);
			n = returnPageIndex(strarr[i],pageS);
			pp[m][n] = true;
		}
		return pp;
	}
	
	//Read if two pages are read by the same person, 
	//the position this pair represent will add 1
	//Then we will record the maximum number in the matrix and find the pair we need
	public static int[] findAffinity(boolean[][] pp) {
		int maxnum = 0;
		int m=0;
		int n=0;
		int[][] nMatrix = new int[1000][5000];
		
		for(int i=0;i<pp.length;i++){
			for(int j=0;j<pp[i].length-1;j++){
				for(int k=j+1;k<pp[i].length;k++){
					if((pp[i][j])&&(pp[i][k])==true){
						nMatrix[j][k] += 1;
						if(nMatrix[j][k]>maxnum){
							maxnum = nMatrix[j][k];
							m = j;
							n = k;
						}
					}
				}
			}
		}
		int[] position = new int[2];
		position[0]=m;
		position[1]=n;
		return position;
	}
	
	public String highestAffinityPair(String logFileString) {
		String[][] strarr = readStringLine(logFileString);
		String[] peopleS = peopleString(strarr);
		String[] pageS = pageString(strarr);
		int[] position = findAffinity(pp(strarr,peopleS,pageS));
		String finalString;
		if(pageS[position[0]].compareTo(pageS[position[1]])<0){
			finalString = pageS[position[0]]+","+pageS[position[1]];
		}else{
			finalString = pageS[position[1]]+","+pageS[position[0]];
		}
		return finalString;
	}
 	  
}

