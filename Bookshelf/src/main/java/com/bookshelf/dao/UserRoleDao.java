package com.bookshelf.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.bookshelf.beans.UserRole;
import com.bookshelf.connection.DBConnection;
import com.bookshelf.connection.DBUtil;
import com.bookshelf.libs.UUIDGenerator;

public class UserRoleDao {

	public static void assign_role(String user_id, String role_id) {
		
		UUIDGenerator uuidGenerator = new UUIDGenerator();
		//create user_role_id
		String user_role_id = uuidGenerator.generateUUID();
		Timestamp assigned_date = new Timestamp(System.currentTimeMillis()); // Current timestamp
		// status options: Active, Pending, Suspend 
		String status = "Pending"; // need to update to Active when user verification is completed.
		
		try (Connection connection = DBConnection.getDBInstance()){
            
			String assign_role_sql = 
					"INSERT INTO " + ApplicationDao.USER_ROLE_TABLE + " VALUES(?,?,?,?,?)";
            
            PreparedStatement preparedStmt = connection.prepareStatement(assign_role_sql);             
            preparedStmt.setString(1, user_role_id);
            preparedStmt.setString(2, user_id);
            preparedStmt.setString(3, role_id);
            preparedStmt.setTimestamp(4, assigned_date);
            preparedStmt.setString(5, status);

            int rowsAffected = preparedStmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Role has been assigned to user successfully.");
            } else {
                System.out.println("Failed to assign role.");
            }

    } catch (SQLException e) {
        DBUtil.processException(e);
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    }

		
		
	}
	
	public static UserRole findUserRoleById(String user_id) {
	    try (Connection connection = DBConnection.getDBInstance()) {
	        String find_user_role_sql = 
	            "SELECT user_role_id, user_id, role_id, assigned_date, status FROM "+ ApplicationDao.USER_ROLE_TABLE + " WHERE user_id = ?";
	            
	        PreparedStatement preparedStmt = connection.prepareStatement(find_user_role_sql);
	        preparedStmt.setString(1, user_id);

	        ResultSet resultSet = preparedStmt.executeQuery();

	        if (resultSet.next()) {

	            String retrievedUserRoleId = resultSet.getString("user_role_id");
	            String userId = resultSet.getString("user_id");
	            String roleId = resultSet.getString("role_id");
	            Timestamp assignedDate = resultSet.getTimestamp("assigned_date");
	            String status = resultSet.getString("status");


	            return new UserRole(retrievedUserRoleId, userId, roleId, assignedDate, status);
	        }
	    } catch (SQLException e) {
	        DBUtil.processException(e);
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    }

	    return null; 
	}
	
	public static boolean deleteUserRolesByUserId(String user_id) {
	    try (Connection connection = DBConnection.getDBInstance()) {
	        String delete_user_role_sql = 
	            "DELETE FROM " + ApplicationDao.USER_ROLE_TABLE + " WHERE user_id = ?";

	        PreparedStatement preparedStmt = connection.prepareStatement(delete_user_role_sql);
	        preparedStmt.setString(1, user_id);


	        int rowsDeleted = preparedStmt.executeUpdate();

	        return rowsDeleted > 0; 
	    } catch (SQLException e) {
	        DBUtil.processException(e);
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	
	public static int countRolesByUserId(String user_id) {
	    int roleCount = 0;

	    try (Connection connection = DBConnection.getDBInstance()) {

	        String count_roles_sql = 
	            "SELECT COUNT(*) AS role_count FROM " + ApplicationDao.USER_ROLE_TABLE + " WHERE user_id = ?";

	        PreparedStatement preparedStmt = connection.prepareStatement(count_roles_sql);
	        preparedStmt.setString(1, user_id);

	        ResultSet resultSet = preparedStmt.executeQuery();

	        if (resultSet.next()) {
	            roleCount = resultSet.getInt("role_count"); 
	        }
	    } catch (SQLException e) {
	        DBUtil.processException(e);
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    }

	    return roleCount; 
	}
	// update role_id 
	public static boolean updateRoleInUserRole(String user_id, String new_role_id) {
	    Timestamp updatedDate = new Timestamp(System.currentTimeMillis()); 
	    
	    try (Connection connection = DBConnection.getDBInstance()) {
	        String update_sql = 
	            "UPDATE " + ApplicationDao.USER_ROLE_TABLE + 
	            " SET role_id = ?, assigned_date = ? WHERE user_id = ?";
	        
	        PreparedStatement preparedStmt = connection.prepareStatement(update_sql);
	        preparedStmt.setString(1, new_role_id);
	        preparedStmt.setTimestamp(2, updatedDate);
	        preparedStmt.setString(3, user_id);
	      

	        int rowsUpdated = preparedStmt.executeUpdate();
	        return rowsUpdated > 0;

	    } catch (SQLException e) {
	        DBUtil.processException(e);
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	// update role_id and status
	public static boolean updateRoleAndStatusInUserRole(String user_id,String new_role_id, String new_status) {
	    Timestamp updatedDate = new Timestamp(System.currentTimeMillis());
	    try (Connection connection = DBConnection.getDBInstance()) {
	        String update_sql = 
	            "UPDATE " + ApplicationDao.USER_ROLE_TABLE + 
	            " SET role_id = ?, assigned_date = ?, status = ? WHERE user_id = ?";
	        
	        PreparedStatement preparedStmt = connection.prepareStatement(update_sql);
	        preparedStmt.setString(1, new_role_id);
	        preparedStmt.setTimestamp(2, updatedDate);
	        preparedStmt.setString(3, new_status);
	        preparedStmt.setString(4, user_id);

	        int rowsUpdated = preparedStmt.executeUpdate();
	        return rowsUpdated > 0;

	    } catch (SQLException e) {
	        DBUtil.processException(e);
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	//update status
	public static boolean updateStatusInUserRole(String user_id, String new_status) {
	    try (Connection connection = DBConnection.getDBInstance()) {
	        String update_sql = 
	            "UPDATE " + ApplicationDao.USER_ROLE_TABLE + 
	            " SET status = ? WHERE user_id = ?";
	        
	        PreparedStatement preparedStmt = connection.prepareStatement(update_sql);
	        preparedStmt.setString(1, new_status);
	        preparedStmt.setString(2, user_id);

	        int rowsUpdated = preparedStmt.executeUpdate();
	        return rowsUpdated > 0;

	    } catch (SQLException e) {
	        DBUtil.processException(e);
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
}
