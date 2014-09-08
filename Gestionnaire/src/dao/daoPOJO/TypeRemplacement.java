package dao.daoPOJO;

public class TypeRemplacement {
	
	public TypeRemplacement(){
		_id = 0;
	}
	
	public TypeRemplacement(int typeId, String name){
		_id = typeId;
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
	
	public String toString(){
		return _name;
	}
	private int _id;
	private String _name;
}
