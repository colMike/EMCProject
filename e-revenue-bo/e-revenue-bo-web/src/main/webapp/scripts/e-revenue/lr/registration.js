/**
 * 
 */
/**
 * User Angular Module
 */
'use strict';
angular.module('app.registration', []).controller("registrationCtrl", ["$scope", "$filter", "registrationSvc","subCountySvc","marketSvc","wardSvc","applicationSvc","$registrationValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter,registrationSvc,subCountySvc,marketSvc,wardSvc,applicationSvc,$registrationValid, $rootScope, blockUI, logger, $location) {

	var init;
	$scope.registers = [];
	var count=0;

	//for displaying all registration details in registration form

	$scope.loadRegistrationData = function () {
		$scope.registers = [], $scope.searchKeywords = "", $scope.filteredRegisters = [], $scope.row = "", $scope.registersEditMode = false;
		registrationSvc.GetRegForms().success(function (response) {
			return $scope.registers = response, $scope.searchKeywords = "", $scope.filteredRegisters = [], $scope.row = "", $scope.select = function (page) {
				var end, start;
				return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageRegisters= $scope.filteredRegisters.slice(start, end)
			}, $scope.onFilterChange = function () {
				return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
			}, $scope.onNumPerPageChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.onOrderChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.search = function () {
				return $scope.filteredRegisters = $filter("filter")($scope.registers, $scope.searchKeywords), $scope.onFilterChange()
			}, $scope.order = function (rowName) {
				return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredRegisters = $filter("orderBy")($scope.registers, rowName), $scope.onOrderChange()) : void 0
			}, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPages = [], (init = function () {
				return $scope.search(), $scope.select($scope.currentPage)
			})();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}

	//$scope.loadRegistrationData();

	//get all land rate application list for individual signup and agent signup

	$scope.loadLandsByLinkId = function () {
		$scope.registers = [], $scope.searchKeywords = "", $scope.filteredRegisters = [], $scope.row = "", $scope.registersEditMode = false;
		registrationSvc.GetLandsByLinkId($rootScope.UsrRghts.linkId,$rootScope.UsrRghts.linkExtInfo,$rootScope.UsrRghts.sessionId).success(function (response) {
			return $scope.registers = response, $scope.searchKeywords = "", $scope.filteredRegisters = [], $scope.row = "", $scope.select = function (page) {
				var end, start;
				return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageRegisters= $scope.filteredRegisters.slice(start, end)
			}, $scope.onFilterChange = function () {
				return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
			}, $scope.onNumPerPageChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.onOrderChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.search = function () {
				return $scope.filteredRegisters = $filter("filter")($scope.registers, $scope.searchKeywords), $scope.onFilterChange()
			}, $scope.order = function (rowName) {
				return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredRegisters = $filter("orderBy")($scope.registers, rowName), $scope.onOrderChange()) : void 0
			}, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPages = [], (init = function () {
				return $scope.search(), $scope.select($scope.currentPage)
			})();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}


	/* condition for checking individual signup or agent signup based on link id
	 if yes display all registration details by using link id otherwise display
	  all registration details*/

	if($rootScope.UsrRghts.linkId==3 || $rootScope.UsrRghts.linkId==4){
		$scope.loadLandsByLinkId()
	}else{
		$scope.loadRegistrationData();
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
	$scope.loadActiveSubCounties();

	/* watch function for sub county select
	 based on sub county selection it will display wards*/

	$scope.$watch("subCountySelect", function (newValue, oldValue) {
		if ($registrationValid(newValue)) {
			if (newValue != oldValue) {
				$scope.showWards=false;
				$scope.loadActiveWardData(newValue);
			}
		}

	});
	$scope.loadActivePermitTypes= function () {
		$scope.permitTypes = [];
		applicationSvc.GetActivePermitTypes("L").success(function (response) {
			$scope.permitTypes = response;
		})
	};
	$scope.loadActivePermitTypes();

	//for creating new registration form

	$scope.addRegistration=function(){
		$scope.regEditMode = true;
		$scope.isExisting = false;
		$scope.plotNumber="";
		$scope.sublocation="";
		$scope.code="";
		$scope.phone="";
		$scope.address="";
		$scope.mapSheetNumber="";
		$scope.location="";
		$scope.acreage="";
		$scope.titleDeedNumber="";
		$scope.name="";
		$scope.krapin="";
		$scope.nationalIdNumber="";
		$scope.wardSelect="";
		$scope.subCountySelect="";
		$scope.id="";
		$scope.isDisabled = true;
		$scope.permitTypeSelect="";
	}

	//for editing registration form

	$scope.editRegistration = function (reg) {
		
		if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" +
				$location.path())) {
			logger.log("Oh snap! You are not allowed to modify the application.");
			return false;
		}
		if($rootScope.UsrRghts.sessionId!=reg.createdBy){
			logger.log("Oh snap! You are not allowed to modify the land application.");
			return false;
		}
		/*if(reg.status=="Amended" && reg.balance==0){
			logger.log("Oh snap! Application is already amended kindly contact administrator.");
			return false;
		}*/

		if(reg.status==="Refer to User" || reg.status==="Pending" || reg.status==="Amended"){
			$scope.regStatus=reg.status;
			$scope.regEditMode = true;
			$scope.isExisting = true;
			$scope.id=reg.id;
			$scope.plotNumber=reg.plotNumber;
			$scope.mapSheetNumber=reg.mapSheetNumber;
			$scope.location=reg.location;

			$scope.acreage=reg.acreage;
			$scope.titleDeedNumber=reg.titleDeedNumber;
			$scope.name=reg.name;
			$scope.krapin=reg.krapin;
			$scope.nationalIdNumber=reg.nationalIdNumber;
			$scope.phone=reg.phone;
			$scope.address=reg.address
			$scope.sublocation=reg.sublocation;
			$scope.wardSelect=reg.wardId;
			$scope.subCountySelect=reg.subCountyId;
			$scope.code=reg.code;
			//$scope.createdBy=reg.createdBy;
			$scope.fee=reg.fee;
			$scope.permitTypeSelect=reg.permitTypeId;
			//$scope.balance=reg.balance;
			//$scope.amount=reg.amount;
			//$scope.appliedFor=reg.appliedFor;
		}else{
			logger.logWarning("Cannot modify, workflow process started.")
			return false;
		}
	};
	$scope.loadActiveWardData($scope.subCountySelect);
	// for cancel registration

	$scope.cancelNewReg=function(){

		$scope.regEditMode=false;
		$scope.isDisabled = false;
	}


	//for inserting and updating registration form

	$scope.updNewReg = function () {
		var reg = {};
		if (!$registrationValid($scope.plotNumber)) {
			logger.logWarning("Opss! You may have skipped specifying the plot number. Please try again.")
			return false;
		}
		if (!$registrationValid($scope.mapSheetNumber)) {
			logger.logWarning("Opss! You may have skipped specifying the map sheet number. Please try again.")
			return false;
		}
		if (!$registrationValid($scope.location)) {
			logger.logWarning("Opss! You may have skipped specifying the location. Please try again.")
			return false;
		}
		/*if (!$registrationValid($scope.acreage)) {
			logger.logWarning("Opss! You may have skipped specifying the acreage. Please try again.")
			return false;
		}*/
		if (!$registrationValid($scope.titleDeedNumber)) {
			logger.logWarning("Opss! You may have skipped specifying the title deed number. Please try again.")
			return false;
		}
		if (!$registrationValid($scope.permitTypeSelect)) {
			logger.logWarning("Opss! You may have skipped specifying the permit's type. Please try again.")
			return false;
		}
		if (!$registrationValid($scope.name)) {
			logger.logWarning("Opss! You may have skipped specifying the name. Please try again.")
			return false;
		}
		if (!$registrationValid($scope.krapin)) {
			logger.logWarning("Opss! You may have skipped specifying the kra pin. Please try again.")
			return false;
		}

		if (!$registrationValid($scope.nationalIdNumber)) {
			logger.logWarning("Opss! You may have skipped specifying the national id number. Please try again.")
			return false;
		}

		if (!$registrationValid($scope.subCountySelect)) {
			logger.logWarning("Opss! You may have skipped specifying the subCounty. Please try again.")
			return false;
		}
		if (!$registrationValid($scope.wardSelect)) {
			logger.logWarning("Opss! You may have skipped specifying the ward. Please try again.")
			return false;
		}
                
                                    //reg.status='N';	
                                   
//		for(var i=0;i<$scope.permitTypes.length;i++){
//			if($scope.permitTypeSelect==$scope.permitTypes[i].permitTypeId){
//				$scope.feeValue=$scope.permitTypes[i].permitTypeName.split(".")[1]+".00";
//				$scope.fee=$scope.permitTypes[i].permitTypeName.split(".")[1]+".00";
//			}
//		}
                                    
                                    
		if(($scope.amount)>($scope.feeValue * $scope.acreage)){
			logger.logWarning("Opss! Amount Entered is greater then land rates. Please try again.")
			return false;
		}
		if(($scope.amount)>($scope.balance)){
			logger.logWarning("Opss! Amount Entered is greater then remaining balance. Please try again.")
			return false;
		}

		if (!$registrationValid($scope.id)){
			reg.id = 0;}
		else{
			reg.id =$scope.id;
		}
		reg.plotNumber=$scope.plotNumber;

		reg.mapSheetNumber=$scope.mapSheetNumber;
		reg.location=$scope.location;
		if($scope.permitTypeSelect==14){
			reg.acreage=1;
		}else{
			reg.acreage=$scope.acreage;
		}
		
		reg.titleDeedNumber=$scope.titleDeedNumber;
		reg.name=$scope.name;
		reg.phone=$scope.phone;
		reg.address=$scope.address;
		reg.sublocation=$scope.sublocation;
		reg.krapin=$scope.krapin;
		reg.nationalIdNumber=$scope.nationalIdNumber;
		reg.subCountyId=$scope.subCountySelect;
		reg.wardId=$scope.wardSelect;
		reg.createdBy = $rootScope.UsrRghts.sessionId;
		reg.permitTypeId=$scope.permitTypeSelect;
		reg.amount=$scope.amount;
		reg.code=$scope.code;
		//reg.appliedFor=$scope.appliedFor;
                                    //reg.fee =   $scope.fee;
                                    
		reg.linkId=$rootScope.UsrRghts.linkId;
		reg.balance=$scope.balance;
		if($scope.regStatus==="Refer to User" || $scope.regStatus==="Pending" || $scope.regStatus==="Amended"){
			reg.status='AM';	
		}else{
			reg.status='N';
		}	
		
		
		blockUI.start()
		registrationSvc.UpdReg(reg).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! Application information was saved succesfully")
				$scope.plotNumber="";
				$scope.mapSheetNumber="";
				$scope.location="";
				$scope.acreage="";
				$scope.titleDeedNumber="";
				$scope.name="";
				$scope.krapin="";
				$scope.sublocation="";
				$scope.phone="";
				$scope.address="";
				$scope.nationalIdNumber="";
				$scope.wardSelect="";
				$scope.subCountySelect="";
				$scope.id="";
				$scope.createdBy="";
				$scope.permitTypeSelect="";
                                                                        $scope.fee  =   0;
				$scope.regEditMode=false;
				$scope.isDisabled = false;
				//$scope.loadRegistrationData();
				if($rootScope.UsrRghts.linkId==3 || $rootScope.UsrRghts.linkId==4){
					$scope.loadLandsByLinkId()
				}else{
					$scope.loadRegistrationData();
				}
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

} ]).factory('registrationSvc', function ($http) {
	var ervRegstrSvc = {};
                  
	ervRegstrSvc.GetRegForms = function () {
		return $http({
			url: '/erevenue/rest/landrate/gtRegs',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};

	ervRegstrSvc.UpdReg = function (reg) {
		return $http({
			url: '/erevenue/rest/landrate/updRegistration',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data:reg
		});
	};
	ervRegstrSvc.GetLandsByLinkId = function (linkId,nationalId,agentId) {
		return $http({
			url: '/erevenue/rest/landrate/gtLandsByLinkId/'+linkId+','+nationalId+','+agentId,
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	return ervRegstrSvc;
}).factory('$registrationValid', function () {
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