/**
 * User Angular Module
 */
'use strict';
angular.module('app.ward', []).controller("wardCtrl", ["$scope", "$filter", "wardSvc","subCountySvc","$wardValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter,wardSvc,subCountySvc,$wardValid, $rootScope, blockUI, logger, $location) {

	var init;
	$scope.wards = [];

	/**
	 * gets the list of wards
	 */
	$scope.loadWardData = function () {
		$scope.wards = [], $scope.searchKeywords = "", $scope.filteredWards = [], $scope.row = "", $scope.wardEditMode = false;
		wardSvc.GetWards().success(function (response) {
			return $scope.wards = response, $scope.searchKeywords = "", $scope.filteredWards = [], $scope.row = "", $scope.select = function (page) {
				var end, start;
				return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageWards = $scope.filteredWards.slice(start, end)
			}, $scope.onFilterChange = function () {
				return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
			}, $scope.onNumPerPageChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.onOrderChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.search = function () {
				return $scope.filteredWards = $filter("filter")($scope.wards, $scope.searchKeywords), $scope.onFilterChange()
			}, $scope.order = function (rowName) {
				return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredWards = $filter("orderBy")($scope.wards, rowName), $scope.onOrderChange()) : void 0
			}, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPages = [], (init = function () {
				return $scope.search(), $scope.select($scope.currentPage)
			})();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	$scope.loadWardData();

	
	/**
	 * gets the detail of selected ward
	 * @param ward-object of ward details
	 */
	$scope.editWard = function (ward) {
		if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" +
				$location.path())) {
			logger.log("Oh snap! You are not allowed to modify the ward.");
			return false;
		}
		$scope.wardEditMode = true;
		$scope.isExisting = true;
		$scope.wardId = ward.wardId;
		$scope.wardCode = ward.wardCode;
		$scope.wardName = ward.wardName;
		$scope.subCountySelect = ward.subCountyId;
		$scope.active = ward.active;

	};

	/**
	 *  initialize the form for creating the new ward
	 */
	$scope.addWard = function () {

		if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" +
				$location.path())) {
			logger.log("Oh snap! You are not allowed to create wards.");
			return false;
		}

		$scope.wardEditMode = true;
		$scope.isExisting = false;
		$scope.wardId = 0;
		$scope.wardCode = "";
		$scope.wardName = "";
		$scope.subCountySelect = "";
		$scope.isDisabled = true;
	};

	/**
	 * cancels the operation of wards creation
	 */
	$scope.cancelWard= function () {
		$scope.wardEditMode = false;
		$scope.active = false;
		$scope.isExisting = false;
		$scope.wardId = 0;
		$scope.wardCode = "";
		$scope.wardName = "";
		$scope.subCountySelect = "";
		$scope.isDisabled = true;
	}
	
	/**
	 * gets the list of active subcounties
	 */
	$scope.loadActiveSubCounties = function () {
		$scope.subCounties = [];

		wardSvc.GetActiveSubCounties().success(function (response) {
			$scope.subCounties = response
		})
	};
	$scope.loadActiveSubCounties();

	/**
	 * updates the ward details
	 */
	$scope.updWard = function () {
		var ward = {};

		if (!$wardValid($scope.wardCode)) {
			logger.logWarning("Opss! You may have skipped specifying the ward's Code. Please try again.")
			return false;
		}
		if (!$wardValid($scope.wardName)) {
			logger.logWarning("Opss! You may have skipped specifying the ward's Name. Please try again.")
			return false;
		}
		if (!$wardValid($scope.subCountySelect)) {
			logger.logWarning("Opss! You may have skipped specifying the Sub-County. Please try again.")
			return false;
		}

		if (!$wardValid($scope.wardId))
			ward.wardId = 0;
		else
			ward.wardId = $scope.wardId;
		ward.wardCode = $scope.wardCode;
		ward.wardName=$scope.wardName;
		ward.subCountyId=$scope.subCountySelect;
		ward.active = $scope.active;
		ward.createdBy = $rootScope.UsrRghts.sessionId;
		blockUI.start()
		wardSvc.UpdWard(ward).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The ward information was saved succesfully")
				$scope.wardId = 0;
				$scope.wardCode = "";
				$scope.wardName = "";
				$scope.subCountySelect="";
				$scope.active = false;
				$scope.wardEditMode = false;
				$scope.loadWardData();
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

} ]).factory('wardSvc', function ($http) {
	var ervWardSvc = {};
	ervWardSvc.GetWards = function () {
		return $http({
			url: '/erevenue/rest/ward/gtWards',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	ervWardSvc.GetActiveSubCounties = function () {
		return $http({
			url: '/erevenue/rest/ward/gtActiveSubCounties',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	ervWardSvc.UpdWard = function (ward) {
		return $http({
			url: '/erevenue/rest/ward/updWard',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data:ward
		});
	};
                    
	return ervWardSvc;
}).factory('$wardValid', function () {
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