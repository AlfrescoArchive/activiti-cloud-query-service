package org.activiti.cloud.services.query.deleteme;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.activiti.cloud.services.query.QueryRestTestApplication;
import org.activiti.cloud.services.query.app.repository.elastic.ProcessInstanceRepository;
import org.activiti.cloud.services.query.app.repository.elastic.VariableRepository;
import org.activiti.cloud.services.query.model.elastic.ProcessInstance;
import org.activiti.cloud.services.query.model.elastic.ValuePls;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QueryRestTestApplication.class)
public class DeletemeTest {

	@Autowired
	private VariableRepository variableRepository;

	@Autowired
	private ProcessInstanceRepository processInstanceRepository;

	@Autowired
	private ElasticsearchTemplate esTemplate;

	@Autowired
	private Client esClient;

	@Test
	public void test() {
		Assert.notNull(variableRepository);
		Assert.notNull(esTemplate);

		ValuePls var1 = new ValuePls();
		var1.setName("var1");
		var1.setType("string");
		var1.setValue("2018-01-01");
		var1.setProcessInstanceId("pi1");

		String typeLong = "long";
		String variableNameToUpdate = "var2";
		ValuePls var2 = new ValuePls();
		var2.setName(variableNameToUpdate);
		var2.setType(typeLong);
		var2.setValue(123l);
		var2.setProcessInstanceId("pi1");

		ValuePls var3 = new ValuePls();
		var3.setName("var3");
		var3.setType(typeLong);
		var3.setValue(123456l);
		var3.setProcessInstanceId("pi1");

		ProcessInstance pi = new ProcessInstance();
		pi.setId("processInstance_" + System.currentTimeMillis());

		Set<ValuePls> valuesSet = new HashSet<>(Arrays.asList(var1, var2, var3));
//		pi.setVariables(valuesSet);

//		Map<String, Variable> map = valuesSet.stream().map(v -> )
//		Map<String, ValuePls> map = new HashMap<>();
//		for (ValuePls value : valuesSet) {
//			map.put(value.getName(), value);
//		}

		Map<String, List<ValuePls>> variablesByTypeList = valuesSet.stream()
				.collect(Collectors.groupingBy(ValuePls::getType));

		Map<String, Set<ValuePls>> variablesByType = new HashMap<>();

		variablesByTypeList.forEach((type, variables) -> {
			variablesByType.put(type, new HashSet<>(variables));
		});

		pi.setVariables(variablesByType);

//		Map<String, Map<String, ValuePls>> finalMap = new HashMap<>();
//		variablesByType.forEach((type, variables) -> {
//			Map<String, ValuePls> variablesByName = new HashMap<>();
//			variables.forEach(variable -> {
//				variablesByName.put(variable.getName(), variable);
//			});
//			finalMap.put(type, variablesByName);
//		});
//		Map<String, ValuePls> map = valuesSet.stream().collect(Collectors.toMap(v -> v.getName(), v -> v));
//		pi.setVariables(finalMap);
//		Variable myVar = new Variable();
//		myVar.setVariables(valuesSet);

//		variableRepository.save(myVar);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_NULL);

		processInstanceRepository.save(pi);

		ProcessInstance piRetrieved = processInstanceRepository.findById(pi.getId()).get();
		try {
			System.out.println(objectMapper.writeValueAsString(piRetrieved));
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}
		piRetrieved.getVariables().forEach((k, variables) -> {
			if (typeLong.equals(k)) {
				variables.forEach(variable -> {
					if (variableNameToUpdate.equals(variable.getName())) {
						variable.setValue(777L);
						System.out.println("UPDATED :)");
					}
				});
			}
		});

		try {
			System.out.println(objectMapper.writeValueAsString(variablesByType));
			System.out.println(objectMapper.writeValueAsString(pi));
			System.out.println(objectMapper.writeValueAsString(piRetrieved));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		IndexRequest indexRequest = new IndexRequest();
//		indexRequest.source("appName", "Ma name is ma name");
		// It does not support it...use script...

//		indexRequest.source("variables", variablesByTypeList);
//		try {
//			indexRequest.source("variables", objectMapper.writeValueAsString(piRetrieved.getVariables()));
//		} catch (JsonProcessingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

//		 Map<String, Object> newObject = new HashMap();
//		    newObject.put("appName", "NEW NAME PLS");
////		    newObject.put("value", "test3");
//		    esClient.prepareUpdate("test", "doc", "1").
//		            .addScriptParam("newobject", newObject)
//		            .setScript("ctx._source.types += newobject")
//		            .execute()
//		            .actionGet();

		UpdateRequest updateRequest = new UpdateRequest();
		updateRequest.index("process_instance");
		updateRequest.type("_doc");
		updateRequest.id(piRetrieved.getId());

//		Map<String, Object> newObject = new HashMap<>();
//		newObject.put("variables", variablesByType);
//		updateRequest.doc(newObject);

		ObjectNode jo = objectMapper.createObjectNode();
		jo.set("variables", objectMapper.valueToTree(piRetrieved.getVariables()));

//		ObjectNode jo = objectMapper.valueToTree(variablesByType);
//		newObject.put("variables2", jo);

		System.out.println("TO UPDATE: " + jo.toString());
		updateRequest.doc(jo.toString(), XContentType.JSON);
//		updateRequest.doc(jsonBuilder()
//		        .startObject()
//		            .field("gender", "male")
//		        .endObject());
		try {
			esClient.update(updateRequest).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		UpdateQuery updateQuery = new UpdateQueryBuilder().withId(piRetrieved.getId()).withClass(ProcessInstance.class)
//				.withIndexRequest(indexRequest).build();
//		esTemplate.update(updateQuery);

//		processInstanceRepository.save(piRetrieved);
		try {
			System.out.println("VEA: "
					+ objectMapper.writeValueAsString(processInstanceRepository.findById(piRetrieved.getId()).get()));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
