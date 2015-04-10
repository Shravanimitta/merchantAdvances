var pcApp = angular.module('pcApp');

pcApp.controller('FundingDetailsController', ['$scope', '$http','$state', 'ngTableParams', '$stateParams',function($scope, $http, $state, ngTableParams, $stateParams){

	if($stateParams.fundId){
		$scope.fundId = $stateParams.fundId;
	}
	else {
		$state.go("fundings");
	}
	
 $scope.tableParams = new ngTableParams({
	       // show first page
		      page: 1,          
		   // count per page 
		      count: 10,          
		      sorting: {
		    	// initial sorting 	  
		          paymentId: 'asc'     
		      },
		      filter: {
		    	// initial filter
		          name: ''       
		      }
		  }, {
		      //total: 0, // length of data
		      getData: function($defer, params) {
		    	  //for later use as bar chart
		    	  //$scope.paybacksChart;
		          // get sort param
		    	  var sortParam = params.sorting();
		    	//set sorting parameter to paymentId by default
		    	  var sortBy = "paymentId";
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
		    	  if($stateParams.fundId){
		    		  // add fundId to the filter query
		    		  filterQuery = filterQuery + "&fundId="+$stateParams.fundId;
		    	  }
		    	  		    	  
		    	  //check empty object
		    	  if(filterQuery !== '') {
		    			  url = "fundingDetails?sortBy="+sortBy+"&pageNo="+pageNo+"&perPage="+perPage+"&sortOrder="+sortOrder+filterQuery;
		    	  }
		    	  else{	    		  
		    			  url = "fundingDetails?sortBy="+sortBy+"&pageNo="+pageNo+"&perPage="+perPage+"&sortOrder="+sortOrder;
		    	  }
		         
		    	  //make ajax call to get the funding payment details and status info 
		    	  $scope.config={};
		    	  $http.get(url)
		          .success(function (data) {
		           	$scope.payments = data.payments;
		           	$scope.status = data.status;
		           	$scope.sector = data.sector || "-";
		           	$scope.industry = data.industry || "-";
		           	$scope.zip = data.zip;
		           	$scope.paybackAmount = data.paybackAmount;
		           	
		          	params.total(data.totalCount);
		          
		          //bind the data to $scope.payments
		          	$defer.resolve($scope.payments);
		     
		          //***********************/	
		          //*charting Code Start*/
		          //***********************/
		          	//variables for line chart
		          	var chartDateArr = [], 
		          	chartPaymentCodeArr = [],
		          	amountArr =  [],
		          	paymentIdArr = [];
		          	
		          	// variables for pie chart
		          	 var PaymentCodeP = ["Payment"],
		          	 PaymentCodeB = ["Bounce"],
		          	 PaymentCodeE = ["Error"],
		          	 PaymentCodeF = ["Uncleared"],
		          	 PaymentCodeN = ["Note"],
		          	 PaymentCodeS = ["Special"];
		          
		          //add date and payment codes to corresponding arrays for display using tooltip in Line chart	
		          	chartDateArr.push("Date");
		          	chartPaymentCodeArr.push("Payment Code");
		          	amountArr.push("Amount");
		          	paymentIdArr.push("Payment Id");
		          	//map of payment ids to payment code
		          	$scope.map = {};
		          	for(p in $scope.payments) {
		          		//line chart related data
		          		$scope.map[$scope.payments[p].paymentId] = $scope.payments[p].pmtCode;
		          		chartDateArr.push($scope.payments[p].systemDateUnformatted);
		          		amountArr.push($scope.payments[p].amount);
		          		paymentIdArr.push($scope.payments[p].paymentId);
		          	//assuming 1 for Payment
		          		if($scope.payments[p].pmtCode=="P"){
		          			chartPaymentCodeArr.push(1) 
	          			}
		          		else {
		          	//0 for all other cases like bounced, error, note, uncleared etc 		
		          			chartPaymentCodeArr.push(0) 
	          			}
		          		
	          			//pie chart related data
		          		if($scope.payments[p].pmtCode=="P"){
		          			PaymentCodeP.push(1);
		          		}
		          		else if($scope.payments[p].pmtCode=="B"){
		          			PaymentCodeB.push(1); 
		          		}
		          		else if($scope.payments[p].pmtCode=="E"){
		          			PaymentCodeE.push(1);
		          		}
		          		else if($scope.payments[p].pmtCode=="U"){
		          			PaymentCodeF.push(1);
		          		}
		          		else if($scope.payments[p].pmtCode=="F"){
		          			PaymentCodeF.push(1); 
		          		}
		          		else if($scope.payments[p].pmtCode=="N"){
		          			PaymentCodeN.push(1);
		          		}
		          		else if($scope.payments[p].pmtCode=="S"){
		          			PaymentCodeS.push(1);
		          		}
	          		}
		          //Plot the line chart for depicting payments as a time series
		          //generate line chart
		          	var lineChart = c3.generate({
						bindto: "#lineChart",
					    data: {
					        x: 'Date',
				//	        xFormat: '%Y%m%d', // 'xFormat' can be used as custom format of 'x'
					        columns: [
					                  chartDateArr,
					                  chartPaymentCodeArr,
					                  amountArr,
					                  paymentIdArr
					        ]
					    },
					    
					   /* zoom: {
					        enabled: true
					    },*/
					    legend: {
					    	item: {
					    		onclick: function(id){ return; },
					    		onmouseover: function(id){ return; }	
					    	}
					    },
					    axis: {
					        x: {
					            type: 'timeseries',
					            tick: {
					                format: '%b-%d-%Y'
					            }
					        },
					        y: {
					        	max:1,
					        	min:0,
					            tick: {
					              values: [0, 1]
					            }
					        }
					    },
					  //code for keeping same color for all rows in tooltip
					    tooltip: {
					    	contents: function (d, defaultTitleFormat, defaultValueFormat, color) {
					    		var $$ = this, config = $$.config,
					            titleFormat = config.tooltip_format_title || defaultTitleFormat,
					            nameFormat = config.tooltip_format_name || function (name) { return name; },
					            valueFormat = config.tooltip_format_value || defaultValueFormat,
					            text, i, title, value, name, bgcolor;
					            
					        for (i = 0; i < d.length; i++) {
					            if (! (d[i] && (d[i].value || d[i].value === 0))) { continue; }
					            
					            //build the table for plotting the graph
					            if (! text) {
					                title = titleFormat ? titleFormat(d[i].x) : d[i].x;
					                text = "<table class='" + $$.CLASS.tooltip + "'>" + (title || title === 0 ? "<tr><th colspan='2'>" + title + "</th></tr>" : "");
					            }
					          
					            value = valueFormat(d[i].value, d[i].ratio, d[i].id, d[i].index);
					            if(d[i].id == "Payment Code"){
					            	value = $scope.map[d[2].value];
					            }
					            if (value !== undefined) {
					                name = nameFormat(d[i].name, d[i].ratio, d[i].id, d[i].index);
					                //changing d[i] to d[0] to get same color for all rows
					                bgcolor = $$.levelColor ? $$.levelColor(d[i].value) : color(d[0].id);

					                text += "<tr class='" + $$.CLASS.tooltipName + "-" + d[i].id + "'>";
					                text += "<td class='name'><span style='background-color:" + bgcolor + "'></span>" + name + "</td>";
					                text += "<td class='value'>" + value + "</td>";
					                text += "</tr>";
					            }
					        }
					        return text + "</table>";
					    	  }
					    }
					});// line chart ends
		          	
		          	//pie chart code for overall health of payments 
		          	var pieChart = c3.generate({
		          		bindto: "#pieChart",
					    data: {
					        columns: [
									PaymentCodeP,
									PaymentCodeB,
									PaymentCodeE,
									PaymentCodeF,
									PaymentCodeN,
									PaymentCodeS
					        ],
					        type : 'donut',
						    legend: {
						    	item: {
						    		onclick: function(id){ return false; }
						    	}
						    },
					    },
					    donut: {
					    	title: "Payment Codes Distribution"
					    }
		          	
					});
		          	
		          	
		          });
		    	  
		    	  
		    	  //make another ajax call to get payments and amount
		    	  var healthUrl = "healthDetails?" + "fundId="+$stateParams.fundId;
		    	  $http.get(healthUrl)
		          .success(function (healthData) {
		        	  $scope.healthData = healthData;
		        	  var totalAmountPaid = 0;
		        	  for(i in healthData){ 
		        		  totalAmountPaid = totalAmountPaid + healthData[i].amount;
		        	  }
		        	  $scope.totalAmountPaid =  parseFloat(totalAmountPaid).toFixed(2);
		        	  
		          });
		      }
		  });
 
		 
		 
		}		  
]);
