package evaluation;

import java.util.ArrayList;

import component.Cache;
import component.User;

public class Evaluation {

	private static int time;
	private static int userNumber;
	private static int cacheCapacity;
	private static int ramCapacity;
	private static int ramNumber;
	private static int datastoreCapacity;
	private static Network network;
	private static int datastoreNumber;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		initiateEvaluation();
		
		for(int t=0;t<time;t++){//t is the current time of evaluation
			ArrayList<Request> requests = generateUserRequests(network.getUsers());
			
			Cache.handle(requests);
		}
	}

	private static void initiateEvaluation() {
		// TODO Auto-generated method stub
		{
			userNumber = 100000;
			cacheCapacity = 1000;
			ramCapacity = 100;
			ramNumber = 100;
			datastoreCapacity = 100;
			datastoreNumber = 100;
			
			network = new Network(userNumber, cacheCapacity, ramCapacity, ramNumber, datastoreCapacity, datastoreNumber);
		}
	}

	private static ArrayList<Request> generateUserRequests(ArrayList<User> users) {
		// TODO Auto-generated method stub
		ArrayList<Request> requests = new ArrayList<Request>();
		
		for(int userorder=0;userorder<users.size();userorder++){
			
		}
		
		return requests;
	}

}
