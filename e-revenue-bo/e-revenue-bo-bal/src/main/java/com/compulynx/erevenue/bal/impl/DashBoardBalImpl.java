package com.compulynx.erevenue.bal.impl;

import java.util.List;

import com.compulynx.erevenue.bal.DashBoardBal;
import com.compulynx.erevenue.dal.impl.DashBoardDalImpl;
import com.compulynx.erevenue.models.DashBoard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DashBoardBalImpl implements DashBoardBal{

	@Autowired
	DashBoardDalImpl dashBoardDal;
	
	public List<DashBoard> GetDashBoardCountDetail() {
	
		return dashBoardDal.GetDashBoardCountDetail();
	}

	@Override
	public List<DashBoard> GetTransChartDetail() {
		return dashBoardDal.GetTransChartDetail();
	}

	@Override
	public List<DashBoard> GetDashBoardAmountDetail() {
		// TODO Auto-generated method stub
		return dashBoardDal.GetDashBoardAmountDetail();
	}

	@Override
	public List<DashBoard> GetFlowChartCountDetail() {
		// TODO Auto-generated method stub
		return dashBoardDal.GetFlowChartCountDetail();
	}

	@Override
	public DashBoard GetMonthStatisticsDetails() {
		// TODO Auto-generated method stub
		return dashBoardDal.GetMonthStatisticsDetails();
	}

	@Override
	public DashBoard GetUserStatisticsDetails() {
		// TODO Auto-generated method stub
		return dashBoardDal.GetUserStatisticsDetails();
	}

	@Override
	public List<DashBoard> GetDashBoardCollectionDetail() {
		// TODO Auto-generated method stub
		return dashBoardDal.GetDashBoardCollectionDetail();
	}

}
