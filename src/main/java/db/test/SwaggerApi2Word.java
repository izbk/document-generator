package db.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.besjon.pojo.Get;
import com.besjon.pojo.JsonRootBean;
import com.besjon.pojo.Parameters;
import com.besjon.pojo.Path;
import com.besjon.pojo.Post;
import com.besjon.pojo.Response;
import com.besjon.pojo.Tags;

import db.utils.FtUtil;
import db.utils.HTTPUtils;

/**
 * swagger api 生成word文档
 * 
 * @ClassName SwaggerApi2Word
 * @Description
 * @author Binke Zhang
 * @date 2018年6月22日 下午4:26:49
 * @Copyright: 2018 izbk@163.com All rights reserved.
 */
public class SwaggerApi2Word {
	/**
	 * 参数类型
	 */
	private static final String PARAM_HEADER = "header";
	private static final String PARAM_QUERY = "query";
	private static final String PARAM_BODY = "body";
	private static final String PARAM_PATH = "path";
	private static final String JSON_REF_KEY = "$ref";
	private static final String SUCESS_CODE = "200";
	/**
	 * 请求类型
	 */
	private static final String REQUEST_METHOD_GET = "GET";
	private static final String REQUEST_METHOD_POST = "POST";
	/**
	 * 自定义类型
	 */
	private static final String DEFINITIONS_PROP = "properties";
	private static final String DEFINITIONS_PROP_TYPE = "type";
	private static final String DEFINITIONS_PROP_DESC = "description";
	/**
	 * 返回数据定义
	 */
	private static final String RESULT_DATA_NAME = "name";
	private static final String RESULT_DATA_DESC = "description";
	private static final String RESULT_DATA_TYPE = "type";
	private static final String RESULT_DATA_REMARK = "remark";
	/**
	 * 模板参数
	 */
	private static final String URI = "uri";
	private static final String PATHPARAM = "pathParam";
	private static final String QUERYPARAM = "queryParam";
	private static final String METHOD = "method";
	private static final String SUMMARY = "summary";
	private static final String CONSUMES = "consumes";
	private static final String HEADERLIST = "headerList";
	private static final String QUERYLIST = "queryList";
	private static final String PATHLIST = "pathList";
	private static final String BODYLIST = "bodyList";
	private static final String RESULTLIST = "resultList";
	private static final String DATALIST = "dataList";
	private static final String TEMPLATE_NAME = "ApiTemplate.xml";
	private static final String CREATEFILE_NAME = "ApiTemplate.doc";

	public static void main(String[] args) throws Exception {
		createDoc2();
	}

	public static void createDoc2() throws Exception {
		List<Map<String, List<Map<String, Object>>>> dataList = new ArrayList<>();
//		File file = new File("swagger.json");
//		InputStream inputStream = new FileInputStream(file);
		HttpURLConnection connection = HTTPUtils.getConnection("http://127.0.0.1:8083/v2/api-docs");
		InputStream inputStream = connection.getInputStream();
		JsonRootBean root = JSON.parseObject(inputStream, JsonRootBean.class, Feature.DisableCircularReferenceDetect);
		List<Tags> tags = root.getTags();
		List<Map<String, Path>> paths = root.getPaths();
		List<Map<String, Object>> definitions = root.getDefinitions();
		for (Tags tag : tags) {
			Map<String, List<Map<String, Object>>> tagData = new HashMap<>();
			List<Map<String, Object>> tagDataList = new ArrayList<>();
			for (Map<String, Path> path : paths) {
				for (Map.Entry<String, Path> map : path.entrySet()) {
					// 一个表格的全部数据data
					Map<String, Object> data = new HashMap<>();
					String k = map.getKey();
					Path v = map.getValue();

					/**
					 * 模板数据定义
					 */
					String method = "";
					String summary = "";
					List<String> consumes = null;
					List<Parameters> paramList = null;
					List<Map<String, Response>> responses = null;
					Boolean hasTags = false;
					if (v.getGet() != null) {
						Get get = v.getGet();
						List<String> tagNames = get.getTags();
						if (tagNames.contains(tag.getName())) {
							method = REQUEST_METHOD_GET;
							summary = get.getSummary();
							consumes = get.getConsumes();
							paramList = get.getParameters();
							responses = get.getResponses();
							hasTags = true;
						}
					}
					if (v.getPost() != null) {
						Post post = v.getPost();
						List<String> tagNames = post.getTags();
						if (tagNames.contains(tag.getName())) {
							method = REQUEST_METHOD_POST;
							summary = post.getSummary();
							consumes = post.getConsumes();
							paramList = post.getParameters();
							responses = post.getResponses();
							hasTags = true;
						}
					}
					if (hasTags) {
						loadData(definitions, data, k, method, summary, consumes, paramList, responses);
						tagDataList.add(data);
					}
				}
			}
			tagData.put(tag.getDescription(), tagDataList);
			dataList.add(tagData);
		}
		GenerateDoc2(dataList);
		// HTTPUtils.close(connection);
	}

	public static void createDoc() throws Exception {
		List<Map<String, Object>> dataList = new ArrayList<>();
		File file = new File("swagger.json");
		InputStream inputStream = new FileInputStream(file);
		// HttpURLConnection connection =
		// HTTPUtils.getConnection("http://127.0.0.1:8081/v2/api-docs");
		// InputStream inputStream = connection.getInputStream();
		JsonRootBean root = JSON.parseObject(inputStream, JsonRootBean.class, Feature.DisableCircularReferenceDetect);
		List<Map<String, Path>> paths = root.getPaths();
		List<Map<String, Object>> definitions = root.getDefinitions();
		for (Map<String, Path> path : paths) {
			for (Map.Entry<String, Path> map : path.entrySet()) {
				// 一个表格的全部数据data
				Map<String, Object> data = new HashMap<>();
				String k = map.getKey();
				Path v = map.getValue();

				/**
				 * 模板数据定义
				 */
				String method = "";
				String summary = "";
				List<String> consumes = null;
				List<Parameters> paramList = null;
				List<Map<String, Response>> responses = null;
				if (v.getGet() != null) {
					Get get = v.getGet();
					method = REQUEST_METHOD_GET;
					summary = get.getSummary();
					consumes = get.getConsumes();
					paramList = get.getParameters();
					responses = get.getResponses();
				}
				if (v.getPost() != null) {
					Post post = v.getPost();
					method = REQUEST_METHOD_POST;
					summary = post.getSummary();
					consumes = post.getConsumes();
					paramList = post.getParameters();
					responses = post.getResponses();
				}
				loadData(definitions, data, k, method, summary, consumes, paramList, responses);
				dataList.add(data);
			}
		}
		GenerateDoc(dataList);
		// HTTPUtils.close(connection);
	}

	/**
	 * 装载模板数据
	 *
	 * @Title loadData
	 * @Description
	 * @param definitions
	 * @param container
	 * @param path
	 * @param method
	 * @param summary
	 * @param consumes
	 * @param paramList
	 * @param responses
	 *
	 */
	private static void loadData(List<Map<String, Object>> definitions, Map<String, Object> container, String path,
			String method, String summary, List<String> consumes, List<Parameters> paramList,
			List<Map<String, Response>> responses) {
		String refKey;
		container.put(URI, path);// 接口路径
		container.put(METHOD, method);// 请求方式
		container.put(SUMMARY, summary);// 模块名称
		container.put(CONSUMES, String.join(";", consumes));// Content-Type
		// 参数类型首字母大写
		paramList = paramList.stream().map(a -> {
			a.setType(upperCaseFirst(a.getType()));
			return a;
		}).collect(Collectors.toList());
		List<Parameters> headerList = paramList.stream().filter(a -> a.getIn().equals(PARAM_HEADER))
				.collect(Collectors.toList());
		List<Parameters> queryList = paramList.stream().filter(a -> a.getIn().equals(PARAM_QUERY))
				.collect(Collectors.toList());
		List<Parameters> pathList = paramList.stream().filter(a -> a.getIn().equals(PARAM_PATH))
				.collect(Collectors.toList());
		List<Parameters> bodyList = paramList.stream().filter(a -> a.getIn().equals(PARAM_BODY))
				.collect(Collectors.toList());
		container.put(HEADERLIST, headerList);// headerList
		container.put(QUERYLIST, queryList);// queryList
		container.put(PATHLIST, pathList);// queryList
		String pathParam = pathParam(pathList);
		container.put(PATHPARAM, pathParam);// pathParam
		String queryParam = queryParam(queryList);
		container.put(QUERYPARAM, queryParam);// queryParam

		List<Map<String, String>> bodyData = buildBodyList(definitions, bodyList);
		container.put(BODYLIST, bodyData);

		// 查找对应的返回结果
		refKey = getJsonRef(responses);
		List<Map<String, String>> resultList = buildResultList(definitions, refKey);
		container.put(RESULTLIST, resultList);
	}

	/**
	 * 生成body数据列表
	 *
	 * @Title buildBodyList
	 * @Description
	 * @param definitions
	 * @param bodyList
	 * @return
	 *
	 */
	private static List<Map<String, String>> buildBodyList(List<Map<String, Object>> definitions,
			List<Parameters> bodyList) {
		List<Map<String, String>> bodyData = new ArrayList<>();
		if (bodyList != null && bodyList.size() > 0) {
			Parameters bodyParam = bodyList.get(0);
			Map<String, String> schema = bodyParam.getSchema();
			// 获取JSON内部引用路径
			if (schema.containsKey(RESULT_DATA_TYPE)) {
				Map<String, String> map = new HashMap<>();
				map.put(RESULT_DATA_NAME, bodyParam.getName());
				map.put(RESULT_DATA_DESC, bodyParam.getDescription());
				map.put(RESULT_DATA_TYPE, schema.get(RESULT_DATA_TYPE));
				bodyData.add(map);
			} else {
				String ref = schema.get(JSON_REF_KEY);
				if (ref != null || ref != "") {
					ref = upperCaseFirst(ref.replaceAll("#/definitions/", "").replaceAll("<", "[").replaceAll(">", "]"));
					if (isJavaType(ref)) {
						Map<String, String> map = new HashMap<>();
						map.put(RESULT_DATA_NAME, bodyParam.getName());
						map.put(RESULT_DATA_DESC, bodyParam.getDescription());
						map.put(RESULT_DATA_TYPE, ref);
						map.put(RESULT_DATA_REMARK, "必填项");
						bodyData.add(map);
					}else {
						bodyData = buildBodyList(definitions, ref);
					}
				}
			}
		}
		return bodyData;
	}

	private static boolean isJavaType(String ref) {
		return ref.equals("String")||ref.equals("Integer")||ref.equals("Long");
	}

	/**
	 * 生成返回数据列表
	 *
	 * @Title buildResultList
	 * @Description
	 * @param definitions
	 * @param refKey
	 * @return
	 *
	 */
	private static List<Map<String, String>> buildBodyList(List<Map<String, Object>> definitions, String refKey) {
		List<Map<String, String>> resultList = new ArrayList<>();
		for (Map<String, Object> def : definitions) {
			Object object = def.get(refKey);
			if (object instanceof Map) {
				@SuppressWarnings("unchecked")
				Map<String, Object> dataMap = (Map<String, Object>) object;
				Object prop = dataMap.get(DEFINITIONS_PROP);
				if (prop instanceof JSONObject) {
					JSONObject jsonProp = (JSONObject) prop;
					Map<String, Map<String, Object>> propMap = jsonProp
							.toJavaObject(new TypeReference<Map<String, Map<String, Object>>>() {
							});
					for (Map.Entry<String, Map<String, Object>> entry : propMap.entrySet()) {
						String key = entry.getKey();
						Map<String, Object> value = entry.getValue();
						String propType = upperCaseFirst((String) value.get(DEFINITIONS_PROP_TYPE));
						String propDesc = upperCaseFirst((String) value.get(DEFINITIONS_PROP_DESC));
						
						Map<String, String> result = new HashMap<>();
						result.put(RESULT_DATA_NAME, key);
						result.put(RESULT_DATA_DESC, propDesc);
						result.put(RESULT_DATA_TYPE, propType);
						resultList.add(result);
					}
				}
			}
		}
		return resultList;
	}
	/**
	 * 生成返回数据列表
	 *
	 * @Title buildResultList
	 * @Description
	 * @param definitions
	 * @param refKey
	 * @return
	 *
	 */
	private static List<Map<String, String>> buildResultList(List<Map<String, Object>> definitions, String refKey) {
		List<Map<String, String>> resultList = new ArrayList<>();
		for (Map<String, Object> def : definitions) {
			Object object = def.get(refKey);
			if (object instanceof Map) {
				@SuppressWarnings("unchecked")
				Map<String, Object> dataMap = (Map<String, Object>) object;
				Object prop = dataMap.get(DEFINITIONS_PROP);
				if (prop instanceof JSONObject) {
					JSONObject jsonProp = (JSONObject) prop;
					Map<String, Map<String, Object>> propMap = jsonProp
							.toJavaObject(new TypeReference<Map<String, Map<String, Object>>>() {
							});
					for (Map.Entry<String, Map<String, Object>> entry : propMap.entrySet()) {
						String key = entry.getKey();
						Map<String, Object> value = entry.getValue();
						String propType = upperCaseFirst((String) value.get(DEFINITIONS_PROP_TYPE));
						String propDesc = upperCaseFirst((String) value.get(DEFINITIONS_PROP_DESC));

						Map<String, String> result = new HashMap<>();
						result.put(RESULT_DATA_NAME, key);
						result.put(RESULT_DATA_DESC, propDesc);
						result.put(RESULT_DATA_TYPE, propType);
						resultList.add(result);
					}
				}
			}
		}
		return resultList;
	}

	/**
	 * 获取JSON内部引用
	 *
	 * @Title getJsonRef
	 * @Description
	 * @param responses
	 * @return
	 *
	 */
	private static String getJsonRef(List<Map<String, Response>> responses) {
		for (Map<String, Response> res : responses) {
			for (Map.Entry<String, Response> entry : res.entrySet()) {
				if (SUCESS_CODE.equals(entry.getKey())) {
					Response rv = entry.getValue();
					Map<String, String> schema = rv.getSchema();
					// 获取JSON内部引用路径
					String ref = schema.get(JSON_REF_KEY);
					if (ref != null || ref != "") {
						ref = ref.replaceAll("#/definitions/Result«", "").replaceFirst("»", "");
						return ref;
					}
				}
			}
		}
		return null;
	}

	/**
	 * 拼接请求字符串
	 *
	 * @Title queryParam
	 * @Description
	 * @param list
	 * @return
	 *
	 */
	private static String queryParam(List<Parameters> list) {
		String queryParam = "?";
		for (Parameters p : list) {
			String name = p.getName();
			queryParam = queryParam + name + "={" + name + "}&";
		}
		if (queryParam.length() > 0) {
			queryParam = queryParam.substring(0, queryParam.length() - 1);
		}
		return queryParam;
	}

	/**
	 * 拼接路径字符串
	 *
	 * @Title pathParam
	 * @Description
	 * @param list
	 * @return
	 *
	 */
	private static String pathParam(List<Parameters> list) {
		String pathParam = "/";
		for (Parameters p : list) {
			String name = p.getName();
			pathParam = pathParam + "{" + name + "}/";
		}
		if (pathParam.length() > 0) {
			pathParam = pathParam.substring(0, pathParam.length() - 1);
		}
		return pathParam;
	}

	private static String upperCaseFirst(String str) {
		if (str == null || str == "") {
			return str;
		}
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	public static void GenerateDoc2(List<Map<String, List<Map<String, Object>>>> dataList)
			throws UnsupportedEncodingException {
		FtUtil ftUtil = new FtUtil();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(DATALIST, dataList);
		String defaultPath = Thread.currentThread().getContextClassLoader().getResource("").getPath()
				.replace("classes/", "");
		String dbFilePath = URLDecoder.decode(defaultPath, "utf-8");
		ftUtil.generateFile("/", "ApiTemplate2.xml", map, dbFilePath, CREATEFILE_NAME);
	}

	public static void GenerateDoc(List<Map<String, Object>> dataList) throws UnsupportedEncodingException {
		FtUtil ftUtil = new FtUtil();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(DATALIST, dataList);
		String defaultPath = Thread.currentThread().getContextClassLoader().getResource("").getPath()
				.replace("classes/", "");
		String dbFilePath = URLDecoder.decode(defaultPath, "utf-8");
		ftUtil.generateFile("/", TEMPLATE_NAME, map, dbFilePath, CREATEFILE_NAME);
	}
}
