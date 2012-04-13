package component;

import java.util.ArrayList;

public class User {

	private int mainkey;
	public int getMainkey() {
		return mainkey;
	}

	private ArrayList<Integer> followers;
	public ArrayList<Integer> getFollowers() {
		return followers;
	}

	private ArrayList<Integer> friends;

	public ArrayList<Integer> getFriends() {
		return friends;
	}
	
	private ArrayList<Integer> rams;
	public ArrayList<Integer> getRams() {
		return rams;
	}

	private ArrayList<Integer> datastores;

	public ArrayList<Integer> getDatastores() {
		return datastores;
	}

	public User(int mainkey, ArrayList<Integer> followers, ArrayList<Integer> friends){
		this.mainkey = mainkey;
		this.followers = followers;
		this.friends = friends;
		rams = new ArrayList<Integer>();
		datastores = new ArrayList<Integer>();
	}
}
