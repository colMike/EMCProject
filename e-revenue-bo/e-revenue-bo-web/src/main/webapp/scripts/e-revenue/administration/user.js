/**
 * User Angular Module
 */
'use strict';
angular.module('app.user', []).controller("usersCtrl", ["$scope", "$filter", "userSvc","groupSvc","submarketSvc","$userValid", "$rootScope", "blockUI", "logger", "$location","localStorageService","loginSvc", function ($scope, $filter,userSvc,groupSvc,submarketSvc,$userValid, $rootScope, blockUI, logger, $location,localStorageService,loginSvc) {

	var init;
	$scope.userGroups = [];
	$scope.selection=[];
	$scope.showMarkets=true;
	$scope.showUserTypes=false;
	
	/**
	 * gets the list of the active groups
	 */
	$scope.loadGroupList = function () {
		$scope.userGroups = [];
		userSvc.GetActiveGroups($scope).success(function (response) {
			$scope.userGroups = response;

		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	$scope.loadGroupList();
	
	/**
	 * gets the list of usertypes
	 */
	$scope.loadUserTypes = function () {
		$scope.userTypes = [];
		userSvc.GetUserTypes().success(function (response) {
			$scope.userTypes = response;

		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	$scope.loadUserTypes();
	
	/**
	 *gets the list of active markets
	 */
	$scope.loadActiveMarketData = function () {
		$scope.markets = [];
		submarketSvc.GetActiveMarkets().success(function (response) {
			$scope.markets = response
		})
	};


	/**
	 * gets the markets if the user type is 'POS' user
	 * else the markets will be not visible
	 */
	$scope.$watch("userTypeSelect", function (newValue, oldValue) {
		if ($userValid(newValue)) {
			if (newValue != oldValue) {
				if(newValue==1){
					$scope.showMarkets=false;
					//$scope.loadActiveMarketData(); 
				}else{
					$scope.showMarkets=true;
				}
			}
		}
	});

	/**
	 * gets list of users created
	 */
	$scope.loadUserData = function () {
		$scope.users = [], $scope.searchKeywords = "", $scope.filteredUsers = [], $scope.row = "", $scope.userEditMode = false, $scope.branchSelectionMode=false;
		userSvc.GetUsers($scope).success(function (response) {
			return $scope.users = response, $scope.searchKeywords = "", $scope.filteredUsers = [], $scope.row = "", $scope.select = function (page) {
				var end, start;
				return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageUsers = $scope.filteredUsers.slice(start, end)
			}, $scope.onFilterChange = function () {
				return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
			}, $scope.onNumPerPageChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.onOrderChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.search = function () {
				return $scope.filteredUsers = $filter("filter")($scope.users, $scope.searchKeywords), $scope.onFilterChange()
			}, $scope.order = function (rowName) {
				return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredUsers = $filter("orderBy")($scope.users, rowName), $scope.onOrderChange()) : void 0
			}, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPageUsers = [], (init = function () {
				return $scope.search(), $scope.select($scope.currentPage)
			})();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	$scope.loadUserData();

	/**
	 * gets the details for the selected user
	 * @param user-object of user details
	 */
	$scope.editUser = function (user) {
		if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" +
				$location.path())) {
			logger.log("Oh snap! You are not allowed to modify the user.");
			return false;
		}
		//$scope.loadActiveMarketData();
		$scope.userEditMode = true;
		$scope.branchSelectionMode=false;
		$scope.isExisting = true;
		$scope.userId = user.userId;
		$scope.userName = user.userName;
		$scope.userFullName = user.userFullName;
		$scope.userPwd = user.userPwd;
		$scope.groupSelect = user.groupId;
		$scope.userEmail = user.userEmail;
		$scope.userPhone = user.userPhone;
		$scope.questionSelect = user.userSecretQuestionId;
		$scope.userSecretAns = user.userSecretAns;
		$scope.active = user.active;
		$scope.branches=user.userBranchDetails;
		$scope.confirmPwd= user.userPwd;
		$scope.userTypeSelect=user.userTypeId;
		$scope.marketSelect=user.marketId;
		$scope.status=user.status;
		if($scope.userTypeSelect==2){
			$scope.showMarkets=true;
			$scope.marketSelect="";
		}else if( $scope.userTypeSelect==1){
			$scope.showMarkets=false;
			
		}
		if(user.userTypeId==3 || user.userTypeId==4){
			$scope.showMarkets=true;
			$scope.showUserTypes=true;
		}else{
			$scope.showUserTypes=false;
		}
		$scope.userNationalId=user.userNationalId;
	};
	
	/**
	 * initialize the form for creating the new user
	 */
	$scope.addUser = function () {
		if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" +
				$location.path())) {
			logger.log("Oh snap! You are not allowed to create users.");
			return false;
		}
		$scope.userEditMode = true;
		$scope.branchSelectionMode=false;
		$scope.showMarkets=true;
		$scope.isExisting = false;
		$scope.userId = 0;
		$scope.userName = "";
		$scope.userFullName = "";
		$scope.userPwd = "";
		$scope.groupSelect = "";
		$scope.userEmail = "";
		$scope.userPhone = "";
		$scope.questionSelect = "";
		$scope.userSecretAns = "";
		$scope.active = false;
		$scope.isDisabled = true;
		$scope.confirmPwd="";
		$scope.userTypeSelect="";
		$scope.userNationalId="";
		$scope.marketSelect="";
		$scope.showUserTypes=false;
	};
	$scope.loadActiveMarketData();
	/**
	 * cancel the user creation operation
	 */
	$scope.cancelUser = function () {
		$scope.userEditMode = false;
		$scope.showMarkets=true;
		$scope.branchSelectionMode=false;
		$scope.active = false;
		$scope.isExisting = false;
		$scope.userId = 0;
		$scope.userName = "";
		$scope.userFullName = "";
		$scope.userPwd = "";
		$scope.groupSelect = "";
		$scope.userEmail = "";
		$scope.userPhone = "";
		$scope.questionSelect = "";
		$scope.userSecretAns = "";
		$scope.active = false;
		$scope.isDisabled = true;
		$scope.confirmPwd="";
		$scope.userNationalId="";
		$scope.marketSelect="";
		$scope.showUserTypes=false;
	}

	/**
	 * updates the user details
	 */
	$scope.updUser = function () {
		var user = [];

		if (!$userValid($scope.userName)) {
			logger.logWarning("Opss! You may have skipped specifying the User's Name. Please try again.")
			return false;
		}
		if ($scope.userName.length > 10) {
			logger.logWarning("Opss!User Name is reach to maximum length of 10 ")
			return false;
		}
		if (!$userValid($scope.userFullName)) {
			logger.logWarning("Opss! You may have skipped specifying the User's Full Name. Please try again.")
			return false;
		}
		if (!$userValid($scope.userNationalId)) {
			logger.logWarning("Opss! You may have skipped specifying the User's National Id. Please try again.")
			return false;
		}
		if (!$userValid($scope.userPwd)) {
			logger.logWarning("Opss! You may have skipped specifying the User's userPwd. Please try again.")
			return false;
		}
		if (!$userValid($scope.confirmPwd)) {
			logger.logWarning("Opss! You may have skipped specifying the User's confirm userPwd. Please try again.")
			return false;
		}
		if ($scope.userPwd!=$scope.confirmPwd) {
			logger.logWarning("Opss! Password and Confirm password does not match. Please try again.")
			return false;
		}
		if (!$userValid($scope.groupSelect)) {
			logger.logWarning("Opss! You may have skipped specifying the User's Group. Please try again.")
			return false;
		}
		if(!$userValid($scope.userTypeSelect)){
			logger.logWarning("Opss! You may have skipped specifying the User's type. Please try again.")
			return false;
		}
		if($scope.userTypeSelect==1){
			if (!$userValid($scope.marketSelect)) {
				logger.logWarning("Opss! You may have skipped specifying the Market. Please try again.")
				return false;
			}
		}
		if (!$userValid($scope.userId))
			user.userId = 0;
		else
			user.userId = $scope.userId;
		user.userName = $scope.userName;
		user.userFullName=$scope.userFullName;
		user.userPwd = $scope.userPwd;
		user.groupId = $scope.groupSelect;
		user.userEmail = $scope.userEmail;
		user.userPhone = $scope.userPhone;
		user.active = $scope.active;
		user.userBranchDetails=$scope.branches;
		user.programmeId=($scope.programmeSelect==0?0:$scope.programmeSelect);
		user.createdBy =  $rootScope.UsrRghts.sessionId;
		user.userTypeId=$scope.userTypeSelect;
		user.userNationalId=$scope.userNationalId;
		if($scope.userTypeSelect==1){
			user.marketId=$scope.marketSelect;
		}
		blockUI.start()
		userSvc.UpdUser(user).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The user information was saved succesfully")
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
				$scope.userId = 0;
				$scope.userName = "";
				$scope.userFullName = "";
				$scope.userPwd = "";
				$scope.groupSelect = "";
				$scope.userEmail = "";
				$scope.userPhone = "";
				$scope.marketSelect = "";
				$scope.userSecretAns = "";
				$scope.active = false;
				$scope.userEditMode = false;
				$scope.showUserTypes=false;
				$scope.showMarkets=true;
				$scope.loadUserData();
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

} ]).factory('userSvc', function ($http) {
	var ervUserSvc = {};
	ervUserSvc.GetUsers = function ($scope) {
		return $http({
			url: '/erevenue/rest/user/gtUsers/',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};

	ervUserSvc.GetGroups = function ($scope) {
		return $http({
			url: '/erevenue/rest/userGroups/gtGroups/',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};

	ervUserSvc.GetUserTypes = function () {
		return $http({
			url: '/erevenue/rest/user/gtUserTypes/',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	ervUserSvc.GetActiveGroups = function () {
		return $http({
			url: '/erevenue/rest/user/gtActiveGroups/',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	ervUserSvc.UpdUser = function (user) {
		return $http({
			url: '/erevenue/rest/user/updUser',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: {
				'userId': user.userId,
				'userName': user.userName,
				'userFullName': user.userFullName,
				'userPwd': user.userPwd,
				'groupId': user.groupId,
				'userEmail': user.userEmail,
				'userPhone': user.userPhone,
				'active': user.active,
				'createdBy': user.createdBy,
				'userBranchDetails':user.userBranchDetails,
				'programmeId':user.programmeId,
				'userTypeId':user.userTypeId,
				'userNationalId':user.userNationalId,
				'marketId':user.marketId
			}
		});
	};
	return ervUserSvc;
}).factory('$userValid', function () {
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