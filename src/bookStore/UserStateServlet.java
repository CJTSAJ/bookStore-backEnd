package bookStore;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Service.JsonReader;
import Service.UserState;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class UserStateServlet
 */
@WebServlet("/UserStateServlet")
public class UserStateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserStateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Headers", "accept, content-type");  
	    response.setHeader("Access-Control-Allow-Method", "POST");  
	    response.setHeader("Access-Control-Allow-Origin", "*");
		System.out.println("user state dopost");
		
		response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        
        JSONArray result = null;       
        PrintWriter out = null;
        
        JSONObject json=JsonReader.receivePost(request);
        
        String op = json.getString("operation");
        
        if(UserState.isLogin) {
        	if(op.equals("add")) {
            	ArrayList<String> arrayList = new ArrayList<String>();
                arrayList.add(json.getString("id"));
                arrayList.add(json.getString("bookName"));
                arrayList.add(json.getString("author"));
                arrayList.add(json.getString("price"));
                arrayList.add(json.getString("amount"));
                System.out.println(arrayList);
                UserState state = new UserState();
                state.additem(arrayList);
                
                ArrayList<String> temp = new ArrayList<String>();
                temp.add("true");
                ArrayList<JSONArray> temp1 = new ArrayList<JSONArray>();
                temp1.add(JSONArray.fromObject(temp));
                result = JSONArray.fromObject(temp1.toArray());
                
            }
        	else if(op.equals("get")) {
        		result = JSONArray.fromObject(UserState.cartJson.toArray());
        	}
        	else if(op.equals("islogin")) {
        		ArrayList<String> temp = new ArrayList<String>();
                temp.add("true");
                ArrayList<JSONArray> temp1 = new ArrayList<JSONArray>();
                temp1.add(JSONArray.fromObject(temp));
                result = JSONArray.fromObject(temp1.toArray());
        	}
        	else if(op.equals("logout")) {
        		UserState.isLogin = false;
        		UserState.cartJson.clear();
        		UserState.userID = null;
        		
        		ArrayList<String> temp = new ArrayList<String>();
        		temp.add("true");
                ArrayList<JSONArray> temp1 = new ArrayList<JSONArray>();
                temp1.add(JSONArray.fromObject(temp));
                result = JSONArray.fromObject(temp1.toArray());
        	}
        	else if(op.equals("delete")) {
        		String temp = json.getString("bookIndex");
        		int bookIndex = Integer.parseInt(temp);
        		UserState.cartJson.remove(bookIndex);
        		result = JSONArray.fromObject(UserState.cartJson.toArray());
        	}
        }
        else {
        	ArrayList<String> temp = new ArrayList<String>();
            temp.add("false");
            ArrayList<JSONArray> temp1 = new ArrayList<JSONArray>();
            temp1.add(JSONArray.fromObject(temp));
            result = JSONArray.fromObject(temp1.toArray());
        }
        
        try {
            out = response.getWriter();
            out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
	}

}
