package enums;

public enum ListType {
	order (0),
	shoppingCart (1),
	wish (2);

	public int value;
	
	ListType (int value)
	{
		this.value = value;
	}
}
