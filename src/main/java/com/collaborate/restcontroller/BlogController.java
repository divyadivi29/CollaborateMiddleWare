package com.collaborate.restcontroller;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.swing.text.rtf.RTFEditorKit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.collaborate.DAO.BlogPostDao;
import com.collaborate.Model.BlogComment;
import com.collaborate.Model.BlogPost;
import com.collaborate.Model.User;
import com.collaborate.Model.Error;

@RestController
public class BlogController
{
@Autowired
private BlogPostDao blogPostDao;
private static BlogPost blogPost;

@RequestMapping(value="/saveblogpost",method=RequestMethod.POST)
public ResponseEntity<?> saveBlogPost(@RequestBody BlogPost blogPost,HttpSession session)
{
	User user=(User)session.getAttribute("user");
	if(user==null)
	{
		Error error =new Error(3,"UnAutorized user");
		return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
	}
	try{
		blogPost.setPostedOn(new Date());
		blogPost.setCreatedBy(user);
		blogPostDao.saveBlogPost(blogPost);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	catch(Exception e)
	{
		Error error =new Error(2,"Cannot insert blog post details");
		return new ResponseEntity<Error>(error,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
@RequestMapping(value="/listofblogs/{approved}",method=RequestMethod.GET)
public ResponseEntity<?> getAllBlogs(@PathVariable int approved,HttpSession session)
{
	User user=(User)session.getAttribute("user");
	if(user==null)
	{
		Error error=new Error(3,"UnAutorized user");
		return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
	}
	List<BlogPost> blogPosts=blogPostDao.getAllBlogs(approved);
	System.out.println(blogPosts.size());
	return new ResponseEntity<List<BlogPost>>(blogPosts,HttpStatus.OK);
}
@RequestMapping(value="/getblogpost/{id}",method=RequestMethod.GET)
public ResponseEntity<?> getBlogPost(@PathVariable int id,HttpSession session){
	User user=(User)session.getAttribute("user");
	if(user==null)
	{
		Error error=new Error(3,"UnAutorized user");
		return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
	}
	BlogPost blogPost=blogPostDao.getBlogById(id);
	return new ResponseEntity<BlogPost>(blogPost,HttpStatus.OK);
}

@RequestMapping(value="/updateApproval",method=RequestMethod.PUT) 
public ResponseEntity<?> updateBlogPost(@RequestBody BlogPost blogPost,HttpSession session){
	User user=(User)session.getAttribute("user");
	if(user==null)
	{
		
		Error error=new Error(3,"UnAutorized user");
	
		return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
}
blogPostDao.updateBlogPost(blogPost);
return new ResponseEntity<Void>(HttpStatus.OK);

}

@RequestMapping(value="/addblogcomment",method=RequestMethod.POST) 
public ResponseEntity<?> addBlogComment(@RequestBody BlogComment blogComment,HttpSession session){
	User user=(User)session.getAttribute("user");
	if(user==null)
	{
		
		Error error=new Error(3,"UnAutorized user");
	
		return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
}
	try
	{
		blogComment.setComment("user");
		//blogComment.setCommentedOn(new Date());
	
blogPostDao.addBlogComment(blogComment);
return new ResponseEntity<Void>(HttpStatus.OK);
}
	catch(Exception e)
	{
		Error error=new Error(4,"Unable to add comment"+e.getMessage());
		
		return new ResponseEntity<Error>(error,HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
@RequestMapping(value="/getblogcomments/{blogPostId}",method=RequestMethod.GET)
public ResponseEntity<?> blogComments(@PathVariable int blogPostId,HttpSession session){
	User user = (User)session.getAttribute("user");
	if(user == null){
    	Error error = new Error(3,"Unauthorized user, please login");
    	return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
    }
	List<BlogComment> blogComments = blogPostDao.getBlogComments(blogPostId);
	System.out.println(blogComments);
	return new ResponseEntity<List<BlogComment>>(blogComments,HttpStatus.OK);
}
}