/**
 * Permit Angular Module
 */
'use strict';
angular.module('app.permit', []).controller("permitCtrl", ["$scope", "$filter", "permitSvc","$permitValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter,permitSvc,$permitvalid, $rootScope, blockUI, logger, $location) {

	var init;
	$scope.showStatus=true;
	$scope.permitPrintMode=true;
	$scope.statusSelect="";

	/**
	 * gets the list of permits
	 */
	$scope.loadPermitData = function () {
		$scope.permits = [], $scope.searchKeywords = "", $scope.filteredPermits = [], $scope.row = "", $scope.permitViewMode = false;
		permitSvc.GetPermits().success(function (response) {
			console.log(response,'=========================');
			return $scope.permits = response, $scope.searchKeywords = "", $scope.filteredPermits= [], $scope.row = "", $scope.select = function (page) {
				var end, start;
				return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPagePermits = $scope.filteredPermits.slice(start, end)
			}, $scope.onFilterChange = function () {
				return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
			}, $scope.onNumPerPageChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.onOrderChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.search = function () {
				return $scope.filteredPermits = $filter("filter")($scope.permits, $scope.searchKeywords), $scope.onFilterChange()
			}, $scope.order = function (rowName) {
				return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredPermits = $filter("orderBy")($scope.permits, rowName), $scope.onOrderChange()) : void 0
			}, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPages = [], (init = function () {
				return $scope.search(), $scope.select($scope.currentPage)
			})();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	$scope.loadPermitData();

	/**
	 * view the selected permit details and update the qr image for the same
	 */
	$scope.viewPermit=function(permit){
		if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" +
				$location.path())) {
			logger.log("Oh snap! You are not allowed to view the permit.");
			return false;
		}
		$scope.permitStatus=[{statusId:2,statusName:'Void'},{statusId:1,statusName:'Suspend'},
		                     {statusId:3,statusName:'ReApproved'}]
		if(permit.permitStatus=='PR'){
			$scope.showStatus=false;
			$scope.permitViewMode = true;

			angular.forEach($scope.permitStatus, function (item, index) {
				if (item.statusId == 2 || item.statusId == 3) {
					$scope.permitStatus.splice(index, 1);
				}
			});
		}else if(permit.permitStatus=='S'){
			$scope.showStatus=false;
			$scope.permitViewMode = true;

			angular.forEach($scope.permitStatus, function (item, index) {
				if (item.statusId == 1) {
					$scope.permitStatus.splice(index, 1);
					$scope.permitStatus.splice(index+1, 1);
				}
			});
		}else if(permit.permitStatus=='V'){
			logger.logWarning("Opss! Permit is voided. Please contact administrator.")
			return false;

		}else if(permit.permitStatus=='RA'){
			$scope.showStatus=true;
			$scope.permitPrintMode=false;
		}else{
			$scope.showStatus=true;
			$scope.permitPrintMode=false;
			$scope.permitViewMode = true;

		}
		$scope.statusSelect="";
		$scope.permitNo=permit.permitNo;
		$scope.businessNo=permit.businessNo;
		$scope.businessName=permit.businessName;
		$scope.businessDesc=permit.businessDesc;
		$scope.appNo=permit.appNo;
		$scope.postalCode=permit.postalCode;
		$scope.postalAdd=permit.postalAdd;
		$scope.regNo=permit.regNo;
		$scope.permitFee=permit.permitFee;
		$scope.email=permit.email;
		$scope.applicant=permit.applicant;
		$scope.wardName=permit.wardName;
		$scope.mktName=permit.mktName;
		$scope.applicant=permit.applicant;
		$scope.userName=permit.permitUser;
		$scope.plotNo=permit.plotNo;
		$scope.validity=permit.validity;
		$scope.expiryDate=permit.expiryDate;
		$scope.permitQr=permit.permitQr;
		$scope.amountInWords=permit.amountInWords;
		console.log($scope.amountInWords);
		$scope.landZone=permit.landZone;
		$scope.nationalId=permit.nationalId;
		var permit={};
		$scope.url="";
		permit.permitNo=$scope.permitNo;
		permit.permitQr=$scope.permitQr;
		permit.permitFee=$scope.permitFee;
		permit.amountInWords=$scope.amountInWords;
		if($scope.statusSelect==""){
			permit.status="PR";
		}

		blockUI.start()
		permitSvc.UpdQrImagePath(permit).success(function (response) {
			if (response.respCode == 200) {
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

	/**
	 * updates the permit status once its printed
	 */
	$scope.updatePermit=function(){
		var permit={};

		permit.permitNo=$scope.permitNo;
		if($scope.statusSelect==1){
			permit.permitStatus="S";
		}else if($scope.statusSelect==2){
			permit.permitStatus="V";
		}else if($scope.statusSelect==3){
			permit.permitStatus="RA";
		}
		if($scope.statusSelect==""){
			permit.permitStatus="PR";
		}
		if($scope.statusSelect=="RA"){
			permit.permitStatus="PR";
		}
		permit.permitFee=$scope.permitFee;
		blockUI.start()
		permitSvc.UpdPermitStatus(permit).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! Application information was saved succesfully")
				$scope.permitViewMode=false;
				$scope.permitNo="";
				$scope.permitPrintMode=true;
				$scope.showStatus=true;
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

	}
	
	/**
	 * cancels the operation of permit printing
	 */
	$scope.cancelPermit=function(){
		$scope.permitViewMode=false;
		$scope.permitNo="";
		$scope.permitPrintMode=true;
		$scope.statusSelect="";
		$scope.permitStatus=[{statusId:2,statusName:'Void'},{statusId:1,statusName:'Suspend'},
		                     {statusId:3,statusName:'ReApproved'}]
	}
	
	/**
	 * cancels the operation of permit status updating
	 */
	$scope.cancelPermitStatus=function(){
		$scope.permitViewMode=false;
		$scope.permitNo="";
		$scope.showStatus=true;
		$scope.permitPrintMode=true;
		$scope.statusSelect="";
		$scope.permitStatus=[{statusId:2,statusName:'Void'},{statusId:1,statusName:'Suspend'},
		                     {statusId:3,statusName:'ReApproved'}]
	}
	
	/**
	 * prints the selected permit 
	 */
	$scope.printPermit=function(){
		$scope.url = '/erevenue/reports?type=P&permitNo=' +$scope.permitNo+'&fee='+$scope.permitFee;
	}

} ]).factory('permitSvc', function ($http) {
	var ervPermitSvc = {};
	ervPermitSvc.GetPermits = function () {
		return $http({
			url: '/erevenue/rest/application/gtPermits',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	ervPermitSvc.UpdQrImagePath = function (permit) {
		return $http({
			url: '/erevenue/rest/application/updQrImagePath',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: permit

		});
	};
	ervPermitSvc.UpdPermitStatus = function (permit) {
		return $http({
			url: '/erevenue/rest/application/updPermitStatus',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: permit

		});
	};
	return ervPermitSvc;
}).factory('$permitValid', function () {
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