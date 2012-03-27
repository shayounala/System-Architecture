package evaluation;

import java.net.UnknownHostException;
import java.util.ArrayList;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.QueryOperators;

import component.Cache;
import component.DataStore;
import component.RAM;
import component.User;

/**
 * @author Chance Huang Initiate the Users with mainkey
 * 
 */
public class Network {

	private ArrayList<User> users;

	public ArrayList<User> getUsers() {
		return users;
	}

	private Cache cache;
	private ArrayList<RAM> rams;
	private ArrayList<DataStore> datastores;

	public Network(int userNumber, int cacheCapacity, int ramCapacity,
			int ramNumber, int datastoreCapacity, int datastoreNumber) {
		users = new ArrayList<User>();
		rams = new ArrayList<RAM>();
		datastores = new ArrayList<DataStore>();

		initiateUsers(userNumber);
		initiateCache(cacheCapacity);
		initiateRAMs(ramCapacity, ramNumber);
		initiateDataStores(datastoreCapacity, datastoreNumber);

	}

	private void initiateDataStores(int datastoreCapacity, int datastoreNumber) {
		// TODO Auto-generated method stub
		for (int datastoreorder = 0; datastoreorder < datastoreNumber; datastoreorder++) {
			datastores.add(new DataStore(datastoreCapacity));
		}
	}

	private void initiateRAMs(int ramCapacity, int ramNumber) {
		// TODO Auto-generated method stub
		for (int ramorder = 0; ramorder < ramNumber; ramorder++) {
			rams.add(new RAM(ramCapacity));
		}
	}

	private void initiateCache(int cacheCapacity) {
		// TODO Auto-generated method stub
		cache = new Cache(cacheCapacity);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initiateUsers(int userNumber) {
		// TODO Auto-generated method stub
		Mongo mongo;
		try {
			mongo = new Mongo("10.3.4.84", 27017);
			mongo.getDB("db").authenticate("cssc", new char[] { '1' });
			DB db = mongo.getDB("db");
			DBCollection anonymouscollection = db
					.getCollection("AnonymousUserInformation");
			ArrayList<ArrayList> followerkeys = getFollowerkeys(
					anonymouscollection, "Followers IDs");
			ArrayList<ArrayList> friendkeys = getFriendkeys(followerkeys);

			for (int userorder = 0; userorder < userNumber; userorder++) {
				users.add(new User(userorder, followerkeys.get(userorder),
						friendkeys.get(userorder)));
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ArrayList<ArrayList> getFriendkeys(ArrayList<ArrayList> followerkeys) {
		// TODO Auto-generated method stub
		ArrayList<ArrayList> friendkeys = new ArrayList<ArrayList>();

		for (int userorder = 0; userorder < followerkeys.size(); userorder++) {
			friendkeys.add(new ArrayList<Integer>());
		}

		for (int userorder = 0; userorder < followerkeys.size(); userorder++) {
			ArrayList<Integer> followerkey = followerkeys.get(userorder);
			for (int followerorder = 0; followerorder < followerkey.size(); followerorder++) {
				int follower = followerkey.get(followerorder);
				friendkeys.get(follower).add(userorder);
			}
		}
		return friendkeys;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ArrayList<ArrayList> getFollowerkeys(DBCollection collection,
			String string) {
		// TODO Auto-generated method stub
		DBCursor cursor = collection.find(new BasicDBObject("User IDs",
				new BasicDBObject(QueryOperators.EXISTS, true)));

		ArrayList<ArrayList> followers = new ArrayList<ArrayList>();
		ArrayList<ArrayList> orderedfollowers = new ArrayList<ArrayList>();
		ArrayList<ArrayList> orderedfollowerkeys = new ArrayList<ArrayList>();
		ArrayList<Integer> follower = new ArrayList<Integer>();
		ArrayList<Integer> followerids = new ArrayList<Integer>();
		ArrayList<Integer> users = new ArrayList<Integer>();

		//read all the user keys and their follower keys
		for (int i = 0; cursor.hasNext(); i++) {
			BasicDBObject object = (BasicDBObject) cursor.next();
			followers.add((ArrayList<Integer>) object.get(string));
			ArrayList<Integer> temp = (ArrayList) object.get("User IDs");
			users.addAll(temp);
		}

		//get the right order of users
		int[] numberorder = new int[users.size()];
		numberorder = getNumberOrder(users);

		//transfer the followers to orderedfollowers
		for (int i = 0; i < followers.size(); i++) {
			follower = followers.get(i);
			for (int j = 0; j < follower.size(); j++) {
				int followerid = follower.get(j);
				if (followerid == -1) {
					orderedfollowers.add((ArrayList) followerids.clone());
					followerids.clear();
				} else {
					followerids.add(followerid);
				}
			}

			follower.clear();
			// System.out.println(i);
		}

		//adjust the order of orderedfollowers by the order of users
		for (int i = 0; i < orderedfollowers.size(); i++) {
			orderedfollowerkeys.add(orderedfollowers.get(numberorder[i]));
			if (i != users.get(numberorder[i])) {
				System.out.println("i: " + i + "  user: "
						+ users.get(numberorder[i]));
			}
		}
		// followers.clear();

		System.out.println("Size of User: " + orderedfollowers.size());

		int number = 0;
		for (ArrayList follow : orderedfollowers) {
			number += follow.size();
		}
		System.out.println("Size of Followers: " + number);
		return orderedfollowers;
	}

	private int[] getNumberOrder(ArrayList<Integer> numbers) {
		// TODO Auto-generated method stub
		int[] numberorder = new int[numbers.size()];

		for (int i = 0; i < numbers.size(); i++) {
			numberorder[i] = numbers.indexOf(i);
		}

		return numberorder;
	}

}
