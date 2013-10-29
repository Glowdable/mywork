package models;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import javax.persistence.MappedSuperclass;
import javax.persistence.PersistenceException;

import org.hibernate.exception.GenericJDBCException;

import play.Logger;
import play.PlayPlugin;
import play.db.jpa.GenericModel;
import play.db.jpa.JPABase;
import play.exceptions.UnexpectedException;

@MappedSuperclass
public abstract class AbstractModel extends GenericModel {
	// public abstract Object _key();
	public <T extends AbstractModel> T saveWithoutFlush() {
		if (!em().contains(this)) {
			em().persist(this);
			PlayPlugin.postEvent("JPASupport.objectPersisted", this);
		}
		return (T) this;
	}

	public void updateBatch(AbstractModel abModel) {
		Class clazz = abModel.getClass();
		try {
			while (true) {
				if (clazz.equals(JPABase.class)) {
					Field field = clazz.getField("willBeSaved");
					if (Modifier.isTransient(field.getModifiers())) {
						field.setAccessible(true);
						Logger.info("willBeSaved setAccessible");
					}
					Object o = field.get(abModel);
					boolean bField = ((Boolean) o).booleanValue();
					if (!bField) {
						field.set(abModel, true);
					}
					break;
				}
				clazz = clazz.getSuperclass();
			}
		} catch (Exception e) {
			throw new UnexpectedException("During set field willBeSaved", e);
		}
	}
}
