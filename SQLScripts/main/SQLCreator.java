package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;

public class SQLCreator {

	public static void main(String[] args) {
		File target = new File("databaseQuery.sql");
		try (PrintWriter writer = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(target, true), Charset.forName("UTF-8"))))){
			BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Current max index: ");
			int i = Integer.parseInt(bf.readLine())+1;
			//TODO: make possible to insert at any point. Meaning just increase all the others and then insert. first check how to dow with group column
			try {
				while(true) {
					StringBuilder query = new StringBuilder("INSERT INTO roles_en (_id, name, team_id, 'group', category) VALUES (");
					query.append(i);
					int endIndex = query.length();
					query.append(",\"");
					query.append(read("Name: ", bf));
					//TODO make split based on names not with the awkward commata behind the team
					query.append("\",");
					String team = read("team: ", bf);
					int teamIndex;
					boolean groupSet = false;
					if(team.equalsIgnoreCase("1,2")) {
						teamIndex = query.length();
						query.append(1);
					} else if(team.contains(",")) {
						String [] parts = team.split(",");
						teamIndex = -1;
						query.append(parts[0]);
						query.append(',');
						query.append(parts[1]);
						groupSet = true;
					} else {
						teamIndex = -1;
						query.append(team);
					}
					if(!groupSet) {
					query.append(',');
					query.append(i);
					}
					query.append(',');
					query.append(read("category: ", bf));
					query.append(");");
					String s = query.toString();
					System.out.println(s);
					writer.print(s);
					if(teamIndex != -1) {
						query.replace(69, endIndex, Integer.toString(++i));
						query.setCharAt(teamIndex, '2');
						s = query.toString();
						System.out.println(s);
						writer.print(s);
					}
					i++;
				}
			} catch (InterruptedException e) {
				//user interrupt
			}
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
	}
	
	private static String read(String what, BufferedReader reader) throws IOException, InterruptedException {
		System.out.println(what);
		String input = reader.readLine();
		if(input.equalsIgnoreCase("exit")){
			throw new InterruptedException("User interrupt.");
		}
		return input;
	}
}