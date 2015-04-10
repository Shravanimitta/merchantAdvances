var pcApp = angular.module('pcApp');

pcApp.controller('FundingDetailsController', ['$scope', '$http','$state', 'ngTableParams', '$stateParams',function($scope, $http, $state, ngTableParams, $stateParams){
	//reroute to funding page if fund id is empty
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
		    	  var sortParam = params.sorting();   // get sort params
		    	  var sortBy = "paymentId";           //set sorting parameter to paymentId by default
		    	  var sortOrder = "asc";			  //set sorting order to ascending order by default  
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
		    	  		    	  
		    	//form url for the ajax call and append required parameters based on availability
		    	  if(filterQuery !== '') {
		    			  url = "payments?sortBy="+sortBy+"&pageNo="+pageNo+"&perPage="+perPage+"&sortOrder="+sortOrder+filterQuery;
		    	  }
		    	  else{	    		  
		    			  url = "payments?sortBy="+sortBy+"&pageNo="+pageNo+"&perPage="+perPage+"&sortOrder="+sortOrder;
		    	  }
		         
		    	  //make ajax call to get the funding payment details and status info 
		    	  $scope.config={};
		    	  $http.get(url)
		          .success(function (data) {
					$scope.payments = data.paymentsList;
		          	params.total(data.totalCount);
		          
		          //bind the data to $scope.payments
		          	$defer.resolve($scope.payments);
		     
		          //***********************/	
		          //*charting Code Start*/
		          //***********************/
		          	//variables for line chart
		          	var chartDateArr = ["Date"], chartPaymentCodeArr = ["Payment Code"], 
		          	amountArr = ["Amount"], paymentIdArr = ["Payment Id"];
		          	
		          	//add date and payment codes to corresponding arrays for display using tooltip in Line chart	
		          	//map of payment ids to payment code to get payment codes on tooltip
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
	          			} else {
		          			//and 0 for all other cases like bounced, error, note, uncleared etc 		
		          			chartPaymentCodeArr.push(0) 
	          			}
	          		}
		          //Plot the line chart for depicting payments as a time series
		          	var lineChart = c3.generate({
						bindto: "#lineChart",
					    data: {
					        x: 'Date',
					        columns: [ chartDateArr, chartPaymentCodeArr, amountArr, paymentIdArr ]
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
					            //replace the digits with the payment Code 
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
		          });
		    	  
		    	// variables for payment codes distribution donut chart
	          	 var PaymentCodeP = ["Payment"], PaymentCodeB = ["Bounce"], PaymentCodeE = ["Error"],
	          	 PaymentCodeF = ["Uncleared"], PaymentCodeN = ["Note"], PaymentCodeS = ["Special"];
		    	  
		    	  //make an ajax call to get payments codes and amount paid
		    	  var url = "fundingDetails?" + "fundId="+$stateParams.fundId;
		    	  $http.get(url).success(function (fdData) {
					$scope.status = fdData.status;
					$scope.sector = fdData.sector || "-";
					$scope.industry = fdData.industry || "-";
					$scope.zip = fdData.zip;
					$scope.paybackAmount = fdData.paybackAmount;
		        	  
		        	  var totalAmountPaid = 0;
		        	  for(i in fdData.amounts){ 
		        		  totalAmountPaid = totalAmountPaid + fdData.amounts[i];
		        		//donut chart related data
			          		if(fdData.pmtCodes[i]=="P"){
			          			PaymentCodeP.push(1);
			          		}
			          		else if(fdData.pmtCodes[i]=="B"){
			          			PaymentCodeB.push(1); 
			          		}
			          		else if(fdData.pmtCodes[i]=="E"){
			          			PaymentCodeE.push(1);
			          		}
			          		else if(fdData.pmtCodes[i]=="U"){
			          			PaymentCodeF.push(1);
			          		}
			          		else if(fdData.pmtCodes[i]=="F"){
			          			PaymentCodeF.push(1); 
			          		}
			          		else if(fdData.pmtCodes[i]=="N"){
			          			PaymentCodeN.push(1);
			          		}
			          		else if(fdData.pmtCodes[i]=="S"){
			          			PaymentCodeS.push(1);
			          		}
		        	  }
		        	  $scope.totalAmountPaid =  parseFloat(totalAmountPaid).toFixed(2);
		        	//donut chart code for overall health of payments 
			          	var donutChart = c3.generate({
			          		bindto: "#donutChart",
						    data: {
						    	//array of payment code arrays
						        columns: [ PaymentCodeP, PaymentCodeB, PaymentCodeE, PaymentCodeF, PaymentCodeN, PaymentCodeS ],
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
		      }
		  });
		}		  
]);
