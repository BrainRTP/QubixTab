package ru.brainrtp.core.test.Example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class H2 {
	static Connection conn;
	static Statement statmt;
	static PreparedStatement preparedStatement = null;

	H2(String url) {
		try {
			Class.forName("org.h2.Driver");
			conn = DriverManager.getConnection("jdbc:h2://" + url + ";mode=MySQL", "sa", "");
			statmt = conn.createStatement();
			statmt.execute(
					"CREATE TABLE IF NOT EXISTS `users` (`user` varchar(255) PRIMARY KEY,`time` varchar(255) NOT NULL)");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insert(String user, Long time) {
		try {
			PreparedStatement e = conn
					.prepareStatement("INSERT INTO users (user,time) VALUES (?,?) ON DUPLICATE KEY UPDATE time = ?;");
			e.setString(1, user);
			e.setLong(2, time);
			e.setLong(3, time);
			e.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<String> select(String user) {
		try {
			preparedStatement = conn.prepareStatement("SELECT * FROM users WHERE user = ?;");
			preparedStatement.setString(1, user);
			ResultSet e = preparedStatement.executeQuery();
			ArrayList<String> item = new ArrayList<String>();
			if (e.next()) {
				item.add(e.getString("user"));
				item.add(e.getString("time"));
				return item;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}