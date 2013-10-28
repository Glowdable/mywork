package models;

import java.util.ArrayList;

import javax.persistence.MappedSuperclass;
import javax.persistence.PersistenceException;

import org.hibernate.exception.GenericJDBCException;

import play.PlayPlugin;
import play.db.jpa.GenericModel;
import play.db.jpa.JPABase;

@MappedSuperclass
public abstract class AbstractModel extends GenericModel {
//	    public abstract Object _key();
    public <T extends AbstractModel> T saveWithoutFlush() {
    	   if (!em().contains(this)) {
               em().persist(this);
               PlayPlugin.postEvent("JPASupport.objectPersisted", this);
           }           
           return (T) this;
    }
}
