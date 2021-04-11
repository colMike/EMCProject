/**
 * SubCounty Angular Module
 */
'use strict';
angular.module('app.subcounty', []).controller("subCountyCtrl", ["$scope", "$filter", "subCountySvc","$subCountyValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter,subCountySvc,$subCountyValid, $rootScope, blockUI, logger, $location) {
	
    var init;
	$scope.subcounties = [];
	
	/**
	 * gets the list of subcounties
	 */
    $scope.loadSubCountyData = function () {
        $scope.subcounties = [], $scope.searchKeywords = "", $scope.filteredSubCounties = [], $scope.row = "", $scope.subCountyEditMode = false;
        subCountySvc.GetSubCounties().success(function (response) {
            return $scope.subcounties = response, $scope.searchKeywords = "", $scope.filteredSubCounties = [], $scope.row = "", $scope.select = function (page) {
                var end, start;
                return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageSubCounties = $scope.filteredSubCounties.slice(start, end)
            }, $scope.onFilterChange = function () {
                return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
            }, $scope.onNumPerPageChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.onOrderChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.search = function () {
                return $scope.filteredSubCounties = $filter("filter")($scope.subcounties, $scope.searchKeywords), $scope.onFilterChange()
            }, $scope.order = function (rowName) {
                return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredSubCounties = $filter("orderBy")($scope.subcounties, rowName), $scope.onOrderChange()) : void 0
            }, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPages = [], (init = function () {
                return $scope.search(), $scope.select($scope.currentPage)
            })();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
        });
    }
    $scope.loadSubCountyData();
    
    /**
     * gets the detail of selected subcounty
     */
    $scope.editSubCounty = function (sunCounty) {
    	if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" +
    			$location.path())) {
    		logger.log("Oh snap! You are not allowed to modify the subcounty.");
    		return false;
    	}
        $scope.subCountyEditMode = true;
        $scope.isExisting = true;
        $scope.subCountyId = sunCounty.subCountyId;
        $scope.subCountyCode = sunCounty.subCountyCode;
        $scope.subCountyName = sunCounty.subCountyName;
        $scope.isDisabled = true;
        $scope.active = sunCounty.active;
        $scope.status=sunCounty.status;
        
    };
    
    /**
     *  initialize the form for creating the new subcounty
     */
    $scope.addSubCounty = function () {
        
    	if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" +
    			$location.path())) {
    		logger.log("Oh snap! You are not allowed to create subcounty.");
    		return false;
    	}
    
        $scope.subCountyEditMode = true;
        $scope.isExisting = false;
        $scope.subCountyId = 0;
        $scope.subCountyCode = "";
        $scope.subCountyName = "";
        $scope.active = false;
        $scope.isDisabled = true;
    };

    /**
     * cacels the operation of subcounty creation 
     */
    $scope.cancelSubCounty = function () {
        $scope.subCountyEditMode = false;
        $scope.active = false;
        $scope.isDisabled = false;
        $scope.subCountyId = 0;
        $scope.subCountyCode = "";
        $scope.subCountyName = "";
    }
    
    /**
     * updates the details of subcounty
     */
    $scope.updSubCounty = function () {
        var subCounty = {};

        if (!$subCountyValid($scope.subCountyCode)) {
            logger.logWarning("Opss! You may have skipped specifying the subCounty's Code. Please try again.")
            return false;
        }
        if (!$subCountyValid($scope.subCountyName)) {
            logger.logWarning("Opss! You may have skipped specifying the subCounty's Name. Please try again.")
            return false;
        }
      
       
        if (!$subCountyValid($scope.subCountyId))
        	subCounty.subCountyId = 0;
        else
        	subCounty.subCountyId = $scope.subCountyId;
        subCounty.subCountyCode = $scope.subCountyCode;
        subCounty.subCountyName=$scope.subCountyName;
        subCounty.active = $scope.active;
        subCounty.createdBy = $rootScope.UsrRghts.sessionId;
        blockUI.start()
        subCountySvc.UpdSubCounty(subCounty).success(function (response) {
            if (response.respCode == 200) {
                logger.logSuccess("Great! The subCounty information was saved succesfully")
                $scope.subCountyId = 0;
                $scope.subCountyCode = "";
                $scope.subCountyName = "";
                $scope.active = false;
                $scope.subCountyEditMode = false;
                $scope.isDisabled = false;
                $scope.loadSubCountyData();
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

} ]).factory('subCountySvc', function ($http) {
    var ervSubCountySvc = {};
    
    ervSubCountySvc.GetSubCounties = function () {
        return $http({
            url: '/erevenue/rest/subcounty/gtSubCounties/',
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
    };
    
    ervSubCountySvc.UpdSubCounty = function (subCounty) {
        return $http({
            url: '/erevenue/rest/subcounty/updSubCounty',
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            data: subCounty
        });
    };
    return ervSubCountySvc;
}).factory('$subCountyValid', function () {
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