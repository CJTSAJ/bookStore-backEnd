package Entity;

public class User_order {
	private int autoid;
	private String userid;
	private String orderid;
	private String total;
	public void setuser(String user) {
		this.userid = user;
	}
	public void setorder(String orderid) {
		this.orderid = orderid;
	}
	public void settotal(String total) {
		this.total = total;
	}
	public String getorder() {
		return this.orderid;
	}
	public String gettotal() {
		return this.total;
	}
}
