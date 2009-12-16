package uk.ac.ox.oucs.vle;

import java.util.Date;

public class OAuthConsumer {

	private String id;
	private String name;
	private String description;
	private String url;
	private Date age;
	
	public OAuthConsumer(String id, String name, String description, String url, Date age) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.url = url;
		this.age = age;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getUrl() {
		return url;
	}

	public Date getAge() {
		return age;
	}

}
