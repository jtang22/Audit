function getReview() {
	var serviceRating = parseInt(getRating("serviceRate"));
	var shippingRating = parseInt(getRating("shippingRate"));
	var qualityRating = parseInt(getRating("qualityRate"));
	var comment = document.getElementById("comment").value;

	var Review = Parse.Object.extend("Review");
	var review = new Review();

	review.set("companyId", "placeholderCompany");
	//review.set("userId", Parse.User.current().id);
	review.set("userId", "placeholderUser");
	review.set("serviceRating", serviceRating);
	review.set("shippingRating", shippingRating);
	review.set("qualityRating", qualityRating);
	review.set("comment", comment);

	review.save(null, {
		success: function(review) {
	    	// Execute any logic that should take place after the object is saved.
	    	alert('New object created with objectId: ' + review.id);
	  	},
	  	error: function(review, error) {
	    	// Execute any logic that should take place if the save fails.
	    	// error is a Parse.Error with an error code and message.
	    	alert('Failed to create new object, with error code: ' + error.message);
	  	}
	});
}

function getRating(rateName) {
	var rates = document.getElementsByName(rateName);
	var rateValue;

	for (var i = 0; i < rates.length; i++) {
		if (rates[i].checked) {
			rateValue = rates[i].value;
		}
	}

	return rateValue;
}