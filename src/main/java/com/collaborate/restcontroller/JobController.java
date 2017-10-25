package com.collaborate.restcontroller;


import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.collaborate.DAO.JobDao;
import com.collaborate.Model.Job;
import com.collaborate.Model.User;
import com.collaborate.Model.Error;

@RestController
public class JobController 
{
	@Autowired
	private JobDao jobDao;
	@RequestMapping(value="/savejob", method=RequestMethod.POST)
	public ResponseEntity<?> saveJob(@RequestBody Job job,HttpSession session)
	{
		User user=(User)session.getAttribute("user");
		if(user==null)
		{
			Error error=new Error(3,"unAuthorized user");
			return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
		}
		try
		{
			if(user.getRole().equals("Admin"))
			{
				job.setPostedOn(new Date());
				jobDao.saveJob(job);
				return new ResponseEntity<Void>(HttpStatus.OK);
			}
			else
			{
				Error error=new Error(4,"Access Denied..");
				return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
			}
		}
		catch (Exception e) 
		{
			Error error=new Error(1,"unable to insert job details...."+ e.getMessage());
			return new ResponseEntity<Error>(error,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/getalljobs",method=RequestMethod.GET)
	public ResponseEntity<?> getAllJobs(HttpSession session)
	{
		User user =(User)session.getAttribute("user");
		if(user==null)
		{
			Error error=new Error(3,"UnAuthorized user");
			return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
		}
		List<Job> jobs=jobDao.getAllJobs();
		return new ResponseEntity<List<Job>>(jobs,HttpStatus.OK);
	}
	

@RequestMapping(value="/getjobbyid/{id}",method=RequestMethod.GET)
public ResponseEntity<?> getJobById(@PathVariable int id,HttpSession session){
    User user=(User)session.getAttribute("user");
    if(user==null){
         Error error=new Error(3,"UnAuthorized user");
            return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
    }
    Job job=jobDao.getJobById(id);
    return new ResponseEntity<Job>(job,HttpStatus.OK);
}
}