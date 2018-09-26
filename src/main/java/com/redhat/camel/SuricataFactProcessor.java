package com.redhat.camel;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.redhat.hacep.playground.rules.model.SuricataFact;

public class SuricataFactProcessor implements Processor {
	private static final ObjectMapper mapper = new ObjectMapper();
	private static final AtomicLong uniqueId = new AtomicLong();

	public void process(Exchange exchange) throws Exception {
		Message inMessage = exchange.getIn();
		String jsonString = inMessage.getBody(String.class);
		JsonNode objTree = mapper.readTree(jsonString);

		long id = uniqueId.incrementAndGet();
		String hostname = objTree.at("/host/name").asText();
		Date timestamp = Date.from(Instant.parse(objTree.at("/@timestamp").asText()));

		inMessage.setHeader("JMSXGroupID", hostname);
		inMessage.setHeader("JMSXGroupSeq", id);
		inMessage.setBody(new SuricataFact(id, jsonString, hostname, timestamp));
	}
}
