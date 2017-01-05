/**
 * 
 */
package enums;


public enum Department {
	Makeup (0),
	Skin (1),
	Fragrance (2),
	Tools (3);

	public int value;
	
	Department (int value)
	{
		this.value = value;
	}
	
}
