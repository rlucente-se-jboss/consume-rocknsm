package com.redhat.camel;

import org.apache.camel.Exchange;
import it.redhat.hacep.playground.rules.model.BroFact;

public class BroFactProcessor extends AbstractRockNSMProcessor {

	public void process(Exchange exchange) throws Exception {
		doProcess(exchange, "/@system", "/ts", BroFact.class);
	}
}
