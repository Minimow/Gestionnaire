package dao.daoPOJO;

public class RaisonRemplacement {
	
	public RaisonRemplacement(){
		_id = 0;
	}
	
	public RaisonRemplacement(int id, String name){
		_id = id;
		_name = name;
	}
	
	public int getId(){
		return _id;
	}
	
	public String getName(){
		return _name;
	}
	
	public void setId(int id){
		_id = id;
	}
	
	public void setName(String name){
		_name = name;
	}
	
	@Override
	public String toString(){
		return _name;
	}
	
	private int _id;
	private String _name;
}