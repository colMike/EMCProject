/**
 * User Import Angular Module
 */
'use strict';
angular.module('app.plotupload', []).controller("plotUploadCtrl", ["$scope", "$filter","wardSvc", "userUploadSvc","localStorageService", "$rootScope", "blockUI", "logger","$location", function ($scope, $filter,wardSvc,userUploadSvc,localStorageService,$rootScope, blockUI, logger,$location) {  
 var init;
	console.log("Entry to bulk user upload"+" "+"++++++++")
	$scope.myFile="";
	   $scope.loadActiveSubCounties = function () {
	        $scope.subCounties = [];
	        wardSvc.GetActiveSubCounties().success(function (response) {
	            $scope.subCounties = response;
	        })
	    };
	    $scope.loadActiveSubCounties();
    $scope.uploadUserDetails = function () {
        //logger.logSuccess("Uploading file in progress");
    	if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
			logger.logWarning("Oh snap! You are not allowed to upload the users.");
			return false;
		}
        var file = $scope.myFile;
       // var file ='myfile';
       console.log('file is## ' + file);
        blockUI.start()
        userUploadSvc.uploadUserDetails(file,$rootScope.UsrRghts.sessionId,$scope.subCountySelect).success(function (response) {
            if (response.respCode == 200) {
            	logger.logSuccess(response.respMessage);

                $location.path('/dashboard');

            } else {
                logger.logWarning(response.respMessage);
            }
            blockUI.stop();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
            blockUI.stop();
        });
    };


  Date.prototype.yyyymmdd = function() {
  	  var mm = this.getMonth() + 1; // getMonth() is zero-based
  	  var dd = this.getDate();

  	  return [this.getFullYear(),
  	          (mm>9 ? '' : '0') + mm,
  	          (dd>9 ? '' : '0') + dd
  	         ].join('');
  };
	var date = new Date();
	date.yyyymmdd();

	console.log("todays date " +date.yyyymmdd());
	
 // agencyusers_24092019_2.apk
	
    $scope.filesChanged=function(elm){
   	  /* $scope.files=elm.files;
   	   $scope.$apply;
   	   var filename=$scope.files[0].name;
   	   var isANumber = isNaN(filename) === false;
   	   var branch = 1;
   	   var numbers = /^[0-9]+$/;
   	   var string = /^[A-Za-z]+$/;
   	   var schar= /[_]/gi;
   	   var partners
   	 var count = (filename.match(/is/g) || []).length;
   	   console.log(count);
   	   console.log("filename  "+filename);
   		  // if((filename.match(string) > -1) && (filename.indexOf(date.yyyymmdd()) > -1)   && (filename.indexOf("plotrent") > -1) && (filename.match(numbers) >-1) ){
   	  		   if((filename.match(string) > -1) && (filename.indexOf(date.yyyymmdd()) > -1) && (filename.match(numbers) >-1)&& (filename.match(schar).length ==3)  && (filename.indexOf("plotrent") > -1)){
   	  			console.log("filename part two  "+filename);
   			   logger.logSuccess("the file is ok...");
   		   return false;
   	   }else{
   		   angular.element("input[type='file']").val(null);
   		   logger.logWarning("The file does not conform to file naming conventions [plot_rent]_[CURRENT_DATE] Please choose another file");

   		   return true;

   	   }*/
   	  		   
   	  		   
   	  		$scope.files=elm.files;
   	  	   $scope.$apply;
   	  	   var filename=$scope.files[0].name;
   	  	   var isANumber = isNaN(filename) === false;
   	  	   var numbers = /^[0-9]+$/;
   	  	   var string = /^[A-Za-z]+$/;
   	  	   var schar= (filename.match( /[_]/gi) || []).length;
   	  	   var partners
   	  	   var count = (filename.match(/is/g) || []).length;
   	  	   console.log(count);
   	  	console.log(schar.length, "schar");
   	  	   console.log("filename  "+filename);
   	  		   if((filename.match(string) > -1) && (filename.indexOf(date.yyyymmdd()) > -1)  && (filename.indexOf("plotrent") > -1)){
   	  			   logger.logSuccess("the file is ok...");
   	  		   return false;
   	  	   }else{
   	  		   angular.element("input[type='file']").val(null);
   	  		   logger.logWarning("The file does not conform to file naming conventions [plotrent]_[CURRENT_DATE]_[FILE_COUNT] Please choose another file");

   	  		   return true;

   	  	   }
      }
} ]).factory('userUploadSvc', function ($http) {
    var uploadSvc = {};
    
    uploadSvc.uploadUserDetails = function (file,createdBy,branchId) {
    	console.log("this are the details of the `file"+createdBy+"  "+branchId)
        var fd = new FormData();
        fd.append('file', file);
        fd.append('createdBy',createdBy);
        fd.append('branchId',branchId);
        return $http({
        	 url: '/erevenue/rest/landrate/uploadPlotRentDetails/',
            method: 'POST',
            transformRequest: angular.identity,
            headers: { 'Content-Type': undefined },
            data: fd
        });
    };

    return uploadSvc;
}).directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;

            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);