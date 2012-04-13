package component;

import java.util.ArrayList;

import evaluation.Evaluation;
import evaluation.Request;

public class Cache {

	private int Capacity;
	private ArrayList<Integer> users;
	private ArrayList<Integer> messages;
	private ArrayList<Integer> uses;
	private ArrayList<Double> frequencies;
	private ArrayList<Double> datas;
	private ArrayList<Double> userss;
	private int factor;
	private int hits;
	public int getHits() {
		return hits;
	}

	public int getHitsuccess() {
		return hitsuccess;
	}

	private int hitsuccess;
	public Cache(int cacheCapacity) {
		// TODO Auto-generated constructor stub
		this.Capacity = cacheCapacity;
		users = new ArrayList<Integer>();
		messages = new ArrayList<Integer>();
		uses = new ArrayList<Integer>();
		frequencies = new ArrayList<Double>();
		datas = new ArrayList<Double>();
		userss = new ArrayList<Double>();
		factor = 0;
		hits = 0;
		hitsuccess = 0;
	}

	public void handle(ArrayList<Request> requests) {
		// TODO Auto-generated method stub
		for(int i=0;i<requests.size();i++){
			System.out.println("Time: "+Evaluation.time+"Request: "+i+requests.get(i).getStyle());
			Request request = requests.get(i);
			if(request.getStyle() == 1){
				addMessage(request.getUser());
			}else if(request.getStyle() == 0){
				visitMessage(request.getUser(), request.getMessagetime());
			}else{
				repostMessage(request.getUser(), request.getUser2(), request.getMessagetime());
			}
		}
		
		factor++;
	}

	private void repostMessage(int mainkey2, int mainkey3, int messagetime) {
		// TODO Auto-generated method stub
		boolean hit = false;
		int index = users.indexOf(mainkey2);
		int index2 = users.indexOf(mainkey3);
		if(index != -1){
			if(index2 != -1){
				if(messages.get(index2)>messagetime){
					hit = true;
				}
			}
		}
		
		if(hit){
			hits++;
			hitsuccess++;
			
			if(getSpace()<Capacity){
				add(mainkey2);
			}else{
				ArrayList<Integer> users3 = new ArrayList<Integer>();
				users3.add(mainkey2);
				users3.add(mainkey3);
				refresh(null, null, users3);
				add(mainkey2);
			}
		}else{
			hits++;
			
			ArrayList<Integer> users2 = new ArrayList<Integer>();
			users2.add(mainkey2);
			users2.add(mainkey3);
			ArrayList<Integer> messagetimes = new ArrayList<Integer>();
			messagetimes.add(-1);
			messagetimes.add(messagetime);
			ArrayList<Integer> users3 = new ArrayList<Integer>();
			users3.add(mainkey2);
			users3.add(mainkey3);
			refresh(users2, messagetimes, users3);
			add(mainkey2);
		}
		
		ArrayList<Integer> users1 = new ArrayList<Integer>();
		users1.add(mainkey2);
		users1.add(mainkey3);
		update(users1);
		ArrayList<Integer> users2number = new ArrayList<Integer>();
		users2number.add(1);
		users2number.add(1);
		update1(users1, users2number);
		
	}

	private void visitMessage(int mainkey2, int messagetime) {
		// TODO Auto-generated method stub
		boolean hit = false;
		int index = users.indexOf(mainkey2);
		if(index != -1){
			if(messages.get(index)>messagetime){
				hit = true;
			}
		}
		
		User user = Evaluation.network.getUsers().get(mainkey2);
		for(int i=0;i<user.getFriends().size();i++){
			int mainkey3 = user.getFriends().get(i);
			int index3 = users.indexOf(mainkey3);
			if(index3 == -1){
				hit =false;
				break;
			}else if(messages.get(index3)<=messagetime){
				hit =false;
				break;
			}
		}
		if(hit){
			hits++;
			hitsuccess++;
		}else{
			hits++;
			
			ArrayList<Integer> users3 = new ArrayList<Integer>();
			users3.add(mainkey2);
			ArrayList<Integer> messagetimes = new ArrayList<Integer>();
			messagetimes.add(messagetime);
			for(int i=0;i<user.getFriends().size();i++){
				int mainkey3 = user.getFriends().get(i);
				int index3 = users.indexOf(mainkey3);
				if(index3 == -1){
					users3.add(mainkey3);
					messagetimes.add(messagetime);
				}else if(messages.get(index3)<=messagetime){
					users3.add(mainkey3);
					messagetimes.add(messagetime);
				}else{
				}
			}
			ArrayList<Integer> users4 = new ArrayList<Integer>();
			users4.add(mainkey2);
			for(int i=0;i<user.getFriends().size();i++){
				int mainkey3 = user.getFriends().get(i);
				users4.add(mainkey3);
			}
			refresh(users3, messagetimes, users4);
		}
		
		ArrayList<Integer> users1 = new ArrayList<Integer>();
		ArrayList<Integer> users2 = new ArrayList<Integer>();
		users1.add(mainkey2);
		users2.add(mainkey2);
		for(int i=0;i<user.getFriends().size();i++){
			int mainkey3 = user.getFriends().get(i);
			users1.add(mainkey3);
		}
		ArrayList<Integer> users2number = new ArrayList<Integer>();
		users2number.add(users1.size());
		update(users1);
		update1(users2, users2number);
		
	}

	private void addMessage(int mainkey2) {
		// TODO Auto-generated method stub
		boolean hit = false;
		
		int index = users.indexOf(mainkey2);
		if(index != -1){
			hit = true;
		}
		
		if(hit){
			hits++;
			hitsuccess++;
			
			if(getSpace()<Capacity){
				add(mainkey2);
			}else{
				ArrayList<Integer> users3 = new ArrayList<Integer>();
				users3.add(mainkey2);
				refresh(null, null, users3);
				index = users.indexOf(mainkey2);
				add(mainkey2);
			}
		}else{
			hits++;
			
			ArrayList<Integer> users2 = new ArrayList<Integer>();
			users2.add(mainkey2);
			ArrayList<Integer> messagetimes = new ArrayList<Integer>();
			messagetimes.add(-1);
			ArrayList<Integer> users3 = new ArrayList<Integer>();
			users3.add(mainkey2);
			refresh(users2, messagetimes, users3);
			add(mainkey2);
		}
		
		ArrayList<Integer> users1 = new ArrayList<Integer>();
		users1.add(mainkey2);
		ArrayList<Integer> users1number = new ArrayList<Integer>();
		users1number.add(1);
		update(users1);
		update1(users1, users1number);
	}
	
	private void update1(ArrayList<Integer> users1,
			ArrayList<Integer> users1number) {
		// TODO Auto-generated method stub
		for(int i=0;i<users1.size();i++){
			int index = users.indexOf(users1.get(i));
			double data = 1;
			data = data/users1number.get(i);
			userss.set(index, userss.get(index)+data*Math.pow(Evaluation.increase, factor));
		}
		
	}

	private void update(ArrayList<Integer> users1) {
		// TODO Auto-generated method stub
		for(int i=0;i<users1.size();i++){
			int index = users.indexOf(users1.get(i));
			uses.set(index, Evaluation.time);
			frequencies.set(index, frequencies.get(index)+Math.pow(Evaluation.increase, factor));
			double data = 1;
			data = data/users1.size();
			datas.set(index, datas.get(index)+data*Math.pow(Evaluation.increase, factor));
		}
		
	}
	

	private void add(int mainkey2) {
		// TODO Auto-generated method stub
		int index = users.indexOf(mainkey2);
		messages.set(index, messages.get(index)+1);
		User user = Evaluation.network.getUsers().get(mainkey2);
		RAM ram = Evaluation.network.getRams().get(user.getRams().get(0));
		ram.addData(mainkey2, 0);
	}

	private void refresh(ArrayList<Integer> users2, ArrayList<Integer> messagetimes, ArrayList<Integer> users3) {
		// TODO Auto-generated method stub
		System.out.println("refresh");
		int removenumber = 0;
		if(users2 != null){
			if(getSpace()+users2.size()*Evaluation.CacheUnit>Capacity){
				removenumber = (getSpace()+users2.size()*Evaluation.CacheUnit-Capacity)/Evaluation.CacheUnit+1;
			}
		}else if(getSpace()>Capacity){
			removenumber = (getSpace()-Capacity)/Evaluation.CacheUnit+1;
		}
		
		switch(Evaluation.CA){
			case 0:
				LFU(removenumber, users3);
				break;
			case 1:
				LRU(removenumber, users3);
				break;
			case 2:
				LFUD(removenumber, users3);
				break;
			case 3:
				LFUN(removenumber, users3);
				break;
		}

		
		searchRAMs(users2,messagetimes);
		
	}

	private void searchRAMs(ArrayList<Integer> users2,
			ArrayList<Integer> messagetimes) {
		// TODO Auto-generated method stub
		if(users2 == null){
			return;
		}
		System.out.println("searchRAMS");
		for(int i=0;i<users2.size();i++){
			User user = Evaluation.network.getUsers().get(users2.get(i));
			RAM ram = Evaluation.network.getRams().get(user.getRams().get(0));
			ram.search(user, messagetimes.get(i));
			if(users.indexOf(user.getMainkey()) == -1){
				users.add(user.getMainkey());
				messages.add(Evaluation.CacheUnit);
				uses.add(Evaluation.time);
				frequencies.add(0.0);
				datas.add(0.0);
				userss.add(0.0);
			}else{
				
			}
		}
	}

	private void LFUN(int space, ArrayList<Integer> users3) {
		// TODO Auto-generated method stub
		int cap = this.Capacity-space;
		ArrayList<Integer> saves = new ArrayList<Integer>();
		if(users3 != null){
			saves.addAll(users3);
		}
		int [] userssorder = insortorder(userss);
		for(int i=0;i<userssorder.length;i++){
			ArrayList<Integer> temp = new ArrayList<Integer>();
			int mainkey = users.get(userssorder[i]);
			if(!saves.contains(mainkey)){
				temp.add(mainkey);
			}
			User user = Evaluation.network.getUsers().get(mainkey);
			for(int j=0;j<user.getFriends().size();j++){
				if(!saves.contains(user.getFriends().get(j))){
					temp.add(user.getFriends().get(j));
				}
			}
			
			if(saves.size()+temp.size() <= cap){
				saves.addAll(temp);
			}
		}
		
		for(int i=0;i<users.size();i++){
			if(!saves.contains(users.get(i))){
				remove(i);
			}
		}
	}

	private int[] insortorder(ArrayList<Double> userss2) {
		// TODO Auto-generated method stub
		int [] order = new int[userss2.size()];
		
		ArrayList<Double> temp = new ArrayList<Double>();
		for(int i=0;i<userss2.size();i++){
			temp.add(userss2.get(i));
		}
		
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		for(int i=0;i<order.length;i++){
			int index = 0;
			double max = 0;
			for(int j=0;j<temp.size();j++){
				if(temp.get(j)>max && !indexes.contains(j)){
					index = j;
					max = temp.get(j);
				}
			}
			indexes.add(index);
		}
		return order;
	}

	private void LFUD(int space, ArrayList<Integer> users3) {
		// TODO Auto-generated method stub
		for(int i=0;i<space;i++){
			int index = 0;
			double data = 10000;
			for(int j=0;j<datas.size();j++){
				int mainkey2 = users.get(j);
				if(users3 != null && !users3.contains(mainkey2)){
					if(datas.get(j)<data){
						data = datas.get(j);
						index = j;
					}
				}
			}
			
			remove(index);
		}
	}

	private void LRU(int space, ArrayList<Integer> users3) {
		// TODO Auto-generated method stub
		for(int i=0;i<space;i++){
			int index = 0;
			double recent = -1;
			for(int j=0;j<uses.size();j++){
				int mainkey2 = users.get(j);
				if(users3 != null && !users3.contains(mainkey2)){
					if(uses.get(j)>recent){
						recent = uses.get(j);
						index = j;
					}
				}else{
				}
			}
			
			remove(index);
		}
	}

	private void LFU(int space, ArrayList<Integer> users3) {
		// TODO Auto-generated method stub
		for(int i=0;i<space;i++){
			int index = 0;
			double frequency = 10000;
			for(int j=0;j<frequencies.size();j++){
				int mainkey2 = users.get(j);
				if(users3 != null && !users3.contains(mainkey2)){
					if(frequencies.get(j)<frequency){
						frequency = frequencies.get(j);
						index = j;
					}
				}else{
				}
			}
			remove(index);
		}
		
	}

	private void remove(int index) {
		// TODO Auto-generated method stub
		users.remove(index);
		messages.remove(index);
		uses.remove(index);
		frequencies.remove(index);
		datas.remove(index);
		userss.remove(index);
	}

	private int getSpace() {
		// TODO Auto-generated method stub
		int space = 0;
		for(int i=0;i<messages.size();i++){
			space += messages.get(i);
		}
		return space;
	}

	public void updatefactors() {
		// TODO Auto-generated method stub
		for(int i=0;i<users.size();i++){
			frequencies.set(i, frequencies.get(i)/Math.pow(Evaluation.increase, 100));
			datas.set(i, datas.get(i)/Math.pow(Evaluation.increase, 100));
			userss.set(i, userss.get(i)/Math.pow(Evaluation.increase, 100));
		}
		
		factor -= 100;
	}

}
