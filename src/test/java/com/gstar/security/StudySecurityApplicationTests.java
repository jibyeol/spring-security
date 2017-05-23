package com.gstar.security;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudySecurityApplicationTests {

	@Test
	public void testDatabaseNoMem() throws SQLException {
	    testDatabase("jdbc:h2:test");
	}
	
	@Test
	public void testDatabaseMem() throws SQLException {
	    testDatabase("jdbc:h2:mem:test");
	    aaaa("jdbc:h2:mem:test");
	}

	private void testDatabase(String url) throws SQLException {
		System.out.println("testDatabase===============================================start");
	    Connection connection= DriverManager.getConnection(url);
	    Statement s=connection.createStatement();
	    try {
	    	s.execute("CREATE TABLE USERS users(username varchar_ignorecase(50) not null primary key,password varchar_ignorecase(50) not null,enabled boolean not null)");
	    } catch(SQLException sqle) {
	        System.out.println("Table not found, not dropping");
	    }
	    s.execute("CREATE TABLE PERSON (ID INT PRIMARY KEY, FIRSTNAME VARCHAR(64), LASTNAME VARCHAR(64))");
	    PreparedStatement ps=connection.prepareStatement("select * from PERSON");
	    ResultSet r=ps.executeQuery();
	    if(r.next()) {
	        System.out.println("data?");
	    }
	    r.close();
	    ps.close();
	    s.close();
	    connection.close();
	    System.out.println("testDatabase===============================================end");
	}
	
	public void aaaa(String url) throws SQLException {
		System.out.println("===============================================start");
	    Connection connection= DriverManager.getConnection(url);
	    Statement s=connection.createStatement();
	    PreparedStatement ps=connection.prepareStatement("SELECT * FROM USERS");
	    ResultSet r=ps.executeQuery();
	    if(r.next()) {
	        System.out.println("data?");
	        System.out.println(r.getString("username"));
	    }
	    r.close();
	    ps.close();
	    s.close();
	    connection.close();
	    System.out.println("===============================================end");
	}

}
