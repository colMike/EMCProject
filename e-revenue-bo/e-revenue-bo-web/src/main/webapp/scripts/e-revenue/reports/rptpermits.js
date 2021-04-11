
"use strict";

angular.module('app.permitsReport', []).controller("permitsReportCtrl", ["$scope", "$filter", "permitRptSvc", "permitSvc", "$rootScope", "blockUI", "logger", "$location","$http","$window", function($scope, $filter, permitRptSvc, permitSvc, $rootScope, blockUI, logger, $location, $http, $window) {
	$scope.mem = [];
	var init;
	
	$scope.permitViewMode = false;
	
	$scope.previewReport=function(mem) {

		$scope.showZDetails=true;
		$scope.showDetails=false;
		$scope.userTransaction = [];
		$scope.fromDt= $filter('date')(mem.FromDt,'yyyy-MM-dd');
		$scope.toDt=$filter('date')(mem.ToDt,'yyyy-MM-dd');
		$scope.loadPermits();
	}
	
	$scope.loadPermits = function () {
		$scope.showZDetails=true;
		$scope.showDetails=false;
		$scope.userTxns = [], $scope.searchKeywords = "", $scope.filteredUserTxns = [], $scope.row = "", $scope.agentEditMode = false; $scope.previewServices=false;
		var permit = {};
		
		permit.fromDate = $scope.fromDt;
		permit.toDate = $scope.toDt;
		
		permitRptSvc(permit).success(function (response) {
			console.log("server response");
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
	
	$scope.viewDetails=function(permit){
		$scope.showSummary = true;
		$scope.showSearchDetails=true;
		$scope.showDetails=true;
		
    	if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" +
    			$location.path())) {
    		logger.log("Oh snap! You are not allowed to view the permit.");
    		return false;
    	}
        $scope.permitViewMode = true;
        $scope.permitNo=permit.permitNo;
        $scope.businessNo=permit.businessNo;
        $scope.businessName=permit.businessName;
        $scope.businessDesc=permit.businessDesc;
        $scope.appNo=permit.appNo;
        $scope.postalCode=permit.postalCode;
        $scope.postalAdd=permit.postalAdd;
        $scope.regNo=permit.regNo;
        $scope.permitFee=permit.permitFee;
        $scope.email=permit.email;
        $scope.userName=permit.permitUser;
        $scope.plotNo=permit.plotNo;
        $scope.validity=permit.validity;
		$scope.amountInWords=permit.amountInWords;
		console.log($scope.amountInWords, 'amountInWords')
        $scope.expiryDate=permit.expiryDate;
        $scope.permitQr=permit.permitQr;
        var permit={};
		$scope.url="";
		permit.permitNo=$scope.permitNo;
		permit.permitQr=$scope.permitQr;
		permit.amountInWords=$scope.amountInWords;
		blockUI.start()
		permitSvc.UpdQrImagePath(permit).success(function (response) {
			if (response.respCode == 200) {
			}
			else  {
				logger.logWarning(response.respMessage);
			}

			blockUI.stop();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
			blockUI.stop();
		});
	
		
    }
	
	$scope.cancelPermit = function() {
		$scope.showSummary = false;
		$scope.showSearchDetails=false;
		$scope.showDetails=false;
		$scope.permitViewMode = false;
		$scope.permitNo = "";
	}

	$scope.printPermit=function(){
		$scope.url = '/erevenue/reports?type=P&permitNo=' +$scope.permitNo;
	}
	
	$scope.loadPermits();
	
}]).factory('permitRptSvc', function ($http) {
	return function(permit) {
		console.log(permit);
		return ($http({

			url: '/erevenue/rest/application/gtPermitsByDate',
			method: 'POST',
			headers: { 'Content-Type':'application/json' },
			data: permit
		}));
	}
});