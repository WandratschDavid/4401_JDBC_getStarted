package main;

import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;

public class TheMain
{
	public static void main(String[] args)
	{
		String driver = null;
		String url = null;
		String username = null;
		String password = null;

		try (FileInputStream in = new FileInputStream("dbconnect.properties");)
		{
			Properties prop = new Properties();
			prop.load(in);
			driver = prop.getProperty("driver");
			url = prop.getProperty("url");
			username = prop.getProperty("username");
			password = prop.getProperty("password");

			if (driver == null || url == null || username == null || password == null)
			{
				throw new Exception("Fehler! Property File muss driver, url, username, password enthalten!");
			}
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}

		try (Connection connection = DriverManager.getConnection(url, username, password);)
		{
			Statement statement = connection.createStatement();

			statement.execute("drop table PERSON if exists");
			statement.execute("create table PERSON(NAME varchar(64) primary key)");

			statement.execute("insert into PERSON values('Widmann')");
			statement.execute("insert into PERSON values('Mayr')");
			statement.execute("insert into PERSON values('Rumpfhuber')");

			ResultSet resultSet = statement.executeQuery("select NAME from PERSON");
			while (resultSet.next())
			{
				System.out.println(resultSet.getString("NAME"));
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}