/**
 * PermitType Angular Module
 */
'use strict';
angular.module('app.permittype', []).controller("permitTypeCtrl", ["$scope", "$filter", "permitTypeSvc","$permitTypeValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter,permitTypeSvc,$permitTypeValid, $rootScope, blockUI, logger, $location) {
	
    var init;
	$scope.permitTypes = [];
    $scope.permits = [];
	
    /**
     * gets the list of permit types
     */
    $scope.loadPermitTypeData = function () {
        $scope.permitTypes = [], $scope.searchKeywords = "", $scope.filteredPermitTypes = [], $scope.row = "", $scope.permitTypeEditMode = false;
        permitTypeSvc.GetPermitTypes().success(function (response) {
            return $scope.permitTypes = response, $scope.searchKeywords = "", $scope.filteredPermitTypes = [], $scope.row = "", $scope.select = function (page) {
                var end, start;
                return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPagePermitTypes = $scope.filteredPermitTypes.slice(start, end)
            }, $scope.onFilterChange = function () {
                return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
            }, $scope.onNumPerPageChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.onOrderChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.search = function () {
                return $scope.filteredPermitTypes = $filter("filter")($scope.permitTypes, $scope.searchKeywords), $scope.onFilterChange()
            }, $scope.order = function (rowName) {
                return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredPermitTypes = $filter("orderBy")($scope.permitTypes, rowName), $scope.onOrderChange()) : void 0
            }, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPages = [], (init = function () {
                return $scope.search(), $scope.select($scope.currentPage)
            })();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
        });
    }
    $scope.loadPermitTypeData();
    
    /**
     * gets the details of selected permit type
     * @param permitType-object of permitType
     */
    $scope.editPermitType = function (permitType) {
    	if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" +
    			$location.path())) {
    		logger.log("Oh snap! You are not allowed to modify the permit type.");
    		return false;
    	}
        $scope.permitTypeEditMode = true;
        $scope.isExisting = true;
        $scope.permitTypeId = permitType.permitTypeId;
        $scope.permitTypeCode = permitType.permitTypeCode;
        $scope.permitTypeName = permitType.permitTypeName;
        $scope.permitFee = permitType.permitFee;
        $scope.permit = permitType.permitType;
        $scope.isDisabled = true;
        $scope.active = permitType.active;
        
    };

    /**
     * gets the list of permit type business
     */
    $scope.loadPermits = function () {
        var permitL = {
            permitName: "LAND RATES",
            permitCode: "L"
        }

        var permitS = {
            permitName: "SBP",
            permitCode: "S"
        }

        $scope.permits.push(permitL);
        $scope.permits.push(permitS);
    };
    $scope.loadPermits();
    
    /**
     *  initialize the form for creating the new permit type
     */
    $scope.addPermitType = function () {
        
    	if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" +
    			$location.path())) {
    		logger.log("Oh snap! You are not allowed to create permit type.");
    		return false;
    	}
    
        $scope.permitTypeEditMode = true;
        $scope.isExisting = false;
        $scope.permitTypeId = 0;
        $scope.permitTypeCode = "";
        $scope.permitTypeName = "";
        $scope.permitFee = "";
        $scope.active = false;
        $scope.isDisabled = true;
        $scope.permit = "";
    };

    /**
     * cancels the operation permit type
     */
    $scope.cancelPermitType = function () {
        $scope.permitTypeEditMode = false;
        $scope.isExisting = false;
        $scope.permitTypeId = 0;
        $scope.permitTypeCode = "";
        $scope.permitTypeName = "";
        $scope.permitFee = "";
        $scope.active = false;
        $scope.isDisabled = true;
        $scope.permit = "";
    }
    
	/**
	 * updates the details of permit type
	 */
    $scope.updPermitType = function () {
        var permitType = {};

        if (!$permitTypeValid($scope.permitTypeCode)) {
            logger.logWarning("Opss! You may have skipped specifying the permit Type's Code. Please try again.")
            return false;
        }
        if (!$permitTypeValid($scope.permitTypeName)) {
            logger.logWarning("Opss! You may have skipped specifying the permit type's Name. Please try again.")
            return false;
        }
        if (!$permitTypeValid($scope.permitFee)) {
            logger.logWarning("Opss! You may have skipped specifying the permit type's fee. Please try again.")
            return false;
        }
        if (!$permitTypeValid($scope.permit)) {
            logger.logWarning("Opss! You may have skipped specifying the type. Please try again.")
            return false;
        }
        if (!$permitTypeValid($scope.permitTypeId))
        	permitType.permitTypeId = 0;
        else
        permitType.permitTypeId = $scope.permitTypeId;
        permitType.permitTypeCode = $scope.permitTypeCode;
        permitType.permitTypeName=$scope.permitTypeName;
        permitType.permitFee=$scope.permitFee;
        permitType.active = $scope.active;
        permitType.permitType = $scope.permit; /* Commented because the control in the view was commented; seemed repetition.*/
        permitType.createdBy = $rootScope.UsrRghts.sessionId;
        blockUI.start()
        permitTypeSvc.UpdPermitType(permitType).success(function (response) {
            if (response.respCode == 200) {
                logger.logSuccess("Great! The permit type information was saved succesfully")
                $scope.permitTypeId = 0;
                $scope.permitTypeCode = "";
                $scope.permitTypeName = "";
                //$scope.permit = "";
                $scope.active = false;
                $scope.permitTypeEditMode = false;
                $scope.isDisabled = false;
                $scope.loadPermitTypeData();
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

} ]).factory('permitTypeSvc', function ($http) {
    var ervPermitTypeSvc = {};
    
    ervPermitTypeSvc.GetPermitTypes = function () {
        return $http({
            url: '/erevenue/rest/permittype/gtPermitTypes/',
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
    };
    
    ervPermitTypeSvc.UpdPermitType = function (permitType) {
        return $http({
            url: '/erevenue/rest/permittype/updPermitType',
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            data: permitType
        });
    };
    return ervPermitTypeSvc;
}).factory('$permitTypeValid', function () {
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