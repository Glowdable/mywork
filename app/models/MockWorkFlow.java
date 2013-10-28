package models;

import java.util.List;
import java.util.Queue;

public class MockWorkFlow {
	private IWorkFlow workFolw;
	private AbstractModel abstractModel;

	public void setWorkFlow(IWorkFlow workFolw) {
		this.workFolw = workFolw;
	}

	public void setAbstractModel(AbstractModel abstractModel) {
		this.abstractModel = abstractModel;
	}

	public Boolean Audit(Queue<Person> personQue) {
		
		if(null==workFolw){
			 throw new RuntimeException("IWorkFlow not yet maintained");
		}
		
		if(null==abstractModel){
			throw new RuntimeException("AbstractModel not yet maintained");
		}
		Person person;
		while ((person = personQue.poll()) != null) {
			System.out.println("Person:"+person.getName());
			Boolean bWork=workFolw.Audit(person);
			System.out.println("Boolean:"+bWork);
			if (bWork) {

			} else {
				return Boolean.FALSE;
			}

		}
		return Boolean.TRUE;

	}
}
