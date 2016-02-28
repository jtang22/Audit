function addUser() {
    var user = new Parse.User();
    
    user.set("username", document.getElementById("username").value);
    user.set("password", document.getElementById("password").value);
    user.set("firstname", document.getElementById("firstname").value);
    user.set("lastname", document.getElementById("lastname").value);
    user.set("email", document.getElementById("email").value);
    user.set("numReviews", 0);
    
    user.signUp(null, {
        success: function(user) {
            alert("User added!");
            window.location.href = "home.html"
        },
        error: function(user, error){
            alert("Error: " + error.code + " " + error.message);
        }
    });
}