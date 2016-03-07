function getReview() {
	var serviceRating = parseInt(getRating("serviceRate"));
	var shippingRating = parseInt(getRating("shippingRate"));
	var qualityRating = parseInt(getRating("qualityRate"));
	var comment = document.getElementById("comment").value;
    var companyTitle = document.getElementById("companyName").textContent;

	var Review = Parse.Object.extend("Review");
	var review = new Review();
    var Company = Parse.Object.extend("Company");
    var query = new Parse.Query(Company);
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
    review.set("flagged", false);
    review.set("score", 0);
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
	}).then(function() {
        query.equalTo("Name", companyTitle);
        query.find({
            success: function(results) {
                results[0].set("Searched", true);
                results[0].save();
                window.location.href = "companyProfile.html";
            },
            error: function(object, error) {
                alert("error with posting a review " + error.code);
            }
        })
    })
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

function writeReview() {
    var name = document.getElementById("companyName").textContent;
    
    var Company = Parse.Object.extend("Company");
    var query = new Parse.Query(Company);
    
    query.equalTo("Name", name);
    query.find({
        success: function(results) {
            results[0].set("Searched", true);
            results[0].save();
        },
        error: function(object, error) {
            alert("Error with write review " + error.code + " " + error.message);
        }
    }).then(function() {
        window.location.href = "newreview.html";
    });
}

function checkCompanyReview() {
    var Company = Parse.Object.extend("Company");
    var query = new Parse.Query(Company);
    query.equalTo("Searched", true);
    query.find({
       success: function(results) {
           document.getElementById("companyName").innerHTML = results[0].get("Name");
           results[0].set("Searched", false);
           results[0].save();
       },
       error: function(object, error) {
           alert("Error with checking which company reviewing for " + error.code);
       }
    });
}