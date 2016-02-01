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