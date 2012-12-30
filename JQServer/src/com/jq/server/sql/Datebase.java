package com.jq.server.sql;

import java.sql.*;

public class Datebase {

	//�������ݿ�
	public Connection getConnection()	
	{
		Connection con = null;
		//String url = "jdbc:mysql://localhost/JQ";	//�������ݿ��URL
		String username="root";	//���ݿ���û���
		String password = "123456";	//���ݿ������
		try{
			Class.forName("com.mysql.jdbc.Driver");	
			//con = DriverManager.getConnection(url,username,password);	//��¼�����������ݿ���
			con = DriverManager.getConnection("jdbc:mysql://localhost/JQ?user="+username+"&password="+password+"&useUnicode=true&characterEncoding=gb2312" );
		}catch(Exception e){
			e.printStackTrace();
		}
		return con;
	}
	
	//�رս���������顢����
	public static void close(ResultSet rs,Statement st,Connection con){
		close(rs);
		close(st);
		close(con);
	}
	
	//�ر�����������
	public static void close(Statement st,Connection con){
		close(st);
		close(con);
	}
	
	//�رս����
	private static void close(ResultSet rs) {
		if(rs != null){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
	}
	
	//�ر�����
	private static void close(Connection con) {
		if(con != null){
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
	}
	
	//�ر�����
	private static void close(Statement st) {
		if(st != null){
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//��ѯ������ע���û�
	public void readRegistedUser() throws Exception{
		Datebase db = new Datebase();
		Connection con = db.getConnection();	//�õ���������
		Statement st = con.createStatement();	//�õ�����
		String sql = "select * from user";	//��дSQL����
		ResultSet rs = st.executeQuery(sql);	//�õ������
		while(rs.next()){
			System.out.print(rs.getString(1)+"\t");	//��ȡID	int(8)
			System.out.print(rs.getString(2)+"\t");	//��ȡname varchar(20)
			System.out.print(rs.getString(3)+"\t");	//��ȡemail varchar(20)
			System.out.print(rs.getString(4)+"\t");	//��ȡpassword varchar(20)
			System.out.print(rs.getString(5)+"\t");	//��ȡcomment varchar(80)
			System.out.print(rs.getString(6)+"\t");	//��ȡhead	varchar(10)
			System.out.println("");
		}
		Datebase.close(rs, st, con);
	}
	
	//��ѯ�����û�
	public void readOnlineUser() throws Exception{
		Datebase db = new Datebase();
		Connection con = db.getConnection();	//�õ���������
		Statement st = con.createStatement();	//�õ�����
		String sql = "select * from user where id in (select id from user_status where status=1)";	//��дSQL����
		ResultSet rs = st.executeQuery(sql);	//�õ������
		while(rs.next()){
			System.out.print(rs.getString(1)+"\t");	//��ȡID	int(8)
			System.out.print(rs.getString(2)+"\t");	//��ȡname varchar(20)
			System.out.print(rs.getString(3)+"\t");	//��ȡemail varchar(20)
			System.out.print(rs.getString(4)+"\t");	//��ȡpassword varchar(20)
			System.out.print(rs.getString(5)+"\t");	//��ȡcomment varchar(80)
			System.out.print(rs.getString(6)+"\t");	//��ȡhead	varchar(10)
			System.out.println("");
		}
		Datebase.close(rs, st, con);
	}

	//ע��һ�����û� id,name,email,password,comment
	public void addUser(String id,String name,String email,String password,String comment) throws Exception{
		Datebase db = new Datebase();
		Connection con = db.getConnection();
		Statement st = con.createStatement();
		String sql = "insert into user values('"+id+"','"+name+"','"+email+"','"+password+"','"+comment+"','01.png');";//headû��Ū��Ĭ����01.png
		//String sql = "insert into user(name,email,password,comment) values('"+name+"','"+email+"','"+password+"','"+comment+"');";//headû��Ū��Ĭ����01.png
		//INSERT INTO USER(NAME,email,PASSWORD) VALUES ('Laycher','laycher@live.cn','123456')
		st.executeUpdate(sql);
		System.out.println(sql);
		sql = "insert into user_status values('"+id+"','0')";	//Ĭ���ǲ�����
		st.executeUpdate(sql);
		Datebase.close(st, con);
	}
	
	//��������״̬
	public void setUserOnline(String id) throws Exception{
		Connection con = new Datebase().getConnection();
		Statement st = con.createStatement();
		String sql="update user_status set status=1 where id='"+id+"'";
		st.executeUpdate(sql);
		Datebase.close(st, con);
	}
	//����Ϊ������״̬
	public void setUserNotOnline(String id) throws Exception{
		Connection con = new Datebase().getConnection();
		Statement st = con.createStatement();
		String sql="update user_status set status=0 where id='"+id+"'";
		st.executeUpdate(sql);
		Datebase.close(st, con);
	}
	
	//�����ǳ�
	public void setUserName(String id,String name)throws Exception{
		Connection con = new Datebase().getConnection();
		Statement st = con.createStatement();
		String sql="update user set name='"+name+"'where id='"+id+"'";
		st.executeUpdate(sql);
		Datebase.close(st, con);
	} 
	
	//����������
	public void setUserPassword(String id,String password) throws Exception{
		Connection con = new Datebase().getConnection();
		Statement st = con.createStatement();
		String sql="update user set password='"+password+"'where id='"+id+"'";
		st.executeUpdate(sql);
		Datebase.close(st, con);
	}
	
	//����ͷ��
	public void setUserHead(String id,String head) throws Exception{
		Connection con = new Datebase().getConnection();
		Statement st = con.createStatement();
		String sql="update user set head='"+head+"'where id='"+id+"'";
		st.executeUpdate(sql);
		Datebase.close(st, con);
	}
	
	//ɾ���û���������ǲ�Ҫ�ð�
	public void delUser(String id) throws Exception{
		Connection con = new Datebase().getConnection();
		Statement st = con.createStatement();
		String sql="delete from user_status where id='"+id+"'";
		st.executeUpdate(sql);
		sql = "delete from user where id='"+id+"'";
		st.executeUpdate(sql);
		Datebase.close(st, con);
	}
	
	//��Ӻ���
	public void search(String str) throws Exception{
		Connection con = new Datebase().getConnection();
		Statement st = con.createStatement();
		String sql="select distinct id,name,comment from user where id like '%"+str+"%' or name like '%"+str+"%'";
		ResultSet rs =st.executeQuery(sql);
		while(rs.next()){
			System.out.print(rs.getString(1)+"\t");	//��ȡID	int(8)
			System.out.print(rs.getString(2)+"\t");	//��ȡname varchar(20)
			System.out.print(rs.getString(3)+"\t");	//��ȡcomment varchar(80)
			System.out.println("");
		}
		Datebase.close(rs, st, con);
	}
	
	public static void main(String[] args){
		Datebase db = new Datebase();
		try {
			//db.addUser("100006", "Laycher", "laycher@live.cn", "123456", null);
			//db.setUserNotOnline("100005");
			//db.setUserName("100005", "Laycher");
			//db.setUserPassword("100005", "000000");
			//db.setUserHead("100005", "02.png");
			//db.delUser("100006");
			//db.readRegistedUser();
			db.search("2");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
