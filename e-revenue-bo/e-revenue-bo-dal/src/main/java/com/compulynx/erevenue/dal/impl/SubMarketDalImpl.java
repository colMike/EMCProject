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

import com.compulynx.erevenue.dal.SubMarketDal;
import com.compulynx.erevenue.dal.operations.DBOperations;
import com.compulynx.erevenue.dal.operations.Queryconstants;
import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.Market;
import com.compulynx.erevenue.models.SubMarket;
import com.compulynx.erevenue.models.Ward;

/**
 * @author Anita
 * @date Apr 14, 2016
 *
 */
public class SubMarketDalImpl implements SubMarketDal{
	private DataSource dataSource;

	public SubMarketDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}
	@Override
	public ErevenueResponse UpdateSubMarket(SubMarket submarket) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			if (submarket.subMarketId == 0) {

				for (int i = 0; i < submarket.subMarketDetails.size(); i++) {
					if (CheckSubMarketName(submarket.subMarketDetails.get(i).subMarketName)) {
						return new ErevenueResponse(201,
								"Sub-Market name already exists");
					}
					if (CheckSubMarketCode(submarket.subMarketDetails.get(i).subMarketCode)) {
						return new ErevenueResponse(201,
								"Sub-Market code already exists");
					}
					preparedStatement = connection
							.prepareStatement(Queryconstants.insertSubMarket);
					preparedStatement.setString(1,
							submarket.subMarketDetails.get(i).subMarketCode);
					preparedStatement.setString(2,
							submarket.subMarketDetails.get(i).subMarketName);
					preparedStatement.setInt(3, submarket.marketId);
					preparedStatement.setBoolean(4, submarket.active);
					preparedStatement.setInt(5, submarket.createdBy);
					preparedStatement.setTimestamp(6, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					if (preparedStatement.executeUpdate() <= 0) {
						throw new Exception("Failed to Insert Sub-Market");
					}
				}
				return new ErevenueResponse(200, "Records Updated");

			} else {
				if (CheckSubMarketNameByCode(submarket.subMarketName, submarket.subMarketCode)) {
					return new ErevenueResponse(201,
							"Sub-Market name already exists");
				}
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateSubMarket);
				preparedStatement.setString(1, submarket.subMarketName);
				preparedStatement.setInt(2, submarket.marketId);
				preparedStatement.setBoolean(3, submarket.active);
				preparedStatement.setInt(4, submarket.createdBy);
				preparedStatement.setTimestamp(5, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(6, submarket.subMarketId);
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

	@Override
	public List<SubMarket> GetAllSubMarkets() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getSubMarkets);
			resultSet = preparedStatement.executeQuery();
			List<SubMarket> submarkets = new ArrayList<SubMarket>();
			while (resultSet.next()) {
				submarkets.add(new SubMarket(resultSet.getInt("ID"), resultSet
						.getString("sub_market_Code"),
						resultSet.getString("sub_market_Name"), resultSet
								.getInt("market_id"), resultSet
								.getString("mkt_name"), resultSet
								.getBoolean("active"), 200, count,resultSet
								.getString("status")));
				count++;
			}
			return submarkets;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	@Override
	public List<Market> GetActiveMarkets() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getActiveMarkets);
			resultSet = preparedStatement.executeQuery();
			List<Market> markets = new ArrayList<Market>();
			while (resultSet.next()) {
				markets.add(new Market(resultSet.getInt("id"),
						resultSet.getString("mkt_Name")));
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
	public boolean CheckSubMarketName(String subMarketName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkSubMarketName);
			preparedStatement.setString(1, subMarketName);

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

	public boolean CheckSubMarketCode(String subMarketode) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkSubMarketCode);
			preparedStatement.setString(1, subMarketode);

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

	public boolean CheckSubMarketNameByCode(String subMarketName, String Code) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkSubMarketNameByCode);
			preparedStatement.setString(1, subMarketName);
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
