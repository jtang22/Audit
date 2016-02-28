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
        alert("Error: " + error.code + " " + error.message);
    }
  }).then(function() {
    companies = companyList.slice(0);
    $('#autocomplete').autocomplete({
        lookup: companies,
        onSelect: function (suggestion) {
            var thehtml = '<strong>Company Name:</strong> ' + suggestion.companyName;
            $('#outputcontent').html(thehtml);
        }
    });
  });
    
})