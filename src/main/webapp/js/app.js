(function(){
	var pcApp = angular.module('pcApp', ['ui.router', 'ngTable']);
	pcApp.config (['$stateProvider', '$urlRouterProvider' ,function($stateProvider,$urlRouterProvider){
		// For any unmatched url, redirect to /fundings
		  $urlRouterProvider.otherwise("/fundings");
		  // Now set up the states
		  $stateProvider
		  //state for funding details
		    .state('fundings', {
		      url: "/fundings",
		      templateUrl: "partials/fundings.html",
		      controller: "FundingController"
		    })
		  
		  //state for payments of a fund 
		    .state('fundingDetails',{
				url:'/fundings/:fundId',
				templateUrl: "partials/fundingDetails.html",
				controller: 'FundingDetailsController'
			});
	}]);
	//change state to /fundings initially
	pcApp.run(['$state',function($state){
		$state.go("fundings");
	}]);
})();
