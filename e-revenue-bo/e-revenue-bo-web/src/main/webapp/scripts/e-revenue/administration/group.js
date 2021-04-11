/**
 * Groups Angular Module
 */
'use strict';
angular.module('app.group.rights', []).controller("groupsCtrl", ["$scope", "$filter", "userSvc", "groupSvc", "$groupValid", "$rootScope", "blockUI", "logger",  "$location", "localStorageService", "loginSvc", function ($scope, $filter, userSvc, groupSvc,$groupValid, $rootScope, blockUI, logger, $location, localStorageService, loginSvc) {
	var init;
	$scope.groups = [];
	
	/**
	 * gets the list of groups details
	 */
	$scope.loadGroupData = function () {
		$scope.groups = [], $scope.searchKeywords = "", $scope.filteredGroups = [], $scope.row = "", $scope.groupEditMode = false;
		groupSvc.GetGroups($scope).success(function (response) {
			return $scope.groups = response, $scope.searchKeywords = "", $scope.filteredGroups = [], $scope.row = "", $scope.select = function (page) {
				var end, start;
				return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageGroups = $scope.filteredGroups.slice(start, end)
			}, $scope.onFilterChange = function () {
				return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
			}, $scope.onNumPerPageChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.onOrderChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.search = function () {
				return $scope.filteredGroups = $filter("filter")($scope.groups, $scope.searchKeywords), $scope.onFilterChange()
			}, $scope.order = function (rowName) {
				return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredGroups = $filter("orderBy")($scope.groups, rowName), $scope.onOrderChange()) : void 0
			}, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPageGroups = [], (init = function () {
				return $scope.search(), $scope.select($scope.currentPage)
			})();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	$scope.loadGroupData(); 

	/**
	 * gets the rights details
	 */
	$scope.loadRightList = function () {
		$scope.groupSave.rights = [];
		groupSvc.GetRights().success(function (response) {
			$scope.groupSave.rights = response;
			console.log( $scope.groupSave.rights.length);
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}

	/**
	 * list of role types used for creating new group
	 */
	$scope.roleTypes=[{roleTypeId:1,roleTypeName:'Back-Office User'},
	                  {roleTypeId:2,roleTypeName:'Agent User'},
	                  {roleTypeId:3,roleTypeName:'Individual User'}]

	/**
	 * initialize the form for creating the new group
	 */
	$scope.addGroup = function () {

		if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
			logger.log("Oh snap! You are not allowed to create new user groups and assign rights.");
			return false;
		}
		$scope.groupHeader = "Group Creation";
		$scope.groupSave = [];
		$scope.groupSave.groupId = "";
		$scope.groupSave.groupName = "";
		$scope.roleSelect="";
		$scope.active = false;
		$scope.groupEditMode = true;
		$scope.loadRightList();
		$scope.isDisabled = true;
		$scope.isExisting = false;

	};

	/**
	 * gets the details for selected group
	 * @param group
	 */
	$scope.editGroup = function (group) {
		if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
			logger.log("Oh snap! You are not allowed to modify the user group and rights.");
			return false;
		}
		$scope.groupEditMode = true;
		$scope.groupHeader = "Edit Group";
		$scope.groupSave = [];
		$scope.groupSave.groupId = group.groupId;
		$scope.groupSave.groupName = group.groupName;
		$scope.active=group.active;
		$scope.groupSave.rights = group.rights;
		$scope.isDisabled = true;
		$scope.isExisting = true;
		$scope.roleSelect=group.roleTypeId;
	};

	/**
	 * saves the group and right details
	 */
	$scope.updGroup = function () {
		var group = [];
		if (!$groupValid($scope.groupSave.groupName)) {
			logger.logWarning("Opss! You may have skipped specifying the Group's Name. Please try again.")
			return false;
		}
		if ($scope.groupSave.groupName.length > 30) {
			logger.logWarning("Opss!Group Name is reach to maximum length of 30 ")
			return false;
		}
		if (!$groupValid($scope.roleSelect)) {
			logger.logWarning("Opss! You may have skipped specifying the Group's Type. Please try again.")
			return false;
		}
		if (!$groupValid($scope.groupSave.groupId))
			$scope.groupSave.groupId = 0;
		else
			group.groupId = $scope.groupSave.groupId;
		group.groupName = $scope.groupSave.groupName;
		group.active = $scope.active;
		group.roleTypeId=$scope.roleSelect;
		group.rights = $scope.groupSave.rights;
		group.createdBy = $rootScope.UsrRghts.sessionId;
		blockUI.start()
		groupSvc.UpdGroup(group).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The group information was saved succesfully")
				if (parseFloat(group.groupId) == parseFloat($rootScope.UsrRghts.userGroupId)) {
					localStorageService.clearAll();
					loginSvc.GetRights($rootScope.UsrRghts.sessionId).success(function (rightLst) {
						localStorageService.set('rxr', rightLst);
						$rootScope.UsrRghts = rightLst;
					}).error(function (data, status, headers, config) {
						logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
					});
				}
				$scope.groupSave.groupId ="";
				$scope.groupSave.groupName = "";
				$scope.groupSave.active=false;
				$scope.groupEditMode = false;
				$scope.groupSave.rights = [];
				$scope.loadGroupData();
				$scope.isDisabled = false;
				$scope.roleSelect="";
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
	 * cancels the operation of creating group 
	 */
	$scope.cancelGroup = function () {
		$scope.groupEditMode = false;
		$scope.isDisabled = false;
		$scope.groupSave.groupId = 0;
		$scope.groupSave.groupName = "";
		$scope.active = false;
	}


}]).factory('groupSvc', function ($http) {
	var ervGroupSvc = {};
	ervGroupSvc.GetGroups = function ($scope) {
		return $http({
			url: '/erevenue/rest/userGroups/gtGroups',
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		});
	};
	ervGroupSvc.UpdGroup = function (group) {
		return $http({
			url: '/erevenue/rest/userGroups/updGroup',
			method: 'POST',
			headers: {'Content-Type': 'application/json'},
			data: {
				'groupId': group.groupId,
				'groupName': group.groupName,
				'active': group.active,
				'rights': group.rights,
				'createdBy': group.createdBy,
				'roleTypeId':group.roleTypeId
			}
		});
	};
	ervGroupSvc.GetRights = function () {
		return $http({
			url: '/erevenue/rest/userGroups/gtRights/',
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		});
	};
	return ervGroupSvc;
}).factory('$groupValid', function () {
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