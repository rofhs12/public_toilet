package work.model.dto;

public class Review {
	private int memberId;
	private String memberNickname;

	private String review;
	private int score;
	private String regDate;

	public Review(int memberId) {
		this.memberId = memberId;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public String getMemberNickname() {
		return memberNickname;
	}

	public void setMemberNickname(String memberNickname) {
		this.memberNickname = memberNickname;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{\"memberId\":");
		builder.append(memberId);
		builder.append(",\" memberNickname\":\"");
		builder.append(memberNickname);
		builder.append("\",\" review\":\"");
		builder.append(review);
		builder.append("\", \"score\":");
		builder.append(score);
		builder.append(", \"regDate\":\"");
		builder.append(regDate);
		builder.append("\"}");
		return builder.toString();
	}

	
	
	
	
}
