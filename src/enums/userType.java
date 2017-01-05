package enums;

//userType defines the types of users in the system with
//integer code in the database.

public enum userType {
	buyer (0),
	seller (1),
	admin (2);

	public int value;
	
	userType (int value)
	{
		this.value = value;
	}
	
}
