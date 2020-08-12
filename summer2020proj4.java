import java.io.*;
import java.util.*;



public class  summer2020proj4{
	static ArrayList<String[]> data = new ArrayList<String[]>();
	//the data arraylist stores all the data from the file -> each row is a string array (each column in the row is a string, and all the columns are stored in an array)
	static ArrayList<String> states = new ArrayList<String>();
	//this stores all the states in the data file
	static ArrayList<Double[]> cases = new ArrayList<Double[]>();
	//this is important
	//this is an arraylist of a double array
	//meaning that each value of the lists is an array
	//in the array, the first value is the sum of the number of positive cases
	//in the array, the second value is the sum of the number of dates that reported positive cases
	static double[] average;
	//the average array stores the average cases of each state
	static double[] percentage;
	//the percentage array stores the percentage of average cases of each state to the whole us
	static double total = 0;
	//the total variable keeps track of the total average cases in the whole us
	
	
	public static void main (String[] args){
		//this reads the file
		readFile("daily.csv");
		//this goes through the data and records all the data we care about
		process();
		average = new double[states.size()];
		percentage = new double[states.size()];
		calc();
		//this finds the average and then the percentage of average cases in each state to the whole US
		
		Scanner scan = new Scanner (System.in);
		System.out.println("Input number of masks: ");
		
		String input = scan.nextLine();
		
		double num = Double.valueOf(input);
		
		for (int i = 0; i<percentage.length; i++){
			System.out.println("State: " + states.get(i) + " Number of Masks: " + (percentage[i]*num)/100);
		}
	}
	public static void readFile(String filename) {
		String sline;
		String[] bufs;
		FileReader frd = null;
		BufferedReader brd = null;
		
		try {
			frd = new FileReader(filename);
			brd = new BufferedReader(frd); 
		}
		catch (IOException e) {
			System.out.println("Can't open file to read:" + filename);
			System.exit(0);
		}
		
		try {
			String line = "";
			while (true){
				line = brd.readLine();
				if (line ==null){
					break;
				}
				//System.out.println(line);
				String[] temp = line.split(",");
				data.add(temp);
			}
			brd.close();
			frd.close();
		}
		catch (IOException e) {
			System.out.println("Read error from file:" + filename);
			System.exit(0);
		}
	}
	
	
	public static void process(){
		//this function goes through all the data and takes record of those that have the time stamp we are looking at
		String[] dates = {"20200803", "20200622", "20200522", "20200422", "20200322"};
		//these are all the time stamps we care about
		
		//we go row by row of all the data we have to process
		for (int i = 1; i<data.size(); i++){
			//this first step checks whether or not the date of the data point is during one of the dates in the dates array
			if (check_date(dates, data.get(i)) == false){
				continue;
			}
			
			//the next step is to update the ArrayList states which records all the states in the data
			//the update states function returns two values
			//the first value is a flag -> if the number is 0, it means the state has never been recorded before
			//and this is the first value of the state
			//if the number is 1, it means the state was previously recorded, so you add the number of positive cases in this data point to however many cases it already had
			
			//the second value it stores is the index -> this shows what index in the arraylist the state is in
			//the cases arraylist has the same indexes as states meaning that if index 0 in arraylist states is CA, the value in the cases arraylist refers to the number of cases in CA
			int [] s = update_states(data.get(i)[1]);
			int index = s[1];
			int flag = s[0];
			
			if (flag == 0){
				double num = Double.valueOf(data.get(i)[2])*1.0;
				Double[] temp = {num, 1.0};
				cases.add(temp);
			}
			else{
				if (data.get(i)[2].length()==0 || data.get(i)[2] == null){
					continue;
				}
				double num = Double.valueOf(data.get(i)[2])*1.0;
				double val = cases.get(index)[0] + num;
				cases.get(index)[0] = val;
				cases.get(index)[1]++;
				
			}
		}
	}
	
	
	
	public static boolean check_date(String[] dates, String[] data){
		//this takes in the row of data you are in (data) and dates which you are looking at (dates)
		String d = data[0];
		
		for (int i = 0; i<dates.length; i++){
			//this loop goes through all the valid dates in the dates array and checks to see if the row date matches
			//if it does this function reports that this row of data needs to be processed
			if (dates[i].equals(d) == true){
				return(true);
			}
		}
		//if after the whole loop, the date of the row does not match with that of all the accepted date values, we report that we should ignore this row
		//for the rest of our calculations (we are only looking at 6 dates from the past 6 months)
		return(false);
		
	}
	
	public static int[] update_states(String s){
		//it takes in the state of the row of data you are looking at as a parameter
		//it goes through all the past states we have recorded in the states array list
		//if we find that the state is present, we report back that this state has been recorded before, and the index of the state
		for (int i = 0; i<states.size(); i++){
			//this if statement checks to see if the state has been reported before and if its true the function stops here
			if (states.get(i).equals(s)==true){
				int [] answer = {1, i};
				return (answer);
			}
		}
		//if we find that the state is not present, we add it to the end of the states arraylist
		//and we report back that the state is found at the last value of the list (the index would be the length of the list - 1) aka why look down one
		states.add(s);
		int[] answer = {0, states.size()-1};
		return(answer);
		
	}
	
	public static void calc(){
		//this function finds the average number of cases for each state
		double sum = 0.0;
		
		for (int i = 0; i<cases.size(); i++){
			double val = cases.get(i)[0];
			double num = cases.get(i)[1];
			double avg = val/num;
			average[i] = avg;
			sum = sum + avg;
		}
		
		total = sum;
		//after the averages for each state is found, we need to check the percentage of the average of each state to the whole us
		calc_percentage();
	}
	
	public static void calc_percentage(){
		//this function finds the percentage of average cases in each state to the whole US
		for (int i = 0; i<average.length; i++){
			double p = average[i]/total * 100;
			percentage[i] = p;
		}
	}


}