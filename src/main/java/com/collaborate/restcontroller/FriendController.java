package com.collaborate.restcontroller;

import java.util.List;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.collaborate.DAO.FriendDao;
import com.collaborate.Model.User;
import com.collaborate.Model.Error;
import com.collaborate.Model.Friend;

@Controller
public class FriendController 
{
	@Autowired
	private FriendDao friendDao;
	@RequestMapping(value="/suggesteduserslist",method=RequestMethod.POST)
	public ResponseEntity<?> getSuggestedUsersList(HttpSession session)
	{
		User user=(User)session.getAttribute("user");
		if(user==null)
		{
			Error error=new Error(3, "UnAuthoribute user");
			return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
		}
		List<User> suggestedUsers=friendDao.listOfSuggestedUsers(user.getUsername());
		return new ResponseEntity<List<User>>(suggestedUsers,HttpStatus.OK);
	}
	
	
	@RequestMapping(value="/friendrequest/{toUsername}",method=RequestMethod.POST)
	public ResponseEntity<?> friendRequest(@PathVariable String toUsername, HttpSession session)
	{
		User user=(User)session.getAttribute("user");
		if(user==null)
		{
			Error error=new Error(3, "UnAuthorised user");
			return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
		}
		String fromUsername=user.getUsername();
		friendDao.friendRequest(fromUsername,toUsername);
		return new ResponseEntity<Void> (HttpStatus.OK);
	}	
	
	
	@RequestMapping(value="/pendingrequests",method=RequestMethod.GET)
	public ResponseEntity<?> pendingRequest(HttpSession session)
	{
		User user=(User)session.getAttribute("user");
		if(user==null)
		{
			Error error=new Error(3, "UnAuthorised user");
			return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
		}
		List<Friend> pendingRequests=friendDao.listOfPendingRequest(user.getUsername());
		return new ResponseEntity<List<Friend>>(pendingRequests,HttpStatus.OK);
	}
	
	
	@RequestMapping(value="/updatependingrequest/{fromId}/{status}",method=RequestMethod.PUT)
	public ResponseEntity<?> updatePendingRequests(@PathVariable String fromId,@PathVariable char status,HttpSession session)
	{
		User user=(User)session.getAttribute("user");
		if(user==null)
		{
			Error error=new Error(3, "UnAuthorised user");
			return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
		}
			friendDao.updatePendingRequest(fromId, user.getUsername(),status);
			return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value="/listoffriends",method=RequestMethod.GET)
	public ResponseEntity<?>listOfFriends(HttpSession session)
	{
		User user=(User)session.getAttribute("user");
		
		if(user==null)
		{
			Error error=new Error(3, "UnAuthorised user");
			return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
		}
			List<Friend> friends=friendDao.listOfFriends(user.getUsername());
			return new ResponseEntity<List<Friend>>(friends,HttpStatus.OK);
	}

}