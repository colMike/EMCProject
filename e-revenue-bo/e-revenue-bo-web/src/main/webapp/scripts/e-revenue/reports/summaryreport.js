/**
 * Transaction Held Angular Module
 */
'use strict';
angular.module('app.rptSummary', []).controller("rptSummaryCtrl",["$scope","$filter","serviceTxnSvc","summarySvc","$summaryValid","$rootScope","blockUI","logger","$location","$http","$window","memStatementSvc","marketSvc","wardSvc",function($scope, $filter,serviceTxnSvc,summarySvc,$summaryValid, $rootScope, blockUI, logger, $location,$http, $window,memStatementSvc,marketSvc,wardSvc) {
	$scope.transMemList = [];
	$scope.mem = [];
	$scope.eType = "P";
	$scope.serviceSelect="";

	$scope.services=[
	                 {'id':'s','name':'Service'},
	                 {'id':'M','name':'Market'},
	                 {'id':'u','name':'User'},
	                 {'id':'d','name':'Device'},
	                 {'id':'w','name':'Ward'},
	                 {'id':'sb','name':'SubCounty'}
	                 ];

    // $scope.$watch("serviceSelect", function (newValue, oldValue) {
    //     if ($serviceValid(newValue)) {
    //         if (newValue != oldValue) {
    //             if(newValue==='s'){
    //                 $scope.loadActiveSubCounties();
    //             }
    //
    //
    //
    //
    //
    //         }
    //     }
    //
    // });
    $scope.loadActiveSubCounties = function () {
        $scope.subCounties = [];
        wardSvc.GetActiveSubCounties().success(function (response) {
            $scope.subCounties = response;
        })
    };
    $scope.loadActiveSubCounties();

    $scope.loadActiveWardData = function (subCountyId) {
        $scope.wards = [];

        summarySvc.GetActiveWardsBySubcounty(subCountyId).success(function (response) {
            $scope.wards = response;
			console.log($scope.wards);
        })
    };
	//$scope.loadActiveWardData();


	$scope.$watch("subCountySelect", function (newValue, oldValue) {
		if ($summaryValid(newValue)) {
			if (newValue != oldValue) {
				// $scope.addChild(serviceSelect);
				$scope.loadActiveWardData(newValue);

			}
		}
	});


	$scope.loadParentSerData = function() {
		$scope.services = [];
		serviceTxnSvc.GetParentServices($scope).success(
				function(response) {
					return $scope.services = response;
				});
	};
	//$scope.loadParentSerData();
	$scope.exportPdfReport = function(mem) {
		console.log($scope.serviceSelect,"////////////");
		console.log($scope.subCountySelect,"/////////////");
		if (!$summaryValid($scope.serviceSelect)) {
			logger.logWarning("Opss! You may have skipped specifying the filter type. Please try again.")
			return false;
		}
		if (!$summaryValid(mem.FromDt)) {
			logger.logWarning("Opss! You may have skipped specifying the From Date. Please try again.")
			return false;
		}
		else if (!$summaryValid(mem.ToDt)) {
			logger.logWarning("Opss! You may have skipped specifying the To Date. Please try again.")
			return false;
		}else if (!$summaryValid( $scope.serviceSelect==='s'&& $scope.subCountySelect)) {
			logger.logWarning("Opss! You may have skipped specifying the SubCounty. Please try again.")
			return false;
		}
		else if (!$summaryValid($scope.serviceSelect==='s' && $scope.wardSelect)) {
			logger.logWarning("Opss! You may have skipped specifying the ward. Please try again.")
			return false;
		}

		var FromDt = $filter('date')(mem.FromDt, 'yyyy-MM-dd');
		var ToDt = $filter('date')(mem.ToDt, 'yyyy-MM-dd');
		var ward=$scope.wardSelect;

		console.log(ward,"////////////");
		console.log(ToDt,"888888888");

		if($scope.serviceSelect=='s'){
			$scope.url = '/erevenue/reports?type=S&eType=P&FrDt='+ FromDt + '&ToDt=' + ToDt + '&ward='+ ward;
		}
		if($scope.serviceSelect=='M'){
			$scope.url = '/erevenue/reports?type=M&eType=P&FrDt='+ FromDt + '&ToDt=' + ToDt;
		}
		if($scope.serviceSelect=='u'){
			$scope.url = '/erevenue/reports?type=u&eType=P&FrDt='+ FromDt + '&ToDt=' + ToDt;
		}
		if($scope.serviceSelect=='d'){
			$scope.url = '/erevenue/reports?type=d&eType=P&FrDt='+ FromDt + '&ToDt=' + ToDt;
		}
		if($scope.serviceSelect=='gs'){
			$scope.url = '/erevenue/reports?type=gs&eType=P&FrDt='+ FromDt + '&ToDt=' + ToDt;
		}
		if($scope.serviceSelect=='w'){
			$scope.url = '/erevenue/reports?type=w&eType=P&FrDt='+ FromDt + '&ToDt=' + ToDt;
		}
		if($scope.serviceSelect=='sb'){
			$scope.url = '/erevenue/reports?type=sb&eType=P&FrDt='+ FromDt + '&ToDt=' + ToDt;
		}

	}
	$scope.exportExcelReport = function(mem) {
		if (!$summaryValid($scope.serviceSelect)) {
			logger.logWarning("Opss! You may have skipped specifying the filter type. Please try again.")
			return false;
		}
		if (!$summaryValid(mem.FromDt)) {
			logger.logWarning("Opss! You may have skipped specifying the From Date. Please try again.")
			return false;
		}
		if (!$summaryValid(mem.ToDt)) {
			logger.logWarning("Opss! You may have skipped specifying the To Date. Please try again.")
			return false;
		}

		var FromDt = $filter('date')(mem.FromDt, 'yyyy-MM-dd');
		var ToDt = $filter('date')(mem.ToDt, 'yyyy-MM-dd');

		if($scope.serviceSelect=='s'){
			$scope.url = '/erevenue/reports?type=S&eType=E&FrDt='+ FromDt + '&ToDt=' + ToDt;
		}
		if($scope.serviceSelect=='M'){
			$scope.url = '/erevenue/reports?type=M&eType=E&FrDt='+ FromDt + '&ToDt=' + ToDt;
		}

		if($scope.serviceSelect=='u'){
			$scope.url = '/erevenue/reports?type=u&eType=E&FrDt='+ FromDt + '&ToDt=' + ToDt;
		}
		if($scope.serviceSelect=='d'){
			$scope.url = '/erevenue/reports?type=d&eType=E&FrDt='+ FromDt + '&ToDt=' + ToDt;
		}
		if($scope.serviceSelect=='gs'){
			$scope.url = '/erevenue/reports?type=gs&eType=E&FrDt='+ FromDt + '&ToDt=' + ToDt;
		}
		if($scope.serviceSelect=='w'){
			$scope.url = '/erevenue/reports?type=w&eType=E&FrDt='+ FromDt + '&ToDt=' + ToDt;
		}
		if($scope.serviceSelect=='sb'){
			$scope.url = '/erevenue/reports?type=sb&eType=E&FrDt='+ FromDt + '&ToDt=' + ToDt;
		}

	}
	$scope.loadAmpuntDetail=function(){
		memStatementSvc.GetCurrentDetailedTxn().success(function (response) {
			$scope.tempAmountList = response;
			if($scope.tempAmountList.length>0){

				for (var i = 0; i <= $scope.tempAmountList.length - 1; i++) {

					$scope.totalAmount=$scope.tempAmountList[i].totalAmount//+$scope.tempAmountList[i].invalidAmount;
					$scope.voideAmount=$scope.tempAmountList[i].voidAmount;
					$scope.invalidAmount=$scope.tempAmountList[i].invalidAmount;
					//	 $scope.netAmount=(($scope.totalAmount-$scope.voideAmount)-$scope.invalidAmount);
					$scope.netAmount=$scope.tempAmountList[i].netAmount;
					$scope.billNo=$scope.tempAmountList[i].billNo;
				}
			}
		})
	}
	$scope.loadAmpuntDetail();

} ]).factory('summarySvc', function($http) {

	var shpMemStatement = {};

	shpMemStatement.GetActiveWardsBySubcounty = function (subCountyId) {
		return $http({
			url: '/erevenue/rest/ward/gtWardBySubCounty/'+subCountyId,
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};

	return shpMemStatement;
}).factory('$summaryValid', function() {
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

});
