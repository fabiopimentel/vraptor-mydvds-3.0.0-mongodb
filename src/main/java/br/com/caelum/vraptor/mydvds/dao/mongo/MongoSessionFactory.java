package br.com.caelum.vraptor.mydvds.dao.mongo;

import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.ComponentFactory;

@Component
@ApplicationScoped
public class MongoSessionFactory implements ComponentFactory<MongoSession> {

	@Override
	public MongoSession getInstance() {

		MongoSession session = new MongoSession("mydvdsDB2");
		return session;
	}

}
