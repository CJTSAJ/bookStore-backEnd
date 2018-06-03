package bookStore;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;

import Entity.BookEnity;
import Service.HibernateUtil;
import net.sf.json.JSONArray;

/**
 * Servlet implementation class BookServlet
 */
@WebServlet("/BookServlet")
public class BookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BookServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setHeader("Access-Control-Allow-Headers", "accept, content-type");  
	    response.setHeader("Access-Control-Allow-Method", "GET");  
	    response.setHeader("Access-Control-Allow-Origin", "*");
		System.out.println("user get");
		getAllBook();
		response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        String jsondata = "{\"logPath\":\"1\",\"success\":true}";
        String res = jsondata;       
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.write(res);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Headers", "accept, content-type");  
	    response.setHeader("Access-Control-Allow-Method", "POST");  
	    response.setHeader("Access-Control-Allow-Origin", "*");
		System.out.println("user get");
		
		response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
		
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    	Transaction transaction = session.beginTransaction();//开启事务
    	
    	Criteria criteria =session.createCriteria(BookEnity.class);
        List<BookEnity> list = criteria.list();
        
        Iterator<BookEnity> it = list.iterator();
        ArrayList<JSONArray> bookJson = new ArrayList<JSONArray>();
        while(it.hasNext()) {
        	BookEnity book = (BookEnity)it.next();
        	ArrayList<String> arrayList = new ArrayList<String>();
        	arrayList.add(book.getid());
        	arrayList.add(book.getname());
        	arrayList.add(book.getauthor());
        	arrayList.add(book.getlanguage());
        	arrayList.add(book.getpublished());
        	arrayList.add(book.getsales());
        	arrayList.add(book.getprice());
        	bookJson.add(JSONArray.fromObject(arrayList));
        }
        session.close();
        JSONArray books = JSONArray.fromObject(bookJson.toArray());
        PrintWriter out = null;
        try {
        	out = response.getWriter();
        	out.println(books);
        }catch (IOException e){
        	e.printStackTrace();
        }finally{
        	out.close();
        }
	}
	
	public static void getAllBook() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    	Transaction transaction = session.beginTransaction();//开启事务
	    try {
	    	/*Query query =session.createQuery("from customer");
            List<Customer> list = query.list();*/
	    	
	    	Criteria criteria =session.createCriteria(BookEnity.class);
            List<BookEnity> list = criteria.list();
            
            for(BookEnity cus : list) {
            	//System.out.println(cus);
            	System.out.println(cus.getid());
                System.out.println(cus.getauthor());
                System.out.println(cus.getpublished());
                System.out.println(cus.getprice());
            }
            transaction.commit();
	    }catch (Exception e){
	    	transaction.rollback();
	    }finally {
	    	session.close();
	    }
	}

}
