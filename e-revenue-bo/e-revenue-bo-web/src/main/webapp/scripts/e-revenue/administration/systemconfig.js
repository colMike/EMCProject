/**
 * System Config Angular Module
 */
'use strict';
angular.module('app.config', []).controller("configCtrl", ["$scope", "$filter", "configSvc","$configValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter,configSvc, $configValid, $rootScope, blockUI, logger, $location) {
	
    var init;
    $scope.configParams=[];
    $scope.loadConfigParams = function () {
        $scope.configParams = [];
        configSvc.GetConfigParams().success(function (response) {
            $scope.configParams = response;
            if(response.length>0){
            	for(var i=0;i<response.length;i++){
            		if(response[i].name=='Logo'){
            			 $scope.myCroppedImage=response[i].value;
            		}
            	}
            }

        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
        });
    }
    $scope.loadConfigParams ()
    $scope.cancelConfig= function () {
    	$location.path('/dashboard');
    }
    
    $scope.myCroppedImage='';

    var handleFileSelect=function(evt) {
  	  $scope.showCamera=true;
        $scope.capPic=false;
        $scope.showImage=false;
      var file=evt.currentTarget.files[0];
      var reader = new FileReader();
      reader.onload = function (evt) {
        $scope.$apply(function($scope){
          $scope.myCroppedImage=evt.target.result;
        });
      };
      reader.readAsDataURL(file);

    };
    angular.element(document.querySelector('#fileInput')).on('change',handleFileSelect);

    $scope.updConfig = function () {
        var config = {};

  console.log($scope.configParams);

          config.configParams=$scope.configParams;
          config.createdBy= $rootScope.UsrRghts.sessionId;
          config.logo=$scope.myCroppedImage;
        blockUI.start()
        configSvc.UpdConfig(config).success(function (response) {
            if (response.respCode == 200) {
                logger.logSuccess("Great! The device information was saved succesfully")
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
                $scope.loadConfigParams ();
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

} ]).factory('configSvc', function ($http) {
    var evConfigSvc = {};
    evConfigSvc.GetConfigParams = function ($scope) {
        return $http({
            url: '/erevenue/rest/login/gtConfig',
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
    };

    evConfigSvc.UpdConfig = function (config) {
        return $http({
            url: '/erevenue/rest/login/updConfig',
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            data: config
                
        });
    };
    return evConfigSvc;
}).factory('$configValid', function () {
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