/**
 * 
 */
/**
 * User Angular Module
 */
'use strict';
angular.module('app.submarket', []).controller("submarketCtrl", ["$scope", "$filter","submarketSvc", "marketSvc","$submarketValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter,submarketSvc,marketSvc,$submarketValid, $rootScope, blockUI, logger, $location) {

	var init;

	$scope.submarkets = [];
	$scope.submarketDetails=[];
	$scope.submarketDetails=[{ subMarketCode:'',subMarketName:''}]

	$scope.loadSubMarketData = function () {
		$scope.submarkets = [], $scope.searchKeywords = "", $scope.filteredSubMarkets = [], $scope.row = "", $scope.submarketEditMode = false;
		submarketSvc.GetSubMarkets().success(function (response) {
			return $scope.submarkets = response, $scope.searchKeywords = "", $scope.filteredSubMarkets = [], $scope.row = "", $scope.select = function (page) {
				var end, start;
				return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageSubMarkets = $scope.filteredSubMarkets.slice(start, end)
			}, $scope.onFilterChange = function () {
				return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
			}, $scope.onNumPerPageChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.onOrderChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.search = function () {
				return $scope.filteredSubMarkets = $filter("filter")($scope.submarkets, $scope.searchKeywords), $scope.onFilterChange()
			}, $scope.order = function (rowName) {
				return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredSubMarkets = $filter("orderBy")($scope.submarkets, rowName), $scope.onOrderChange()) : void 0
			}, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPages = [], (init = function () {
				return $scope.search(), $scope.select($scope.currentPage)
			})();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	$scope.loadSubMarketData();

	$scope.loadActiveMarketData = function () {
		$scope.markets = [];

		submarketSvc.GetActiveMarkets().success(function (response) {
			$scope.markets = response
		})
	};
	$scope.loadActiveMarketData();

	$scope.editSubMarket = function (submarket) {
		if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" +
				$location.path())) {
			logger.log("Oh snap! You are not allowed to modify the sub-market.");
			return false;
		}
		$scope.submarketEditMode = true;
		$scope.isExisting = true;
		$scope.subMarketId = submarket.subMarketId;
		$scope.subMarketCode = submarket.subMarketCode;
		$scope.subMarketName = submarket.subMarketName;
		$scope.marketSelect = submarket.marketId;
		$scope.active = submarket.active;
		$scope.status=submarket.status;

	};

	$scope.addSubMarket = function () {

		if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" +
				$location.path())) {
			logger.log("Oh snap! You are not allowed to create sub-markets.");
			return false;
		}

		$scope.submarketEditMode = true;
		$scope.isExisting = false;
		$scope.subMarketId = 0;
		$scope.subMarketCode = "";
		$scope.subMarketName = "";
		$scope.marketSelect = 0;
		$scope.isDisabled = true;
		$scope.active=false;
	};

	$scope.cancelSubMarket= function () {
		$scope.submarketEditMode = false;
		$scope.active = false;
		$scope.isExisting = false;
		$scope.subMarketId = 0;
		$scope.subMarketCode = "";
		$scope.subMarketName = "";
		$scope.marketSelect = 0;
		$scope.isDisabled = true;
		$scope.submarketDetails=[];
		$scope.submarketDetails=[{ subMarketCode:'',subMarketName:''}]

	}


	$scope.updSubMarket = function () {
		var submarket = {};
		$scope.tempSubMarkets=[];

		if ($scope.subMarketId==0){
			submarket.subMarketId = 0;
			for(var i=0;i<$scope.submarketDetails.length;i++){
				if($scope.submarketDetails[i].subMarketCode!="" && $scope.submarketDetails[i].subMarketName!=""){
					$scope.tempSubMarkets.push({"subMarketCode":$scope.submarketDetails[i].subMarketCode,
						"subMarketName":$scope.submarketDetails[i].subMarketName});
				}
			}
			if($scope.tempSubMarkets.length==0){
				logger.logWarning("Opss! You may have skipped specifying the sub-market's Code/Name. Please try again.")
				return false;
			}
			console.log($scope.tempMarkets)
			submarket.subMarketDetails = $scope.tempSubMarkets;
		}
		else{
			if (!$submarketValid($scope.subMarketCode)) {
				logger.logWarning("Opss! You may have skipped specifying the sub-market's Code. Please try again.")
				return false;
			}
			if (!$submarketValid($scope.subMarketName)) {
				logger.logWarning("Opss! You may have skipped specifying the sub-market's Name. Please try again.")
				return false;
			}
			submarket.subMarketId = $scope.subMarketId;
			submarket.subMarketCode=$scope.subMarketCode;
			submarket.subMarketName=$scope.subMarketName;
		}

		if (!$submarketValid($scope.marketSelect)) {
			logger.logWarning("Opss! You may have skipped specifying the market. Please try again.")
			return false;
		}

		submarket.marketId=$scope.marketSelect;
		submarket.active = $scope.active;
		submarket.createdBy = $rootScope.UsrRghts.sessionId;
		blockUI.start()
		submarketSvc.UpdSubMarket(submarket).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The market information was saved succesfully")
				$scope.subMarketId = 0;
				$scope.subMarketCode = "";
				$scope.subMarketName = "";
				$scope.marketSelect="";
				$scope.active = false;
				$scope.submarketEditMode = false;
				$scope.loadSubMarketData();
			}
			else  {
				logger.logWarning(response.respMessage);
			}

			blockUI.stop();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
			blockUI.stop();
		});
	};

	$scope.addElement = function() {
		$scope.submarketDetails.push({subMarketCode:'',subMarketName:'' })
		console.log($scope.submarketDetails);
	}


} ]).factory('submarketSvc', function ($http) {
	var ervSubMarketSvc = {};
	ervSubMarketSvc.GetSubMarkets = function () {
		return $http({
			url: '/erevenue/rest/submarket/gtSubMarkets',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	ervSubMarketSvc.GetActiveMarkets = function () {
		return $http({
			url: '/erevenue/rest/submarket/gtActiveMarkets',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};

	ervSubMarketSvc.UpdSubMarket = function (submarket) {
		return $http({
			url: '/erevenue/rest/submarket/updSubMarket',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data:submarket
		});
	};
	return ervSubMarketSvc;
}).factory('$submarketValid', function () {
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