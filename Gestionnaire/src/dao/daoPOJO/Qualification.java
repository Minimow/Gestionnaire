package dao.daoPOJO;

public class Qualification {

	public Qualification() {
		_id = 0;
		_yearsValid = 0;
		_priority = 1;
	}

	public Qualification(int id, String nom, String acronyme, int priorite) {
		_id = id;
		_name = nom;
		_acronyme = acronyme;
		_priority = priorite;
	}

	public int getId() {
		return _id;
	}

	public String getName() {
		return _name;
	}

	public String getAcronyme() {
		return _acronyme;
	}

	public int getPriority() {
		return _priority;
	}

	public int getYearsValid() {
		return _yearsValid;
	}

	public void setId(int id) {
		_id = id;
	}

	public void setName(String name) {
		_name = name;
	}

	public void setAcronyme(String acronyme) {
		_acronyme = acronyme;
	}

	public void setPriority(int priority) {
		_priority = priority;
	}

	public void setYearsValid(int years) {
		_yearsValid = years;
	}

	@Override
	public String toString() {
		return _name;
	}

	private String _name;
	private String _acronyme;
	private int _id;
	private int _yearsValid;
	// The highest priority will make the qualif show first when getting
	// multiple qualif.
	private int _priority;

}
