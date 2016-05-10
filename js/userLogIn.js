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
    var userName = document.getElementById("userName");
    name = user.get("firstname") + " " + user.get("lastname");
    
    // member name label
    para = document.createElement("label");
    node = document.createTextNode(name);
    para.appendChild(node);
    userName.appendChild(para);
    
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
           var reviewTable = document.createElement("TABLE");
           reviewTable.style.width = '1000px';
           
           reviewTable.style.borderCollapse = "collapse";
           var row = document.createElement("TR");
           row.style.height = '80px';
           
           var boldCompany = document.createElement("h4");
           var boldRatings = document.createElement("h4");
           var boldComment = document.createElement("h4");
           var boldHelp = document.createElement("h4");
           var cCell = document.createElement("TD");
           var rCell = document.createElement("TD");
           var comCell = document.createElement("TD");
           var helpCell = document.createElement("TD");
           
           boldCompany.appendChild(document.createTextNode("Company"));
           boldRatings.appendChild(document.createTextNode("Ratings"));
           boldComment.appendChild(document.createTextNode("Comment"));
           boldHelp.appendChild(document.createTextNode("Helpful"));
           
           cCell.appendChild(boldCompany);
           cCell.style.width = '150px';
           rCell.appendChild(boldRatings);
           rCell.style.width = '300px';
           comCell.appendChild(boldComment);
           helpCell.appendChild(boldHelp);
           helpCell.style.width = '250px'   
           helpCell.style.paddingLeft = '40px';
           
           row.appendChild(cCell);
           row.appendChild(rCell);
           row.appendChild(comCell);
           row.appendChild(helpCell);
           
           reviewTable.appendChild(row);
            
            var elementNumReview = document.getElementById("numReviews");
            var numReviews = userReviews.length;
           
           //number of reviews user has submitted
            numReviewElem = document.createElement("p");
            if(numReviews == 1) {
                node = document.createTextNode(numReviews + " Review")
            }
            else {
                node = document.createTextNode(numReviews + " Reviews");
            }
            numReviewElem.appendChild(node);
            elementNumReview.appendChild(numReviewElem);
                      
           
           for(var i = 0; i < userReviews.length; i++) {
               
                var firstRow = document.createElement("TR");
                firstRow.style.height = '100px';
                firstRow.style.textAlign = 'left';
                firstRow.style.verticalAlign = 'top';
               
                var companyCell = document.createElement("TD");
                var ratingCell = document.createElement("TD");
                var commentCell = document.createElement("TD");
                var helpfulCell = document.createElement("TD");
               
                
                var companyName = document.createTextNode(userReviews[i].get("companyId"));
//                companyPara.onclick = (function() {
//                        var company = companyName;
//                        return function() {
//                            document.cookie = "companySearch=" + company;
//                            window.location.href = "companyProfile.html";
//                        }
//                    })();
               
               
                //obtain ratings
                var ratingsTable = document.createElement("TABLE");
                var shippingRow = document.createElement("TR");
                var serviceRow = document.createElement("TR");
               var qualityRow = document.createElement("TR");
               
               var shippingTextCell = document.createElement("TD");
                var shippingText = document.createTextNode("Shipping: ");
                var shippingStarsCell = document.createElement("TD");
                var shippingStars = createStars("Shipping", userReviews[i].get("shippingRating"));
               shippingStarsCell.appendChild(shippingStars);
               shippingTextCell.appendChild(shippingText);
               shippingRow.appendChild(shippingTextCell);
               shippingRow.appendChild(shippingStarsCell);
               ratingsTable.appendChild(shippingRow);

               var serviceTextCell = document.createElement("TD");
               var serviceText = document.createTextNode("Service: ");
                var serviceStarsCell = document.createElement("TD");
                var serviceStars = createStars("Service", userReviews[i].get("serviceRating"));
                serviceStarsCell.appendChild(serviceStars);
                serviceTextCell.appendChild(serviceText);
                serviceRow.appendChild(serviceTextCell);
                serviceRow.appendChild(serviceStarsCell);
                ratingsTable.appendChild(serviceRow);

                var qualityTextCell = document.createElement("TD");
                var qualityText = document.createTextNode("Quality: ");
                var qualityStarsCell = document.createElement("TD");
                var qualityStars = createStars("Quality", userReviews[i].get("qualityRating"));
                qualityStarsCell.appendChild(qualityStars);
                qualityTextCell.appendChild(qualityText);
                qualityRow.appendChild(qualityTextCell);
                qualityRow.appendChild(qualityStarsCell);
                ratingsTable.appendChild(qualityRow);

               
               var userCompanyComment = document.createTextNode(userReviews[i].get("comment"));
               
               var foundHelpful = document.createTextNode(userReviews[i].get("score") + " user\(s\) found this review helpful");
                              
               companyCell.appendChild(companyName);
               ratingCell.appendChild(ratingsTable);
               commentCell.appendChild(userCompanyComment);
               helpfulCell.appendChild(foundHelpful);
               
               firstRow.appendChild(companyCell);
               firstRow.appendChild(ratingCell);
               firstRow.appendChild(commentCell);
               firstRow.appendChild(helpfulCell);
              firstRow.style.height = '200px'

               helpfulCell.style.paddingLeft = '40px';
               
               reviewTable.appendChild(firstRow);
           }
           elementReviews.appendChild(reviewTable);
       } 
    });
    
}

function loadCompanyData() {
    checkUserLoggedIn();
    var name;
    var reviewTable;
    var userReviewExists = false;
    
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
        var Review = Parse.Object.extend("Review");
        query = new Parse.Query(Review);
        query.equalTo("companyId", name);
        query.find({
           success: function(userReviews) {
               //sets up a table
               reviewTable = document.createElement("TABLE");
               reviewTable.style.width = '1500px';
               reviewTable.id = "reviewTable";
               var shipping = 0;
               var quality = 0;
               var service = 0;
               var ticker = 0;
               userReviews.forEach(function(review) {
                    //Get user profile picture
                    var imgQuery = new Parse.Query(Parse.User);
                    imgQuery.equalTo("username", review.get("userId"));
                    imgQuery.find().then(function(foundUser) {
                        var imageFile = foundUser[0].get("picture");
                        var imgUrl;
                        var userImg = document.createElement('img');
                        if(imageFile != null) {
                            imgUrl = imageFile.url();
                        }
                        else {
                            imgUrl = "https://u.o0bc.com/avatars/no-user-image.gif";
                        }
                        userImg.setAttribute("height", "100");
                        userImg.setAttribute("width", "100");
                        userImg.src = imgUrl;
                        /*var Review = Parse.Object.extend("Review");
                        var revQuery = new Parse.Query(Review); */   
                        var flagButton;
                        var helpfulText = "Was this review helpful?";
                        var helpfulButton;
                        var notHelpfulButton;
                        var user = Parse.User.current();
                       //checks if user has written a review.
                        if(user) {          
                            console.log(user.get("username") + " " + review.get("userId"));
                            if(!user.get("username").localeCompare(review.get("userId"))) {
                                console.log("disabling button");
                                document.getElementById("writeReview").style.display = '';
                            }
                        }
                        //for company overall ratings
                        service += review.get("serviceRating");
                        shipping += review.get("shippingRating");
                        quality += review.get("qualityRating");

                        //if review is not flag, add to page
                        if(review.get("flagged") != true) {
                            //sets up a new row
                            var firstRow = document.createElement("TR");
                            firstRow.style.verticalAlign = 'top';
                            firstRow.style.padding = 0;
                            var secondRow = document.createElement("TR");
                            secondRow.style.height = '80px';
                            secondRow.style.textAlign = 'left';
                            secondRow.style.verticalAlign = 'top';
                            secondRow.style.padding = 0;

                            //sets up new cells
                            var userCell = document.createElement("TD");
                            userCell.style.textAlign = 'center';

                            var commentCell = document.createElement("TD");
                            commentCell.style.textAlign = 'left';
                            var ratingsCell = document.createElement("TD");
                            var buttonsCell = document.createElement("TD");

                            //obtain's user's name
                            var userName = document.createElement("h4");
                            var userNode = document.createTextNode((review).get("userId"));

                            //obtain ratings
                            var ratingsTable = document.createElement("TABLE");
                            var shippingRow = document.createElement("TR");
                            var serviceRow = document.createElement("TR");
                            var qualityRow = document.createElement("TR");

                            var shippingTextCell = document.createElement("TD");
                            var shippingText = document.createTextNode("Shipping: ");
                            var shippingStarsCell = document.createElement("TD");
                            var shippingStars = createStars("Shipping", review.get("shippingRating"));
                            shippingStarsCell.appendChild(shippingStars);
                            shippingTextCell.appendChild(shippingText);
                            shippingRow.appendChild(shippingTextCell);
                            shippingRow.appendChild(shippingStarsCell);
                            ratingsTable.appendChild(shippingRow);

                            var serviceTextCell = document.createElement("TD");
                            var serviceText = document.createTextNode("Service: ");
                            var serviceStarsCell = document.createElement("TD");
                            var serviceStars = createStars("Service", review.get("serviceRating"));
                            serviceStarsCell.appendChild(serviceStars);
                            serviceTextCell.appendChild(serviceText);
                            serviceRow.appendChild(serviceTextCell);
                            serviceRow.appendChild(serviceStarsCell);
                            ratingsTable.appendChild(serviceRow);

                            var qualityTextCell = document.createElement("TD");
                            var qualityText = document.createTextNode("Quality: ");
                            var qualityStarsCell = document.createElement("TD");
                            var qualityStars = createStars("Quality", review.get("qualityRating"));
                            qualityStarsCell.appendChild(qualityStars);
                            qualityTextCell.appendChild(qualityText);
                            qualityRow.appendChild(qualityTextCell);
                            qualityRow.appendChild(qualityStarsCell);
                            ratingsTable.appendChild(qualityRow);

                            //obtain comment
                            var commentNode = document.createTextNode(review.get("comment"));

                            //creating the buttons
                            flagButton = document.createElement("button");
                            flagButton.innerHTML = "Flag";
                            flagButton.onclick = (function() {
                                return function() {
                                    var thisReview = review;
                                    thisReview.set("flagged", true);
                                    thisReview.save();
                                }
                            })();

                            helpfulButton = document.createElement("button");
                            helpfulButton.innerHTML = "Helpful";
                            helpfulButton.onclick = (function() {
                                return function() {
                                    var thisReview = review;
                                    var curScore = thisReview.get("score");
                                    curScore++;
                                    thisReview.set("score", curScore);
                                    thisReview.save();
                                }
                            })();

                            notHelpfulButton = document.createElement("button");
                            notHelpfulButton.innerHTML = "Not Helpful";
                            notHelpfulButton.onclick = (function() {
                                return function() {
                                    var thisReview = review;
                                    var curScore = thisReview.get("score");
                                    if(curScore > 0) {
                                        curScore--;
                                        thisReview.set("score", curScore);
                                        thisReview.save();
                                    }
                                }
                            })();
                            var helpfulNode = document.createTextNode(review.get("score") + " user(s) found this review helpful");
                            var helpfulPara = document.createElement("p");
                            helpfulPara.appendChild(helpfulNode);
                            
                            //add values to cells
                            //add user info to the userCell
                            userName.appendChild(userNode);
                            userCell.appendChild(userName);
                            userCell.appendChild(userImg);
                            userCell.style.width = '300px';
                            //userCell.appendChild(document.createElement("img"));
                            
                            //add ratings to the ratingsCell
                            ratingsCell.appendChild(ratingsTable);
                            ratingsCell.style.width = '300px';

                            //add comment and buttons to the commentCell
                            commentCell.appendChild(commentNode);
                            commentCell.appendChild(document.createElement("br"));  
                            commentCell.appendChild(document.createElement("br"));
                            commentCell.appendChild(document.createElement("br"));
                            commentCell.appendChild(document.createElement("br"));
                            commentCell.appendChild(document.createElement("br"));
                            commentCell.appendChild(document.createElement("br"));

                            commentCell.appendChild(helpfulPara);
                            
                            buttonsCell.appendChild(flagButton);
                            buttonsCell.appendChild(helpfulButton);
                            buttonsCell.appendChild(notHelpfulButton);

                            //add cells to row
                            firstRow.appendChild(userCell);
                            firstRow.appendChild(ratingsCell);
                            firstRow.appendChild(commentCell);
                            reviewTable.appendChild(firstRow);
                            secondRow.appendChild(document.createElement("TD"));
                            secondRow.appendChild(document.createElement("TD"));
                            secondRow.appendChild(buttonsCell);
                            reviewTable.appendChild(secondRow);
                            ticker++;
                            if(ticker == userReviews.length) {
                                service /= userReviews.length;
                                shipping /= userReviews.length;
                                quality /= userReviews.length;

                                var totalRating = (service + shipping + quality) / 3;
                                setStars("avgRating", totalRating);
                                setStars("shippingRating", service);
                                setStars("qualityRating", quality);
                                setStars("serviceRating", service);
                            }
                        }
                    });
                });
               
               var elementReviews = document.getElementById("displayReview");
               elementReviews.appendChild(reviewTable);
               
               
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

function createStars(starType, ratingNum) {
    var id = "user" + starType + "Rating";
    
    var displayStars = document.createElement("SPAN");
    displayStars.className = "displayRate";
    
    var userStarFive = document.createElement("INPUT");
    userStarFive.setAttribute("type", "radio");
    userStarFive.setAttribute("value", "5");
    userStarFive.setAttribute("id", id + "5")
    var userStarFiveLabel = document.createElement("LABEL");
    userStarFiveLabel.htmlFor = id + "5"
    userStarFive.disabled = true;
    //userStarFive.appendChild(userStarFiveLabel);
    if(ratingNum == 5) {
        userStarFive.checked = true;
    }
    
    var userStarFour = document.createElement("INPUT");
    userStarFour.setAttribute("type", "radio");
    userStarFour.setAttribute("value", "4");
    userStarFour.setAttribute("id", id + "4")
    var userStarFourLabel = document.createElement("LABEL");
    userStarFourLabel.htmlFor = id + "4"
    userStarFour.disabled = true;
    //userStarFour.appendChild(userStarFourLabel);
    if(ratingNum >= 4) {
        userStarFour.checked = true;
    }
    
    var userStarThree = document.createElement("INPUT");
    userStarThree.setAttribute("type", "radio");
    userStarThree.setAttribute("value", "3");
    userStarThree.setAttribute("id", id + "3")
    var userStarThreeLabel = document.createElement("LABEL");
    userStarThreeLabel.htmlFor = id + "3"
    userStarThree.disabled = true;
    //userStarThree.appendChild(userStarThreeLabel);
    if(ratingNum >= 3) {
        userStarThree.checked = true;
    }
    
    var userStarTwo = document.createElement("INPUT");
    userStarTwo.setAttribute("type", "radio");
    userStarTwo.setAttribute("value", "2");
    userStarTwo.setAttribute("id", id + "2")
    var userStarTwoLabel = document.createElement("LABEL");
    userStarTwoLabel.htmlFor = id + "2"
    userStarTwo.disabled = true;
    //userStarTwo.appendChild(userStarTwoLabel);
    if(ratingNum >= 2) {
        userStarTwo.checked = true;
    }
    
    var userStarOne = document.createElement("INPUT");
    userStarOne.setAttribute("type", "radio");
    userStarOne.setAttribute("value", "1");
    userStarOne.setAttribute("id", id + "1")
    var userStarOneLabel = document.createElement("LABEL");
    userStarOneLabel.htmlFor = id + "1"
    userStarOne.disabled = true;
    //userStarOne.appendChild(userStarOneLabel);
    if(ratingNum >= 1) {
        userStarOne.checked = true;
    }
    
    displayStars.appendChild(userStarFive);
    displayStars.appendChild(userStarFiveLabel);
    displayStars.appendChild(userStarFour);
    displayStars.appendChild(userStarFourLabel);
    displayStars.appendChild(userStarThree);
    displayStars.appendChild(userStarThreeLabel);
    displayStars.appendChild(userStarTwo);
    displayStars.appendChild(userStarTwoLabel);
    displayStars.appendChild(userStarOne);
    displayStars.appendChild(userStarOneLabel);

    return displayStars;
}