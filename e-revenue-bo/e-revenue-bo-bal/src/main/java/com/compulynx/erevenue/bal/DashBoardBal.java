package com.compulynx.erevenue.bal;

import java.util.List;

import com.compulynx.erevenue.models.DashBoard;

public interface DashBoardBal {
	List<DashBoard> GetDashBoardCountDetail();

	List<DashBoard> GetTransChartDetail();

	List<DashBoard> GetDashBoardAmountDetail();

	List<DashBoard> GetFlowChartCountDetail();

	DashBoard GetMonthStatisticsDetails();

	DashBoard GetUserStatisticsDetails();

	List<DashBoard> GetDashBoardCollectionDetail();
}
