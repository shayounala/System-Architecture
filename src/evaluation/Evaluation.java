package evaluation;

import java.util.ArrayList;
import java.util.Random;

import component.DataStore;
import component.RAM;
import component.User;

public class Evaluation {

	public static int k2;
	public static int k3;
	public static int CacheUnit;
	public static double increase;
	private static double PerA;
	private static double PerV;
	private static double PerT;
	private static double PerR;
	public static int CA;
	public static int PA;

	public static int time;
	public static int times;
	private static int userNumber;
	private static int cacheCapacity;
	private static int ramCapacity;
	private static int ramNumber;
	public static Network network;
	private static int datastoreNumber;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		initiateEvaluation();

		for (time = 0; time < times; time++) {// t is the current time of
												// evaluation
			System.out.println("Time: " + time);

			ArrayList<Request> requests = generateUserRequests(network
					.getUsers());

			network.getCache().handle(requests);

			if (time % 100 == 0) {
				network.getCache().updatefactors();
			}
		}

		saveResutls();
	}

	private static void saveResutls() {
		// TODO Auto-generated method stub
		double hitrate = network.getCache().getHitsuccess();
		hitrate = hitrate / network.getCache().getHits();
		System.out.println("Hit rate of Cache: " + hitrate + " "
				+ network.getCache().getHitsuccess() + " "
				+ network.getCache().getHits());

		double averageratios = 0;
		int rationumber = 0;
		double averagemoveratio = 0;
		int movenumber = 0;
		double averagedatamoveratio = 0;
		int movedatanumber = 0;
		for (int i = 0; i < network.getRams().size(); i++) {
			RAM ram = network.getRams().get(i);
			for (int j = 0; j < ram.getRatios().size(); j++) {
				averageratios += ram.getRatios().get(j);
			}
			rationumber += ram.getRatios().size();
			averagemoveratio += ram.getRelationmove();
			movenumber += ram.getRelationadd();
			averagedatamoveratio += ram.getDatamove();
			movedatanumber += ram.getDataadd();
			// System.out.println(ram.getUsers().size());
		}

		averageratios = averageratios / rationumber;
		averagemoveratio = averagemoveratio / movenumber;
		averagedatamoveratio = averagedatamoveratio / movedatanumber;
		System.out.println("Average Ratio: " + averageratios
				+ " Average Relation Move Ratio: " + averagemoveratio
				+ "Average Data Move Ratio: " + averagedatamoveratio);

		int totalspace = 0;
		double averagemoveratio1 = 0;
		int movenumber1 = 0;
		double averagedatamoveratio1 = 0;
		int movedatanumber1 = 0;
		for (int i = 0; i < network.getDatastores().size(); i++) {
			DataStore store = network.getDatastores().get(i);
			totalspace += store.getSpace();

			averagemoveratio1 += store.getRelationmove();
			movenumber1 += store.getRelationadd();
			averagedatamoveratio1 += store.getDatamove();
			movedatanumber1 += store.getDataadd();
			// System.out.println(store.getSpace());
		}

		System.out.println("Total Space: " + totalspace
				+ " Average Relation Move Ratio: " + averagemoveratio1
				+ "Average Data Move Ratio: " + averagedatamoveratio1);

		System.out.println(hitrate + "	" + network.getCache().getHitsuccess()
				+ "	" + network.getCache().getHits() + "	" + averageratios + "	"
				+ averagemoveratio + "	" + averagedatamoveratio + "	"
				+ totalspace + "	" + averagemoveratio1 + "	"
				+ averagedatamoveratio1);

	}

	private static void initiateEvaluation() {
		// TODO Auto-generated method stub
		{

			k2 = 2;
			k3 = 3;
			CacheUnit = 10;
			increase = 1.01;
			PerA = 0;
			PerV = 0.8;
			PerT = 0.95;
			PerR = 0.99;
			CA = 1;
			PA = 0;

			userNumber = 10000;
			cacheCapacity = 12000;
			ramCapacity = 10000;
			ramNumber = 100;
			datastoreNumber = 100;

			times = 100;

			network = new Network(userNumber, cacheCapacity, ramCapacity,
					ramNumber, datastoreNumber);

			network.initiateUserInformation();
		}
	}

	private static ArrayList<Request> generateUserRequests(ArrayList<User> users) {
		// TODO Auto-generated method stub
		ArrayList<Request> requests = new ArrayList<Request>();

		for (int userorder = 0; userorder < users.size(); userorder++) {
			Random random = new Random();
			double randomdouble = random.nextDouble();
			if (randomdouble > Evaluation.PerA) {
				randomdouble = random.nextDouble();
				if (randomdouble < Evaluation.PerV) {
					requests.add(new Request(users.get(userorder).getMainkey(),
							0));
				} else if (randomdouble < Evaluation.PerT) {
					requests.add(new Request(users.get(userorder).getMainkey(),
							1));
				} else if (randomdouble < Evaluation.PerR) {
					if (users.get(userorder).getFriends().size() != 0) {
						int index = random.nextInt(users.get(userorder)
								.getFriends().size());
						int mainkey2 = users.get(userorder).getFriends()
								.get(index);
						requests.add(new Request(users.get(userorder)
								.getMainkey(), mainkey2, 2));
					}
				}
			}
		}

		return requests;
	}

}
