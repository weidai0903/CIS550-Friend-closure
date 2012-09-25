package database;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;


public class weidai_Q8 {

	/**
	 * @param args
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) {
		
		HashMap<String,ArrayList<String>> friendsDB= new HashMap<String,ArrayList<String>>();
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
	
			String url="jdbc:oracle:thin:@fling.seas.upenn.edu:1521:cis";
			String username="weidai";
			String password="david911";
			Connection connection = DriverManager.getConnection(url, username, password);
			
			Statement sqlStat = connection.createStatement();
			String sql="select * from Friends";
			ResultSet result = sqlStat.executeQuery(sql);
			
			ResultSetMetaData metaData = result.getMetaData();
			
			
			ArrayList<String> friends;
			while(result.next()) {
				String login = result.getString("login");
				friends = friendsDB.get(login);
				if(friends == null){
					friends = new ArrayList<String>();
					friends.add(result.getString("friend"));
					friendsDB.put(login,friends);
					closure.put(login,new ArrayList<String>());
				}
				else{
					friends.add(result.getString("friend"));
				}
			}
			
			closure(friendsDB);
			
			File file = new File("Q8.txt");
			FileWriter fileWriter = new FileWriter(file);
			BufferedWriter writer = new BufferedWriter(fileWriter);
			
			String columnName1 = metaData.getColumnName(1);
			String columnName2 = metaData.getColumnName(2);
			writer.write(columnName1 + "\t" + columnName2 + "\n");
			Set entries = closure.entrySet();
			Iterator iter = entries.iterator();
			while(iter.hasNext()){
				Entry<String, ArrayList<String>> entry = (Entry<String, ArrayList<String>>)iter.next();
				String login = entry.getKey();
				ArrayList<String> closureFriends = entry.getValue();
				//System.out.println();
				for(int numFriends=0; numFriends<closureFriends.size();numFriends++){
					//System.out.println(login+"\t"+closureFriends.get(numFriends));
					writer.write(login + "\t" + closureFriends.get(numFriends)+ "\n" );
				}
			}	
			
			writer.close();
			System.out.println("done without error");
			
		}catch(Exception e) {
			System.out.println("Failed to connect to database");
			e.printStackTrace();
		}
		
	}
	
	public static void closure(HashMap<String,ArrayList<String>> friendsDB){
		
		for(int i=0; i<friendsDB.size(); i++){
			Set entries = friendsDB.entrySet();
			Iterator iter = entries.iterator();
			while(iter.hasNext()){
				Entry<String, ArrayList<String>> entry = (Entry<String, ArrayList<String>>)iter.next();
				String login = entry.getKey();
				recursive(login,login,friendsDB);

			}
		}
	}
	
	public static HashMap<String, ArrayList<String>> closure = new HashMap<String, ArrayList<String>>();
	
	
	public static void recursive(String key, String login, HashMap<String, ArrayList<String>> friendsDB){

		ArrayList<String> closureFriends = closure.get(key);

		ArrayList<String> friends= friendsDB.get(login);

		if(friends==null) return;
		
		for(int i=0; i<friends.size(); i++){
			String oneFriend = friends.get(i);

			if(closureFriends.contains(oneFriend)) continue;
			
			closure.get(key).add(oneFriend);
			recursive(key, oneFriend, friendsDB);
			
		}	
		
	}
}

