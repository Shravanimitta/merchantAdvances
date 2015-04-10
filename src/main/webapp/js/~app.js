var app = angular.module('pcApp', ['ngTouch', 'ui.grid', 'ui.grid.pagination']);

app.controller('MainCtrl', [
'$scope', '$http', 'uiGridConstants', function($scope, $http, uiGridConstants) {

  var paginationOptions = {
    pageNumber: 1,
    pageSize: 25,
    sort: null
  };
  
  /*
   * 
   * 
   * columnDefs: [
		  { name: 'paymentId', 
			displayName : "Payment Id",
			cellTemplate : '<a href="/funding/{{row.entity.paymentId}}">{{row.entity.paymentId}}</a>'
		  },
		  { name: 'fundId' },
		  { name: 'merchantId' },
		  { name: 'date' },
		  { name: 'paymentCode' },
		  { name: 'achCode' },
		  { name: 'amount' }
    ],
   */

  $scope.gridOptions = {
	  showFilter : false,
	  showGroupPanel : false,
	  jqueryUIDraggable : false,
	  enablePaging : true,
	  paginationPageSizes: [25, 50, 75],
	  paginationPageSize: 25,
	  useExternalPagination: true,
	  useExternalSorting: true,
	  columnDefs: [
		  { name: 'fundId', 
			displayName : "Fund Id",
			cellTemplate : '<a href="payment.html?fundId={{row.entity.fundId}}">{{row.entity.fundId}}</a>'
		  },
		  { name: 'fundLegId', 
			displayName : "Fund Leg Id" },
		  { name: 'fundDate', 
			displayName : "Fund Date" },
		  { name: 'merchId' , 
			displayName : "Merchant Id"},
		  { name: 'term' , 
			displayName : "Term"},
		  { name: 'factorPnt' , 
			displayName : "Factor Point"},
		  { name: 'fundedAmount' , 
			displayName : "Funded Amount"},
		  { name: 'paybackAmount', 
			displayName : "Payback Amount" },
		  { name: 'status' , 
			displayName : "Status"},
		  { name: 'zipCode' , 
			displayName : "Zip Code"},
		  { name: 'sector' , 
			displayName : "Sector" },
		  { name: 'industry' , 
			displayName : "Industry"}
    ],
    onRegisterApi: function(gridApi) {
      $scope.gridApi = gridApi;
      $scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
        if (sortColumns.length == 0) {
          paginationOptions.sort = null;
        } else {
          paginationOptions.sort = sortColumns[0].sort.direction;
        }
        getPage();
      });
      gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
        paginationOptions.pageNumber = newPage;
        paginationOptions.pageSize = pageSize;
        getPage();
      });
    }
  };

  var getPage = function() {
    var url;
    switch(paginationOptions.sort) {
      case uiGridConstants.ASC:
        url = 'https://cdn.rawgit.com/angular-ui/ui-grid.info/gh-pages/data/100_ASC.json';
        break;
      case uiGridConstants.DESC:
        url = 'https://cdn.rawgit.com/angular-ui/ui-grid.info/gh-pages/data/100_DESC.json';
        break;
      default:
        url = 'https://cdn.rawgit.com/angular-ui/ui-grid.info/gh-pages/data/100.json';
        break;
    }
    url = "funding";
    $http.get(url)
    .success(function (data) {
      $scope.gridOptions.totalItems = 100;
      var firstRow = (paginationOptions.pageNumber - 1) * paginationOptions.pageSize;
      $scope.gridOptions.data = data.slice(firstRow, firstRow + paginationOptions.pageSize);
    });
  };

  getPage();
}
]);
