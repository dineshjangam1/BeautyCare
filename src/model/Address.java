package model;



public class Address {


	
  public String street;

  public String city;

  public String state;

  public String zip;

  public Address() {
		city = "";
		state = "";
		street = "";
		zip = "";
  }
  
  public Address(String city, String state, String street, String zip){
	  this.city = city;
	  this.state = state;
	  this.street = street;
	  this.zip = zip;  
  }


  public String getCity() {
	  return city;
  }

  public String getState() {
	  return state;
  }

  public String getZip() {
	  return zip;
  }
  
  public String getStreet() {
	  return street;
  }

}