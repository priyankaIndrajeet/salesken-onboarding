package pojos;

public class Currency {
	private Integer id;
	private String name;

	public Currency() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Currency(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
