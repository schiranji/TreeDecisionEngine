package com.techsavy.de.processor.sample3.processor;

import com.techsavy.de.processor.AbstractXlsProcessor;

public class XlsProcessor extends AbstractXlsProcessor {
	private static final String PROCESSOR_VERSION = "1.0.0";
	@Override
	public String getRulesFileName()  {
		return "Rules.xlsx";
	}

	@Override
	protected String getProcessorVersion() {
		return PROCESSOR_VERSION;
	}
}
