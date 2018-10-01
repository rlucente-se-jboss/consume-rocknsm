package com.redhat.camel;

import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.camel.Message;

import it.redhat.hacep.playground.rules.model.BroFact;

public class BroFactProcessor extends AbstractRockNSMProcessor {
	public void process(Exchange exchange) throws Exception {
		Message inMessage = exchange.getIn();
		BroFact broFact = inMessage.getBody(BroFact.class);

		broFact.setId(uniqueId.incrementAndGet());

                if (broFact.getTs() == null) {
                	broFact.setTs(new Date(System.currentTimeMillis()));
		}
	}
}
