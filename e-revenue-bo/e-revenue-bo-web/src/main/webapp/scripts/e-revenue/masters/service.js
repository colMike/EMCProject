/**
 * 
 */
/**
 * User Angular Module
 */
'use strict';
angular.module('app.service', []).controller("servicesCtrl", ["$scope", "$filter", "serviceSvc","marketSvc","submarketSvc","wardSvc","$serviceValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter,serviceSvc,marketSvc,submarketSvc,wardSvc, $serviceValid, $rootScope, blockUI, logger, $location) {

	var init;

	$scope.services = [];
	$scope.subServices1=[];
	$scope.subServices2=[];
	$scope.subServices3=[];
	$scope.subServices4=[];
	$scope.subServices5=[];
	$scope.subServices6=[];
	$scope.level1=true;
	$scope.level2=true;
	$scope.level3=true;
	$scope.level4=true;
	$scope.level5=true;
	$scope.level6=true;
	$scope.serviceEditMode = false;
	$scope.priceActive=false;
	$scope.prntServiceId=0;
	$scope.servicePriceMode=true;
	$scope.showSubCounty=true;
	$scope.showWards=true;
	$scope.showMarkets=true;
	$scope.isPriceAttached=false;
	
	$scope.loadServiceData = function () {
		$scope.services = [], $scope.searchKeywords = "", $scope.filteredServices = [], $scope.row = "", $scope.serviceEditMode = false;
		serviceSvc.GetServices($scope).success(function (response) {
			return $scope.services = response, $scope.searchKeywords = "", $scope.filteredServices = [], $scope.row = "", $scope.select = function (page) {
				var end, start;
				return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageServices = $scope.filteredServices.slice(start, end)
			}, $scope.onFilterChange = function () {
				return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
			}, $scope.onNumPerPageChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.onOrderChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.search = function () {
				return $scope.filteredServices = $filter("filter")($scope.services, $scope.searchKeywords), $scope.onFilterChange()
			}, $scope.order = function (rowName) {
				return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredServices = $filter("orderBy")($scope.services, rowName), $scope.onOrderChange()) : void 0
			}, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPages = [], (init = function () {
				return $scope.search(), $scope.select($scope.currentPage)
			})();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}

	$scope.loadServiceData();

	$scope.loadParameterData=function(){
		$scope.params=[];
		serviceSvc.GetParameter($scope).success(function (response) {
			return $scope.params=response;
		});
	}

	$scope.loadParameterData();

// $scope.getSubServices= function(serviceId){
// $scope.subServices=[];
// serviceSvc.GetSubServices(serviceId).success(function (response) {
// return $scope.subServices=response;
// });
// }


	$scope.editService = function (service) {
// if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" +
// $location.path())) {
// logger.log("Oh snap! You are not allowed to modify the project.");
// return false;
// }
		$scope.serviceEditMode = true;
		$scope.isExisting = true;
		$scope.serviceId = service.serviceId;
		$scope.serviceCode = service.serviceCode;
		$scope.serviceName = service.serviceName;
		$scope.active = service.active;

	};

	$scope.addService = function () {

// if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" +
// $location.path())) {
// logger.log("Oh snap! You are not allowed to create projects.");
// return false;
// }
		$scope.level1=true;
		$scope.level2=true;
		$scope.level3=true;
		$scope.level4=true;
		$scope.level5=true;
		$scope.level6=true;
		$scope.subServices1=[];
		$scope.subServices2=[];
		$scope.subServices3=[];
		$scope.subServices4=[];
		$scope.subServices5=[];
		$scope.subServices6=[];
		$scope.priceActive=false;
		$scope.serviceEditMode = true;
		$scope.isExisting = false;
		$scope.serviceId = 0;
		$scope.serviceCode = "";
		$scope.serviceName = "";
		$scope.active = false;
		// $scope.isDisabled = true;
		$scope.serviceSelect="";
	
	};

	$scope.cancelService = function () {
		$scope.serviceEditMode = false;
		$scope.active = false;
		$scope.serviceSelect=0;
		$scope.l1Disable=false;
		$scope.l2Disable=false;
		$scope.l3Disable=false;
		$scope.l4Disable=false;
		$scope.l5Disable=false;
		$scope.l6Disable=false;
		$location.path('/dashboard');

	}
	$scope.loadSubService1=function(parentId){
		$scope.subServices1=[];
		serviceSvc.GetSubServiceById(parentId).success(function (response) {
			$scope.subServices1=response;
			if( $scope.subServices1.length>0) {
				$scope.level1=false;
				for(var i=0;i<$scope.subServices1.length;i++){
					
					if(($scope.subServices1[i].level==-1)){
						if($scope.subServices1[i].serviceName=="Parent"){
							$scope.level1=true;
							$scope.priceActive=true;
							$scope.params=$scope.subServices1[i].params;
							$scope.serviceValue=$scope.subServices1[i].serviceValue;
							$scope.prntServiceId=$scope.subServices1[i].serviceId;
							$scope.isExisting=true;
						}
						else{
							$scope.loadParameterData();
							$scope.priceActive=false;
							
						}
						
						if($scope.subServices1[i].active==true){
							$scope.active=true;
						}
						// $scope.level1=true;
						
					}
						
				}
// if($scope.subServices1.params.length>0){
// $scope.params=$scope.subServices1.params;
// }
				
				
			}else{
				$scope.level1=true;
				// $scope.serviceId=parentId;
			}
			console.log( $scope.subServices);
		});
	}
	$scope.loadSubService2=function(parentId){
		$scope.subServices2=[];
		serviceSvc.GetSubServiceById(parentId).success(function (response) {
			$scope.subServices2=response;
			if( $scope.subServices2.length>0) {
				$scope.level2=false;
				
				
			}else{
				$scope.level2=true;
			}
		});
	}
	$scope.loadSubService3=function(parentId){
		$scope.subServices3=[];
		serviceSvc.GetSubServiceById(parentId).success(function (response) {
			$scope.subServices3=response;
			if( $scope.subServices3.length>0) {
				$scope.level3=false;
				
				
			}else{
				$scope.level3=true;
			}
		});
	}
	$scope.loadSubService4=function(parentId){
		$scope.subServices4=[];
		serviceSvc.GetSubServiceById(parentId).success(function (response) {
			$scope.subServices4=response;
			if( $scope.subServices4.length>0) {
				$scope.level4=false;
				
			}else{
				$scope.level4=true;
			}
		});
	}
	$scope.loadSubService5=function(parentId){
		$scope.subServices5=[];
		serviceSvc.GetSubServiceById(parentId).success(function (response) {
			$scope.subServices5=response;
			if( $scope.subServices5.length>0) {
				$scope.level5=false;
			}else{
				$scope.level5=true;
			}
			console.log( $scope.subServices);
		});
	}
	$scope.loadSubService6=function(parentId){
		$scope.subServices6=[];
		serviceSvc.GetSubServiceById(parentId).success(function (response) {
			$scope.subServices6=response;
			if( $scope.subServices6.length>0) {
				$scope.level6=false;
			}else{
				$scope.level6=true;
			}

		});
	}
	$scope.$watch("serviceSelect", function (newValue, oldValue) {
		if ($serviceValid(newValue)) {
			if (newValue != oldValue) {

				$scope.loadSubService1(newValue);



			}
		}

// / $scope.users = [];
	});
	$scope.$watch("level1Select", function (newValue, oldValue) {
		if ($serviceValid(newValue)) {
			if (newValue != oldValue) {
				// $scope.addChild(serviceSelect);
				$scope.loadSubService2(newValue);
				$scope.level2=false;
				for(var i=0;i<$scope.subServices1.length;i++){
					if(newValue==$scope.subServices1[i].serviceId){
						if($scope.subServices1[i].level==-1){
							// $scope.l1Disable=true;
							$scope.priceActive=true;
							$scope.isPriceAttached=true;
							$scope.params=$scope.subServices1[i].params;
							$scope.serviceValue=$scope.subServices1[i].serviceValue;
							$scope.isExisting=true;
						}else{
							$scope.isExisting=false;
						}
					}
					
					if($scope.subServices1[i].active==true){
						$scope.active=true;
					}
				}
			}
		}
	});
	$scope.$watch("level2Select", function (newValue, oldValue) {
		if ($serviceValid(newValue)) {
			if (newValue != oldValue) {
				// $scope.addChild(serviceSelect);
				$scope.loadSubService3(newValue);
				$scope.level3=false;
				for(var i=0;i<$scope.subServices2.length;i++){
					if(newValue==$scope.subServices2[i].serviceId){
						if($scope.subServices2[i].level==-1){
							// $scope.l2Disable=true;
							$scope.priceActive=true;
							$scope.isPriceAttached=true;
							$scope.params=$scope.subServices2[i].params;
							$scope.serviceValue=$scope.subServices2[i].serviceValue;
							$scope.isExisting=true;
						}else{
							$scope.isExisting=false;
						}
					}
					if($scope.subServices2[i].active==true){
						$scope.active=true;
					}
				}
			}
		}
	});
	$scope.$watch("level3Select", function (newValue, oldValue) {
		if ($serviceValid(newValue)) {
			if (newValue != oldValue) {
				// $scope.addChild(serviceSelect);
				$scope.loadSubService4(newValue);
				$scope.level4=false;
				for(var i=0;i<$scope.subServices3.length;i++){
					if(newValue==$scope.subServices3[i].serviceId){
					if($scope.subServices3[i].level==-1){
						// $scope.l3Disable=true;
						$scope.priceActive=true;
						$scope.isPriceAttached=true;
						$scope.params=$scope.subServices3[i].params;
						$scope.serviceValue=$scope.subServices3[i].serviceValue;
						$scope.isExisting=true;
					}else{
						$scope.isExisting=false;
					}
					}
					if($scope.subServices3[i].active==true){
						$scope.active=true;
					}
				}
			}
		}
	});
	$scope.$watch("level4Select", function (newValue, oldValue) {
		if ($serviceValid(newValue)) {
			if (newValue != oldValue) {
				// $scope.addChild(serviceSelect);
				$scope.loadSubService5(newValue);
				$scope.level5=false;
				for(var i=0;i<$scope.subServices4.length;i++){
					if(newValue==$scope.subServices4[i].serviceId){
					if($scope.subServices4[i].level==-1){
						// $scope.l4Disable=true;
						$scope.priceActive=true;
						$scope.isPriceAttached=true;
						$scope.params=$scope.subServices4[i].params;
						$scope.serviceValue=$scope.subServices4[i].serviceValue;
						$scope.isExisting=true;
						
					}else{
						$scope.isExisting=false;
					}
					}
					if($scope.subServices4[i].active==true){
						$scope.active=true;
					}
				}
			}
		}
	});
	$scope.$watch("level5Select", function (newValue, oldValue) {
		if ($serviceValid(newValue)) {
			if (newValue != oldValue) {
				// $scope.addChild(serviceSelect);
				$scope.loadSubService6(newValue);
				$scope.level6=false;
				for(var i=0;i<$scope.subServices5.length;i++){
					if(newValue==$scope.subServices5[i].serviceId){
						if($scope.subServices5[i].level==-1){
							$scope.l5Disable=true;
							$scope.priceActive=true;
							$scope.isPriceAttached=true;
							$scope.params=$scope.subServices5[i].params;
							$scope.serviceValue=$scope.subServices5[i].serviceValue;
							$scope.isExisting=true;
						}else{
							$scope.isExisting=false;
						}
					}
					if($scope.subServices5[i].active==true){
						$scope.active=true;
					}
				}
			}
		}
	});
	
	$scope.$watch("level6Select", function (newValue, oldValue) {
		if ($serviceValid(newValue)) {
			if (newValue != oldValue) {
				// $scope.addChild(serviceSelect);
				$scope.loadSubService6(newValue);
				$scope.level6=false;
				for(var i=0;i<$scope.subServices6.length;i++){
					if(newValue==$scope.subServices6[i].serviceId){
						if($scope.subServices6[i].level==-1){
							$scope.l6Disable=true;
							$scope.priceActive=true;
							$scope.isPriceAttached=true;
							$scope.params=$scope.subServices6[i].params;
							$scope.serviceValue=$scope.subServices6[i].serviceValue;
							$scope.isExisting=true;
						}else{
							$scope.isExisting=false;
						}
					}
					if($scope.subServices6[i].active==true){
						$scope.active=true;
					}
				}
			}
		}
	});

	$scope.updService = function () {
		var service = {};
		service.parentType=true;
		if($scope.priceActive==false){
		if (!$serviceValid($scope.serviceCode)) {
			logger.logWarning("Opss! You may have skipped specifying the Service's Code. Please try again.")
			return false;
		}
		if (!$serviceValid($scope.serviceName)) {
			logger.logWarning("Opss! You may have skipped specifying the Service's Name. Please try again.")
			return false;
		}
		}
		
		if($scope.serviceSelect>0)
		{
			if($scope.prntServiceId!=0){
				service.parentServiceId=$scope.prntServiceId
			}
			else{
			service.parentServiceId=$scope.serviceSelect;
			service.parentType=false;
			}
			// service.parentServiceId=$scope.serviceSelect;
			if($scope.level1Select>0)
			{
				service.parentServiceId=$scope.level1Select
				service.parentType=true;
			}if ($scope.level2Select>0){
				service.parentServiceId=$scope.level2Select
				service.parentType=true;
			}if($scope.level3Select>0){
				service.parentServiceId=$scope.level3Select
				service.parentType=true;
			}if($scope.level4Select>0){
				service.parentServiceId=$scope.level4Select
				service.parentType=true;
			}if($scope.level5Select>0){
				service.parentServiceId=$scope.level5Select
				service.parentType=true;
			}if($scope.level6Select>0){
				service.parentServiceId=$scope.level6Select
				service.parentType=true;
			}

			service.serviceCode = $scope.serviceCode;
			service.serviceName=$scope.serviceName;
			service.active=$scope.active;
			
			if($scope.serviceValue>0 && $scope.priceActive==true){
				service.isActive=true;
				service.level=-1;
				service.hasChild=0;
				service.serviceValue=$scope.serviceValue
			}
			else if($scope.priceActive==true)
			{	service.isActive=true;
				service.level=-1;
				service.hasChild=0;
                service.serviceValue=-1;
			}
			else{
				service.level=1;
				service.hasChild=1;
				service.serviceValue=0;
			}
			service.params=$scope.params;	
			service.serviceId = $scope.serviceId;
			service.createdBy = $rootScope.UsrRghts.sessionId;
		}
		else{
			if($scope.priceActive==true){
				service.isActive=true;
				if($scope.serviceValue>0 && $scope.priceActive==true){
					service.level=-1;
					service.hasChild=0;
					service.serviceValue=$scope.serviceValue
					
				}
				else if($scope.priceActive==true)
				{
					service.level=-1;
					service.hasChild=0;
                    service.serviceValue=-1;
				}
				else{
					service.level=-1;
					service.hasChild=1;
					service.serviceValue=0;
				}
				service.params=$scope.params;
			}
			if (!$serviceValid($scope.serviceId))
				service.serviceId = 0;
			else
				service.serviceId = $scope.serviceId;
			service.serviceCode = $scope.serviceCode;
			service.serviceName=$scope.serviceName;
			service.active = $scope.active;
			service.createdBy = $rootScope.UsrRghts.sessionId;
		}
		if($scope.priceActive==true){
			if($scope.regionSelect==1){
				service.subCountyList=$scope.subCounties;
			}else if($scope.regionSelect==2){
				service.wardList=$scope.wards;
			}else if($scope.regionSelect==3){
				service.marketList=$scope.markets;
			}else{
				// service.priceList=[];
			}
		}
		blockUI.start()
		serviceSvc.UpdService(service).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The service information was saved succesfully")
				if (parseFloat($scope.userId) == parseFloat($rootScope.UsrRghts.sessionId)) {
					localStorageService.clearAll();
					loginSvc.GetRights($rootScope.UsrRghts.sessionId).success(function (rightLst)
							{
						localStorageService.set('rxr', rightLst);
						$rootScope.UsrRghts = rightLst;
							}).error(function (data, status, headers, config) {
								logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
							});
				}
				$scope.serviceId = 0;
				$scope.serviceCode = "";
				$scope.serviceName = "";
				$scope.active = false;
				$scope.serviceEditMode = false;
				$scope.servicePriceMode = true;
				$scope.serviceSelect=0;
				$scope.priceActive=false;
				$scope.loadServiceData();
				$scope.serviceValue="";
				$scope.isExisting=false;
				$scope.active=false;
				$scope.level1=true;
				$scope.level2=true;
				$scope.level3=true;
				$scope.level4=true;
				$scope.level5=true;
				$scope.level6=true;
				$scope.subServices1=[];
				$scope.subServices2=[];
				$scope.subServices3=[];
				$scope.subServices4=[];
				$scope.subServices5=[];
				$scope.subServices6=[];
				
				$scope.active = false;
				// $scope.isDisabled = true;
				$scope.serviceSelect="";
				$scope.prntServiceId=0;
				$scope.markets = [];
				$scope.wards = [];
				$scope.subCounties = [];
				$scope.showSubCounty=true;
				$scope.showWards=true;
				$scope.showMarkets=true;
				$scope.regionSelect="";
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

	$scope.next=function(){
	
		if($scope.isPriceAttached==true){
			$scope.priceActive=true;
		}else{
		if(!$serviceValid($scope.serviceSelect)){
			if (!$serviceValid($scope.serviceCode)) {
				logger.logWarning("Opss! You may have skipped specifying the Service's Code. Please try again.")
				return false;
			}
			if ($scope.serviceCode.length>4) {
				logger.logWarning("Opss! You may have the Service's Code  maximum length of 4. Please try again.")
				return false;
			}
			if (!$serviceValid($scope.serviceName)) {
				logger.logWarning("Opss! You may have skipped specifying the Service's Name. Please try again.")
				return false;
			}
		}else{
			if($scope.serviceCode!=""){
				if (!$serviceValid($scope.serviceCode)) {
					logger.logWarning("Opss! You may have skipped specifying the Service's Code. Please try again.")
					return false;
				}
				if ($scope.serviceCode.length>4) {
					logger.logWarning("Opss! You may have the Service's Code  maximum length of 4. Please try again.")
					return false;
				}
			}
			if($scope.serviceName!=""){
				if (!$serviceValid($scope.serviceName)) {
					logger.logWarning("Opss! You may have skipped specifying the Service's Name. Please try again.")
					return false;
				}
			}
		}
		}
		$scope.serviceEditMode = true;
		$scope.servicePriceMode = false;
		
	}
	
	$scope.regions=[{regionId:1,regionName:'Sub-County'},
	                {regionId:2,regionName:'Wards'},
	                {regionId:3,regionName:'Markets'}]
	
	$scope.loadActiveSubCounties = function () {
		$scope.subCounties = [];

		wardSvc.GetActiveSubCounties().success(function (response) {
			$scope.subCounties = response
		})
	};

	
	$scope.loadActiveWardData = function () {
		$scope.wards = [];

		marketSvc.GetActiveWards().success(function (response) {
			$scope.wards = response
		})
	};
	
	$scope.loadActiveMarketData = function () {
		$scope.markets = [];

		submarketSvc.GetActiveMarkets().success(function (response) {
			$scope.markets = response
		})
	};
	
	
	$scope.$watch("regionSelect", function (newValue, oldValue) {
		if ($serviceValid(newValue)) {
			if (newValue != oldValue) {
				if(newValue==1){
					$scope.showSubCounty=false;
					$scope.showWards=true;
					$scope.showMarkets=true;
					$scope.loadActiveSubCounties();
				}else if(newValue==2){
					$scope.showWards=false;
					$scope.showMarkets=true;
					$scope.showSubCounty=true;
					$scope.loadActiveWardData();
				}else if(newValue==3){
					$scope.showMarkets=false;
					$scope.showWards=true;
					$scope.showSubCounty=true;
					$scope.loadActiveMarketData();
				}
			}
		}
		else
			$scope.markets = [];
			$scope.wards = [];
			$scope.subCounties = [];
	});
	

	
} ]).factory('serviceSvc', function ($http) {
	var ervServiceSvc = {};

	ervServiceSvc.GetServices = function ($scope) {
		return $http({
			url: '/erevenue/rest/service/gtServices',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	ervServiceSvc.GetSubServices = function (serviceId) {
		return $http({
			url: '/erevenue/rest/service/gtSubServices/'+serviceId,
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	ervServiceSvc.GetParameter = function ($scope) {
		return $http({
			url: '/erevenue/rest/service/gtParams',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	ervServiceSvc.GetSubServiceById = function (parentServiceId) {
		return $http({
			url: '/erevenue/rest/service/gtSubServices/'+parentServiceId,
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	ervServiceSvc.UpdService = function (service) {
		return $http({
			url: '/erevenue/rest/service/updService',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: service
		});
	};
	return ervServiceSvc;
}).factory('$serviceValid', function () {
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
})  .directive('dropDown', function($compile) {
	return {

		restrict: 'E',

		scope: {
			user: '=user'
		},

		controller: function($scope) {

			$scope.addChild = function (child) {
				var index = $scope.user.children.length;
				$scope.user.children.push({
					"parent": $scope.user,
					"children": [],
					"index": index
				});
			}

			$scope.remove = function () {
				if ($scope.user.parent) {
					var parent = $scope.user.parent;
					var index = parent.children.indexOf($scope.user);
					parent.children.splice(index, 1);
				}
			}
		},

		// templateUrl: '/kcmc/masters/service',

		link: function ($scope, $element, $attrs) {

		},

		compile: function(tElement, tAttr) {
			var contents = tElement.contents().remove();
			var compiledContents;
			return function(scope, iElement, iAttr) {
				if(!compiledContents) {
					compiledContents = $compile(contents);
				}
				compiledContents(scope, function(clone, scope) {
					iElement.append(clone); 
				});
			};
		}
	};
});