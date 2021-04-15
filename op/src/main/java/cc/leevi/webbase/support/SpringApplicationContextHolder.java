package cc.leevi.webbase.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringApplicationContextHolder implements ApplicationContextAware, DisposableBean {

	private static ApplicationContext applicationContext;

	private static boolean isDestroyed = false;

	@Override
	public void destroy() {

		if (applicationContext != null) {
			log.info("destroy() ,displayName:" + applicationContext.getDisplayName());
		}
		this.isDestroyed = true;
		this.applicationContext = null;

	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {

		this.isDestroyed = false;
		this.applicationContext = context;
		log.info("holded applicationContext,displayName:" + applicationContext.getDisplayName());
	}

	public static ApplicationContext getApplicationContext() {
		if (isDestroyed) {
			throw new IllegalStateException("'ApplicationContextHolder already destroyed.");
		}
		if (applicationContext == null) {
			throw new IllegalStateException(
					"'applicationContext' property is null,ApplicationContextHolder not yet init.");
		}
		return applicationContext;
	}

	public static Object getBean(String beanName){
		return getApplicationContext().getBean(beanName);
	}

	public static <T> T getBean(Class<T> clazz){
		return getApplicationContext().getBean(clazz);
	}

	private static Logger log = LoggerFactory.getLogger(SpringApplicationContextHolder.class);


}
