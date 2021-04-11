/**
 * Signup User Profile Agular Module
 */
'use strict';
angular.module('app.profile', []).controller("profileCtrl", ["$scope", "$filter", "profileSvc", "userSvc", "groupSvc","loginSvc", "localStorageService", "$profileValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter, profileSvc, userSvc,groupSvc, loginSvc, localStorageService, $profileValid, $rootScope, blockUI, logger, $location) {
    $scope.groups = [];
    $scope.questions = [];
   
    /**
     * gets the details for the sign up user profile
     */
    $scope.loadProfileDetail = function () {
        $scope.profileSave = [];
        profileSvc.GetUserById($rootScope.UsrRghts.sessionId).success(function (response) {
            blockUI.stop();
            $scope.userId = response.userId;
            $scope.userName = response.userName;
            $scope.userFullName = response.userFullName;
            $scope.userPwd = response.userPwd;
            $scope.groupId = response.groupId;
            $scope.userEmail = response.userEmail;
            $scope.userPhone = response.userPhone;
            $scope.userNationalId = response.userNationalId;
            $scope.profileSave.active = response.active;
            $scope.idDisabled = true;
        }).error(function (data, status, headers, config) {
            blockUI.stop();
            logger.logError("Oh snap! There is a problem with the server, please contact the administrator.");
        });
    };
    $scope.loadProfileDetail();
    
    /**
     * calcel the operation of profile editing
     */
    $scope.cancelProfile = function () {
        $scope.isDisabled = false;
        $scope.profileSave.userId =0;
        $scope.profileSave.userName = "";
        $scope.profileSave.userFullName ="";
        $scope.profileSave.userPwd = "";
        $scope.profileSave.groupSelect =0;
        $scope.profileSave.groupName ="";
        $scope.profileSave.userEmail = "";
        $scope.profileSave.userPhone = "";
        $scope.profileSave.questionSelect= "";
        $scope.profileSave.userSecretAns ="";
        $scope.profileSave.linkId = 0;
        $scope.profileSave.active = false;
        $scope.active = false;
        $location.path('/dashboard');
    };

    /**
     * update the profile details
     */
    $scope.updUserProfile=function(){
		var user = [];
		if (!$profileValid($scope.userName)) {
			logger.logWarning("Opss! You may have skipped specifying the User's Name. Please try again.")
			return false;
		}
		if ($scope.userName.length > 10) {
			logger.logWarning("Opss!User Name is reach to maximum length of 10 ")
			return false;
		}
		if (!$profileValid($scope.userFullName)) {
			logger.logWarning("Opss! You may have skipped specifying the User's Full Name. Please try again.")
			return false;
		}
		if (!$profileValid($scope.userNationalId)) {
			logger.logWarning("Opss! You may have skipped specifying the User's National Id. Please try again.")
			return false;
		}
		if (!$profileValid($scope.userPwd)) {
			logger.logWarning("Opss! You may have skipped specifying the User's userPwd. Please try again.")
			return false;
		}
		if (!$profileValid($scope.confirmPwd)) {
			logger.logWarning("Opss! You may have skipped specifying the User's confirm userPwd. Please try again.")
			return false;
		}
		if ($scope.userPwd!=$scope.confirmPwd) {
			logger.logWarning("Opss! Password and Confirm password does not match. Please try again.")
			return false;
		}
		if (!$profileValid($scope.userEmail)) {
            logger.logWarning("Opss! You may have skipped specifying the User's Email or Invalid Email. Please try again.")
            return false;
        }
	
		if (!$profileValid($scope.userId))
			user.userId = 0;
		else
			user.userId = $scope.userId;
		user.userName = $scope.userName;
		user.userFullName=$scope.userFullName;
		user.userPwd = $scope.userPwd;
		user.groupId = $scope.groupId;
		user.userEmail = $scope.userEmail;
		user.userPhone = $scope.userPhone;
		user.active =true;
		user.createdBy =  $rootScope.UsrRghts.sessionId;
		user.userTypeId=3;
		user.userNationalId=$scope.userNationalId;
		blockUI.start()
		userSvc.UpdUser(user).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The Agent information was saved succesfully")
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
				$scope.userId ="";
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
				$scope.showSignUp=true;
				$scope.showMarkets=true;
				$scope.userNationalId="";
				$scope.confirmPwd="";
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
} ]).factory('profileSvc', function ($http) {
    var mrmProfileSvc = {};
    mrmProfileSvc.GetUserById = function (userId) {
        return $http({
            url: '/erevenue/rest/user/gtUserById/' + userId,
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });
    };
    return mrmProfileSvc;
}).factory('$profileValid', function () {
    return function (valData) {
        if (angular.isUndefined(valData))
            return false;
        else {
            if (valData == null)
                return false;
            else {
                return !(valData.toString().trim() == "");
            }
        }
    };
}).filter('getGroupName', function () {
    return function (input, id) {
        var i = 0, len = input.length;
        for (; i < len; i++) {
        	//console.log("Checking: " + input[i].groupId + " against: " + id);
            if (input[i].groupId == id) {
                return input[i];
            }
        }
        return null;
    }
});