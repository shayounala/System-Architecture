package component;

import java.util.ArrayList;
import java.util.Random;

import evaluation.Evaluation;

public class RAM {

	private int mainkey;

	public int getMainkey() {
		return mainkey;
	}

	private ArrayList<Integer> users;
	public RAM(int mainkey){
		this.mainkey = mainkey;
		users = new ArrayList<Integer>();
	}

	public void addUser(int mainkey2, ArrayList<RAM> rams) {
		// TODO Auto-generated method stub
		this.users.add(mainkey2);
		for(int i=0;i<Evaluation.k2;i++){
			Random random = new Random();
			int index = random.nextInt(rams.size());
			while(rams.get(index).containUser(mainkey2)){
				index = random.nextInt(rams.size());
			}
			rams.get(index).users.add(mainkey2);
		}
	}

	private boolean containUser(int mainkey2) {
		// TODO Auto-generated method stub
		
		return users.contains(mainkey2);
	}

	public void addRelation(User user, RAM ram, ArrayList<User> users2) {
		// TODO Auto-generated method stub
		if(this.users.contains(user.getMainkey())){
			return;
		}
		
		if(movecost(user, ram, users2) < 1){
			move(user, ram, users2);
		}else{
			this.users.add(user.getMainkey());
		}
	}

	private void move(User user, RAM ram, ArrayList<User> users2) {
		// TODO Auto-generated method stub
		for(int i=0;i<user.getFriends().size();i++){
			if(!this.containUser(user.getFriends().get(i))){
				addUserCopy(user.getFriends().get(i));
			}
		}
		
		
		
		for(int i=0;i<user.getFriends().size();i++){
			ArrayList<Integer> ramfollower = users2.get(user.getFriends().get(i)).getRamfollowers().get(ram.mainkey);
			if(ramfollower.size() == 1 && ramfollower.contains(user.getMainkey())){
				removeUserCopy(user.getFriends().get(i), ram);
			}
		}
		
		ArrayList<Integer> ramfollower = user.getRamfollowers().get(ram.mainkey);
		if(ramfollower.size() == 1 && ramfollower.contains(user.getMainkey())){
			removeUserMain(user, ram);
		}
	}

	private void removeUserMain(User user, RAM ram) {
		// TODO Auto-generated method stub
		ram.users.remove(user.getMainkey());
		user.getRams().remove(0);
		user.getRams().add(0, this.mainkey);
		
	}

	private void removeUserCopy(int mainkey2, RAM ram) {
		// TODO Auto-generated method stub
		ram.users.remove(mainkey2);
	}

	private void addUserCopy(int mainkey2) {
		// TODO Auto-generated method stub
		this.users.add(mainkey2);
	}

	private int movecost(User user, RAM ram, ArrayList<User> users2) {
		// TODO Auto-generated method stub
		int addnumber = 0;
		for(int i=0;i<user.getFriends().size();i++){
			if(!this.containUser(user.getFriends().get(i))){
				addnumber++;
			}
		}
		
		
		
		int removenumber = 0;
		for(int i=0;i<user.getFriends().size();i++){
			ArrayList<Integer> ramfollower = users2.get(user.getFriends().get(i)).getRamfollowers().get(ram.mainkey);
			if(ramfollower.size() == 1 && ramfollower.contains(user.getMainkey())){
				removenumber++;
			}
		}
		
		ArrayList<Integer> ramfollower = user.getRamfollowers().get(ram.mainkey);
		if(ramfollower.size() == 1 && ramfollower.contains(user.getMainkey())){
			removenumber++;
		}
		
		return addnumber-removenumber;
	}
}
