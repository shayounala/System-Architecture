package evaluation;

public class Request {

	private int user;
	private int mainkey;
	private int starttime;
	private int style;

	public Request(int user, int mainkey, int starttime, int style){
		this.user = user;
		this.mainkey = mainkey;
		this.starttime = starttime;
		this.style = style;
	}
}
