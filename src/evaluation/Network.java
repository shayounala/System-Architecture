package evaluation;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;

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
	public Cache getCache() {
		return cache;
	}

	private ArrayList<RAM> rams;
	public ArrayList<RAM> getRams() {
		return rams;
	}

	public ArrayList<DataStore> getDatastores() {
		return datastores;
	}

	private int ramsorder;
	private ArrayList<DataStore> datastores;
	private int storesorder;

	public Network(int userNumber, int cacheCapacity, int ramCapacity,
			int ramNumber, int datastoreNumber) {
		users = new ArrayList<User>();
		rams = new ArrayList<RAM>();
		datastores = new ArrayList<DataStore>();

		initiateUsers(userNumber);
		initiateCache(cacheCapacity);
		initiateRAMs(ramCapacity, ramNumber);
		initiateDataStores(datastoreNumber);
		
	}

	public void initiateUserInformation() {
		// TODO Auto-generated method stub
		for(int i=0;i<users.size();i++){
			if(Evaluation.PA != 0){
				rams.get(ramsorder).addUser(users.get(i), rams);
				datastores.get(storesorder).addUser(users.get(i), datastores);
				updateorder();
			}else{
				Random random = new Random();
				int randomram = random.nextInt(rams.size());
				rams.get(randomram).addUser(users.get(i), rams);
				
				int randomstore = random.nextInt(datastores.size());
				datastores.get(randomstore).addUser(users.get(i), datastores);
			}
		}
		
		for(int i=0;i<users.size();i++){
			User user = users.get(i);
			for(int j=0;j<user.getFollowers().size();j++){
				User follower = users.get(user.getFollowers().get(j));
				int ramindex = follower.getRams().get(0);
				rams.get(ramindex).addRelation(user, rams.get(user.getRams().get(0)), users);
				
				int storeindex = users.get(users.get(i).getFollowers().get(j)).getDatastores().get(0);
				datastores.get(storeindex).addRelation(user, datastores.get(user.getDatastores().get(0)), users);
			}
		}
		
		for(int i=0;i<users.size();i++){

			if(users.get(i).getRams().size()<= Evaluation.k2+1){
				int left = Evaluation.k2-users.get(i).getRams().size()+1;
				for(int j=0;j<left;j++){
					Random random = new Random();
					int randomram = random.nextInt(rams.size());
					while(rams.get(randomram).containUser(users.get(i).getMainkey())){
						randomram = random.nextInt(rams.size());
					}
					rams.get(randomram).addUserCopy(users.get(i));
				}
			}
			
			if(users.get(i).getDatastores().size()<= Evaluation.k3+1){
				int left = Evaluation.k3-users.get(i).getDatastores().size()+1;
				for(int j=0;j<left;j++){
					System.out.println(users.get(i).getDatastores().size()+"before");
					Random random = new Random();
					int randomstore = random.nextInt(datastores.size());
					while(datastores.get(randomstore).containUser(users.get(i).getMainkey())){
						randomstore = random.nextInt(datastores.size());
					}
					datastores.get(randomstore).addUserCopy(users.get(i));
					System.out.println(users.get(i).getDatastores().size()+"after");
				}
			}
			
			if(users.get(i).getDatastores().size() <= Evaluation.k3){
				System.exit(0);
			}
		}
	}

	private void updateorder() {
		// TODO Auto-generated method stub
		int index = 0;
		int min = rams.get(0).getSpace();
		for(int i=0;i<rams.size();i++){
			if(rams.get(i).getSpace()<min){
				min = rams.get(i).getSpace();
				index = i;
			}
		}
		
		ramsorder = index;
		
		index = 0;
		min = datastores.get(0).getSpace();
		for(int i=0;i<datastores.size();i++){
			if(datastores.get(i).getSpace()<min){
				min = datastores.get(i).getSpace();
				index = i;
			}
		}
		
		storesorder = index;
	}

	private void initiateDataStores(int datastoreNumber) {
		// TODO Auto-generated method stub
		for (int datastoreorder = 0; datastoreorder < datastoreNumber; datastoreorder++) {
			datastores.add(new DataStore(datastoreorder));
		}
		
		storesorder = 0;
	}

	private void initiateRAMs(int ramCapacity, int ramNumber) {
		// TODO Auto-generated method stub
		for (int ramorder = 0; ramorder < ramNumber; ramorder++) {
			rams.add(new RAM(ramorder, ramCapacity));
		}
		ramsorder = 0;
	}

	private void initiateCache(int cacheCapacity) {
		// TODO Auto-generated method stub
		cache = new Cache(cacheCapacity);
	}

	private void initiateUsers(int userNumber) {
		// TODO Auto-generated method stub
		Mongo mongo;
		try {
			mongo = new Mongo("10.3.4.84", 27017);
			mongo.getDB("db").authenticate("cssc", new char[] { '1' });
			DB db = mongo.getDB("db");
			DBCollection anonymouscollection = db
					.getCollection("NewFilterUserInformation");
			ArrayList<ArrayList<Integer>> followerkeys = getFollowerkeys(
					anonymouscollection, "Followers IDs", userNumber);
			ArrayList<ArrayList<Integer>> friendkeys = getFollowerkeys(
					anonymouscollection, "Friends IDs", userNumber);

			
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

	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
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
	private ArrayList<ArrayList<Integer>> getFollowerkeys(DBCollection collection,
			String string, int userNumber) {
		// TODO Auto-generated method stub
		DBCursor cursor = collection.find(new BasicDBObject("Users IDs",
				new BasicDBObject(QueryOperators.EXISTS, true)));

		ArrayList<ArrayList<Long>> followers = new ArrayList<ArrayList<Long>>();
		ArrayList<ArrayList<Integer>> orderedfollowers = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> orderedfollowerkeys = new ArrayList<ArrayList<Integer>>();
		ArrayList<Long> follower = new ArrayList<Long>();
		ArrayList<Integer> followerids = new ArrayList<Integer>();
		ArrayList<Long> users = new ArrayList<Long>();

		//read all the user keys and their follower keys
		for (int i = 0; cursor.hasNext(); i++) {
			BasicDBObject object = (BasicDBObject) cursor.next();
			followers.add((ArrayList<Long>) object.get(string));
			ArrayList<Long> temp = (ArrayList<Long>) object.get("Users IDs");
			users.addAll(temp);
			//System.out.println(i);
		}

		//System.out.println(users);
		//get the right order of users
		int[] numberorder = new int[userNumber];
		numberorder = getNumberOrder(users, userNumber);

		//transfer the followers to orderedfollowers
		for (int i = 0; i < followers.size(); i++) {
			follower = followers.get(i);
			for (int j = 0; j < follower.size(); j++) {
				int followerid = follower.get(j).intValue();
				if (followerid == -1) {
					orderedfollowers.add((ArrayList) followerids.clone());
					followerids.clear();
				} else {
					if(followerid<userNumber){
						followerids.add(followerid);
					}
				}
			}

			follower.clear();
			// System.out.println(i);
		}

		//adjust the order of orderedfollowers by the order of users
		for (int i = 0; i < numberorder.length; i++) {
			orderedfollowerkeys.add(orderedfollowers.get(numberorder[i]));
			if (i != users.get(numberorder[i])) {
				//System.out.println("i: " + i + "  user: "
					//	+ users.get(numberorder[i]));
			}
		}
		// followers.clear();

		System.out.println("Size of User: " + orderedfollowerkeys.size());

		int number = 0;
		int max =0;
		for (ArrayList follow : orderedfollowerkeys) {
			number += follow.size();
			if(follow.size()>max){
				max = follow.size();
			}
		}
		System.out.println("Max of Followers: "+max);
		System.out.println("Size of Followers: " + number);
		return orderedfollowers;
	}

	private int[] getNumberOrder(ArrayList<Long> numbers, int userNumber) {
		// TODO Auto-generated method stub
		int[] numberorder = new int[userNumber];

		for (int i = 0; i < userNumber; i++) {
			numberorder[i] = numbers.indexOf(Long.valueOf(i));
			//System.out.println(i+" "+numberorder[i]);
		}


		return numberorder;
	}

}
