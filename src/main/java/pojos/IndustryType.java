/**
 * 
 */
package pojos;

/**
 * @author Vaibhav Verma
 *
 */
public class IndustryType {
	String name; 
	Integer id;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public IndustryType(String name, Integer id) {
		super();
		this.name = name;
		this.id = id;
	}
	public IndustryType() {
		super();
	} 
	
	
}
