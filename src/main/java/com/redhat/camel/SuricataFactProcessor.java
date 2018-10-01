package com.redhat.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Message;

import it.redhat.hacep.playground.rules.model.SuricataFact;

public class SuricataFactProcessor extends AbstractRockNSMProcessor {
	public void process(Exchange exchange) throws Exception {
		Message inMessage = exchange.getIn();
		SuricataFact suricataFact = inMessage.getBody(SuricataFact.class);

		suricataFact.setId(uniqueId.incrementAndGet());

		if (suricataFact.get_at_timestamp() == null) {
			suricataFact.set_at_timestamp(new Date(System.currentTimeMillis()));
		}
	}
}
