package br.com.caelum.vraptor.mydvds.dao;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import br.com.caelum.vraptor.mydvds.dao.mongo.MongoDvdDao;
import br.com.caelum.vraptor.mydvds.dao.mongo.MongoSession;
import br.com.caelum.vraptor.mydvds.dao.mongo.MongoUserDao;
import br.com.caelum.vraptor.mydvds.model.Dvd;
import br.com.caelum.vraptor.mydvds.model.DvdCopy;
import br.com.caelum.vraptor.mydvds.model.DvdType;
import br.com.caelum.vraptor.mydvds.model.User;

public class MongoUserDaoTest {

	private MongoUserDao userDAO;
	private MongoDvdDao dvdDao;
	private User user;
	private MongoSession session;
	private Dvd dvd;
	
	@Before
	public void before() {
		session = new MongoSession("mydvdsDB");
		userDAO = new MongoUserDao(session);
		dvdDao = new MongoDvdDao(session);
		

		user = new User();
		user.setName("Fabio");
		user.setPassword("fabio123");
		user.setLogin("fabio");
		
		userDAO.add(user);
		
		dvd = new Dvd();
		dvd.setTitle("Procurando Nemo");
		dvd.setType(DvdType.VIDEO);
		dvd.setDescription("Filme infantil");
		
		DvdCopy dvdCopy = new DvdCopy();
		dvdCopy.setDvd(dvd);
		dvdCopy.setOwner(user);
		
		
		dvdDao.add(dvd);
		dvdDao.add(dvdCopy);
	
	}

	@After
	public void after() {
		session.getUserCollection().drop();
		session.getDvdCollection().drop();
		session.getDvdCopyCollection().drop();
	}

	@Test
	public void testaAdicionaUser() {
		// userDAO.add(user);
		User retornado = userDAO.find(user.getLogin());
		compara(retornado);

	}

	private void compara(User retornado) {
		Assert.assertEquals("fabio", retornado.getLogin());
		Assert.assertEquals("Fabio", retornado.getName());
		Assert.assertEquals("fabio123", retornado.getPassword());
	}

	@Test
	public void testaBuscaPorLoginESenha() {
		// userDAO.add(user);
		User retornado = userDAO.find(user.getLogin(), user.getPassword());

		compara(retornado);
		 
	}

	@Test
	public void testaBuscaPorLogin() {
		User retornado = userDAO.find(user.getLogin());
		compara(retornado);

	}

	@Test
	public void testaListaTodosUsuarios() {
		List<User> users= userDAO.listAll();
		for(int i=0 ; i< users.size();i++) {
			//System.out.println(i);
			Assert.assertEquals("fabio",users.get(i).getLogin());
			Assert.assertEquals("Fabio",users.get(i).getName());
			Assert.assertEquals("fabio123",users.get(i).getPassword());
		}
	}
	
	
	@Test
	public void procuraDvdsDoUsuario(){
		userDAO.procuraDvdDoUsuario(user);
		for (DvdCopy dvdCopy : user.getCopies()) {
			Assert.assertEquals("Procurando Nemo",dvdCopy.getDvd().getTitle());
			Assert.assertEquals("Filme infantil", dvdCopy.getDvd().getDescription());
			Assert.assertEquals(DvdType.VIDEO, dvdCopy.getDvd().getType());
		}
		
	}
	
	
	
	@Test
	public void testaRefresh() {
		userDAO.refresh(user);
		User retornado = userDAO.find(user.getLogin());
		compara(retornado);
		
	}

	@Test(expected=IllegalArgumentException.class)
	public void testaAdicionaUsuarioJaExistente() {
		User novo = new User();
		novo.setLogin("fabio");
		userDAO.add(novo);
		
		
	}
	
}
