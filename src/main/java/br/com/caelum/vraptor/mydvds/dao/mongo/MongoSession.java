package br.com.caelum.vraptor.mydvds.dao.mongo;

import java.net.UnknownHostException;
import java.util.Set;

import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class MongoSession {

	private static final String USERS = "users";
	private static final String DVDS = "dvds";
	private static final String DVD_COPY = "dvd_copy";
	
	private final Mongo db;

	public MongoSession(String nomeDoBanco) {
		try {
			db = new Mongo("localhost", 27017, nomeDoBanco);
		} catch (UnknownHostException e) {
			throw new IllegalArgumentException(nomeDoBanco);
		} catch (MongoException e) {
			throw new IllegalArgumentException(nomeDoBanco);
		}
	}

	public void getCollections() {

		Set<String> colls = db.getCollectionNames();

		for (String s : colls) {
			System.out.println(s);
		}

	}
	
	public DBCollection getDvdCopyCollection() {
		return this.conectaComColecao(DVD_COPY);
	}
	
	public DBCollection getDvdCollection() {
		return this.conectaComColecao(DVDS);
	}
	
	public DBCollection getUserCollection() {
		return this.conectaComColecao(USERS);
	}

	public DBCollection conectaComColecao(String nomeDaColecao) {
		DBCollection coll = db.getCollection(nomeDaColecao);
		return coll;
	}

}
