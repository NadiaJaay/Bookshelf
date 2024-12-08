package com.bookshelf.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookshelf.beans.User;
import com.bookshelf.dao.RoleDao;
import com.bookshelf.dao.UserDao;
import com.bookshelf.dao.UserRoleDao;

@WebServlet("/editUser")
public class editUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public editUserServlet() {
        super();
 
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String current_user_id = request.getParameter("user_id");
		String edit_is_verified = request.getParameter("is_verified");
	    String edit_role_name = request.getParameter("role_name");
	    String edit_status = request.getParameter("status");
	    
	    String pre_is_verified = request.getParameter("pre_is_verified");
	    String pre_role_name = request.getParameter("pre_role_name");
	    String pre_status = request.getParameter("pre_status");
	    
	    
	    String role_id = RoleDao.findRoleIdByName(edit_role_name);
	    Boolean success_edit =false;
	    
	    if(edit_is_verified != pre_is_verified) {
	    	boolean has_updated =  UserDao.updateIsVerified(current_user_id, edit_is_verified);
	    	
	    	if(has_updated) {
	    		
	    		success_edit =true;
	    		
	    	}else {
	    		System.out.println("[editUserServlet]: verification status has failed ");
	    	}
	    }
	    
	    if(edit_role_name != pre_role_name || edit_status != pre_status ) {

	    	
	    	if(edit_role_name != pre_role_name) {
	    		
	    		String edit_role_id = RoleDao.findRoleIdByName(edit_role_name);
	    		
	    		if(edit_status != pre_status ) {
	    			Boolean updated = UserRoleDao.updateRoleAndStatusInUserRole(current_user_id, edit_role_id, edit_status);
	    			
	    			if(updated) {
	    				success_edit =true;
	    	    		
	    	    	}else {
	    	    		System.out.println("[editUserServlet]: role and status in UserRole update has failed ");
	    	    	}
	    			
	    		} else {
	    			Boolean update_role = UserRoleDao.updateRoleInUserRole(current_user_id, edit_role_id);
	    			if(update_role ) {
	    				success_edit =true;
	    	    		
	    	    	}else {
	    	    		System.out.println("[editUserServlet]: role in UserRole update has failed ");
	    	    	}
	    		}
	    	} else {
	    		
	    		Boolean update_status= UserRoleDao.updateStatusInUserRole(current_user_id, edit_status);
	    		if(update_status ) {
	    			success_edit =true;
    	    		
    	    	}else {
    	    		System.out.println("[editUserServlet]: status in UserRole update has failed ");
    	    	}
	    		
	    	}
	    	
	    	
	    	
	    	
	    }
	    
	    if(success_edit) {
	    	request.setAttribute("message", "User has been updated successfully.");
	    }else {
	    	request.setAttribute("error", "An error occurred. Please try again later.");
	    }
	    
	    response.sendRedirect("UserManager");
	}

}
