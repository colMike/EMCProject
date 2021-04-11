/**
 * Permit Renewal Angular Module
 */
'use strict';
angular.module('app.permitrenewal', []).controller("permitRenewCtrl", ["$scope", "$filter", "permitRenewSvc","permitSvc","$permitRenewValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter,permitRenewSvc,permitSvc,$permitRenewValid, $rootScope, blockUI, logger, $location) {

	var init;
	$scope.businessNo="";
	$scope.yearSelect="";

	/**
	 * gets the permit details
	 */
	$scope.loadPermitData = function () {
		$scope.permits = [];
		$scope.businessList = []	
		permitSvc.GetPermits().success(function (response) {
			console.log(response);
			$scope.businessList =[];
			$scope.permits=response;
			$scope.businessNo = void 0;
			for (var i = 0; i <= response.length - 1; i++) {
				$scope.businessList.push(response[i].businessNo);
			}
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});      
	}
	$scope.loadPermitData();

	/**
	 * builds an array of business no for loading in autocomplete
	 */
	$scope.$watch("businessNo", function (newValue, oldValue) {
		if ($permitRenewValid(newValue)) {
			if (newValue != oldValue) {
				for (var i = 0; i <=  $scope.permits.length - 1; i++) {
					if(newValue==$scope.permits[i].businessNo){
						$scope.permitNo=$scope.permits[i].permitNo;
						$scope.yearList=$scope.permits[i].yearList;
						$scope.businessId=$scope.permits[i].businessId;
						$scope.permitFee=$scope.permits[i].permitFee;
						$scope.permitStatus=$scope.permits[i].status;
						$scope.validity=$scope.permits[i].validity;
					}
				}
			}
		}
		//else
		//$scope.businessList = [];
	});

	/**
	 * cancels the operation of permit renewal
	 */
	$scope.cancelPermitRenewal=function(){
		$scope.permits = [];
		$scope.businessList = [];
		$scope.yearSelect="";
		$scope.businessNo="";
		$scope.permitNo="";
		$scope.loadPermitData();
		$location.path('/dashboard');
	}

	/**
	 * updates the permit renewal details
	 */
	$scope.updPermitRenewal = function () {
		var renew = {};

		if (!$permitRenewValid($scope.businessNo)) {
			logger.logWarning("Opss! You may have skipped specifying the Business No. Please try again.")
			return false;
		}
		if (!$permitRenewValid($scope.permitNo)) {
			logger.logWarning("Opss! You may have skipped specifying the permit No. Please try again.")
			return false;
		}
		if (!$permitRenewValid($scope.yearSelect)) {
			logger.logWarning("Opss! You may have skipped specifying the year. Please try again.")
			return false;
		}
		if ($scope.permitStatus=='Renew') {
			logger.logWarning("Opss!Permit is already applied for renewal. Please try again.")
			return false;
		}
		if($scope.validity==parseFloat($scope.yearSelect)){
			logger.logWarning("Opss! Permit is already renewed for the selected year. Please try again.")
			return false;
		}
		renew.businessNo = $scope.businessNo;
		renew.permitNo = $scope.permitNo;
		renew.validity=$scope.yearSelect;
		renew.active = $scope.active;
		renew.createdBy =  $rootScope.UsrRghts.sessionId;
		renew.businessId=$scope.businessId;
		renew.permitFee=$scope.permitFee;
		blockUI.start()
		permitRenewSvc.UpdPermitRenew(renew).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The Renewal information was saved succesfully");
				$scope.permits = [];
				$scope.businessList = [];
				$scope.businessNo="";
				$scope.yearSelect=""
				$scope.permitNo="";
				$scope.loadPermitData();
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

} ]).factory('permitRenewSvc', function ($http) {
	var ervPermitRenewSvc = {};

	ervPermitRenewSvc.UpdPermitRenew = function (permit) {
		return $http({
			url: '/erevenue/rest/application/updPermitRenew',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: permit

		});
	};
	return ervPermitRenewSvc;
}).factory('$permitRenewValid', function () {
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
})


