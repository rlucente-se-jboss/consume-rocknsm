package com.redhat.camel;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.camel.Processor;

public abstract class AbstractRockNSMProcessor implements Processor {
	protected static final AtomicLong uniqueId = new AtomicLong();
}
