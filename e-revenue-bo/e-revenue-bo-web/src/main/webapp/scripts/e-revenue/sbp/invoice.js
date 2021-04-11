/**
 * Invoice Angular Module
 */
'use strict';
angular.module('app.invoice', []).controller("invoiceCtrl", ["$scope", "$filter", "invoiceSvc","$invoiceValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter,invoiceSvc,$invoicevalid, $rootScope, blockUI, logger, $location) {

	var init;
	$scope.feeValue=0;
	
	/**
	 * gets the list of invoices
	 */
    $scope.loadInvoiceData = function () {
        $scope.invocies = [], $scope.searchKeywords = "", $scope.filteredInvoices = [], $scope.row = "", $scope.invoiceEditMode = false;
        invoiceSvc.GetInvoices().success(function (response) {
            return $scope.invoices = response, $scope.searchKeywords = "", $scope.filteredInvoices= [], $scope.row = "", $scope.select = function (page) {
                var end, start;
                return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageInvoices = $scope.filteredInvoices.slice(start, end)
            }, $scope.onFilterChange = function () {
                return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
            }, $scope.onNumPerPageChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.onOrderChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.search = function () {
                return $scope.filteredInvoices = $filter("filter")($scope.invoices, $scope.searchKeywords), $scope.onFilterChange()
            }, $scope.order = function (rowName) {
                return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredInvoices = $filter("orderBy")($scope.invoices, rowName), $scope.onOrderChange()) : void 0
            }, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPages = [], (init = function () {
                return $scope.search(), $scope.select($scope.currentPage)
            })();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
        });
    }
    $scope.loadInvoiceData();
    
    /**
     * 
     * gets the list of invoices by signup user
     */
    $scope.loadInvoiceByLinkId = function () {
        $scope.invocies = [], $scope.searchKeywords = "", $scope.filteredInvoices = [], $scope.row = "", $scope.invoiceEditMode = false;
        invoiceSvc.GetInvoicesByLinkId($rootScope.UsrRghts.linkId,$rootScope.UsrRghts.linkExtInfo,$rootScope.UsrRghts.sessionId).success(function (response) {
            return $scope.invoices = response, $scope.searchKeywords = "", $scope.filteredInvoices= [], $scope.row = "", $scope.select = function (page) {
                var end, start;
                return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageInvoices = $scope.filteredInvoices.slice(start, end)
            }, $scope.onFilterChange = function () {
                return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
            }, $scope.onNumPerPageChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.onOrderChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.search = function () {
                return $scope.filteredInvoices = $filter("filter")($scope.invoices, $scope.searchKeywords), $scope.onFilterChange()
            }, $scope.order = function (rowName) {
                return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredInvoices = $filter("orderBy")($scope.invoices, rowName), $scope.onOrderChange()) : void 0
            }, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPages = [], (init = function () {
                return $scope.search(), $scope.select($scope.currentPage)
            })();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
        });
    }
    
   /**
    * gets the list of invoices based on user type
    */

    
  

} ]).factory('invoiceSvc', function ($http) {
	var ervInvopiceSvc = {};
	
	ervInvopiceSvc.GetInvoicesByLinkId = function (linkId,nationalId,agentId) {
		return $http({
			url: '/erevenue/rest/application/gtInvoicesByLinkId/'+linkId+','+nationalId+','+agentId,
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	ervInvopiceSvc.GetInvoices = function () {
		return $http({
			url: '/erevenue/rest/application/gtInvoices',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	return ervInvopiceSvc;
}).factory('$invoiceValid', function () {
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
}).directive("uiWizardForm", [function () {
	return {
		link: function (scope, ele) {
			return ele.steps()
		}
	}
}])