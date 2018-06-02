package bookStore;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import bookStore.UserEnity;
import net.sf.json.JSONObject;
import bookStore.HibernateUtil;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
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
		System.out.println("user get");
		getAllUser();
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
		System.out.println("user post");
		
		/*BufferedReader reader = request.getReader();
        String json = reader.readLine();
        System.out.println(json);*/
		
		response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        String jsondata = null;       
        PrintWriter out = null;
        
        
        
        JSONObject json=JsonReader.receivePost(request);
        
        String op = json.getString("operation");
        
        /*添加账户*/
        if(op.equals("add")) {
        	System.out.println(json.getString("phone"));
            System.out.println(json.getString("email"));
            System.out.println(json.getString("id"));
            System.out.println(json.getString("password"));
            System.out.println(json.getString("operation"));
            
            addUser(json);
        }
        
        /*登录请求*/
        else if(op.equals("login")) {
        	if(json.getString("id").equals("admin")) { /*admin login*/
        		if(json.getString("password").equals("123456")) {
        			jsondata = "{\"result\":\"admin\"}";
        		}
        		else {
        			jsondata = "{\"result\":\"wrongPwd\"}";
        		}
        	}
        	else { /*user login*/
        		String result = login(json);
            	if(result.equals("wrongID")) {
            		jsondata = "{\"result\":\"wrongID\"}";  /*"{\"result\":\"1\",\"success\":true}";*/
            	}
            	else if(result.equals("wrongPwd")) {
            		jsondata = "{\"result\":\"wrongPwd\"}";
            	}
            	else if(result.equals("rightPwd")) {
            		jsondata = "{\"result\":\"rightPwd\"}";
            		UserState.isLogin = true;  /*设置登录状态*/
            	}
        	}
        	
        }
        /*请求注册*/
        else if(op.equals("register")) {
        	if(json.getString("id").equals("admin")) {
        		jsondata = "{\"result\":\"false\"}";
        	}
        	else {
        		String result = register(json);
            	if(result.equals("true")) {
            		jsondata = "{\"result\":\"true\"}";
            	}
            	else {
            		jsondata = "{\"result\":\"false\"}";
            	}
        	}
        }
        
        else if(op.equals("delete")) {
        	String result = deleteUser(json.getString("id"));
        	if(result.equals("ok")) {
        		jsondata = "{\"result\":\"true\"}";
        	}
        	else {
        		jsondata = "{\"result\":\"false\"}";
        	}
        }
    
        try {
            out = response.getWriter();
            out.write(jsondata);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
	}
	public static String deleteUser(String delid) {
		System.out.println("dlete");
		
		String result = null;
		Session session = null;
	    Transaction transaction = null;
	    
	    try {
	    	session = HibernateUtil.getSessionFactory().getCurrentSession();
		    transaction = session.beginTransaction();//开启事务
		
		    UserEnity customer=(UserEnity)session.get(UserEnity.class, delid);
		    
		    if(customer == null) {
		    	System.out.println("no exist!");
		    	result = "wrongID";
		    }
		    else {
		    	Query query = session.createQuery("delete from UserEnity where id = ?0");
		    	query.setParameter(0,delid);
		    	query.executeUpdate();
		    	result = "ok";
		    }
		    transaction.commit();
	    }catch (Exception e) {
	    	transaction.rollback();
	    }finally{
	    	session.close();
	    }
	    return result;
	}
	
	
	/*登录匹配函数*/
	public static String login(JSONObject json) {
		System.out.println("login");
		
		String result = null;
		Session session = null;
	    Transaction transaction = null;
	    
	    try {
	    	session = HibernateUtil.getSessionFactory().getCurrentSession();
		    transaction = session.beginTransaction();//开启事务
		    
		    String loginId = json.getString("id");
		    String loginPwd = json.getString("password");
		    
		    System.out.println(loginPwd);
		    UserEnity customer=(UserEnity)session.get(UserEnity.class, loginId);
		    
		    if(customer == null) {
		    	System.out.println("no exist!");
		    	result = "wrongID";
		    }
		    else if(customer.getPassword().equals(loginPwd)) {
		    	System.out.println("right password!");
		    	result = "rightPwd";
		    	UserState.userID = loginId;
		    }
		    else {
		    	System.out.println("wrong password!");
		    	result = "wrongPwd";
		    }
		    transaction.commit();
	    }catch (Exception e) {
	    	transaction.rollback();
	    }finally{
	    	session.close();
	    }
	    return result;
	}
	
	/*添加用户函数*/
	public static void addUser(JSONObject json) {
		UserEnity cus = new UserEnity();
		
		cus.setId(json.getString("id"));
		cus.setPassword(json.getString("password"));
		cus.setEmail(json.getString("email"));
		cus.setPhone(json.getString("phone"));
		Session session = null;
	    Transaction transaction = null;
	    
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		    transaction = session.beginTransaction();//开启事务
		    System.out.println("save successfully!");
		    session.save(cus);/*插入数据*/
		    
		    transaction.commit();
		}catch(Exception e) {
			transaction.rollback();/*若发生异常  回滚*/
		}finally {
			session.close();
		}
		
		System.out.println("save successfully!");
	}
	
	/*注册函数 成功返回true 否则返回false*/
	public static String register(JSONObject json) {
		System.out.println("register");
		
		String result = null;
		Session session = null;
	    Transaction transaction = null;
	    
	    try {
	    	session = HibernateUtil.getSessionFactory().getCurrentSession();
		    transaction = session.beginTransaction();//开启事务
		    
		    String regId = json.getString("id");
		    
		    UserEnity customer=(UserEnity)session.get(UserEnity.class, regId);
		    
		    if(customer == null) {
		    	System.out.println("bingo!");
		    	
		    	UserEnity cus = new UserEnity();
				
				cus.setId(regId);
				cus.setPassword(json.getString("password"));
				cus.setEmail(json.getString("email"));
				cus.setPhone(json.getString("phone"));
				
				System.out.println("save successfully!");
				session.save(cus);/*插入数据*/				    
				
		    	result = "true";
		    }
		    else {
		    	System.out.println("false!");
		    	result = "false";
		    }
		    transaction.commit();
	    }catch (Exception e) {
	    	transaction.rollback();
	    }finally{
	    	session.close();
	    }
	    return result;
	}
	
	public static void getAllUser() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    	Transaction transaction = session.beginTransaction();//开启事务
	    try {
	    	/*Query query =session.createQuery("from customer");
            List<Customer> list = query.list();*/
	    	
	    	Criteria criteria =session.createCriteria(UserEnity.class);
            List<UserEnity> list = criteria.list();
            
            for(UserEnity cus : list) {
            	//System.out.println(cus);
            	System.out.println(cus.getId());
                System.out.println(cus.getPassword());
                System.out.println(cus.getEmail());
                System.out.println(cus.getPhone());
            }
            transaction.commit();
	    }catch (Exception e){
	    	transaction.rollback();
	    }finally {
	    	session.close();
	    }
	}
}



