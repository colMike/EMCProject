/**
 * Member Angular Module
 */
'use strict';
angular.module('app.dashboard', []).controller("dashBoardCtrl", ["$scope", "$filter", "$rootScope", "dashBoardSvc","memStatementSvc","blockUI", "logger" ,"$location","$interval", function ($scope, $filter, $rootScope,dashBoardSvc,memStatementSvc, blockUI, logger, $location,$interval) {
	var init;
	$scope.hide=false;
	$scope.tempCountList = [];
	$scope.tempCountList2 = [];
	$scope.donutChart2=[];
	$scope.countList=[];
	$scope.userCount=0;
	$scope.marketCount=0;
	$scope.serviceCount=0;
	$scope.txnCount=0;
	$scope.tempLineChartDetail=[];
	$scope.lineChartDetail=[];
	$scope.dashBoardViewMode=true;
	$scope.totalAmount=0.00;
	$scope.netAmount=0.00;
	$scope.voideAmount=0.00;
	$scope.tempAmountList =[];
	$scope.tempCAmountList= [];
	$scope.flowChartList =[];
	$scope.pieChart=[];
	$scope.mothnlyStatistics=[];
	$scope.userStatistics=[];
	$rootScope.showProfile=$rootScope.UsrRghts.linkId;
	$scope.showDashBoard=false;
	$scope.collectionList=[];
	$scope.totalLand=0.00;
	$scope.totalCollection=0.00
	$scope.sbp=0.00
	$scope.landrates=0.00
	$scope.pos=0.00
	
	//test for loading accordian
	$scope.items = [{itemName:"Item 1"},{itemName:"Item 2"}]
	 $scope.oneAtATime = !0, $scope.groups = [{
         title: "Last Month",
         content: $scope.items
     }, {
         title: "Two Months ago",
         content: $scope.items
     }], $scope.addItem = function () {
         var newItemNo;
         newItemNo = $scope.items.length + 1, $scope.items.push("Item " + newItemNo)
     }
	$scope.loadMonthlyStatisticsDetail=function(){
		dashBoardSvc.GetMonthStatisticsDetail().success(function (response) {
			$scope.mothnlyStatistics = response;
			if($scope.mothnlyStatistics.length>0){
				$scope.currentMonth=$scope.mothnlyStatistics.currentMonth
				$scope.oneAtATime = !0, $scope.groups = [{
			         title: "Last Month",
			         content: $scope.mothnlyStatistics.previousMonth
			     }, {
			         title: "Two Months ago",
			         content: $scope.mothnlyStatistics.twoMonth
			     }]
			}
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	//$scope.loadMonthlyStatisticsDetail();
	
	$scope.loadUserStatisticsDetail=function(){
		dashBoardSvc.GetUserStatisticsDetail().success(function (response) {
			$scope.userStatistics = response;
			if($scope.userStatistics.userDsc.length>0){
					$scope.userDscAmount=$scope.userStatistics.userDsc[0].amount;
					$scope.userDscUserName=$scope.userStatistics.userDsc[0].userName;
				
				
			}
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
//	$scope.loadUserStatisticsDetail();
	
	$scope.loadCountDetail=function(){
		dashBoardSvc.GetCountDetail().success(function (response) {
			$scope.tempCountList = response;
			if($scope.tempCountList.length>0){

				for (var i = 0; i <= $scope.tempCountList.length - 1; i++) {
					if($scope.tempCountList[i].detailDescription=="USERS"){
						$scope.userCount=$scope.tempCountList[i].detailCount;

					}else if($scope.tempCountList[i].detailDescription=="MARKETS"){
						$scope.marketCount=$scope.tempCountList[i].detailCount;
					}
					else if($scope.tempCountList[i].detailDescription=="REGISTERED DEVICES"){
						$scope.deviceCount=$scope.tempCountList[i].detailCount;
					} else if($scope.tempCountList[i].detailDescription=="SERVICES"){
						$scope.serviceCount=$scope.tempCountList[i].detailCount;
					}
				}
			}
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	
	$scope.loadCollectionDetail=function(){
		dashBoardSvc.GetCollectionDetail().success(function (response) {
			$scope.collectionList = response;
			var  fractionSize;
		
			if($scope.collectionList.length>0){
				var sbp=0;
				var landRated=0;
				var pos=0;
				var plotrent=0;	var totallandrates=0;
				for (var i = 0; i <= $scope.collectionList.length - 1; i++) {
					if($scope.collectionList[i].detailDescription=="SBP Collections"){
						$scope.sbp=$scope.collectionList[i].amount;
						var sbp=$scope.collectionList[i].totalTxns;

					}else if($scope.collectionList[i].detailDescription=="LandRates Collections"){
						$scope.landrates=$scope.collectionList[i].amount;
						var landRates=$scope.collectionList[i].totalTxns;
					}
					else if($scope.collectionList[i].detailDescription=="POS Collections"){
						$scope.pos=$scope.collectionList[i].amount;
						var pos=$scope.collectionList[i].totalTxns;
					}else if($scope.collectionList[i].detailDescription=="Plot Rent Collections"){
						$scope.plotrent=$scope.collectionList[i].amount;
						var plotrent=$scope.collectionList[i].totalTxns;
					}
				}
				var totallandrates= landRates+plotrent;
				$scope.totalLand=$filter('number')(totallandrates,fractionSize);
				$scope.totalCollection=$filter('number')(sbp + totallandrates + pos, fractionSize);
				
				//$scope.totalCollection=sbp + landRates + pos
			}
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	$scope.loadCollectionDetail();
	
	$scope.loadFlowChartDetail=function(){
		dashBoardSvc.GetFlowChartDetail().success(function (response) {
			$scope.countList=[];
			$scope.flowChartList = response;
			if($scope.flowChartList.length>0){

				for (var i = 0; i <= $scope.flowChartList.length - 1; i++) {
					var obj =new Object();
					obj.data=$scope.flowChartList[i].transCount;
					obj.label=$scope.flowChartList[i].service;
					$scope.countList.push(obj);
					console.log($scope.countList)
					$scope.isdata=true;

				}
			}
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	$scope.loadFlowChartDetail();
	$scope.loadCountDetail();

	$scope.loadChartAmpuntDetail=function(){
		$scope.tempCAmountList= [];
		dashBoardSvc.GetAmountDetail().success(function (response) {
			$scope.tempCountList2 = response;
			console.log(response);
			if($scope.tempCountList2.length>0){
				$scope.donutChart = {}
				for (var j = 0; j <= $scope.tempCountList2.length - 1; j++) {
					var obj2 =new Object();
					obj2.data=$scope.tempCountList2[j].detailCount;
					obj2.label=$scope.tempCountList2[j].detailDescription;
					$scope.tempCAmountList.push( obj2);
					$scope.isdata=true;
				}
			}
		})
	}
	$scope.loadChartAmpuntDetail();

	$scope.loadAmpuntDetail=function(){
		memStatementSvc.GetCurrentDetailedTxn().success(function (response) {
			$scope.tempAmountList = response;
			if($scope.tempAmountList.length>0){

				for (var i = 0; i <= $scope.tempAmountList.length - 1; i++) {

					$scope.totalAmount=$scope.tempAmountList[i].totalAmount// +$scope.tempAmountList[i].invalidAmount;
					$scope.voideAmount=$scope.tempAmountList[i].voidAmount;
					$scope.invalidAmount=$scope.tempAmountList[i].invalidAmount;
					// $scope.netAmount=(($scope.totalAmount-$scope.voideAmount)-$scope.invalidAmount);
					$scope.netAmount=$scope.tempAmountList[i].netAmount;
					$scope.billNo=$scope.tempAmountList[i].billNo;

				}
			}
		})
	}
	$scope.loadAmpuntDetail();
	$scope.loadTransChartDetail=function(){
		dashBoardSvc.GetTransChartDetail().success(function (response) {
			$scope.tempLineChartDetail = response;
			if($scope.tempLineChartDetail.length>0){
				for(var i=0;i <= $scope.tempLineChartDetail.length - 1; i++){
					var objd =new Object();
					objd.year=$scope.tempLineChartDetail[i].monthName;
					objd.a=$scope.tempLineChartDetail[i].totalTxns;
					objd.b=$scope.tempLineChartDetail[i].voidTxns;
					objd.c=$scope.tempLineChartDetail[i].invalidTxns;
					objd.d=$scope.tempLineChartDetail[i].netTxns;
					$scope.lineChartDetail.push(objd);
				}
			}
			else{
				var objd =new Object();
				objd.year="";
				objd.a=0.00;
				objd.b=0.00;
				objd.c=0.00;
				objd.d=0.00;
				$scope.lineChartDetail.push(objd);
			}

		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	
	angular.foreach=function(e){
		for (var i=0;$scope.relations.length-1;i++ ){
			$scope.relations[i].slice(i,1)
		}
	}
	if($rootScope.UsrRghts.userClassId == 4){
		$scope.dashBoardViewMode=false;
		$scope.loadCountDetail();
		$scope.loadTransChartDetail();
	}

//	$scope.donutChart = {}, $scope.donutChart.data = [{
//		label: "Download Sales",
//		data: 12
//	}, {
//		label: "In-Store Sales",
//		data: 30
//	}, {
//		label: "Mail-Order Sales",
//		data: 20
//	}, {
//		label: "Online Sales",
//		data: 19
//	}], $scope.donutChart.options = {
//			series: {
//				pie: {
//					show: !0,
//					innerRadius: .5
//				}
//			},
//			legend: {
//				show: !0
//			},
//			grid: {
//				hoverable: !0,
//				clickable: !0
//			},
//			colors: ["#23AE89", "#2EC1CC", "#FFB61C", "#E94B3B"],
//			tooltip: !0,
//			tooltipOpts: {
//				content: "%p.0%, %s",
//				defaultTheme: !1
//			}
//	},
//	$scope.donutChart2.options = {
//			series: {
//				pie: {
//					show: !0,
//					innerRadius: .45
//				}
//			},
//			legend: {
//				show: !1
//			},
//			grid: {
//				hoverable: !0,
//				clickable: !0
//			},
//			colors: ["#176799", "#2F87B0", "#42A4BB", "#5BC0C4", "#78D6C7"],
//			tooltip: !0,
//			tooltipOpts: {
//				content: "%p.0%, %s",
//				defaultTheme: !1
//			}
//	}
	$scope.pieChart.options = {
			series: {
				pie: {
					show: !0
				}
			},
			legend: {
				show: !0
			},
			grid: {
				hoverable: !0,
				clickable: !0
			},
			colors: ["#23AE89", "#2EC1CC", "#FFB61C", "#E94B3B"],
			tooltip: !0,
			tooltipOpts: {
				content: "%p.0%, %s",
				defaultTheme: !1
			}
	}
	
	  if($rootScope.UsrRghts.linkId==3 || $rootScope.UsrRghts.linkId==4){
		  $scope.showDashBoard=true;
		}else{
			$scope.showDashBoard=false;
		}
	$scope.loadTransChartDetail();

}]).factory('dashBoardSvc', function ($http) {
	var ervDashBoardSvc = {};
	ervDashBoardSvc.GetCountDetail = function () {
		return $http({
			url: '/erevenue/rest/dashBoard/gtCountDetail',
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		});
	};
	ervDashBoardSvc.GetFlowChartDetail = function () {
		return $http({
			url: '/erevenue/rest/dashBoard/gtFlowChartDetail',
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		});
	};
	ervDashBoardSvc.GetTransChartDetail = function () {
		return $http({
			url: '/erevenue/rest/dashBoard/gtTransChartDetail',
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		});
	};


	ervDashBoardSvc.GetAmountDetail = function () {
		return $http({
			url: '/erevenue/rest/dashBoard/gtAmountDetail',
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		});
	};
	
	ervDashBoardSvc.GetMonthStatisticsDetail = function () {
		return $http({
			url: '/erevenue/rest/dashBoard/gtMonthStatisticDetail',
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		});
	};
	
	ervDashBoardSvc.GetUserStatisticsDetail = function () {
		return $http({
			url: '/erevenue/rest/dashBoard/gtUserStatisticsDetail',
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		});
	};
	
	ervDashBoardSvc.GetCollectionDetail = function () {
		return $http({
			url: '/erevenue/rest/dashBoard/gtCollectionDetail',
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		});
	};
	return ervDashBoardSvc;
})
.directive("flotChart", [function () {

	return {
		restrict: "A",
		scope: {
			data: "=",
			data2: "=",
			options: "="
		},

		link: function (scope, ele,attrs) {
			var data, options, plot,data2,plot2;
			scope.$watch("$parent.countList",function (newValue, oldValue){
				return data = scope.$parent.countList, options = scope.options, plot = $.plot(ele[0], data, options)
			},true)


		}
	}

}]).directive("morrisChart", [function () {
	return {
		restrict: "A",
		scope: {
			data: "="
		},
		link: function (scope, ele, attrs) {
			var colors, data, func, options;

			switch (data = scope.data, attrs.type) {
			case "bar":
				scope.$watch("$parent.lineChartDetail",function (newValue, oldValue){ 
					if(newValue.length>0){
						data = scope.$parent.lineChartDetail;
						data = scope.$parent.lineChartDetail;
						return colors = void 0 === attrs.barColors || "" === attrs.barColors ? null : JSON.parse(attrs.barColors), options = {
								element: ele[0],
								data: data,
								xkey: attrs.xkey,
								ykeys: JSON.parse(attrs.ykeys),
								labels: JSON.parse(attrs.labels),
								barColors: colors || ["#0b62a4", "#7a92a3", "#4da74d", "#afd8f8", "#edc240", "#cb4b4b", "#9440ed"],
								stacked: attrs.stacked || null,
								resize: !0
						}, attrs.formatter && (func = new Function("y", "data", attrs.formatter), options.formatter = func),new Morris.Bar(options);}
				},true);


			}
		}

	}
}]).directive("lineChart", [function () {
	return {
		restrict: "A",
		scope: {
			data: "="
		},

		link: function (scope, ele,attrs) {
			var colors, data, func, options;
			scope.$watch("$parent.lineChartDetail",function (newValue, oldValue){ 
				if(newValue.length>0){
					data = scope.$parent.lineChartDetail;
					return colors = void 0 === attrs.lineColors || "" === attrs.lineColors ? null : JSON.parse(attrs.lineColors), options = {
							element: ele[0],
							data: data,
							xkey: attrs.xkey,
							ykeys: JSON.parse(attrs.ykeys),
							labels: JSON.parse(attrs.labels),
							lineWidth: attrs.lineWidth || 2,
							lineColors: colors || ["#0b62a4", "#7a92a3", "#4da74d", "#afd8f8", "#edc240", "#cb4b4b", "#9440ed"],
							resize: !0
					}, new Morris.Line(options);
				}},true);

		}
	}

}])