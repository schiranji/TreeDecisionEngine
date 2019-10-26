package com.techsavy.de.processor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import com.techsavy.de.common.Constants;
import com.techsavy.de.domain.DecisionEngineRequest;
import com.techsavy.de.domain.ProcessorResponse;

public interface ProcessorInt extends Callable<List<ProcessorResponse>>, Constants {
	List<ProcessorResponse> processRules();
	void setProcessorData(DecisionEngineRequest decisionEngineRequest, ProcessorInt processor, ProcessorResponse clone,
			Map<String, Object> childProcessors, int depth);
	void setDecisionEngineRequest(DecisionEngineRequest decisionEngineRequest);
	void setProcessorResponse(ProcessorResponse argProcessorResponse);
	void setChildProcessorMap(Map<String, Object> argProcessorMap);
	void setDepth(int depth);
	Map<String, Object> getChildProcessorMap();
}
