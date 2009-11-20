 package br.com.caelum.vraptor.mydvds.dao;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

public class MongoDvdDaoTest {

	private MongoDvdDao dvdDao;
	private MongoUserDao userDao;
	private User user;
	private Dvd dvd;
	private DvdCopy dvdCopy;
	private MongoSession session;

	@Before
	public void before() {
		session = new MongoSession("mydvdsDB");
		
		dvdDao = new MongoDvdDao(session);
		userDao = new MongoUserDao(session);
				
		dvdCopy = new DvdCopy();
		
		user = new User();
		user.setName("Fabio");
		user.setLogin("fabio");
		userDao.add(user);
		
		dvd = new Dvd();
		dvd.setTitle("Besouro");
		dvd.setDescription("Filme brasileiro");
		dvd.setType(DvdType.VIDEO);
		
		dvdCopy.setDvd(dvd);
		dvdCopy.setOwner(user);		
		
		dvdDao.add(dvd);
		
		Set<DvdCopy> dvds = new HashSet<DvdCopy>();
		
		dvds.add(dvdCopy);
		
		user.setCopies(dvds);
		
		dvdDao.add(dvdCopy);
		
	}
	@After
	public void after(){
		
		session.getDvdCollection().drop();
		session.getDvdCopyCollection().drop();
	}

	@Test
	public void testaAdiciona() {
		//dvdDao.add(dvd);
		List<Dvd> dvds= dvdDao.searchSimilarTitle(dvd.getTitle());

		compara(dvds);

	}

	private void compara(List<Dvd> dvds) {
		Assert.assertEquals("Besouro", dvds.get(0).getTitle());
		Assert.assertEquals("Filme brasileiro", dvds.get(0).getDescription());
		Assert.assertEquals(DvdType.VIDEO, dvds.get(0).getType());

	}

	@Test
	public void testaSearchSimilarTittle(){
		List<Dvd> dvds = dvdDao.searchSimilarTitle(dvd.getTitle());
		compara(dvds);
	}


}
