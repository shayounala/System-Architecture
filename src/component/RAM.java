package component;

import java.util.ArrayList;
import java.util.Random;

import evaluation.Evaluation;

public class RAM {

	private int mainkey;

	public int getMainkey() {
		return mainkey;
	}

	private int Capacity;
	private ArrayList<Integer> users;
	
	public ArrayList<Integer> getUsers() {
		return users;
	}

	private ArrayList<Integer> messages;
	
	private ArrayList<Integer> messagess;
	
	private ArrayList<Double> ratios;
	public ArrayList<Double> getRatios() {
		return ratios;
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

	public RAM(int mainkey, int ramCapacity){
		this.mainkey = mainkey;
		this.Capacity = ramCapacity;
		users = new ArrayList<Integer>();
		messages = new ArrayList<Integer>();
		messagess = new ArrayList<Integer>();
		ratios = new ArrayList<Double>();
		relationadd = 0;
		relationmove =0;
		dataadd = 0;
		datamove = 0;
	}

	public void addUser(User user, ArrayList<RAM> rams) {
		// TODO Auto-generated method stub
		addUserMain(user);
		if(Evaluation.PA == 0){
			for(int i=0;i<Evaluation.k2;i++){
				Random random = new Random();
				int index = random.nextInt(rams.size());
				while(rams.get(index).containUser(user.getMainkey())){
					index = random.nextInt(rams.size());
				}
				rams.get(index).addUserCopy(user);
			}
		}
	}

	public boolean containUser(int mainkey2) {
		// TODO Auto-generated method stub
		
		return users.contains(mainkey2);
	}

	public void addRelation(User user, RAM ram, ArrayList<User> users2) {
		// TODO Auto-generated method stub
		relationadd++;
		if(this.users.contains(user.getMainkey())){
			return;
		}
		
		if(movecost(user, ram, users2) < 1 && Evaluation.PA != 0){
			relationmove++;
			move(user, ram, users2);
		}else{
			addUserCopy(user);
		}
	}

	private void move(User user, RAM ram, ArrayList<User> users2) {
		// TODO Auto-generated method stub
		for(int i=0;i<user.getFriends().size();i++){
			if(!this.containUser(user.getFriends().get(i))){
				addUserCopy(users2.get(user.getFriends().get(i)));
			}
		}
		
		
		
		for(int i=0;i<user.getFriends().size();i++){
			User user1 = users2.get(user.getFriends().get(i));
			if(isRemove(ram, user1)){
				ram.removeUserCopy(user1);
			}
		}
		
		if(isRemove(ram, user)){
			ram.removeUserMain(user);
			addUserMain(user);
		}
	}

	private boolean isRemove(RAM ram, User user) {
		// TODO Auto-generated method stub
		if(!ram.users.contains(user.getMainkey())){
			return false;
		}
		for(int i=0;i<user.getFollowers().size();i++){
			if(ram.containUser(user.getFollowers().get(i))){
				return false;
			}
		}
		if(user.getRams().indexOf(ram.mainkey) == 0){
			return false;
		}
		if(user.getRams().size()>Evaluation.k2){
			return true;
		}else{
			return false;
		}

	}

	private void addUserMain(User user) {
		// TODO Auto-generated method stub
		user.getRams().add(0, this.mainkey);
		users.add(user.getMainkey());
		int usermessages = Evaluation.CacheUnit;
		int usermessagess = Evaluation.CacheUnit;
		double userratios = 1;
		if(user.getRams().size() != 0){
			RAM ram = Evaluation.network.getRams().get(user.getRams().get(0));
			int index = ram.users.indexOf(user.getMainkey());
			if(ram.messages.size() > index){
				usermessages = ram.messages.get(index);
				usermessagess = ram.messagess.get(index);
				userratios = ram.ratios.get(index);
			}

		}
		
		messages.add(usermessages);
		messagess.add(usermessagess);
		ratios.add(userratios);
	}

	private void removeUserMain(User user) {
		// TODO Auto-generated method stub
		int index = users.indexOf(user.getMainkey());
		users.remove(index);
		if(user.getRams().get(0) == mainkey){
			System.out.println("usermain");
		}else{
			System.out.println("usermain error");
		}
		user.getRams().remove(0);
		messages.remove(index);
		messagess.remove(index);
		ratios.remove(index);
		
		if(user.getRams().indexOf(mainkey) != -1){
			System.out.println("1error"+mainkey);
		}
	}

	private void removeUserCopy(User user) {
		// TODO Auto-generated method stub
		int index = users.indexOf(user.getMainkey());
		users.remove(index);
		messages.remove(index);
		messagess.remove(index);
		ratios.remove(index);
		
		int index1 = user.getRams().indexOf(mainkey);
		user.getRams().remove(index1);
		if(user.getRams().indexOf(mainkey) != -1){
			System.out.println("error"+mainkey);
		}
	}

	public void addUserCopy(User user) {
		// TODO Auto-generated method stub
		users.add(user.getMainkey());
		user.getRams().add(this.mainkey);
		int usermessages = 0;
		int usermessagess = 0;
		double userratios = 0;
		if(user.getRams().size() != 0){
			RAM ram = Evaluation.network.getRams().get(user.getRams().get(0));
			int index = ram.users.indexOf(user.getMainkey());
			usermessages = ram.messages.get(index);
			usermessagess = ram.messagess.get(index);
			userratios = ram.ratios.get(index);
		}
		
		messages.add(usermessages);
		messagess.add(usermessagess);
		ratios.add(userratios);
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
			User user1 = users2.get(user.getFriends().get(i));
			if(isRemove(ram, user1)){
				removenumber++;
			}
		}
		

		if(isRemove(ram, user)){
			removenumber++;
		}
		
		return addnumber-removenumber;
	}
	
	public void addData(int mainkey2, int order){
		dataadd++;
		if(Evaluation.PA == 2){
			adjust(mainkey2);
		}
		
		User user = Evaluation.network.getUsers().get(mainkey2);
		for(int i=0;i<user.getRams().size();i++){
			RAM ram = Evaluation.network.getRams().get(user.getRams().get(i));
			
			System.out.println("ram"+ram.users.indexOf(mainkey2));
			ram.addData(mainkey2);
		}
	}

	private void addData(int mainkey2) {
		// TODO Auto-generated method stub
		int index = users.indexOf(mainkey2);
		messagess.set(index, messagess.get(index)+1);
		
		if(getSpace()<Capacity){
			messages.set(index, messages.get(index)+1);
			double ratio = (double)messages.get(index);
			ratio = ratio/messagess.get(index);
			ratios.set(index, ratio);
		}else{
			int index1 = getmaxratio();
			refresh(users.get(index1));
			messages.set(index, messages.get(index)+1);
			double ratio = (double)messages.get(index);
			ratio = ratio/messagess.get(index);
			ratios.set(index, ratio);
		}
	}

	private void adjust(int mainkey2) {
		// TODO Auto-generated method stub
		User user = Evaluation.network.getUsers().get(mainkey2);
		if(user.getRams().size()>Evaluation.k2+1){
			for(int i=0;i<user.getFollowers().size();i++){
				User user1 = Evaluation.network.getUsers().get(user.getFollowers().get(i));
				RAM ram = Evaluation.network.getRams().get(user1.getRams().get(0));
				if(movecosts(user1, ram, Evaluation.network.getUsers()) < 0){
					datamove++;
					move(user, ram, Evaluation.network.getUsers());
				}
			}
		}
	}

	private int movecosts(User user1, RAM ram, ArrayList<User> users2) {
		// TODO Auto-generated method stub
		int addnumber = 0;
		for(int i=0;i<user1.getFriends().size();i++){
			if(!this.containUser(user1.getFriends().get(i))){
				RAM ram1 = Evaluation.network.getRams().get(Evaluation.network.getUsers().get(user1.getFriends().get(i)).getRams().get(0));
				int index = ram1.users.indexOf(user1.getFriends().get(i));
				addnumber += ram1.messages.get(index);
			}
		}
		
		
		
		int removenumber = 0;
		for(int i=0;i<user1.getFriends().size();i++){
			User user2 = users2.get(user1.getFriends().get(i));
			if(isRemove(ram, user2)){
				int index = ram.users.indexOf(user1.getFriends().get(i));
				removenumber += ram.messages.get(index);
			}
		}
		

		if(isRemove(ram, user1)){
			int index = ram.users.indexOf(user1.getMainkey());
			removenumber += ram.messages.get(index);
		}
		
		return addnumber-removenumber;
	}

	private void refresh(int mainkey2) {
		// TODO Auto-generated method stub
		int index = users.indexOf(mainkey2);
		User user = Evaluation.network.getUsers().get(mainkey2);
		for(int i=0;i<user.getRams().size();i++){
			RAM ram = Evaluation.network.getRams().get(user.getRams().get(i));
			index = ram.users.indexOf(mainkey2);
			ram.messages.set(index, ram.messages.get(index)-1);
			double ratio = (double)ram.messages.get(index);
			ratio = ratio/ram.messagess.get(index);
			ram.ratios.set(index, ratio);
		}
		
		DataStore store = Evaluation.network.getDatastores().get(user.getDatastores().get(0));
		index = users.indexOf(mainkey2);
		store.addData(mainkey2, messagess.get(index), 0);
	}

	private int getmaxratio() {
		// TODO Auto-generated method stub
		int index =0;
		double maxratio = ratios.get(0);
		for(int i=0;i<ratios.size();i++){
			if(maxratio<ratios.get(i)){
				index = i;
				maxratio = ratios.get(i);
			}
		}
		return index;
	}

	public int getSpace() {
		// TODO Auto-generated method stub
		int space = 0;
		for(int i=0;i<messages.size();i++){
			space += messages.get(i);
		}
		return space;
	}

	public void search(User user, int message) {
		// TODO Auto-generated method stub
		System.out.println("search");
		int index = users.indexOf(user.getMainkey());
		if(messages.get(index)>message){
			
		}else{
			DataStore store = Evaluation.network.getDatastores().get(user.getDatastores().get(0));
			store.search(user, message);
		}
	}
}
