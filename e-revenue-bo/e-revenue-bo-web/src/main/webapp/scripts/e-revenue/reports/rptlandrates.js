/*
 * by cyrus
 */
"use strict";

angular.module('app.landRatesReport', []).controller("landRatesReportCtrl", ["$scope", "$filter","wardSvc", "landRatesRptSvc",  "$rootScope", "blockUI", "logger","$landValid", "$location","$http","$window", function($scope, $filter,wardSvc, landRatesRptSvc, $rootScope, blockUI, logger,$landValid, $location, $http, $window) {
	$scope.mem = [];
	var init;
	$scope.statusSelect="";
	
	$scope.landRatesViewMode = false;
    $scope.status=[{statusId:0,Name:"All"},{statusId:1,Name:"Paid "},{statusId:2,Name:"Paid partially"},{statusId:3,Name:"Pending Payment"}]

	$scope.previewReport=function(mem) {

		$scope.showZDetails=true;
		$scope.showDetails=false;
		$scope.userTransaction = [];
		$scope.fromDt= $filter('date')(mem.FromDt,'yyyy-MM-dd');
		$scope.toDt=$filter('date')(mem.ToDt,'yyyy-MM-dd');
		$scope.loadlandRatess();
	}
    $scope.loadActiveSubCounties = function () {
        $scope.subCounties = [];
        wardSvc.GetActiveSubCounties().success(function (response) {
            $scope.subCounties = response;
        })
    };
    $scope.loadActiveSubCounties();
    
	$scope.loadlandRatess = function () {
		$scope.showZDetails=true;
		$scope.showDetails=false;
		$scope.userTxns = [], $scope.searchKeywords = "", $scope.filteredUserTxns = [], $scope.row = "", $scope.agentEditMode = false; $scope.previewServices=false;
		var landRates = {};
		
		landRates.fromDate = $scope.fromDt;
		landRates.toDate = $scope.toDt;
		
		landRatesRptSvc(landRates).success(function (response) {
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
	
	$scope.viewDetails=function(landRates){
		$scope.showSummary = true;
		$scope.showSearchDetails=true;
		$scope.showDetails=true;
		
    	if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" +
    			$location.path())) {
    		logger.log("Oh snap! You are not allowed to view the landRates.");
    		return false;
    	}
        $scope.landRatesViewMode = true;
        $scope.landRatesNo=landRates.landRatesNo;
        $scope.businessNo=landRates.businessNo;
        $scope.businessName=landRates.businessName;
        $scope.businessDesc=landRates.businessDesc;
        $scope.appNo=landRates.appNo;
        $scope.postalCode=landRates.postalCode;
        $scope.postalAdd=landRates.postalAdd;
        $scope.regNo=landRates.regNo;
        $scope.landRatesFee=landRates.landRatesFee;
        $scope.email=landRates.email;
        $scope.userName=landRates.landRatesUser;
        $scope.plotNo=landRates.plotNo;
        $scope.validity=landRates.validity;
        $scope.expiryDate=landRates.expiryDate;
        $scope.landRatesQr=landRates.landRatesQr;
        var landRates={};
		$scope.url="";
		landRates.landRatesNo=$scope.landRatesNo;
		landRates.landRatesQr=$scope.landRatesQr;
	
		
    }
	
	$scope.cancellandRates = function() {
		$scope.showSummary = false;
		$scope.showSearchDetails=false;
		$scope.showDetails=false;
		$scope.landRatesViewMode = false;
		$scope.landRatesNo = "";
	}

	$scope.printlandRates=function(){
		$scope.url = '/erevenue/reports?type=P&landRatesNo=' +$scope.landRatesNo;
	}
	
	   $scope.exportPdfReport = function(mem) {
			
			if (!$landValid(mem.FromDt)) {
				logger.logWarning("Opss! You may have skipped specifying the From Date. Please try again.")
				return false;
			}
			else if (!$landValid(mem.ToDt)) {
				logger.logWarning("Opss! You may have skipped specifying the To Date. Please try again.")
				return false;
			}
		

			var FromDt = $filter('date')(mem.FromDt, 'yyyy-MM-dd');
			var ToDt = $filter('date')(mem.ToDt, 'yyyy-MM-dd');
			var subcounty=mem.subCountySelect;
			//var status = $scope.statusSelect;

				$scope.url = '/erevenue/reports?type=LRP&eType=P&FrDt='+ FromDt + '&ToDt=' + ToDt +'&subcounty='+subcounty;
			

		}
	  $scope.exportExcelReport = function(mem) {
			 if (!$landValid(mem.FromDt)) {
			 logger.logWarning("Opss! You may have skipped specifying the From Date. Please try again.")
			 return false;
			 }
			 else if (!$landValid(mem.ToDt)) {
			 logger.logWarning("Opss! You may have skipped specifying the To Date. Please try again.")
			 return false;
			 }
			 else if (!$landValid($scope.statusSelect)) {
				 logger.logWarning("Opss! You may have skipped specifying the Payment status. Please try again.")
				 return false;
				 }
			 
			 var status = $scope.statusSelect;

			var FromDt = $filter('date')(mem.FromDt, 'yyyy-MM-dd');
			var ToDt = $filter('date')(mem.ToDt, 'yyyy-MM-dd');
		
			
				$scope.url = '/erevenue/reports?type=LRP&eType=E&FrDt='+ FromDt + '&ToDt=' + ToDt +'&subcounty='+subcounty;
		}
	//$scope.loadlandRatess();
	
}]).factory('landRatesRptSvc', function ($http) {
	return function(landRates) {
		console.log(landRates);
		return ($http({

			url: '/erevenue/rest/application/gtLandRatesByDate',
			method: 'POST',
			headers: { 'Content-Type':'application/json' },
			data: landRates
		}));
	}
}).factory('$landValid', function () {
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