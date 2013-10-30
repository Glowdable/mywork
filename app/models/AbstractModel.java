package models;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.MappedSuperclass;
import javax.persistence.PersistenceException;

import org.hibernate.exception.GenericJDBCException;

import play.Logger;
import play.PlayPlugin;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
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

	public void updateBatch(final List<AbstractModel> lstModel) {
		try {
			for (AbstractModel abModel : lstModel) {
				setWillBeSaved(abModel, true);
			}
		} catch (Exception e) {
			throw new UnexpectedException("During set field willBeSaved", e);
		}

		try {
			em().flush();
		} catch (PersistenceException e) {
			if (e.getCause() instanceof GenericJDBCException) {
				throw new PersistenceException(
						((GenericJDBCException) e.getCause()).getSQL());
			} else {
				throw e;
			}
		}
		
		try {
			for (AbstractModel abModel : lstModel) {
				setWillBeSaved(abModel, false);
			}
		} catch (Exception e) {
			throw new UnexpectedException("During set field willBeSaved", e);
		}

	}

	private void setWillBeSaved(AbstractModel abModel, boolean willBeSaved)
			throws NoSuchFieldException, IllegalAccessException {
		Class clazz = abModel.getClass();
		while (true) {
			if (clazz.equals(JPABase.class)) {
				Field field = clazz.getField("willBeSaved");
				if (Modifier.isTransient(field.getModifiers())) {
					field.setAccessible(true);
					Logger.info("willBeSaved setAccessible");
				}
				/*
				 * Object o = field.get(abModel); boolean bField = ((Boolean)
				 * o).booleanValue();
				 */

				field.set(abModel, willBeSaved);

				break;
			}
			clazz = clazz.getSuperclass();
		}
	}
}
