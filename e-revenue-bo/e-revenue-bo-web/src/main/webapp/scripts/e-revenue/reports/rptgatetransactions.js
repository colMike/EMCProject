/**
 * Transaction Held Angular Module
 */
'use strict';
angular.module('app.rptGateTransaction', []).controller("rptGateTransactionCtrl", ["$scope", "$filter", "gateTxnSvc", "entryPointSvc","memStatementSvc","$gateTxnValid", "$rootScope", "blockUI", "logger", "$location","$http","$window", function ($scope, $filter, gateTxnSvc,entryPointSvc ,memStatementSvc,$gateTxnValid, $rootScope, blockUI, logger, $location,$http,$window) {
  
    $scope.mem=[];
    $scope.showFilters=true;
    $scope.showDetails=true;
	var init;
	
	   $scope.loadGateData = function () {
	        $scope.gates = [];
	        entryPointSvc.GetGates($scope).success(function (response) {
	            return $scope.gates = response;
        });
    }
    $scope.loadGateData();
   
    
    $scope.addFilters=function(){
    	  $scope.showFilters=false;
    }
    $scope.loadGateTxns = function () {
        $scope.userTxns = [], $scope.searchKeywords = "", $scope.filteredUserTxns = [], $scope.row = "", $scope.agentEditMode = false; $scope.previewServices=false;
 
        gateTxnSvc.GetGateTxns().success(function (response) {
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
    
    $scope.loadGateTxns ();
    
   $scope.previewReport=function(mem)
   {
	   if (!$gateTxnValid($scope.gateSelect)) {
           logger.logWarning("Opss! You may have skipped specifying the Entry Point. Please try again.")
           return false;
       }
	   $scope.showDetails=false;
	   $scope.userTransaction = [];
	   $scope.fromDt= $filter('date')(mem.FromDt,'yyyy-MM-dd');
	   $scope.toDt=$filter('date')(mem.ToDt,'yyyy-MM-dd');
	   $scope.userTxns = [], $scope.searchKeywords = "", $scope.filteredUserTxns = [], $scope.row = "", $scope.agentEditMode = false; $scope.previewServices=false;
       var gate ={};
       gate.gateId=$scope.gateSelect;
       gate.fromDate=$scope.fromDt;
       gate.toDate=$scope.toDt;
       gateTxnSvc.GetGateDetailedTxn(gate).success(function (response) {
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

}]).factory('gateTxnSvc', function ($http) {
    var compasRptStatementSvc = {};
    compasRptStatementSvc.GetGateTxns = function () {
        return $http({
            url: '/erevenue/rest/report/gtGateTxns/',
            method: 'GET',
            headers: { 'Content-Type': 'application/json' },
          
          
        });
    };
    compasRptStatementSvc.GetGateDetailedTxn = function (gate) {
        return $http({
            url: '/erevenue/rest/report/gtGateTxnDetail/',
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            data:gate
          
        });
    };
    
    return compasRptStatementSvc;
}).factory('$gateTxnValid', function () {
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
