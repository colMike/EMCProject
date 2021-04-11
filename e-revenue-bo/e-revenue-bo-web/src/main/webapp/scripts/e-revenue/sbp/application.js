/**
 *SBP-Application Angular Module
 */
'use strict';
angular.module('app.application', []).controller("applicationCtrl", ["$scope", "$filter", "applicationSvc","marketSvc","wardSvc","$applicationValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter,applicationSvc,marketSvc,wardSvc,$applicationValid, $rootScope, blockUI, logger, $location) {

	var init;
	$scope.priceDetailMode=true;
	$scope.appDetailMode=true;
	$scope.showWards=true;
	$scope.showMarkets=true;
	$scope.feeValue="";
	$scope.apps = [];
	var count=0;

	/**
	 * gets the list of all the applications 
	 */
	$scope.loadApplicationData = function () 
                 {
		$scope.apps = [], $scope.searchKeywords = "", $scope.filteredApps = [], $scope.row = "", $scope.appsEditMode = false;
                
		applicationSvc.GetApps().success(function (response)
                                    { console.log(response,"...................") 
			return $scope.apps = response, $scope.searchKeywords = "", $scope.filteredApplications = [], $scope.row = "", $scope.select = function (page) 
                                                        {
				var end, start;
				return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageApplications = $scope.filteredApplications.slice(start, end)
			}, $scope.onFilterChange = function () 
                                                      {
				return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
			}, $scope.onNumPerPageChange = function () 
                                                      {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.onOrderChange = function () 
                                                      {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.search = function () 
                                                      {
				return $scope.filteredApplications = $filter("filter")($scope.apps, $scope.searchKeywords), $scope.onFilterChange()
			}, $scope.order = function (rowName) 
                                                      {
				return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredApplications = $filter("orderBy")($scope.apps, rowName), $scope.onOrderChange()) : void 0
			}, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPages = [], (init = function () 
                                                      {
				return $scope.search(), $scope.select($scope.currentPage)
			})();
		}).error(function (data, status, headers, config) 
                                    {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}

	/**
	 * gets the list of applications for signup users
	 * @param $rootScope.UsrRghts.linkId-signup user type,if value=3 then agent user,if value=4 then individual user
	 * @param $rootScope.UsrRghts.linkExtInfo-fetches the national id from session for the signed up user
	 * @param $rootScope.UsrRghts.sessionId-userId of logged in user
	 */
/*	$scope.loadAppsByLinkId = function () {
		$scope.apps = [], $scope.searchKeywords = "", $scope.filteredApps = [], $scope.row = "", $scope.appsEditMode = false;
		applicationSvc.GetAppsByLinkId($rootScope.UsrRghts.linkId,$rootScope.UsrRghts.linkExtInfo,$rootScope.UsrRghts.sessionId).success(function (response) {
			return $scope.apps = response, $scope.searchKeywords = "", $scope.filteredApplications = [], $scope.row = "", $scope.select = function (page) {
				var end, start;
				return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageApplications = $scope.filteredApplications.slice(start, end)
			}, $scope.onFilterChange = function () {
				return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
			}, $scope.onNumPerPageChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.onOrderChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.search = function () {
				return $scope.filteredApplications = $filter("filter")($scope.apps, $scope.searchKeywords), $scope.onFilterChange()
			}, $scope.order = function (rowName) {
				return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredApplications = $filter("orderBy")($scope.apps, rowName), $scope.onOrderChange()) : void 0
			}, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPages = [], (init = function () {
				return $scope.search(), $scope.select($scope.currentPage)
			})();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}*/

	/**
	 * display the list of application based on user type
	 */
/*	if($rootScope.UsrRghts.linkId==3 || $rootScope.UsrRghts.linkId==4){
		$scope.loadAppsByLinkId()
	}else{
		$scope.loadApplicationData();
	}*/
	$scope.loadApplicationData();
	/**
	 * gets the active ward data
	 */
	$scope.loadActiveWardData = function (subCountyId) {
		$scope.wards = [];
		applicationSvc.GetWardsBySubCounty(subCountyId).success(function (response) {
			$scope.wards = response;
		})
	};
	
	/**
	 * gets the list of active subcounties
	 */
	$scope.loadActiveSubCounties = function () {
		$scope.subCounties = [];
		wardSvc.GetActiveSubCounties().success(function (response) {
			$scope.subCounties = response;
		})
	};
	$scope.loadActiveSubCounties();
        
                  
	/**
	 * gets the active Market data
	 */
	$scope.loadActiveMarketData = function (wardId) {
		$scope.markets = [];
		applicationSvc.GetMarketsByWard(wardId).success(function (response) {
			$scope.markets = response;
			console.log(response,"========================");
		});
	};
	
	/**
	 * gets the list of active permitTypes
	 */
	$scope.loadActivePermitTypes= function () 
                 {
		$scope.permitTypes = [];
		applicationSvc.GetActivePermitTypes("S").success(function (response) 
                                    {
			$scope.permitTypes = response;
		});
	};
	$scope.loadActivePermitTypes();
	
	/**
	 * gets the list of wards based on subcounty selected
	 */
	$scope.$watch("subCountySelect", function (newValue, oldValue) {
		if ($applicationValid(newValue)) {
			if (newValue != oldValue) {
					$scope.showWards=false;
					$scope.loadActiveWardData(newValue);
					//$scope.$apply();
			}
		}

	});
	/**
	 * gets the list of markets based on ward selected
	 */
	$scope.$watch("wardSelect", function (newValue, oldValue) {
		if ($applicationValid(newValue)) {
			if (newValue != oldValue) {
					$scope.showMarkets=false;
					$scope.loadActiveMarketData(newValue);
					//$scope.$apply();
			}
		}

	});
	/**
	 * initialize the form for creating the new application
	 */
	$scope.addApp=function(){
		$scope.appEditMode = true;
		$scope.appDetailMode=false;
		$scope.applicant="";
		$scope.businessName="";
		$scope.noOfEmployees="";
		$scope.permitTypeSelect="";
		$scope.waterAccNo="";
		$scope.conservancy_fee="";
		$scope.appFee="";
		$scope.electricityAccNo="";
		$scope.area="";
		$scope.regNo="";
		$scope.businessDesc="";
		$scope.postalAdd="";
		$scope.postalCode="";
		$scope.email="";
		$scope.fax="";		
		$scope.mobileNo="";
		$scope.landLineNo="";
		$scope.subCountySelect="";
		$scope.wardSelect="";
		$scope.marketSelect="";
		$scope.landZone="";
	       $scope.road="";
           $scope.pinNumber="";
           $scope.vatNumber="";
           $scope.detailDesc="";
           $scope.town="";
           $scope.building="";
           $scope.floor="";
           $scope.room="";
		$scope.plotNo="";
		$scope.active=false;
		$scope.agree=false;
		$scope.businessId="";
		$scope.nationalId="";
		$scope.name="";
	}

	/**
	 * gets the details of selected application
	 * @param app-object of application
	 */
    $scope.editApp = function (app)
    {
    	if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" +
    			$location.path())) {
    		logger.log("Oh snap! You are not allowed to modify the application.");
    		return false;
    	}
    	if($rootScope.UsrRghts.sessionId!==app.createdBy){
        		logger.log("Oh snap! You are not allowed to modify the application.");
        		return false;
    	}
    	if(app.status==="Amended"){
    		logger.log("Oh snap! Application is already amended kindly contact administrator.");
    		return false;
    	}
                  if(app.status==="Refer to User" || app.status==="Pending" )
                  {
                        $scope.appStatus=app.status;
                        $scope.appEditMode = true;
                        $scope.appDetailMode=false;
                        $scope.businessId=app.businessId;
                        $scope.businessName=app.businessName;
                        $scope.noOfEmployees=app.noOfEmployees;
                        $scope.permitTypeSelect=app.permitTypeId;
                        $scope.waterAccNo=app.waterAccNo;
                        $scope.electricityAccNo=app.electricityAccNo;
                        $scope.regNo=app.regNo;
                        $scope.businessDesc=app.businessDesc;
                        $scope.postalAdd=app.postalAdd;
                        $scope.postalCode=app.postalCode;
                        $scope.appFee=app.appFee;
                        $scope.conservancy_fee=app.conservancy_fee
                        console.log($scope.appFee)
                        $scope.road=app.road;
                        $scope.pinNumber=app.pinNumber;
                        $scope.vatNumber=app.vatNumber;
                        $scope.detailDesc=app.detailDesc;
                        $scope.town=app.town;
                        $scope.building=app.building;
                        $scope.floor=app.floor;
                        $scope.room=app.room;
                        $scope.email=app.email;
                        $scope.fax=app.fax;
                        $scope.applicant=app.applicant;
                        $scope.mobileNo=app.mobileNo;
                        $scope.landLineNo=app.landLineNo;
                        $scope.subCountySelect=app.area;
                        $scope.wardSelect=app.wardId;
                        $scope.landZone=app.landZone;
                        $scope.plotNo=app.plotNo;
                        $scope.permitFee=app.permitFee;
                        $scope.agree=true;
                        $scope.active=false;
                        $scope.nationalId=app.nationalId;
                        $scope.name=app.name;
                        $scope.marketSelect=app.marketId;
                  }
                  else
                  {
                    logger.logWarning("Cannot be modified; workflow completed. Please contact administrator");
                    return false;
                  }
        };
	
	/**
	 * takes to the next step in form creation
	 */
	$scope.next=function(){
		if (!$applicationValid($scope.businessName)) {
			logger.logWarning("Opss! You may have skipped specifying the business's name. Please try again.");
			return false;
		}
		if (!$applicationValid($scope.noOfEmployees)) {
			logger.logWarning("Opss! You may have skipped specifying the no of employees. Please try again.");
			return false;
		}
		if (!$applicationValid($scope.permitTypeSelect)) {
			logger.logWarning("Opss! You may have skipped specifying the permit's type. Please try again.");
			return false;
		}
		if (!$applicationValid($scope.applicant)) { /*Originally name*/
			logger.logWarning("Opss! You may have skipped specifying the applicant's name. Please try again.");
			return false;
		}
		if (!$applicationValid($scope.nationalId)) {
			logger.logWarning("Opss! You may have skipped specifying the national id. Please try again.");
			return false;
		}
		
		if (!$applicationValid($scope.subCountySelect)) {
			logger.logWarning("Opss! You may have skipped specifying the sub-county. Please try again.");
			return false;
		}
		if (!$applicationValid($scope.wardSelect)) {
			logger.logWarning("Opss! You may have skipped specifying the ward. Please try again.");
			return false;
		}
		if (!$applicationValid($scope.marketSelect)) {
			logger.logWarning("Opss! You may have skipped specifying the market. Please try again.");
			return false;
		}
		if (!$applicationValid($scope.regNo)) {
			logger.logWarning("Opss! You may have skipped specifying the registration no. Please try again.");
			return false;
		}

		if (!$applicationValid($scope.businessDesc)) {
			logger.logWarning("Opss! You may have skipped specifying the business description. Please try again.");
			return false;
		}
		if (!$applicationValid($scope.postalAdd)) {
			logger.logWarning("Opss! You may have skipped specifying the postal address. Please try again.");
			return false;
		}
		if (!$applicationValid($scope.postalCode)) {
			logger.logWarning("Opss! You may have skipped specifying the postal code. Please try again.");
			return false;
		}

		if (!$applicationValid($scope.email)) {
			logger.logWarning("Opss! You may have skipped specifying the email or Invalid email code. Please try again.");
			return false;

		}
		/*if (!$applicationValid($scope.fax)) {
			logger.logWarning("Opss! You may have skipped specifying the fax. Please try again.")
			return false;

		}*/
		if (!$applicationValid($scope.mobileNo)) {
			logger.logWarning("Opss! You may have skipped specifying the mobile no. Please try again.");
			return false;
		}
		/*if (!$applicationValid($scope.landLineNo)) {
			logger.logWarning("Opss! You may have skipped specifying the land line no. Please try again.")
			return false;
		}*/
		
		if (!$applicationValid($scope.landZone)) {
			logger.logWarning("Opss! You may have skipped specifying the land zone. Please try again.");
			return false;
		}
		if (!$applicationValid($scope.plotNo)) {
			logger.logWarning("Opss! You may have skipped specifying the plot no. Please try again.");
			return false;
		}
                
                
                                /*Get item price*/
                                  $scope.loadPrice  =   function()
                                  {
                                            $scope.itemPrice  =   0;
                                            applicationSvc.GetItemPrice($scope.permitTypeSelect).success(function (response)
                                            {
                                                $scope.itemPrice = response;
                                                $scope.permitFee    =   response;
                                            });
                                  };
                                  
                                  $scope.loadPrice();
                                  
                
		$scope.priceDetailMode=false;
		$scope.appEditMode=true;
		$scope.appDetailMode=true;
//		for(var i=0;i<$scope.permitTypes.length;i++){
//			if($scope.permitTypeSelect==$scope.permitTypes[i].permitTypeId)
//                                                      {
//				$scope.feeValue=$scope.permitTypes[i].permitTypeName.split(".")[1]+".00";
//				$scope.permitFee=$scope.itemPrice;//$scope.permitTypes[i].permitTypeName.split(".")[1]+".00";
//			}
//		}
	}
	
	/**
	 * takes to the previous step in application creation
	 */
	$scope.back=function(){
		$scope.priceDetailMode=true;
		$scope.appDetailMode=false;
	}
	
	/**
	 * cancels the operation of creating application
	 */
	$scope.cancelNewApp=function(){
		$scope.priceDetailMode=true;
		$scope.appEditMode=false;
		$scope.appDetailMode=true;
	}
	$scope.loadActiveWardData($scope.subCountySelect);
	$scope.loadActiveMarketData($scope.wardSelect)
	/**
	 * updates the new application details
	 */
	$scope.updNewApp = function () {
		var app = {};


		if ($scope.agree==false) {
			logger.logWarning("Please agree temrms and conditions before continue. Please try again.")
			return false;
		}

		if (!$applicationValid($scope.businessId))
			app.businessId = 0;
		else
		app.businessId = $scope.businessId;
		app.businessName = $scope.businessName;
		app.noOfEmployees = $scope.noOfEmployees;
		app.permitTypeId=$scope.permitTypeSelect;
		app.electricityAccNo=$scope.electricityAccNo;
		app.waterAccNo=  $scope.waterAccNo;
	
		app.area=$scope.subCountySelect;
		app.regNo=$scope.regNo;
		app.businessDesc=$scope.businessDesc;
		app.postalAdd=$scope.postalAdd;
		app.postalCode=$scope.postalCode;
		app.email=$scope.email;
		app.fax=$scope.fax;
		app.appFee=$scope.appFee;
		app.conservancy_fee=$scope.conservancy_fee;
		console.log(app.appFee, "=====================")
		  app.road=$scope.road;
		  app.pinNumber=$scope.pinNumber;
		  app.vatNumber=$scope.vatNumber;
		  app.detailDesc=$scope.detailDesc;
		  app.town=$scope.town;
		  app.building=$scope.building;
		  app.floor=$scope.floor;
		  app.room=$scope.room;
		app.applicant=$scope.applicant;
		app.mobileNo=$scope.mobileNo;
		app.landLineNo= $scope.landLineNo;
		app.wardId=$scope.wardSelect;
		app.landZone=$scope.landZone;
		app.plotNo= $scope.plotNo;
		app.active = $scope.active;
		app.createdBy = $rootScope.UsrRghts.sessionId;
		app.appNo="VHI1500"+count;
		app.permitFee=$scope.permitFee;
		app.nationalId=$scope.nationalId;
		app.name=$scope.name;
		app.marketId=$scope.marketSelect;
		app.linkId=$rootScope.UsrRghts.linkId;
		if($scope.appStatus==="Refer to User"){
			app.status='AM';	
		}else{
			app.status='N';
		}	
		blockUI.start();
		applicationSvc.UpdApp(app).success(function (response) {
			if (response.respCode === 200) {
				logger.logSuccess("Great! Application information was saved succesfully");
				$scope.subCountyId = 0;
				$scope.subCountyCode = "";
				$scope.subCountyName = "";
				$scope.active = false;
				$scope.priceDetailMode=true;
				$scope.appEditMode=false;
				$scope.appDetailMode=true;
				$scope.isDisabled = false;
			
					$scope.loadApplicationData();
				
			}
			else  {
				logger.logWarning(response.respMessage);
			}

			blockUI.stop();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.");
			blockUI.stop();
		});
	};

} ]).factory('applicationSvc', function ($http) {
	var ervNewAppSvc = {};
        
                ervNewAppSvc.GetItemPrice   =   function(businessID){
                      return $http({
                         url: '/erevenue/rest/application/getPrices/'+businessID,
                         method: 'GET',
                         headers: { 'Content-Type': 'application/json' }
                         
                      });
                  };
	ervNewAppSvc.GetApps = function () {
		return $http({
			url: '/erevenue/rest/application/gtApps',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	ervNewAppSvc.GetWardsBySubCounty = function (subCountyId) {
		return $http({
			url: '/erevenue/rest/application/gtWardsBySubCounty/'+subCountyId,
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};//
	ervNewAppSvc.GetMarketsByWard = function (wardId) {
		return $http({
			url: '/erevenue/rest/application/gtMarketsByWard/'+wardId,
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};//
	ervNewAppSvc.GetAppsByLinkId = function (linkId,nationalId,agentId) {
		return $http({
			url: '/erevenue/rest/application/gtAppsByLinkId/'+linkId+','+nationalId+','+agentId,
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};

	ervNewAppSvc.GetActivePermitTypes = function (permitType) {
		return $http({
			url: '/erevenue/rest/application/gtActivePermitTypes/'+permitType,
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};

	ervNewAppSvc.UpdApp = function (app) {
		return $http({
			url: '/erevenue/rest/application/updApp',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data:app
		});
	};
	return ervNewAppSvc;
}).factory('$applicationValid', function () {
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