package Service;

import java.util.ArrayList;

import net.sf.json.JSONArray;

public class UserState {
	/*是否登录*/
	public static boolean isLogin = false;
	public static String userID = null;
	
	public static ArrayList<JSONArray> cartJson = new ArrayList<JSONArray>(); /*购物车数据*/
	public void setLogin(boolean log) {
		isLogin = log;
	}
	
	public void setid(String id) {
		userID = id;
	}
	
	public void additem(ArrayList<String> itemList) {
		cartJson.add(JSONArray.fromObject(itemList));
	}
	
	public void removeitem(ArrayList<String> itemList) {
		
	}
	
}
