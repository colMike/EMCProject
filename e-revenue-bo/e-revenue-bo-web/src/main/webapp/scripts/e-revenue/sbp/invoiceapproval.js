/**
 * 
 */
/**
 * User Angular Module
 */
'use strict';
angular.module('app.invoiceapproval', []).controller("invoiceapprovalCtrl", ["$scope", "$filter", "invoiceApprovalSvc","invoiceSvc","$invoiceApprovalValid", "$rootScope", "blockUI", "logger", "$location","$modal","$sce", function ($scope, $filter,invoiceApprovalSvc,invoiceSvc,$invoiceApprovalValid, $rootScope, blockUI, logger, $location,$modal,$sce) {

	var init;
	$scope.feeValue=0;
	$scope.showPaymentDetails=true;
	$scope.headerTag="";
	$scope.statusList=[];
	$scope.showPaymentModes=true;
	$scope.showReason=false;
	$scope.isExisting=false;
	$scope.paymentMode="";
	$scope.statusSelect="";
	var reason={}
	
	/**
	 * gets the list of invoice details
	 */
	$scope.loadInvoiceData = function () {
		$scope.invocies = [], $scope.searchKeywords = "", $scope.filteredInvoices = [], $scope.row = "", $scope.appViewMode = false;
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
	 * gets the details of selected invoice
	 */
	$scope.viewInvoice=function(invoice)
                  {
		if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" +
				$location.path()))
                                   {
			logger.log("Oh snap! You are not allowed to view the application.");
			return false;
		}
		
		$scope.appViewMode = true;
		$scope.invoiceDetailMode=false;
		$scope.businessName=invoice.businessName;
		$scope.postalAdd=invoice.postalAdd+"-"+invoice.postalCode;
		$scope.email=invoice.email;
		$scope.mobileNo=invoice.mobileNo;
		$scope.permitFee=invoice.permitFee;
		$scope.appNo=invoice.appNo;
		$scope.penalty=invoice.penalty;
		$scope.applicant=invoice.applicant;
		$scope.invoicedate = new Date();
		$scope.businessNo=invoice.businessNo;
		$scope.noOfEmployees=invoice.noOfEmployees;
		$scope.area=invoice.area;
		$scope.regNo=invoice.regNo;
		$scope.waterAccNo=invoice.waterAccNo;
		$scope.electricityAccNo=invoice.electricityAccNo;
		$scope.appStatus=invoice.status;
		$scope.businessId=invoice.businessId;
		$scope.appId=invoice.appId;
		$scope.appliedFor=invoice.appliedFor;
		$scope.approvedUser=invoice.approvedUser;
		$scope.paidUser=invoice.paidUser;
		$scope.paidOn=$filter('date')(new Date(invoice.paidDate),'MM-dd-yyyy');
		$scope.rejectReason=invoice.rejectReason;
		$scope.approvedOn=$filter('date')(new Date(invoice.approvedOn),'MM-dd-yyyy');
                                     $scope.paidStatus=invoice.paidStatus;
		$scope.appType=invoice.appType;
		$scope.preSbp=invoice.preSbp;
		$scope.getTotal(invoice);
		$scope.receiptNo="";
		$scope.statusSelect="";
		$scope.paymentMode="";
		$scope.statusList=[{statusId:1,statusName:'Approve'},
		                   {statusId:3,statusName:'Inspection'},
		                   {statusId:2,statusName:'Reject'},
		                   {statusId:4,statusName:'Refere to user'}]
		if($scope.appStatus=="Pending" || $scope.appStatus=="Renew" || $scope.appStatus=="Amended"){
			angular.forEach($scope.statusList, function (item, index) {
				if (item.statusId == 1  || item.statusId == 4 || item.statusId == 2) {
					$scope.statusList.splice(index, 1);
					$scope.statusList.splice(index
							+1, 2);
				}
				if(item.statusId == 2){
					
				}
			});
			$scope.showPaymentDetails=true;
			$scope.showPaymentModes=true;
			$scope.headerTag="Inspect Application";
		}else if($scope.appStatus=="Inspected"){
			angular.forEach($scope.statusList, function (item, index) {
				if (item.statusId == 3) {
					$scope.statusList.splice(index, 1);
				}
			});
			$scope.showPaymentDetails=true;
			$scope.showPaymentModes=true;
			$scope.headerTag="Approve/Reject Application";
		}else if($scope.appStatus=="Approved"){
			$scope.showPaymentDetails=false;
			$scope.showPaymentModes=false;
			$scope.headerTag="Payment Details";
		}else if($scope.appStatus=="Rejected"){
			$scope.showPaymentDetails=true;
			$scope.showPaymentModes=true;
			$scope.showReason=true;
			$scope.isExisting=true;
			$scope.headerTag="Application Rejected";
			$scope.rejectMessage="Application is rejected. Please contact administrator";
		}else if($scope.appStatus=="Refer to User"){
			$scope.showPaymentDetails=true;
			$scope.showPaymentModes=true;
			$scope.headerTag="Application Refered to user";
			$scope.rejectMessage="Application is refered to user. Please contact administrator";
		}
		if($scope.paidStatus=="Paid"){
			$scope.showPaymentDetails=false;
			$scope.showPaymentModes=true;
			$scope.headerTag="Payment Details";
		}
		
		$scope.bankName="";
		$scope.accNo="";
		$scope.transNo="";
	}

	/**
	 * cancels the operation of invoice approvals
	 */
	$scope.cancelInvoice=function(){
		$scope.appViewMode=false;
		$scope.showPaymentDetails=true;
		$scope.statusSelect="";
		$scope.statusList=[];
		$scope.rejectReason="";
		$scope.showReason=false;
	}

	/**
	 * prints the invoice and payment receipts
	 */
	$scope.printInvoice=function(){
		if($scope.paidStatus=="Paid"){
			$scope.url = '/erevenue/reports?type=R&businessNo=' +$scope.businessNo+'&paidUser='+$scope.paidUser+'&year='+$scope.appliedFor;
		}else{
			$scope.url = '/erevenue/reports?type=I&businessNo=' +$scope.businessNo+'&approvedUser='+$scope.approvedUser+'&year='+$scope.appliedFor;
		}
	}
	
	/**
	 * shows the texbox for entering reason if invoice status is reject 
	 */
	$scope.$watch("statusSelect", function (newValue, oldValue) {
		if ($invoiceApprovalValid(newValue)) {
			if (newValue != oldValue) {
				if(newValue==2){
					$scope.showReason=true;
				}else{
					$scope.showReason=false;
				}
			}
		}
	});
	
	/**
	 * update the invoice details
	 */
	$scope.updInvoiceStatus= function () 
                  {
		var app = {};
		if($scope.paidStatus=="Pending Payment" )
                                    {
			if (!$invoiceApprovalValid($scope.paymentMode)  && $scope.appStatus=="Approved") 
                                                      {
				logger.logWarning("Opss! You may have skipped specifying the payment mode. Please try again.");
				return false;
			}
		}
                                   else
                                   {
                                        if (!$invoiceApprovalValid($scope.statusSelect)) 
                                        {
                                                logger.logWarning("Opss! You may have skipped specifying the status. Please try again.")
                                                return false;
                                        }
                                    }
		
                                    if ($scope.statusSelect==1)
                                    {
			app.status = 'A';
		}
                                    else if($scope.statusSelect==2)
                                    {
                                        if (!$invoiceApprovalValid($scope.rejectReason)) 
                                        {
                                                logger.logWarning("Opss! You may have skipped specifying the reason. Please try again.")
                                                return false;
                                        }
                                        app.status = 'R';
                                        app.rejectReason=$scope.rejectReason;
		}
                                    else if($scope.statusSelect==3)
                                    {
			app.status = 'I';
		}else if($scope.statusSelect==4){
			app.status = 'RU';
		}else{
			app.status = 'N';
		}
		if($scope.paymentMode=="M" ){
			app.status="P";
                                                      app.paymentMode =   "MPesa";
                                                      
				if (!$invoiceApprovalValid($scope.mpesaCode)) {
					logger.logWarning("Opss! You may have skipped specifying the mpesa code. Please try again.");
					return false;
				}
                                                      app.transNo   =   $scope.mpesaCode;
			app.mpesaCode=$scope.mpesaCode;
		}if($scope.paymentMode=="K" ){
			app.status="P";
                                                      app.paymentMode =   "Cash";
			app.mpesaCode="";
		}else if($scope.paymentMode=="C"){
			app.status="P";
                                                      app.paymentMode =   "Bank";
				if (!$invoiceApprovalValid($scope.bankName)) {
					logger.logWarning("Opss! You may have skipped specifying the bank name. Please try again.");
					return false;
				}
			if (!$invoiceApprovalValid($scope.accNo)) {
				logger.logWarning("Opss! You may have skipped specifying the account number. Please try again.");
				return false;
			}
			if (!$invoiceApprovalValid($scope.transNo)) {
				logger.logWarning("Opss! You may have skipped specifying the transaction number. Please try again.");
				return false;
			}
			app.bankName=$scope.bankName;
			app.accNo=$scope.accNo;
			app.transNo=$scope.transNo;
		}
		app.appId=$scope.appId;
		app.businessId=$scope.businessId;
		app.businessNo=$scope.businessNo;
		app.createdBy = $rootScope.UsrRghts.sessionId;
		app.appType=$scope.appType;
		app.preSbp=$scope.preSbp;
		app.appliedFor=$scope.appliedFor;
                                    
                                    
		blockUI.start()
		invoiceApprovalSvc.UpdAppInvoiceStatus(app).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! Application information was saved succesfully")
				if(app.status == 'I' || app.status == 'R' || app.status == 'A'){
					$scope.appViewMode=false;
					$scope.showPaymentDetails=true;
				}
				$scope.active = false;
				$scope.priceDetailMode=true;
				$scope.appEditMode=false;
				$scope.appDetailMode=true;
				$scope.isDisabled = false;
				$scope.statusList=[];
				$scope.loadInvoiceData();
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

	/**
	 * gets the sum of penalty and amount of invoice
	 * jst to display in html-not saving in db
	 */
	$scope.getTotal = function(invoice){
		$scope.total = 0;
		for(var i = 0; i < $scope.invoices.length; i++){
			if(invoice.businessId==$scope.invoices[i].businessId){
				var obj = $scope.invoices[i];
				$scope.total = (obj.permitFee + obj.penalty);
			}


		}
		return $scope.total;
	}
//	$scope.open = function () {
//
//		// console.log(familyMemId);
//		var modalInstance = $modal.open({
//			templateUrl: 'views/NewFi.html',
//			controller: ModalInstanceCtrl,
//			backdrop: 'static',
//			keyboard: false,
//			scope: $scope,
//			resolve: {
//				reason:function(){
//					return reason;
//				}
//			}
//		});
//
//
//	};
//	var ModalInstanceCtrl = function ($scope, $modalInstance,reason) {
//
//		$scope.save = function () {
//			$modalInstance.close($scope.reason)
//			$scope.reason= $('#reasonDesc').val();
//			console.log($scope.reason)
//			var app = {};
//
//			app.status="C"
//				app.cancelReason=$scope.reason;
//			app.businessId=$scope.businessId;
//			app.createdBy = $rootScope.UsrRghts.sessionId;
//
//			blockUI.start()
//			invoiceApprovalSvc.UpdAppInvoiceStatus(app).success(function (response) {
//				if (response.respCode == 200) {
//					logger.logSuccess("Great! Application information was saved succesfully")
//					$scope.active = false;
//					$scope.priceDetailMode=true;
//					$scope.appEditMode=false;
//					$scope.appDetailMode=true;
//					$scope.isDisabled = false;
//					$$scope.loadInvoiceData();
//				}
//				else  {
//					logger.logWarning(response.respMessage);
//				}
//
//				blockUI.stop();
//			}).error(function (data, status, headers, config) {
//				logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
//				blockUI.stop();
//			});
//
//		}
//	}
} ]).factory('invoiceApprovalSvc', function ($http) {
	var ervInvopiceApprSvc = {};
	ervInvopiceApprSvc.UpdAppInvoiceStatus = function (device) {
		return $http({
			url: '/erevenue/rest/application/updInvoiceStatus',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: device

		});
	};

	return ervInvopiceApprSvc;
}).factory('$invoiceApprovalValid', function () {
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