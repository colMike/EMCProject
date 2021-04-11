/**
 * Transaction Held Angular Module
 */
'use strict';
angular.module('app.rptServiceTransaction', []).controller("rptServiceTransactionCtrl", ["$scope", "$filter", "serviceTxnSvc","memStatementSvc","$serviceTxnValid", "$rootScope", "blockUI", "logger", "$location","$http","$window", function ($scope, $filter, serviceTxnSvc,memStatementSvc,$serviceTxnValid, $rootScope, blockUI, logger, $location,$http,$window) {
  
    $scope.mem=[];
    $scope.showFilters=true;
    $scope.showDetails=true;
    $scope.showSubService=true;
	var init;
	
	   $scope.loadParentSerData = function () {
	        $scope.services = [];
	        serviceTxnSvc.GetParentServices($scope).success(function (response) {
	            return $scope.services = response;
        });
    }
    $scope.loadParentSerData();
   
    $scope.loadSubSerData = function (parentServiceId) {
        $scope.subServices = [];
        serviceTxnSvc.GetSubServices(parentServiceId).success(function (response) {
            return $scope.subServices = response;
    });
}
  

    
    $scope.addFilters=function(){
    	  $scope.showFilters=false;
    }
    
    $scope.$watch("serviceSelect", function (newValue, oldValue) {
        if ($serviceTxnValid(newValue)) {
            if (newValue != oldValue) {
            	$scope.showSubService=false;
            	  $scope.loadSubSerData(newValue);
            }
        }
        else
            $scope.subServices = [];
    });

    $scope.loadServiceTxns = function () {
        $scope.userTxns = [], $scope.searchKeywords = "", $scope.filteredUserTxns = [], $scope.row = "", $scope.agentEditMode = false; $scope.previewServices=false;
 
        serviceTxnSvc.GetServiceTxns().success(function (response) {
        	console.log(response);
            return $scope.userTxns = response, $scope.searchKeywords = "", $scope.filteredUserTxns = [], $scope.row = "", $scope.select = function (page) {
                var end, start;
                return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageUserTxns = $scope.filteredUserTxns.slice(start, end)
            }, $scope.onFilterChange = function () {
                return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
            }, $scope.onNumPerPageChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.onOrderChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.search = function () {
                return $scope.filteredUserTxns = $filter("filter")($scope.userTxns, $scope.searchKeywords), $scope.onFilterChange()
            }, $scope.order = function (rowName) {
                return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredUserTxns = $filter("orderBy")($scope.userTxns, rowName), $scope.onOrderChange()) : void 0
            }, $scope.numPerPageOpt = [10, 20, 50, 100,200], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPageUserTxns = [], (init = function () {
                return $scope.search(), $scope.select($scope.currentPage)
            })();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
        });
    }
    
    $scope.loadServiceTxns ();
    
   $scope.previewReport=function(mem)
   {
//	   if (!$serviceTxnValid($scope.serviceSelect)) {
//           logger.logWarning("Opss! You may have skipped specifying the Parent Service . Please try again.")
//           return false;
//       }
	   $scope.showDetails=false;
	
	   $scope.fromDt= $filter('date')(mem.FromDt,'yyyy-MM-dd');
	   $scope.toDt=$filter('date')(mem.ToDt,'yyyy-MM-dd');
	   $scope.userTxns = [], $scope.searchKeywords = "", $scope.filteredUserTxns = [], $scope.row = "", $scope.agentEditMode = false; $scope.previewServices=false;
       var service ={};
       service.parentSerId=$scope.serviceSelect;
       service.subSerId=$scope.subSerSelect;
       service.fromDate=$scope.fromDt;
       service.toDate=$scope.toDt;
       serviceTxnSvc.GetServiceDetailedTxn(service).success(function (response) {
       	console.log(response);
           return $scope.userTxns = response, $scope.searchKeywords = "", $scope.filteredUserTxns = [], $scope.row = "", $scope.select = function (page) {
               var end, start;
               return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageUserTxns = $scope.filteredUserTxns.slice(start, end)
           }, $scope.onFilterChange = function () {
               return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
           }, $scope.onNumPerPageChange = function () {
               return $scope.select(1), $scope.currentPage = 1
           }, $scope.onOrderChange = function () {
               return $scope.select(1), $scope.currentPage = 1
           }, $scope.search = function () {
               return $scope.filteredUserTxns = $filter("filter")($scope.userTxns, $scope.searchKeywords), $scope.onFilterChange()
           }, $scope.order = function (rowName) {
               return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredUserTxns = $filter("orderBy")($scope.userTxns, rowName), $scope.onOrderChange()) : void 0
           }, $scope.numPerPageOpt = [10, 20, 50, 100,200], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPageUserTxns = [], (init = function () {
               return $scope.search(), $scope.select($scope.currentPage)
           })();
       }).error(function (data, status, headers, config) {
           logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
       });
   }
   
   $scope.loadAmpuntDetail=function(){
	   memStatementSvc.GetCurrentDetailedTxn().success(function (response) {
        $scope.tempAmountList = response;
        if($scope.tempAmountList.length>0){

        for (var i = 0; i <= $scope.tempAmountList.length - 1; i++) {
       	
       		 $scope.totalAmount=$scope.tempAmountList[i].totalAmount// +$scope.tempAmountList[i].invalidAmount;
       		 $scope.voideAmount=$scope.tempAmountList[i].voidAmount;
       		 $scope.invalidAmount=$scope.tempAmountList[i].invalidAmount;
      		// $scope.netAmount=(($scope.totalAmount-$scope.voideAmount)-$scope.invalidAmount);
       		 $scope.netAmount=$scope.tempAmountList[i].netAmount;
       		 $scope.billNo=$scope.tempAmountList[i].billNo;
       		
       		
       	 
       	
        }
        }
   	})
   }
   $scope.loadAmpuntDetail();
   
   $scope.exportPdfReport = function(mem) {
		 if (!$serviceTxnValid(mem.FromDt)) {
		 logger.logWarning("Opss! You may have skipped specifying the From Date. Please try again.")
		 return false;
		 }
		 else if (!$serviceTxnValid(mem.ToDt)) {
		 logger.logWarning("Opss! You may have skipped specifying the To Date. Please try again.")
		 return false;
		 }

		var FromDt = $filter('date')(mem.FromDt, 'yyyy-MM-dd');
		var ToDt = $filter('date')(mem.ToDt, 'yyyy-MM-dd');
		var serviceId=$scope.serviceSelect;
		var subSerId=$scope.subSerSelect;
		if(serviceId==null){
			serviceId=0;
		}
		if(subSerId==null){
			subSerId=0;
		}
		$scope.url = '/erevenue/reports?type=SD&eType=P&serId='+serviceId+'&subSerId='+subSerId+'&FrDt='+ FromDt + '&ToDt=' + ToDt;
	
	}
	$scope.exportExcelReport = function(mem) {
		 if (!$serviceTxnValid(mem.FromDt)) {
		 logger.logWarning("Opss! You may have skipped specifying the From Date. Please try again.")
		 return false;
		 }
		 else if (!$serviceTxnValid(mem.ToDt)) {
		 logger.logWarning("Opss! You may have skipped specifying the To Date. Please try again.")
		 return false;
		 }

		var FromDt = $filter('date')(mem.FromDt, 'yyyy-MM-dd');
		var ToDt = $filter('date')(mem.ToDt, 'yyyy-MM-dd');
		var serviceId=$scope.serviceSelect;
		var subSerId=$scope.subSerSelect;
		if(serviceId==null){
			serviceId=0;
		}
		if(subSerId==null){
			subSerId=0;
		}
		
		
			$scope.url = '/erevenue/reports?type=SD&eType=E&serId='+serviceId+'&subSerId='+subSerId+'&FrDt='+ FromDt + '&ToDt=' + ToDt;
	}

}]).factory('serviceTxnSvc', function ($http) {
    var compasRptServiceSvc = {};
    compasRptServiceSvc.GetParentServices = function () {
        return $http({
            url: '/erevenue/rest/report/gtParentSer/',
            method: 'GET',
            headers: { 'Content-Type': 'application/json' },
          
          
        });
    };
    compasRptServiceSvc.GetSubServices = function (parentServiceId) {
        return $http({
            url: '/erevenue/rest/report/gtSubSer/'+parentServiceId,
            method: 'GET',
            headers: { 'Content-Type': 'application/json' },
          
          
        });
    };
    compasRptServiceSvc.GetServiceTxns = function () {
        return $http({
            url: '/erevenue/rest/report/gtServiceTxns/',
            method: 'GET',
            headers: { 'Content-Type': 'application/json' },
          
          
        });
    };
    compasRptServiceSvc.GetServiceDetailedTxn = function (service) {
        return $http({
            url: '/erevenue/rest/report/gtServiceTxnDetail/',
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            data:service
          
        });
    };
    
    return compasRptServiceSvc;
}).factory('$serviceTxnValid', function () {
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
