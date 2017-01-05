package model;

public class Image {

  public String location;

  public Integer imageId;

  public Integer productId;


  public String getLocation() {
	  return this.location;
  }

  public Image() {
	  this.location = "";
	  this.imageId = -1;
	  this.productId = -1;
  }
  
  public Image(int imageId, int productId, String location) {
	  this.location = location;
	  this.imageId = imageId;
	  this.productId = productId;
  }

  public int getProductId() {
	  return this.productId;
  }
  
  public int getImageId() {
	  return this.imageId;
  }

}