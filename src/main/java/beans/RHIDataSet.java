package beans;

public class RHIDataSet {
	
	/** The card holder id. */
	public String cardHolderId;

	/** The email. */
	public String email;
	
	/** The first name. */
	public String firstName;
	
	/** The last name. */
	public String lastName;

	/** The date of birth. */
	public String dateOfBirth;
	
	/**
	 * Instantiates a new RHI data set.
	 *
	 * @param email the email
	 * @param cardHolderId the card holder id
	 * @param firstName the first name
	 * @param lastName the last name
	 * @param dateOfBirth the date of birth
	 */
	public RHIDataSet(String email, String cardHolderId, String firstName,String lastName,String dateOfBirth) {
		this.email = email;
		this.cardHolderId = cardHolderId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;

	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return email + "," + cardHolderId + "," + firstName + "," + lastName + "," + dateOfBirth;
	}

}
