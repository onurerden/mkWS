/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crons;

/**
 *
 * @author onurerden
 */
// your package
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
@WebListener
public class TomcatListenerClass implements ServletContextListener {

private ScheduledExecutorService scheduler;

@Override
public void contextInitialized(ServletContextEvent event) {
    scheduler = Executors.newSingleThreadScheduledExecutor();
   // scheduler.scheduleAtFixedRate(new DailyJob(), 0, 1, TimeUnit.DAYS);
    scheduler.scheduleAtFixedRate(new HourlyJob(), 0, 1, TimeUnit.HOURS);
   scheduler.scheduleAtFixedRate(new MinJob(), 0, 1, TimeUnit.MINUTES);
   // scheduler.scheduleAtFixedRate(new SecJob(), 0, 15, TimeUnit.SECONDS);
}

@Override
public void contextDestroyed(ServletContextEvent event) {
    scheduler.shutdownNow();
 }

}
