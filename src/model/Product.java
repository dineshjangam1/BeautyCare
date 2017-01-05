package model;

import java.util.Vector;
import java.util.ListIterator;


public class Product {

  public String name;

  public String description;

  public Integer productId;

  public Integer department;

  public Integer isDeleted;

    /**
   * 
   * @element-type Image
   */
  public Vector<Image>  images;
    /**
   * 
   * @element-type ProductSeller
   */
  public Vector<ProductSeller>  sellers;
    /**
   * 
   * @element-type ReviewsRanking
   */
  public Vector<ReviewsRanking>  reviewsRankings;

  public ListIterator<ProductSeller> getSellers()
  {
	  return sellers.listIterator();
  }
  
  public ListIterator<Image> getImages()
  {
	  return images.listIterator();
  }
  
  public ListIterator<ReviewsRanking> getReviewsRanking()
  {
	  return reviewsRankings.listIterator();
  }

  public String getName() {
	  return name;
  }

  public String getDescription() {
	  return description;
  }

  public int getProductId() {
	  return productId;
  }

  public int getDepartment() {
	  return department;
  }

  public int getIsDeleted() {
	  return isDeleted;
  }

  public Product() {
	  this.productId = -1;
	  this.name= "";
	  this.description="";
	  this.department = -1;
	  this.isDeleted = 0;
  }
  
  public Product(int productId, String name, String description, int department, int isDeleted) {
	  this.productId = productId;
	  this.name=name;
	  this.description=description;
	  this.department = department;
	  this.isDeleted = isDeleted;
  }
  
  //Returns productSeller data if exists, else null
  public ProductSeller getProductSellerById(int sellerId) {
	  ProductSeller seller = null;
	  ProductSeller temp = null;
	  ListIterator<ProductSeller> sellersIterator = sellers.listIterator();
	  while(sellersIterator.hasNext())
	  {
		  temp = sellersIterator.next();
		  if(temp.getProductSellerId() == sellerId)
		  {
			  seller = temp;
			  break;
		  }
	  }
	  temp = null;
	  return seller;
  }
  
  //Returns productSeller data if exists, else null
  public ProductSeller getProductSellerBySellerId(int sellerId) {
	  ProductSeller seller = null;
	  ProductSeller temp = null;
	  ListIterator<ProductSeller> sellersIterator = sellers.listIterator();
	  while(sellersIterator.hasNext())
	  {
		  temp = sellersIterator.next();
		  if(temp.getSellerId() == sellerId)
		  {
			  seller = temp;
			  break;
		  }
	  }
	  temp = null;
	  return seller;
  }

}