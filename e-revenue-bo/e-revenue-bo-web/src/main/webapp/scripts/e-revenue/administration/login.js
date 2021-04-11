/**
 * Login Angular Module
 */
'use strict';
angular.module('app.login', []).controller('loginCtrl', function($scope, loginSvc,userSvc, $location, $rootScope, blockUI, logger, localStorageService, $loginValidation) {

	$scope.UserName = "";
	$scope.Password = "";
	$scope.UserId = 0;
	$rootScope.UsrRghts = [];
	$rootScope.showProfile="";
	$scope.showSignUp=true;
	
	/**
	 * validates the user manual login
	 * @param UserName
	 * @param Password
	 */
	$scope.Login = function(UserName, Password) {
		if (!$loginValidation(UserName)) {
			logger.logWarning("Opss! You may have skipped entering your user name. Please try again.")
			return false;
		}
		if (!$loginValidation(Password)) {
			logger.logWarning("Opss! You may have skipped entering your password. Please try again.")
			return false;
		}
		blockUI.start();
		localStorageService.clearAll();
		var loginUser = new Object();
		loginUser.userName = UserName;
		loginUser.userPwd = Password;
		$scope.userName = UserName;
		$scope.password = Password;
		loginSvc.AuthManual(loginUser).success(function(response) {
			if (response.respCode == 201) {
				logger.logWarning("Opss! You specified invalid login credentials, Please try again.")
			} else {
				if (response.respCode == 202) {
					logger.logWarning("Opss! User is diactivated, Please contact the adminstrator")

				}else{
					loginSvc.GetRights(response.userId).success(function(rightLst) {
						if (rightLst.respCode == 202) {
							logger.logWarning("Opss! User role is disabled, Please contact the adminstrator")
						}
						localStorageService.set('rxr', rightLst);
						$rootScope.UsrRghts = rightLst;
						$location.path('/dashboard');
						$rootScope.showProfile=$rootScope.UsrRghts.linkId;
					}).error(function(data, status, headers, config) {
						logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
					});
				}
			}
			blockUI.stop();
		}).error(function(data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
			blockUI.stop();
		});
	};
	
	/**
	 * switches to the form of signup
	 */
	$scope.signUp=function(){
		$scope.showSignUp=false;
	}
	
	/**
	 * switches to the form of manual login
	 */
	$scope.showLogin=function(){
		$scope.userId = "";
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
		$scope.userTypeSelect="";
	}

	/**
	 * gets the list of userTypes- only for signup user
	 */
	$scope.userTypes=[{userTypeId:3,userTypeName:'Agent'},
	                  {userTypeId:4,userTypeName:'Individual'}]

	/**
	 * updates the details for sign up user
	 */
	$scope.updSignUpDetails=function(){
		var user = [];
		if (!$loginValidation($scope.userName)) {
			logger.logWarning("Opss! You may have skipped specifying the User's Name. Please try again.")
			return false;
		}
		if ($scope.userName.length > 10) {
			logger.logWarning("Opss!User Name is reach to maximum length of 10 ")
			return false;
		}
		if (!$loginValidation($scope.userFullName)) {
			logger.logWarning("Opss! You may have skipped specifying the User's Full Name. Please try again.")
			return false;
		}
		if (!$loginValidation($scope.userNationalId)) {
			logger.logWarning("Opss! You may have skipped specifying the User's National Id. Please try again.")
			return false;
		}
		if (!$loginValidation($scope.userPwd)) {
			logger.logWarning("Opss! You may have skipped specifying the User's userPwd. Please try again.")
			return false;
		}
		if (!$loginValidation($scope.confirmPwd)) {
			logger.logWarning("Opss! You may have skipped specifying the User's confirm userPwd. Please try again.")
			return false;
		}
		if ($scope.userPwd!=$scope.confirmPwd) {
			logger.logWarning("Opss! Password and Confirm password does not match. Please try again.")
			return false;
		}
		if (!$loginValidation($scope.userEmail)) {
			logger.logWarning("Opss! You may have skipped specifying the User's Email or Invalid Email. Please try again.")
			return false;
		}
		if (!$loginValidation($scope.userId))
			user.userId = 0;
		else
			user.userId = $scope.userId;
		user.userName = $scope.userName;
		user.userFullName=$scope.userFullName;
		user.userPwd = $scope.userPwd;
		user.groupId = $scope.groupSelect;
		user.userEmail = $scope.userEmail;
		user.userPhone = $scope.userPhone;
		user.active =true;
		user.createdBy = $rootScope.UsrRghts.sessionId;
		user.userTypeId=$scope.userTypeSelect;
		user.userNationalId=$scope.userNationalId;
		blockUI.start()
		userSvc.UpdUser(user).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The User was saved succesfully")
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
				$scope.userTypeSelect="";
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

}).directive('showErrors', function($timeout) {
	return {
		restrict : 'A',
		require : '^form',
		link : function(scope, el, attrs, formCtrl) {
			// find the text box element, which has the
			// 'name' attribute
			// directive not assiging correct class, need to
			// check later
			var inputEl = el[0].querySelector("[name]");
			// convert the native text box element to an
			// angular element
			var inputNgEl = angular.element(inputEl);
			// get the name on the text box
			var inputName = inputNgEl.attr('name');

			// only apply the has-error class after the
			// user leaves the text box
			inputNgEl.bind('blur', function() {
				el.toggleClass('input-danger', formCtrl[inputName].$invalid);
			});

			scope.$on('show-errors-check-validity', function() {
				el.toggleClass('input-danger', formCtrl[inputName].$invalid);
			});

			scope.$on('show-errors-reset', function() {
				$timeout(function() {
					el.removeClass('input-danger');
				}, 0, false);
			});
		}
	}
}).factory('$loginValidation', function() {
	return function(valData) {
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
}).factory('loginSvc', function($http) {
	var evLoginSvc = {};

	evLoginSvc.AuthManual = function(loginUser) {
		return $http({
			url : '/erevenue/rest/login/manualAuth',
			method : 'POST',
			headers : {
				'Content-Type' : 'application/json'
			},
			data : loginUser

		});
	};
	evLoginSvc.AuthDeviceUser = function(deviceUser) {
		return $http({
			url : '/erevenue/rest/login/deviceAuth/',
			method : 'POST',
			headers : {
				'Content-Type' : 'application/json'

			},
			data : deviceUser
		});
	};
	evLoginSvc.GetRights = function(userId) {
		return $http({
			url : '/erevenue/rest/login/getUserRights/' + userId,
			method : 'GET',
			headers : {
				'Content-Type' : 'application/json'
			}
		});
	};
	return evLoginSvc;
});
