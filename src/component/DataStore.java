package component;

import java.util.ArrayList;
import java.util.Random;

import evaluation.Evaluation;

public class DataStore {

	private int mainkey;
	private ArrayList<Integer> users;

	public int getMainkey() {
		return mainkey;
	}

	public DataStore(int mainkey){
		this.mainkey = mainkey;
		users = new ArrayList<Integer>();
	}

	public void addUser(int mainkey2, ArrayList<DataStore> datastores) {
		// TODO Auto-generated method stub
		this.users.add(mainkey2);
		for(int i=0;i<Evaluation.k3;i++){
			Random random = new Random();
			int index = random.nextInt(datastores.size());
			while(datastores.get(index).containUser(mainkey2)){
				index = random.nextInt(datastores.size());
			}
			datastores.get(index).users.add(mainkey2);
		}
	}

	private boolean containUser(int mainkey2) {
		// TODO Auto-generated method stub
		return users.contains(mainkey2);
	}

	public void addRelation(User user, DataStore datastore, ArrayList<User> users2) {
		// TODO Auto-generated method stub
		if(this.users.contains(user.getMainkey())){
			return;
		}
		
		if(movecost(user, datastore, users2) < 1){
			move(user, datastore, users2);
		}else{
			this.users.add(user.getMainkey());
		}
	}

	private void move(User user, DataStore datastore, ArrayList<User> users2) {
		// TODO Auto-generated method stub
		for(int i=0;i<user.getFriends().size();i++){
			if(!this.containUser(user.getFriends().get(i))){
				addUserCopy(user.getFriends().get(i));
			}
		}
		
		
		
		for(int i=0;i<user.getFriends().size();i++){
			ArrayList<Integer> storefollower = users2.get(user.getFriends().get(i)).getStorefollowers().get(datastore.mainkey);
			if(storefollower.size() == 1 && storefollower.contains(user.getMainkey())){
				removeUserCopy(user.getFriends().get(i), datastore);
			}
		}
		
		ArrayList<Integer> storefollower = user.getStorefollowers().get(datastore.mainkey);
		if(storefollower.size() == 1 && storefollower.contains(user.getMainkey())){
			removeUserMain(user, datastore);
		}
	}
	private void removeUserMain(User user, DataStore datastore) {
		// TODO Auto-generated method stub
		datastore.users.remove(user.getMainkey());
		user.getDatastores().remove(0);
		user.getDatastores().add(0, this.mainkey);
	}

	private void removeUserCopy(Integer mainkey2, DataStore datastore) {
		// TODO Auto-generated method stub
		datastore.users.remove(mainkey2);
	}

	private void addUserCopy(int mainkey2) {
		// TODO Auto-generated method stub
		this.users.add(mainkey2);
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
			ArrayList<Integer> storefollower = users2.get(user.getFriends().get(i)).getStorefollowers().get(datastore.mainkey);
			if(storefollower.size() == 1 && storefollower.contains(user.getMainkey())){
				removenumber++;
			}
		}
		
		ArrayList<Integer> storefollower = user.getStorefollowers().get(datastore.mainkey);
		if(storefollower.size() == 1 && storefollower.contains(user.getMainkey())){
			removenumber++;
		}
		
		return addnumber-removenumber;
	}
}
