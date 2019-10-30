package com.techsavy.de.processor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import com.techsavy.de.domain.PostrequisiteResponse;
import com.techsavy.de.domain.PrerequisiteResponse;
import com.techsavy.de.domain.RuleResponse;
import com.techsavy.de.domain.XlsRuleData;

public abstract class AbstractXlsProcessor extends AbstractProcessor {
	private static final Logger log = LogManager.getLogger();

	private static List<XlsRuleData> rulesList = new ArrayList<>();
	private static List<XlsRuleData> actionsList = new ArrayList<>();
	private static int conditionsCount = 0;
	
	protected void readRulesFile() {
		if (rulesList != null && rulesList.size() > 0) {
			return;
		}
		String xlsFileName = getRulesFileName();
		log.info("Loading rules from file in classpath {}", xlsFileName);
		ExpressionParser parser = new SpelExpressionParser();
		try (InputStream inputStream = AbstractXlsProcessor.class.getClassLoader().getResourceAsStream(xlsFileName);
				XSSFWorkbook myWorkBook = new XSSFWorkbook(inputStream);) {
			XSSFSheet mySheet = myWorkBook.getSheetAt(0);
			Iterator<Row> rowIterator = mySheet.iterator(); // Traversing over each row of XLSX file
			conditionsCount = getConditionCount(rowIterator.next());
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next(); // For each row, iterate through each columns
				Iterator<Cell> cellIterator = row.cellIterator();
				XlsRuleData ruleData = new XlsRuleData();
				ruleData.setRuleName(cellIterator.next().getStringCellValue());
				//ruleData.setDecision(cellIterator.next().getStringCellValue());
				for (int i=0; cellIterator.hasNext(); i++) {
					Cell cell = cellIterator.next();
					Expression expression = parser.parseExpression(cell.getStringCellValue());
					if(i < conditionsCount) {
						ruleData.getConditions().add(expression);
						rulesList.add(ruleData);
					} else {
						ruleData.getActions().add(expression);
						actionsList.add(ruleData);						
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.debug("readRulesFile complete.");
	}

	private int getConditionCount(Row row) {
		Iterator<Cell> cellIterator = row.cellIterator();
		int conditionIndex = 0;
		while (cellIterator.hasNext()) {
			Cell cell = cellIterator.next();
			if(conditionIndex == 0) { //Blank cell
				conditionIndex++;
				continue;
			}
			if("Condition".equalsIgnoreCase(cell.getStringCellValue())) {
				conditionIndex++;
				continue;
			}
		}
		return conditionIndex;
	}

	public abstract String getRulesFileName();

	@Override
	protected void buildPrerequistes() {
		log.debug("Processing XlsProcecssor:buildPrerequistes");
		readRulesFile();
		prerequisites.add((decisionEngineRequest) -> {
			PrerequisiteResponse prerequisiteResponse = PrerequisiteResponse.getInstance("XlsProcessor:Prerequiste1");
			prerequisiteResponse.setPassed(true);
			return prerequisiteResponse;
		});
	}

	@Override
	protected void buildRules() {
		rulesList.forEach(rule -> {
			rules.add((decisionEngineRequest, processorResponse) -> {
				RuleResponse ruleResponse = RuleResponse.getInstance(rule.getRuleName());
				rule.getConditions().forEach(expression -> {
					log.debug("Processing XlsProcessor:" + rule.getRuleName() + ":" + expression.getExpressionString());
					Boolean conditionResult = expression.getValue(decisionEngineRequest, Boolean.class);
					processorResponse.getDecisionArrivalSteps().put(rule.getRuleName()+"."+expression.getExpressionString(),
							conditionResult.toString());
					if(!conditionResult) { ruleResponse.setRuleResult(conditionResult); }
					processorResponse.setScore(processorResponse.getScore()+1);
				});
				return ruleResponse;
			});
		});
		log.info("buildRules complete");
	}
	
	@Override
	protected void buildActions() {
		actionsList.forEach(action -> {
			postActions.add((decisionEngineRequest, processorResponse) -> {
				PostrequisiteResponse postrequisiteResponse = PostrequisiteResponse.getInstance(action.getRuleName());
				action.getActions().forEach(expression -> {
					Object actionResult = expression.getValue(processorResponse);
					processorResponse.getDecisionArrivalSteps().put(action.getRuleName()+"."+expression.getExpressionString(),
							actionResult.toString());
				});
				return postrequisiteResponse;
			});			
		});
		log.info("buildActions complete");
	}
}
