package ai.salesken.onboarding.injector;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import ai.salesken.onboarding.dao.FileUploadDao;
import ai.salesken.onboarding.dao.OrganizationDao;
import ai.salesken.onboarding.dao.UserDao;
import ai.salesken.onboarding.dao.impl.FileUploadDaoImpl;
import ai.salesken.onboarding.dao.impl.OrganizationDaoImpl;
import ai.salesken.onboarding.dao.impl.UserDaoImpl;
import ai.salesken.onboarding.utils.Validator;
import ai.salesken.onboarding.utils.impl.ValidatorImpl;

public class OnboardingBinder extends AbstractBinder {
	@Override
	protected void configure() {
		bind(UserDaoImpl.class).to(UserDao.class);
		bind(FileUploadDaoImpl.class).to(FileUploadDao.class);
		bind(OrganizationDaoImpl.class).to(OrganizationDao.class);
		bind(ValidatorImpl.class).to(Validator.class);
	}
}
