$(function(){ 
  var companyList = [];    
  var globalWindow = this;
  var Company = Parse.Object.extend("Company");
  var query = new Parse.Query(Company);

  query.find({
    success: function(results) {

        for(var i = 0; i < results.length; i++) {
            companyList.push({value:results[i].get("Name")});
        }
    },
    error: function(error) {
        alert("Error fetching companies: " + error.code + " " + error.message);
    }
  }).then(function() {
    companies = companyList.slice(0);
    $('#autocomplete').autocomplete({
        lookup: companies,
        onSelect: function (suggestion) {
            query = new Parse.Query(Company);
            query.equalTo("Name", suggestion.value);
            query.find({
                success: function(results) {
                    document.cookie = "companySearch=" + suggestion.value;
                    window.location.href = "companyProfile.html";
                },
                error: function(error) {
                    alert("Error with search results: " + error.code + " " + error.message);
                }
            })
        }
    });
  });
    
})