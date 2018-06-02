package bookStore;

public class Order_item {
	private int autoid;
	private String orderid;
	private String bookid;
	private String amount;
	
	public void setorder(String orderid) {
		this.orderid = orderid;
	}
	public void setbook(String bookid) {
		this.bookid = bookid;
	}
	public void setamount(String amount) {
		this.amount = amount;
	}
	
	public String getbook() {
		return this.bookid;
	}
	public String getamount() {
		return this.amount;
	}
	
}
