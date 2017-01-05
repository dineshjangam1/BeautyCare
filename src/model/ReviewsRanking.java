package model;


public class ReviewsRanking {

  public Integer ranking;

  public String review;

  public Integer productId;

  public Integer reviewsRankingId;


  public int getRanking() {
	  return ranking;
  }

  public String getReview() {
	  return review;
  }

  public ReviewsRanking() {
	  this.ranking = -1;
	  this.review = "";
	  this.productId = -1;
	  this.reviewsRankingId = -1;
  }
  
  public ReviewsRanking(int reviewsRankingId, int ranking, String review, int productId) {
	  this.ranking = ranking;
	  this.review = review;
	  this.productId = productId;
	  this.reviewsRankingId = reviewsRankingId;
  }

  public int getProductId() {
	  return productId;
  }

  public int getReviewsRankingId() {
	  return reviewsRankingId;
  }

}