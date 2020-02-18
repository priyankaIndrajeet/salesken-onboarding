/**
 * 
 */
package pojos;

/**
 * @author Vaibhav Verma
 *
 */
public class Ticket {
	private String name;
	private String email;
	private String type;
	private String description;
	private String attachment;
	private String subject;

	public Ticket() {
		super();
	}

	public Ticket(String name, String email, String type, String description, String attachment, String subject) {
		super();
		this.name = name;
		this.email = email;
		this.type = type;
		this.description = description;
		this.attachment = attachment;
		this.subject = subject;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

}
