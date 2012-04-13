package component;

import java.util.ArrayList;
import java.util.Random;

import evaluation.Evaluation;

public class DataStore {

	private int mainkey;
	private ArrayList<Integer> users;
	public ArrayList<Integer> getUsers() {
		return users;
	}

	private ArrayList<Integer> messages;

	public int getMainkey() {
		return mainkey;
	}
	
	private int relationadd, relationmove;
	public int getRelationadd() {
		return relationadd;
	}

	public int getRelationmove() {
		return relationmove;
	}

	public int getDataadd() {
		return dataadd;
	}

	public int getDatamove() {
		return datamove;
	}

	private int dataadd, datamove;

	public DataStore(int mainkey){
		this.mainkey = mainkey;
		users = new ArrayList<Integer>();
		messages = new ArrayList<Integer>();
		relationadd = 0;
		relationmove =0;
		dataadd = 0;
		datamove = 0;
	}

	public void addUser(User user, ArrayList<DataStore> datastores) {
		// TODO Auto-generated method stub
		addUserMain(user);
		if(Evaluation.PA == 0){
			for(int i=0;i<Evaluation.k3;i++){
				Random random = new Random();
				int index = random.nextInt(datastores.size());
				while(datastores.get(index).containUser(user.getMainkey())){
					index = random.nextInt(datastores.size());
				}
				datastores.get(index).addUserCopy(user);
			}
		}
	}



	public boolean containUser(int mainkey2) {
		// TODO Auto-generated method stub
		return users.contains(mainkey2);
	}

	public void addRelation(User user, DataStore datastore, ArrayList<User> users2) {
		// TODO Auto-generated method stub
		relationadd++;
		if(this.users.contains(user.getMainkey())){
			return;
		}
		
		if(movecost(user, datastore, users2) < 1 && Evaluation.PA != 0){
			relationmove++;
			move(user, datastore, users2);
		}else{
			addUserCopy(user);
		}
	}

	private void move(User user, DataStore datastore, ArrayList<User> users2) {
		// TODO Auto-generated method stub
		for(int i=0;i<user.getFriends().size();i++){
			if(!this.containUser(user.getFriends().get(i))){
				addUserCopy(users2.get(user.getFriends().get(i)));
			}
		}
		
		
		
		for(int i=0;i<user.getFriends().size();i++){
			User user1 = users2.get(user.getFriends().get(i));			
			if(isRemove(datastore, user1)){
				datastore.removeUserCopy(user1);
			}
		}
		
		if(isRemove(datastore, user)){
			datastore.removeUserMain(user);
			addUserMain(user);
		}
	}
	
	private boolean isRemove(DataStore datastore,
			User user1) {
		// TODO Auto-generated method stub
		if(!datastore.users.contains(user1.getMainkey())){
			return false;
		}
		
		for(int i=0;i<user1.getFollowers().size();i++){
			if(datastore.containUser(user1.getFollowers().get(i))){
				return false;
			}
		}
		if(user1.getDatastores().indexOf(mainkey) == 0){
			return false;
		}
		if(user1.getDatastores().size()>Evaluation.k3){
			return true;
		}else{
			return false;
		}
	}

	private void addUserMain(User user) {
		// TODO Auto-generated method stub
		if(containUser(user.getMainkey())){
			System.out.println("Main");
			System.exit(0);
		}
		user.getDatastores().add(0, this.mainkey);
		users.add(user.getMainkey());
		int usermessages = 10;
		if(user.getDatastores().size() != 0){
			DataStore store = Evaluation.network.getDatastores().get(user.getDatastores().get(0));
			int index = store.users.indexOf(user.getMainkey());
			if(store.messages.size() > index){
				usermessages = store.messages.get(index);
			}
		}
		
		messages.add(usermessages);
	}
	
	private void removeUserMain(User user) {
		// TODO Auto-generated method stub
		int index = users.indexOf(user.getMainkey());
		users.remove(index);
		user.getDatastores().remove(0);
		messages.remove(index);
	}

	private void removeUserCopy(User user) {
		// TODO Auto-generated method stub
		int index = users.indexOf(user.getMainkey());
		users.remove(index);
		user.getDatastores().remove(Integer.valueOf(mainkey));
		messages.remove(index);
	}

	public void addUserCopy(User user) {
		// TODO Auto-generated method stub
		if(containUser(user.getMainkey())){
			System.out.println("Copy");
			System.exit(0);
		}
		users.add(user.getMainkey());
		user.getDatastores().add(this.mainkey);
		int usermessages = 0;
		if(user.getDatastores().size() != 0){
			DataStore store = Evaluation.network.getDatastores().get(user.getDatastores().get(0));
			int index = store.users.indexOf(user.getMainkey());
			usermessages = store.messages.get(index);
		}
		
		messages.add(usermessages);
	}

	private int movecost(User user, DataStore datastore, ArrayList<User> users2) {
		// TODO Auto-generated method stub
		int addnumber = 0;
		for(int i=0;i<user.getFriends().size();i++){
			if(!this.containUser(user.getFriends().get(i))){
				addnumber++;
			}
		}
		
		
		
		int removenumber = 0;
		for(int i=0;i<user.getFriends().size();i++){
			User user1 = users2.get(user.getFriends().get(i));
			if(isRemove(datastore, user1)){
				removenumber++;
			}
		}
		
		if(isRemove(datastore, user)){
			removenumber++;
		}
		
		return addnumber-removenumber;
	}

	public void addData(int mainkey2, int messageNumber, int order) {
		// TODO Auto-generated method stub
		dataadd++;
		if(Evaluation.PA == 2){
			adjust(mainkey2);
		}
		
		User user = Evaluation.network.getUsers().get(mainkey2);
		for(int i=0;i<user.getDatastores().size();i++){
			DataStore store = Evaluation.network.getDatastores().get(user.getDatastores().get(i));
			store.addData(mainkey2);
		}
	}

	private void addData(int mainkey2) {
		// TODO Auto-generated method stub
		int index = users.indexOf(mainkey2);
		
		messages.set(index, messages.get(index)+1);
	}

	private void adjust(int mainkey2) {
		// TODO Auto-generated method stub
		User user = Evaluation.network.getUsers().get(mainkey2);
		if(user.getDatastores().size()>Evaluation.k3+1){
			for(int i=0;i<user.getFollowers().size();i++){
				User user1 = Evaluation.network.getUsers().get(user.getFollowers().get(i));
				DataStore store = Evaluation.network.getDatastores().get(user1.getDatastores().get(0));
				if(movecosts(user1, store, Evaluation.network.getUsers()) < 0){
					datamove++;
					move(user, store, Evaluation.network.getUsers());
				}
			}
		}
	}

	private int movecosts(User user1, DataStore store, ArrayList<User> users2) {
		// TODO Auto-generated method stub
		int addnumber = 0;
		for(int i=0;i<user1.getFriends().size();i++){
			if(!this.containUser(user1.getFriends().get(i))){
				DataStore store1 = Evaluation.network.getDatastores().get(Evaluation.network.getUsers().get(user1.getFriends().get(i)).getDatastores().get(0));
				int index = store1.users.indexOf(user1.getFriends().get(i));
				addnumber += store1.messages.get(index);
			}
		}
		
		
		
		int removenumber = 0;
		for(int i=0;i<user1.getFriends().size();i++){
			User user2 = users2.get(user1.getFriends().get(i));
			if(isRemove(store, user2)){
				int index = store.users.indexOf(user1.getFriends().get(i));
				removenumber += store.messages.get(index);
			}
		}
		
		if(isRemove(store, user1)){
			int index = store.users.indexOf(user1.getMainkey());
			removenumber += store.messages.get(index);
		}
		
		return addnumber-removenumber;
	}

	public void search(User user, int message) {
		// TODO Auto-generated method stub
		
	}
	
	public int getSpace() {
		// TODO Auto-generated method stub
		int space = 0;
		for(int i=0;i<messages.size();i++){
			space += messages.get(i);
		}
		return space;
	}
}
