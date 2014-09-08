package dao.daoPOJO;

import java.util.Calendar;

public class EmployeQualifications extends Qualification{

	public EmployeQualifications(){
		super();
		_dateQualification = Calendar.getInstance();
		_dateExpiration = Calendar.getInstance();
		_employeeId = 0;
	}
	
	public EmployeQualifications(int employeeId, Calendar dateQualification, Calendar dateExpiration){
		super();
		_dateQualification = dateQualification;
		_dateExpiration = dateExpiration;
		_employeeId = employeeId;
	}
	
	public Calendar getDateQualification(){
		return _dateQualification;
	}
	
	public Calendar getDateExpiration(){
		return _dateExpiration;
	}
	
	public int getEmployeeId(){
		return _employeeId;
	}
	
	public void setDateQualification(Calendar date){
		_dateQualification = date;
	}
	
	public void setDateExpiration(Calendar date){
		_dateExpiration = date;
	}
	
	public void setEmployeeId(int employeeId){
		_employeeId = employeeId;
	}
	
	private Calendar _dateQualification;
	private Calendar _dateExpiration;
	private int _employeeId;
}
