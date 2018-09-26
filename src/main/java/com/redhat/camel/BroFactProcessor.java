package com.redhat.camel;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.redhat.hacep.playground.rules.model.BroFact;

public class BroFactProcessor implements Processor {
	private static final ObjectMapper mapper = new ObjectMapper();
	private static final AtomicLong uniqueId = new AtomicLong();

	public void process(Exchange exchange) throws Exception {
		Message inMessage = exchange.getIn();
		String jsonString = inMessage.getBody(String.class);
		JsonNode objTree = mapper.readTree(jsonString);

		long id = uniqueId.incrementAndGet();
		String system = objTree.at("/@system").asText();
		Date timestamp = Date.from(Instant.parse(objTree.at("/ts").asText()));

		inMessage.setHeader("JMSXGroupID", system);
		inMessage.setHeader("JMSXGroupSeq", id);
		inMessage.setBody(new BroFact(id, jsonString, system, timestamp));
	}
}
