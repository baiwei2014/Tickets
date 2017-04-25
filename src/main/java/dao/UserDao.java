package dao;

import java.sql.*;
import bean.*;

public class UserDao extends Data_Built{
	String sql;
	public PreparedStatement cps = null;
	
	String create_user_table = "create table if not exists userInfo(uId int AUTO_INCREMENT primary key, userName varchar(1000), passWord varchar(1000), PhoneNumber varchar(1000), UserLocation varchar(1000))";
	String add_user = "insert into userInfo(userName, passWord, PhoneNumber, UserLocation) values (?, ?, ?, ?)";
	String query_user_byName = "select * from userInfo where userName=?";
	String query_user_byPhone = "select * from userInfo where PhoneNumber=?";
	
	String update_userInfo = "update userInfo set userName=?, PhoneNumber=?, UserLocation=? where uId=?";
	String update_userName = "update userInfo set userName=? where userName=?";
	String update_userPassword = "update userInfo set passWord=? where uId=?";
	
	String delete_user = "delete from userInfo where uId = ?";
	public UserDao() {//�����ݿ�������Ӳ������û���
		openCon();
		try {
			cps = con.prepareStatement(create_user_table);
			cps.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	//����û�
	public boolean addUser(User user){
		openCon();
		boolean bool=false;
		try {
			ps= con.prepareStatement(add_user);
			ps.setString(1,user.getUsername());
			ps.setString(2,user.getPassword());
			ps.setString(3, user.getPhoneNumber());
			ps.setString(4, user.getUserLocation());
			int num = ps.executeUpdate();
			if(num>0){
				bool=true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			this.closePs();
			this.closeCon();
		}
		return bool;
	}
	//��ѯ�û�
	public boolean queryUserbyName(User bean){
		openCon();
		boolean bool=false;
		try {
			ps=con.prepareStatement(query_user_byName);
			ps.setString(1, bean.getUsername());
			rs = ps.executeQuery();
			while(rs.next()){
				bool=true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			this.closeRs();
			this.closePs();
			this.closeCon();
		}
		return bool;
	}
	public boolean queryUserbyPhone(User bean){
		openCon();
		boolean bool=false;
		try {
			ps=con.prepareStatement(query_user_byPhone);
			ps.setString(1, bean.getPhoneNumber());
			rs = ps.executeQuery();
			while(rs.next()){
				bool=true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			this.closeRs();
			this.closePs();
			this.closeCon();
		}
		return bool;
	}
	//�����û�
	public boolean UpdateUser(User user, int userId) {
		openCon();
		boolean bool=false;
		try {
			ps=con.prepareStatement(update_userInfo);
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPhoneNumber());
			ps.setString(3, user.getUserLocation());
			ps.setInt(4, userId);
			int num = ps.executeUpdate();
			if (num > 0) {
				bool = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			this.closeRs();
			this.closePs();
			this.closeCon();
		}
		return bool;
	}
	public boolean UpdateUserName(String name, String ori_name) {
		openCon();
		boolean bool=false;
		try {
			ps=con.prepareStatement(update_userName);
			ps.setString(1, name);
			ps.setString(2, ori_name);
			int num = ps.executeUpdate();
			if (num > 0) {
				bool = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			this.closeRs();
			this.closePs();
			this.closeCon();
		}
		return bool;
	}
	public boolean UpdateUserpassword(String _password, int userId) {
		openCon();
		boolean bool=false;
		try {
			ps=con.prepareStatement(update_userPassword);
			ps.setString(1, _password);
			ps.setInt(2, userId);
			int num = ps.executeUpdate();
			if (num > 0) {
				bool = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			this.closeRs();
			this.closePs();
			this.closeCon();
		}
		return bool;
	}
	//ע���û�
	public boolean DeleteUser(int userId) {
		openCon();
		boolean bool=false;
		try {
			ps= con.prepareStatement(delete_user);
			ps.setInt(1,userId);
			int num = ps.executeUpdate();
			if(num>0){
				bool=true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			this.closePs();
			this.closeCon();
		}
		return bool;
	}
}
