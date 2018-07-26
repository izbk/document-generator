package db.mysql;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.utils.FtUtil;

/**
 * 
 * @author F
 *
 */
public class MainMysql {
	public static void main(String[] args) throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.10.202:3306/log", "alps", "123qwer");

		List<Map<String, Object>> tableList = getTableList(conn);

		conn.close();

		FtUtil ftUtil = new FtUtil();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("table", tableList);

		ftUtil.generateFile("/", "template.xml", map, "D:/", "mysql.doc");
	}

	// 获取数据库中所有表的表名，并添加到列表结构中。
	public static List<Map<String, Object>> getTableList(Connection conn) throws SQLException {
		List<Map<String, Object>> tableList = new ArrayList<Map<String, Object>>();

		String sql = "SHOW TABLE STATUS";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Map<String, Object> map = new HashMap<String, Object>();
			String TABLE_NAME = rs.getString("NAME");
			String COMMENTS = rs.getString("COMMENT");
			map.put("TABLE_NAME", TABLE_NAME);
			map.put("COMMENTS", COMMENTS == null ? "" : COMMENTS);

			// 获取列
			List<Map<String, Object>> columnList = getColumnList(conn, TABLE_NAME);
			map.put("COLUMNS", columnList);

			// if (TABLE_NAME.startsWith("ZJ"))
			tableList.add(map);
			System.out.println("TABLE_NAME ==>" + TABLE_NAME + "  COMMENTS==>" + COMMENTS);
		}
		rs.close();
		ps.close();
		return tableList;
	}

	// 获取数据表中所有列的列名，并添加到列表结构中。
	public static List<Map<String, Object>> getColumnList(Connection conn, String tableName) throws SQLException {

		List<Map<String, Object>> columnList = new ArrayList<Map<String, Object>>();

		// SHOW FULL COLUMNS FROM sys_org
		String sql = "SHOW FULL COLUMNS FROM " + tableName;

		PreparedStatement ps = conn.prepareStatement(sql);
		// ps.setString(1, tableName);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Map<String, Object> map = new HashMap<String, Object>();

			String COLUMN_NAME = rs.getString("FIELD");
			String DATA_TYPE = rs.getString("TYPE");// varchar(200)
			String DATA_LENGTH = "";// rs.getString("DATA_LENGTH");
			String DATA_DEFAULT = rs.getString("DEFAULT");
			String NULLABLE = rs.getString("NULL");
			String COMMENTS = rs.getString("COMMENT");
			String PRIMARY_KEY = rs.getString("KEY");

			map.put("COLUMN_NAME", COLUMN_NAME.toUpperCase());
			map.put("DATA_TYPE", DATA_TYPE.toUpperCase());
			map.put("DATA_LENGTH", DATA_LENGTH);
			map.put("DATA_DEFAULT", DATA_DEFAULT == null ? "" : DATA_DEFAULT);
			map.put("NULLABLE", "NO".equals(NULLABLE) ? "否" : "是");
			map.put("COMMENTS", COMMENTS == null ? "" : COMMENTS);
			map.put("PRIMARY_KEY", "PRI".equals(PRIMARY_KEY) ? true : false);
			columnList.add(map);

			System.out.println(
					"COLUMN_NAME ==>" + COLUMN_NAME + "  DATA_TYPE==>" + DATA_TYPE + "  DATA_LENGTH==>" + DATA_LENGTH
							+ " NULLABLE==>" + NULLABLE + "  COMMENTS==>" + COMMENTS + " PRIMARY_KEY==>" + PRIMARY_KEY);
		}
		rs.close();
		ps.close();
		return columnList;
	}

}
