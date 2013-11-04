/*But sometimes you will need to execute some application logic outside of any HTTP request. 
It can be useful for initialization tasks, 
maintenance tasks or to run long task without blocking the HTTP request execution pool.

Jobs are fully managed by the framework. 
That means that play will manage for you all the database connection stuff, 
JPA entity manager synchronization and transactions management for you. 
To create a job you just need to extends the play.jobs.Job super class.*/


/*Triggering task jobs
You can also trigger a Job at any time to perform a specific task by simply calling now() 
on a Job instance. Then this job will be run immediately in a non blocking way.

public static void encodeVideo(Long videoId) {
    new VideoEncoder(videoId).now();
    renderText("Encoding started");
}
Calling now() on a Job returns a Promise value that you can use to 
retrieve the task result once finished.

Stopping the application
Because you sometimes need to perform some action before the application shutdown, 
Play also provides a @OnApplicationStop annotation.

import play.jobs.*;
 
@OnApplicationStop
public class Bootstrap extends Job {
 
    public void doJob() {
        Fixture.deleteAll();
    }
}*/

package jobs;
//Asynchronous Jobs
import play.Logger;
import play.jobs.Job;
import models.AbstractModel;
import models.DeliveryVouch;
import models.DeliveryVouchSub;
import models.IWorkFlow;
import models.MockWorkFlow;
import models.ModelHelper;
import models.OutboundVouch;
import models.OutboundVouchSub;
import models.Person;
import models.StandingCrop;
import models.Customer;
  
public class MyJob extends Job<String> {
    
	//Here the use of String is just for the example, of course a job can return any object type
    public String doJobWithResult() {
    	Logger.info("doJobWithResult");
    	OutboundVouch out=OutboundVouch.findById((long)1);
    	out.cCusAddress="successs";
    	out.save();
        // execute some application logic here ...
        return "result";
    }
    
/*    @OnApplicationStart
    public class Bootstrap extends Job {
        
        public void doJob() {
            if(Page.count() == 0) {
                new Page("root").save();
                Logger.info("The page tree was empty. A root page has been created.");
            }
        }
        
    }*/
    
//    @Every("1h")
//    public class Scheduled  extends Job {
//        
//        public void doJob() {
//            List<User> newUsers = User.find("newAccount = true").fetch();
//            for(User user : newUsers) {
//                Notifier.sayWelcome(user);
//            }
//        }
//        
//    }
    
}