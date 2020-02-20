package ai.salesken.onboarding.injector;


import org.glassfish.hk2.utilities.binding.AbstractBinder;

import ai.salesken.onboarding.dao.FileUploadDao;
import ai.salesken.onboarding.dao.UserDao;
import ai.salesken.onboarding.dao.impl.FileUploadDaoImpl;
import ai.salesken.onboarding.dao.impl.UserDaoImpl;

 
public class OnboardingBinder extends AbstractBinder {
    @Override
    protected void configure() {
    	 bind(UserDaoImpl.class).to(UserDao.class);
    	 bind(FileUploadDaoImpl.class).to(FileUploadDao.class);
    }
}
