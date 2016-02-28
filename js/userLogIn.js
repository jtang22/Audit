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
    var para;
    var node;
    var elementName = document.getElementById("userName");
    name = user.get("firstname") + " " + user.get("lastname");
    reviews = user.get("reviews");
    numReviews = reviews.length;
    
    // member name label
    para = document.createElement("label");
    node = document.createTextNode(name);
    para.appendChild(node);
    elementName.appendChild(para);
    
    // number of reviews
    var elementNumReview = document.getElementById("numReviews");
    
    para = document.createElement("p");
    if(reviews.length == 1) {
        node = document.createTextNode(numReviews + " Review")
    }
    else {
        node = document.createTextNode(numReviews + " Reviews");
    }
    para.appendChild(node);
    elementNumReview.appendChild(para);
    
    //displays all reviews
    
    var elementReviews = document.getElementId("displayReview");
    //var elementReviewDescr = document.getElementId("reviewDescription");
    for (var i = 0; i < numReviews; i++) {
        para = document.createElement("label");
        node = document.createTextNode(reviews[i]);
        para.appendChild(node);
        elementReviews.appendChild(para);
    }
    
}

function loadCompanyData() {
    checkUserLoggedIn();
    var name;
    var image;
    var url;
    
    var Company = Parse.Object.extend("Company");
    var query = new Parse.Query(Company);
    
    query.get("fbe2ipKebQ", {
        success: function(company) {
            //when successful
            var reviews;
            var para;
            var node;
            name = company.get("Name");
            image = company.get("Image");
            url = company.get("Url");


            var companyName = document.getElementById("companyName");
            // company name label
            para = document.createElement("label");
            node = document.createTextNode(name);
            para.appendChild(node);
            companyName.appendChild(para);

            var companyWeb = document.getElementById("companyWebsite");
            para = document.createElement("p");
            node = document.createTextNode(url);
            para.appendChild(node)
            companyWeb.appendChild(para);
            
            //insert reviews
            var elementReviews = document.getElementId("displayReview");
            //var elementReviewDescr = document.getElementId("reviewDescription");
            for (var i = 0; i < numReviews; i++) {
                para = document.createElement("label");
                node = document.createTextNode(reviews[i]);
                para.appendChild(node);
                elementReviews.appendChild(para);
    }
          
        },
        
        error: function(object, error) {
            //error checking stuff
        }
    });
}