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
	public void testDatabaseMem() throws SQLException {
		//testDatabase("jdbc:h2:mem:test");
	}

	private void testDatabase(String url) throws SQLException {
		System.out.println("testDatabase===============================================start");
	    Connection connection= DriverManager.getConnection(url);
	    Statement s=connection.createStatement();
	    try {
	    	s.execute("CREATE TABLE USERS(username varchar_ignorecase(50) not null primary key,password varchar_ignorecase(50) not null,enabled boolean not null)");
	    	s.execute("create table authorities (username varchar_ignorecase(50) not null,authority varchar_ignorecase(50) not null,constraint fk_authorities_users foreign key(username) references users(username));");
	    	s.execute("create unique index ix_auth_username on authorities (username,authority);");
	    	s.execute("insert into users('admin', '1234', true)");
	    	s.execute("insert into users('user', '1234', true)");
	    	s.execute("insert into authorities('admin', 'ROLE_ADMIN')");
	    	s.execute("insert into authorities('user', 'ROLE_USER')");
	    } catch(SQLException sqle) {
	    	sqle.printStackTrace();
	        System.out.println("Table not found, not dropping");
	    }
	    PreparedStatement ps=connection.prepareStatement("select * from users");
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
