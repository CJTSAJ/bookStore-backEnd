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

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import Entity.Order_item;
import Entity.User_order;
import Service.HibernateUtil;
import Service.JsonReader;
import Service.UserState;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class OrderServlet
 */
@WebServlet("/OrderServlet")
public class OrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrderServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Headers", "accept, content-type");  
	    response.setHeader("Access-Control-Allow-Method", "GET");  
	    response.setHeader("Access-Control-Allow-Origin", "*");
		System.out.println("order get");
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	    Transaction transaction = session.beginTransaction();//开启事务
		Query query = session.createQuery("select orderid,total from User_order where userid = ?0");
        //2、填写上一步中占位符的内容
        query.setParameter(0, "CJTSAJ");
        List list = query.list();
        Iterator iterator=list.iterator();
        while(iterator.hasNext()) {
        	Object[] obj=(Object[])iterator.next();
        	System.out.println(obj[0]);
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Headers", "accept, content-type");  
	    response.setHeader("Access-Control-Allow-Method", "POST");
	    response.setHeader("Access-Control-Allow-Origin", "*");
		System.out.println("order post");
		
		response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        String jsondata = null;       
        PrintWriter out = null;
        
        JSONObject json=JsonReader.receivePost(request);
        
        String operation = json.getString("operation");
        
        if(operation.equals("add")) { /*commit shopCar*/
        	String books = json.getString("bookList");
            String orderid = json.getString("orderid");
            String total = json.getString("total");
            JSONArray itemArray = JSONArray.fromObject(books);
            
            User_order user_order = new User_order();
            user_order.setuser(UserState.userID);
            user_order.setorder(orderid);
            user_order.settotal(total);
            
            Session session = null;
    	    Transaction transaction = null;
    	    
    		try {
    			session = HibernateUtil.getSessionFactory().getCurrentSession();
    		    transaction = session.beginTransaction();//开启事务
    		    
    		    session.save(user_order);/*插入数据*/
    		    
    		    transaction.commit();
    		}catch(Exception e) {
    			transaction.rollback();/*若发生异常  回滚*/
    		}finally {
    			session.close();
    		}
    		
    		try {
    			session = HibernateUtil.getSessionFactory().getCurrentSession();
    		    transaction = session.beginTransaction();//开启事务
    		    
    		    for(int i=0;i<itemArray.size();i++){
    	        	JSONArray temp = itemArray.getJSONArray(i);
    	        	String str = temp.toString();
    	        	str = str.substring(1);
    	        	str = str.substring(0, str.length() - 1);
    	        	str = str.replace("\"", "");
    	        	String[] book = str.split(",");
    	        	
    	        	Order_item order_item = new Order_item();
    	        	order_item.setorder(orderid);
    	        	order_item.setbook(book[0]);
    	        	order_item.setamount(book[4]);
    			    
    			    session.save(order_item);/*存order_item*/
    	        }
    		    
    		    transaction.commit();
    		}catch(Exception e) {
    			transaction.rollback();/*若发生异常  回滚*/
    		}finally {
    			session.close();
    		}
           
    		jsondata = "{\"result\":\"true\"}";
            try {
            	out = response.getWriter();
            	out.println(books);
            }catch (IOException e){
            	e.printStackTrace();
            }finally{
            	out.close();
            }
            UserState.cartJson.clear(); /*清空购物车*/
        }
        
        else if(operation.equals("detail")) {
        	String orderid = json.getString("orderid");
        	
        	Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    	    Transaction transaction = session.beginTransaction();//开启事务
    		Query query = session.createQuery("select bookid,amount from Order_item where orderid = ?0");
            //2、填写上一步中占位符的内容
            query.setParameter(0, orderid);
            List list = query.list();
            Iterator iterator=list.iterator();
            String tempStr = null;
            ArrayList<JSONArray> bookJson = new ArrayList<JSONArray>();
            while(iterator.hasNext()) {
            	Object[] obj=(Object[])iterator.next();
            	String bookid = (String)obj[0]; /*bookid*/
            	ArrayList<String> arrayList = new ArrayList<String>();
            	arrayList.add(bookid); /*add bookid*/
            	Query query1 = session.createQuery("select bookName,author,price from BookEnity where id = ?0");
                //2、填写上一步中占位符的内容
                query1.setParameter(0, bookid);
                List list1 = query1.list();
                Iterator iterator1=list1.iterator();
                Object[] bookInfo = (Object[])iterator1.next();     
                
                tempStr = (String)bookInfo[0]; /*bookname*/
                arrayList.add(tempStr);
                
                tempStr = (String)bookInfo[1]; /*author*/
                arrayList.add(tempStr);
                
                tempStr = (String)bookInfo[2]; /*price*/
                arrayList.add(tempStr);
                
                tempStr = (String)obj[1]; /*amount*/
                arrayList.add(tempStr);
                
                bookJson.add(JSONArray.fromObject(arrayList));
            }
            
            transaction.commit();
            session.close();
            JSONArray books = JSONArray.fromObject(bookJson.toArray());
            
            try {
            	out = response.getWriter();
            	out.println(books);
            }catch (IOException e){
            	e.printStackTrace();
            }finally{
            	out.close();
            }
        }
        
        /*get order*/
        else {
        	Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    	    Transaction transaction = session.beginTransaction();//开启事务
    		Query query = session.createQuery("select autoid,orderid,total from User_order where userid = ?0");
            //2、填写上一步中占位符的内容
            query.setParameter(0, UserState.userID);
            List list = query.list();
            Iterator iterator=list.iterator();
            String temp = null;
            
            ArrayList<JSONArray> orderJson = new ArrayList<JSONArray>();
            while(iterator.hasNext()) {
            	Object[] obj=(Object[])iterator.next();
            	System.out.println(obj[0]);
            	ArrayList<String> arrayList = new ArrayList<String>();
            	int temp1 = (int)obj[0];
            	temp = Integer.toString(temp1);
            	arrayList.add(temp);
            	temp = (String)obj[1];
            	arrayList.add(temp);
            	temp = (String)obj[2];
            	arrayList.add(temp);
            	
            	orderJson.add(JSONArray.fromObject(arrayList));
            }
            
            transaction.commit();
            session.close();
            JSONArray orders = JSONArray.fromObject(orderJson.toArray());
            try {
            	out = response.getWriter();
            	out.println(orders);
            }catch (IOException e){
            	e.printStackTrace();
            }finally{
            	out.close();
            }
        }
        
	}
	
	/*添加用户函数*/
	public static void addOrder() {
		User_order cus = new User_order();
		
		cus.setuser("CJTSAJ");
		cus.setorder("55555555");
		cus.settotal("500");
		
		Session session = null;
	    Transaction transaction = null;
	    
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		    transaction = session.beginTransaction();//开启事务
		    
		    session.save(cus);/*插入数据*/
		    
		    transaction.commit();
		}catch(Exception e) {
			transaction.rollback();/*若发生异常  回滚*/
		}finally {
			session.close();
		}
		
		System.out.println("save successfully!");
	}

}

//JSONObject json=JsonReader.receivePost(request);
//JSONArray itemArray = json.getJSONArray("bookList");
//String[][] arr = (String[][])json.get("bookList");
//String[] s = request.getParameterValues("bookList");
/*for(int i = 0;i<s.length;i++) {
	System.out.println(s[i]);
}*/
//JSONArray itemArray = JSONArray.fromObject(books);

/*for(int i=0;i<itemArray.size();i++){
	ArrayList<String> tempJson = itemArray.getJSONObject(i);
	String tempStr = tempJson.toString();
	tempStr = tempStr.substring(1);
	tempStr = tempStr.substring(0, tempStr.length() - 1);
	String[] stringArr = tempStr.split(",");
	System.out.println(stringArr[1]);
}
System.out.println(itemArray);*/
