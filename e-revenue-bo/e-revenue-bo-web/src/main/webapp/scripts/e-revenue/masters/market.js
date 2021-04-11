/**
 * Market Angular Module
 */
'use strict';
angular.module('app.market', []).controller("marketCtrl", ["$scope", "$filter", "marketSvc","wardSvc","$marketValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter,marketSvc,wardSvc,$marketValid, $rootScope, blockUI, logger, $location) {

	var init;
	$scope.markets = [];
	$scope.marketDetails=[];
	$scope.marketDetails=[{ marketCode:'',marketName:''}]

	/**
	 * gets the details of the market created
	 */
	$scope.loadMarketData = function () {
		$scope.markets = [], $scope.searchKeywords = "", $scope.filteredMarkets = [], $scope.row = "", $scope.marketEditMode = false;
		marketSvc.GetMarkets().success(function (response) {
			return $scope.markets = response, $scope.searchKeywords = "", $scope.filteredMarkets = [], $scope.row = "", $scope.select = function (page) {
				var end, start;
				return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageMarkets = $scope.filteredMarkets.slice(start, end)
			}, $scope.onFilterChange = function () {
				return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
			}, $scope.onNumPerPageChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.onOrderChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.search = function () {
				return $scope.filteredMarkets = $filter("filter")($scope.markets, $scope.searchKeywords), $scope.onFilterChange()
			}, $scope.order = function (rowName) {
				return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredMarkets = $filter("orderBy")($scope.markets, rowName), $scope.onOrderChange()) : void 0
			}, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPages = [], (init = function () {
				return $scope.search(), $scope.select($scope.currentPage)
			})();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	$scope.loadMarketData();

	/**
	 * initialize the form for creating the new market
	 */
	$scope.addMarket = function () {
		if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" +
				$location.path())) {
			logger.log("Oh snap! You are not allowed to create markets.");
			return false;
		}
		$scope.marketEditMode = true;
		$scope.isExisting = false;
		$scope.marketId = 0;
		$scope.marketCode = "";
		$scope.marketName = "";
		$scope.wardSelect = "";
		$scope.isDisabled = true;
		$scope.marketDetails=[];
		$scope.marketDetails=[{ marketCode:'',marketName:''}]
		
	};
	
	/**
	 * gets the details of the selected market
	 * @param market-object of market details
	 */
	$scope.editMarket = function (market) {
		if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" +
				$location.path())) {
			logger.log("Oh snap! You are not allowed to modify the market.");
			return false;
		}
		$scope.marketEditMode = true;
		$scope.isExisting = true;
		$scope.marketId = market.marketId;
		$scope.marketCode = market.marketCode;
		$scope.marketName = market.marketName;
		$scope.wardSelect = market.wardId;
		$scope.active = market.active;
		$scope.satus=market.status;

	};
	
	/**
	 * gets the details of the active wards
	 */
	$scope.loadActiveWardData = function () {
		$scope.wards = [];

		marketSvc.GetActiveWards().success(function (response) {
			$scope.wards = response
		})
	};
	$scope.loadActiveWardData();

	/**
	 * updates the market details
	 */
	$scope.updMarket = function () {
		var market = {};
		$scope.tempMarkets=[];

		if ($scope.marketId==0){
			market.marketId = 0;
			for(var i=0;i<$scope.marketDetails.length;i++){
				if($scope.marketDetails[i].marketCode!="" && $scope.marketDetails[i].marketName!=""){
					$scope.tempMarkets.push({"marketCode":$scope.marketDetails[i].marketCode,
						"marketName":$scope.marketDetails[i].marketName});
				}
			}
			console.log($scope.tempMarkets)
			
			if($scope.tempMarkets.length==0){
				
				logger.logWarning("Opss! You may have skipped specifying the market's Code/Name. Please try again.")
				return false;
			}
			market.marketDetails = $scope.tempMarkets;
		}
		else{
			if (!$marketValid($scope.marketCode)) {
				logger.logWarning("Opss! You may have skipped specifying the market's Code. Please try again.")
				return false;
			}
			if (!$marketValid($scope.marketName)) {
				logger.logWarning("Opss! You may have skipped specifying the market's Name. Please try again.")
				return false;
			}
			market.marketId = $scope.marketId;
			market.marketCode=$scope.marketCode;
			market.marketName=$scope.marketName;
		}

		if (!$marketValid($scope.wardSelect)) {
			logger.logWarning("Opss! You may have skipped specifying the ward. Please try again.")
			return false;
		}

		market.wardId=$scope.wardSelect;
		market.active = $scope.active;
		market.createdBy = $rootScope.UsrRghts.sessionId;
		blockUI.start()
		marketSvc.UpdMarket(market).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The market information was saved succesfully")
				$scope.marketId = 0;
				$scope.marketCode = "";
				$scope.wardName = "";
				$scope.wardSelect=0;
				$scope.active = false;
				$scope.marketEditMode = false;
				$scope.loadMarketData();
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
	
	/**
	 * cancels the operation of market creation
	 */
	$scope.cancelMarket= function () {
		$scope.marketEditMode = false;
		$scope.active = false;
		$scope.isExisting = false;
		$scope.marketId = 0;
		$scope.marketCode = "";
		$scope.marketName = "";
		$scope.wardSelect = 0;
		$scope.isDisabled = true;
		$scope.marketDetails=[];
		$scope.marketDetails=[{ marketCode:'',marketName:''}]
	}

	/**
	 * addes the texbox dynamically for market name,market code
	 */
	$scope.addElement = function() {
		$scope.marketDetails.push({marketCode:'',marketName:'' })
		console.log($scope.marketDetails);
	}

} ]).factory('marketSvc', function ($http) {
	var ervMarketSvc = {};
	ervMarketSvc.GetMarkets = function () {
		return $http({
			url: '/erevenue/rest/market/gtMarkets',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	ervMarketSvc.GetActiveWards = function () {
		return $http({
			url: '/erevenue/rest/market/gtActiveWards',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};

	ervMarketSvc.UpdMarket = function (market) {
		return $http({
			url: '/erevenue/rest/market/updMarket',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data:market
		});
	};
	return ervMarketSvc;
}).factory('$marketValid', function () {
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