/**
 * 
 */
package com.compulynx.erevenue.dal.impl;

import com.compulynx.erevenue.dal.ServiceDal;
import com.compulynx.erevenue.dal.operations.DBOperations;
import com.compulynx.erevenue.dal.operations.Queryconstants;
import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.Market;
import com.compulynx.erevenue.models.Params;
import com.compulynx.erevenue.models.Service;

import javax.sql.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * class to handle the functionality of service creation/param creation/price
 * configuration
 * 
 * @author Anita
 * @date Apr 26, 2016
 *
 */
public class ServiceDalImpl implements ServiceDal {
	private DataSource dataSource;

	public ServiceDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	/**
	 * checks the service code if its exists or not at the time of service
	 * creation
	 * 
	 * @param serviceCode
	 * @return true if exists else false
	 */
	public boolean checkServiceCode(String serviceCode) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getServiceByName);
			preparedStatement.setString(1, serviceCode);

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
	 * updates the service details updates the prices based on the parameter
	 * selected,like subcounty,wards or markets
	 * 
	 * @return 200 on successful,201 on failed and 202 on exception
	 */
	public ErevenueResponse UpdateService(Service service) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		ResultSet resultSet1 = null;
		ResultSet rs = null;
		int parentServiceId = 0;
		int subServiceId = 0;
		try {
			connection = dataSource.getConnection();
			if ((service.parentServiceId > 0)) {
				if ((service.isActive == true)
						&& (service.level == -1)
						&& ((service.serviceName == "") || (service.serviceName == null))) {
					preparedStatement = connection
							.prepareStatement(Queryconstants.updateSubServiceDetails);
					preparedStatement.setBoolean(1, service.active);
					preparedStatement.setInt(2, service.createdBy);
					preparedStatement.setTimestamp(3, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					preparedStatement.setInt(4, service.level);
					preparedStatement.setBoolean(5, service.hasChild);
					preparedStatement.setInt(6, service.parentServiceId);
					subServiceId = service.parentServiceId;
				} else {
					DBOperations.DisposeSql(preparedStatement);
					preparedStatement = connection.prepareStatement(
							Queryconstants.insertSubServiceDetails,
							Statement.RETURN_GENERATED_KEYS);
					preparedStatement.setInt(1, service.parentServiceId);
					preparedStatement.setString(2, service.serviceName);
					preparedStatement.setBoolean(3, service.hasChild);
					preparedStatement.setInt(4, service.level);
					preparedStatement.setBoolean(5, service.active);
					preparedStatement.setInt(6, service.createdBy);
					preparedStatement.setTimestamp(7, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					preparedStatement.setBoolean(8, service.parentType);
				}
				if (preparedStatement.executeUpdate() > 0) {
					if (service.isActive == true) {
						if (subServiceId == 0) {
							rs = preparedStatement.getGeneratedKeys();
							rs.next();
							subServiceId = rs.getInt(1);
						}
						DBOperations.DisposeSql(preparedStatement);
						preparedStatement = connection
								.prepareStatement(Queryconstants.deleteParams);
						preparedStatement.setInt(1, subServiceId);
						preparedStatement.executeUpdate();
						for (int i = 0; i < service.params.size(); i++) {

							DBOperations.DisposeSql(preparedStatement);
							preparedStatement = connection
									.prepareStatement(Queryconstants.insertParams);
							preparedStatement.setInt(1,
									service.params.get(i).paramId);
							preparedStatement.setInt(2, subServiceId);
							preparedStatement.setBoolean(3,
									service.params.get(i).isActive);
							preparedStatement.setInt(4, service.createdBy);
							preparedStatement.setTimestamp(
									5,
									new java.sql.Timestamp(new java.util.Date()
											.getTime()));
							if (preparedStatement.executeUpdate() <= 0) {
								throw new Exception(
										"Failed to Insert Param Id "
												+ service.params.get(i).paramId);
							}
						}
					}
					if (service.subCountyList != null) {
						if (service.subCountyList.size() > 0) {
							ArrayList<Market> marketList = new ArrayList<Market>();
							for (int i = 0; i < service.subCountyList.size(); i++) {
								if (service.subCountyList.get(i).serviceValue > 0
										|| service.subCountyList.get(i).serviceValue == -1) {
									preparedStatement1 = connection
											.prepareStatement(Queryconstants.getMarketsForSubcounty);
									preparedStatement1
											.setInt(1, service.subCountyList
													.get(i).subCountyId);
									resultSet1 = preparedStatement1
											.executeQuery();

									while (resultSet1.next()) {
										Market objMarket = new Market();
										objMarket.marketId = resultSet1
												.getInt("id");
										objMarket.serviceValue = service.subCountyList
												.get(i).serviceValue;
										marketList.add(objMarket);
									}
								}
							}
							for (int i = 0; i < marketList.size(); i++) {
								preparedStatement1 = connection
										.prepareStatement(Queryconstants.insertMatketPriceDtl);
								preparedStatement1.setInt(1,
										marketList.get(i).marketId);
								preparedStatement1.setInt(2, subServiceId);
								preparedStatement1.setDouble(3,
										marketList.get(i).serviceValue);
								preparedStatement1.setInt(4, service.createdBy);
								preparedStatement1
										.setTimestamp(
												5,
												new java.sql.Timestamp(
														new java.util.Date()
																.getTime()));
								if (preparedStatement1.executeUpdate() <= 0) {
									throw new Exception(
											"Failed to Insert Market Id "
													+ marketList.get(i).marketId);
								}
							}
						}
					}
					if (service.wardList != null) {
						if (service.wardList.size() > 0) {
							ArrayList<Market> marketList = new ArrayList<Market>();
							for (int i = 0; i < service.wardList.size(); i++) {
								if (service.wardList.get(i).serviceValue > 0
										|| service.wardList.get(i).serviceValue == -1) {
									preparedStatement1 = connection
											.prepareStatement(Queryconstants.getMarketsForWards);
									preparedStatement1.setInt(1,
											service.wardList.get(i).wardId);
									resultSet1 = preparedStatement1
											.executeQuery();

									while (resultSet1.next()) {
										Market objMarket = new Market();
										objMarket.marketId = resultSet1
												.getInt("id");
										objMarket.serviceValue = service.wardList
												.get(i).serviceValue;
										marketList.add(objMarket);
									}
								}
							}
							for (int i = 0; i < marketList.size(); i++) {
								preparedStatement1 = connection
										.prepareStatement(Queryconstants.insertMatketPriceDtl);
								preparedStatement1.setInt(1,
										marketList.get(i).marketId);
								preparedStatement1.setInt(2, subServiceId);
								preparedStatement1.setDouble(3,
										marketList.get(i).serviceValue);
								preparedStatement1.setInt(4, service.createdBy);
								preparedStatement1
										.setTimestamp(
												5,
												new java.sql.Timestamp(
														new java.util.Date()
																.getTime()));
								if (preparedStatement1.executeUpdate() <= 0) {
									throw new Exception(
											"Failed to Insert Market Id "
													+ marketList.get(i).marketId);
								}
							}
						}
					}
					if (service.marketList != null) {
						if (service.marketList.size() > 0) {
							for (int i = 0; i < service.marketList.size(); i++) {
								if (service.marketList.get(i).serviceValue > 0
										|| service.marketList.get(i).serviceValue == -1) {
									preparedStatement1 = connection
											.prepareStatement(Queryconstants.insertMatketPriceDtl);
									preparedStatement1.setInt(1,
											service.marketList.get(i).marketId);
									preparedStatement1.setInt(2, subServiceId);
									preparedStatement1
											.setDouble(
													3,
													service.marketList.get(i).serviceValue);
									preparedStatement1.setInt(4,
											service.createdBy);
									preparedStatement1.setTimestamp(
											5,
											new java.sql.Timestamp(
													new java.util.Date()
															.getTime()));
									if (preparedStatement1.executeUpdate() <= 0) {
										throw new Exception(
												"Failed to Insert Market Id "
														+ service.params.get(i).paramId);
									}
								}
							}
						}
					}
					return new ErevenueResponse(200, "Records Updated");
				} else {
					return new ErevenueResponse(201, "Failed to Updated");
				}

			} else {
				if (service.serviceId == 0) {
					if (checkServiceCode(service.serviceCode)) {
						return new ErevenueResponse(201,
								"Service Code Already Exists");
					}
					preparedStatement = connection.prepareStatement(
							Queryconstants.insertServiceDetails,
							Statement.RETURN_GENERATED_KEYS);
					preparedStatement.setString(1, service.serviceCode);
					preparedStatement.setString(2, service.serviceName);
					preparedStatement.setBoolean(3, service.active);
					preparedStatement.setInt(4, service.createdBy);
					preparedStatement.setTimestamp(5, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					if (preparedStatement.executeUpdate() > 0) {
						if (service.serviceId == 0) {
							rs = preparedStatement.getGeneratedKeys();
							rs.next();
							parentServiceId = rs.getInt(1);
						}
						if (service.isActive == true) {
							DBOperations.DisposeSql(preparedStatement, rs);
							preparedStatement = connection.prepareStatement(
									Queryconstants.insertSubServiceDetails,
									Statement.RETURN_GENERATED_KEYS);
							preparedStatement.setInt(1, parentServiceId);
							preparedStatement.setString(2, "Parent");
							preparedStatement.setBoolean(3, service.hasChild);
							preparedStatement.setInt(4, service.level);
							preparedStatement.setBoolean(5, service.active);
							preparedStatement.setInt(6, service.createdBy);
							preparedStatement.setTimestamp(
									7,
									new java.sql.Timestamp(new java.util.Date()
											.getTime()));
							preparedStatement.setBoolean(8, service.parentType);
							if (preparedStatement.executeUpdate() > 0) {
								if (subServiceId == 0) {
									rs = preparedStatement.getGeneratedKeys();
									rs.next();
									subServiceId = rs.getInt(1);
								}
								DBOperations.DisposeSql(preparedStatement);
								preparedStatement = connection
										.prepareStatement(Queryconstants.deleteParams);
								preparedStatement.setInt(1, subServiceId);
								preparedStatement.executeUpdate();
								for (int i = 0; i < service.params.size(); i++) {

									DBOperations.DisposeSql(preparedStatement);
									preparedStatement = connection
											.prepareStatement(Queryconstants.insertParams);
									preparedStatement.setInt(1,
											service.params.get(i).paramId);
									preparedStatement.setInt(2, subServiceId);
									preparedStatement.setBoolean(3,
											service.params.get(i).isActive);
									preparedStatement.setInt(4,
											service.createdBy);
									preparedStatement.setTimestamp(
											5,
											new java.sql.Timestamp(
													new java.util.Date()
															.getTime()));
									if (preparedStatement.executeUpdate() <= 0) {
										throw new Exception(
												"Failed to Insert Param Id "
														+ service.params.get(i).paramId);
									}
								}
							}
						}
						return new ErevenueResponse(200, "Records Updated");
					} else {
						return new ErevenueResponse(201, "Failed to Updated");
					}
				}
				if (service.subCountyList.size() > 0) {
					ArrayList<Market> marketList = new ArrayList<Market>();
					for (int i = 0; i < service.subCountyList.size(); i++) {
						if (service.subCountyList.get(i).serviceValue > 0
								|| service.subCountyList.get(i).serviceValue == -1) {
							preparedStatement1 = connection
									.prepareStatement(Queryconstants.getMarketsForSubcounty);
							preparedStatement1.setInt(1,
									service.subCountyList.get(i).subCountyId);
							resultSet1 = preparedStatement1.executeQuery();

							while (resultSet1.next()) {
								Market objMarket = new Market();
								objMarket.marketId = resultSet1.getInt("id");
								objMarket.serviceValue = service.subCountyList
										.get(i).serviceValue;
								marketList.add(objMarket);
							}
						}
					}
					for (int i = 0; i < marketList.size(); i++) {
						preparedStatement1 = connection
								.prepareStatement(Queryconstants.insertMatketPriceDtl);
						preparedStatement1.setInt(1,
								service.marketList.get(i).marketId);
						preparedStatement1.setInt(2, subServiceId);
						preparedStatement1.setDouble(3,
								service.marketList.get(i).serviceValue);
						preparedStatement1.setInt(4, service.createdBy);
						preparedStatement1.setTimestamp(
								5,
								new java.sql.Timestamp(new java.util.Date()
										.getTime()));
						if (preparedStatement1.executeUpdate() <= 0) {
							throw new Exception("Failed to Insert Market Id "
									+ service.marketList.get(i).marketId);
						}
					}
				}
				if (service.wardList.size() > 0) {
					ArrayList<Market> marketList = new ArrayList<Market>();
					for (int i = 0; i < service.wardList.size(); i++) {
						if (service.wardList.get(i).serviceValue > 0
								|| service.wardList.get(i).serviceValue == -1) {
							preparedStatement1 = connection
									.prepareStatement(Queryconstants.getMarketsForWards);
							preparedStatement1.setInt(1,
									service.wardList.get(i).wardId);
							resultSet1 = preparedStatement1.executeQuery();

							while (resultSet1.next()) {
								Market objMarket = new Market();
								objMarket.marketId = resultSet1.getInt("id");
								objMarket.serviceValue = service.wardList
										.get(i).serviceValue;
								marketList.add(objMarket);
							}

						}
					}
					for (int i = 0; i < marketList.size(); i++) {
						preparedStatement1 = connection
								.prepareStatement(Queryconstants.insertMatketPriceDtl);
						preparedStatement1.setInt(1,
								service.marketList.get(i).marketId);
						preparedStatement1.setInt(2, subServiceId);
						preparedStatement1.setDouble(3,
								service.marketList.get(i).serviceValue);
						preparedStatement1.setInt(4, service.createdBy);
						preparedStatement1.setTimestamp(
								5,
								new java.sql.Timestamp(new java.util.Date()
										.getTime()));
						if (preparedStatement1.executeUpdate() <= 0) {
							throw new Exception("Failed to Insert Market Id "
									+ service.marketList.get(i).marketId);
						}
					}
				}
				if (service.marketList.size() > 0) {
					for (int i = 0; i < service.marketList.size(); i++) {
						if (service.marketList.get(i).serviceValue > 0
								|| service.marketList.get(i).serviceValue == -1) {
							preparedStatement1 = connection
									.prepareStatement(Queryconstants.insertMatketPriceDtl);
							preparedStatement1.setInt(1,
									service.marketList.get(i).marketId);
							preparedStatement1.setInt(2, subServiceId);
							preparedStatement1.setDouble(3,
									service.marketList.get(i).serviceValue);
							preparedStatement1.setInt(4, service.createdBy);
							preparedStatement1.setTimestamp(
									5,
									new java.sql.Timestamp(new java.util.Date()
											.getTime()));
							if (preparedStatement1.executeUpdate() <= 0) {
								throw new Exception(
										"Failed to Insert Market Id "
												+ service.params.get(i).paramId);
							}
						}
					}
				}
				return new ErevenueResponse(201, "Nothing to update");
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
	 * gets the list of services
	 * 
	 * @return list of services
	 */
	public List<Service> GetAllServices() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getServices);

			resultSet = preparedStatement.executeQuery();
			List<Service> services = new ArrayList<Service>();
			while (resultSet.next()) {
				services.add(new Service(resultSet.getInt("ID"), resultSet
						.getString("Service_Code"), resultSet
						.getString("Service_Name"), resultSet
						.getBoolean("Active"), resultSet.getInt("Created_By"),
						200, (resultSet.getBoolean("Active") == true ? "Active"
								: "Inactive"), count, true,
						"glyphicon glyphicon-pencil"));
				count++;
			}
			return services;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * gets the service details based on service id passed
	 * 
	 * @param serviceId
	 * @return 200 on successful,201 on failed and 202 on exception
	 */
	public Service GetServiceById(int serviceId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();

			preparedStatement = connection
					.prepareStatement(Queryconstants.getServiceById);
			preparedStatement.setInt(1, serviceId);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return new Service(resultSet.getInt("ServiceID"),
						resultSet.getString("ServiceCode"),
						resultSet.getString("ServiceName"),
						resultSet.getBoolean("active"),
						resultSet.getInt("CreatedBy"), 200,
						(resultSet.getBoolean("Active") == true ? "Active"
								: "Inactive"), count, true,
						"glyphicon glyphicon-pencil");
			} else {

				return new Service(201);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * gets the subservice details and param details by service id
	 * 
	 * @param serviceId
	 *            - parent service id for the subservice
	 * @return the list of subservices for selected parent service
	 */
	public List<Service> GetSubServiceById(int serviceId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();

			preparedStatement = connection
					.prepareStatement(Queryconstants.getSubServiceById);
			preparedStatement.setInt(1, serviceId);

			resultSet = preparedStatement.executeQuery();
			List<Service> services = new ArrayList<Service>();
			while (resultSet.next()) {
				Service service = new Service();
				service.serviceId = resultSet.getInt("ID");
				service.serviceCode = resultSet.getString("subService_Name");
				service.serviceName = resultSet.getString("subService_Name");
				service.active = resultSet.getBoolean("active");
				service.createdBy = resultSet.getInt("Created_By");
				service.level = resultSet.getInt("level");
				service.hasChild = resultSet.getBoolean("has_child");
				service.parentServiceId = resultSet.getInt("parent_Service_Id");
				service.count = count;
				if (resultSet.getInt("level") == -1) {
					preparedStatement = connection
							.prepareStatement(Queryconstants.getServiceParams);
					preparedStatement.setInt(1, service.serviceId);
					preparedStatement.setInt(2, service.serviceId);
					resultSet2 = preparedStatement.executeQuery();
					List<Params> params = new ArrayList<Params>();
					while (resultSet2.next()) {
						Params objParam = new Params();
						objParam.paramId = resultSet2.getInt("Param_Id");
						objParam.paramName = resultSet2.getString("param_Name");
						objParam.isActive = resultSet2.getBoolean("active");
						params.add(objParam);
					}
					service.params = params;
				}
				services.add(service);
				count++;
			}
			return services;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * gets the list of all param
	 * 
	 * @return list of params
	 */
	public List<Params> GetAllParams() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getParams);

			resultSet = preparedStatement.executeQuery();
			List<Params> params = new ArrayList<Params>();
			while (resultSet.next()) {
				params.add(new Params(resultSet.getInt("ID"), resultSet
						.getString("Param_Name"), resultSet
						.getString("Param_type"), resultSet
						.getBoolean("Active"), resultSet.getInt("Created_By"),
						200, count, resultSet.getString("status")));
				count++;
			}
			return params;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * updates the param details
	 * 
	 * @return 200 on successful,201 on failed and 202 on exception
	 */
	public ErevenueResponse UpdateParam(Params param) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();

			if (param.paramId == 0) {
				if (checkParamName(param.paramName)) {
					return new ErevenueResponse(201,
							"Param Name Already Exists");
				}

				preparedStatement = connection
						.prepareStatement(Queryconstants.insertParamInfo);
				preparedStatement.setString(1, param.paramName);
				preparedStatement.setString(2, param.paramType);
				preparedStatement.setBoolean(3, param.active);
				preparedStatement.setInt(4, param.createdBy);
				preparedStatement.setTimestamp(5, new java.sql.Timestamp(
						new java.util.Date().getTime()));
			} else {
				if (checkParamNameById(param.paramName, param.paramId)) {
					return new ErevenueResponse(201,
							"Param Name Already Exists");
				}
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateParamInfo);
				preparedStatement.setString(1, param.paramName);
				preparedStatement.setString(2, param.paramType);
				preparedStatement.setBoolean(3, param.active);
				preparedStatement.setInt(4, param.createdBy);
				preparedStatement.setTimestamp(5, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(6, param.paramId);
			}
			return (preparedStatement.executeUpdate() > 0) ? new ErevenueResponse(
					200, "Records Updated") : new ErevenueResponse(201,
					"Nothing To Update");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
				return new ErevenueResponse(201, "Param name Already Exists");
			} else {
				return new ErevenueResponse(202, "Exception Occured");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ErevenueResponse(202, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * checks the param name if its exists at the time of inserting the new
	 * param
	 * 
	 * @param paramName
	 * @return true if exists else false
	 */
	public boolean checkParamName(String paramName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getParamName);
			preparedStatement.setString(1, paramName);

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
	 * checks the param name if exists at the time of upating the param details
	 * 
	 * @param paramName
	 * @param paramId
	 * @return true if exists else false
	 */
	public boolean checkParamNameById(String paramName, int paramId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getParamNameById);
			preparedStatement.setString(1, paramName);
			preparedStatement.setInt(2, paramId);

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
	 * gets the list of all the subservices created this function used for price
	 * config
	 * 
	 * @return list of price config
	 */
	public List<Service> GetAllSubServices() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getAllSubServices);

			resultSet = preparedStatement.executeQuery();
			List<Service> subSer = new ArrayList<Service>();
			while (resultSet.next()) {
				subSer.add(new Service(
						(resultSet.getInt("level") == -1 ? "Final Service"
								: "Not Final Service"), resultSet.getInt("Id"),
						resultSet.getString("SubService_Name"), (resultSet
								.getBoolean("Active") == true ? "Active"
								: "InActive"), resultSet.getBoolean("Active"),
						count, true, "glyphicon glyphicon-pencil"));
				count++;
			}
			return subSer;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * updates the price details for the selected services this function used in
	 * price configuration
	 * 
	 * @return 200 on successful,201 on failed and 202 on exception
	 */
	public ErevenueResponse UpdateSerPrice(Service ser) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();

			if (ser.serviceType == 1) {
				for (int i = 0; i < ser.serviceList.size(); i++) {
					preparedStatement = connection
							.prepareStatement(Queryconstants.updateParentSerInfo);
					preparedStatement.setString(1,
							ser.serviceList.get(i).serviceName);
					preparedStatement.setBoolean(2,
							ser.serviceList.get(i).active);
					preparedStatement.setInt(3, ser.createdBy);
					preparedStatement.setTimestamp(4, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					preparedStatement.setInt(5,
							ser.serviceList.get(i).serviceId);
					if (preparedStatement.executeUpdate() <= 0) {
						throw new Exception(
								"Failed to update parent services details ");
					}
				}
			} else if (ser.serviceType == 2) {
				for (int i = 0; i < ser.serviceList.size(); i++) {
					preparedStatement = connection
							.prepareStatement(Queryconstants.updateSubSerInfo);
					preparedStatement.setString(1,
							ser.serviceList.get(i).serviceName);
					preparedStatement.setBoolean(2,
							ser.serviceList.get(i).active);
					preparedStatement.setInt(3, ser.createdBy);
					preparedStatement.setTimestamp(4, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					preparedStatement.setInt(5,
							ser.serviceList.get(i).serviceId);
					if (preparedStatement.executeUpdate() <= 0) {
						throw new Exception(
								"Failed to update sub services details ");
					}

				}
			} else if (ser.serviceType == 3) {
				preparedStatement = connection
						.prepareStatement(Queryconstants.deleteMarketPriceDtl);
				preparedStatement.setInt(1, ser.marketId);
				preparedStatement.executeUpdate();
				DBOperations.DisposeSql(preparedStatement);
				for (int i = 0; i < ser.serviceList.size(); i++) {
					if (ser.serviceList.get(i).serviceValue > 0
							|| ser.serviceList.get(i).serviceValue == -1) {
						preparedStatement = connection
								.prepareStatement(Queryconstants.insertMatketPriceDtl);
						preparedStatement.setInt(1, ser.marketId);
						preparedStatement.setInt(2,
								ser.serviceList.get(i).serviceId);
						preparedStatement.setDouble(3,
								ser.serviceList.get(i).serviceValue);
						preparedStatement.setInt(4, ser.createdBy);
						preparedStatement.setTimestamp(
								5,
								new java.sql.Timestamp(new java.util.Date()
										.getTime()));

						if (preparedStatement.executeUpdate() <= 0) {
							throw new Exception("Failed to Insert Service Id "
									+ ser.serviceList.get(i).serviceId);
						}
					}

				}
			}
			return new ErevenueResponse(200, "Update Successfully");
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
	 * gets the price details for the subservices by market
	 * 
	 * @param markerId
	 * @return list of suservices attached to market
	 */
	public List<Service> GetMarketSubServices(int marketId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getMarketPriceDetails);
			preparedStatement.setInt(1, marketId);
			preparedStatement.setInt(2, marketId);
			preparedStatement.setInt(3, marketId);
			resultSet = preparedStatement.executeQuery();
			List<Service> subSer = new ArrayList<Service>();
			while (resultSet.next()) {
				subSer.add(new Service(resultSet.getInt("ser_id"), resultSet
						.getString("subservice_name"), resultSet
						.getDouble("fee"),
						(resultSet.getBoolean("Active") == true ? "Active"
								: "Inactive"), resultSet.getBoolean("Active"),
						count, true, "glyphicon glyphicon-pencil"));
				count++;
			}
			return subSer;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

}
