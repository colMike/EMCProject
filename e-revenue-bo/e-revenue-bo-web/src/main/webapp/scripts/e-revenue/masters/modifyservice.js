/**
 * Modify Services Angular Module
 */
'use strict';
angular.module('app.modifyservice', []).controller("modifyServiceCtrl", ["$scope", "$filter", "modifyServiceSvc","serviceSvc","submarketSvc","$modifyServiceValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter,modifyServiceSvc,serviceSvc,submarketSvc,$modifyServiceValid, $rootScope, blockUI, logger, $location) {

	var init;
	$scope.serTypes==[];   
	$scope.showParentService=true;
	$scope.serEditMode=true;
	$scope.showMarkets=true;
	$scope.isDisabled=true;
	$scope.iconStyle="glyphicon glyphicon-pencil";
	$scope.serTypes=[{'typeId':'1','typeName':'Parent'},
	                 {'typeId':'2','typeName':'SubService'},
	                 {'typeId':'3','typeName':'Markets'}];

	/**
	 * gets the list of parent services
	 */
	$scope.loadParentServices= function () {
		$scope.pServices = [], $scope.searchKeywords = "", $scope.filteredParentServices = [], $scope.row = "", $scope.agentEditMode = false; $scope.previewServices=false;
		serviceSvc.GetServices().success(function (response) {
			return $scope.pServices = response, $scope.searchKeywords = "", $scope.filteredParentServices = [], $scope.row = "", $scope.select = function (page) {
				var end, start;
				return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentParentServices = $scope.filteredParentServices.slice(start, end)
			}, $scope.onFilterChange = function () {
				return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
			}, $scope.onNumPerPageChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.onOrderChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.search = function () {
				return $scope.filteredParentServices = $filter("filter")($scope.pServices, $scope.searchKeywords), $scope.onFilterChange()
			}, $scope.order = function (rowName) {
				return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredParentServices = $filter("orderBy")($scope.pServices, rowName), $scope.onOrderChange()) : void 0
			}, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentParentServices = [], (init = function () {
				return $scope.search(), $scope.select($scope.currentPage)
			})();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}

	/**
	 * gets the list of sub services
	 */
	$scope.loadSubServiceData = function () {
		$scope.pServices = [], $scope.searchKeywords = "", $scope.filteredParentServices = [], $scope.row = "", $scope.agentEditMode = false; $scope.previewServices=false;
		modifyServiceSvc.GetSubService().success(function (response) {
			return $scope.pServices = response, $scope.searchKeywords = "", $scope.filteredParentServices = [], $scope.row = "", $scope.select = function (page) {
				var end, start;
				return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentParentServices = $scope.filteredParentServices.slice(start, end)
			}, $scope.onFilterChange = function () {
				return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
			}, $scope.onNumPerPageChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.onOrderChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.search = function () {
				return $scope.filteredParentServices = $filter("filter")($scope.pServices, $scope.searchKeywords), $scope.onFilterChange()
			}, $scope.order = function (rowName) {
				return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredParentServices = $filter("orderBy")($scope.pServices, rowName), $scope.onOrderChange()) : void 0
			}, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentParentServices = [], (init = function () {
				return $scope.search(), $scope.select($scope.currentPage)
			})();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")

		});
	}

	/**
	 * gets the list of services marketwise with prices
	 */
	$scope.loadMarketServices = function (marketId) {
		$scope.pServices = [], $scope.searchKeywords = "", $scope.filteredParentServices = [], $scope.row = "", $scope.agentEditMode = false; $scope.previewServices=false;
		modifyServiceSvc.GetMarketService(marketId).success(function (response) {
			return $scope.pServices = response, $scope.searchKeywords = "", $scope.filteredParentServices = [], $scope.row = "", $scope.select = function (page) {
				var end, start;
				return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentParentServices = $scope.filteredParentServices.slice(start, end)
			}, $scope.onFilterChange = function () {
				return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
			}, $scope.onNumPerPageChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.onOrderChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.search = function () {
				return $scope.filteredParentServices = $filter("filter")($scope.pServices, $scope.searchKeywords), $scope.onFilterChange()
			}, $scope.order = function (rowName) {
				return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredParentServices = $filter("orderBy")($scope.pServices, rowName), $scope.onOrderChange()) : void 0
			}, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentParentServices = [], (init = function () {
				return $scope.search(), $scope.select($scope.currentPage)
			})();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")

		});
	}

	/**
	 * gets the list of active markets
	 */
	$scope.loadActiveMarketData = function () {
		$scope.markets = [];

		submarketSvc.GetActiveMarkets().success(function (response) {
			$scope.markets = response
		})
	};

	/**
	 * shows the list of services based on type select
	 * if type=1 then show parent services
	 * if type=2 then show subservices
	 * if type=3 then show market service where the prices are attached
	 */
	$scope.$watch("typeSelect", function (newValue, oldValue) {
		$scope.pServices = [];
		if ($modifyServiceValid(newValue)) {
			if (newValue != oldValue) {

				if(newValue=='1'){
					$scope.showParentService=false;
					$scope.showMarkets=true;
					$scope.markets=[];
					$scope.loadParentServices();
				}else if(newValue=='3'){
					$scope.showParentService=true;
					$scope.showMarkets=false;
					$scope.loadActiveMarketData();
				}else if(newValue=='2'){
					$scope.showParentService=false;
					$scope.showMarkets=true;
					$scope.loadSubServiceData();
				}
			}
		}
		else
			$scope.pServices = [];
	});

	/**
	 * selects the markets and dispalys the list of services for that market
	 */
	$scope.$watch("marketSelect", function (newValue, oldValue) {
		$scope.pServices = [];
		if ($modifyServiceValid(newValue)) {
			if (newValue != oldValue) {
				$scope.showParentService=false;
				$scope.loadMarketServices(newValue);
			}

		}
		else
			$scope.pServices = [];
	});
	
	/**
	 * enables the texbox in the gird for editing the details
	 */
	$scope.modifyService=function(service){
		if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" +
				$location.path())) {
			logger.log("Oh snap! You are not allowed to modify the service.");
			return false;
		}
		if(service.iconStyle=="glyphicon glyphicon-pencil"){
			service.isDisabled=false;
			service.iconStyle="glyphicon glyphicon-ok";
		}else{
			service.isDisabled=true;
			service.iconStyle="glyphicon glyphicon-pencil";
		}

	}

	/**
	 * cancels the modify service operation
	 */
	$scope.cancelSer = function () {
		$scope.showParentService=true;
		$scope.showMarkets=true;
		$scope.markets=[];
		$scope.pServices=[];
		$scope.typeSelect="";
		$scope.isDisabled=false;
	}

	/**
	 * update the modified service details
	 */
	$scope.updSer= function () {
		var ser = {};
		ser.serviceList = $scope.pServices;
		ser.marketId=$scope.marketSelect;
		ser.createdBy = $rootScope.UsrRghts.sessionId;
		ser.serviceType=$scope.typeSelect;

		blockUI.start()
		modifyServiceSvc.UpdSer(ser).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The Service information was saved succesfully")
				$scope.serviceId = 0;
				$scope.serviceName = "";
				$scope.active = false;
				$scope.serviceType=0;
				$scope.serEditMode=true;
				$scope.showParentService=true;
				$scope.showMarkets=true;
				$scope.markets=[];
				$scope.pServices=[];
				$scope.typeSelect="";
				$scope.isDisabled=false;
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

} ]).factory('modifyServiceSvc', function ($http) {
	var ervModifysvc = {};
	ervModifysvc.GetSubService = function () {
		return $http({
			url: '/erevenue/rest/service/gtSubServices/',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
//	/
	ervModifysvc.GetMarketService = function (marketId) {
		return $http({
			url: '/erevenue/rest/service/gtMarketServices/'+marketId,
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	ervModifysvc.UpdSer = function (ser) {
		return $http({
			url: '/erevenue/rest/service/updSer',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: ser
		});
	};

	return ervModifysvc;
}).factory('$modifyServiceValid', function () {
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