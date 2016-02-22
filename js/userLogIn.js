function loginUser() {
    var username = document.getElementById("loginUsername").value;
    var password = document.getElementById("loginPassword").value;
    
    Parse.User.logIn(username, password, {
       success: function(user) {
            alert("Signed in!");
            showLogOut();
       }, 
        error: function(user, error) {
            alert("error!" + error.code + " " + error.message);
        }
    });
    event.preventDefault();
}

function logoutUser() {
    Parse.User.logOut();
    alert("Signed Out!");
    showLogIn();
    var title = document.getElementsByTagName("title")[0].innerHTML;
    if(title.localeCompare("User Profile") == 0) {
        alert("I should be going home");
        window.location.href = "home.html";
    }
}

function showLogOut() {
    document.getElementById("loginUsername").style.display = 'none';
    document.getElementById("loginPassword").style.display = 'none';
    document.getElementById("signInButton").style.display = 'none';
    document.getElementById("signUpButton").style.display = 'none';
    document.getElementById("passwordReset").style.display = 'none';
    document.getElementById("signOutButton").style.display = '';
    document.getElementById("profileButton").style.display = '';    
}

function showLogIn() {
    document.getElementById("loginUsername").style.display = '';
    document.getElementById("loginPassword").style.display = '';
    document.getElementById("signInButton").style.display = '';
    document.getElementById("signUpButton").style.display = '';
    document.getElementById("passwordReset").style.display = '';
    document.getElementById("signOutButton").style.display = 'none';
    document.getElementById("profileButton").style.display = 'none';    
}

function checkUserLoggedIn() {
    var user = Parse.User.current();
    if(user) {
        showLogOut();
    }
    else {
        showLogIn();
    }
}

function checkEmail(email)
{
    var atPosition = email.indexOf("@");
    var dotPosition = email.lastIndexOf(".");

    if (atPosition < 1 || dotPosition < atPosition + 2 || dotPosition + 2 >= email.length) 
    {
        return false;
    }
    return true;
}

function resetPassword()
{           
    var email = document.getElementById("email").value;
    var emailIsValid = checkEmail(email);

    if (!emailIsValid)
    {
        window.alert("Not a valid e-mail address");
        return false;
    }

    Parse.User.requestPasswordReset(email, {
                success:function() {
                    window.alert("Password reset link has been sent to "+ email + ".");
                    return true;
                },
                error:function(error) {
                    window.alert("Could not send reset link to " + email + ". Please try again." + error.code + " " + error.message);
                    return false;
                }
    });
}

function validateForm() {
    var x = document.forms["myForm"]["fname"].value;
    if (x == null || x == "") {
        alert("Name must be filled out");
        return false;
    }
}

function loadUserData() {
    checkUserLoggedIn();
    document.getElementById("profileButton").style.display = 'none';

    var user = Parse.User.current();
    var name;
    var numReviews;
    var reviews;
    var elementName = document.getElementById("userName");
    name = user.get("firstname") + " " + user.get("lastname");
    
    // member name label
    para = document.createElement("label");
    node = document.createTextNode(name);
    para.appendChild(node);
    elementName.appendChild(para);
    
    //displays all reviews
    
    var elementReviews = document.getElementById("displayReview");
    //var elementReviewDescr = document.getElementId("reviewDescription");
    var Review = Parse.Object.extend("Review");
    var query = new Parse.Query(Review);
    query.equalTo("userId", user.get("username"));
    query.find({
       success: function(userReviews) {
                       // number of reviews
            var elementNumReview = document.getElementById("numReviews");
            var numReviews = userReviews.length;
            para = document.createElement("p");
            if(numReviews == 1) {
                node = document.createTextNode(numReviews + " Review")
            }
            else {
                node = document.createTextNode(numReviews + " Reviews");
            }
            para.appendChild(node);
            elementNumReview.appendChild(para);
           
           for(var i = 0; i < userReviews.length; i++) {
                var reviewDiv = document.createElement("div");
                reviewDiv.className = 'row';
               
                var companyPara = document.createElement("h2");
                var companyNode = document.createTextNode(userReviews[i].get("companyId"));
                companyPara.appendChild(companyNode);
                reviewDiv.appendChild(companyPara);
                //elementReviews.appendChild(companyDiv);
               
                var shipPara = document.createElement("h4");
                var shipNode = document.createTextNode("Shipping:" + userReviews[i].get("shippingRating"));
                shipPara.appendChild(shipNode);
                //elementReviews.appendChild(shipPara);
                reviewDiv.appendChild(shipPara);
               
                var servPara = document.createElement("h4");
                var servNode = document.createTextNode("Service:" + userReviews[i].get("serviceRating"));
                servPara.appendChild(servNode);
                //elementReviews.appendChild(servPara);
                reviewDiv.appendChild(servPara);
               
                var qualPara = document.createElement("h4");
                var qualNode = document.createTextNode("Quality:" + userReviews[i].get("qualityRating"));
                qualPara.appendChild(qualNode);
                //elementReviews.appendChild(qualPara);
               reviewDiv.appendChild(qualPara);
               
               var commentPara = document.createElement("p");
               var commentNode = document.createTextNode(userReviews[i].get("comment"));
               commentPara.appendChild(commentNode);
               //elementReviews.appendChild(commentPara);
               reviewDiv.appendChild(commentPara);
               var lineBreak = document.createElement("hr");
               reviewDiv.appendChild(lineBreak);
               
               elementReviews.appendChild(reviewDiv);

           }
       } 
    });
    
    /*for (var i = 0; i < numReviews; i++) {
        shipPara = document.createElement("label");
        shipNode = document.createTextNode("Shipping:" + reviews[i].get("shippingRating"));
        shipPara.appendChild(shipNode);
        elementReviews.appendChild(shipPara);
        
    }*/
    
}