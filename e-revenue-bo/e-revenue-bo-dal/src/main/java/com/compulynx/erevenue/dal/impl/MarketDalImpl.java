/**
 * 
 */
package com.compulynx.erevenue.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.compulynx.erevenue.dal.MarketDal;
import com.compulynx.erevenue.dal.operations.DBOperations;
import com.compulynx.erevenue.dal.operations.Queryconstants;
import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.Market;
import com.compulynx.erevenue.models.Ward;

/**
 * class to handle the functionality of markets creation
 * @author Anita
 * @date Apr 13, 2016
 *
 */
public class MarketDalImpl implements MarketDal {
	private DataSource dataSource;

	public MarketDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	/**
	 * update the details of markets 
	 * @return 200 on successful update,201 on failed ,202 on exception
	 */
	@Override
	public ErevenueResponse UpdateMarket(Market market) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			if (market.marketId == 0) {

				for (int i = 0; i < market.marketDetails.size(); i++) {
					if (checkMarketName(market.marketDetails.get(i).marketName)) {
						return new ErevenueResponse(201,
								"Market name already exists");
					}
					if (checkMarketCode(market.marketDetails.get(i).marketCode)) {
						return new ErevenueResponse(201,
								"Market code already exists");
					}
					preparedStatement = connection
							.prepareStatement(Queryconstants.insertMarket);
					preparedStatement.setString(1,
							market.marketDetails.get(i).marketCode);
					preparedStatement.setString(2,
							market.marketDetails.get(i).marketName);
					preparedStatement.setInt(3, market.wardId);
					preparedStatement.setBoolean(4, market.active);
					preparedStatement.setInt(5, market.createdBy);
					preparedStatement.setTimestamp(6, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					if (preparedStatement.executeUpdate() <= 0) {
						throw new Exception("Failed to Insert Market");
					}
				}
				return new ErevenueResponse(200, "Records Updated");

			} else {
				if (checkMarketNameByCode(market.marketName, market.marketCode)) {
					return new ErevenueResponse(201,
							"Market name already exists");
				}
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateMarket);
				preparedStatement.setString(1, market.marketName);
				preparedStatement.setInt(2, market.wardId);
				preparedStatement.setBoolean(3, market.active);
				preparedStatement.setInt(4, market.createdBy);
				preparedStatement.setTimestamp(5, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(6, market.marketId);
				return (preparedStatement.executeUpdate() > 0) ? new ErevenueResponse(
						200, "Records Updated") : new ErevenueResponse(201,
								"Nothing To Update");
			}

		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			return new ErevenueResponse(202, "Exception Occured");

		} catch (Exception ex) {
			ex.printStackTrace();
			return new ErevenueResponse(202, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}

	}

	/**
	 * gets the list of markets
	 * @return list of markets
	 */
	@Override
	public List<Market> GetAllMarkets() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getMarkets);
			resultSet = preparedStatement.executeQuery();
			List<Market> markets = new ArrayList<Market>();
			while (resultSet.next()) {
				markets.add(new Market(resultSet.getInt("ID"), resultSet
						.getString("mkt_Code"),
						resultSet.getString("mkt_Name"), resultSet
						.getInt("ward_id"), resultSet
						.getString("ward_name"), resultSet
						.getInt("sub_county_id"), resultSet
						.getString("name"), resultSet
						.getBoolean("active"), 200, count,resultSet
						.getString("status")));
				count++;
			}
			return markets;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * gets the list of active wards
	 * @retrun list of active wards
	 */
	@Override
	public List<Ward> GetActiveWards() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getActiveWards);
			resultSet = preparedStatement.executeQuery();
			List<Ward> wards = new ArrayList<Ward>();
			while (resultSet.next()) {
				wards.add(new Ward(resultSet.getInt("id"),
						resultSet.getString("ward_Name")));
				count++;
			}
			return wards;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * checks if the market name is exists at the time of creating new market
	 * @param marketName
	 * @return true if exists else false
	 */
	public boolean checkMarketName(String marketName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkMarketName);
			preparedStatement.setString(1, marketName);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * check if the market code is exists at the time of creating new market
	 * @param marketode
	 * @return true if exists else false
	 */
	public boolean checkMarketCode(String marketode) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkMarketCode);
			preparedStatement.setString(1, marketode);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * check if the market code is exists at the time of updating market
	 * @param marketName
	 * @param Code
	 * @return true if exists else false
	 */
	public boolean checkMarketNameByCode(String marketName, String Code) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkMarketNameByCode);
			preparedStatement.setString(1, marketName);
			preparedStatement.setString(2, Code);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}


}
