package evaluation;

public class Request {

	private int user;
	public int getUser() {
		return user;
	}


	public int getStyle() {
		return style;
	}

	private int style;
	private int messagetime;
	public int getMessagetime() {
		return messagetime;
	}

	public int getUser2() {
		return user2;
	}

	private int user2;

	public Request(int user, int style){
		this.user = user;
		this.style = style;
	}


	public Request(int user, int user2, int style) {
		// TODO Auto-generated constructor stub
		this.user = user;
		this.user2 = user2;
		this.style = style;
	}
}
