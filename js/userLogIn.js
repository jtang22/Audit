function loginUser() {
    var username = document.getElementById("loginUsername").value;
    var password = document.getElementById("loginPassword").value;
    
    Parse.User.logIn(username, password, {
       success: function(user) {
           alert("Signed in!");
            document.getElementById("loginUsername").style.display = 'none';
            document.getElementById("loginPassword").style.display = 'none';
            document.getElementById("signInButton").style.display = 'none';
            document.getElementById("signUpButton").style.display = 'none';
            document.getElementById("signOutButton").style.display = '';
            document.getElementById("profileButton").style.display = '';
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
    document.getElementById("loginUsername").style.display = '';
    document.getElementById("loginPassword").style.display = '';
    document.getElementById("signInButton").style.display = '';
    document.getElementById("signUpButton").style.display = '';
    document.getElementById("signOutButton").style.display = 'none';
    document.getElementById("profileButton").style.display = 'none';
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
    var email = document.forms["resetpassword"]["email"].value;
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
                    window.alert("Could not send reset link to " + email + ". Please try again.");
                    return false;
                }
    });
}