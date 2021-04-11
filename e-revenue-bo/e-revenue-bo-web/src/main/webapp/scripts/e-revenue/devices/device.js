/**
 * Device Angular Module
 */
'use strict';
angular.module('app.device', []).controller("deviceCtrl", ["$scope", "$filter", "deviceSvc","$deviceValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter,deviceSvc, $deviceValid, $rootScope, blockUI, logger, $location) {

	var init;
	$scope.devices = [];

	/**
	 * gets the list of device list
	 */
	$scope.loadDeviceData = function () {
		$scope.devices = [], $scope.searchKeywords = "", $scope.filteredDevices = [], $scope.row = "", $scope.deviceEditMode = false;
		deviceSvc.GetDevices($scope).success(function (response) {
			return $scope.devices = response, $scope.searchKeywords = "", $scope.filteredDevices = [], $scope.row = "", $scope.select = function (page) {
				var end, start;
				return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageDevices = $scope.filteredDevices.slice(start, end)
			}, $scope.onFilterChange = function () {
				return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
			}, $scope.onNumPerPageChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.onOrderChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.search = function () {
				return $scope.filteredDevices = $filter("filter")($scope.devices, $scope.searchKeywords), $scope.onFilterChange()
			}, $scope.order = function (rowName) {
				return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredDevices = $filter("orderBy")($scope.devices, rowName), $scope.onOrderChange()) : void 0
			}, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPages = [], (init = function () {
				return $scope.search(), $scope.select($scope.currentPage)
			})();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	$scope.loadDeviceData();

	/**
	 * gets the details of selected device
	 * @param device-object for device details
	 */
	$scope.editDevice = function (device) {
		if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" +
				$location.path())) {
			logger.log("Oh snap! You are not allowed to modify the Device Info.");
			return false;
		}
		$scope.deviceEditMode = true;
		$scope.isExisting = true;
		$scope.id = device.id;
		//$scope.devCode = device.devCode;
		$scope.serialNo = device.serialNo;
		//$scope.macAddress= device.macAddress;
		//$scope.devType = device.devType;
		$scope.status = device.status;

	};

	/**
	 * initialize the form for creating the new device
	 */
	$scope.addDevice = function () {
		if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" +
				$location.path())) {
			logger.log("Oh snap! You are not allowed to create Device Info.");
			return false;
		}
		$scope.deviceEditMode = true;
		$scope.id = 0;
		$scope.serialNo= "";
		$scope.status = false;
	};

	/**
	 * cancels the operation of creating device
	 */
	$scope.cancelDevice= function () {
		$scope.deviceEditMode = false;
		$scope.id = 0;
		$scope.serialNo= "";
		$scope.status = false;
	}

	/**
	 * updates the device details
	 */
	$scope.updDevice = function () {
		var device = {};
		if (!$deviceValid($scope.serialNo)) {
			logger.logWarning("Opss! You may have skipped specifying the Serial No. Please try again.")
			return false;
		}
		if (!$deviceValid($scope.id))
			device.id = 0;
		else
			device.id = $scope.id;
		device.serialNo = $scope.serialNo;
		device.status = $scope.status;
		device.createdBy =  $rootScope.UsrRghts.sessionId;
		blockUI.start()
		deviceSvc.UpdDevice(device).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The device information was saved succesfully")
				if (parseFloat($scope.userId) == parseFloat($rootScope.UsrRghts.sessionId)) {
					localStorageService.clearAll();
					loginSvc.GetRights($rootScope.UsrRghts.sessionId).success(function (rightLst)
							{
						localStorageService.set('rxr', rightLst);
						$rootScope.UsrRghts = rightLst;
							}).error(function (data, status, headers, config) {
								logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
							});
				}
				$scope.id = 0;
				$scope.serialNo = "";
				$scope.status = false;
				$scope.deviceEditMode = false;
				$scope.loadDeviceData();
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

} ]).factory('deviceSvc', function ($http) {
	var evDeviceSvc = {};
	evDeviceSvc.GetDevices = function ($scope) {
		return $http({
			url: '/erevenue/rest/device/gtDevices',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};

	evDeviceSvc.UpdDevice = function (device) {
		return $http({
			url: '/erevenue/rest/device/updDevice',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: device

		});
	};
	return evDeviceSvc;
}).factory('$deviceValid', function () {
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