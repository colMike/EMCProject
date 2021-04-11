/**
 * Permit Angular Module
 */
'use strict';
angular.module('app.landrenewal', []).controller("landRenewCtrl", ["$scope", "$filter", "landRenewSvc","landApprovalSvc","$landRenewValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter,landRenewSvc,landApprovalSvc,$landRenewValid, $rootScope, blockUI, logger, $location) {

	var init;
	$scope.loadLandPermitData = function () {
		$scope.landpermits = [];
		$scope.landList = []	

		//get all permit lands auto complete function
		landApprovalSvc.GetPermitlands().success(function (response) {
			console.log(response);
			$scope.landList =[];
			$scope.landpermits=response;
			$scope.plotNumber = void 0;
			for (var i = 0; i <= response.length - 1; i++) {
				$scope.landList.push(response[i].plotNumber);
			}
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});      
	}
	$scope.loadLandPermitData();
	/*   $scope.loadPaidLandData = function () {
		   $scope.landpermits = [];
		   $scope.landList = []	
		   landApprovalSvc.GetPaidlands().success(function (response) {
				  $scope.landList =[];
				  $scope.landpermits=response;
	        	   $scope.plotNumber = void 0;
	               for (var i = 0; i <= response.length - 1; i++) {
	                   $scope.landList.push(response[i].plotNumber);
	               }
		   }).error(function (data, status, headers, config) {
			   logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		   });      
	    }*/
	//$scope.loadPaidLandData();

	/*watch function for plot number
	  if plot number is exist it will display in drop down*/

	$scope.$watch("plotNumber", function (newValue, oldValue) {
		if ($landRenewValid(newValue)) {
			if (newValue != oldValue) {
				for (var i = 0; i <=  $scope.landpermits.length - 1; i++) {
					if(newValue==$scope.landpermits[i].plotNumber){
						console.log($scope.yearList);
						$scope.permitNo=$scope.landpermits[i].permitNo;
						$scope.yearList=$scope.landpermits[i].yearList;
						$scope.landId=$scope.landpermits[i].landId;
						$scope.id=$scope.landpermits[i].id;
						$scope.fee=$scope.landpermits[i].fee;
						$scope.acreage=$scope.landpermits[i].acreage;
						$scope.permitStatus=$scope.landpermits[i].status;
					}
				}
			}
		}

	});
	//cancel function
	$scope.cancel=function(){
		$location.path('/dashboard');
	}
	// for inserting and updating land renewal
	$scope.updLandRenewal = function () {
		var renew = {};

		if (!$landRenewValid($scope.plotNumber)) {
			logger.logWarning("Opss! You may have skipped specifying the plot No. Please try again.")
			return false;
		}

		/* if (!$landRenewValid($scope.permitNo)) {
          logger.logWarning("Opss! You may have skipped specifying the permit No. Please try again.")
          return false;
      }*/
		if (!$landRenewValid($scope.yearSelect)) {
			logger.logWarning("Opss! You may have skipped specifying the year. Please try again.")
			return false;
		}
		if ($scope.permitStatus=='Renew') {
			logger.logWarning("Opss!Permit is already applied for renewal. Please try again.")
			return false;
		}
		renew.landNo = $scope.landNo;
		renew.plotNumber = $scope.plotNumber;
		renew.regNo=$scope.regNo;
		console.log("regNo"+renew.regNo)
		renew.permitNo = $scope.permitNo;
		renew.validity=$scope.year;
		renew.active = $scope.active;
		renew.createdBy =  $rootScope.UsrRghts.sessionId;
		renew.id=$scope.id;
		renew.fee=$scope.fee;
		renew.appliedFor=$scope.yearSelect;
		renew.acreage=$scope.acreage;
		blockUI.start()
		landRenewSvc.UpdLandRenew(renew).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The Renewal information was saved succesfully");
				$scope.landpermits = [];
				$scope.landList = [];
				$scope.yearList=[];
				$scope.landNo="";
				$scope.permitNo="";

				//$scope.loadPaidLandData();
				$scope.loadLandPermitData();

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

} ]).factory('landRenewSvc', function ($http) {
	var ervlandRenewSvc = {};

	ervlandRenewSvc.UpdLandRenew = function (permit) {
		return $http({
			url: '/erevenue/rest/landrate/updLandRenew',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: permit

		});
	};
	return ervlandRenewSvc;
}).factory('$landRenewValid', function () {
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


