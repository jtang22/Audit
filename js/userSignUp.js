function addUser() {
    var user = new Parse.User();
    
    user.set("username", document.getElementById("username").value);
    user.set("password", document.getElementById("password").value);
    user.set("firstname", document.getElementById("firstname").value);
    user.set("lastname", document.getElementById("lastname").value);
    user.set("email", document.getElementById("email").value);
    var reviews = ["review1", "review2", "review3"];
    user.set("reviews", reviews);
    
    user.signUp(null, {
        success: function(user) {
            alert("User added!");
        },
        error: function(user, error){
            alert("Error: " + error.code + " " + error.message);
        }
    });
}