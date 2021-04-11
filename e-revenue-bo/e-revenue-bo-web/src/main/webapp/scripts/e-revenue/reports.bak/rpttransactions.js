/**
 * Transaction Held Angular Module
 */
'use strict';
angular.module('app.rptTransaction', []).controller("rptTransactionCtrl", ["$scope", "$filter", "memStatementSvc", "$memStatementValid", "$rootScope", "blockUI", "logger", "$location","$http","$window","$timeout", function ($scope, $filter, memStatementSvc, $memStatementValid, $rootScope, blockUI, logger, $location,$http,$window,$timeout) {
    $scope.mem=[];
    $scope.showFilters=true;
    $scope.showDetails=true;
    $scope.tempAmountList=[];
    $scope.voideAmount=0;
    $scope.invalidAmount=0;
    $scope.totalAmount=0;
    $scope.netAmount=0;
    $scope.billNo=0;
    $scope.counter = 1;
    var tmrAuthData;
    var init;
    $scope.showDetails=true;
    var cancelledStyle = {'background-color': '#fdefee', 'color': '#b13d31'};
    var cancelledStyleInputs = {'background-color': '#fdefee', 'color': '#b13d31', 'border-color': '#fdefee'};

    var pendingAuthStyle = {'background-color': 'floralwhite', 'color': '#af7f18'};
    var pendingAuthStyleInputs = {'color': '#af7f18'};

    var authenticatedStyle = {'background-color': '#eef8fc', 'color': '#26929c'};
    var authenticatedStyleInputs = {'color': '#26929c'};
    
   $scope.previewReport=function(mem)
   {
	  // $scope.showDetails=false;
//  	 if (!$memStatementValid(mem.FromDt)) {
//         logger.logWarning("Opss! You may have skipped specifying the From Date. Please try again.")
//         return false;
//     }
//	 else  if (!$memStatementValid(mem.ToDt)) {
//         logger.logWarning("Opss! You may have skipped specifying the To Date. Please try again.")
//         return false;
//     }
	  // $scope.showDetails=false;
	   $scope.userTransaction = [];
	   $scope.showDetails=false;
	   $scope.fromDt= $filter('date')(mem.FromDt,'yyyy-MM-dd');
	   $scope.toDt=$filter('date')(mem.ToDt,'yyyy-MM-dd');
	   $scope.loadTransactions();
  	
   }
   
   $scope.loadTransactions=function(){
	   $scope.showDetails=false;
	   $scope.userTxns = [], $scope.searchKeywords = "", $scope.filteredUserTxns = [], $scope.row = "", $scope.agentEditMode = false; $scope.previewServices=false;
     var market ={};
    
     market.fromDate=$scope.fromDt;
     market.toDate=$scope.toDt;
     memStatementSvc.GetAllDetailedTxn(market).success(function (response) {
     	console.log(response);
         return $scope.userTxns = response, $scope.searchKeywords = "", $scope.filteredUserTxns = [], $scope.row = "", $scope.select = function (page) {
             var end, start;
             return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageUserTxns = $scope.filteredUserTxns.slice(start, end)
         }, $scope.onFilterChange = function () {
             return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
         }, $scope.onNumPerPageChange = function () {
             return $scope.select(1), $scope.currentPage = 1
         }, $scope.onOrderChange = function () {
             return $scope.select(1), $scope.currentPage = 1
         }, $scope.search = function () {
             return $scope.filteredUserTxns = $filter("filter")($scope.userTxns, $scope.searchKeywords), $scope.onFilterChange()
         }, $scope.order = function (rowName) {
             return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredUserTxns = $filter("orderBy")($scope.userTxns, rowName), $scope.onOrderChange()) : void 0
         }, $scope.numPerPageOpt = [10,20, 50, 100,200], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPageUserTxns = [], (init = function () {
             return $scope.search(), $scope.select($scope.currentPage)
         })();
     }).error(function (data, status, headers, config) {
         logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
     });
   }
   //$scope.loadTransactions();
   $scope.onTimeout = function(){
   
	    	if ($scope.userTxns.length > 0) {
	          
	            $scope.$apply($scope.loadTransactions());
	         //   $scope.authPending = $scope.setAuthPending();
	        }
   	
       tmrAuthData = $timeout($scope.onTimeout,1000);
   }
 //  tmrAuthData = $timeout($scope.onTimeout,1000);
   $scope.loadAmpuntDetail=function(){
	   memStatementSvc.GetCurrentDetailedTxn().success(function (response) {
        $scope.tempAmountList = response;
        if($scope.tempAmountList.length>0){

        for (var i = 0; i <= $scope.tempAmountList.length - 1; i++) {
       	
       		 $scope.totalAmount=$scope.tempAmountList[i].totalAmount//+$scope.tempAmountList[i].invalidAmount;
       		 $scope.voideAmount=$scope.tempAmountList[i].voidAmount;
       		 $scope.invalidAmount=$scope.tempAmountList[i].invalidAmount;
      		// $scope.netAmount=(($scope.totalAmount-$scope.voideAmount)-$scope.invalidAmount);
      		 $scope.netAmount=$scope.tempAmountList[i].netAmount;
       		 $scope.billNo=$scope.tempAmountList[i].billNo;
       		
       		
       	 
       	
        }
        }
   	})
   }
   $scope.loadAmpuntDetail();
   
   $scope.exportPdfReport = function(mem) {
	   console.log(mem);
		 if (!$memStatementValid(mem.FromDt)) {
		 logger.logWarning("Opss! You may have skipped specifying the From Date. Please try again.")
		 return false;
		 }
		 else if (!$memStatementValid(mem.ToDt)) {
		 logger.logWarning("Opss! You may have skipped specifying the To Date. Please try again.")
		 return false;
		 }

		var FromDt = $filter('date')(mem.FromDt, 'yyyy-MM-dd');
		var ToDt = $filter('date')(mem.ToDt, 'yyyy-MM-dd');
	
		$scope.url = '/kcmc/reports?type=TX&eType=P&FrDt='+ FromDt + '&ToDt=' + ToDt;
	
	}
	$scope.exportExcelReport = function(mem) {
		 if (!$memStatementValid(mem.FromDt)) {
		 logger.logWarning("Opss! You may have skipped specifying the From Date. Please try again.")
		 return false;
		 }
		 else if (!$memStatementValid(mem.ToDt)) {
		 logger.logWarning("Opss! You may have skipped specifying the To Date. Please try again.")
		 return false;
		 }

		var FromDt = $filter('date')(mem.FromDt, 'yyyy-MM-dd');
		var ToDt = $filter('date')(mem.ToDt, 'yyyy-MM-dd');
	
		
			$scope.url = '/kcmc/reports?type=TX&eType=E&FrDt='+ FromDt + '&ToDt=' + ToDt;
	}


}]).factory('memStatementSvc', function ($http) {
    var compasRptStatementSvc = {};

    compasRptStatementSvc.GetAllDetailedTxn = function (market) {
        return $http({
            url: '/erevenue/rest/report/gtAllTxnDetail/',
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            data: market
          
        });
    };
    compasRptStatementSvc.GetCurrentDetailedTxn = function () {
        return $http({
            url: '/erevenue/rest/report/gtCurrTxnDetail/',
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
           
          
        });
    };
    return compasRptStatementSvc;
}).factory('$memStatementValid', function () {
    return function (valData) {
        if (angular.isUndefined(valData))
            return false;
        else {
            if (valData == null)
                return false;
            else {
                if (valData.toString().trim() == "")
                    return false;
                else
                    return true;
            }
        }
        return false;
    };

});
