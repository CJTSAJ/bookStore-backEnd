package bookStore;

public class BookEnity {
	private String id;
	private String bookName;
	private String author;
	private String useLanguage;
	private String published;
	private String sales;
	private String price;
	
	public BookEnity() {}
	public String getid() {
		return this.id;
	}
	public void setid(String id) {
		this.id = id;
	}
	
	public String getname() {
		return this.bookName;
	}
	public void setname(String bookName) {
		this.bookName = bookName;
	}
	
	public String getauthor() {
		return this.author;
	}
	public void setauthor(String author) {
		this.author = author;
	}
	
	public String getlanguage() {
		return this.useLanguage;
	}
	public void setlanguage(String useLanguage) {
		this.useLanguage = useLanguage;
	}
	
	public String getpublished() {
		return this.published;
	}
	public void setpublished(String published) {
		this.published = published;
	}
	
	public String getsales() {
		return this.sales;
	}
	public void setsales(String sales) {
		this.sales = sales;
	}
	
	public String getprice() {
		return this.price;
	}
	public void setprice(String price) {
		this.price = price;
	}
}
