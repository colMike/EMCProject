/**
 * 
 */
/**
 * User Angular Module
 */
'use strict';
angular.module('app.landapproval', []).controller("landapprovalCtrl", ["$scope", "$filter", "landApprovalSvc","marketSvc","wardSvc","applicationSvc","$landApprovalValid", "$rootScope", "blockUI", "logger", "$location","$modal", function ($scope, $filter,landApprovalSvc,marketSvc,wardSvc,applicationSvc,$landApprovalValid, $rootScope, blockUI, logger, $location,$modal) {

	var init;
	$scope.feeValue=0;
	$scope.showPaymentDetails=true;
	$scope.headerTag="";
	$scope.statusList=[];
	$scope.showPaymentModes=true;
	$scope.showReason=false;
	$scope.isExisting=false;
	var reason={}

	//for displaying all land invoices

	$scope.loadlandInvoiceData = function () {
		$scope.landinvocies = [], $scope.searchKeywords = "", $scope.filteredlandInvoices = [], $scope.row = "", $scope.landViewMode = false;
		landApprovalSvc.GetlandInvoices().success(function (response) {
			console.log(response,"]]]]")
			return $scope.landinvoices = response, $scope.searchKeywords = "", $scope.filteredlandInvoices= [], $scope.row = "", $scope.select = function (page) {
				var end, start;
				return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPagelandInvoices = $scope.filteredlandInvoices.slice(start, end)
			}, $scope.onFilterChange = function () {
				return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
			}, $scope.onNumPerPageChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.onOrderChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.search = function () {
				return $scope.filteredlandInvoices = $filter("filter")($scope.landinvoices, $scope.searchKeywords), $scope.onFilterChange()
			}, $scope.order = function (rowName) {
				return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredlandInvoices = $filter("orderBy")($scope.landinvoices, rowName), $scope.onOrderChange()) : void 0
			}, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPages = [], (init = function () {
				return $scope.search(), $scope.select($scope.currentPage)
			})();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}

	//get all land rate invoice list for individual signup and agent signup

	$scope.loadlandInvoiceByLinkId = function () {
		$scope.landinvocies = [], $scope.searchKeywords = "", $scope.filteredlandInvoices = [], $scope.row = "", $scope.landViewMode = false;
		landApprovalSvc.GetlandInvoicesByLinkId($rootScope.UsrRghts.linkId,$rootScope.UsrRghts.linkExtInfo,$rootScope.UsrRghts.sessionId).success(function (response) {
			return $scope.landinvoices = response, $scope.searchKeywords = "", $scope.filteredlandInvoices= [], $scope.row = "", $scope.select = function (page) {
				var end, start;
				return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPagelandInvoices = $scope.filteredlandInvoices.slice(start, end)
			}, $scope.onFilterChange = function () {
				return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
			}, $scope.onNumPerPageChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.onOrderChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.search = function () {
				return $scope.filteredlandInvoices = $filter("filter")($scope.landinvoices, $scope.searchKeywords), $scope.onFilterChange()
			}, $scope.order = function (rowName) {
				return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredlandInvoices = $filter("orderBy")($scope.landinvoices, rowName), $scope.onOrderChange()) : void 0
			}, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPages = [], (init = function () {
				return $scope.search(), $scope.select($scope.currentPage)
			})();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}

	/*condition for checking individual signup or agent signup based on link id
	  if yes display all registration details by using link id otherwise display
	  all registration details*/

	if($rootScope.UsrRghts.linkId==3 || $rootScope.UsrRghts.linkId==4){
		$scope.loadlandInvoiceByLinkId()
	}else{
		$scope.loadlandInvoiceData();
	}

	//display all wards by using sub county

	$scope.loadActiveWardData = function (subCountyId) {
		$scope.wards = [];

		applicationSvc.GetWardsBySubCounty(subCountyId).success(function (response) {
			$scope.wards = response
		})
	};
	//$scope.loadActiveWardData();

	//for all active sub counties

	$scope.loadActiveSubCounties = function () {
		$scope.subCounties = [];

		wardSvc.GetActiveSubCounties().success(function (response) {
			$scope.subCounties = response
		})
	};

	/*watch function for sub county select
	 based on sub county selection it will display wards*/

	$scope.$watch("subCountySelect", function (newValue, oldValue) {
		if ($landApprovalValid(newValue)) {
			if (newValue != oldValue) {
				$scope.showWards=false;
				$scope.loadActiveWardData(newValue);
			}
		}

	});

	// for displaying land permit types

	$scope.loadActivePermitTypes= function () {
		$scope.permitTypes = [];
		applicationSvc.GetActivePermitTypes().success(function (response) {
			$scope.permitTypes = response
		})
	};
	$scope.loadActivePermitTypes();

	// view function for land invoice

	$scope.viewlandInvoice=function(landinvoice){
		if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" +
				$location.path())) {
			logger.log("Oh snap! You are not allowed to view the application.");
			return false;
		}

		$scope.landViewMode = true;
		$scope.invoiceDetailMode=false;
		$scope.plotNumber=landinvoice.plotNumber;
		$scope.mapSheetNumber=landinvoice.mapSheetNumber;
		$scope.location=landinvoice.location;
		$scope.acreage=landinvoice.acreage;
		$scope.titleDeedNumber=landinvoice.titleDeedNumber;
		$scope.name=landinvoice.name;
		$scope.krapin=landinvoice.krapin;
		$scope.nationalIdNumber=landinvoice.nationalIdNumber;
		//$scope.subCountySelect=landinvoice.subCountyId;
		//$scope.wardSelect=landinvoice.wardId;
		$scope.paidStatus=landinvoice.paidStatus;
		$scope.subCountyName=landinvoice.subCountyName;
		$scope.wardName=landinvoice.wardName;
		$scope.permitTypeSelect=landinvoice.permitTypeId;
		$scope.invoicedate = new Date();
		$scope.landNo=landinvoice.landNo;
		$scope.regNo=landinvoice.regNo;
		$scope.landStatus=landinvoice.status;
		$scope.id=landinvoice.id;
		$scope.landId=landinvoice.landId;
		$scope.approvedUser=landinvoice.approvedUser;
		$scope.rejectReason=landinvoice.rejectReason;
		var dt=$filter('date')(new Date(landinvoice.approvedOn),'MM-dd-yyyy');
		$scope.approvedOn=dt;
		
		$scope.fee=landinvoice.fee;
		$scope.penalty=landinvoice.penalty;
		$scope.getTotal(landinvoice);
		$scope.paidUser=landinvoice.paidUser;
		$scope.paidOn=$filter('date')(new Date(landinvoice.paidDate),'MM-dd-yyyy');
		$scope.appliedFor=landinvoice.appliedFor;
		$scope.approvedOn=$filter('date')(new Date(landinvoice.approvedOn),'MM-dd-yyyy');
		$scope.landType=landinvoice.landType;
		$scope.preLr=landinvoice.preLr;
		$scope.landTypeName=landinvoice.landTypeName;
		
		if($scope.paidStatus=="Paid"|| $scope.paidStatus=="Paid Partially"){
			$scope.balance=landinvoice.balance;
			console.log($scope.paidStatus+",,,,,,");
		}else{
			console.log($scope.paidStatus+",,,,,,");
		$scope.balance=landinvoice.balance+$scope.penalty;
		console.log($scope.balance);
		}
		$scope.amount=0.00;//landinvoice.amount;
		$scope.amountpaid=landinvoice.amount;
                                    $scope.paidStatus = landinvoice.paidStatus;
		$scope.receiptNo="";
		$scope.statusSelect="";
		$scope.paymentMode="";
                                    
		$scope.statusList=[{statusId:1,statusName:'Approve'},
		                   {statusId:3,statusName:'Inspection'},
		                   {statusId:2,statusName:'Reject'},
		                   {statusId:4,statusName:'Refere to user'}]
		if($scope.landStatus=="Pending"|| $scope.landStatus=="Renew" || $scope.landStatus=="Amended" || $scope.landStatus=="Refer to User"){
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
		}else if($scope.landStatus=="Inspected"){
			angular.forEach($scope.statusList, function (item, index) {
				if (item.statusId == 3) {
					$scope.statusList.splice(index, 1);
				}
			});
			$scope.showPaymentDetails=true;
			$scope.showPaymentModes=true;
			$scope.headerTag="Approve/Reject Application";
		}else if($scope.landStatus=="Approved"){
			$scope.showPaymentDetails=false;
			$scope.showPaymentModes=false;
			$scope.headerTag="Payment Details";
		}else if($scope.landStatus=="Paid"){
			$scope.showPaymentDetails=false;
			$scope.showPaymentModes=false;
			$scope.headerTag="Payment Details";
		}else if($scope.landStatus=="Rejected"){
			$scope.showPaymentDetails=true;
			$scope.showPaymentModes=true;
			$scope.showReason=true;
			$scope.isExisting=true;
			$scope.headerTag="Application Rejected";
			$scope.rejectMessage="Application is rejected. Please contact administrator";
		}else if($scope.landStatus=="Refer to User"){
			$scope.showPaymentDetails=true;
			$scope.showPaymentModes=true;
			//$scope.showReason=true;

			$scope.headerTag="Application Refered to user";
			$scope.rejectMessage="Application is refered to user. Please contact administrator";
		}
		if($scope.paidStatus=="Paid" && $scope.balance==0){
			$scope.showPaymentDetails=false;
			$scope.showPaymentModes=false;
			$scope.headerTag="Payment Details";
		}if($scope.paidStatus=="Paid Partially" && $scope.balance!=0){
			$scope.showPaymentDetails=false;
			$scope.showPaymentModes=false;
			$scope.headerTag="Payment Details";
		}
		$scope.bankName="";
		$scope.accNo="";
		$scope.transNo="";
	}


	//cancel function

	$scope.cancellandInvoice=function(){
		$scope.landViewMode=false;
		$scope.showPaymentDetails=true;
		$scope.statusSelect="";
		$scope.rejectReason="";
		$scope.showReason=false;
		$scope.statusList=[];
	}

	//reject land invoice function

	$scope.rejectlandInvoice=function(){
		var land= {};

		land.status="C"
			land.id=$scope.id;
		land.landId=$scope.landId;
		land.createdBy = $rootScope.UsrRghts.sessionId;

		blockUI.start()
		landinvoiceApprovalSvc.UpdlandInvoiceStatus(land).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! Application information was saved succesfully")
				$scope.active = false;
				//$scope.priceDetailMode=true;
				$scope.landEditMode=false;
				$scope.landDetailMode=true;
				$scope.isDisabled = false;
				//$scope.statusList=[];
				$$scope.loadlandInvoiceData();
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

	/*print land invoice function
	 if status is paid it will display land rate payment receipt report
	 otherwise it will display invoice report*/

	$scope.printlandInvoice=function(){
		//$scope.url = '/erevenue/reports?type=I&plotNumber=' +$scope.plotNumber+'&approvedUser='+$scope.approvedUser;
		if($scope.paidStatus=="Paid"){
			$scope.url = '/erevenue/reports?type=LRRec&plotNumber=' +$scope.plotNumber+'&paidUser='+$scope.paidUser+'&year='+$scope.appliedFor;
		}else{
			$scope.url = '/erevenue/reports?type=LRInv&plotNumber=' +$scope.plotNumber+'&approvedUser='+$scope.approvedUser+'&year='+$scope.appliedFor;
		}
                
                console.log("Url: "+$scope.url)
	}
	$scope.$watch("statusSelect", function (newValue, oldValue) {
		if ($landApprovalValid(newValue)) {
			if (newValue != oldValue) {
				if(newValue==2){
					$scope.showReason=true;
				}else{
					$scope.showReason=false;
				}

			}


		}
		//else
		//$scope.groups = [];
	});

	// for inserting and updating land invoice status with payment mode

	$scope.updlandInvoiceStatus= function () {
		var land = {};
//		if (!$landApprovalValid($scope.statusSelect)) {
//			logger.logWarning("Opss! You may have skipped specifying the status. Please try again.")
//			return false;
//		}
                                
		if ($scope.statusSelect==1){
			land.status = 'A';
		}else if($scope.statusSelect==2){
			if (!$landApprovalValid($scope.rejectReason)) {
				logger.logWarning("Opss! You may have skipped specifying the reason. Please try again.")
				return false;
			}
			land.status = 'R';
			land.rejectReason=$scope.rejectReason;
		}else if($scope.statusSelect==3){
			land.status = 'I';
		}else if($scope.statusSelect==4){
			land.status = 'RU';
		}else{
			land.status = 'N';
		}
                
                                   //return logger.logError(land.status)
                                    if($scope.statusSelect != 3 && $scope.statusSelect != 1  && $scope.statusSelect != 4 && $scope.statusSelect != 2 )
                                    {
                                                if($scope.paymentMode == null || $scope.paymentMode==""){
                                                       logger.logWarning("Opss! You may have skipped selecting payment mode.");
                                                       return false;
                                                   }
                                                if($scope.amount == null || $scope.amount=="0" || $scope.amount==""){
                                                        logger.logWarning("Opss! Amount cannot be zero or null!");
                                                        return false;
                                                }
                                                if($scope.paymentMode=="M"){

                                                        land.status="P";
                                                        if (!$landApprovalValid($scope.mpesaCode)) {
                                                                logger.logWarning("Opss! You may have skipped specifying the mpesa code. Please try again.")
                                                                return false;
                                                        }
                                                        land.mpesaCode=$scope.mpesaCode;
                                                }
                                                 if($scope.paymentMode=="K" ){

                                                                 land.status="P";

                                                                 /*if (!$landApprovalValid($scope.mpesaCode)) {
                                                                         logger.logWarning("Opss! You may have skipped specifying the receipt no. Please try again.")
                                                                         return false;
                                                                 }*/
                                                         //app.mpesaCode="888455";//$scope.mpesaCode;
                                                 }
                                                 else if($scope.paymentMode=="C"){

                                                land.status="P";

                                                 if (!$landApprovalValid($scope.bankName)) {
                                                         logger.logWarning("Opss! You may have skipped specifying the bank name. Please try again.")
                                                         return false;
                                                 }
                                                if (!$landApprovalValid($scope.accNo)) {
                                                        logger.logWarning("Opss! You may have skipped specifying the account number. Please try again.")
                                                        return false;
                                                }
                                                if (!$landApprovalValid($scope.transNo)) {
                                                        logger.logWarning("Opss! You may have skipped specifying the transaction number. Please try again.")
                                                        return false;
                                                }
                                                land.bankName=$scope.bankName;
                                                land.accNo=$scope.accNo;
                                                land.transNo=$scope.transNo;
                                           }
                                    }
                                   
		land.landId=$scope.landId;
		land.id=$scope.id;
		land.landNo=$scope.landNo;
		land.regNo=$scope.regNo;
		console.log(land.regNo);
		//land.regId=$scope.regId;
		land.createdBy = $rootScope.UsrRghts.sessionId;
		land.landType=$scope.landType;
		land.preLr=$scope.preLr;
		land.appliedFor=$scope.appliedFor;
		land.amount=$scope.amount;
		land.balance=$scope.balance-$scope.amountpaid;
		console.log(land.balance,"//////////");
		console.log(land.amount);
		land.balance=$scope.balance-$scope.amount;
		console.log($scope.total,"toatal")
		console.log($scope.amountpaid,"paid");
		console.log($scope.penalty,"pena");
		console.log(land.balance,"?????");

                
                                    if(land.balance<0)
                                    {
										console.log(land.balance,"?????");
                                        logger.logError("Paying more than the indicated amount not allowed");
                                        
                                        return false;
                                    }
                                    
		blockUI.start()
		landApprovalSvc.UpdlandInvoiceStatus(land).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! Land information was saved succesfully")
				if(land.status == 'I' || land.status == 'R' || land.status == 'A'){
					$scope.landViewMode=false;
					$scope.showPaymentDetails=true;
				}
				$scope.active = false;
				//$scope.priceDetailMode=true;
				$scope.landEditMode=false;
				$scope.landDetailMode=true;
				$scope.isDisabled = false;
				if($rootScope.UsrRghts.linkId==3 || $rootScope.UsrRghts.linkId==4){
					$scope.loadlandInvoiceByLinkId()
				}else{
					$scope.loadlandInvoiceData();
				}
				$scope.statusList=[];
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

	//get total land invoice

	$scope.getTotal = function(landinvoice){
		$scope.total = 0;
		for(var i = 0; i < $scope.landinvoices.length; i++){
			if(landinvoice.id==$scope.landinvoices[i].id){
				var obj = $scope.landinvoices[i];
				console.log(obj.fee);
				$scope.total += (((obj.fee)*(obj.acreage)) + obj.penalty );
			}


		}
		return $scope.total;
	}

	//for displaying reason for reject

	$scope.open = function () {

		// console.log(familyMemId);
		var modalInstance = $modal.open({
			templateUrl: 'views/NewFi.html',
			controller: ModalInstanceCtrl,
			backdrop: 'static',
			keyboard: false,
			scope: $scope,
			resolve: {
				reason:function(){
					return reason;
				}
			}
		});


	};

	//for saving reason

	var ModalInstanceCtrl = function ($scope, $modalInstance,reason) {

		$scope.save = function () {
			$modalInstance.close($scope.reason)
			$scope.reason= $('#reasonDesc').val();
			console.log($scope.reason)
			var land = {};

			land.status="C"
			land.cancelReason=$scope.reason;
			land.id=$scope.id;
			land.createdBy = $rootScope.UsrRghts.sessionId;

			blockUI.start()
			landApprovalSvc.UpdlandInvoiceStatus(land).success(function (response) {
				if (response.respCode == 200) {
					logger.logSuccess("Great! Land information was saved succesfully")
					$scope.active = false;
					//$scope.priceDetailMode=true;
					$scope.landEditMode=false;
					$scope.landDetailMode=true;
					$scope.isDisabled = false;
					$$scope.loadlandInvoiceData();
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
	}
} ]).factory('landApprovalSvc', function ($http) {
	var ervlandInvopiceApprSvc = {};
	ervlandInvopiceApprSvc.UpdlandInvoiceStatus = function (land) {
		return $http({
			url: '/erevenue/rest/landrate/updlandInvoiceStatus',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: land

		});
	};//
	ervlandInvopiceApprSvc.GetlandInvoicesByLinkId = function (linkId,nationalId,agentId) {
		return $http({
			url: '/erevenue/rest/landrate/gtLandInvoicesByLinkId/'+linkId+','+nationalId+','+agentId,
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	ervlandInvopiceApprSvc.GetlandInvoices = function () {
		return $http({
			url: '/erevenue/rest/landrate/gtlandInvoices',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	ervlandInvopiceApprSvc.GetPaidlands = function () {
		return $http({
			url: '/erevenue/rest/landrate/gtPaidlands',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	ervlandInvopiceApprSvc.GetPermitlands = function () {
		return $http({
			url: '/erevenue/rest/landrate/gtPermitlands',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	return ervlandInvopiceApprSvc;
}).factory('$landApprovalValid', function () {
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