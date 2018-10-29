package org.activiti.cloud.services.query.deleteme;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.activiti.cloud.api.model.shared.events.CloudRuntimeEvent;
import org.activiti.cloud.api.process.model.events.CloudProcessCreatedEvent;
import org.activiti.cloud.services.query.app.repository.elastic.ProcessInstanceRepository;
import org.activiti.cloud.services.query.model.elastic.ProcessInstance;
import org.activiti.cloud.services.query.model.elastic.ValuePls;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = QueryRestTestApplication.class)
public class DeletemePlease {

	@Autowired
	private ProcessInstanceRepository processInstanceRepository;

	@Autowired
	private ElasticsearchTemplate esTemplate;

	@Autowired
	private Client esClient;

	public static void main(String[] args) throws Exception {
		String json = "{\r\n" + 
				"		\"eventType\": \"PROCESS_CREATED\",\r\n" + 
				"		\"id\": \"2d5d6f5f-8797-45a3-8993-b445862489a8\",\r\n" + 
				"		\"timestamp\": 1540843776199,\r\n" + 
				"		\"entity\": {\r\n" + 
				"			\"id\": \"8eda1da4-dbb6-11e8-a6cf-44032cd8bff7\",\r\n" + 
				"			\"processDefinitionId\": \"simpleProcess:1:24290c0c-ccca-11e8-ac41-44032cd8bff7\",\r\n" + 
				"			\"processDefinitionKey\": \"simpleProcess\",\r\n" + 
				"			\"initiator\": \"dino\",\r\n" + 
				"			\"startDate\": \"2018-10-29T20:09:36.199+0000\",\r\n" + 
				"			\"businessKey\": \"simpleProcessTest\",\r\n" + 
				"			\"status\": \"RUNNING\"\r\n" + 
				"		},\r\n" + 
				"		\"appName\": \"activiti-simple-process-cloud-app\",\r\n" + 
				"		\"serviceFullName\": \"simple-process-cloud-app\",\r\n" + 
				"		\"appVersion\": \"\",\r\n" + 
				"		\"serviceName\": \"simple-process-cloud-app\",\r\n" + 
				"		\"serviceVersion\": \"\",\r\n" + 
				"		\"serviceType\": \"runtime-bundle\",\r\n" + 
				"		\"entityId\": \"8eda1da4-dbb6-11e8-a6cf-44032cd8bff7\",\r\n" + 
				"		\"eventType\": \"PROCESS_CREATED\"\r\n" + 
				"	}";
		
		ObjectMapper m = new ObjectMapper();
		System.out.println(m.readValue(json, CloudRuntimeEvent.class));
	}

	
//	@Test
	public void test() {

		Assert.notNull(esTemplate);

		ValuePls var1 = new ValuePls();
		var1.setName("var1");
		var1.setType("string");
		var1.setValue("2018-01-01");
		var1.setProcessInstanceId("pi1");

		String typeLong = "long";
		String ValuePlsNameToUpdate = "var2";
		ValuePls var2 = new ValuePls();
		var2.setName(ValuePlsNameToUpdate);
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
//		pi.setValuePlss(valuesSet);

//		Map<String, ValuePls> map = valuesSet.stream().map(v -> )
//		Map<String, ValuePls> map = new HashMap<>();
//		for (ValuePls value : valuesSet) {
//			map.put(value.getName(), value);
//		}

		Map<String, List<ValuePls>> ValuePlssByTypeList = valuesSet.stream()
				.collect(Collectors.groupingBy(ValuePls::getType));

		Map<String, Set<ValuePls>> ValuePlssByType = new HashMap<>();

		ValuePlssByTypeList.forEach((type, ValuePlss) -> {
			ValuePlssByType.put(type, new HashSet<>(ValuePlss));
		});

//		pi.setVariables(ValuePlssByType);

//		Map<String, Map<String, ValuePls>> finalMap = new HashMap<>();
//		ValuePlssByType.forEach((type, ValuePlss) -> {
//			Map<String, ValuePls> ValuePlssByName = new HashMap<>();
//			ValuePlss.forEach(ValuePls -> {
//				ValuePlssByName.put(ValuePls.getName(), ValuePls);
//			});
//			finalMap.put(type, ValuePlssByName);
//		});
//		Map<String, ValuePls> map = valuesSet.stream().collect(Collectors.toMap(v -> v.getName(), v -> v));
//		pi.setValuePlss(finalMap);
//		ValuePls myVar = new ValuePls();
//		myVar.setValuePlss(valuesSet);

//		ValuePlsRepository.save(myVar);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_NULL);

		processInstanceRepository.save(pi);

		ProcessInstance piRetrieved = processInstanceRepository.findById(pi.getId()).get();
		try {
			System.out.println(objectMapper.writeValueAsString(piRetrieved));
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}
//		piRetrieved.getVariables().forEach((k, ValuePlss) -> {
//			if (typeLong.equals(k)) {
//				ValuePlss.forEach(ValuePls -> {
//					if (ValuePlsNameToUpdate.equals(ValuePls.getName())) {
//						ValuePls.setValue(777L);
//						System.out.println("UPDATED :)");
//					}
//				});
//			}
//		});

		try {
			System.out.println(objectMapper.writeValueAsString(ValuePlssByType));
			System.out.println(objectMapper.writeValueAsString(pi));
			System.out.println(objectMapper.writeValueAsString(piRetrieved));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		IndexRequest indexRequest = new IndexRequest();
//		indexRequest.source("appName", "Ma name is ma name");
		// It does not support it...use script...

//		indexRequest.source("ValuePlss", ValuePlssByTypeList);
//		try {
//			indexRequest.source("ValuePlss", objectMapper.writeValueAsString(piRetrieved.getValuePlss()));
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
//		newObject.put("ValuePlss", ValuePlssByType);
//		updateRequest.doc(newObject);

		ObjectNode jo = objectMapper.createObjectNode();
//		jo.set("ValuePlss", objectMapper.valueToTree(piRetrieved.getVariables()));

//		ObjectNode jo = objectMapper.valueToTree(ValuePlssByType);
//		newObject.put("ValuePlss2", jo);

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
