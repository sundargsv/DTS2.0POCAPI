package com.dts.DAO;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLDecoder;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;

public class PersisteanceManager 
{
	public SessionFactory getOpenSession()
	{
		try{
			String path= System.getProperty("catalina.base") ;
			String pathNew = this.getClass().getClassLoader().getResource("").getPath();
			String fullPath = URLDecoder.decode(pathNew, "UTF-8");
			String pathArr[] = fullPath.split("/WEB-INF/classes/");
			System.out.println(fullPath);
			System.out.println(pathArr[0]);
			Properties property= new Properties();
			property.load(new FileInputStream(path+File.separator+"db.properties") );
		Configuration config= new Configuration().configure("hibernateNew.cfg.xml").addProperties(property);
		System.out.println("Configuration done");
		SessionFactory sessionFactory= config.buildSessionFactory();
		//Session session= sessionFactory.openSession();
		
		return sessionFactory;
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	public boolean closeSession(Session session,SessionFactory sessionFactory)
	{
		try{
		session.close();
		sessionFactory.close();
		return true;
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	public Object InserDetails(Object object,Session session)
	{
		Object obj=null;
		try{
			//Session session= getOpenSession();
		//session.beginTransaction();
		//String qyery="from com.zinnia.model.Customer ";
		//org.hibernate.Query query= session.createQuery(qyery);
		//cust= query.list();
		
		obj=session.merge(object);
		//session.getTransaction().commit();
		//session.close();
		//sessionFactory.close();
		//return cust;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		//return cust;
		return obj;
	}
	
	@SuppressWarnings("unchecked")
	public List<?> executeCustomeQuery(Session session,String query)
	{
		List<?> obj=null;
		try{
			
			Query que= session.createQuery(query);
			obj=que.list();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return obj;
	}
	// Pass the start row and no. of rows  
	@SuppressWarnings("unchecked")
	public List<?> featchResultWithLimit(Session session,Object className,String startRows,String limit)
	{
		List<?> obj=null;
		try{
			Criteria criteria= session.createCriteria(className.getClass());
			criteria.setFirstResult(Integer.parseInt(startRows));
			criteria.setMaxResults(Integer.parseInt(limit));
			//Query que= session.createQuery(query);
			obj=criteria.list();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return obj;
	}
	
	@SuppressWarnings("unchecked")
	public int featchTotalNoOfRows(Session session,Object className)
	{
		List<?> obj=null;
		try{
			Criteria criteria= session.createCriteria(className.getClass());
			criteria.setProjection(Projections.rowCount());
			Integer count = (Integer)criteria.uniqueResult();
			return count;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
}
