package br.com.caelum.vraptor.mydvds.dao.mongo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.mydvds.dao.DvdDao;
import br.com.caelum.vraptor.mydvds.model.Dvd;
import br.com.caelum.vraptor.mydvds.model.DvdCopy;
import br.com.caelum.vraptor.mydvds.model.DvdType;
import br.com.caelum.vraptor.mydvds.model.User;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.ObjectId;

@Component
public class MongoDvdDao implements DvdDao {
	private DBCollection collectionDvd;
	private DBCollection collectionCopy;
	private MongoUserDao userDao;

	public MongoDvdDao(MongoSession session) {
		this.collectionDvd = session.getDvdCollection();
		this.collectionCopy = session.getDvdCopyCollection();
	}

	@Override
	public void add(Dvd dvd) {
		if (dvd == null) {
			throw new IllegalArgumentException("Dvd nao deve ser nulo");
		}
		BasicDBObject documentoDvd = createDvdObject(dvd);

		DBObject insert = collectionDvd.insert(documentoDvd);
		Object object = insert.get("_id");
		dvd.setId1(object.toString());
		System.out.println(dvd.getId1());

	}

	private BasicDBObject createDvdObject(Dvd dvd) {

		BasicDBObject documentoDvd = new BasicDBObject();

		documentoDvd.put("title", dvd.getTitle());
		documentoDvd.put("description", dvd.getDescription());
		documentoDvd.put("type", dvd.getType().toString());

		return documentoDvd;
	}

	@Override
	public void add(DvdCopy copy) {
		if (copy.getOwner() == null || copy.getDvd() == null) {
			throw new IllegalArgumentException("Usuario e/ou dvd nulos");
		}

		User user = copy.getOwner();
		Dvd dvd = copy.getDvd();

		BasicDBObject documentoDvdCopy = createCopyObject(dvd, user);

		collectionCopy.insert(documentoDvdCopy);

	}

	private BasicDBObject createCopyObject(Dvd dvd, User user) {

		BasicDBObject documentoDvdCopy = new BasicDBObject();

		documentoDvdCopy.put("owner", user.getLogin());
		documentoDvdCopy.put("dvd_id", dvd.getId1());

		return documentoDvdCopy;
	}

	@Override
	public List<Dvd> searchSimilarTitle(String title) {
		List<Dvd> dvds = new ArrayList<Dvd>();

		BasicDBObject query = new BasicDBObject();
		query.put("title", title);
		DBCursor cursor = this.collectionDvd.find(query);

		if (cursor == null) {
			throw new IllegalArgumentException("Dvd nao encontrado.");
		}
		while (cursor.hasNext()) {

			Dvd dvd = new Dvd();

			BasicDBObject obj = (BasicDBObject) cursor.next();
			ObjectId id = (ObjectId) obj.get("_id");

			dvd.setId1(id.toString());
			dvd.setTitle((String) obj.get("title"));
			dvd.setDescription((String) obj.get("description"));
			DvdType dvdType = DvdType.valueOf(String.valueOf(obj.get("type")));
			dvd.setType(dvdType);
			dvds.add(dvd);
		}
		return dvds;
	}


	private Dvd createDvd(DBObject resultado) {

		Dvd dvd = new Dvd();
		ObjectId _id = (ObjectId) resultado.get("_id");

		dvd.setId1(_id.toString());
		dvd.setTitle((String) resultado.get("title"));
		dvd.setDescription((String) resultado.get("description"));
		DvdType dvdType = DvdType
				.valueOf(String.valueOf(resultado.get("type")));
		dvd.setType(dvdType);

		return dvd;

	}



}
