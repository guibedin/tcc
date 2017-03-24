package guilherme.tcc.services;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class ServicePrevisaoApplication extends Application{

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		classes.add(ServicePrevisao.class);
		return classes;
	}
}
