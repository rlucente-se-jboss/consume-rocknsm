package com.redhat.camel;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractRockNSMProcessor implements Processor {
	protected static final ObjectMapper mapper = new ObjectMapper();
	protected static final AtomicLong uniqueId = new AtomicLong();

	protected void doProcess(Exchange exchange, String groupIdJsonPath, String timestampJsonPath, Class<?> factClazz)
			throws Exception {
		Message inMessage = exchange.getIn();
		String jsonString = inMessage.getBody(String.class);
		
		JsonNode objTree = mapper.readTree(jsonString);

		long id = uniqueId.incrementAndGet();
		String groupId = objTree.at(groupIdJsonPath).asText();
		Date timestamp = Date.from(Instant.parse(objTree.at(timestampJsonPath).asText()));

		inMessage.setHeader("JMSXGroupID", groupId);
		inMessage.setHeader("JMSXGroupSeq", Long.toString(id));

		Object factObject = factClazz.getConstructor(long.class, String.class, String.class, Date.class).newInstance(id,
				jsonString, groupId, timestamp);

		inMessage.setBody(factObject);
	}
}
