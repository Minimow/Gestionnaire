package dao.daoPOJO;

import java.util.Calendar;

public class Remplacement {
	
	public Remplacement(){
		_requester = 0;
		_taker = 0;
	}
	
	public Remplacement(int demandeurId, int preneurId, Calendar dateDebut,
			Calendar dateFin, String type, String raison, String detail, boolean approuve){
		_requester = demandeurId;
		_taker = preneurId;
		_isApproved = approuve;
		_details = detail;
		_reason = raison;
		_type = type;
		_beginDate = dateDebut;
		_endDate = dateFin;
	}
	
	public int getRequester(){
		return _requester;
	}
	
	public int getTaker(){
		return _taker;
	}
	
	public String getDetails(){
		return _details;
	}
	
	public String getType(){
		return _type;
	}
	
	public Calendar getBeginDate(){
		return _beginDate;
	}
	
	public Calendar getEndDate(){
		return _endDate;
	}
	
	public String getReason(){
		return _reason;
	}
	
	public boolean isApproved(){
		return _isApproved;
	}
	
	public void setRequester(int requesterId){
		_requester = requesterId;
	}
	
	public void setTaker(int takerId){
		_taker = takerId;
	}
	
	public void setDetails(String details){
		_details = details;
	}
	
	public void setType(String type){
		_type = type;
	}
	
	public void setBeginDate(Calendar beginDate){
		_beginDate = beginDate;
	}
	
	public void setEndDate(Calendar endDate){
		_endDate = endDate;
	}
	
	public void setApproved(boolean isApproved){
		_isApproved = isApproved;
	}
	
	public void setReason(String reason){
		_reason = reason;
	}
	
	public void setSession(String session){
		_session = session;
	}
	
	public String getSession(){
		return _session;
	}
	
	private int _requester;
	private int _taker;
	private boolean _isApproved;
	private String _details;
	private String _type;
	private String _reason;
	private String _session;
	private Calendar _beginDate;
	private Calendar _endDate;

}
