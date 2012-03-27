package component;

import java.util.ArrayList;

public class User {

	private int mainkey;
	private ArrayList<Integer> followers;
	private ArrayList<Integer> friends;

	public User(int mainkey, ArrayList<Integer> followers, ArrayList<Integer> friends){
		this.mainkey = mainkey;
		this.followers = followers;
		this.friends = friends;
	}
}
