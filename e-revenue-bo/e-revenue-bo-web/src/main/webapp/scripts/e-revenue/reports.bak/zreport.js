/**
 * Transaction Held Angular Module
 */
'use strict';
angular.module('app.zreport', []).controller("zReportCtrl", ["$scope", "$filter", "zReportSvc", "$zReportValid", "$rootScope", "blockUI", "logger", "$location","$http","$window", function ($scope, $filter, zReportSvc, $zReportValid, $rootScope, blockUI, logger, $location,$http,$window) {
    $scope.mem=[];
    $scope.showFilters=true;
    $scope.showDetails=true;
    $scope.showZDetails=true;
    $scope.totalAmount = 0;
    $scope.voideAmount = 0;
    $scope.invalidAmount = 0;
    $scope.netAmount = 0;
    var init;
   $scope.previewReport=function(mem)
   {
	   $scope.showZDetails=true;
	   $scope.showDetails=false;
	   $scope.userTransaction = [];
	   $scope.fromDt= $filter('date')(mem.FromDt,'yyyy-MM-dd');
	   $scope.toDt=$filter('date')(mem.ToDt,'yyyy-MM-dd');
	   $scope.loadZtransactions();
   }
   
   $scope.loadZtransactions=function(){
	   $scope.showZDetails=true;
	   $scope.showDetails=false;
	   $scope.userTxns = [], $scope.searchKeywords = "", $scope.filteredUserTxns = [], $scope.row = "", $scope.agentEditMode = false; $scope.previewServices=false;
	     var market ={};
	    
	     market.fromDate=$scope.fromDt;
	     market.toDate=$scope.toDt;
	     zReportSvc.GetZDetails(market).success(function (response) {
	     	console.log(response);
	     	
	     	for(var i = 0; i < response.length; i++) {
	     		$scope.totalAmount += parseFloat(response[i].overAllAmount);
	     	    $scope.voideAmount += parseFloat(response[i].totVoidAmount);
	     	    $scope.invalidAmount += parseFloat(response[i].totInvalidAmount);
	     	    $scope.netAmount += parseFloat(response[i].totNetAmount);
	     	}
	     	
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
   $scope.loadZtransactions();
   $scope.viewDetails=function(txn){
	   $scope.showDetails=true;
	   $scope.showZDetails=false;
	   $scope.zNumber=txn.zNumber;
	   $scope.marketName=txn.marketName;
	   $scope.serialNo=txn.serialNo;
	   $scope.totOnlineTxns=txn.totOnlineTxns;
	   $scope.totOnlineAmount=txn.totOnlineAmount;
	   $scope.totOfflineTxns=txn.totOfflineTxns;
	   $scope.totOfflineAmount=txn.totOfflineAmount;
	   $scope.totInvalidTxns=txn.totInvalidTxns;
	   $scope.totInvalidAmount=txn.totInvalidAmount;
	   $scope.totVoidTxns=txn.totVoidTxns;
	   $scope.totVoidAmount=txn.totVoidAmount;
	   $scope.totNetTxns=txn.totNetTxns;
	   $scope.totNetAmount=txn.totNetAmount;
	   $scope.userName=txn.userName;
	   $scope.zDate=txn.zDate;
	   $scope.overAllAmount=txn.overAllAmount;
   }

   $scope.cancelView=function(){
	   $scope.showDetails=false;
	   $scope.showZDetails=true;
   }
   $scope.loadAmpuntDetail=function(){
	   /*zReportSvc.GetCurrentDetailedTxn().success(function (response) {
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
	   })*/
   }
   //$scope.loadAmpuntDetail();
}]).factory('zReportSvc', function ($http) {
    var compasRptStatementSvc = {};

    compasRptStatementSvc.GetZDetails= function (market) {
        return $http({
            // url: '/kcmc/rest/report/gtZDetails/',
        	url: '/erevenue/rest/report/gtZDetails',
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            data:market
          
        });
    };
    
    return compasRptStatementSvc;
}).factory('$zReportValid', function () {
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
