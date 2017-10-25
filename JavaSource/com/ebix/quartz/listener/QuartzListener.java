package com.ebix.quartz.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzListener implements ServletContextListener{
	Scheduler scheduler = null;
	Logger log = Logger.getLogger(QuartzListener.class);
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		log.info("Context Destroyed");
        try 
        {
                scheduler.shutdown();
        } 
        catch (SchedulerException e) 
        {
               log.error(e.getMessage());
        }
	}

	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
		} catch (SchedulerException e) {
			// TODO Auto-generated 
			log.error(e.getMessage());
		}
	}

}
