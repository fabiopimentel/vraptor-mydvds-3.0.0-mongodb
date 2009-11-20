package br.com.caelum.vraptor.mydvds.dao.mongo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.mydvds.dao.UserDao;
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
public class MongoUserDao implements UserDao {

	private DBCollection collectionUser;
	private DBCollection collectionDvd;
	private DBCollection collectionCopy;
	

	public MongoUserDao(MongoSession session) {
		super();
		this.collectionUser = session.getUserCollection();
		this.collectionDvd = session.getDvdCollection();
		this.collectionCopy = session.getDvdCopyCollection();

	}

	@Override
	public void add(User user) {
		if (user == null) {
			throw new IllegalArgumentException("Usuario nao deve ser nulo");
		}
		if(jaExiste(user.getLogin())){
			throw new IllegalArgumentException("Usuario ja existente");
		}
	
		BasicDBObject documentoUser = createUserObject(user);

		DBObject insert = collectionUser.insert(documentoUser);
		Object object = insert.get("_id");
		user.setId(object.toString());
		System.out.println(user.getId());

	}

	@Override
	public User find(String login, String password) {
		BasicDBObject query = new BasicDBObject();
		query.put("login", login);
		query.put("password", password);

		DBObject resultado = this.collectionUser.findOne(query);

		if (resultado == null) {
			throw new IllegalArgumentException("Login " + login + " e senha "
					+ password + " errados.");

		}
		User user = createUser(resultado);

		return user;

	}

	@Override
	public User find(String login) {
		BasicDBObject query = new BasicDBObject();
		query.put("login", login);

		DBObject resultado = this.collectionUser.findOne(query);

		if (resultado == null) {
			throw new IllegalArgumentException("Login " + login + " errados.");

		}
		User user = createUser(resultado);
		
		return user;
	}
	
	
	public boolean jaExiste(String login) {
		BasicDBObject query = new BasicDBObject();
		query.put("login", login);

		DBObject resultado = this.collectionUser.findOne(query);

		if (resultado == null) {
			return false;
		}
		else{
			return true;
		}
		
	}

	@Override
	public List<User> listAll() {
		DBCursor cursor = collectionUser.find();

		List<User> users = new ArrayList<User>();

		while (cursor.hasNext()) {

			User user = new User();
			BasicDBObject obj = (BasicDBObject) cursor.next();
			ObjectId id = (ObjectId) obj.get("_id");

			user.setId(id.toString());
			user.setName((String) obj.get("name"));
			user.setLogin((String) obj.get("login"));
			user.setPassword((String) obj.get("password"));

			users.add(user);

		}

		return users;
	}

	@Override
	public void refresh(User user) {
		
		if(user ==null) {
			throw new IllegalArgumentException("User nulo");
		}
		BasicDBObject query = new BasicDBObject();
		
		query.put("_id",new ObjectId(user.getId()));
		
		DBObject resultado = this.collectionUser.findOne(query);
		
		if (resultado == null) {
			throw new IllegalArgumentException("Nome " + user.getName() + " errados.");

		}
		createUser(resultado,user);
		procuraDvdDoUsuario(user);
		

	}

	@Override
	public void update(User user) {
		
		if(user==null){
			throw new IllegalArgumentException("Usuário não deve ser nulo");
		}
		DBObject documentoUser= updateUser(user);
		
		BasicDBObject query = new BasicDBObject();
		query.put("_id",new ObjectId(user.getId()));
		
		this.collectionUser.update(query, documentoUser, false, false);
		
	}

	private BasicDBObject updateUser(User user) {
		BasicDBObject documentoUser = new BasicDBObject();

		documentoUser.put("name", user.getName());
		documentoUser.put("login", user.getLogin());
		documentoUser.put("password", user.getPassword());
		documentoUser.put("id", user.getId());
		
		return documentoUser;
	}

	@Override
	public boolean containsUserWithLogin(String login) {
		BasicDBObject query = new BasicDBObject();
		query.put("login", login);

		DBObject resultado = this.collectionUser.findOne(query);

		if (resultado == null) {
			throw new IllegalArgumentException("Login " + login + " errados.");

		}
		User user = createUser(resultado);

		if (user.getLogin() == null) {
			return false;
		} else {
			return true;
		}
	}
	
	private User createUser(DBObject resultado) {

		User user = new User();
		user.setName((String) resultado.get("name"));
		user.setLogin((String) resultado.get("login"));
		user.setPassword((String) resultado.get("password"));

		ObjectId _id = (ObjectId) resultado.get("_id");
		user.setId(_id.toString());
		return user;
	}
	private void createUser(DBObject resultado,User user) {


		user.setName((String) resultado.get("name"));
		user.setLogin((String) resultado.get("login"));
		user.setPassword((String) resultado.get("password"));

		ObjectId _id = (ObjectId) resultado.get("_id");
		user.setId(_id.toString());

	}
	
	private BasicDBObject createUserObject(User user) {

		BasicDBObject documentoUser = new BasicDBObject();

		documentoUser.put("name", user.getName());
		documentoUser.put("login", user.getLogin());
		documentoUser.put("password", user.getPassword());

		return documentoUser;
	}

	public void procuraDvdDoUsuario(User user) {

		Set<DvdCopy> dvdsCopy = new HashSet<DvdCopy>();

		BasicDBObject query = new BasicDBObject();
		query.put("owner", user.getLogin());

		DBCursor cursor = this.collectionCopy.find(query);
		while (cursor.hasNext()) {
			DBObject documento = cursor.next();

			Dvd dvd = new Dvd();

			dvd.setId1((String) documento.get("dvd_id"));

			BasicDBObject query2 = new BasicDBObject();
			query2.put("_id", new ObjectId(dvd.getId1()));

			System.out.println(query2);
			DBObject resultado = this.collectionDvd.findOne(query2);
			if (resultado == null) {
				throw new IllegalArgumentException("Resultado inválido");
			}

			DvdCopy dvdCopyRetornado = createDvdCopy(resultado);

			dvdsCopy.add(dvdCopyRetornado);


		}
		user.setCopies(dvdsCopy);

	}
	private DvdCopy createDvdCopy(DBObject resultado) {

		DvdCopy dvdCopy = new DvdCopy();

		Dvd dvd = new Dvd();
		ObjectId _id = (ObjectId) resultado.get("_id");

		dvd.setId1(_id.toString());
		dvd.setTitle((String) resultado.get("title"));
		dvd.setDescription((String) resultado.get("description"));
		DvdType dvdType = DvdType
				.valueOf(String.valueOf(resultado.get("type")));
		dvd.setType(dvdType);

		dvdCopy.setDvd(dvd);

		return dvdCopy;

	}

}
