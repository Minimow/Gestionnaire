package dao.daoPOJO;

import java.util.Calendar;

public class Session {
	
	public Session(){
		_id = 0;
		_beginDate = Calendar.getInstance();
		_endDate = Calendar.getInstance();
	}
	
	public void setName(String name){
		_name = name;
	}
	
	public void setBeginDate(Calendar beginDate){
		_beginDate = beginDate;
	}
	
	public void setEndDate(Calendar endDate){
		_endDate = endDate;
	}
	
	public void setId(int id){
		_id = id;
	}
	
	public String getName(){
		return _name;
	}
	
	public Calendar getBeginDate(){
		return _beginDate;
	}
	
	public Calendar getEndDate(){
		return _endDate;
	}
	
	public int getId(){
		return _id;
	}
	
	@Override
	public String toString(){
		return _name + " " + _beginDate.get(Calendar.YEAR);
	}
	
	private String _name;
	private Calendar _beginDate;
	private Calendar _endDate;
	private int _id;

}
