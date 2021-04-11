package com.compulynx.erevenue.dal;

import java.util.List;

import com.compulynx.erevenue.models.DashBoard;

public interface DashBoardDal {
	List<DashBoard> GetDashBoardCountDetail();

	List<DashBoard> GetTransChartDetail();

	List<DashBoard> GetDashBoardAmountDetail();

	List<DashBoard> GetFlowChartCountDetail();

	DashBoard GetMonthStatisticsDetails();

	DashBoard GetUserStatisticsDetails();

	List<DashBoard> GetDashBoardCollectionDetail();
}
