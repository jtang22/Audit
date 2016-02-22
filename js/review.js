function getReview() {
	var serviceRating = parseInt(getRating("serviceRate"));
	var shippingRating = parseInt(getRating("shippingRate"));
	var qualityRating = parseInt(getRating("qualityRate"));
	var comment = document.getElementById("comment").value;
    var companyTitle = document.getElementById("companyName").innerHTML;

	var Review = Parse.Object.extend("Review");
	var review = new Review();
    
    var user = Parse.User.current();
    user.set("numReviews", user.get("numReviews") + 1);
    user.save();

	review.set("companyId", companyTitle);
	//review.set("userId", Parse.User.current().id);
	review.set("userId", Parse.User.current().get("username"));
	review.set("serviceRating", serviceRating);
	review.set("shippingRating", shippingRating);
	review.set("qualityRating", qualityRating);
	review.set("comment", comment);

    /*var reviews = user.get("reviews");
    reviews.push(review);
    user.set("reviews", reviews);
    user.save(null, {
        success: function() {
            alert("successfully added review to " + user.get("firstname") + " " + user.get("lastname") + "'s profile");
        },
        error: function(error) {
            alert("failed to add review, with error code: " + error.message);
        }
    });*/
    
	review.save(null, {
		success: function(review) {
	    	// Execute any logic that should take place after the object is saved.
	    	alert('New object created with objectId: ' + review.get("userId"));
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