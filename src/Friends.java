import java.io.*;
import java.util.*;

public class Friends {

	static BufferedReader x = new BufferedReader(new InputStreamReader(System.in));
	static  HashMap<String, ArrayList<String>> friendship = new HashMap<String, ArrayList<String>>();
	static ArrayList<String> subgraph = new ArrayList<String>();
	static ArrayList<String> friends = new ArrayList<String>();
	static String me = new String();

	public static void main(String[] args)throws Exception {


		ArrayList<String> name = new ArrayList<String>();
		ArrayList<String> school = new ArrayList<String>();
		ArrayList<String> connection = new ArrayList<String>();
		int num =0; //number of people
		int choice = 0;

		System.out.print("Enter the name of the file(ex: doc.txt): ");
		String infile = x.readLine();

		File file = new File(infile);
		Scanner input = new Scanner(file);

		while(input.hasNext()){

			String s = input.nextLine();
			if(num == 0){
				num = Integer.parseInt(s);
			}else{
				makeWord(s,name,school,connection);
			}
		}
		while(choice != '5'){
			System.out.println(" ");
			System.out.println("Choose: ");
			System.out.println("1-Subgraph (students at a school)");
			System.out.println("2-Shortest path (Intro chain)");
			System.out.println("3-Connected Islands (cliques)");
			System.out.println("4-Connectors (Friends who keep friends together)"); 
			System.out.println("5-quit");
			choice = Integer.parseInt(x.readLine());
			if(choice =='0' || choice > '5'){
				System.out.println("Wrong choice... Try Again");
			}
			switch (choice) {
			case 1: System.out.print("Enter the school name: ");
			String infile1 = x.readLine();
			subgraph(infile1,name,school,connection);
			if(subgraph.size() == 0){
				System.out.println("WE COULDN'T FIND THE SCHOOL");
			}else{
				for(int i =0; i < subgraph.size(); i++){
					System.out.println(subgraph.get(i));
				}
			}
			break;

			case 2: System.out.print("Name of the person who wants the intro");
			String a = x.readLine();
			System.out.print("Name of the other person");
			String b = x.readLine();
			System.out.println(introChain(a,b));
			break;

			case 3: System.out.print("Name of the school for which cliques are to be found: ");
			String schoolName = x.readLine();
			System.out.println("clique:");
			cliques(schoolName,name,school,connection);
			break;

			case 4: System.out.print("Connector: " + connectors());
			break;

			case 5: return;

			}

		}
	}

	private static void makeWord(String s,ArrayList<String> name, ArrayList<String> school,ArrayList<String> connection){

		int w = s.indexOf('|');
		boolean node = false; //true if the name part starts first

		if((s.charAt(w+1) == 'y' && w+1 != s.length()-1 && s.charAt(w+2) == '|') ||(s.charAt(w+1) == 'n' && w+1 == s.length()-1)){
			node = true;
		}
		String word = "";
		if(node == true){
			int i = 0;

			for(i = 0; i < s.length(); i++){ 
				if(s.charAt(i) != '|'){
					word += s.charAt(i);
				}else{
					name.add(word);
					word= "";
					break;
				}
			}
			if(s.charAt(i+1) != 'y'){
				school.add("");
			}else{
				for(i +=3; i <s.length();i++){
					if(s.charAt(i) != '|'){
						word += s.charAt(i);
					}
				}
				school.add(word);  

			}
		}else{
			connection.add(s);
		}
		for(int i = 0; i < name.size(); i++){

			ArrayList<String> tmp = new ArrayList<String>();

			for(int j= 0; j < connection.size();j++){

				if(connection.get(j).substring(0,connection.get(j).indexOf('|')).equals(name.get(i))){
					int x = connection.get(j).indexOf('|');
					String friend = connection.get(j).substring(x+1);
					tmp.add(friend); 
				}else if(connection.get(j).substring(connection.get(j).indexOf('|')+1).equals(name.get(i))){
					tmp.add(connection.get(j).substring(0,connection.get(j).indexOf('|')));
				}

			}
			friendship.put(name.get(i), tmp);
		}
	}

	private static void subgraph(String s,ArrayList<String> name, ArrayList<String> school,ArrayList<String> connection)throws Exception{
		int count = 0;
		subgraph.clear();
		
		if(school.contains(s)){
			for(int i = 0; i < school.size();i++){
				if(school.get(i).equals(s)){
					subgraph.add(name.get(i));
					count ++;
				}
			}
			for(int j = 0; j < connection.size();j++){
				for(int i = 0; i < count; i++){
					if(connection.get(j).substring(0,connection.get(j).indexOf('|')).equals(subgraph.get(i)) && !subgraph.contains(connection.get(j))){
						for(int k = 0; k < count;k++){
							if(connection.get(j).substring(connection.get(j).indexOf('|')+1).equals(subgraph.get(k))){
								subgraph.add(connection.get(j));
							}
						}
					}else if(connection.get(j).substring(connection.get(j).indexOf('|')+1).equals(subgraph.get(i)) && !subgraph.contains(connection.get(j))){
						for(int k = 0; k < count;k++){
							if(connection.get(j).substring(0,connection.get(j).indexOf('|')).equals(subgraph.get(k))){
								subgraph.add(connection.get((j)));
							}
						}
					}
				}
			}
		}else
			return;
	}

	private static String introChain(String a, String b){

		if(friendship.get(a).size()==0){
			return "No chance.";
		}

		if(a==b){
			return a;
		}

		String result = "";

		HashMap<String, String> resultMap = new HashMap<String, String>();
		Set<String> keys = friendship.keySet();
		String[] people = keys.toArray(new String[friendship.size()]);
		for(int x = 0; x<people.length; x++){
			resultMap.put(people[x], "");
		}

		Queue<String> queue = new LinkedList<String>();
		String nextFriend = "";

		String tmp = a;
		while(!tmp.equals(b)){
			for(int x = 0; x<friendship.get(tmp).size(); x++){
				nextFriend = friendship.get(tmp).get(x);
				if(resultMap.get(nextFriend)=="" && !nextFriend.equals(a)){
					queue.add(nextFriend);
					resultMap.put(nextFriend, resultMap.get(tmp) + tmp + "-");
				}
			}			
			if(queue.isEmpty()){
				return "No chance.";
			}
			tmp = queue.remove();
			
		}
		
		result = resultMap.get(tmp) + tmp;
		return result;

	}


	private static void cliques(String s,ArrayList<String> name, ArrayList<String> school,ArrayList<String> connection){
		
		int count = 0;
		subgraph.clear();
		friends.clear();
		
		if(school.contains(s)){
			for(int i = 0; i < school.size();i++){
				if(school.get(i).equals(s)){
					subgraph.add(name.get(i));
					count ++;
				}
			}
			for(int j = 0; j < connection.size();j++){
				for(int i = 0; i < count; i++){
					if(connection.get(j).substring(0,connection.get(j).indexOf('|')).equals(subgraph.get(i)) && !subgraph.contains(connection.get(j))){
						for(int k = 0; k < count;k++){
							if(connection.get(j).substring(connection.get(j).indexOf('|')+1).equals(subgraph.get(k))){
								friends.add(connection.get(j));
							}
						}
					}else if(connection.get(j).substring(connection.get(j).indexOf('|')+1).equals(subgraph.get(i)) && !subgraph.contains(connection.get(j))){
						for(int k = 0; k < count;k++){
							if(connection.get(j).substring(0,connection.get(j).indexOf('|')).equals(subgraph.get(k))){
								friends.add(connection.get((j)));
							}
						}
					}
				}
			}
		}else
			return;
	}
	private static String connectors(){

		Set<String> keys = friendship.keySet();
		String[] people = keys.toArray(new String[friendship.size()]);
		ArrayList<String> connector = new ArrayList<String>();
		String result = "";

		for(int x = 0; x<friendship.size(); x++){
			if(friendship.get(people[x]).size()==1){
				if(friendship.get(friendship.get(people[x]).get(0)).size()>1){
					if(!connector.contains(friendship.get(people[x]).get(0))){
						connector.add(friendship.get(people[x]).get(0 ));
					}
				}
			}
		}

		int x;
		for(x = 0; x<connector.size()-1; x++){
			result += connector.get(x) + ",";
		}
		result += connector.get(x);
		return result;
	}

}