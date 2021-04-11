/**
 * Issue Device Angular Module
 */
'use strict';
angular.module('app.issueDevice', []).controller("issueDeviceCtrl", ["$scope", "$filter", "issueDeviceSvc","userSvc","deviceSvc","$issueDeviceValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter,issueDeviceSvc,userSvc,deviceSvc,$issueDeviceValid, $rootScope, blockUI, logger, $location) {

	var init;
	$scope.issueDevices = [];
	$scope.editMode=true;
	$scope.showGate=true;
	
	/**
	 * gets the list of issed devices
	 */
	$scope.loadIssueDeviceData = function () {
		$scope.issueDevices = [], $scope.searchKeywords = "", $scope.filteredIssueDevices = [], $scope.row = "", $scope.issueDeviceEditMode = false;
		issueDeviceSvc.GetIssueDevices($scope).success(function (response) {
			return $scope.issueDevices = response, $scope.searchKeywords = "", $scope.filteredIssueDevices = [], $scope.row = "", $scope.select = function (page) {
				var end, start;
				return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageIssueDevices = $scope.filteredIssueDevices.slice(start, end)
			}, $scope.onFilterChange = function () {
				return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
			}, $scope.onNumPerPageChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.onOrderChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.search = function () {
				return $scope.filteredIssueDevices = $filter("filter")($scope.issueDevices, $scope.searchKeywords), $scope.onFilterChange()
			}, $scope.order = function (rowName) {
				return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredIssueDevices = $filter("orderBy")($scope.issueDevices, rowName), $scope.onOrderChange()) : void 0
			}, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPages = [], (init = function () {
				return $scope.search(), $scope.select($scope.currentPage)
			})();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	$scope.loadIssueDeviceData();

	/**
	 * gets the list active users
	 */
	$scope.loadUserData = function () {
		$scope.users = []
		issueDeviceSvc.GetActiveUsers($scope).success(function (response) {
			return $scope.users = response
		});
	}
	$scope.loadUserData();

	/**
	 * gets the list of active devices
	 */
	$scope.loadDeviceData = function () {
		$scope.devices = [];
		issueDeviceSvc.GetActiveDevices($scope).success(function (response) {
			return $scope.devices = response
		})
	}
	$scope.loadDeviceData ();

	/**
	 * gets the details of selected issued device
	 * @param-issueDevice-object of issued device details
	 */
	$scope.editIssueDevice = function (issueDevice) {
		$scope.editMode=false;
		$scope.selectMode=false;
		if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" +
				$location.path())) {
			logger.log("Oh snap! You are not allowed to modify the Device Issue Info.");
			return false;
		}
		$scope.issueDeviceEditMode = true;
		$scope.isExisting = true;
		$scope.issueId = issueDevice.issueId;
		$scope.serialNo=issueDevice.serialNo;
		if( $scope.devId=="0"){
			$scope.rdReturnDevice = "RD";
			$scope.selectDevice=true;
			$scope.userSelect=issueDevice.issuedTo;
		}else{
			$scope.deviceSelect=issueDevice.devId;
			$scope.rdNewDevice="ND"
				$scope.selectDevice=false;
		}
		$scope.userSelect = issueDevice.issuedTo;
		$scope.userName=issueDevice.userName;

	};

	/**
	 * shows the option of return device
	 */
	$scope.showDeviceSelect=function(mode){
		if(mode=="RD"){
			$scope.selectDevice=true;
		}
		else
		{
			$scope.selectDevice=false;
		}
	}

	/**
	 * initialize the form for creating the new issue
	 */
	$scope.addIssueDevice = function () {

		if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" +
				$location.path())) {
			logger.log("Oh snap! You are not allowed to create Issue Device Info.");
			return false;
		}
		$scope.loadDeviceData ();
		$scope.loadUserData();
		$scope.editMode=true;
		$scope.isExisting = false;
		$scope.issueDeviceEditMode = true;
		$scope.issueId=0;
		$scope.serialNo= "";
		// $scope.userId ="";
		$scope.issuedTo ="";
		$scope.selectDevice=false;
		$scope.selectMode=true;
		$scope.deviceSelect="";
		$scope.userSelect="";
		$scope.rdNewDevice="ND";
	};

	/**
	 * cancels the operation of issue device creation
	 */
	$scope.cancelIssueDevice= function () {
		$scope.issueDeviceEditMode = false;
		$scope.editMode=true;
		$scope.issueId ="";
		$scope.serialNo= "";
		$scope.deviceSelect="";
		$scope.userSelect=0;
		$scope.rdNewDevice="ND";

	}

	/**
	 * updates the issued device details
	 */
	$scope.updIssueDevice = function () {
		var issueDevice = {};
		if (!$issueDeviceValid($scope.userSelect)) {
			logger.logWarning("Opss! You may have skipped specifying the User Name. Please try again.")
			return false;
		}

		if (!$issueDeviceValid($scope.deviceSelect)) {
			logger.logWarning("Opss! You may have skipped specifying the Serial No. Please try again.")
			return false;
		}

		if (!$issueDeviceValid($scope.issueId)){
			issueDevice.issueId = 0;
		}
		else{
			issueDevice.issueId = $scope.issueId;
		}
		issueDevice.devId = $scope.deviceSelect;
		issueDevice.issuedTo=$scope.userSelect;
		issueDevice.status=true;
		if($scope.rdNewDevice=="RD"){
			issueDevice.issueId=$scope.issueId;
			issueDevice.issuedTo=$scope.userSelect;
			issueDevice.status=false;
		}
		issueDevice.createdBy =  $rootScope.UsrRghts.sessionId;
		blockUI.start()
		issueDeviceSvc.UpdIssueDevice(issueDevice).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The device issued succesfully to the user")
				if (parseFloat($scope.issuedTo) == parseFloat($rootScope.UsrRghts.sessionId)) {
					localStorageService.clearAll();
					loginSvc.GetRights($rootScope.UsrRghts.sessionId).success(function (rightLst)
							{
						localStorageService.set('rxr', rightLst);
						$rootScope.UsrRghts = rightLst;
							}).error(function (data, status, headers, config) {
								logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
							});
				}
				$scope.issueId = 0;
				$scope.serialNo = "";
				$scope.userName = "";
				$scope.issuedTo="";
				$scope.issueDeviceEditMode = false;
				$scope.loadIssueDeviceData();
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

} ]).factory('issueDeviceSvc', function ($http) {
	var evIssueDeviceSvc = {};
	evIssueDeviceSvc.GetIssueDevices = function ($scope) {
		return $http({
			url: '/erevenue/rest/device/gtIssueDevices',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};

	evIssueDeviceSvc.GetActiveDevices = function ($scope) {
		return $http({
			url: '/erevenue/rest/device/gtActiveDevices',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};

	evIssueDeviceSvc.GetActiveUsers = function ($scope) {
		return $http({
			url: '/erevenue/rest/device/gtActiveUsers',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	evIssueDeviceSvc.UpdIssueDevice = function (issueDevice) {
		return $http({
			url: '/erevenue/rest/device/updIssueDevice',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: issueDevice

		});
	};

	return evIssueDeviceSvc;
}).factory('$issueDeviceValid', function () {
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