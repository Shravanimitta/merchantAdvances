var pcApp = angular.module('pcApp');


pcApp.filter('customUserDateFilter', function($filter) {
    return function(values, dateString) {
        var filtered = [];
     //add filter for date type
         if(typeof values != 'undefined' && typeof dateString != 'undefined') {
           angular.forEach(values, function(value) {
               if($filter('date')(value.Date).indexOf(dateString) >= 0) {
                 filtered.push(value);
               }
             });
         }
         
         return filtered;
       }
   });

//Controller for funding.html  
pcApp.controller('FundingController', ['$scope', '$http','$state', 'ngTableParams', '$filter', function($scope, $http, $state, ngTableParams, $filter) {

//change state to fundingPayments : If the user clicks on a fundid in the fundings page, redirect to payments page for that Fund ID	.
  $scope.goToFundingPayments = function(fundId){
	  //include fundid in the arguments
	  $state.go("fundingDetails",{fundId: fundId});
  };
  
  $scope.tableParams = new ngTableParams({
	// show first page
      page: 1,            
   // count per page  
      count: 100,
      sorting: {
      // initial sorting 	  
          fundId: 'asc'    
      },
      filter: {
      // initial filter  
          name: ''       
      }
  }, {
      counts: [10, 25, 100],
      //total: 0, // length of data
      getData: function($defer, params) {
          // get sort param
    	  var sortParam = params.sorting();
    	  //set sorting parameter to fundId by default
    	  var sortBy = "fundId";
    	  //set sorting order to ascending order by default
    	  var sortOrder = "asc";
    	  
    	  //loop through the sorting parameters and store in an array. 
    	  for(var propertyName in sortParam) { 
    		sortBy = propertyName;
    		sortOrder = sortParam[propertyName];
		  }
    	  
    	  //get the page number and rows per page
    	  var pageNo = params.$params.page;
          var perPage = params.$params.count;
    	  
          //get the filter parameters
    	  var filters = params.filter();
    	  var filterQuery = '';
    	  var i = 1;
    	  for(var propertyName in filters) { 
      		if(filters[propertyName]){
      			filterQuery = filterQuery + "&filterBy"+i+"="+propertyName+"&"+"filterBy"+i+"Text"+"="+filters[propertyName];
      			i++;
      		}
  		  }
    	  //check empty object
    	  if(filterQuery !== '') {
    		  //form url for the servlet and append required parameters based on availability
    		  url = "funding?sortBy="+sortBy+"&pageNo="+pageNo+"&perPage="+perPage+"&sortOrder="+sortOrder+filterQuery;
    	  }
    	  else{
    		  url = "funding?sortBy="+sortBy+"&pageNo="+pageNo+"&perPage="+perPage+"&sortOrder="+sortOrder;
    	  }
          
    	  //make ajax call to servlet and get data response.
    	  $http.get(url)
          .success(function (data) {
           
          	$scope.fundings = data.fundingsList;
          	params.total(data.totalCount);
          	
           //bind the data to $scope.funding
          	$defer.resolve($scope.fundings);
          });
          
      }
  });
}
]);
