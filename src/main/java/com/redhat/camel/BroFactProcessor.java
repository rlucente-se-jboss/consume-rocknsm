package com.redhat.camel;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.redhat.hacep.playground.rules.model.BroFact;

public class BroFactProcessor implements Processor {
	protected static final ObjectMapper mapper = new ObjectMapper();
	protected static final AtomicLong uniqueId = new AtomicLong();

	public void process(Exchange exchange) throws Exception {
		Message inMessage = exchange.getIn();
		String jsonString = inMessage.getBody(String.class);

		BroFact broFact = mapper.readValue(jsonString, BroFact.class);

		long id = uniqueId.incrementAndGet();
		broFact.setId(id);

		inMessage.setHeader("JMSXGroupID", broFact.get_at_system());
		inMessage.setHeader("JMSXGroupSeq", Long.toString(id));
		inMessage.setBody(broFact);
	}
}
