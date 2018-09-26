package com.redhat.camel;

import org.apache.camel.Exchange;
import it.redhat.hacep.playground.rules.model.SuricataFact;

public class SuricataFactProcessor extends AbstractRockNSMProcessor {

	public void process(Exchange exchange) throws Exception {
		doProcess(exchange, "/host/name", "/@timestamp", SuricataFact.class);
	}
}
