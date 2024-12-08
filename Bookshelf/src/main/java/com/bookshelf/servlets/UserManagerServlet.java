package com.bookshelf.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bookshelf.beans.Address;
import com.bookshelf.beans.Library;
import com.bookshelf.beans.User;
import com.bookshelf.beans.UserRole;
import com.bookshelf.dao.AddressDao;
import com.bookshelf.dao.LibraryDao;
import com.bookshelf.dao.ReservationDao;
import com.bookshelf.dao.RoleDao;
import com.bookshelf.dao.UserDao;
import com.bookshelf.dao.UserRoleDao;
import com.bookshelf.dtos.UserManagerDto;
import com.bookshelf.dtos.UserRoleDto;


@WebServlet("/UserManager")
public class UserManagerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public UserManagerServlet() {
        super();

    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("index.jsp");
            return;
        }
        List<UserManagerDto> userManagerDtos = new ArrayList<>();

        
       try {
    	      // Get all Users
    	      List<User> users = UserDao.getAllUsers();
        	
        	  System.out.println("[UserManagerServlet] found all users! ");
        	 
        	  for (User user:users) {

                UserManagerDto userManagerDto = new UserManagerDto();
                UserRoleDto userRoleDto = new UserRoleDto();
        		  
        		userManagerDto.setUser(user);        		
        		
        		// Get user_id and address_id from user.
        		String user_id =  user.getUser_id();
        		String address_id = user.getAddress_id();
        		
        		System.out.println("[UserManagerServlet] user_id: "+user_id+" / address_id: "+ address_id + ".");
            	 
        		// Get address 
        		Address address = AddressDao.findAddressById(address_id);
      			
            	userManagerDto.setAddress(address);   

     		    // Get role 
            	UserRole userRole = UserRoleDao.findUserRoleById(user_id);
            	System.out.println("[UserManagerServlet] userRole: "+userRole);
  			
        		String role_id =  userRole.getRole_id();
        		String status =  userRole.getStatus();
        		String role_name = RoleDao.findRoleNameByRoleId(role_id);
        		
        		System.out.println("[UserManagerServlet] role_name: "+role_name);
        		
        		userRoleDto.setUser_role_id(role_id);
        		userRoleDto.setRole_name(role_name);
        		userRoleDto.setStatus(status);
        		
        		userManagerDto.setUserRole(userRoleDto);   		        		
        		

        		int totalReservation= ReservationDao.countReservationsByUserId(user_id);
        		userManagerDto.setTotalReservation(totalReservation);
        		
        		userManagerDtos.add(userManagerDto);

        	 } 
        	
        	  request.setAttribute("userManagerDtos", userManagerDtos);

        	
        } catch(Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred. Please try again later.");
        }
         
		request.getRequestDispatcher("usermanager.jsp").forward(request, response);
	}



	    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        // Determine the action based on which button was clicked
	        String editUser_id = request.getParameter("edit");
	        String deleteUser_id = request.getParameter("delete");
	        
	        System.out.println("[UMS]editUser_id:"+editUser_id+"/ deleteUser_id: "+deleteUser_id);

	        if (editUser_id != null) {
	        	
	          try {
                   UserManagerDto userManagerDto = new UserManagerDto();
                   UserRoleDto userRoleDto = new UserRoleDto();
	        	
	        	   //redirect to editUser.jsp
	        	
	               // find the user
	        	   User user = UserDao.getUserById(editUser_id);
	        	   userManagerDto.setUser(user);      
	        	   
	        	   System.out.println("[UserManageServlet]:Edit-user:"+user);
	        	
	        	   // get address
	        	   String address_id =  user.getAddress_id();
	        	   Address address = AddressDao.findAddressById(address_id);
	           	   userManagerDto.setAddress(address);
	           	   
	            	System.out.println("[UserManageServlet]:Edit-address:"+address);
	        	
	        	   // get userRole
	        	   String user_id = user.getUser_id();
	        	   UserRole userRole = UserRoleDao.findUserRoleById(user_id);
	        	
        		   String role_id =  userRole.getRole_id();
        		   String status =  userRole.getStatus();
        		   String role_name = RoleDao.findRoleNameByRoleId(role_id);
        		
        		   System.out.println("[UserManagerServlet] role_name: "+role_name);
        		
        		   userRoleDto.setUser_role_id(role_id);
        		   userRoleDto.setRole_name(role_name);
        		   userRoleDto.setStatus(status);
        		
        		   userManagerDto.setUserRole(userRoleDto);   		        		
        		
        		   int totalReservation= ReservationDao.countReservationsByUserId(user_id);
        		   userManagerDto.setTotalReservation(totalReservation);
        		
        		   request.setAttribute("userInfo", userManagerDto);
        		   request.getRequestDispatcher("editUser.jsp").forward(request, response);
        		   
	          }catch(Exception e) {
		            e.printStackTrace();
		            request.setAttribute("error", "An error occurred. Please try again later.");
		            response.sendRedirect("UserManager");
		        }
		         
		           
			 

	        } else if (deleteUser_id != null) {
	        	
	        	boolean no_more_rev = false;
	        	boolean no_more_role = false;
	        	boolean no_more_user = false;
	        	
	        	
	        	try {
	        	     int count_reservations = ReservationDao.countReservationsByUserId(deleteUser_id);
	        	
	              	  //check if user has reservation and delete
	        	      if (count_reservations > 0){	        		
	        		      no_more_rev = ReservationDao.deleteReservationsByUserId(deleteUser_id);	        
	        	      } else {
	        		      no_more_rev = true; 
	        	      }
	        	
	        	      // if there's no more reservation, then delete userRole
	        	      if (no_more_rev == true) {
	        	    	
	        	          // need to check if user has userRole. if user has a role then delete.
	        	    	  int count_userRole = UserRoleDao.countRolesByUserId(deleteUser_id);
	        	    	  
	        	    	  if (count_userRole > 0) {
	        	    		  // first check if it's librarian assign to library. if so, change library_id to null, then delete userRole.
	        	    		 boolean is_librarian = LibraryDao.isLibrarian(deleteUser_id);
	        	    		 
	        	    		 if(is_librarian == true) {
	        	    			 String target_library_id = LibraryDao.getLibraryId(deleteUser_id);
	        	    			 
	        	    			 if(target_library_id != null) {
	        	    				 // update library_id to null
	        	    				 String new_librarian_id = null;
	        	    				 boolean is_updated = LibraryDao.updateLibrarianId(target_library_id, new_librarian_id);
	        	    			 }
	
	        	    		 }
	        	    		  
	        	    	  
	        		       no_more_role = UserRoleDao.deleteUserRolesByUserId(deleteUser_id);
	        		       
	        		      } else {
	        		    	   no_more_role =  true;
	        		      }
	        	      }	  
	        	      
	        	      // delete user 
	        	      if (no_more_role == true) {
	        	    	  String address_id =  UserDao.findAddressId(deleteUser_id);
	        	    	 
	        	          no_more_user = UserDao.deleteUserByUserId(deleteUser_id);
	        	          
	        	          if (no_more_user ==true) {
	        	        	  //delete address if there's no address used by other user.
	        	        	  int same_address_user = UserDao.countUsersByAddressId(address_id);
	        	        	  
	        	        	  if(same_address_user == 0) {
	        	        		  boolean no_more_address = AddressDao.deleteAddressById(address_id);
	        	        	  } else {
	        	        		 System.out.println("[UserManageServlet]: address is in use by other user. No address deletion required.");
	        	        	  }
	        	        	          	        	 
	        	          } else {
	        	        	  System.out.println("[UserManageServlet]: user deletion has failed.");
	        	        	  request.setAttribute("error", "User deletion has failed. Please try again later.");	        	        	  
	        	          }
     
        	          }else {
        	        	  System.out.println("[UserManageServlet]: userRole deletion has failed.");
        	        	  request.setAttribute("error", "Cannot process User Deletion due toUserRole deletion has failed. Please try again later.");
        	          }

	        } catch(Exception e) {
	            e.printStackTrace();
	            request.setAttribute("error", "An error occurred. Please try again later.");
	        }
	         
	        	 response.sendRedirect("UserManager");
		 }
	    }
	    

}
