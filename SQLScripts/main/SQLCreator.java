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
			while(true) {
				StringBuilder query = new StringBuilder("INSERT INTO roles VALUES (");
				query.append(i);
				int endIndex = query.length();
				query.append(",\"");
				System.out.println("Name: ");
				String firstInput = bf.readLine();
				if(firstInput.equalsIgnoreCase("exit"))
					break;
				query.append(firstInput);
				query.append("\",\"");
				System.out.println("Beschreibung: ");
				query.append(bf.readLine());
				query.append("\",");
				System.out.println("team: ");
				String team = bf.readLine();
				int teamIndex;
				if(team.equalsIgnoreCase("1,2")) {
					teamIndex = query.length();
					query.append(1);
				} else {
					teamIndex = -1;
					query.append(team);
				}
				query.append(',');
				query.append(i++);
				query.append(',');
				System.out.println("category: ");
				query.append(bf.readLine());
				query.append(");");
				String s = query.toString();
				System.out.println(s);
				writer.print(s);
				if(teamIndex != -1) {
					query.replace(26, endIndex, Integer.toString(i));
					query.setCharAt(teamIndex, '2');
					s = query.toString();
					System.out.println(s);
					writer.print(s);
				}
			}
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
	}
}