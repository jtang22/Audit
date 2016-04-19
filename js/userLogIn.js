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
    
    //Get user profile picture
    var imageFile = user.get("picture");
    if(imageFile != null) {
        var imageURL = imageFile.url();
        document.getElementById("profilepic").src = imageURL;
    }
    else {
        document.getElementById("profilepic").src = "https://u.o0bc.com/avatars/no-user-image.gif";
    }
    
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
                var companyName = userReviews[i].get("companyId");
                companyPara.onclick = (function() {
                        var company = companyName;
                        return function() {
                            document.cookie = "companySearch=" + company;
                            window.location.href = "companyProfile.html";
                        }
                    })();
                var companyNode = document.createTextNode(companyName);
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
               
               var foundHelpfulPara = document.createElement("p");
               var foundHelpfulNode = document.createTextNode(userReviews[i].get("score") + " user\(s\) found this review helpful");
               foundHelpfulPara.appendChild(foundHelpfulNode);
               reviewDiv.appendChild(foundHelpfulPara);
               
               var lineBreak = document.createElement("hr");
               reviewDiv.appendChild(lineBreak);
               elementReviews.appendChild(reviewDiv);

           }
       } 
    });
    
}

function loadCompanyData() {
    checkUserLoggedIn();
    var name;
//    var image;
//    var url;
    
    var Company = Parse.Object.extend("Company");
    var query = new Parse.Query(Company);
    //query.equalTo("Searched", true);
    query.equalTo("Name", getCookie("companySearch"))
    query.find({
        success: function(results) {
            //when successful
            var reviews;
            var para;
            var node;
            name = results[0].get("Name");
            var imageFile = results[0].get("Image");
            var url = results[0].get("Url");

            results[0].set("Searched", false);
            results[0].save();

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
            
            if(imageFile != null) {
                var imageURL = imageFile.url()
                document.getElementById("companyImage").setAttribute("src",imageURL);        
            }
            else {
                document.getElementById("companyImage").setAttribute("src", "https://u.o0bc.com/avatars/no-user-image.gif");
            }
            
        
        },
        error: function(object, error) {
            //error checking stuff
        }
    }).then(function() {
    
        //displays all reviews
        var elementReviews = document.getElementById("displayReview");
        //var elementReviewDescr = document.getElementId("reviewDescription");
        var Review = Parse.Object.extend("Review");
        var query = new Parse.Query(Review);
        var flagButton;
        var helpfulText = "Was this review helpful?";
        var helpfulButton;
        var notHelpfulButton;
        var userReviewExists = false;
        var user = Parse.User.current();
        var shipping = 0;
        var quality = 0;
        var service = 0;
        var reviewTable = document.getElementById("reviewTable");
        var infoCell;
        var reviewCell;
        var curRow;
        query.equalTo("companyId", name);
        query.find({
           success: function(userReviews) {
               for(var i = 0; i < userReviews.length; i++) {
                    if(user) {
                        if(user.get("username").localeCompare(userReviews[i].get("userId"))) {
                            userReviewExists = true;
                        }
                    }
                    var reviewDiv = document.createElement("div");
                    curRow = reviewTable.insertRow(0);
                    infoCell = curRow.insertCell(0);
                    reviewCell = curRow.insertCell(1);
                    flagButton = document.createElement("button");
                    flagButton.innerHTML = "Flag";
                    flagButton.onclick = (function() {
                        var thisNdx = i;
                        return function() {
                            var thisReview = userReviews[thisNdx];
                            thisReview.set("flagged", true);
                            thisReview.save();
                        }
                    })();
                    helpfulButton = document.createElement("button");
                    helpfulButton.innerHTML = "Helpful";
                    helpfulButton.onclick = (function() {
                        var thisNdx = i;
                        return function() {
                            var thisReview = userReviews[thisNdx];
                            var curScore = thisReview.get("score");
                            curScore++;
                            thisReview.set("score", curScore);
                            thisReview.save();
                        }
                    })();
                    notHelpfulButton = document.createElement("button");
                    notHelpfulButton.innerHTML = "Not Helpful";
                    notHelpfulButton.onclick = (function() {
                        var thisNdx = i;
                        return function() {
                            var thisReview = userReviews[thisNdx];
                            var curScore = thisReview.get("score");
                            if(curScore > 0) {
                                curScore--;
                                thisReview.set("score", curScore);
                                thisReview.save();
                            }
                        }
                    })();
                    service += userReviews[i].get("serviceRating");
                    shipping += userReviews[i].get("shippingRating");
                    quality += userReviews[i].get("qualityRating");
                    if(userReviews[i].get("flagged") != true) {
                        reviewDiv.className = 'row';
                        var userDiv = document.createElement('div');
                        var commentDiv = document.createElement('div');
                        var userImg = document.createElement("img");
                        var imgUrl;

                        userDiv.style.cssFloat = "left";
                        userDiv.style.clear = "both";
                        commentDiv.className = 'userReview';
                        
                        var companyPara = document.createElement("h4");
                        var companyNode = document.createTextNode(userReviews[i].get("userId"));
                        companyPara.appendChild(companyNode);
                        infoCell.appendChild(companyPara);
                        userDiv.appendChild(companyPara);
                        
                        //Get user profile picture
                        var imgQuery = new Parse.Query(Parse.User);
                        imgQuery.equalTo("username", userReviews[i].get("userId"));
                        imgQuery.find({
                            success:function(user) {
                                var imageFile = user[0].get("picture");
                                if(imageFile != null) {
                                imgUrl = imageFile.url();

                                }
                                else {
                                    imgUrl = "https://u.o0bc.com/avatars/no-user-image.gif";
                                }

                                userImg.setAttribute("height", "50");
                                userImg.setAttribute("width", "50");
                                userImg.src = imgUrl;
                                userDiv.appendChild(userImg);
                                infoCell.appendChild(userImg);
                                
                            }
                        });
                        
                        var shipPara = document.createElement("h4");
                        var shipNode = document.createTextNode("Shipping:" + userReviews[i].get("shippingRating"));
                        shipPara.appendChild(shipNode);
                        userDiv.appendChild(shipPara);
                        infoCell.appendChild(shipPara);
                        var servPara = document.createElement("h4");
                        var servNode = document.createTextNode("Service:" + userReviews[i].get("serviceRating"));
                        servPara.appendChild(servNode);
                        userDiv.appendChild(servPara);
                        infoCell.appendChild(servPara);

                        var qualPara = document.createElement("h4");
                        var qualNode = document.createTextNode("Quality:" + userReviews[i].get("qualityRating"));
                        qualPara.appendChild(qualNode);
                        userDiv.appendChild(qualPara);
                        infoCell.appendChild(qualPara);

                        var commentPara = document.createElement("p");
                        var commentNode = document.createTextNode(userReviews[i].get("comment"));
                        commentPara.appendChild(commentNode);
                        reviewCell.appendChild(commentPara);
                        
                        var foundHelpfulPara = document.createElement("p");
                        var foundHelpfulNode = document.createTextNode(userReviews[i].get("score") + " user\(s\) found this review helpful");
                        foundHelpfulPara.appendChild(foundHelpfulNode);
                        commentDiv.appendChild(commentPara);
                        reviewDiv.appendChild(userDiv);
                        reviewDiv.appendChild(commentDiv);
                        var br = document.createElement("br");
                        reviewDiv.appendChild(br);
                        reviewDiv.appendChild(foundHelpfulPara);
                        reviewDiv.appendChild(flagButton);
                        reviewDiv.appendChild(helpfulButton);
                        reviewDiv.appendChild(notHelpfulButton);
                        var lineBreak = document.createElement("hr");
                        reviewDiv.appendChild(lineBreak);
                        //elementReviews.appendChild(reviewDiv);

                    }

               }
               service /= userReviews.length;
               shipping /= userReviews.length;
               quality /= userReviews.length;
               
               var totalRating = (service + shipping + quality) / 3;
               setStars("avgRating", totalRating)
               setStars("shippingRating", service)
               setStars("qualityRating", quality)
               setStars("serviceRating", service)
                
                
//                //company customer service rating
//                var companyService = document.getElementById("companyService");
//                para = document.createElement("p");
//                node = document.createTextNode("Customer Service: " + service);
//                para.appendChild(node)
//                companyService.appendChild(para);
//
//                //company shipping rating
//                var companyShipping = document.getElementById("companyShipping");
//                para = document.createElement("p");
//                node = document.createTextNode("Shipping: " + shipping);
//                para.appendChild(node)
//                companyShipping.appendChild(para);
//
//                //company quality rating
//                var companyQuality = document.getElementById("companyQuality");
//                para = document.createElement("p");
//                node = document.createTextNode("Quality: " + quality);
//                para.appendChild(node)
//                companyQuality.appendChild(para);
               if(!userReviewExists) {
                   document.getElementById("writeReview").style.display = '';
               }
           } 
        });
    });
}

function setPicture() {
    var picture = document.form1.filesent.files;
    var user = Parse.User.current();
    var name = user.get("firstname") + " " + user.get("lastname");
    var parseImage = new Parse.File(name, picture[0]);
    user.set("picture", parseImage);
    user.save().then(function() {

        var profilePhoto = user.get("picture");
        //Get user profile picture
        var imageFile = user.get("picture");
        var imageURL = imageFile.url();
        document.getElementById("profilepic").src = imageURL;
    });
}
    /*user = Parse.User.current();
    user.fetch({
        success: function(objUser) {              
            objUser.set("picture", picture);
            objUser.save(null, {
                success: function() {
                    alert("File successfully uploaded.");
                },
                error: function() {
                    alert("Failed to upload file.");
                }
            });
        },
        error: function(objUser, error) {
            alert("Could not fetch user.");
        }
    });*/

function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1);
        if (c.indexOf(name) == 0) return c.substring(name.length,c.length);
    }
    return "";
}


function setStars(fieldName, totalRating) {
    var ratingButton;
    for(var i = 0; i < Math.floor(totalRating); i++) {
        switch(i) {
           case 0:
                ratingButton = document.getElementById(fieldName + "1");
                break;
            case 1:
                ratingButton = document.getElementById(fieldName + "2");
                break;
            case 2:
                ratingButton = document.getElementById(fieldName + "3");
                break;
            case 3:
                ratingButton = document.getElementById(fieldName + "4");
                break;
            case 4:
                ratingButton = document.getElementById(fieldName + "5");
                break;
        }
        ratingButton.checked = true;
        ratingButton.click(function(e) {
           e.preventDefault();
        });
    }
    document.getElementById(fieldName + "1").disabled = true;  
    document.getElementById(fieldName + "2").disabled = true;  
    document.getElementById(fieldName + "3").disabled = true;  
    document.getElementById(fieldName + "4").disabled = true;  
    document.getElementById(fieldName + "5").disabled = true;   
}